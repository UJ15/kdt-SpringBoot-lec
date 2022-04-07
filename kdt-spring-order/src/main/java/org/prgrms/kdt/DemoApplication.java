package org.prgrms.kdt;

import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JdbcVoucherRepository;
import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.MessageFormat;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgrms.kdt.order","org.prgrms.kdt.voucher","org.prgrms.kdt.configuration"})

public class DemoApplication {

	public static void main(String[] args) {
		var springApplication = new SpringApplication(DemoApplication.class);
		springApplication.setAdditionalProfiles("local");
		var applicationContext = springApplication.run();
//		var applicationContext = SpringApplication.run(DemoApplication.class, args);

		OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
		System.out.println(MessageFormat.format("version {0}", orderProperties.getVersion()));
		System.out.println(MessageFormat.format("minimumOrderAmount {0}", orderProperties.getMinimumOrderAmount()));
		System.out.println(MessageFormat.format("supportVendors {0}", orderProperties.getSupportVendors()));
		System.out.println(MessageFormat.format("description {0}", orderProperties.getDescription()));


		var customerId = UUID.randomUUID();
		VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
		Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));


		System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));
		System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

	}

}
