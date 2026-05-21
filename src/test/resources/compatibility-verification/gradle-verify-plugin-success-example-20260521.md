```java
$  ./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alexander.goudemond\.gradle\native
Initialized jansi services in: C:\Users\alexander.goudemond\.gradle\native
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
Found daemon DaemonInfo{pid=27164, address=[3a3178be-4322-494a-b01d-ee36be3c950a port:51477, addresses:[/127.0.0.1]], state=Idle, lastBusy=1779334756487, context=DefaultDaemonContext[uid=ba579a20-bd3e-4790-9ac6-5a1da5c92954,javaHome=C:\myprograms\java\jdk\jdk21.0.7,javaVersion=21,javaVendor=Microsoft,daemonRegistryDir=C:\Users\alexander.goudemond\.gradle\daemon,pid=27164,idleTimeout=10800000,priority=NORMAL,applyInstrumentationAgent=true,nativeServicesMode=ENABLED,daemonOpts=-XX:MaxMetaspaceSize=384m,-XX:+HeapDumpOnOutOfMemoryError,-Xms256m,-Xmx512m,-Dfile.encoding=UTF-8,-Duser.country=ZA,-Duser.language=en,-Duser.variant]} however its context does not match the desired criteria.
At least one daemon option is different.
Wanted: DaemonRequestContext{jvmCriteria=C:\myprograms\java\jdk\jdk21.0.7 (no Daemon JVM specified, using current Java home), daemonOpts=[--add-opens=java.base/java.lang=ALL-UNNAMED, -Dfile.encoding=UTF-8, -Duser.country=ZA, -Duser.language=en, -Duser.variant], applyInstrumentationAgent=true, nativeServicesMode=ENABLED, priority=NORMAL}
Actual: DefaultDaemonContext[uid=ba579a20-bd3e-4790-9ac6-5a1da5c92954,javaHome=C:\myprograms\java\jdk\jdk21.0.7,javaVersion=21,javaVendor=Microsoft,daemonRegistryDir=C:\Users\alexander.goudemond\.gradle\daemon,pid=27164,idleTimeout=10800000,priority=NORMAL,applyInstrumentationAgent=true,nativeServicesMode=ENABLED,daemonOpts=-XX:MaxMetaspaceSize=384m,-XX:+HeapDumpOnOutOfMemoryError,-Xms256m,-Xmx512m,-Dfile.encoding=UTF-8,-Duser.country=ZA,-Duser.language=en,-Duser.variant]

  Looking for a different daemon...
The client will now receive all logging from the daemon (pid: 7520). The daemon log file: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0\daemon-7520.out.log
Starting 7th build in daemon [uptime: 12 mins 48.206 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Encryption key source: default Gradle keystore (pkcs12)
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel] as hierarchies to watch
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/jbr/bin/java.exe -XshowSettings:properties -version
Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/jbr/bin/java.exe''
Reusing configuration cache.
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':instrumentCode', task ':jar', task ':instrumentedJar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#397,Execution worker,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#399,Execution worker Thread 3,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#399,Execution worker Thread 3,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#400,Execution worker Thread 4,5,main]) started.

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#399,Execution worker Thread 3,5,main]) started.
Build 7fec5c3d-f178-4772-8119-5bba0f8e3ddb is started
:compileKotlin (Thread[#399,Execution worker Thread 3,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
  Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Resolve mutations for :generateManifest (Thread[#403,Execution worker Thread 7,5,main]) started.
:generateManifest (Thread[#403,Execution worker Thread 7,5,main]) started.
Resolve mutations for :patchPluginXml (Thread[#400,Execution worker Thread 4,5,main]) started.
:patchPluginXml (Thread[#400,Execution worker Thread 4,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 6444d598696b88b04c621ecd968c91ef
Task ':generateManifest' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 6444d598696b88b04c621ecd968c91ef

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is ce1d8fab5bdf751c34208404b8d2a70b
Task ':patchPluginXml' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key ce1d8fab5bdf751c34208404b8d2a70b
Resolve mutations for :processResources (Thread[#400,Execution worker Thread 4,5,main]) started.
:processResources (Thread[#400,Execution worker Thread 4,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
  Not worth caching
Task ':processResources' is not up-to-date because:
  Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for task ':compileKotlin' is 29075780fce375a7a446a4df21a55fcc
Task ':compileKotlin' is not up-to-date because:
  Executed with '--rerun-tasks'.
The input changes require a full rebuild for incremental task ':compileKotlin'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\java', not found
Kotlin source files: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\persistence\WindowCustomColorStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\persistence\WindowPanelAppearanceStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\persistence\WindowTitleNumberingStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\settings\ColorMagnifier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\settings\ScreenColorPicker.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\settings\WindowColorPanelSettings.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\configuration\tool_window\WindowColorPanelToolWindowFactory.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\feature\window_color\WindowColorApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\feature\window_title\WindowTitleApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\i18n\MyMessageBundle.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\src\main\kotlin\com\window_color_panel\PluginStartupActivity.kt
Java source files:
Script source files:
Script file extensions:
Using Kotlin/JVM incremental compilation
[KOTLIN] Kotlin compilation 'jdkHome' argument: C:\myprograms\java\jdk\jdk21.0.7
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@643e5751, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\kotlin\compileKotlin\local-state\build-history.bin, useModuleDetection=false), icFeatures=IncrementalCompilationFeatures(usePreciseJavaTracking=true, withAbiSnapshot=false, preciseCompilationResultsBackup=true, keepIncrementalCompilationCachesInMemory=true, enableUnsafeIncrementalCompilationForMultiplatform=false, enableMonotonousIncrementalCompileSetExpansion=true), outputFiles=[C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\classes\kotlin\main, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\kotlin\compileKotlin\cacheable, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\kotlin\compileKotlin\local-state])
Finished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key 29075780fce375a7a446a4df21a55fcc
Resolve mutations for :compileJava (Thread[#399,Execution worker Thread 3,5,main]) started.
:compileJava (Thread[#399,Execution worker Thread 3,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#399,Execution worker Thread 3,5,main]) started.
:classes (Thread[#399,Execution worker Thread 3,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :instrumentCode (Thread[#410,Execution worker Thread 14,5,main]) started.
:instrumentCode (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :instrumentCode SKIPPED
Skipping task ':instrumentCode' as task onlyIf 'Task is enabled' is false.
Resolve mutations for :jar (Thread[#410,Execution worker Thread 14,5,main]) started.
:jar (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Not worth caching
Task ':jar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\classes\java\main', not found
Resolve mutations for :instrumentedJar (Thread[#410,Execution worker Thread 14,5,main]) started.
:instrumentedJar (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :instrumentedJar
Build cache key for task ':instrumentedJar' is ba843b989f6231704f71086bbafd142c
Task ':instrumentedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\instrumented\instrumentCode', not found
Stored cache entry for task ':instrumentedJar' with cache key ba843b989f6231704f71086bbafd142c
Resolve mutations for :composedJar (Thread[#410,Execution worker Thread 14,5,main]) started.
:composedJar (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is 7a406efbc1f580c36ef4cd648db8b2da
Task ':composedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key 7a406efbc1f580c36ef4cd648db8b2da
Resolve mutations for :prepareSandbox (Thread[#410,Execution worker Thread 14,5,main]) started.
:prepareSandbox (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
  Not worth caching
Task ':prepareSandbox' is not up-to-date because:
  Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\idea-sandbox\IU-2025.3\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\idea-sandbox\IU-2025.3\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\idea-sandbox\IU-2025.3\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\idea-sandbox\IU-2025.3\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.
:buildSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :buildSearchableOptions
Build cache key for task ':buildSearchableOptions' is 94daa0fc747c2b2dce9ab13283dc59f8
Task ':buildSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Starting process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\jbr\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win Command: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\jbr\bin\java.exe -Daether.connector.resumeDownloads=false -Dcompose.swing.render.on.graphics=true -Dide.native.launcher=false -Didea.config.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/idea-sandbox/IU-2025.3/config -Didea.l10n.keys=only -Didea.log.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/idea-sandbox/IU-2025.3/log -Didea.paths.selector=IntelliJIdea2025.3 -Didea.plugins.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/idea-sandbox/IU-2025.3/plugins -Didea.required.plugins.id=WindowColorPanel -Didea.system.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/idea-sandbox/IU-2025.3/system -Didea.vendor.name=JetBrains -Dintellij.platform.runtime.repository.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/modules/module-descriptors.dat -Dio.netty.allocator.type=pooled -Djava.nio.file.spi.DefaultFileSystemProvider=com.intellij.platform.core.nio.fs.MultiRoutingFileSystemProvider -Djava.system.class.loader=com.intellij.util.lang.PathClassLoader -Djava.util.zip.use.nio.for.zip.file.access=true -Djbr.catch.SIGABRT=true -Djdk.attach.allowAttachSelf=true -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.module.illegalAccess.silent=true -Djdk.nio.maxCachedBufferSize=2097152 -Djna.boot.library.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/lib/jna/amd64 -Djna.noclasspath=true -Djna.nosys=true -Dplugin.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/idea-sandbox/IU-2025.3/plugins/WindowColorPanel -Dpty4j.preferred.native.folder=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/lib/pty4j -Dsplash=true -Dsun.io.useCanonCaches=false -Dsun.java2d.metal=true -Dwsl.use.remote.agent.for.nio.filesystem=true -XX:JbrShrinkingGcMaxHeapFreeRatio=40 -XX:ReservedCodeCacheSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -XX:CICompilerCount=2 -XX:+IgnoreUnrecognizedVMOptions -XX:+UnlockDiagnosticVMOptions -XX:TieredOldPercentage=100000 -javaagent:C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\.intellijPlatform\coroutines-javaagent.jar -Xbootclasspath/a:C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win\lib\nio-fs.jar --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.ref=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED --add-opens=java.base/sun.net.dns=ALL-UNNAMED --add-opens=java.base/sun.nio=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.fs=ALL-UNNAMED --add-opens=java.base/sun.security.ssl=ALL-UNNAMED --add-opens=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.desktop/com.sun.java.swing=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED --add-opens=java.desktop/java.awt.event=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED --add-opens=java.desktop/java.awt.image=ALL-UNNAMED --add-opens=java.desktop/java.awt.peer=ALL-UNNAMED --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html.parser=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED --add-opens=java.desktop/sun.font=ALL-UNNAMED --add-opens=java.desktop/sun.java2d=ALL-UNNAMED --add-opens=java.desktop/sun.swing=ALL-UNNAMED --add-opens=java.management/sun.management=ALL-UNNAMED --add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED -Xms128m -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -ea -cp C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\platform-loader.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\util-8.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\app.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\util.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\product-backend.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\app-backend.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\annotations.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\eclipse.lsp4j.debug.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\eclipse.lsp4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\eclipse.lsp4j.jsonrpc.debug.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\eclipse.lsp4j.jsonrpc.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\external-system-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\externalProcess-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\forms_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\groovy.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\idea_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\intellij-test-discovery.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\javax.activation.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\javax.annotation-api.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\jaxb-api.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\jaxb-runtime.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\jps-model.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\jsch-agent.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\lib-backend.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\lib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.aalto.xml.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.asm.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.asm.tools.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.automaton.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.blockmap.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.bouncy.castle.pgp.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.bouncy.castle.provider.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.caffeine.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.cglib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.classgraph.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.cli.parser.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.cli.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.codec.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.compress.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.imaging.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.io.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.lang3.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.commons.logging.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.fastutil.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.gson.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.guava.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.hash4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.hdr.histogram.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.http.client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.icu4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.imgscalr.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ini4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ion.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jackson.databind.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jackson.dataformat.yaml.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jackson.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jackson.jr.objects.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jackson.module.kotlin.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.java.websocket.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.javax.annotation.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jaxen.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jbr.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jcef.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jcip.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jediterm.core.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jediterm.ui.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jettison.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jsonpath.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jsoup.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jsvg.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jvm.native.trusted.roots.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.jzlib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlin.reflect.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.collections.immutable.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.coroutines.slf4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.datetime.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.html.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.io.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.serialization.core.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.serialization.json.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kotlinx.serialization.protobuf.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.kryo5.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ktor.client.cio.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ktor.client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ktor.io.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ktor.network.tls.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.ktor.utils.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.lz4.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.markdown.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.miglayout.swing.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.mvstore.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.oro.matcher.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.proxy.vole.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.pty4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.rd.text.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.rhino.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.snakeyaml.engine.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.snakeyaml.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.sshj.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.stream.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.velocity.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.winp.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.xerces.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.xstream.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.xtext.xbase.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.libraries.xz.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.debugger.impl.rpc.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.debugger.impl.shared.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.eel.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.find.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.managed.cache.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.polySymbols.backend.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.polySymbols.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.rpc.topics.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.scopes.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.vcs.core.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.vcs.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.platform.vcs.shared.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.regexp.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.analysis.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.analysis.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.dom.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.dom.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.parser.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.psi.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.psi.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.structureView.impl.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.structureView.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.syntax.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\module-intellij.xml.ui.common.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\opentelemetry.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\product.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\protobuf.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\rd.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\rhino.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\stats.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\trove.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\lib\util_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\jbr\lib\tools com.intellij.idea.Main traverseUI C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/tmp/buildSearchableOptions true
Successfully started process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\jbr\bin\java.exe''
[0.008s][warning][cds] Archived non-system classes are disabled because the java.system.class.loader property is specified (value = "com.intellij.util.lang.PathClassLoader"). To use archived non-system classes, this property must not be set
2026-05-21 05:57:09,543 [   2018]   WARN - #c.i.o.e.c.i.EditorColorsManagerImpl - resource not found: colorSchemes/SqlDefault.xml
2026-05-21 05:57:09,543 [   2018]   WARN - #c.i.o.e.c.i.EditorColorsManagerImpl - resource not found: colorSchemes/sassDefault.xml
2026-05-21 05:57:09,544 [   2019]   WARN - #c.i.o.e.c.i.EditorColorsManagerImpl - resource not found: colorSchemes/SqlDarcula.xml
2026-05-21 05:57:12,455 [   4930]   WARN - #c.i.i.u.UIThemeBean - Unknown object: CombinedDiff
2026-05-21 05:57:12,456 [   4931]   WARN - #c.i.i.u.UIThemeBean - Unknown object: BlockBorder
2026-05-21 05:57:21,330 [  13805]   WARN - #c.i.u.j.JBCefApp - JCEF is manually disabled in headless env via 'ide.browser.jcef.headless.enabled=false'
2026-05-21 05:57:22,351 [  14826]   WARN - #c.i.o.u.BrowseFolderRunnable - multiple selection not supported
Found 391 configurables
save to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\tmp\buildSearchableOptions
2026-05-21 05:57:36,333 [  28808]   WARN - #c.i.i.shutdown - Application Application (headless) (command line) (RA allowed) (exit in progress): scope was not completed in 10s.

- "ApplicationImpl@1202288174 container":supervisor:ChildScope{Cancelling} [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor]
        - "(ApplicationImpl@1202288174 x tanvd.grazi)":supervisor:ChildScope{Cancelling} [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor]
                - "com.intellij.grazie.cloud.license.GrazieLoginManager":supervisor:ChildScope{Cancelling} [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor]
                        - "com.intellij.grazie.cloud.license.GrazieLoginManager#2041":StandaloneCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2041), Dispatchers.Default]
                                at kotlinx.coroutines.flow.internal.ChannelFlow$collect$2.invokeSuspend(ChannelFlow.kt:119)
                                at com.intellij.grazie.cloud.license.GrazieLoginManager$2.invokeSuspend(GrazieLoginManager.kt:86)
                                - "com.intellij.grazie.cloud.license.GrazieLoginManager#2045":ProducerCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2045), Dispatchers.Default]
                                        at java.base/jdk.internal.misc.Unsafe.park(Native Method)
                                        at java.base/java.util.concurrent.locks.LockSupport.park(LockSupport.java:221)
                                        at java.base/java.util.concurrent.CompletableFuture$Signaller.block(CompletableFuture.java:1864)
                                        at java.base/java.util.concurrent.ForkJoinPool.unmanagedBlock(ForkJoinPool.java:4013)
                                        at java.base/java.util.concurrent.ForkJoinPool.managedBlock(ForkJoinPool.java:3961)
                                        at java.base/java.util.concurrent.CompletableFuture.waitingGet(CompletableFuture.java:1898)
                                        at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2072)
                                        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
                                        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
                                        at com.intellij.ide.d.X.IA.X(IA.java:295)
                                        at com.intellij.d.U.getIdToken(U.java:45)
                                        at com.intellij.grazie.cloud.license.JbaToken$Companion.obtain(JbaToken.kt:8)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager.computeLoginState(GrazieLoginManager.kt:152)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager.computeLoginState$default(GrazieLoginManager.kt:144)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$2$1.invokeSuspend(GrazieLoginManager.kt:87)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$2$1.invoke(GrazieLoginManager.kt)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$2$1.invoke(GrazieLoginManager.kt)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invokeSuspend(Merge.kt:213)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invokeSuspend(Merge.kt:30)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invoke(Merge.kt)
                                        at kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(Undispatched.kt:20)
                                        at kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:360)
                                        at kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:134)
                                        at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch(Builders.common.kt:52)
                                        at kotlinx.coroutines.BuildersKt.launch(Unknown Source)
                                        at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(Builders.common.kt:43)
                                        at kotlinx.coroutines.BuildersKt.launch$default(Unknown Source)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1.emit(Merge.kt:29)
                                        at kotlinx.coroutines.flow.StateFlowImpl.collect(StateFlow.kt:406)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3.invokeSuspend(Merge.kt:23)
                                        at kotlinx.coroutines.flow.internal.ChannelFlow$collectToFun$1.invokeSuspend(ChannelFlow.kt:56)
                                        - "com.intellij.grazie.cloud.license.GrazieLoginManager#2047":StandaloneCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2047), Dispatchers.Default]
                        - "com.intellij.grazie.cloud.license.GrazieLoginManager#2042":StandaloneCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2042), Dispatchers.Default]
                                at kotlinx.coroutines.flow.internal.ChannelFlow$collect$2.invokeSuspend(ChannelFlow.kt:119)
                                at com.intellij.grazie.cloud.license.GrazieLoginManager$3.invokeSuspend(GrazieLoginManager.kt:95)
                                - "com.intellij.grazie.cloud.license.GrazieLoginManager#2159":ProducerCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2159), Dispatchers.Default]
                                        at java.base/jdk.internal.misc.Unsafe.park(Native Method)
                                        at java.base/java.util.concurrent.locks.LockSupport.park(LockSupport.java:221)
                                        at java.base/java.util.concurrent.CompletableFuture$Signaller.block(CompletableFuture.java:1864)
                                        at java.base/java.util.concurrent.ForkJoinPool.unmanagedBlock(ForkJoinPool.java:4013)
                                        at java.base/java.util.concurrent.ForkJoinPool.managedBlock(ForkJoinPool.java:3961)
                                        at java.base/java.util.concurrent.CompletableFuture.waitingGet(CompletableFuture.java:1898)
                                        at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2072)
                                        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
                                        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
                                        at com.intellij.ide.d.X.IA.X(IA.java:295)
                                        at com.intellij.d.U.getIdToken(U.java:45)
                                        at com.intellij.grazie.cloud.license.JbaToken$Companion.obtain(JbaToken.kt:8)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager.computeLoginState(GrazieLoginManager.kt:152)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager.computeLoginState$default(GrazieLoginManager.kt:144)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$3$1.invokeSuspend(GrazieLoginManager.kt:97)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$3$1.invoke(GrazieLoginManager.kt)
                                        at com.intellij.grazie.cloud.license.GrazieLoginManager$3$1.invoke(GrazieLoginManager.kt)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invokeSuspend(Merge.kt:213)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invokeSuspend(Merge.kt:30)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2.invoke(Merge.kt)
                                        at kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(Undispatched.kt:20)
                                        at kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:360)
                                        at kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:134)
                                        at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch(Builders.common.kt:52)
                                        at kotlinx.coroutines.BuildersKt.launch(Unknown Source)
                                        at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(Builders.common.kt:43)
                                        at kotlinx.coroutines.BuildersKt.launch$default(Unknown Source)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1.emit(Merge.kt:29)
                                        at kotlinx.coroutines.flow.internal.FlowValueWrapperInternalKt.emitInternal(FlowValueWrapperInternal.kt:39)
                                        at kotlinx.coroutines.flow.StateFlowImpl.collect(StateFlow.kt:406)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3.invokeSuspend(Merge.kt:23)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3.invoke(Merge.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3.invoke(Merge.kt)
                                        at kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(Undispatched.kt:44)
                                        at kotlinx.coroutines.CoroutineScopeKt.coroutineScope(CoroutineScope.kt:285)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.flowCollect(Merge.kt:21)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowOperator.collectTo$suspendImpl(ChannelFlow.kt:153)
                                        at kotlinx.coroutines.flow.internal.ChannelFlowOperator.collectTo(ChannelFlow.kt)
                                        at kotlinx.coroutines.flow.internal.ChannelFlow$collectToFun$1.invokeSuspend(ChannelFlow.kt:56)
                                        - "com.intellij.grazie.cloud.license.GrazieLoginManager#2160":StandaloneCoroutine{Cancelling}, state: RUNNING [Kernel@3pvf4qa5gj1dkka3mv3l, Rete(abortOnError=false, commands=capacity=2147483647,data=[onReceive], reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c, dbSource=ReteDbSource(reteState=kotlinx.coroutines.flow.StateFlowImpl@631330c)), DbSourceContextElement(kernel Kernel@3pvf4qa5gj1dkka3mv3l), ComponentManager(ApplicationImpl@1202288174), com.intellij.codeWithMe.ClientIdContextElementPrecursor, CoroutineId(2160), Dispatchers.Default]

Stored cache entry for task ':buildSearchableOptions' with cache key 94daa0fc747c2b2dce9ab13283dc59f8
Resolve mutations for :prepareJarSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.
:prepareJarSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :prepareJarSearchableOptions
Build cache key for task ':prepareJarSearchableOptions' is 88efde604b38495c3cb2c2dc078863de
Task ':prepareJarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':prepareJarSearchableOptions' with cache key 88efde604b38495c3cb2c2dc078863de
Resolve mutations for :jarSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.
:jarSearchableOptions (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :jarSearchableOptions
Build cache key for task ':jarSearchableOptions' is 5d4752a45a16530db585d5c54b40a773
Task ':jarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':jarSearchableOptions' with cache key 5d4752a45a16530db585d5c54b40a773
Resolve mutations for :buildPlugin (Thread[#410,Execution worker Thread 14,5,main]) started.
:buildPlugin (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
  Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
  Executed with '--rerun-tasks'.
Resolve mutations for :verifyPlugin (Thread[#410,Execution worker Thread 14,5,main]) started.
:verifyPlugin (Thread[#410,Execution worker Thread 14,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
  Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.404\c742b35fee7d288cbb4b10630762c905bea6676a\verifier-cli-1.404-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/reports/pluginVerifier -runtime-dir C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/e0187cd46b48be584a4fe539bd45e063/transformed/idea-2025.3-win/jbr -subsystems-to-check all -verification-reports-formats plain,html C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/distributions/WindowColorPanel-1.0.2.zip C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\37779718418610f5f212b9d702c1b248\transformed\idea-2025.3.5-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.404
2026-05-21T05:57:37 [main] INFO  c.j.p.options.OptionsParser - The verification directory C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\reports\pluginVerifier is being deleted because it is not empty.
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\reports\pluginVerifier
2026-05-21T05:57:41 [main] INFO  verification - Reading IDE C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\37779718418610f5f212b9d702c1b248\transformed\idea-2025.3.5-win
2026-05-21T05:57:41 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\37779718418610f5f212b9d702c1b248\transformed\idea-2025.3.5-win
2026-05-21T05:57:41 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e0187cd46b48be584a4fe539bd45e063\transformed\idea-2025.3-win\jbr
2026-05-21T05:57:41 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'intellij.android.gradle.declarative.lang.flags' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.flags.jar'
Layout component 'intellij.android.gradle.declarative.lang.sync' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.sync.jar'
Layout component 'intellij.clouds.docker.agent' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-impl\lib\modules\intellij.clouds.docker.agent.jar'
Layout component 'intellij.clouds.docker.dependencies' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-impl\lib\modules\intellij.clouds.docker.dependencies.jar'
Layout component 'intellij.clouds.docker.devcontainers.cli' has some nonexistent 'classPath' elements: 'plugins\clouds-docker-gateway\lib\modules\intellij.clouds.docker.devcontainers.cli.jar'
Layout component 'intellij.clouds.kubernetes' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.jar'
Layout component 'intellij.clouds.kubernetes.libraries' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.libraries.jar'
Layout component 'intellij.community.wintools' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.community.wintools.jar'
Layout component 'intellij.cwm.plugin' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.plugin.jar'
Layout component 'intellij.cwm.plugin.common' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.cwm.plugin.common.jar'
Layout component 'intellij.database' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.jar'
Layout component 'intellij.database.connectivity.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.connectivity.ex.jar'
Layout component 'intellij.database.dialects.postgres.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgres.ex.jar'
Layout component 'intellij.database.dialects.postgresbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresbase.ex.jar'
Layout component 'intellij.database.dialects.postgresgreenplumbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresgreenplumbase.ex.jar'
Layout component 'intellij.driver.client' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.client.jar'
Layout component 'intellij.driver.impl' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.impl.jar'
Layout component 'intellij.driver.model' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.model.jar'
Layout component 'intellij.gateway.ssh' has some nonexistent 'classPath' elements: 'plugins\gateway-plugin\lib\modules\intellij.gateway.ssh.jar'
Layout component 'intellij.java.debugger.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.debugger.impl.shared.jar'
Layout component 'intellij.java.execution.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.execution.impl.shared.jar'
Layout component 'intellij.java.frontback.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.impl.jar'
Layout component 'intellij.java.frontback.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.jar'
Layout component 'intellij.java.frontback.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.impl.jar'
Layout component 'intellij.java.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.jar'
Layout component 'intellij.java.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.impl.jar'
Layout component 'intellij.java.syntax' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.syntax.jar'
Layout component 'intellij.javascript.common' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.common.jar'
Layout component 'intellij.javascript.common.css' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.common.css.jar'
Layout component 'intellij.javascript.parser' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.parser.jar'
Layout component 'intellij.javascript.psi.impl' has some nonexistent 'classPath' elements: 'plugins\javascript-plugin\lib\modules\intellij.javascript.psi.impl.jar'
Layout component 'intellij.json' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.jar'
Layout component 'intellij.json.syntax' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.syntax.jar'
Layout component 'intellij.libraries.aalto.xml' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.aalto.xml.jar'
Layout component 'intellij.libraries.ai.grazie.spell.gec.engine.local' has some nonexistent 'classPath' elements: 'plugins\grazie\lib\modules\intellij.libraries.ai.grazie.spell.gec.engine.local.jar'
Layout component 'intellij.libraries.asm' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.asm.jar'
Layout component 'intellij.libraries.asm.tools' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.asm.tools.jar'
Layout component 'intellij.libraries.automaton' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.automaton.jar'
Layout component 'intellij.libraries.blockmap' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.blockmap.jar'
Layout component 'intellij.libraries.bouncy.castle.pgp' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.bouncy.castle.pgp.jar'
Layout component 'intellij.libraries.bouncy.castle.provider' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.bouncy.castle.provider.jar'
Layout component 'intellij.libraries.caffeine' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.caffeine.jar'
Layout component 'intellij.libraries.cglib' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.cglib.jar'
Layout component 'intellij.libraries.classgraph' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.classgraph.jar'
Layout component 'intellij.libraries.cli.parser' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.cli.parser.jar'
Layout component 'intellij.libraries.commons.cli' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.cli.jar'
Layout component 'intellij.libraries.commons.codec' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.codec.jar'
Layout component 'intellij.libraries.commons.compress' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.compress.jar'
Layout component 'intellij.libraries.commons.imaging' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.imaging.jar'
Layout component 'intellij.libraries.commons.io' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.io.jar'
Layout component 'intellij.libraries.commons.lang3' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.lang3.jar'
Layout component 'intellij.libraries.commons.logging' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.commons.logging.jar'
Layout component 'intellij.libraries.fastutil' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.fastutil.jar'
Layout component 'intellij.libraries.gson' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.gson.jar'
Layout component 'intellij.libraries.guava' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.guava.jar'
Layout component 'intellij.libraries.hash4j' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.hash4j.jar'
Layout component 'intellij.libraries.hdr.histogram' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.hdr.histogram.jar'
Layout component 'intellij.libraries.http.client' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.http.client.jar'
Layout component 'intellij.libraries.icu4j' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.icu4j.jar'
Layout component 'intellij.libraries.imgscalr' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.imgscalr.jar'
Layout component 'intellij.libraries.ini4j' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ini4j.jar'
Layout component 'intellij.libraries.ion' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ion.jar'
Layout component 'intellij.libraries.jackson' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jackson.jar'
Layout component 'intellij.libraries.jackson.databind' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jackson.databind.jar'
Layout component 'intellij.libraries.jackson.dataformat.yaml' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jackson.dataformat.yaml.jar'
Layout component 'intellij.libraries.jackson.jr.objects' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jackson.jr.objects.jar'
Layout component 'intellij.libraries.jackson.module.kotlin' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jackson.module.kotlin.jar'
Layout component 'intellij.libraries.java.websocket' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.java.websocket.jar'
Layout component 'intellij.libraries.javax.annotation' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.javax.annotation.jar'
Layout component 'intellij.libraries.jaxen' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jaxen.jar'
Layout component 'intellij.libraries.jbr' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jbr.jar'
Layout component 'intellij.libraries.jcef' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jcef.jar'
Layout component 'intellij.libraries.jcip' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jcip.jar'
Layout component 'intellij.libraries.jediterm.core' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jediterm.core.jar'
Layout component 'intellij.libraries.jediterm.ui' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jediterm.ui.jar'
Layout component 'intellij.libraries.jettison' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jettison.jar'
Layout component 'intellij.libraries.jsonpath' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jsonpath.jar'
Layout component 'intellij.libraries.jsoup' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jsoup.jar'
Layout component 'intellij.libraries.jsvg' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jsvg.jar'
Layout component 'intellij.libraries.jvm.native.trusted.roots' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jvm.native.trusted.roots.jar'
Layout component 'intellij.libraries.jzlib' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.jzlib.jar'
Layout component 'intellij.libraries.kotlin.reflect' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlin.reflect.jar'
Layout component 'intellij.libraries.kotlinx.collections.immutable' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.collections.immutable.jar'
Layout component 'intellij.libraries.kotlinx.coroutines.slf4j' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.coroutines.slf4j.jar'
Layout component 'intellij.libraries.kotlinx.datetime' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.datetime.jar'
Layout component 'intellij.libraries.kotlinx.html' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.html.jar'
Layout component 'intellij.libraries.kotlinx.io' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.io.jar'
Layout component 'intellij.libraries.kotlinx.serialization.core' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.serialization.core.jar'
Layout component 'intellij.libraries.kotlinx.serialization.json' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.serialization.json.jar'
Layout component 'intellij.libraries.kotlinx.serialization.protobuf' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kotlinx.serialization.protobuf.jar'
Layout component 'intellij.libraries.kryo5' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.kryo5.jar'
Layout component 'intellij.libraries.ktor.client' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ktor.client.jar'
Layout component 'intellij.libraries.ktor.client.cio' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ktor.client.cio.jar'
Layout component 'intellij.libraries.ktor.io' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ktor.io.jar'
Layout component 'intellij.libraries.ktor.network.tls' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ktor.network.tls.jar'
Layout component 'intellij.libraries.ktor.utils' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.ktor.utils.jar'
Layout component 'intellij.libraries.lz4' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.lz4.jar'
Layout component 'intellij.libraries.markdown' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.markdown.jar'
Layout component 'intellij.libraries.miglayout.swing' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.miglayout.swing.jar'
Layout component 'intellij.libraries.mvstore' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.mvstore.jar'
Layout component 'intellij.libraries.oro.matcher' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.oro.matcher.jar'
Layout component 'intellij.libraries.proxy.vole' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.proxy.vole.jar'
Layout component 'intellij.libraries.pty4j' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.pty4j.jar'
Layout component 'intellij.libraries.rd.text' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.rd.text.jar'
Layout component 'intellij.libraries.rhino' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.rhino.jar'
Layout component 'intellij.libraries.snakeyaml' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.snakeyaml.jar'
Layout component 'intellij.libraries.snakeyaml.engine' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.snakeyaml.engine.jar'
Layout component 'intellij.libraries.sshj' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.sshj.jar'
Layout component 'intellij.libraries.stream' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.stream.jar'
Layout component 'intellij.libraries.velocity' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.velocity.jar'
Layout component 'intellij.libraries.winp' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.winp.jar'
Layout component 'intellij.libraries.xerces' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.xerces.jar'
Layout component 'intellij.libraries.xstream' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.xstream.jar'
Layout component 'intellij.libraries.xtext.xbase' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.xtext.xbase.jar'
Layout component 'intellij.libraries.xz' has some nonexistent 'classPath' elements: 'lib\modules\intellij.libraries.xz.jar'
Layout component 'intellij.markdown' has some nonexistent 'classPath' elements: 'plugins\markdown\lib\modules\intellij.markdown.jar'
Layout component 'intellij.platform.backend.split' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.backend.split.jar'
Layout component 'intellij.platform.debugger.impl.rpc' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.debugger.impl.rpc.jar'
Layout component 'intellij.platform.debugger.impl.shared' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.debugger.impl.shared.jar'
Layout component 'intellij.platform.eel.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.eel.impl.jar'
Layout component 'intellij.platform.find' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.find.jar'
Layout component 'intellij.platform.lsp' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.lsp.jar'
Layout component 'intellij.platform.lsp.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.lsp.impl.jar'
Layout component 'intellij.platform.managed.cache' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.managed.cache.jar'
Layout component 'intellij.platform.polySymbols' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.polySymbols.jar'
Layout component 'intellij.platform.polySymbols.backend' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.polySymbols.backend.jar'
Layout component 'intellij.platform.remoteController' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.remoteController.jar'
Layout component 'intellij.platform.remoteController.backend' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.remoteController.backend.jar'
Layout component 'intellij.platform.rpc.topics' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.rpc.topics.jar'
Layout component 'intellij.platform.scopes' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.scopes.jar'
Layout component 'intellij.platform.split' has some nonexistent 'classPath' elements: 'plugins\cwm-plugin\lib\modules\intellij.platform.split.jar'
Layout component 'intellij.platform.ssh' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.ssh.jar'
Layout component 'intellij.platform.ssh.attach' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.ssh.attach.jar'
Layout component 'intellij.platform.ssh.core' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.ssh.core.jar'
Layout component 'intellij.platform.ssh.ui' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.ssh.ui.jar'
Layout component 'intellij.platform.vcs' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.vcs.jar'
Layout component 'intellij.platform.vcs.core' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.vcs.core.jar'
Layout component 'intellij.platform.vcs.shared' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.vcs.shared.jar'
Layout component 'intellij.properties' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.jar'
Layout component 'intellij.properties.psi' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.psi.jar'
Layout component 'intellij.qodana.sarif' has some nonexistent 'classPath' elements: 'plugins\qodana\lib\modules\intellij.qodana.sarif.jar'
Layout component 'intellij.regexp' has some nonexistent 'classPath' elements: 'lib\modules\intellij.regexp.jar'
Layout component 'intellij.sh.core' has some nonexistent 'classPath' elements: 'plugins\sh-plugin\lib\modules\intellij.sh.core.jar'
Layout component 'intellij.toml.core' has some nonexistent 'classPath' elements: 'plugins\toml\lib\modules\intellij.toml.core.jar'
Layout component 'intellij.vcs.git.shared' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git.shared.jar'
Layout component 'intellij.vcs.git/localHistory' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git\localHistory.jar'
Layout component 'intellij.vcs.github' has some nonexistent 'classPath' elements: 'plugins\vcs-github\lib\modules\intellij.vcs.github.jar'
Layout component 'intellij.vcs.gitlab' has some nonexistent 'classPath' elements: 'plugins\vcs-gitlab\lib\modules\intellij.vcs.gitlab.jar'
Layout component 'intellij.xml.analysis' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.analysis.jar'
Layout component 'intellij.xml.analysis.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.analysis.impl.jar'
Layout component 'intellij.xml.dom' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.dom.jar'
Layout component 'intellij.xml.dom.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.dom.impl.jar'
Layout component 'intellij.xml.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.impl.jar'
Layout component 'intellij.xml.parser' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.parser.jar'
Layout component 'intellij.xml.psi' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.psi.jar'
Layout component 'intellij.xml.psi.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.psi.impl.jar'
Layout component 'intellij.xml.structureView' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.structureView.jar'
Layout component 'intellij.xml.structureView.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.structureView.impl.jar'
Layout component 'intellij.xml.syntax' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.syntax.jar'
Layout component 'intellij.xml.ui.common' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.ui.common.jar'
Layout component 'intellij.yaml' has some nonexistent 'classPath' elements: 'plugins\yaml\lib\modules\intellij.yaml.jar'
2026-05-21T05:57:41 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\distributions\WindowColorPanel-1.0.2.z2026-05-21T05:57:45 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowColorPanel:1.0.2 against IU-253.33514.17

2026-05-21T06:00:13 [main] INFO  verification - Finished 1 of 1 verifications (in 147,6 s): IU-253.33514.17 against WindowColorPanel:1.0.2: Compatible
Plugin WindowColorPanel:1.0.2 against IU-253.33514.17: Compatible
Dynamic Plugin Eligibility:
    Plugin can probably be enabled or disabled without IDE restart

2026-05-21T06:00:13 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 0 ms
2026-05-21T06:00:13 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 0 B
2026-05-21T06:00:13 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,72 GB
2026-05-21T06:00:13 [main] INFO  verification - Verification reports for WindowColorPanel:1.0.2 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\reports\pluginVerifier\IU-253.33514.17
2026-05-21T06:00:13 [main] INFO  verification - Total time spent in plugin verification: 2 m 32 s 128 ms
Build 7fec5c3d-f178-4772-8119-5bba0f8e3ddb is closed

BUILD SUCCESSFUL in 3m 15s
14 actionable tasks: 14 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel]
Configuration cache entry reused.

```