package ru.maza.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.Locale;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class TelegramApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        Locale.setDefault(new Locale("ru", "RU"));
        SpringApplication.run(TelegramApplication.class, args);
    }

}
