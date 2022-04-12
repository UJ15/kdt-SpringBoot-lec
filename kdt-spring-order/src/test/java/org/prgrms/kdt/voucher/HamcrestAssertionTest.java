package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;


public class HamcrestAssertionTest {

    @Test
    @DisplayName("여러 hamcrest matcher 테스트")
    void hamcrestTest(){
        assertEquals(2, 1+1);//equals는 expected먼저
        assertThat(1 + 1, equalTo(2));//assertThat은 actual 먼저
        assertThat(1 + 1, is(2));
        assertThat(1 + 1, anyOf(is(1), is(2)));// 여러가지 결과가 나올 수 있을떄


        assertNotEquals(1, 1 + 1);
        assertThat(1 + 1, not(1));//assertThat으로 시작해서 결과를 검사 할 수 있다
        //햄 크리스트는 컬렉션도 편리하게 테스트가 가능하다.
    }

    @Test
    @DisplayName("컬렉션에 대한 matcher 테스트")
    void hamcrestListMatcherTest(){
        var prices = List.of(1, 2 ,3);

        assertThat(prices, hasSize(3)); //사이즈 재기
        assertThat(prices, everyItem(greaterThan(0)));// 전체를 돌며 해당 메서드 확인 (모든 원소가 0보다 커?)
        assertThat(prices, containsInAnyOrder(3, 1, 2));//순서를 모를때 애니오더 (순서 노상관) 얘네가 포함되어 있어?
        assertThat(prices, hasItem(2));//안에 2가 있어?
        //컬렉션 같은 경우는 햄크레스트를 자주 쓴다.
        //assert that이 actual이 앞에 먼저 와서 조금 더 직관적인 장점이 있다.
    }


}
