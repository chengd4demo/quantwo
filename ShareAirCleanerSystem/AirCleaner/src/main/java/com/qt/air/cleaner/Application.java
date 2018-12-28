package com.qt.air.cleaner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qt.air.cleaner.system.service.security.UserService;

@Controller
@SpringBootApplication(scanBasePackages="com.qt.air.cleaner")
@EnableTransactionManagement
public class Application  extends SpringBootServletInitializer {
	@Autowired UserService userService;
	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);
	}
    /* (non-Javadoc) 打war包上线需要的操作
     * @see org.springframework.boot.web.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
