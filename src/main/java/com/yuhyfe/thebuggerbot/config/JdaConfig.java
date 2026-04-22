package com.yuhyfe.thebuggerbot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdaConfig {

    private final DiscordProperties properties;
    private final List<ListenerAdapter> listeners;

    @Bean(destroyMethod = "shutdown")
    public JDA jda() throws InterruptedException {
        log.info("Starting JDA with {} listeners...", listeners.size());

        JDA jda = JDABuilder.createDefault(properties.getToken())
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES
                )
                .addEventListeners(listeners.toArray())
                .build()
                .awaitReady();

        log.info("JDA ready. Logged in as: {}", jda.getSelfUser().getName());
        return jda;
    }
}
