package com.yuhyfe.thebuggerbot.discord.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ping")) return;

        long gatewayPing = event.getJDA().getGatewayPing();
        event.reply("Pong! Gateway: " + gatewayPing + "ms")
                .setEphemeral(true)
                .queue();

        log.debug("Ping from {} (gateway: {}ms)", event.getUser().getName(), gatewayPing);
    }
}
