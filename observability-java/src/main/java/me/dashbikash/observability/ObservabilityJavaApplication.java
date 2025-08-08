package me.dashbikash.observability;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ObservabilityJavaApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ObservabilityJavaApplication.class);
	    app.setBannerMode(Banner.Mode.OFF); // Disable the banner
	    app.run(args);
	}

}
