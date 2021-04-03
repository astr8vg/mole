package com.zd.mole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * mole 鼹鼠网络爬取工具（爬虫）
 *
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zd.mole.**"})
@EntityScan(basePackages = {"com.zd.mole.**.entity"})
@ImportResource(locations = {"classpath:spring.xml"})
public class App {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		Mole mole = context.getBean(Mole.class);
		mole.start();
    }
}
