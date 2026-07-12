# IntelliJ Platform Plugin Window Accent

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
JetBrains Marketplace Plugin.

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
