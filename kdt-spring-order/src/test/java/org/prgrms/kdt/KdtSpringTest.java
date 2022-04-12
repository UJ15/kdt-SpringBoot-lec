package org.prgrms.kdt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.order.OrderStatus;
import org.prgrms.kdt.voucher.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

//이건 통합테스트다.
@ExtendWith(SpringExtension.class)//실질적으로 만들어준다.
@ContextConfiguration//어떤 식으로 앱컨피스래이션이 만들어져야되는지 알려주고
//위 두개의 어노테이션을 둘다 사용할 수 있는
@SpringJUnitConfig //얘만 있어도 코드가 잘 실행된다.
public class KdtSpringTest {

    @Configuration//기본적으로 찾게됨단 ContextConfiguration에 별도로 설정을 안줘도
    @ComponentScan(// #2 오더 서비스 테스트
            basePackages = {"org.prgrms.kdt.voucher", "org.prgrms.kdt.order"}
    )
    static class Config{
//        @Bean   //1번째 : @Configuration만 있었음 그리고 이제 order서비스 테스트 하기위해 주석처리
//        VoucherRepository voucherRepository(){
//            return new VoucherRepository() {
//                @Override
//                public Optional<Voucher> findById(UUID voucherId) {
//                    return Optional.empty();
//                }
//
//                @Override
//                public Voucher insert(Voucher voucher) {
//                    return null;
//                }
//            };
//        }
    }
    @Autowired //#1
    ApplicationContext context;

    @Autowired //#2
    OrderService orderService;

    @Autowired //#2
    VoucherRepository voucherRepository;

    @Test //#1 컨텍스트 테스트 , 빈 테스트
    @DisplayName("applicationContext가 생성되어야 한다.")
    public void testApplicationContext(){
        assertThat(context, notNullValue());

    }

    @Test
    @DisplayName("bean check")
    public void testVoucherRepositoryCreattion(){

        var bean = context.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
    }

    @Test
    @DisplayName("orderService Test, use orderService create order")
    public void testOrderService(){
        // Given
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);//픽스 바우처 만듦
        //오더 레포는 스텁으로 만듦(없다는 가정)

        // When
        var order = orderService.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());

        // Then//order에 getter
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }
}
