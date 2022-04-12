package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FixedAmountVoucherTest { // SUT

    private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);
    @BeforeAll //실행시 한 번
    static void set(){
        logger.info("@BeforeAll Just One");
    }

    @BeforeEach// 모든 테스트마다 실행
    void init(){
        logger.info("@BeforeEach every time");
    }

    @Test
    void nameAssertEqual() { //MUT
        assertEquals("1", "1");
    }

    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다.")
    void discount() { //MUT
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, sut.discount(1000));
    }

    @Test
    @DisplayName("할인된 금액은 음수가 될 수 없다")
    void discountMax(){ //기존에 생각해두지 않았던 것들을 테스트를 하며 원본 객체를 수정한다.
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 1000);
        assertEquals(0, sut.discount(100));
    }

    @Test
    @DisplayName("유효한 금액으로만 생성 할 수 있다.")
    void testVoucherCreation(){// 여러 케이스를 동시에 확인, 다시 테스트를 성공시키면서 프로덕션 코드 수정
        assertAll("FixedAmountVoucher creation",
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(),  10000000)));
    }


    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    void testWithMinus(){ //예외를 던지는지 확인하는 클래스
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }

    @Test
    @DisplayName("이 테스트는 잠시 보류")
    @Disabled //테스트 제외
    void testDisable(){

    }
}