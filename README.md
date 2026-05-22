# IntelliJ Platform Plugin Template

[![Twitter Follow](https://img.shields.io/badge/follow-%40JBPlatform-1DA1F2?logo=twitter)](https://twitter.com/JBPlatform)
[![Developers Forum](https://img.shields.io/badge/JetBrains%20Platform-Join-blue)][jb:forum]

## Window Accent

**Window Accent** helps you distinguish multiple JetBrains IDE windows with visual cues, a dedicated tool window, and
configurable settings.

## What it offers

- A **tool window** for quick access to the plugin’s features
- A **settings page** for configuring the panel’s behavior and appearance
- Visual cues that help you tell multiple open IDE windows apart
- A simple workflow for checking and adjusting the plugin without leaving the IDE

## How is it different from other plugins

The author is aware of the following plugins previously published to the Jetbrains Marketplace:

- [Project Color](https://plugins.jetbrains.com/plugin/19463-project-color)
- [Colorized Project](https://plugins.jetbrains.com/plugin/20920-colorized-project)

This plugin was developed without the awareness of the plugins described above. I.E. it was not developed as an
alternative to the plugins mentioned above, but rather as a personal project to explore the Proof of Concept of a
Jetbrains Marketplace Plugin.

However, inspiration may be taken from the plugins mentioned above - especially if those plugins are not compatible with
the current versions of the IntelliJ IDEs. Attribution will be shared in the Plugin description as well

## Where to find the settings

Open **Settings / Preferences** in the IDE and look under the plugin’s settings area.  
From there, you can adjust how the window accent behaves and how it is displayed.

## Tool window

The tool window provides quick access to window-related controls and status.  
It is designed to make multi-window workflows easier by giving you a central place to review and manage the plugin’s
visual behavior.

## Screenshots

Screenshots have been added to a folder `docs/screenshots/` and uploaded them to the JetBrains Marketplace listing.

## Plugin template structure

This project uses the content structure:

```
├── .run/ Predefined Run/Debug Configurations 
├── build/ Output build directory 
├── gradle 
│ ├── wrapper/ Gradle Wrapper 
│ ├── libs.versions.toml Version catalog 
├── src Plugin sources 
│ ├── main 
│ │ ├── kotlin/ Kotlin production sources 
│ │ └── resources/ Resources - plugin.xml, icons, messages 
├── .gitignore Git ignoring rules 
├── build.gradle.kts Gradle build configuration 
├── gradle.properties Gradle configuration properties 
├── gradlew *nix Gradle Wrapper script 
├── gradlew.bat Windows Gradle Wrapper script 
├── README.md README 
└── settings.gradle.kts Gradle project settings
```

In addition to the configuration files, the most crucial part is the `src` directory, which contains the implementation
and the manifest for our plugin – [plugin.xml][file:plugin.xml].

> [!NOTE]
> To use Java in your plugin, create the `/src/main/java` directory.

## Plugin configuration file

The plugin configuration file is a [plugin.xml][file:plugin.xml] file located in the `src/main/resources/META-INF`
directory.
It provides general information about the plugin, its dependencies, extensions, and listeners.

You can read more about this file in the [Plugin Configuration File][docs:plugin.xml] section of our documentation.

If you're still not quite sure what this is all about, read our
introduction: [What is the IntelliJ Platform?][docs:intro]

$H$H Predefined Run/Debug configurations

Within the default project structure, there is a `.run` directory provided containing predefined *Run/Debug
configurations* that expose corresponding Gradle tasks:

| Configuration name | Description                                                                                                                                                                         |
|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Run Plugin         | Runs [`:runIde`][gh:intellij-platform-gradle-plugin-runIde] IntelliJ Platform Gradle Plugin task. Use the *Debug* icon for plugin debugging.                                        |
| Run Tests          | Runs [`:test`][gradle:lifecycle-tasks] Gradle task.                                                                                                                                 |
| Run Verifications  | Runs [`:verifyPlugin`][gh:intellij-platform-gradle-plugin-verifyPlugin] IntelliJ Platform Gradle Plugin task to check the plugin compatibility against the specified IntelliJ IDEs. |

> [!NOTE]
> You can find the logs from the running task in the `idea.log` tab.

## Publishing the plugin

> [!TIP]
> Make sure to follow all guidelines listed in [Publishing a Plugin][docs:publishing] to follow all recommended and
> required steps.

Releasing a plugin to [JetBrains Marketplace](https://plugins.jetbrains.com) is a straightforward operation that uses
the `publishPlugin` Gradle task provided by
the [intellij-platform-gradle-plugin][gh:intellij-platform-gradle-plugin-docs].

You can also upload the plugin to the [JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/upload)
manually via UI.

## Useful links

- [IntelliJ Platform SDK Plugin SDK][docs]
- [IntelliJ Platform Gradle Plugin Documentation][gh:intellij-platform-gradle-plugin-docs]
- [IntelliJ Platform Explorer][jb:ipe]
- [JetBrains Marketplace Quality Guidelines][jb:quality-guidelines]
- [IntelliJ Platform UI Guidelines][jb:ui-guidelines]
- [JetBrains Marketplace Paid Plugins][jb:paid-plugins]
- [IntelliJ SDK Code Samples][gh:code-samples]

[docs]: https://plugins.jetbrains.com/docs/intellij

[docs:intro]: https://plugins.jetbrains.com/docs/intellij/intellij-platform.html?from=IJPluginTemplate

[docs:plugin.xml]: https://plugins.jetbrains.com/docs/intellij/plugin-configuration-file.html?from=IJPluginTemplate

[docs:publishing]: https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate

[file:plugin.xml]: ./src/main/resources/META-INF/plugin.xml

[gh:code-samples]: https://github.com/JetBrains/intellij-sdk-code-samples

[gh:intellij-platform-gradle-plugin]: https://github.com/JetBrains/intellij-platform-gradle-plugin

[gh:intellij-platform-gradle-plugin-docs]: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html

[gh:intellij-platform-gradle-plugin-runIde]: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#runIde

[gh:intellij-platform-gradle-plugin-verifyPlugin]: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#verifyPlugin

[gradle:lifecycle-tasks]: https://docs.gradle.org/current/userguide/java_plugin.html#lifecycle_tasks

[jb:github]: https://github.com/JetBrains/.github/blob/main/profile/README.md

[jb:forum]: https://platform.jetbrains.com/

[jb:quality-guidelines]: https://plugins.jetbrains.com/docs/marketplace/quality-guidelines.html

[jb:paid-plugins]: https://plugins.jetbrains.com/docs/marketplace/paid-plugins-marketplace.html

[jb:quality-guidelines]: https://plugins.jetbrains.com/docs/marketplace/quality-guidelines.html

[jb:ipe]: https://jb.gg/ipe

[jb:ui-guidelines]: https://jetbrains.github.io/ui