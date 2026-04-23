# TheBuggerBot

A Discord bot that automates project management for small dev teams. Built as a hands-on project to learn Spring Boot and explore real-world framework usage beyond typical CRUD web apps.

## What it does

Running a Discord server for a programming group involves repetitive setup: creating a category when a new project starts, spinning up the same set of channels, wiring GitHub notifications, archiving when work finishes. TheBuggerBot handles this through slash commands, so the server structure stays consistent and contributors focus on the work instead of the scaffolding.

## Status

Early development. Currently connects to Discord, registers commands per guild, and responds to `/ping`. Project lifecycle commands and GitHub integration are next.

## Tech stack

- **Java 21**
- **Spring Boot 3.4** — IoC container, configuration binding, scheduled tasks, and HTTP endpoints for GitHub webhooks
- **JDA 5.2** — Discord gateway client
- **Spring Data JPA** with **H2** — project persistence (PostgreSQL planned for production)
- **Lombok** — reduces boilerplate in entities and services
- **Maven**

## Design notes

Listeners and commands self-register with JDA through Spring's collection injection: any `@Component` extending `ListenerAdapter` is picked up automatically and passed to the JDA builder. Adding a new command is a matter of creating the class — no wiring, no registry to update.

GitHub webhooks (planned) will be handled by a Spring `@RestController` rather than Discord's built-in GitHub integration, which allows custom embed formatting, signature verification via HMAC, and associating events with internal project IDs.

## Running locally

Requires JDK 21 and a Discord bot token. Create an application at [discord.com/developers](https://discord.com/developers/applications), enable the `SERVER MEMBERS` and `MESSAGE CONTENT` privileged intents, and invite the bot to a test server.

Clone and configure:

\`\`\`bash
git clone https://github.com/YUhyfe/TheBuggerBot.git
cd TheBuggerBot
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
\`\`\`

Edit `application-local.properties` with your bot token and test guild ID, then run:

\`\`\`bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
\`\`\`

The bot will appear online and register slash commands on the configured guild within a few seconds.

## Project structure

```
com.yuhyfe.thebuggerbot
├── config/          Spring configuration, JDA bean, properties binding
├── discord/
│   ├── commands/    Slash command handlers
│   └── listeners/   Gateway event listeners
├── project/         Project domain (planned)
└── github/          Webhook handling (planned)
```