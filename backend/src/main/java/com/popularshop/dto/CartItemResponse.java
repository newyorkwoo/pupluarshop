package com.popularshop.dto;

import com.popularshop.entity.CartItem;
import com.popularshop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private String brand;
    private String size;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static CartItemResponse fromEntity(CartItem item) {
        Product product = item.getProduct();
        if (product == null) {
            return CartItemResponse.builder()
                    .id(item.getId())
                    .size(item.getSize())
                    .quantity(item.getQuantity())
                    .price(BigDecimal.ZERO)
                    .subtotal(BigDecimal.ZERO)
                    .build();
        }
        BigDecimal itemPrice = product.getSalePrice() != null
                && product.getSalePrice().compareTo(BigDecimal.ZERO) > 0
                        ? product.getSalePrice()
                        : product.getPrice();
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImages() != null && !product.getImages().isEmpty()
                        ? product.getImages().get(0).getImageUrl()
                        : null)
                .brand(product.getBrand())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .price(itemPrice)
                .subtotal(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
