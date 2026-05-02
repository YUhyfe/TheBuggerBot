package com.yuhyfe.thebuggerbot.discord.listeners;

import com.yuhyfe.thebuggerbot.config.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReadyListener extends ListenerAdapter {

    private final DiscordProperties properties;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(properties.getGuildId());
        if (guild == null) {
            log.error("Guild {} not found! Check your config or bot invitation.", properties.getGuildId());
            return;
        }

        guild.updateCommands().addCommands(
                Commands.slash("ping", "Sprawdź czy bot żyje"),

                Commands.slash("createproject", "Create a new project with category and channels")
                        .addOptions(
                                new OptionData(OptionType.STRING, "name", "Project name (kebab-case)")
                                        .setMinLength(3)
                                        .setMaxLength(20)
                        ),
                Commands.slash("closeproject", "Close a project archiving its category.")
                        .addOptions(
                                new OptionData(OptionType.STRING, "name", "Project name (kebab-case)")
                                        .setMinLength(3)
                                        .setMaxLength(20)
                        )
        ).queue(
                success -> log.info("Registered {} command for guild '{}'", success.size(), guild.getName()),
                error -> log.error("Failed to register commands", error)
        );
    }
}
