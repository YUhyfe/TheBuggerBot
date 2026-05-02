package com.yuhyfe.thebuggerbot.project;

import com.yuhyfe.thebuggerbot.project.exception.ProjectAlreadyArchivedException;
import com.yuhyfe.thebuggerbot.project.exception.ProjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final Pattern VALID_NAME = Pattern.compile("^[a-z0-9-]{3,20}$");

    private final ProjectRepository repository;

    @Transactional
    public Project createProject(String name, Guild guild, String createdByUserId) {
        validateName(name);

        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Project with a name '" + name + "' already exists");
        }

        Category category = guild.createCategory("🚀 " + name).complete();
        log.info("Created Category '{}' with id {}", category.getName(), category.getId());

        category.createTextChannel(name + "-general").complete();
        category.createTextChannel(name + "-github").complete();
        category.createForumChannel(name + "-todo").complete();
        category.createVoiceChannel(name + "-VC").complete();

        Project project = Project.builder()
                .name(name)
                .categoryId(category.getId())
                .createdBy(createdByUserId)
                .createdAt(Instant.now())
                .status(Project.ProjectStatus.ACTIVE)
                .build();

        Project saved = repository.save(project);
        log.info("Project '{}' saved with id {}", name, saved.getId());
        return saved;
    }

    @Transactional
    public Project closeProject(String name, Guild guild) {
        Project project = repository.findByName(name)
                .orElseThrow(() -> new ProjectNotFoundException(name));

        if (project.getStatus() == Project.ProjectStatus.ARCHIVED)
            throw new ProjectAlreadyArchivedException(name);

        Category category = guild.getCategoryById(project.getCategoryId());
        if (category == null)
            throw new IllegalStateException(
                    "Category for project '" + name + "' missing on Discord - was it manually deleted?"
            );

        category.getManager().setName("📦 " + project.getName()).queue();

        List<Role> roles = guild.getRolesByName("Dev", false);
        if (roles.isEmpty()) {
            log.warn("Dev role not found — skipping permission update for project '{}'", name);
        } else {
            for (GuildChannel channel : category.getChannels()) {

                if (channel instanceof VoiceChannel) {
                    channel.delete().queue();
                    continue;
                }

                if (channel instanceof IPermissionContainer container) {
                    container.upsertPermissionOverride(roles.getFirst())
                            .deny(
                                    Permission.MESSAGE_SEND,
                                    Permission.MESSAGE_SEND_IN_THREADS,
                                    Permission.CREATE_PUBLIC_THREADS,
                                    Permission.CREATE_PRIVATE_THREADS
                            )
                            .queue();
                }
            }
        }

        category.getManager().setPosition(999).queue();

        project.setStatus(Project.ProjectStatus.ARCHIVED);
        repository.save(project);

        return project;
    }

    private void validateName(String name) {
        if (name == null || !VALID_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Invalid name. Allowed: 3-20 characters, lower case, numbers, dashes"
            );
        }
    }
}
