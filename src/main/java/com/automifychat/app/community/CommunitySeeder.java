package com.automifychat.app.community;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class CommunitySeeder implements CommandLineRunner {

    private static final List<Map.Entry<String, String[]>> DEFAULTS = List.of(
        Map.entry("java",          new String[]{"Java",           "Core Java language, JDK, JVM features"}),
        Map.entry("kotlin",        new String[]{"Kotlin",         "Kotlin language, multiplatform, coroutines"}),
        Map.entry("android",       new String[]{"Android",        "Android SDK, Jetpack, Compose"}),
        Map.entry("spring",        new String[]{"Spring",         "Spring Boot, Spring Framework, Spring Cloud"}),
        Map.entry("gradle",        new String[]{"Gradle",         "Build automation: Gradle, Maven, build perf"}),
        Map.entry("jvm-internals", new String[]{"JVM Internals",  "Hotspot, GC, JIT, performance tuning"}),
        Map.entry("scala",         new String[]{"Scala",          "Scala 3, Akka, functional JVM"})
    );

    private final CommunityRepository communities;

    public CommunitySeeder(CommunityRepository communities) {
        this.communities = communities;
    }

    @Override
    @Transactional
    public void run(String... args) {
        for (var entry : DEFAULTS) {
            String slug = entry.getKey();
            if (!communities.existsBySlug(slug)) {
                communities.save(new Community(slug, entry.getValue()[0], entry.getValue()[1], null));
            }
        }
    }
}
