package com.qt.air.cleaner.config.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
	@Value("${file.uploadFolder.path}")
	public String DEVICE_QRCODE_CREATE_PATH;
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/qrcode/**").addResourceLocations("file:" + DEVICE_QRCODE_CREATE_PATH);
	}
}
