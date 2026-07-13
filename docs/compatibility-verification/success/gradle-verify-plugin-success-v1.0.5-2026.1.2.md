Command used to generate the results: `./gradlew verifyPlugin --rerun-tasks --info`

```markdown
$ ./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alexander.goudemond\.gradle\native
Initialized jansi services in: C:\Users\alexander.goudemond\.gradle\native
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
The client will now receive all logging from the daemon (pid: 26060). The daemon log file: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0\daemon-26060.out.log
Starting 54th build in daemon [uptime: 32 mins 14.605 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Encryption key source: default Gradle keystore (pkcs12)
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe -XshowSettings:properties -version
Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe''
Calculating task graph as configuration cache cannot be reused because a build logic input of type 'JavaRuntimeMetadataValueSource' has changed.
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Starting Build
Settings evaluated using settings file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\settings.gradle.kts'.
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Projects loaded. Root project using build file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build.gradle.kts'.
Included projects: [root project 'WindowAccent']

> Configure project :
Evaluating root project 'WindowAccent' using build file 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build.gradle.kts'.
Resolved plugin [id: 'java']
Resolved plugin [id: 'org.jetbrains.kotlin.jvm', version: '2.2.20']
Resolved plugin [id: 'org.jetbrains.intellij.platform', version: '2.10.5']
Resolved plugin [id: 'org.jetbrains.changelog', version: '2.5.0']
Build 5353e373-0b7b-4ecd-bdda-a73193c3b6f6 is started
Using Kotlin Gradle Plugin gradle813 variant
kotlin scripting plugin: created the scripting discovery configuration: kotlinScriptDef
kotlin scripting plugin: created the scripting discovery configuration: testKotlinScriptDef
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform
[org.jetbrains.intellij.platform] Configuring task: patchPluginXml
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform.base
[org.jetbrains.intellij.platform] Configuring task: initializeIntellijPlatformPlugin
[org.jetbrains.intellij.platform] Configuring task: printBundledPlugins
[org.jetbrains.intellij.platform] Configuring task: printProductsReleases
[org.jetbrains.intellij.platform] Configuring task: setupDependencies
[org.jetbrains.intellij.platform] Configuring plugin: org.jetbrains.intellij.platform.module
[org.jetbrains.intellij.platform] Configuring task: generateManifest
[org.jetbrains.intellij.platform] Configuring task: jar
[org.jetbrains.intellij.platform] Configuring task: instrumentCode
[org.jetbrains.intellij.platform] Configuring task: instrumentTestCode
[org.jetbrains.intellij.platform] Configuring task: instrumentedJar
[org.jetbrains.intellij.platform] Configuring task: composedJar
[org.jetbrains.intellij.platform] Configuring task: prepareSandbox
[org.jetbrains.intellij.platform] Configuring task: prepareTestSandbox
[org.jetbrains.intellij.platform] Configuring task: prepareTestIdePerformanceSandbox
[org.jetbrains.intellij.platform] Configuring task: prepareTest
[org.jetbrains.intellij.platform] Configuring task: test
[org.jetbrains.intellij.platform] Configuring task: verifyPluginProjectConfiguration
[org.jetbrains.intellij.platform] Configuring plugin configuration verification task
[org.jetbrains.intellij.platform] Configuring task: patchPluginXml
[org.jetbrains.intellij.platform] Configuring task: processResources
[org.jetbrains.intellij.platform] Configuring task: buildSearchableOptions
[org.jetbrains.intellij.platform] Configuring task: prepareJarSearchableOptions
[org.jetbrains.intellij.platform] Configuring task: jarSearchableOptions
[org.jetbrains.intellij.platform] Configuring task: buildPlugin
[org.jetbrains.intellij.platform] Configuring task: testIdePerformance
[org.jetbrains.intellij.platform] Configuring task: runIde
[org.jetbrains.intellij.platform] Configuring task: verifyPlugin
[org.jetbrains.intellij.platform] Configuring task: verifyPluginSignature
[org.jetbrains.intellij.platform] Configuring task: verifyPluginStructure
[org.jetbrains.intellij.platform] Configuring task: signPlugin
[org.jetbrains.intellij.platform] Configuring task: publishPlugin
All projects evaluated.
Task name matched 'verifyPlugin'
Selected primary task 'verifyPlugin' from project :
Directory 'C:\myprograms\jdk\jdk1.6.0_45' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jdk-11.0.17' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre6' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre8' (Windows Registry) auto-detected used for java installations does not exist
[org.jetbrains.intellij.platform] LocalIvyArtifactPathComponentMetadataRule has been registered.
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe -XshowSettings:properties -version
Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr/bin/java.exe''
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':instrumentCode', task ':jar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Build 5353e373-0b7b-4ecd-bdda-a73193c3b6f6 is closed
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Ignoring listeners of task graph ready event, as this build (:) has already executed work.
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#1708,Execution worker,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#1708,Execution worker,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#1708,Execution worker,5,main]) started.
:compileKotlin (Thread[#1708,Execution worker,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
  Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Resolve mutations for :patchPluginXml (Thread[#1709,Execution worker Thread 2,5,main]) started.
Resolve mutations for :generateManifest (Thread[#1707,included builds,5,main]) started.
:generateManifest (Thread[#1707,included builds,5,main]) started.
:patchPluginXml (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 6cb767a2092b278499f8ac708bf45c98
Task ':generateManifest' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 6cb767a2092b278499f8ac708bf45c98

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is 7f8a9db0ed9c754a2343ebe231455047
Task ':patchPluginXml' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key 7f8a9db0ed9c754a2343ebe231455047
Resolve mutations for :processResources (Thread[#1709,Execution worker Thread 2,5,main]) started.
:processResources (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
  Not worth caching
Task ':processResources' is not up-to-date because:
  Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for task ':compileKotlin' is 7f228e48493365f524f4a728a1060f0a
Task ':compileKotlin' is not up-to-date because:
  Executed with '--rerun-tasks'.
The input changes require a full rebuild for incremental task ':compileKotlin'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
Kotlin source files: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowCustomColorStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowPanelAppearanceStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowTitleNumberingStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ColorMagnifier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ScreenColorPicker.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\WindowAccentSettings.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\tool_window\WindowAccentToolWindowFactory.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_color\WindowColorApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_title\WindowTitleApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\i18n\MyMessageBundle.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\PluginLifecycleListener.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\PluginStartupActivity.kt
Java source files:
Script source files:
Script file extensions:
Using Kotlin/JVM incremental compilation
[KOTLIN] Kotlin compilation 'jdkHome' argument: C:\myprograms\java\jdk\jdk21.0.7
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@3ec3d9b5, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state\build-history.bin, useModuleDetection=false), icFeatures=IncrementalCompilationFeatures(usePreciseJavaTracking=true, withAbiSnapshot=false, preciseCompilationResultsBackup=true, keepIncrementalCompilationCachesInMemory=true, enableUnsafeIncrementalCompilationForMultiplatform=false, enableMonotonousIncrementalCompileSetExpansion=true), outputFiles=[C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\kotlin\main, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state])
Finished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key 7f228e48493365f524f4a728a1060f0a
Resolve mutations for :compileJava (Thread[#1708,Execution worker,5,main]) started.
:compileJava (Thread[#1708,Execution worker,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#1709,Execution worker Thread 2,5,main]) started.
:classes (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :instrumentCode (Thread[#1709,Execution worker Thread 2,5,main]) started.
:instrumentCode (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :instrumentCode SKIPPED
Skipping task ':instrumentCode' as task onlyIf 'Task is enabled' is false.
Resolve mutations for :jar (Thread[#1709,Execution worker Thread 2,5,main]) started.
:jar (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Not worth caching
Task ':jar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\java\main', not found
Resolve mutations for :composedJar (Thread[#1709,Execution worker Thread 2,5,main]) started.
:composedJar (Thread[#1716,Execution worker Thread 9,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is eac2bddecd42da0a36c30bafe0e04408
Task ':composedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key eac2bddecd42da0a36c30bafe0e04408
Resolve mutations for :prepareSandbox (Thread[#1716,Execution worker Thread 9,5,main]) started.
:prepareSandbox (Thread[#1716,Execution worker Thread 9,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
  Not worth caching
Task ':prepareSandbox' is not up-to-date because:
  Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2026.1\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2026.1\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2026.1\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2026.1\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#1716,Execution worker Thread 9,5,main]) started.
:buildSearchableOptions (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :buildSearchableOptions SKIPPED
Skipping task ':buildSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :prepareJarSearchableOptions (Thread[#1709,Execution worker Thread 2,5,main]) started.
:prepareJarSearchableOptions (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :prepareJarSearchableOptions SKIPPED
Skipping task ':prepareJarSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :jarSearchableOptions (Thread[#1709,Execution worker Thread 2,5,main]) started.
:jarSearchableOptions (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :jarSearchableOptions SKIPPED
Skipping task ':jarSearchableOptions' as task onlyIf 'Task satisfies onlyIf spec' is false.
Resolve mutations for :buildPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.
:buildPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
  Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
  Executed with '--rerun-tasks'.
Resolve mutations for :verifyPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.
:verifyPlugin (Thread[#1709,Execution worker Thread 2,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
  Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.405\ff594280b5bcae48d0e09a2b78066ee019e50081\verifier-cli-1.405-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/pluginVerifier -runtime-dir C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/eb402e9c13426d10e082fb4ddc8ed52f/transformed/idea-2026.1-win/jbr -subsystems-to-check all -verification-reports-formats plain,html C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/distributions/WindowAccent-1.0.5.zip C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\8b502a37c202834833c6c65d8c298fe9\transformed\idea-2026.1.2-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.405
2026-05-22T10:46:02 [main] INFO  c.j.p.options.OptionsParser - The verification directory C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier is being deleted because it is not empty.
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier
2026-05-22T10:46:06 [main] INFO  verification - Reading IDE C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\8b502a37c202834833c6c65d8c298fe9\transformed\idea-2026.1.2-win
2026-05-22T10:46:06 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\8b502a37c202834833c6c65d8c298fe9\transformed\idea-2026.1.2-win
2026-05-22T10:46:06 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\eb402e9c13426d10e082fb4ddc8ed52f\transformed\idea-2026.1-win\jbr
2026-05-22T10:46:06 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'intellij.android.gradle.declarative.lang' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.jar'
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
2026-05-22T10:46:07 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\distributions\WindowAccent-1.0.5.zip
2026-05-22T10:46:12 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:1.0.5 against IU-261.24374.151

2026-05-22T10:50:23 [main] INFO  verification - Finished 1 of 1 verifications (in 251,6 s): IU-261.24374.151 against WindowAccent:1.0.5: Compatible
Plugin WindowAccent:1.0.5 against IU-261.24374.151: Compatible
Dynamic Plugin Eligibility:
    Plugin can probably be enabled or disabled without IDE restart

2026-05-22T10:50:24 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 0 ms
2026-05-22T10:50:24 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 0 B
2026-05-22T10:50:24 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,95 GB
2026-05-22T10:50:24 [main] INFO  verification - Verification reports for WindowAccent:1.0.5 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier\IU-261.24374.151
2026-05-22T10:50:24 [main] INFO  verification - Total time spent in plugin verification: 4 m 17 s 917 ms
Build 38aca8a2-e062-4fb4-bd63-891688e757ec is started
Build 38aca8a2-e062-4fb4-bd63-891688e757ec is closed

See the complete report at file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/configuration-cache/h54o76ni36nhctwkb5zfwgmi/15wy4c0hk5jvm1jz02vr7rpv7/configuration-cache-report.html

BUILD SUCCESSFUL in 4m 38s
10 actionable tasks: 10 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent]
Configuration cache entry stored.
Configuration Cache (C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache) [subdir: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache\40fcf85b-1ec2-48b1-9735-6feff614b2fe] cleanup deleted 8 files/directories.
```