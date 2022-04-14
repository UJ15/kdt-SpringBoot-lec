package org.prgrms.kdt.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {
    private final UUID customerId;
    private String name;
    private final String email;
    private LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;

    //빌더 패턴을 공부해보자
    public Customer(UUID customerId, String name, String email, LocalDateTime createdAt) {
        validation(name);
        this.name = name;
        this.customerId = customerId;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Customer(UUID customerId, String name, String email, LocalDateTime lastLoginAt, LocalDateTime createdAt) {
        validation(name);
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    //세터는 무분별하게 쓰지 말것

    public void changeName(String name){
        //validation까지
        validation(name);
        this.name = name;
    }

    //이런 식으로 잘못된 이름 변경을 valid 처리
    //이런식으로 도메인 클래스를 만들 때 적용된 비즈니스 룰을 작 작성하는게 중요
    private void validation(String name) throws RuntimeException {
        if (name.isBlank()){
            throw new RuntimeException("Name should not be blank");
        }
    }

    //마지막 로그인은 현재 시간으로
    public void login(){
        this.lastLoginAt = LocalDateTime.now();
    }


    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
