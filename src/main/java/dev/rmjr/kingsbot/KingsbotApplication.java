package dev.rmjr.kingsbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class KingsbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingsbotApplication.class, args);
    }
}
