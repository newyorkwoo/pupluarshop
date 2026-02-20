package com.popularshop.service;

import com.popularshop.dto.*;
import com.popularshop.entity.Address;
import com.popularshop.entity.User;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.AddressRepository;
import com.popularshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (request.getName() != null)
            user.setName(request.getName());
        if (request.getPhone() != null)
            user.setPhone(request.getPhone());

        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("目前密碼不正確");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // Address management
    public List<AddressResponse> getAddresses(Long userId) {
        return addressRepository.findByUserIdOrderByIsDefaultDesc(userId).stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = Address.builder()
                .user(user)
                .name(request.getName())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .zipCode(request.getZipCode())
                .addressLine(request.getAddressLine())
                .isDefault(request.isDefault())
                .build();

        if (request.isDefault()) {
            clearDefaultAddresses(userId);
        }

        address = addressRepository.save(address);
        return AddressResponse.fromEntity(address);
    }

    @Transactional
    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setZipCode(request.getZipCode());
        address.setAddressLine(request.getAddressLine());

        if (request.isDefault()) {
            clearDefaultAddresses(userId);
            address.setDefault(true);
        }

        address = addressRepository.save(address);
        return AddressResponse.fromEntity(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        addressRepository.delete(address);
    }

    private void clearDefaultAddresses(Long userId) {
        addressRepository.findByUserIdOrderByIsDefaultDesc(userId).forEach(a -> {
            if (a.isDefault()) {
                a.setDefault(false);
                addressRepository.save(a);
            }
        });
    }

    // Admin
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        var users = userRepository.findAll(pageable);
        List<UserResponse> content = users.getContent().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
        return PageResponse.of(content, users.getNumber(), users.getSize(),
                users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    @Transactional
    public void toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}
