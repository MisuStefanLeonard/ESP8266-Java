package JavaESPControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EspcontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(EspcontrolApplication.class, args);
	}

}
