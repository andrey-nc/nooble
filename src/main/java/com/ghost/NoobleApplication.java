package com.ghost;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class NoobleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoobleApplication.class, args);
		freemarkerConfiguration();
		System.out.println("NoobleApplication - main");
	}

	private static void freemarkerConfiguration() {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(NoobleApplication.class, "templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLocale(Locale.US);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

}
