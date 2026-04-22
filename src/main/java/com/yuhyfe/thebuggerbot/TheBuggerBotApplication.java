package com.yuhyfe.thebuggerbot;

import com.yuhyfe.thebuggerbot.config.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TheBuggerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheBuggerBotApplication.class, args);
    }

    @Slf4j
    @Component
    @RequiredArgsConstructor
    static class ConfigCheck implements CommandLineRunner {

        private final DiscordProperties properties;

        @Override
        public void run(String... args) {
            log.info("Token loaded: {}", properties.getToken().isEmpty() ? "NO ❌" : "YES ✅");
            log.info("Guild ID: {}", properties.getGuildId().isEmpty() ? "NO ❌" : "YES ✅ (" + properties.getGuildId() + ")");
        }
    }
}