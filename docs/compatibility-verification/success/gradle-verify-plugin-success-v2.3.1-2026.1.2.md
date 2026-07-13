Below is proof of stability of the plugin version 2.3.1 with IntelliJ IDEA 2026.1.2, verified using the Gradle 
`verifyPlugin` task: `./gradlew verifyPlugin --rerun-tasks --info`

```markdown
./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alex.personal\.gradle\native
Initialized jansi services in: C:\Users\alex.personal\.gradle\native
The client will now receive all logging from the daemon (pid: 37668). The daemon log file: C:\Users\alex.personal\.gradle\daemon\9.5.0\daemon-37668.out.log
Starting 3rd build in daemon [uptime: 43.68 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Encryption key source: default Gradle keystore (pkcs12)
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Calculating task graph as configuration cache cannot be reused because file 'build.gradle.kts' has changed.
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Starting Build
Settings evaluated using settings file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\settings.gradle.kts'.
Using local directory build cache for the root build (location = C:\Users\alex.personal\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Projects loaded. Root project using build file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build.gradle.kts'.
Included projects: [root project 'WindowAccent']

> Configure project :
Evaluating root project 'WindowAccent' using build file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build.gradle.kts'.
Build cache key for Kotlin DSL script compilation (Project/TopLevel/stage1) is c5160959579164e02d3f30965e51ed7a
Stored cache entry for Kotlin DSL script compilation (Project/TopLevel/stage1) with cache key c5160959579164e02d3f30965e51ed7a
Resolved plugin [id: 'java']
Resolved plugin [id: 'org.jetbrains.kotlin.jvm', version: '2.2.20']
Resolved plugin [id: 'org.jetbrains.intellij.platform', version: '2.16.0']
Resolved plugin [id: 'org.jetbrains.changelog', version: '2.5.0']
Build 2d5bfc30-d630-4630-a496-212421fe16e2 is started
Using Kotlin Gradle Plugin gradle813 variant
kotlin scripting plugin: created the scripting discovery configuration: kotlinScriptDef
kotlin scripting plugin: created the scripting discovery configuration: testKotlinScriptDef
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform
[org.jetbrains.intellij.platform] Registering task: patchPluginXml
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform.base
[org.jetbrains.intellij.platform] Registering task: initializeIntellijPlatformPlugin
[org.jetbrains.intellij.platform] Registering task: printBundledModules
[org.jetbrains.intellij.platform] Registering task: printBundledPlugins
[org.jetbrains.intellij.platform] Registering task: printProductsReleases
[org.jetbrains.intellij.platform] Registering task: setupDependencies
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform.module
[org.jetbrains.intellij.platform] Registering task: generateManifest
[org.jetbrains.intellij.platform] Registering task: jar
[org.jetbrains.intellij.platform] Registering task: instrumentCode
[org.jetbrains.intellij.platform] Registering task: instrumentTestCode
[org.jetbrains.intellij.platform] Registering task: instrumentedJar
[org.jetbrains.intellij.platform] Registering task: composedJar
[org.jetbrains.intellij.platform] Registering task: prepareSandbox
[org.jetbrains.intellij.platform] Registering task: prepareTestSandbox
[org.jetbrains.intellij.platform] Registering task: prepareTestIdePerformanceSandbox
[org.jetbrains.intellij.platform] Registering task: cleanSandbox
[org.jetbrains.intellij.platform] Registering task: prepareTest
[org.jetbrains.intellij.platform] Registering task: test
[org.jetbrains.intellij.platform] Registering task: verifyPluginProjectConfiguration
[org.jetbrains.intellij.platform] Registering task: patchPluginXml
[org.jetbrains.intellij.platform] Registering task: processResources
[org.jetbrains.intellij.platform] Registering task: buildSearchableOptions
[org.jetbrains.intellij.platform] Registering task: prepareJarSearchableOptions
[org.jetbrains.intellij.platform] Registering task: jarSearchableOptions
[org.jetbrains.intellij.platform] Registering task: buildPlugin
[org.jetbrains.intellij.platform] Registering task: testIdePerformance
[org.jetbrains.intellij.platform] Registering task: generateSplitModeRunConfigurations
[org.jetbrains.intellij.platform] Registering task: runIde
[org.jetbrains.intellij.platform] Registering task: prepareSandbox_runIdeBackend
[org.jetbrains.intellij.platform] Registering task: prepareSandbox_runIdeFrontend
[org.jetbrains.intellij.platform] Registering task: runIdeBackend
[org.jetbrains.intellij.platform] Registering task: runIdeFrontend
[org.jetbrains.intellij.platform] Registering task: verifyPlugin
[org.jetbrains.intellij.platform] Registering task: verifyPluginSignature
[org.jetbrains.intellij.platform] Registering task: verifyPluginStructure
[org.jetbrains.intellij.platform] Registering task: signPlugin
[org.jetbrains.intellij.platform] Registering task: publishPlugin
Build cache key for Kotlin DSL script compilation (Project/TopLevel/stage2) is e576a8e1509365802047fe7f2726dfa5
Stored cache entry for Kotlin DSL script compilation (Project/TopLevel/stage2) with cache key e576a8e1509365802047fe7f2726dfa5
All projects evaluated.
Task name matched 'verifyPlugin'
Selected primary task 'verifyPlugin' from project :
Directory 'C:\myprograms\jdk\jdk1.6.0_45' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jdk-11.0.17' (Windows Registry) auto-detected used for java installations does not exist
[org.jetbrains.intellij.platform] LocalIvyArtifactPathComponentMetadataRule has been registered.
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':jar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Build 2d5bfc30-d630-4630-a496-212421fe16e2 is closed
Using local directory build cache for the root build (location = C:\Users\alex.personal\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Ignoring listeners of task graph ready event, as this build (:) has already executed work.
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#182,Execution worker,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#183,Execution worker Thread 2,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#188,Execution worker Thread 7,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#183,Execution worker Thread 2,5,main]) started.

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#183,Execution worker Thread 2,5,main]) started.
:compileKotlin (Thread[#183,Execution worker Thread 2,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
Task is untracked because: Should always run
[org.jetbrains.intellij.platform] IntelliJ Platform Gradle Plugin is outdated: 2.16.0. Update `org.jetbrains.intellij.platform` to: 2.18.1
Resolve mutations for :patchPluginXml (Thread[#188,Execution worker Thread 7,5,main]) started.
Resolve mutations for :generateManifest (Thread[#181,included builds,5,main]) started.
:patchPluginXml (Thread[#188,Execution worker Thread 7,5,main]) started.
:generateManifest (Thread[#182,Execution worker,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 94a2e7611a94fb5f8395891cc25b7b89
Task ':generateManifest' is not up-to-date because:
Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 94a2e7611a94fb5f8395891cc25b7b89

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is e385d286c887b97a693253344dc78093
Task ':patchPluginXml' is not up-to-date because:
Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key e385d286c887b97a693253344dc78093
Resolve mutations for :processResources (Thread[#188,Execution worker Thread 7,5,main]) started.
:processResources (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
Not worth caching
Task ':processResources' is not up-to-date because:
Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for task ':compileKotlin' is 84422b36ee07f30a7030cd9a67852de0
Task ':compileKotlin' is not up-to-date because:
Executed with '--rerun-tasks'.
The input changes require a full rebuild for incremental task ':compileKotlin'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
Kotlin source files: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\ClassLoaderLeakDiagnostics.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\GlobalCustomTitleStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\GlobalPanelBackgroundColorStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\UnicodeXmlSanitizer.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowCustomColorStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowCustomTitleStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowPanelAppearanceStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowTitleNumberingStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ColorMagnifier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ScreenColorPicker.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\WindowAccentSettings.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\tool_window\WindowAccentToolWindowFactory.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\diagnostic\WindowAccentLogger.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_color\ColoredPanel.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_color\WindowColorApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_title\TitleTextStyler.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_title\WindowTitleApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\i18n\MyMessageBundle.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\PluginLifecycleListener.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\PluginStartupActivity.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\WindowAccentApplicationService.kt
Java source files:
Script source files:
Script file extensions:
Using Kotlin/JVM incremental compilation
[KOTLIN] Kotlin compilation 'jdkHome' argument: C:\myprograms\java\jdk\jdk21.0.7
i: starting the daemon as: C:\myprograms\java\jdk\jdk21.0.7\bin\java -cp C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar -Djava.awt.headless=true -Djava.rmi.server.hostname=127.0.0.1 -Xmx8112m -XX:ReservedCodeCacheSize=320m -Dkotlin.environment.keepalive -ea -XX:+UseCodeCacheFlushing -XX:+UseParallelGC -Dkotlin.daemon.initiator.marker.file=C:\Users\ALEX~1.PER\AppData\Local\Temp\kotlin-compiler-in-indowccent-8660330154716436183.alive --add-exports java.base/sun.nio.ch=ALL-UNNAMED org.jetbrains.kotlin.daemon.KotlinCompileDaemon --daemon-runFilesPath C:\Users\alex.personal\AppData\Local\kotlin\daemon --daemon-autoshutdownIdleSeconds=7200 --daemon-compilerClasspath C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar
i: #1 retrying connecting to the daemon
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@5f0a7bf6, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state\build-history.bin, useModuleDetection=false), icFeatures=IncrementalCompilationFeatures(usePreciseJavaTracking=true, withAbiSnapshot=false, preciseCompilationResultsBackup=true, keepIncrementalCompilationCachesInMemory=true, enableUnsafeIncrementalCompilationForMultiplatform=false, enableMonotonousIncrementalCompileSetExpansion=true), outputFiles=[C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\kotlin\main, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state])
Finished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key 84422b36ee07f30a7030cd9a67852de0
Resolve mutations for :compileJava (Thread[#188,Execution worker Thread 7,5,main]) started.
:compileJava (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#183,Execution worker Thread 2,5,main]) started.
:classes (Thread[#183,Execution worker Thread 2,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :jar (Thread[#183,Execution worker Thread 2,5,main]) started.
:jar (Thread[#183,Execution worker Thread 2,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
Not worth caching
Task ':jar' is not up-to-date because:
Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\java\main', not found
Resolve mutations for :composedJar (Thread[#188,Execution worker Thread 7,5,main]) started.
:composedJar (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is 3e5107e4736a65e33f5209729d4ca020
Task ':composedJar' is not up-to-date because:
Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key 3e5107e4736a65e33f5209729d4ca020
Resolve mutations for :prepareSandbox (Thread[#188,Execution worker Thread 7,5,main]) started.
:prepareSandbox (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
Not worth caching
Task ':prepareSandbox' is not up-to-date because:
Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\sandbox\WindowAccent\IU-2026.1\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\sandbox\WindowAccent\IU-2026.1\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\sandbox\WindowAccent\IU-2026.1\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\sandbox\WindowAccent\IU-2026.1\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.
:buildSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :buildSearchableOptions SKIPPED
Skipping task ':buildSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :prepareJarSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.
:prepareJarSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :prepareJarSearchableOptions SKIPPED
Skipping task ':prepareJarSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :jarSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.
:jarSearchableOptions (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :jarSearchableOptions SKIPPED
Skipping task ':jarSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :buildPlugin (Thread[#188,Execution worker Thread 7,5,main]) started.
:buildPlugin (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\libs\WindowAccent-2.3.1-searchableOptions.jar', not found
Resolve mutations for :verifyPlugin (Thread[#188,Execution worker Thread 7,5,main]) started.
:verifyPlugin (Thread[#188,Execution worker Thread 7,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alex.personal\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.408\a56154fe5ce12e71a4351cb3983a378a684acc7b\verifier-cli-1.408-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/pluginVerifier -runtime-dir C:/Users/alex.personal/.gradle/caches/9.5.0/transforms/229f610e6ab4e4b444e17b4e4063fcee/transformed/idea-2026.1-win/jbr -subsystems-to-check all -verification-reports-formats plain,html -ignored-problems C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/plugin-verifier-ignored-problems.txt C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/distributions/WindowAccent-2.3.1.zip C:/Users/alex.personal/.gradle/caches/9.5.0/transforms/56beb1883f2ff10932c2aa7547f2d47f/transformed/idea-2026.1.2-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.408
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier
2026-07-13T04:58:58 [main] INFO  verification - Reading IDE C:/Users/alex.personal/.gradle/caches/9.5.0/transforms/56beb1883f2ff10932c2aa7547f2d47f/transformed/idea-2026.1.2-win
2026-07-13T04:58:58 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alex.personal\.gradle\caches\9.5.0\transforms\56beb1883f2ff10932c2aa7547f2d47f\transformed\idea-2026.1.2-win
2026-07-13T04:58:58 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alex.personal\.gradle\caches\9.5.0\transforms\229f610e6ab4e4b444e17b4e4063fcee\transformed\idea-2026.1-win\jbr
2026-07-13T04:58:58 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'intellij.android.gradle.declarative.lang' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.jar'
Layout component 'intellij.android.gradle.declarative.lang.flags' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.flags.jar'
Layout component 'intellij.android.gradle.declarative.lang.sync' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.sync.jar'
Layout component 'intellij.clouds.docker.agent' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-impl\lib\modules\intellij.clouds.docker.agent.jar'
Layout component 'intellij.clouds.docker.dependencies' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-impl\lib\modules\intellij.clouds.docker.dependencies.jar'
Layout component 'intellij.clouds.docker.devcontainers.cli' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-gateway\lib\modules\intellij.clouds.docker.devcontainers.cli.jar'
Layout component 'intellij.clouds.kubernetes' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.jar'
Layout component 'intellij.clouds.kubernetes.libraries' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.libraries.jar'
Layout component 'intellij.community.wintools' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.community.wintools.jar'
Layout component 'intellij.cwm.connection.backend' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.connection.backend.jar'
Layout component 'intellij.cwm.connection.backend.license' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.connection.backend.license.jar'
Layout component 'intellij.cwm.plugin' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.plugin.jar'
Layout component 'intellij.cwm.plugin.common' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.plugin.common.jar'
Layout component 'intellij.database' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.jar'
Layout component 'intellij.database.connectivity.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.connectivity.ex.jar'
Layout component 'intellij.database.dialects.postgres.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgres.ex.jar'
Layout component 'intellij.database.dialects.postgresbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresbase.ex.jar'
Layout component 'intellij.database.dialects.postgresgreenplumbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresgreenplumbase.ex.jar'
Layout component 'intellij.dfa.analysis.rml' has some nonexistent 'classPath' elements: 'plugins\dfa-analysis-plugin\lib\modules\intellij.dfa.analysis.rml.jar'
Layout component 'intellij.dfa.analysis.rml.taint' has some nonexistent 'classPath' elements: 'plugins\dfa-analysis-plugin\lib\modules\intellij.dfa.analysis.rml.taint.jar'
Layout component 'intellij.dfa.analysis.ui' has some nonexistent 'classPath' elements: 'plugins\dfa-analysis-plugin\lib\modules\intellij.dfa.analysis.ui.jar'
Layout component 'intellij.driver.client' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.client.jar'
Layout component 'intellij.driver.impl' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.impl.jar'
Layout component 'intellij.driver.model' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.model.jar'
Layout component 'intellij.gradle' has some nonexistent 'classPath' elements: 'plugins\gradle-plugin\lib\modules\intellij.gradle.jar'
Layout component 'intellij.gradle.java' has some nonexistent 'classPath' elements: 'plugins\gradle-java-plugin\lib\modules\intellij.gradle.java.jar'
Layout component 'intellij.gradle.toolingExtension' has some nonexistent 'classPath' elements: 'plugins\gradle-plugin\lib\modules\intellij.gradle.toolingExtension.jar'
Layout component 'intellij.grazie.core' has some nonexistent 'classPath' elements: 'plugins\grazie\lib\modules\intellij.grazie.core.jar'
Layout component 'intellij.java.compiler.antTasks' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.compiler.antTasks.jar'
Layout component 'intellij.java.compiler.instrumentationUtil' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.compiler.instrumentationUtil.jar'
Layout component 'intellij.java.compiler.instrumentationUtil.java8' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.compiler.instrumentationUtil.java8.jar'
Layout component 'intellij.java.debugger.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.debugger.impl.shared.jar'
Layout component 'intellij.java.execution.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.execution.impl.shared.jar'
Layout component 'intellij.java.frontback.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.impl.jar'
Layout component 'intellij.java.frontback.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.jar'
Layout component 'intellij.java.frontback.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.impl.jar'
Layout component 'intellij.java.guiForms.compiler' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.guiForms.compiler.jar'
Layout component 'intellij.java.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.jar'
Layout component 'intellij.java.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.impl.jar'
Layout component 'intellij.java.syntax' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.syntax.jar'
Layout component 'intellij.javascript.common' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.common.jar'
Layout component 'intellij.javascript.common.css' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.common.css.jar'
Layout component 'intellij.javascript.parser' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.parser.jar'
Layout component 'intellij.javascript.psi.impl' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.psi.impl.jar'
Layout component 'intellij.javascript.web' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.web.jar'
Layout component 'intellij.json' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.jar'
Layout component 'intellij.json.syntax' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.syntax.jar'
Layout component 'intellij.libraries.ai.grazie.spell.gec.engine.local' has some nonexistent 'classPath' elements: 'plugins\grazie\lib\modules\intellij.libraries.ai.grazie.spell.gec.engine.local.jar'
Layout component 'intellij.markdown' has some nonexistent 'classPath' elements: 'plugins\markdown\lib\modules\intellij.markdown.jar'
Layout component 'intellij.platform.backend.split' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.backend.split.jar'
Layout component 'intellij.platform.commercial' has some nonexistent 'classPath' elements: 'lib\intellij.platform.commercial.jar'
Layout component 'intellij.platform.commercial.license' has some nonexistent 'classPath' elements: 'lib\intellij.platform.commercial.license.jar'
Layout component 'intellij.platform.duplicates' has some nonexistent 'classPath' elements: 'lib\intellij.platform.duplicates.jar'
Layout component 'intellij.platform.duplicatesDetector' has some nonexistent 'classPath' elements: 'lib\intellij.platform.duplicatesDetector.jar'
Layout component 'intellij.platform.graph' has some nonexistent 'classPath' elements: 'lib\intellij.platform.graph.jar'
Layout component 'intellij.platform.graph.impl' has some nonexistent 'classPath' elements: 'lib\intellij.platform.graph.impl.jar'
Layout component 'intellij.platform.jps.build.javac.rt' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.platform.jps.build.javac.rt.jar'
Layout component 'intellij.platform.lsp' has some nonexistent 'classPath' elements: 'lib\intellij.platform.lsp.jar'
Layout component 'intellij.platform.lsp.impl' has some nonexistent 'classPath' elements: 'lib\intellij.platform.lsp.impl.jar'
Layout component 'intellij.platform.remoteController' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.remoteController.jar'
Layout component 'intellij.platform.remoteController.backend' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.remoteController.backend.jar'
Layout component 'intellij.platform.split' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.split.jar'
Layout component 'intellij.platform.ssh' has some nonexistent 'classPath' elements: 'lib\intellij.platform.ssh.jar'
Layout component 'intellij.platform.ssh.attach' has some nonexistent 'classPath' elements: 'lib\intellij.platform.ssh.attach.jar'
Layout component 'intellij.platform.ssh.core' has some nonexistent 'classPath' elements: 'lib\intellij.platform.ssh.core.jar'
Layout component 'intellij.platform.ssh.ui' has some nonexistent 'classPath' elements: 'lib\intellij.platform.ssh.ui.jar'
Layout component 'intellij.properties' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.jar'
Layout component 'intellij.properties.psi' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.psi.jar'
Layout component 'intellij.qodana.sarif' has some nonexistent 'classPath' elements: 'plugins\qodana\lib\modules\intellij.qodana.sarif.jar'
Layout component 'intellij.qodana.util' has some nonexistent 'classPath' elements: 'plugins\qodana\lib\modules\intellij.qodana.util.jar'
Layout component 'intellij.sh.core' has some nonexistent 'classPath' elements: 'plugins\sh-plugin\lib\modules\intellij.sh.core.jar'
Layout component 'intellij.toml.core' has some nonexistent 'classPath' elements: 'plugins\toml\lib\modules\intellij.toml.core.jar'
Layout component 'intellij.vcs.git.shared' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git.shared.jar'
Layout component 'intellij.vcs.github' has some nonexistent 'classPath' elements: 'plugins\vcs-github\lib\modules\intellij.vcs.github.jar'
Layout component 'intellij.vcs.gitlab' has some nonexistent 'classPath' elements: 'plugins\vcs-gitlab\lib\modules\intellij.vcs.gitlab.jar'
Layout component 'intellij.yaml' has some nonexistent 'classPath' elements: 'plugins\yaml\lib\modules\intellij.yaml.jar'
2026-07-13T04:58:59 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\distributions\WindowAccent-2.3.1.zip
2026-07-13T04:59:05 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:2.3.1 against IU-261.24374.151

2026-07-13T05:04:25 [main] INFO  verification - Finished 1 of 1 verifications (in 320,5 s): IU-261.24374.151 against WindowAccent:2.3.1: Compatible. 1 usage of deprecated API
2026-07-13T05:04:25 [main] INFO  c.j.p.PluginVerifierMain - Started printing results (CheckPluginResultPrinter)
Plugin WindowAccent:2.3.1 against IU-261.24374.151: Compatible. 1 usage of deprecated API
Deprecated API usages (1):
#Deprecated method com.intellij.openapi.wm.ToolWindowManager.unregisterToolWindow(String) invocation
Deprecated method com.intellij.openapi.wm.ToolWindowManager.unregisterToolWindow(java.lang.String arg0) : void is invoked in com.window_accent.WindowAccentApplicationService.Companion.flushToolWindowRegistrations() : void
Dynamic Plugin Eligibility:
Plugin can probably be enabled or disabled without IDE restart

2026-07-13T05:04:25 [main] INFO  c.j.p.PluginVerifierMain - Finished printing results in 161ms
2026-07-13T05:04:25 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 2 m 56 s 646 ms
2026-07-13T05:04:25 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 781,16 MB
2026-07-13T05:04:25 [main] INFO  verification - Total amount of space used for plugins and dependencies: 781,16 MB
2026-07-13T05:04:26 [main] INFO  verification - PluginDetailsCache statistics: hits=50, misses=15, failures=0, evictions=0, total=65, hit-rate=76,9%
2026-07-13T05:04:26 [main] INFO  verification - Verification reports for WindowAccent:2.3.1 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier\IU-261.24374.151
2026-07-13T05:04:26 [main] INFO  verification - Total time spent in plugin verification: 5 m 27 s 101 ms
Build a93f5843-de0f-43af-9e23-237ed86dbda1 is started
Build a93f5843-de0f-43af-9e23-237ed86dbda1 is closed

See the complete report at file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/configuration-cache/dcz716v1nmyhw3b9atul2seb9/9m3c28ezaohmb0km6yz9lx6s0/configuration-cache-report.html

[Incubating] Problems report is available at: file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/problems/problems-report.html

BUILD SUCCESSFUL in 5m 51s
10 actionable tasks: 10 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent]
Configuration cache entry stored.
Configuration Cache (C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache) [subdir: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache\1c828583-3e6b-45f0-93da-bbae98cd59e5] cleanup deleted 8 files/directories.
```