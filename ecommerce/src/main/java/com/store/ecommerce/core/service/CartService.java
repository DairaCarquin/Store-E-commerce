package com.store.ecommerce.core.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import com.store.ecommerce.infrastructure.persistence.entity.Cart;
import com.store.ecommerce.infrastructure.persistence.entity.CartItem;
import com.store.ecommerce.infrastructure.persistence.entity.Coupon;
import com.store.ecommerce.infrastructure.persistence.entity.Product;
import com.store.ecommerce.infrastructure.persistence.entity.User;
import com.store.ecommerce.infrastructure.repository.CartItemRepository;
import com.store.ecommerce.infrastructure.repository.CartRepository;
import com.store.ecommerce.infrastructure.repository.CouponRepository;
import com.store.ecommerce.infrastructure.repository.ProductRepository;
import com.store.ecommerce.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final CouponRepository couponRepo;
    private final UserRepository userRepo;
    private final CartItemRepository cartItemRepository;

    private final ConcurrentHashMap<Long, Object> userLocks = new ConcurrentHashMap<>();

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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart addItem(Long userId, Long productId, int qty, String name, double price,
            String description, String imageBase64) {

        synchronized (userLocks.computeIfAbsent(userId, k -> new Object())) {
            for (int i = 0; i < 3; i++) {
                try {
                    return doAddItem(userId, productId, qty, name, price, description, imageBase64);
                } catch (CannotAcquireLockException e) {
                    if (i == 2)
                        throw e;
                    try {
                        Thread.sleep(100 + (int) (Math.random() * 200));
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            throw new RuntimeException("No se pudo agregar el producto al carrito después de varios intentos.");
        }
    }

    private Cart doAddItem(Long userId, Long productId, int qty, String name, double price,
            String description, String imageBase64) {

        Cart cart = getOrCreate(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < qty)
            throw new RuntimeException("Stock insuficiente");

        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existing.isPresent()) {
            CartItem ci = existing.get();
            ci.setQuantity(ci.getQuantity() + qty);
            cartItemRepository.save(ci);
        } else {
            CartItem ci = new CartItem();
            ci.setCart(cart);
            ci.setProduct(product);
            ci.setQuantity(qty);
            ci.setPriceSnapshot(BigDecimal.valueOf(price));
            ci.setName(name);
            ci.setDescription(description);
            ci.setImageBase64(imageBase64);
            cartItemRepository.save(ci);
            cart.getItems().add(ci);
        }

        cart.recalcTotals();
        return cartRepo.save(cart);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart updateItem(Long userId, Long productId, int qty) {
        synchronized (userLocks.computeIfAbsent(userId, k -> new Object())) {
            Cart cart = getOrCreate(userId);
            CartItem target = cart.getItems().stream()
                    .filter(ci -> ci.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

            if (qty <= 0) {
                cart.getItems().remove(target);
            } else {
                target.setQuantity(qty);
            }

            cart.recalcTotals();
            return cartRepo.save(cart);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart clear(Long userId) {
        synchronized (userLocks.computeIfAbsent(userId, k -> new Object())) {
            Cart cart = getOrCreate(userId);
            cart.getItems().clear();
            cart.recalcTotals();
            return cartRepo.save(cart);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart applyCoupon(Long userId, String code) {
        synchronized (userLocks.computeIfAbsent(userId, k -> new Object())) {
            Cart cart = getOrCreate(userId);
            Coupon coupon = couponRepo.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
            if (!coupon.isValidNow())
                throw new RuntimeException("Cupón expirado");

            if (coupon.getPercentOff() != null)
                cart.setDiscount(cart.getSubtotal()
                        .multiply(coupon.getPercentOff().divide(BigDecimal.valueOf(100))));
            else if (coupon.getAmountOff() != null)
                cart.setDiscount(coupon.getAmountOff());

            cart.recalcTotals();
            return cartRepo.save(cart);
        }
    }
}
