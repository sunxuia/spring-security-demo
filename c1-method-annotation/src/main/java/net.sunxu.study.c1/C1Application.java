package net.sunxu.study.c1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class C1Application {
    public static void main(String[] args) {
        SpringApplication.run(C1Application.class, args);
    }
}
