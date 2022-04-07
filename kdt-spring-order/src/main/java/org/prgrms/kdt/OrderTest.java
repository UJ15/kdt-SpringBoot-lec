package org.prgrms.kdt;

import org.prgrms.kdt.AppConfiguration;
import org.prgrms.kdt.order.Order;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JdbcVoucherRepository;
import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;



public class OrderTest {
    public static void main(String[] args) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AppConfiguration.class);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("local");
        applicationContext.refresh();
//        String version = environment.getProperty("kdt.version");
//        Integer minimumOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class);
//        List<String> supportVendors = environment.getProperty("kdt.support-vendors", List.class);
//        var description = environment.getProperty("kdt.description", List.class);
//        System.out.println(MessageFormat.format("version -> {0}", version));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", minimumOrderAmount));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", supportVendors));
//        System.out.println(MessageFormat.format("description -> {0}", description));
        OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
        System.out.println(MessageFormat.format("version {0}", orderProperties.getVersion()));
        System.out.println(MessageFormat.format("minimumOrderAmount {0}", orderProperties.getMinimumOrderAmount()));
        System.out.println(MessageFormat.format("supportVendors {0}", orderProperties.getSupportVendors()));
        System.out.println(MessageFormat.format("description {0}", orderProperties.getDescription()));


        var resource = applicationContext.getResource("application.yaml");
        System.out.println(MessageFormat.format("Resource -> {0}", resource.getClass().getCanonicalName()));
        var file = resource.getFile();
        var strings = Files.readAllLines(file.toPath());
        System.out.println(strings.stream().reduce("", (a, b) -> a + "\n" + b));


        var customerId = UUID.randomUUID();
        VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));


        System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));
        System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

        OrderService orderService = applicationContext.getBean(OrderService.class);//정의된 빈을 가져옴
        Order order = orderService.createOrder(customerId, new ArrayList<OrderItem>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());


        Assert.isTrue(order.totalAmount() == 90L , MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));

    }
}