package com.ghost;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Locale;

@SpringBootApplication
public class NoobleApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NoobleApplication.class, args);
		System.out.println(NoobleApplication.class);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NoobleApplication.class);
	}

	@Bean
	private static Configuration freemarkerConfiguration() {
		System.out.println(Configuration.class);
		Configuration configuration = new Configuration(freemarker.template.Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(NoobleApplication.class, "templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLocale(Locale.US);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		return configuration;
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer configurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
