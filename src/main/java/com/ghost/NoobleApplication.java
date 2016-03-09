package com.ghost;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Locale;

@SpringBootApplication
public class NoobleApplication {

	public static final String APP_NAME = "nooble";

	@Value("${spring.application.name}")
	private static String name;

	public static void main(String[] args) {
		SpringApplication.run(NoobleApplication.class, args);
		System.out.println("NoobleApplication - main");
	}

	@Bean
	private static Configuration freemarkerConfiguration(/*@Value("${spring.application.name}") String name*/)
			throws TemplateModelException {
		System.out.println("NoobleApplication - getFreemarkerConfiguration");
		System.out.println("name - " + name);
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(NoobleApplication.class, "templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLocale(Locale.US);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		configuration.setSharedVariable("appName",name);
		return configuration;
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer () {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
