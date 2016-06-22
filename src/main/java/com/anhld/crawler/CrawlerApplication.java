package com.anhld.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class CrawlerApplication extends SpringBootServletInitializer{

	//test
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CrawlerApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(CrawlerApplication.class, args);
	}

}
