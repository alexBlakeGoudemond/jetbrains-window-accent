Command used to generate the results: `./gradlew verifyPlugin --rerun-tasks --info`

```markdown
$ ./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alexander.goudemond\.gradle\native
Initialized jansi services in: C:\Users\alexander.goudemond\.gradle\native
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
The client will now receive all logging from the daemon (pid: 26060). The daemon log file: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0\daemon-26060.out.log
Starting 3rd build in daemon [uptime: 2 mins 58.483 secs, performance: 99%, GC rate: 0.00/s, heap usage: 0% of 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\fileHashes\fileHashes.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\fileHashes\resourceHashesCache.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\md5-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha1-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha256-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha512-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\buildOutputCleanup\outputFiles.bin
Encryption key source: default Gradle keystore (pkcs12)
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\journal-1\file-access.bin
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\kotlinDslCompileAvoidanceClasspathHashCache.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\fileHashes.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\KotlinMetadataCompatibilityCache.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\resourceHashesCache.bin
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/jbr/bin/java.exe -XshowSettings:properties -version
Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/jbr/bin/java.exe''
Reusing configuration cache.
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\md-rule\md-rule.bin
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':instrumentCode', task ':jar', task ':instrumentedJar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#170,Execution worker Thread 2,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#171,Execution worker Thread 3,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#171,Execution worker Thread 3,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#170,Execution worker Thread 2,5,main]) started.
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\executionHistory\executionHistory.bin

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#171,Execution worker Thread 3,5,main]) started.
Build 76f31683-001c-47bd-992e-3cbfbebb54e0 is started
:compileKotlin (Thread[#171,Execution worker Thread 3,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
  Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Resolve mutations for :patchPluginXml (Thread[#170,Execution worker Thread 2,5,main]) started.
Resolve mutations for :generateManifest (Thread[#172,Execution worker Thread 4,5,main]) started.
:generateManifest (Thread[#172,Execution worker Thread 4,5,main]) started.
:patchPluginXml (Thread[#168,included builds,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 04100bd84e755e479e38bd63b2c27c2f
Task ':generateManifest' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 04100bd84e755e479e38bd63b2c27c2f

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is 2e2f19678a2a54c5fda8818f39bd280c
Task ':patchPluginXml' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key 2e2f19678a2a54c5fda8818f39bd280c
Resolve mutations for :processResources (Thread[#168,included builds,5,main]) started.
:processResources (Thread[#168,included builds,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
  Not worth caching
Task ':processResources' is not up-to-date because:
  Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for task ':compileKotlin' is a6d4e4a4a413f4749d2579c7f9e84645
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
i: starting the daemon as: C:\myprograms\java\jdk\jdk21.0.7\bin\java -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar -Djava.awt.headless=true -Djava.rmi.server.hostname=127.0.0.1 -Xmx8112m -XX:ReservedCodeCacheSize=320m -Dkotlin.environment.keepalive -ea -XX:+UseCodeCacheFlushing -XX:+UseParallelGC -Dkotlin.daemon.initiator.marker.file=C:\Users\ALEXAN~1.GOU\AppData\Local\Temp\kotlin-compiler-in-indowccent-8279337428804068418.alive --add-exports java.base/sun.nio.ch=ALL-UNNAMED org.jetbrains.kotlin.daemon.KotlinCompileDaemon --daemon-runFilesPath C:\Users\alexander.goudemond\AppData\Local\kotlin\daemon --daemon-autoshutdownIdleSeconds=7200 --daemon-compilerClasspath C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar
i: #1 retrying connecting to the daemon
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@6c38f7e2, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state\build-history.bin, useModuleDetection=false), icFeatures=IncrementalCompilationFeatures(usePreciseJavaTracking=true, withAbiSnapshot=false, preciseCompilationResultsBackup=true, keepIncrementalCompilationCachesInMemory=true, enableUnsafeIncrementalCompilationForMultiplatform=false, enableMonotonousIncrementalCompileSetExpansion=true), outputFiles=[C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\kotlin\main, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state])
Finished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key a6d4e4a4a413f4749d2579c7f9e84645
Resolve mutations for :compileJava (Thread[#171,Execution worker Thread 3,5,main]) started.
:compileJava (Thread[#168,included builds,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#168,included builds,5,main]) started.
:classes (Thread[#168,included builds,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :instrumentCode (Thread[#168,included builds,5,main]) started.
:instrumentCode (Thread[#168,included builds,5,main]) started.

> Task :instrumentCode SKIPPED
Skipping task ':instrumentCode' as task onlyIf 'Task is enabled' is false.
Resolve mutations for :jar (Thread[#168,included builds,5,main]) started.
:jar (Thread[#168,included builds,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Not worth caching
Task ':jar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\java\main', not found
Resolve mutations for :instrumentedJar (Thread[#168,included builds,5,main]) started.
:instrumentedJar (Thread[#171,Execution worker Thread 3,5,main]) started.

> Task :instrumentedJar
Build cache key for task ':instrumentedJar' is 9b60f9babe3cacaac17cfea0d83f8ff7
Task ':instrumentedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\instrumented\instrumentCode', not found
Stored cache entry for task ':instrumentedJar' with cache key 9b60f9babe3cacaac17cfea0d83f8ff7
Resolve mutations for :composedJar (Thread[#171,Execution worker Thread 3,5,main]) started.
:composedJar (Thread[#171,Execution worker Thread 3,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is af98d8abf5009258c6580ea53a8fdc40
Task ':composedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key af98d8abf5009258c6580ea53a8fdc40
Resolve mutations for :prepareSandbox (Thread[#171,Execution worker Thread 3,5,main]) started.
:prepareSandbox (Thread[#171,Execution worker Thread 3,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
  Not worth caching
Task ':prepareSandbox' is not up-to-date because:
  Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2024.3\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2024.3\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2024.3\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2024.3\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#171,Execution worker Thread 3,5,main]) started.
:buildSearchableOptions (Thread[#168,included builds,5,main]) started.

> Task :buildSearchableOptions
Build cache key for task ':buildSearchableOptions' is 29cf9e5ea7d3ba13655c600a1255d53b
Task ':buildSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Starting process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\jbr\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win Command: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\jbr\bin\java.exe -Daether.connector.resumeDownloads=false -Dcompose.swing.render.on.graphics=true -Dide.native.launcher=false -Didea.config.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2024.3/config -Didea.l10n.keys=only -Didea.log.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2024.3/log -Didea.paths.selector=IntelliJIdea2024.3 -Didea.plugins.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2024.3/plugins -Didea.required.plugins.id=WindowAccent -Didea.system.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2024.3/system -Didea.vendor.name=JetBrains -Dintellij.platform.runtime.repository.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/modules/module-descriptors.jar -Djava.nio.file.spi.DefaultFileSystemProvider=com.intellij.platform.core.nio.fs.MultiRoutingFileSystemProvider -Djava.security.manager=com.intellij.platform.core.nio.fs.CoreBootstrapSecurityManager -Djava.system.class.loader=com.intellij.util.lang.PathClassLoader -Djava.util.zip.use.nio.for.zip.file.access=true -Djbr.catch.SIGABRT=true -Djdk.attach.allowAttachSelf=true -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.module.illegalAccess.silent=true -Djdk.nio.maxCachedBufferSize=2097152 -Djna.boot.library.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/lib/jna/amd64 -Djna.noclasspath=true -Djna.nosys=true -Dplugin.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2024.3/plugins/WindowAccent -Dpty4j.preferred.native.folder=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/lib/pty4j -Dsplash=true -Dsun.io.useCanonCaches=false -Dsun.java2d.metal=true -Dwsl.use.remote.agent.for.nio.filesystem=true -XX:ReservedCodeCacheSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -XX:CICompilerCount=2 -XX:+IgnoreUnrecognizedVMOptions -XX:+UnlockDiagnosticVMOptions -XX:TieredOldPercentage=100000 -javaagent:C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\coroutines-javaagent-legacy.jar --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.ref=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED --add-opens=java.base/sun.net.dns=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.fs=ALL-UNNAMED --add-opens=java.base/sun.security.ssl=ALL-UNNAMED --add-opens=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.desktop/com.sun.java.swing=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED --add-opens=java.desktop/java.awt.event=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED --add-opens=java.desktop/java.awt.image=ALL-UNNAMED --add-opens=java.desktop/java.awt.peer=ALL-UNNAMED --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED --add-opens=java.desktop/sun.font=ALL-UNNAMED --add-opens=java.desktop/sun.java2d=ALL-UNNAMED --add-opens=java.desktop/sun.swing=ALL-UNNAMED --add-opens=java.management/sun.management=ALL-UNNAMED --add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED -Xms128m -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -ea -cp C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\platform-loader.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\util-8.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\util.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\app-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\util_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\product.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\opentelemetry.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\app.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\product-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\lib-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\stats.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\jps-model.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\external-system-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\rd.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\bouncy-castle.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\protobuf.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\intellij-test-discovery.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\forms_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\lib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\externalProcess-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\groovy.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\annotations.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\idea_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\jsch-agent.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\kotlinx-coroutines-slf4j-1.8.0-intellij.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\nio-fs.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\lib\trove.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\jbr\lib\tools com.intellij.idea.Main traverseUI C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/tmp/buildSearchableOptions true
Successfully started process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win\jbr\bin\java.exe''
[0.011s][warning][cds] Archived non-system classes are disabled because the java.system.class.loader property is specified (value = "com.intellij.util.lang.PathClassLoader"). To use archived non-system classes, this property must not be set
WARNING: A command line option has enabled the Security Manager
WARNING: The Security Manager is deprecated and will be removed in a future release
WARNING: A terminally deprecated method in java.lang.System has been called
WARNING: System::setSecurityManager has been called by com.intellij.platform.core.nio.fs.CoreBootstrapSecurityManager (file:/C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/lib/nio-fs.jar)
WARNING: Please consider reporting this to the maintainers of com.intellij.platform.core.nio.fs.CoreBootstrapSecurityManager
WARNING: System::setSecurityManager will be removed in a future release
2026-05-22 10:17:00,636 [    412]   WARN - c.i.o.u.r.overrides - Conflicting registry key definition for key kotlin.mpp.tests.force.gradle: it was defined by plugin org.jetbrains.kotlin but redefined by plugin org.jetbrains.kotlin. Consider adding overrides="true" for one of the plugins, see the documentation for com.intellij.openapi.util.registry.RegistryKeyBean.overrides for more details.
2026-05-22 10:17:07,330 [   7106]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.codeInsight.template.postfix.settings.PostfixTemplatesConfigurable.<init>(PostfixTemplatesConfi2026-05-22 10:17:07,711 [   7487]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.codeInsight.intention.impl.config.IntentionSettingsPanel.<init>(IntentionSettingsPanel.java:32)
2026-05-22 10:17:07,743 [   7519]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.codeInsight.template.impl.TemplateListPanel.<init>(TemplateListPanel.java:79)
2026-05-22 10:17:08,596 [   8372]   WARN - #c.i.i.u.UIThemeBean - Unknown field: CombinedDiff
2026-05-22 10:17:08,597 [   8373]   WARN - #c.i.i.u.UIThemeBean - Unknown field: BlockBorder
2026-05-22 10:17:15,165 [  14941]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3317ad99, GRID_HELPER_KEY=com.2026-05-22 10:17:15,388 [  15164]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3317ad99, GRID_HELPER_KEY=com.2026-05-22 10:17:15,487 [  15263]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3317ad99, GRID_HELPER_KEY=com.2026-05-22 10:17:15,576 [  15352]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3e4b05e, GRID_HELPER_KEY=com.intellij.database.datagrid.GridHelperImpl@7bd7d224, FACTORY_PROVIDER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorFactoryImpl@7a06f028, RENDERER_FACTORIES_KEY=com.intellij.database.run.ui.grid.renderers.GridCellRendererFactories@78157531, FORMATTER_CREATOR_KEY=com.intellij.database.datagrid.FormatterCreatorProvider$$Lambda/0x0000029e63668928@29af7608, CONVERSION_GRAPH_KEY=com.intellij.database.data.types.BaseConversionGraph@fef4523}.Make sure DATA_GRID_SETTINGS_KEY set for your grid.TableResultPanel inheritors could use 'configurator' constructor parameter to pass settings
2026-05-22 10:17:15,596 [  15372]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3e4b05e, GRID_HELPER_KEY=com.intellij.database.datagrid.GridHelperImpl@7bd7d224, FACTORY_PROVIDER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorFactoryImpl@7a06f028, RENDERER_FACTORIES_KEY=com.intellij.database.run.ui.grid.renderers.GridCellRendererFactories@78157531, FORMATTER_CREATOR_KEY=com.intellij.database.datagrid.FormatterCreatorProvider$$Lambda/0x0000029e63668928@29af7608, CONVERSION_GRAPH_KEY=com.intellij.database.data.types.BaseConversionGraph@fef4523}.Make sure DATA_GRID_SETTINGS_KEY set for your grid.TableResultPanel inheritors could use 'configurator' constructor parameter to pass settings
2026-05-22 10:17:15,599 [  15375]   WARN - #c.i.d.d.GridUtil - No settings for grid {GRID_CELL_EDITOR_HELPER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorHelperImpl@3e4b05e, GRID_HELPER_KEY=com.intellij.database.datagrid.GridHelperImpl@7bd7d224, FACTORY_PROVIDER_KEY=com.intellij.database.run.ui.grid.editors.GridCellEditorFactoryImpl@7a06f028, RENDERER_FACTORIES_KEY=com.intellij.database.run.ui.grid.renderers.GridCellRendererFactories@78157531, FORMATTER_CREATOR_KEY=com.intellij.database.datagrid.FormatterCreatorProvider$$Lambda/0x0000029e63668928@29af7608, CONVERSION_GRAPH_KEY=com.intellij.database.data.type2026-05-22 10:17:20,813 [  20589]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:20,850 [  20626]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:20,926 [  20702]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:20,939 [  20715]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:20,972 [  20748]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,103 [  20879]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(Arra2026-05-22 10:17:21,459 [  21235]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,462 [  21238]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,475 [  21251]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,605 [  21381]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,633 [  21409]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,634 [  21410]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:21,658 [  21434]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:21,665 [  21441]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(Arra2026-05-22 10:17:22,106 [  21882]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,127 [  21903]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,128 [  21904]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,143 [  21919]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:22,152 [  21928]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:22,320 [  22096]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,326 [  22102]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:22,329 [  22105]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:22,340 [  22116]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,437 [  22213]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,515 [  22291]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,522 [  22298]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:22,522 [  22298]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(Arra2026-05-22 10:17:22,609 [  22385]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,610 [  22386]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,614 [  22390]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,753 [  22529]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,832 [  22608]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,891 [  22667]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:22,895 [  22671]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,039 [  22815]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,044 [  22820]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,274 [  23050]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,282 [  23058]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:23,282 [  23058]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(Arra2026-05-22 10:17:23,397 [  23173]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,426 [  23202]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,430 [  23206]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,432 [  23208]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:23,908 [  23684]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:24,052 [  23828]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:24,230 [  24006]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:24,592 [  24368]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:24,778 [  24554]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:25,029 [  24805]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:25,519 [  25295]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:25,792 [  25568]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:26,196 [  25972]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:26,530 [  26306]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:26,712 [  26488]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:26,948 [  26724]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:27,029 [  26805]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:27,034 [  26810]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:27,035 [  26811]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.codeStyle.arrangement.component.ArrangementTextFieldUiComponent.<init>(ArrangementTextFieldUiComponent.java:20)
2026-05-22 10:17:27,042 [  26818]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:27,079 [  26855]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.application.options.CodeStyleAbstractPanel.<init>(CodeStyleAbstractPanel.java:106)
2026-05-22 10:17:27,974 [  27750]   WARN - #c.i.u.Alarm - Do not create alarm without coroutineScope: com.intellij.openapi.ui.cellvalidators.CellTooltipManager.<init>(CellTooltipManager.java:36)
2026-05-22 10:17:29,688 [  29464]   WARN - #c.i.u.j.JBCefApp - JCEF is manually disabled in headless env via 'ide.browser.jcef.headless.enabled=false'
2026-05-22 10:17:31,791 [  31567]   WARN - #c.i.o.u.BrowseFolderRunnable - multiple selection not supported
Found 381 configurables
save to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\tmp\buildSearchableOptions
Stored cache entry for task ':buildSearchableOptions' with cache key 29cf9e5ea7d3ba13655c600a1255d53b
Resolve mutations for :prepareJarSearchableOptions (Thread[#168,included builds,5,main]) started.
:prepareJarSearchableOptions (Thread[#168,included builds,5,main]) started.

> Task :prepareJarSearchableOptions
Build cache key for task ':prepareJarSearchableOptions' is 7caa827e503d1917b3fa761ed740cb39
Task ':prepareJarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':prepareJarSearchableOptions' with cache key 7caa827e503d1917b3fa761ed740cb39
Resolve mutations for :jarSearchableOptions (Thread[#168,included builds,5,main]) started.
:jarSearchableOptions (Thread[#168,included builds,5,main]) started.

> Task :jarSearchableOptions
Build cache key for task ':jarSearchableOptions' is b9d2c866fc9b86b9a02d0902f047273b
Task ':jarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':jarSearchableOptions' with cache key b9d2c866fc9b86b9a02d0902f047273b
Resolve mutations for :buildPlugin (Thread[#168,included builds,5,main]) started.
:buildPlugin (Thread[#168,included builds,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
  Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
  Executed with '--rerun-tasks'.
Resolve mutations for :verifyPlugin (Thread[#168,included builds,5,main]) started.
:verifyPlugin (Thread[#168,included builds,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
  Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.405\ff594280b5bcae48d0e09a2b78066ee019e50081\verifier-cli-1.405-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/pluginVerifier -runtime-dir C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/4ab025371cc8bfbc159d0ca6292c2e21/transformed/ideaIU-2024.3-win/jbr -subsystems-to-check all -verification-reports-formats plain,html C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/distributions/WindowAccent-1.0.5.zip C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dbaa62b6634acfba7f5e5abfc318b7cb\transformed\ideaIU-2024.3.7.1-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.405
2026-05-22T10:17:40 [main] INFO  c.j.p.options.OptionsParser - The verification directory C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier is being deleted because it is not empty.
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier
2026-05-22T10:17:44 [main] INFO  verification - Reading IDE C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dbaa62b6634acfba7f5e5abfc318b7cb\transformed\ideaIU-2024.3.7.1-win
2026-05-22T10:17:44 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dbaa62b6634acfba7f5e5abfc318b7cb\transformed\ideaIU-2024.3.7.1-win
2026-05-22T10:17:44 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\4ab025371cc8bfbc159d0ca6292c2e21\transformed\ideaIU-2024.3-win2026-05-22T10:17:44 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'org.jetbrains.plugins.emojipicker' has some nonexistent 'classPath' elements: 'plugins\emojipicker\lib\emojipi2026-05-22T10:17:45 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\distributions\WindowAccent-1.0.5.zip
2026-05-22T10:17:49 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:1.0.5 against IU-243.28141.41

2026-05-22T10:18:32 [main] INFO  verification - Finished 1 of 1 verifications (in 42,3 s): IU-243.28141.41 against WindowAccent:1.0.5: 1 compatibility problem
Plugin WindowAccent:1.0.5 against IU-243.28141.41: 1 compatibility problem
Compatibility problems (1):
    #Access to unresolved class kotlin.coroutines.jvm.internal.SpillingKt
        Method com.window_accent.feature.window_color.WindowColorApplier.applyColorToWindow$1.invokeSuspend(Object $result) : Object references an unresolved class kotlin.coroutines.jvm.internal.SpillingKt. This can lead to **NoSuchClassError** exception at runtime.
Dynamic Plugin Eligibility:
    Plugin can probably be enabled or disabled without IDE restart

2026-05-22T10:18:32 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 0 ms
2026-05-22T10:18:32 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 0 B
2026-05-22T10:18:32 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,95 GB
2026-05-22T10:18:32 [main] INFO  verification - Verification reports for WindowAccent:1.0.5 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier\IU-243.28141.41
2026-05-22T10:18:32 [main] INFO  verification - Total time spent in plugin verification: 48 s 53 ms

> Task :verifyPlugin FAILED
Build 76f31683-001c-47bd-992e-3cbfbebb54e0 is closed

[Incubating] Problems report is available at: file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':verifyPlugin' (registered by plugin 'org.jetbrains.intellij.platform').
> Verification failed with [COMPATIBILITY_PROBLEMS] problems. See the report at: file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/problems/problems-report.html

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.

BUILD FAILED in 1m 58s
14 actionable tasks: 14 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent]
Configuration cache entry reused.

```