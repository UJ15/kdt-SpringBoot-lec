package org.prgrms.kdt.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer insert(Customer customer) throws RuntimeException;

    Customer update(Customer customer) throws RuntimeException;

    //Customer save(Customer customer) 없으면 ~ 넣고 있으면 ~ 업데이트 하고

    List<Customer> findAll();

    Optional<Customer> findById(UUID customerId);
    //findById UUID가 데이터 베이스에 있을 수도 있지만 없을 수 도 있다?
    //그럼 널이 아닌 Optional로 감싸서 나온다.

    public int count();

    Optional<Customer> findByName(String name);

    Optional<Customer> findByEmail(String email);

    void deleteAll();
}
