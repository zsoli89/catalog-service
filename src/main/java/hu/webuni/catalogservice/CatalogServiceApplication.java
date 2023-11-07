package hu.webuni.catalogservice;

import hu.webuni.catalogservice.service.InitDbService;
import hu.thesis.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackageClasses = {CatalogServiceApplication.class, JwtAuthFilter.class})
@RequiredArgsConstructor
@EnableCaching
public class CatalogServiceApplication implements CommandLineRunner {

	private final InitDbService initDbService;

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initDbService.deleteDb();
		initDbService.deleteAudTables();
		initDbService.addInitData();
		initDbService.modifyPriceofProduct();
		initDbService.modifyPriceofProduct2();
	}
}
