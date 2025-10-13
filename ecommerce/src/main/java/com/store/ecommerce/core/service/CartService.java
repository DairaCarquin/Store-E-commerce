package com.store.ecommerce.core.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.store.ecommerce.infrastructure.persistence.entity.Cart;
import com.store.ecommerce.infrastructure.persistence.entity.CartItem;
import com.store.ecommerce.infrastructure.persistence.entity.Coupon;
import com.store.ecommerce.infrastructure.persistence.entity.Product;
import com.store.ecommerce.infrastructure.persistence.entity.User;
import com.store.ecommerce.infrastructure.repository.CartRepository;
import com.store.ecommerce.infrastructure.repository.CouponRepository;
import com.store.ecommerce.infrastructure.repository.ProductRepository;
import com.store.ecommerce.infrastructure.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final CouponRepository couponRepo;
    private final UserRepository userRepo;

    public Cart getOrCreate(Long userId) {
        return cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepo.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepo.save(c);
                });
    }

    public Cart addItem(Long userId, Long productId, int qty) {
        Cart cart = getOrCreate(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < qty)
            throw new RuntimeException("Stock insuficiente");

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + qty);
        } else {
            CartItem ci = new CartItem();
            ci.setCart(cart);
            ci.setProduct(product);
            ci.setQuantity(qty);
            ci.setPriceSnapshot(product.getPrice());
            cart.getItems().add(ci);
        }

        cart.recalcTotals();
        return cartRepo.save(cart);
    }

    public Cart updateItem(Long userId, Long productId, int qty) {
        Cart cart = getOrCreate(userId);
        for (CartItem ci : cart.getItems()) {
            if (ci.getProduct().getId().equals(productId)) {
                if (qty <= 0) {
                    cart.getItems().remove(ci);
                } else {
                    ci.setQuantity(qty);
                }
                break;
            }
        }
        cart.recalcTotals();
        return cartRepo.save(cart);
    }

    public Cart clear(Long userId) {
        Cart cart = getOrCreate(userId);
        cart.getItems().clear();
        cart.recalcTotals();
        return cartRepo.save(cart);
    }

    public Cart applyCoupon(Long userId, String code) {
        Cart cart = getOrCreate(userId);
        Coupon coupon = couponRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        if (!coupon.isValidNow())
            throw new RuntimeException("Cupón expirado");

        if (coupon.getPercentOff() != null)
            cart.setDiscount(cart.getSubtotal()
                    .multiply(coupon.getPercentOff().divide(java.math.BigDecimal.valueOf(100))));
        else if (coupon.getAmountOff() != null)
            cart.setDiscount(coupon.getAmountOff());

        cart.recalcTotals();
        return cartRepo.save(cart);
    }
}