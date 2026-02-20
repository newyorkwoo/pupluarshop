package com.popularshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popularshop.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String name;
    private String phone;
    private String city;
    private String district;
    private String zipCode;
    private String addressLine;
    @JsonProperty("isDefault")
    private boolean isDefault;

    public static AddressResponse fromEntity(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .phone(address.getPhone())
                .city(address.getCity())
                .district(address.getDistrict())
                .zipCode(address.getZipCode())
                .addressLine(address.getAddressLine())
                .isDefault(address.isDefault())
                .build();
    }
}
