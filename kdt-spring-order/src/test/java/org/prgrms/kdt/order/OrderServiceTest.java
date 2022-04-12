package org.prgrms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.MemoryVoucherRepository;
import org.prgrms.kdt.voucher.VoucherService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    class OrderRepositoryStub implements  OrderRepository{//OrderRepo에 대한 스텁 (만약 없다면)
        @Override
        public Order insert(Order order) {
            return order;
        }
    }
    @Test
    @DisplayName("오더가 생성되어야 한다 (stub)")
    void createOrder() {
        // Given
        var voucherRepository = new MemoryVoucherRepository();//바우처 레포 생성
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);//픽스 바우처 만듦
        //오더 레포는 스텁으로 만듦(없다는 가정)
        var sut = new OrderService(new VoucherService(voucherRepository), new OrderRepositoryStub());//

        // When
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());

        // Then//order에 getter
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    @DisplayName("오더가 생성되어야 한다. (mock)")
    void createOderByMock(){
        //Given
        var voucherService = mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        //Mock 은 이 when으로 호출하는 부분에서만 사용이 되는 것이다.
        when(voucherService.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);
        var sut = new OrderService(voucherService, orderRepositoryMock); //목객체가 만들어져서 전달이 되었다
        //행위에 집중이 돼
        // 야한다.

        //When
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());

        //Then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        //행위 관점에서 메서드가 호출이 됐는지를 확인
        //특정한 순서에 의해서 호출되어야 한다.
        var inOrder = inOrder(voucherService);
        inOrder.verify(voucherService).getVoucher(fixedAmountVoucher.getVoucherId());
        verify(orderRepositoryMock).insert(order);//실제로 호출이 돼서 들어갔는지
        inOrder.verify(voucherService).useVoucher(fixedAmountVoucher);//사용했는지

    }
}