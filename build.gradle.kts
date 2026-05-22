plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intellijPlatform)
    alias(libs.plugins.changelog)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
changelog {
    groups.empty()
    repositoryUrl = "https://github.com/alexBlakeGoudemond/jetbrains-window-accent"
}

dependencies {
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion"))
        jetbrainsRuntime()
    }
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set(providers.gradleProperty("pluginSinceBuild"))
        }

        val changelogHandle = changelog
        changeNotes.set(
            providers.gradleProperty("pluginVersion").map {
                changelogHandle.renderItem(
                    changelogHandle.get(it).withHeader(false).withEmptySections(false),
                    org.jetbrains.changelog.Changelog.OutputType.HTML,
                )
            }
        )
    }

    instrumentCode = false
    buildSearchableOptions = false

    // to run manually: `./gradlew verifyPlugin --rerun-tasks --info`
    pluginVerification {
        ides {
            // See https://www.jetbrains.com/idea/download/other/ for other versions
            create("IU", "2026.1.2")
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    instrumentCode {
        enabled = false
    }
    instrumentTestCode {
        enabled = false
    }
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    // not building gradle plugin, so dont load it
    runIde {
        systemProperty("idea.max.content.load.filesize", "2000000")
    }
}
