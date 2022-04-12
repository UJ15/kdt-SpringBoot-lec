package org.prgrms.kdt;


import org.prgrms.kdt.order.OrderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class DemoApplication {
	private static final Logger logger = LoggerFactory.getLogger(OrderTest.class);
	public static void main(String[] args) {
		var applicationContext = SpringApplication.run(DemoApplication.class, args);


		OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
		logger.info("version {}", orderProperties.getVersion());
		logger.info("minimumOrderAmount {}", orderProperties.getMinimumOrderAmount());
		logger.info("supportVendors {}", orderProperties.getSupportVendors());
		logger.info("description {}", orderProperties.getDescription());


	}

}
