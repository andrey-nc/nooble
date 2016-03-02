package com.ghost;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoobleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoobleApplication.class, args);
		System.out.println("main");
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(NoobleApplication.class, "templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

	}
}
