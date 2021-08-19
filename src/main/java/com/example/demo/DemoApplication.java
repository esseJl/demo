package com.example.demo;


import com.github.eloyzone.jalalicalendar.DateConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DemoApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public DateConverter getDateConverter() {
        return new DateConverter();
    }

    @Override
    public void run(String... args) throws Exception {

    }


}
