package com.tcs.finalproject.bankapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index").setViewName("index");
		registry.addViewController("/user").setViewName("user/index");
		registry.addViewController("/user/faqs.html").setViewName("user/faqs");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/roles").setViewName("roles");
		registry.addViewController("/error").setViewName("error");
		registry.addViewController("/admin").setViewName("admin/index");
		registry.addViewController("/admin/faqs.html").setViewName("admin/faqs");
	}
}
