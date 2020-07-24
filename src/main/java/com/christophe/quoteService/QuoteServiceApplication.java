package com.christophe.quoteService;

import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
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
	@Autowired
	CommandLineRunner addUser(UserRepository userRepository){
		return args -> {

			User api = new User();
			api.setEnabled(true);
			api.setCredentialsNonExpired(true);
			api.setAccountNonLocked(true);
			api.setAccountNonExpired(true);
			api.getRoles().add(Role.API);
			api.setUsername("API");
			api.setPassword("API");
			userRepository.save(api);

			User u = new User();
			u.setUsername("admin");
			u.setPassword("admin");
			u.setFirstName("ad");
			u.setLastName("min");
			u.setEmail("admin@admin.com");
			u.getRoles().add(Role.ADMIN);
			u.getRoles().add(Role.USER);
			u.setAccountNonExpired(true);
			u.setAccountNonLocked(true);
			u.setCredentialsNonExpired(true);
			u.setEnabled(true);
			userRepository.save(u);
			User v = new User();
			v.setLastName("tophe");
			v.setFirstName("Chris");
			v.setUsername("chris");
			v.setPassword("chris");
			v.setEmail("chris@chris.com");
			v.getRoles().add(Role.USER);
			v.setAccountNonExpired(true);
			v.setAccountNonLocked(true);
			v.setCredentialsNonExpired(true);
			v.setEnabled(true);
			User w = userRepository.findByUsername("admin");
			v.setUserManager(w);
			userRepository.save(w);
			userRepository.save(v);


		};
	}

}
