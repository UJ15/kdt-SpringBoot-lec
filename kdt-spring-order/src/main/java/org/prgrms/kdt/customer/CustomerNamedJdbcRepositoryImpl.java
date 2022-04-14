package org.prgrms.kdt.customer;

import org.prgrms.kdt.JdbcCustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;


@Repository
public class CustomerNamedJdbcRepositoryImpl implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);


    private final NamedParameterJdbcTemplate jdbcTemplate;
    //사실 템플릿이 있으면 데이터 소스가 필요없다.

    private static RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name"); //name collum value call
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var customerEmail = resultSet.getString("email");
        var customerLastAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Customer(customerId, customerName, customerEmail, customerLastAt, createdAt);
    };

    private Map<String, Object> toParamMap(Customer customer){
        return new HashMap<>() {{
            put ( "customerId", customer.getCustomerId().toString().getBytes());
            put ( "name", customer.getName());
            put ( "email", customer.getEmail());
            put ("createdAt", customer.getCreatedAt());
            put ( "lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};
    }

    public CustomerNamedJdbcRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer insert(Customer customer) throws RuntimeException {
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(:customerId), :name, :email, :createdAt)",
                toParamMap(customer));
        if (update != 1){
            throw new RuntimeException("Noting was inserted");
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = :name, email = :email,  last_login_at = :lastLoginAt WHERE customer_id = UUID_TO_BIN(:customerId)",
                toParamMap(customer));
        if (update != 1){
            throw new RuntimeException("Noting was update");
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
        //이런식으로 코드가 줄어
    }



    @Override
    public Optional<Customer> findById(UUID customerId) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UUID_TO_BIN(:customerId)",
                    Collections.singletonMap("customerId", customerId.toString().getBytes()) ,customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.info("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public int count() {//리턴 값이 하나인 경우 queryForObject로 반환 가능
        return jdbcTemplate.queryForObject("select count(*) from customers", Collections.emptyMap(), Integer.class);
        //맵을 파라미터로 전달해줘야 하기 때문에 빈 맵을 넣어줬다.
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = :name ",
                    Collections.singletonMap("name", name)
                    ,customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.info("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = :email ",
                    Collections.singletonMap("email", email)
                    ,customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.info("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM customers");
    }

    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
