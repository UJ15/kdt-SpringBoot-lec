package org.prgrms.kdt.order;

import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service//스테레오타ㅒ
public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems){
        Order order =  new Order(UUID.randomUUID(), customerId, orderItems);
        return orderRepository.insert(order);
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId){
        Voucher voucher = voucherService.getVoucher(voucherId);
        Order order =  new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        voucherService.useVoucher(voucher);
        return orderRepository.insert(order);
    }
}