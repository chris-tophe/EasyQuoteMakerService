package com.christophe.quoteService;

import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.var;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource({"classpath:database-${spring.profiles.active}.properties"})
public class QuoteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteServiceApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("HTTPSONLY");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		return tomcat;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	@Autowired
	CommandLineRunner addUser(UserRepository userRepository){
		return args -> {

			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setFirstName("Admin");
			admin.setLastName("Admin");
			admin.setEmail("admin@admin.com");
			admin.getRoles().add(Role.ADMIN);
			admin.setAccountNonExpired(true);
			admin.setAccountNonLocked(true);
			admin.setCredentialsNonExpired(true);
			admin.setEnabled(true);
			userRepository.save(admin);

			var faker = new Faker();

			for (int i = 0 ; i < 100; i++){
				User u = new User();
				u.setFirstName(faker.name().firstName());
				u.setLastName(faker.name().lastName());
				u.setUsername( u.getFirstName() + "." + u.getLastName());
				u.setPassword(faker.internet().password(8,10));
				u.setEmail(faker.internet().emailAddress(u.getUsername()));
				u.getRoles().add(Role.USER);
				u.setAccountNonExpired(true);
				u.setAccountNonLocked(true);
				u.setCredentialsNonExpired(true);
				u.setEnabled(true);
				userRepository.save(u);
			}
		};
	}

}
