# AutomifyChat

A community site for JVM developers — discussion threads and Q&A posts, organized into communities. Reddit-style hot/new/top feeds, Stack Overflow-style accepted answers. Built in Spring Boot, eating its own dogfood.

![logo](src/main/resources/static/images/logo.png)

## Status

**Phase 0–2 shipped.** What works today:
- Auth (BCrypt, CSRF, form login)
- 7 seeded communities (`java`, `kotlin`, `android`, `spring`, `gradle`, `jvm-internals`, `scala`) + create your own
- Posts (Discussion + Q&A types) with markdown bodies and syntax-highlighted code blocks
- Threaded comments with reply, ±1 voting (HTMX inline), accepted-answer marking for Q&A
- Sort tabs (new / top), per-community feeds
- **Phase 2:** Tags, soft-delete, reports, /u/{username} profiles with karma, full-text search across posts + comments, notifications (REPLY_TO_POST, REPLY_TO_COMMENT, ACCEPTED_ANSWER, @MENTION) with bell + unread count + /notifications page

The original 2018 PHP prototype is preserved at `../Automify-Chat/` as the legacy reference.

## Stack

- Spring Boot 3.4 · Java 21
- Spring Security (form auth, BCrypt)
- Spring Data JPA + Postgres 16 + Flyway
- Thymeleaf + HTMX 2 for the UI

## Running locally

Prerequisites: JDK 21+, Maven 3.9+, Docker.

```bash
# 1. start postgres
docker compose up -d

# 2. run the app (Flyway applies V1__init.sql on startup)
mvn spring-boot:run
```

Open http://localhost:8080. Sign up, log in, create a post.

## Project layout

```
src/main/java/com/automifychat/app/
  AutomifyChatApplication.java
  config/SecurityConfig.java
  auth/    — UserAccount, repo, signup, login wiring
  post/    — Post, PostType (DISCUSSION|QUESTION), repo, service, controller
src/main/resources/
  application.yml         — dev/prod profiles
  db/migration/V1__init.sql
  templates/              — Thymeleaf views
  static/css/app.css
  static/images/logo.png
```

## Roadmap

- **Phase 0 (done)**: Auth, global feed, create/view posts, Q&A vs discussion post types.
- **Phase 1 (done)**: Communities, threaded comments, voting, markdown + syntax highlighting, new/top sort, accepted-answer.
- **Phase 2 (done)**: Tags, soft-delete, reports, user profiles, search, notifications + mentions.
- **Phase 3**: Java-specific moat — inline runnable snippets, library-tagged feeds, snippet-review threads.
- **Phase 4**: DMs, real-time chat rooms (the original 2018 vision, back as a feature), file attachments, mobile app.

## Local dev quick start

```powershell
# H2 in-memory (no Docker):
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Postgres (production-like):
docker compose up -d
mvn spring-boot:run
```

## License

See `../Automify-Chat/LICENSE` (carried forward from the original project).
