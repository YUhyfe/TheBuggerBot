package com.yuhyfe.thebuggerbot.project;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    private void validateName(String name) {
        if (name == null || !VALID_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Invalid name. Allowed: 3-20 characters, lower case, numbers, dashes"
            );
        }
    }
}
