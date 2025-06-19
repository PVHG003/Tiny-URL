package vn.pvhg.tinyurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TinyUrlBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyUrlBackendApplication.class, args);
    }

}
