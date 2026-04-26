package com.yuhyfe.thebuggerbot.discord.commands;

import com.yuhyfe.thebuggerbot.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloseProjectCommand extends ListenerAdapter {

    private final ProjectService projectService;

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("closeproject")) return;

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command is only usable on a server.")
                    .setEphemeral(true).queue();
            return;
        }

        OptionMapping nameOption = event.getOption("name");
        if (nameOption == null) {
            event.reply("Missing 'name' option.").setEphemeral(true).queue();
            return;
        }
        String name = nameOption.getAsString();

        event.deferReply().queue();
    }

}
