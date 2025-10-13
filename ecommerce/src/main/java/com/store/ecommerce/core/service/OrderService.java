package com.store.ecommerce.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.store.ecommerce.infrastructure.persistence.entity.Address;
import com.store.ecommerce.infrastructure.persistence.entity.Cart;
import com.store.ecommerce.infrastructure.persistence.entity.CartItem;
import com.store.ecommerce.infrastructure.persistence.entity.Order;
import com.store.ecommerce.infrastructure.persistence.entity.OrderItem;
import com.store.ecommerce.infrastructure.persistence.entity.Shipping;
import com.store.ecommerce.infrastructure.persistence.enums.OrderStatus;
import com.store.ecommerce.infrastructure.persistence.enums.PaymentMethod;
import com.store.ecommerce.infrastructure.repository.AddressRepository;
import com.store.ecommerce.infrastructure.repository.CartRepository;
import com.store.ecommerce.infrastructure.repository.OrderRepository;
import com.store.ecommerce.infrastructure.repository.ProductRepository;
import com.store.ecommerce.infrastructure.repository.ShippingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final ShippingRepository shippingRepo;
    private final AddressRepository addressRepo;

    public Order createFromCart(Long userId, Long addressId, PaymentMethod method) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (cart.getItems().isEmpty())
            throw new RuntimeException("Carrito vacío");

        Address addr = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Dirección inválida"));

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setShippingAddress(addr);
        order.setPaymentMethod(method);
        order.setSubtotal(cart.getSubtotal());
        order.setDiscount(cart.getDiscount());
        order.setTotal(cart.getTotal());

        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cart.getItems()) {
            if (ci.getProduct().getStock() < ci.getQuantity())
                throw new RuntimeException("Stock insuficiente: " + ci.getProduct().getName());
            ci.getProduct().setStock(ci.getProduct().getStock() - ci.getQuantity());
            productRepo.save(ci.getProduct());

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setProductName(ci.getProduct().getName());
            oi.setUnitPrice(ci.getPriceSnapshot());
            oi.setQuantity(ci.getQuantity());
            items.add(oi);
        }

        order.setItems(items);
        orderRepo.save(order);

        Shipping s = new Shipping();
        s.setOrder(order);
        s.setProvider("Olva Courier");
        shippingRepo.save(s);

        cart.getItems().clear();
        cart.recalcTotals();
        cartRepo.save(cart);

        return order;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId, Pageable.unpaged()).getContent();
    }

    public Order updateStatus(Long orderId, OrderStatus status) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        o.setStatus(status);
        return orderRepo.save(o);
    }
}