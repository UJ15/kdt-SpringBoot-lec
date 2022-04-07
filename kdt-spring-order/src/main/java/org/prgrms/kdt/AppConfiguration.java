package org.prgrms.kdt;

import org.prgrms.kdt.configuration.YamlPropertiesFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan (basePackages = {"org.prgrms.kdt.order","org.prgrms.kdt.voucher"})
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class) //야믈 설정파일로 잡으려면
@EnableConfigurationProperties //ComfigurationProperties를 쓸 수 있게
public class AppConfiguration {

}