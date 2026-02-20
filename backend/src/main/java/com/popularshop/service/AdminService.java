package com.popularshop.service;

import com.popularshop.dto.DashboardResponse;
import com.popularshop.entity.Order;
import com.popularshop.repository.OrderRepository;
import com.popularshop.repository.ProductRepository;
import com.popularshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardResponse getDashboard() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();

        return DashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalProducts(productRepository.countByActiveTrue())
                .totalOrders(orderRepository.count())
                .totalRevenue(orderRepository.getTotalRevenue())
                .pendingOrders(orderRepository.countByStatus(Order.OrderStatus.PENDING))
                .todayOrders(orderRepository.countByCreatedAtAfter(todayStart))
                .todayRevenue(orderRepository.getRevenueSince(todayStart))
                .build();
    }
}
