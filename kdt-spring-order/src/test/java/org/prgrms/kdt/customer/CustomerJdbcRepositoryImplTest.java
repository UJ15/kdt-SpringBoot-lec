package org.prgrms.kdt.customer;

import com.wix.mysql.EmbeddedMysql;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerJdbcRepositoryImplTest {

    @Configuration//기본적으로 찾게됨단 ContextConfiguration에 별도로 설정을 안줘도
    @ComponentScan(// #2 오더 서비스 테스트
            basePackages = {"org.prgrms.kdt.customer"}
    )
    static class Config {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test-order_mgmt")
                    .username("test")
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
    }

    @Autowired
    CustomerRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    Customer customer;

    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp(){
        customer = new Customer(UUID.randomUUID(), "testuser", "test-user@gmail.com", LocalDateTime.now());
        var mysqlConfig = aMysqldConfig(v8_0_11)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
      embeddedMysql = anEmbeddedMysql(mysqlConfig)
                .addSchema("test-order_mgmt", classPathScript("schema.sql"))
                .start();
      //  customerJdbcRepository.deleteAll();
    }

    @AfterAll
    void cleanUp(){
        embeddedMysql.stop();
    }

    @Test
    @Order(1)
    public void testHikariConnectionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }//성공시 Autowired 모두 잘되고 주입이 잘됐지?

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll(){

        var customers = customerJdbcRepository.findAll();

        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    public void testFindByName(){

        var customers = customerJdbcRepository.findByName(customer.getName());

        assertThat(customers.isEmpty(), is(false));

        var customerNo = customerJdbcRepository.findByName("unknown");

        assertThat(customerNo.isEmpty(), is(true));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert(){

        customerJdbcRepository.insert(customer);

        var retrivedCustomer = customerJdbcRepository.findById(customer.getCustomerId());
        assertThat(retrivedCustomer.isEmpty(), is(false));
        assertThat(retrivedCustomer.get(), samePropertyValuesAs(customer));
    }

    @Test
    @Order(5)
    @DisplayName("고객을 수정할 수 있다.")
    public void testUpdateCustomer(){//업데이트 확인 테스트
        customer.changeName("update user");
        customerJdbcRepository.update(customer);

        var all = customerJdbcRepository.findAll();

        assertThat(all, hasSize(1));
        assertThat(all, everyItem(samePropertyValuesAs(customer)));

        var retrivedCustomer = customerJdbcRepository.findById(customer.getCustomerId());
        assertThat(retrivedCustomer.isEmpty(), is(false));
        assertThat(retrivedCustomer.get(), samePropertyValuesAs(customer));
    }

}