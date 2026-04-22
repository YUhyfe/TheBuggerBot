package com.yuhyfe.thebuggerbot;

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
    static class ConfigCheck implements CommandLineRunner {

        @Value("${discord.token:}")
        private String token;

        @Value("${discord.guild-id:}")
        private String guildId;

        @Override
        public void run(String... args) {
            log.info("Token loaded: {}", token.isEmpty() ? "NO ❌" : "YES ✅ (length: " + token.length() + ")");
            log.info("Guild ID: {}", guildId.isEmpty() ? "NO ❌" : "YES ✅ (" + guildId + ")");
        }
    }
}