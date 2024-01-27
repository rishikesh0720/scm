package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication                // same as @Configuration @EnableAutoConfiguration @ComponentScan

//@ComponentScan({"com.smart.controller"}) //done to solve bean error
//@ComponentScan is an annotation in Spring Framework that is used to enable component scanning in your application. Component 
//scanning is a way for Spring to automatically discover and register Spring beans (components, services, repositories, etc.)
//The argument passed to @ComponentScan is the base package(s) where Spring should start searching for components.
//"com.smart". This means that Spring will scan the package com.smart and its sub-packages to find and register Spring beans. 
@ComponentScan({"com.smart"})

public class SmartcontactmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}

}
