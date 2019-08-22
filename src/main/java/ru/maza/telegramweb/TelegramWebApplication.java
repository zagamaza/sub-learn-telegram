package ru.maza.telegramweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.Locale;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class TelegramWebApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        Locale.setDefault(new Locale("ru", "RU"));
        SpringApplication.run(TelegramWebApplication.class, args);
    }

}
