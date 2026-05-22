```markdown
$ ./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alexander.goudemond\.gradle\native
Initialized jansi services in: C:\Users\alexander.goudemond\.gradle\native
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
The client will now receive all logging from the daemon (pid: 27240). The daemon log file: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0\daemon-27240.out.log
Starting 6th build in daemon [uptime: 35 mins 22.47 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Encryption key source: default Gradle keystore (pkcs12)
Calculating task graph as no cached configuration is available for tasks: verifyPlugin
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
Build cache key for Kotlin DSL script compilation (Project/TopLevel/stage1) is ff972a5d0d6c19a62c01a3ecdec89c46
Stored cache entry for Kotlin DSL script compilation (Project/TopLevel/stage1) with cache key ff972a5d0d6c19a62c01a3ecdec89c46
Resolved plugin [id: 'java']
Resolved plugin [id: 'org.jetbrains.kotlin.jvm', version: '2.2.20']
Resolved plugin [id: 'org.jetbrains.intellij.platform', version: '2.10.5']
Resolved plugin [id: 'org.jetbrains.changelog', version: '2.5.0']
Build 53b0dbfc-f146-4601-95af-3903f2a997a0 is started
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
Build cache key for Kotlin DSL script compilation (Project/TopLevel/stage2) is d2c3c5a48bd06603512c2eca1f31a9ed
Stored cache entry for Kotlin DSL script compilation (Project/TopLevel/stage2) with cache key d2c3c5a48bd06603512c2eca1f31a9ed
All projects evaluated.
Task name matched 'verifyPlugin'
Selected primary task 'verifyPlugin' from project :
Directory 'C:\myprograms\jdk\jdk1.6.0_45' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jdk-11.0.17' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre8' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre6' (Windows Registry) auto-detected used for java installations does not exist
[org.jetbrains.intellij.platform] LocalIvyArtifactPathComponentMetadataRule has been registered.
Downloading https://repo.maven.apache.org/maven2/org/jetbrains/intellij/plugins/verifier-cli/maven-metadata.xml to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download8782342493099915882bin
Downloading https://repo.maven.apache.org/maven2/org/jetbrains/intellij/plugins/verifier-cli/1.405/verifier-cli-1.405.pom to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download12158536050879889135bin
Downloading https://repo.maven.apache.org/maven2/org/jetbrains/intellij/plugins/verifier-cli/1.405/verifier-cli-1.405.module to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download2167817722222945198bin
Downloading https://repo.maven.apache.org/maven2/org/jetbrains/intellij/plugins/verifier-cli/1.405/verifier-cli-1.405-all.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download9045450281746568831bin
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/idea/ideaIU/2025.2.6.2/ideaIU-2025.2.6.2.pom]
Resource missing. [HTTP HEAD: https://download-cdn.jetbrains.com/idea/ideaIU-2025.2.6.2-win.zip]
Resource missing. [HTTP GET: https://d2cico3c979uwg.cloudfront.net/idea/ideaIU/2025.2.6.2/ideaIU-2025.2.6.2.pom]
Resource missing. [HTTP GET: https://d2s4y8xcwt8bet.cloudfront.net/idea/ideaIU/2025.2.6.2/ideaIU-2025.2.6.2.pom]
Resource missing. [HTTP GET: https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/idea/ideaIU/2025.2.6.2/ideaIU-2025.2.6.2.pom]
Resource missing. [HTTP GET: https://artifacts-caching-proxy.aws.intellij.net/plugins.jetbrains.com/maven/idea/ideaIU/2025.2.6.2/ideaIU-2025.2.6.2.pom]
Resource missing. [HTTP GET: https://download-cdn.jetbrains.com/idea/ideaIU-2025.2.6.2-win.zip]
Downloading https://download.jetbrains.com/idea/ideaIU-2025.2.6.2.win.zip to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download15392959425068901511bin
Caching disabled for ExtractorTransformer: C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\idea\ideaIU\2025.2.6.2\3bf0cc1ac16fdcff8deb2b7b7bd9558662fee2b6\ideaIU-2025.2.6.2-win.zip because:
  Caching not enabled.
[org.jetbrains.intellij.platform] Extracting archive 'C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\idea\ideaIU\2025.2.6.2\3bf0cc1ac16fdcff8deb2b7b7bd9558662fee2b6\ideaIU-2025.2.6.2-win.zip' to directory 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win'.
[org.jetbrains.intellij.platform] Resolving the content directory in 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win'.
[org.jetbrains.intellij.platform] The content directory is 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win'.
[org.jetbrains.intellij.platform] Extracting to 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win' completed.
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/jbr/bin/java.Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/jbr/bin/java.exe''
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':instrumentCode', task ':jar', task ':instrumentedJar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-ant-tasks/252.23892.409/java-compiler-ant-tasks-252.23892.409.pom to CResource missing. [HTTP HEAD: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-ant-tasks/252.23892.409/java-compiler-ant-tasks-252.23892.409.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-gui-forms-compiler/252.23892.409/java-gui-forms-compiler-252.23892.409.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-instrumentation-util/252.23892.409/java-compiler-instrumentation-util-252.23892.409.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-gui-forms-rt/252.23892.409/java-gui-forms-rt-252.23892.409.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-instrumentation-util-java8/252.23892.409/java-compiler-instrumentation-util-java8-252.23892.409.pom]
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util/252.23892.409/java-compiler-instrumentation-util-Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-gui-forms-compiler/252.23892.409/java-gui-forms-compiler-252.23892.409.pom to CDownloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util-java8/252.23892.409/java-compiler-instrumentation-util-java8-252.23892.409.pom to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download13224007263731412636bin
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/platform/util-jdom/252.23892.409/util-jdom-252.23892.409.pom]
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/platform/util-jdom/252.23892.409/util-jdom-252.23892.409.pom to C:\Users\alexander.goudemDownloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-gui-forms-compiler/252.23892.409/java-gui-forms-compiler-252.23892.409.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download14349460077287966828bin
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/platform/util-jdom/252.23892.409/util-jdom-252.23892.409.jar to C:\Users\alexander.goudemDownloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-ant-tasks/252.23892.409/java-compiler-ant-tasks-252.23892.409.jar to CDownloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util-java8/252.23892.409/java-compiler-instrumentation-util-java8-252.23892.409.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download4091385538118855228bin
Build 53b0dbfc-f146-4601-95af-3903f2a997a0 is closed
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Ignoring listeners of task graph ready event, as this build (:) has already executed work.
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#335,Execution worker,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#336,Execution worker Thread 2,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#336,Execution worker Thread 2,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#335,Execution worker,5,main]) started.

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#336,Execution worker Thread 2,5,main]) started.
:compileKotlin (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
  Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Resolve mutations for :patchPluginXml (Thread[#335,Execution worker,5,main]) started.
Resolve mutations for :generateManifest (Thread[#334,included builds,5,main]) started.
:patchPluginXml (Thread[#335,Execution worker,5,main]) started.
:generateManifest (Thread[#334,included builds,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 42e138cba47304528c527e5c729b17f0
Task ':generateManifest' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 42e138cba47304528c527e5c729b17f0

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is f72921eb2e204e5cdb7072d593a873be
Task ':patchPluginXml' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key f72921eb2e204e5cdb7072d593a873be
Resolve mutations for :processResources (Thread[#335,Execution worker,5,main]) started.
:processResources (Thread[#335,Execution worker,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
  Not worth caching
Task ':processResources' is not up-to-date because:
  Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app-client.jar is cd9b1acf0758ff33f1dff2a2258cc679
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util-8.jar is 5d93a86f261f7cbc2e3c2ba84d096079
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\externalProcess-rt.jar is 91d7d94b3f886d9932dd4af22c93da59
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product.jar is 66ef27eae0a68ecf8243fab803fb8831
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\platform-loader.jar is 50e7cb5b55c687f2562cf0d0711d24c3
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\groovy.jar is 098907b04967c08f125c8d87d76a7e50
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\idea_rt.jar is 3971279b82e28f9d87262907072618df
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app.jar is d84f1fc2969605a1310965cd7891295c
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\kotlinx-coroutines-slf4j-1.10.1-intellij.jar is 9cc11f2717ee3223889676d20f09de23
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\bouncy-castle.jar is 2a859289f983ce93faf32548ed962c44
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\external-system-rt.jar is 28480155253dca14f8dc06db4edb932d
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util.jar is b747095ab38444b2cb9a2b9ec40f09b8
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jps-model.jar is 43852bd085202618df140c0bd7f75a25
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib-client.jar is 6e4f9abce17fac412da7a5f3bc50ff87
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib.jar is 502e99dbf00bc4afd11c6b0248a6b208
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product-client.jar is fb702355e7308af49636f20c435e15ca
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\externalProcess-rt.jar with cache key 91d7d94b3f886d9932dd4af22c93da59
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\external-system-rt.jar with cache key 28480155253dca14f8dc06db4edb932d
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\idea_rt.jar with cache key 3971279b82e28f9d87262907072618df
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\platform-loader.jar with cache key 50e7cb5b55c687f2562cf0d0711d24c3
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jps-model.jar with cache key 43852bd085202618df140c0bd7f75a25
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\rd.jar is 5c3f10a52d37a4fea00fdad38ba1d6c2
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\stats.jar is 163d77f3aa83e929f1e7c613ecd7db70
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util_rt.jar is c18849b3c2d5a69d5f9607c37f2a3d3b
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\nio-fs.jar is 069a048dbebb5ba6404263c517b0a79f
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\nio-fs.jar with cache key 069a048dbebb5ba6404263c517b0a79f
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util_rt.jar with cache key c18849b3c2d5a69d5f9607c37f2a3d3b
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product-client.jar with cache key fb702355e7308af49636f20c435e15ca
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\bouncy-castle.jar with cache key 2a859289f983ce93faf32548ed962c44
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\groovy.jar with cache key 098907b04967c08f125c8d87d76a7e50
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\kotlinx-coroutines-slf4j-1.10.1-intellij.jar with cache key 9cc11f2717ee3223889676d20f09de23
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util.jar with cache key b747095ab38444b2cb9a2b9ec40f09b8
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\stats.jar with cache key 163d77f3aa83e929f1e7c613ecd7db70
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product.jar with cache key 66ef27eae0a68ecf8243fab803fb8831
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib.jar with cache key 502e99dbf00bc4afd11c6b0248a6b208
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\rd.jar with cache key 5c3f10a52d37a4fea00fdad38ba1d6c2
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app.jar with Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util-8.jar wiStored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib-client.jaStored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app-client.jar with cache key cd9b1acf0758ff33f1dff2a2258cc679
Build cache key for task ':compileKotlin' is 2d23fe21cfeaa771d638398f4ee1a97a
Task ':compileKotlin' is not up-to-date because:
  Executed with '--rerun-tasks'.
The input changes require a full rebuild for incremental task ':compileKotlin'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\java', not found
Kotlin source files: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowCustomColorStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowPanelAppearanceStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\persistence\WindowTitleNumberingStateService.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ColorMagnifier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\ScreenColorPicker.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\settings\WindowAccentSettings.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\configuration\tool_window\WindowAccentToolWindowFactory.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_color\WindowColorApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\feature\window_title\WindowTitleApplier.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\i18n\MyMessageBundle.kt, C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\src\main\kotlin\com\window_accent\PluginStartupActivity.kt
Java source files:
Script source files:
Script file extensions:
Using Kotlin/JVM incremental compilation
[KOTLIN] Kotlin compilation 'jdkHome' argument: C:\myprograms\java\jdk\jdk21.0.7
i: starting the daemon as: C:\myprograms\java\jdk\jdk21.0.7\bin\java -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar -Djava.awt.headless=true -Djava.rmi.server.hostname=127.0.0.1 -Xmx8112m -XX:ReservedCodeCacheSize=320m -Dkotlin.environment.keepalive -ea -XX:+UseCodeCacheFlushing -XX:+UseParallelGC -Dkotlin.daemon.initiator.marker.file=C:\Users\ALEXAN~1.GOU\AppData\Local\Temp\kotlin-compiler-in-indowccent-2667693395039675759.alive --add-exports java.base/sun.nio.ch=ALL-UNNAMED org.jetbrains.kotlin.daemon.KotlinCompileDaemon --daemon-runFilesPath C:\Users\alexander.goudemond\AppData\Local\kotlin\daemon --daemon-autoshutdownIdleSeconds=7200 --daemon-compilerClasspath C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-compiler-embeddable\2.2.20\bb8331b7585e36ea311825b01a1c06860c055fd1\kotlin-compiler-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\2.2.20\5380b19fa1924399b62ce3a1faffebb2b4f82272\kotlin-stdlib-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-script-runtime\2.2.20\4c679cbeac0bb583b75c3a080013da8eaf240807\kotlin-script-runtime-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-reflect\1.6.10\1cbe9c92c12a94eea200d23c2bbaedaf3daf5132\kotlin-reflect-1.6.10.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-daemon-embeddable\2.2.20\feff12d48d7f1eb628742b0a721395e16f8755bf\kotlin-daemon-embeddable-2.2.20.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-coroutines-core-jvm\1.8.0\ac1dc37a30a93150b704022f8d895ee1bd3a36b3\kotlinx-coroutines-core-jvm-1.8.0.jar;C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains\annotations\13.0\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\annotations-13.0.jar
i: #1 retrying connecting to the daemon
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@246a6931, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\local-state\build-history.bin, useModuleDetection=false), icFeatures=IncrementalCompilationFeatures(usePreciseJavaTracking=true, withAbiSnapshot=false, preciseCompilationResultsBackup=true, keepIncrementalCompilationCachesInMemory=true, enableUnsafeIncrementalCompilationForMultiplatform=false, enableMonotonousIncrementalCompileSetExpansion=true), outputFiles=[C:\myworFinished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key 2d23fe21cfeaa771d638398f4ee1a97a
Resolve mutations for :compileJava (Thread[#336,Execution worker Thread 2,5,main]) started.
:compileJava (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#336,Execution worker Thread 2,5,main]) started.
:classes (Thread[#335,Execution worker,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :instrumentCode (Thread[#335,Execution worker,5,main]) started.
:instrumentCode (Thread[#335,Execution worker,5,main]) started.

> Task :instrumentCode SKIPPED
Skipping task ':instrumentCode' as task onlyIf 'Task is enabled' is false.
Resolve mutations for :jar (Thread[#335,Execution worker,5,main]) started.
:jar (Thread[#335,Execution worker,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Not worth caching
Task ':jar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\java\main', not found
Resolve mutations for :instrumentedJar (Thread[#335,Execution worker,5,main]) started.
:instrumentedJar (Thread[#335,Execution worker,5,main]) started.

> Task :instrumentedJar
Build cache key for task ':instrumentedJar' is efc258a0627487c91351e1c9d2f8e393
Task ':instrumentedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\instrumented\instrumentCode', not found
Stored cache entry for task ':instrumentedJar' with cache key efc258a0627487c91351e1c9d2f8e393
Resolve mutations for :composedJar (Thread[#335,Execution worker,5,main]) started.
:composedJar (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is 0f9f39984988431544e9ee1dea0624aa
Task ':composedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key 0f9f39984988431544e9ee1dea0624aa
Resolve mutations for :prepareSandbox (Thread[#336,Execution worker Thread 2,5,main]) started.
:prepareSandbox (Thread[#335,Execution worker,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
  Not worth caching
Task ':prepareSandbox' is not up-to-date because:
  Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.2\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.2\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.2\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.2\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#335,Execution worker,5,main]) started.
:buildSearchableOptions (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :buildSearchableOptions
Deleting stale output file: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\tmp\buildSearchableOptions
Build cache key for task ':buildSearchableOptions' is d6c3fb93c2e9a6bb19bd8d9ec28068ae
Task ':buildSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Starting process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\jbr\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win Command: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\jbr\bin\java.exe -Daether.connector.resumeDownloads=false -Dcompose.swing.render.on.graphics=true -Dide.native.launcher=false -Didea.config.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.2/config -Didea.kotlin.plugin.use.k2=true -Didea.l10n.keys=only -Didea.log.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.2/log -Didea.paths.selector=IntelliJIdea2025.2 -Didea.plugins.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.2/plugins -Didea.required.plugins.id=WindowAccent -Didea.system.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.2/system -Didea.vendor.name=JetBrains -Dintellij.platform.runtime.repository.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/modules/module-descriptors.dat -Dio.netty.allocator.type=pooled -Djava.nio.file.spi.DefaultFileSystemProvider=com.intellij.platform.core.nio.fs.MultiRoutingFileSystemProvider -Djava.system.class.loader=com.intellij.util.lang.PathClassLoader -Djava.util.zip.use.nio.for.zip.file.access=true -Djbr.catch.SIGABRT=true -Djdk.attach.allowAttachSelf=true -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.module.illegalAccess.silent=true -Djdk.nio.maxCachedBufferSize=2097152 -Djna.boot.library.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/lib/jna/amd64 -Djna.noclasspath=true -Djna.nosys=true -Dplugin.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.2/plugins/WindowAccent -Dpty4j.preferred.native.folder=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/lib/pty4j -Dsplash=true -Dsun.io.useCanonCaches=false -Dsun.java2d.metal=true -Dwsl.use.remote.agent.for.nio.filesystem=true -XX:JbrShrinkingGcMaxHeapFreeRatio=40 -XX:ReservedCodeCacheSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -XX:CICompilerCount=2 -XX:+IgnoreUnrecognizedVMOptions -XX:+UnlockDiagnosticVMOptions -XX:TieredOldPercentage=100000 -javaagent:C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\coroutines-javaagent.jar -Xbootclasspath/a:C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win\lib\nio-fs.jar --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.ref=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED --add-opens=java.base/sun.net.dns=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.fs=ALL-UNNAMED --add-opens=java.base/sun.security.ssl=ALL-UNNAMED --add-opens=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.desktop/com.sun.java.swing=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED --add-opens=java.desktop/java.awt.event=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED --add-opens=java.desktop/java.awt.image=ALL-UNNAMED --add-opens=java.desktop/java.awt.peer=ALL-UNNAMED --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html.parser=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED --add-opens=java.desktop/sun.font=ALL-UNNAMED --add-opens=java.desktop/sun.java2d=ALL-UNNAMED --add-opens=java.desktop/sun.swing=ALL-UNNAMED --add-opens=java.management/sun.management=ALL-UNNAMED --add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED -Xms128m -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -ea -cp C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\platform-loader.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util-8.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\app.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\annotations.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\bouncy-castle.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\eclipse.lsp4j.debug.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\eclipse.lsp4j.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\eclipse.lsp4j.jsonrpc.debug.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\eclipse.lsp4j.jsonrpc.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\external-system-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\externalProcess-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\forms_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\groovy.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\idea_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\intellij-test-discovery.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\javax.activation.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\javax.annotation-api.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jaxb-api.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jaxb-runtime.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jps-model.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\jsch-agent.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\kotlinx-coroutines-slf4j-1.10.1-intellij.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\lib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\opentelemetry.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\product-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\protobuf.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\rd.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\rhino.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\stats.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\trove.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\lib\util_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\jbr\lib\tools com.intellij.idea.Main traverseUI C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/tmp/buildSearchableOptions true
Successfully started process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win\jbr\bin\java.exe''
2026-05-22 05:55:59,745 [   3086]   WARN - #c.i.i.p.n.DefaultUiPluginManagerController - kotlinx.coroutines.JobCancellationException: Job was cancelled; job="com.intellij.ide.plugins.PluginManagerConfigurable":supervisor:ChildScope{Cancelling}@6343ab07
javax.net.ssl.SSLException: kotlinx.coroutines.JobCancellationException: Job was cancelled; job="com.intellij.ide.plugins.PluginManagerConfigurable":supervisor:ChildScope{Cancelling}@6343ab07
        at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:132)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:383)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:326)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:321)
        at java.base/sun.security.ssl.SSLSocketImpl.handleException(SSLSocketImpl.java:1708)
        at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:470)
        at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:426)
        at java.base/sun.net.www.protocol.https.HttpsClient.afterConnect(HttpsClient.java:586)
        at java.base/sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(AbstractDelegateHttpsURLConnection.java:187)
        at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1690)
        at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1614)
        at java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:531)
        at java.base/sun.net.www.protocol.https.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:307)
        at com.intellij.util.io.HttpRequests.openConnection(HttpRequests.java:626)
        at com.intellij.util.io.HttpRequests$RequestImpl.getConnection(HttpRequests.java:366)
        at com.intellij.util.io.HttpRequests$RequestImpl.getInputStream(HttpRequests.java:374)
        at com.intellij.ide.plugins.marketplace.MarketplaceRequests.executePluginSearch$lambda$5(MarketplaceRequests.kt:533)
        at com.intellij.util.io.HttpRequests.doProcess(HttpRequests.java:529)
        at com.intellij.util.io.HttpRequests.process(HttpRequests.java:511)
        at com.intellij.util.io.HttpRequests$RequestBuilderImpl.connect(HttpRequests.java:340)
        at com.intellij.ide.plugins.marketplace.MarketplaceRequests.executePluginSearch(MarketplaceRequests.kt:531)
        at com.intellij.ide.plugins.newui.DefaultUiPluginManagerController.executePluginsSearch(DefaultUiPluginManagerController.kt:514)
        at com.intellij.ide.plugins.newui.UiPluginManager.executeMarketplaceQuery(UiPluginManager.kt:51)
        at com.intellij.ide.plugins.PluginManagerPanelFactory$createMarketplacePanel$1.invokeSuspend(PluginManagerPanelFactory.kt:47)
        at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
        at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
        at kotlinx.coroutines.internal.SoftLimitedDispatcher$Worker.run(SoftLimitedDispatcher.kt:130)
        at kotlinx.coroutines.scheduling.TaskImpl.run(Tasks.kt:89)
        at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:613)
        at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:1183)
        at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:778)
        at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:765)
Caused by: com.intellij.openapi.progress.CeProcessCanceledException: kotlinx.coroutines.JobCancellationException: Job was cancelled; job="com.intellij.ide.plugins.PluginManagerConfigurable":supervisor:ChildScope{Cancelling}@6343ab07
        at com.intellij.serviceContainer.ComponentManagerImplKt.runBlockingInitialization$lambda$10(ComponentManagerImpl.kt:1660)
        at com.intellij.openapi.progress.ContextKt.prepareThreadContext(context.kt:82)
        at com.intellij.serviceContainer.ComponentManagerImplKt.runBlockingInitialization(ComponentManagerImpl.kt:1644)
        at com.intellij.serviceContainer.ComponentManagerImplKt.doGetOrCreateInstanceBlocking(ComponentManagerImpl.kt:1534)
        at com.intellij.serviceContainer.ComponentManagerImplKt.getOrCreateInstanceBlocking(ComponentManagerImpl.kt:1529)
        at com.intellij.serviceContainer.ComponentManagerImpl.doGetService(ComponentManagerImpl.kt:729)
        at com.intellij.serviceContainer.ComponentManagerImpl.getService(ComponentManagerImpl.kt:673)
        at com.intellij.util.net.ssl.ConfirmingTrustManager.withCalculatedCertificateStrategy(ConfirmingTrustManager.java:225)
        at com.intellij.util.net.ssl.ConfirmingTrustManager.checkServerTrusted(ConfirmingTrustManager.java:212)
        at com.intellij.util.net.ssl.ConfirmingTrustManager.checkServerTrusted(ConfirmingTrustManager.java:198)
        at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.checkServerCerts(CertificateMessage.java:1302)
        at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.onConsumeCertificate(CertificateMessage.java:1195)
        at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.consume(CertificateMessage.java:1138)
        at java.base/sun.security.ssl.SSLHandshake.consume(SSLHandshake.java:393)
        at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:476)
        at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:447)
        at java.base/sun.security.ssl.TransportContext.dispatch(TransportContext.java:206)
        at java.base/sun.security.ssl.SSLTransport.decode(SSLTransport.java:172)
        at java.base/sun.security.ssl.SSLSocketImpl.decode(SSLSocketImpl.java:1506)
        at java.base/sun.security.ssl.SSLSocketImpl.readHandshakeRecord(SSLSocketImpl.java:1421)
        at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:455)
        ... 26 more
Caused by: kotlinx.coroutines.JobCancellationException: Job was cancelled; job="com.intellij.ide.plugins.PluginManagerConfigurable":supervisor:ChildScope{Cancelling}@6343ab07
        at kotlinx.coroutines.JobSupport.cancel(JobSupport.kt:1685)
        at kotlinx.coroutines.CoroutineScopeKt.cancel(CoroutineScope.kt:308)
        at com.intellij.ide.plugins.PluginManagerConfigurable.disposeUIResources(PluginManagerConfigurable.java:2021)
        at com.intellij.openapi.options.ex.ConfigurableWrapper.disposeUIResources(ConfigurableWrapper.java:224)
        at com.intellij.ide.ui.search.TraverseUIStarterKt.processConfigurables(TraverseUIStarter.kt:504)
        at com.intellij.ide.ui.search.TraverseUIStarterKt.access$processConfigurables(TraverseUIStarter.kt:1)
        at com.intellij.ide.ui.search.TraverseUIStarterKt$doBuildSearchableOptions$2.invokeSuspend$lambda$0(TraverseUIStarter.kt:388)
        at com.intellij.openapi.application.CoroutinesKt.writeIntentReadAction$lambda$0(coroutines.kt:363)
        at com.intellij.openapi.application.impl.AppImplKt$rethrowCheckedExceptions$2.invoke(appImpl.kt:106)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.doRunWriteIntentReadAction(NestedLocksThreadingSupport.kt:666)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.runPreventiveWriteIntentReadAction(NestedLocksThreadingSupport.kt:640)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.runWriteIntentReadAction(NestedLocksThreadingSupport.kt:633)
        at com.intellij.openapi.application.impl.ApplicationImpl.runWriteIntentReadAction(ApplicationImpl.java:1166)
        at com.intellij.openapi.application.CoroutinesKt.writeIntentReadAction(coroutines.kt:363)
        at com.intellij.ide.ui.search.TraverseUIStarterKt$doBuildSearchableOptions$2.invokeSuspend(TraverseUIStarter.kt:384)
        at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
        at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
        at com.intellij.openapi.application.impl.EdtCoroutineDispatcher.wrapWithLocking$lambda$3$lambda$2(EdtCoroutineDispatcher.kt:71)
        at com.intellij.openapi.application.WriteIntentReadAction.lambda$run$0(WriteIntentReadAction.java:24)
        at com.intellij.openapi.application.impl.AppImplKt$rethrowCheckedExceptions$2.invoke(appImpl.kt:106)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.doRunWriteIntentReadAction(NestedLocksThreadingSupport.kt:666)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.runPreventiveWriteIntentReadAction(NestedLocksThreadingSupport.kt:640)
        at com.intellij.platform.locking.impl.NestedLocksThreadingSupport.runWriteIntentReadAction(NestedLocksThreadingSupport.kt:633)
        at com.intellij.openapi.application.impl.ApplicationImpl.runWriteIntentReadAction(ApplicationImpl.java:1166)
        at com.intellij.openapi.application.WriteIntentReadAction.compute(WriteIntentReadAction.java:55)
        at com.intellij.openapi.application.WriteIntentReadAction.run(WriteIntentReadAction.java:23)
        at com.intellij.openapi.application.impl.EdtCoroutineDispatcher.wrapWithLocking$lambda$3(EdtCoroutineDispatcher.kt:70)
        at com.intellij.openapi.application.impl.DispatchedRunnable.run(DispatchedRunnable.kt:42)
        at com.intellij.openapi.application.TransactionGuardImpl.runWithWritingAllowed(TransactionGuardImpl.java:240)
        at com.intellij.openapi.application.TransactionGuardImpl.access$100(TransactionGuardImpl.java:26)
        at com.intellij.openapi.application.TransactionGuardImpl$2.run(TransactionGuardImpl.java:222)
        at com.intellij.openapi.application.impl.FlushQueue.runNextEvent(FlushQueue.java:122)
        at com.intellij.openapi.application.impl.FlushQueue.flushNow(FlushQueue.java:43)
        at java.desktop/java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:318)
        at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:781)
        at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:728)
        at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:722)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:400)
        at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:87)
        at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:750)
        at com.intellij.ide.IdeEventQueue.defaultDispatchEvent(IdeEventQueue.kt:595)
        at com.intellij.ide.IdeEventQueue._dispatchEvent(IdeEventQueue.kt:488)
        at com.intellij.ide.IdeEventQueue.dispatchEvent$lambda$12$lambda$11$lambda$10(IdeEventQueue.kt:309)
        at com.intellij.ide.IdeEventQueueKt.performActivity$lambda$3(IdeEventQueue.kt:974)
        at com.intellij.openapi.application.TransactionGuardImpl.performActivity(TransactionGuardImpl.java:110)
        at com.intellij.ide.IdeEventQueueKt.performActivity(IdeEventQueue.kt:974)
        at com.intellij.ide.IdeEventQueue.dispatchEvent$lambda$12(IdeEventQueue.kt:307)
        at com.intellij.ide.IdeEventQueue.dispatchEvent(IdeEventQueue.kt:347)
        at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:207)
        at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:128)
        at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:117)
        at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:113)
        at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:105)
        at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:92)
2026-05-22 05:56:01,908 [   5249]   WARN - #c.i.i.u.UIThemeBean - Unknown field: CombinedDiff
2026-05-22 05:56:01,909 [   5250]   WARN - #c.i.i.u.UIThemeBean - Unknown field: BlockBorder
2026-05-22 05:56:10,018 [  13359]   WARN - #c.i.u.j.JBCefApp - JCEF is manually disabled in headless env via 'ide.browser.jcef.headless.enabled=false'
2026-05-22 05:56:10,657 [  13998]   WARN - #c.i.o.p.i.JavaHomeFinderWindows - Failed to get Java home path for %IntelliJ IDEA%
com.intellij.platform.eel.path.EelPathException: `%IntelliJ IDEA%`: Not a valid absolute path
        at com.intellij.platform.eel.path.EelPath$Companion.parse(EelPath.kt:29)
        at com.intellij.openapi.projectRoots.impl.EelSystemInfoProvider.getPath(JavaHomeFinderEel.kt:33)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findInPATH(JavaHomeFinderBasic.java:150)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findExistingJdks(JavaHomeFinderBasic.java:109)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findExistingJdkEntries(JavaHomeFinderBasic.java:124)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.findJdks(JavaHomeFinder.java:134)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.findJdks(JavaHomeFinder.java:130)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.suggestHomePaths(JavaHomeFinder.java:101)
        at com.intellij.openapi.projectRoots.impl.JavaSdkImpl.suggestHomePaths(JavaSdkImpl.java:206)
        at com.intellij.openapi.projectRoots.impl.JavaSdkImpl.suggestHomePaths(JavaSdkImpl.java:201)
        at com.intellij.openapi.projectRoots.impl.DefaultJdkConfiguratorImpl.guessJavaHome(DefaultJdkConfiguratorImpl.java:13)
        at com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl.guessJdk(JavaAwareProjectJdkTableImpl.java:59)
        at com.intellij.openapi.progress.impl.CoreProgressManager$1.run(CoreProgressManager.java:320)
        at com.intellij.openapi.progress.impl.CoreProgressManager.startTask(CoreProgressManager.java:498)
        at com.intellij.openapi.progress.impl.ProgressManagerImpl.startTask(ProgressManagerImpl.java:119)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcessWithProgressSynchronously$10(CoreProgressManager.java:588)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$new$0(ProgressRunner.java:88)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$submit$4(ProgressRunner.java:252)
        at com.intellij.openapi.progress.ProgressManager.lambda$runProcess$0(ProgressManager.java:98)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcess$1(CoreProgressManager.java:229)
        at com.intellij.platform.diagnostic.telemetry.helpers.TraceKt.use(trace.kt:44)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcess$2(CoreProgressManager.java:228)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$executeProcessUnderProgress$14(CoreProgressManager.java:681)
        at com.intellij.openapi.progress.impl.CoreProgressManager.registerIndicatorAndRun(CoreProgressManager.java:756)
        at com.intellij.openapi.progress.impl.CoreProgressManager.computeUnderProgress(CoreProgressManager.java:712)
        at com.intellij.openapi.progress.impl.CoreProgressManager.executeProcessUnderProgress(CoreProgressManager.java:680)
        at com.intellij.openapi.progress.impl.ProgressManagerImpl.executeProcessUnderProgress(ProgressManagerImpl.java:78)
        at com.intellij.openapi.progress.impl.CoreProgressManager.runProcess(CoreProgressManager.java:209)
        at com.intellij.openapi.progress.ProgressManager.runProcess(ProgressManager.java:98)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$submit$5(ProgressRunner.java:252)
        at com.intellij.openapi.progress.impl.ProgressRunner$ProgressRunnable.run(ProgressRunner.java:515)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$launchTask$18(ProgressRunner.java:480)
        at com.intellij.util.concurrency.ChildContext$runInChildContext$1.invoke(propagation.kt:167)
        at com.intellij.util.concurrency.ChildContext$runInChildContext$1.invoke(propagation.kt:167)
        at com.intellij.util.concurrency.ChildContext.runInChildContext(propagation.kt:173)
        at com.intellij.util.concurrency.ChildContext.runInChildContext(propagation.kt:167)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$launchTask$19(ProgressRunner.java:476)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1$1.run(Executors.java:735)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1$1.run(Executors.java:732)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:400)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1.run(Executors.java:732)
        at java.base/java.lang.Thread.run(Thread.java:1583)
2026-05-22 05:56:11,692 [  15033]   WARN - #c.i.o.u.BrowseFolderRunnable - multiple selection not supported
Found 392 configurables
save to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\tmp\buildSearchableOptions
Stored cache entry for task ':buildSearchableOptions' with cache key d6c3fb93c2e9a6bb19bd8d9ec28068ae
Resolve mutations for :prepareJarSearchableOptions (Thread[#336,Execution worker Thread 2,5,main]) started.
:prepareJarSearchableOptions (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :prepareJarSearchableOptions
Deleting stale output file: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\tmp\prepareJarSearchableOptions
Build cache key for task ':prepareJarSearchableOptions' is aee698363dcce1dffa6617f713d565b6
Task ':prepareJarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':prepareJarSearchableOptions' with cache key aee698363dcce1dffa6617f713d565b6
Resolve mutations for :jarSearchableOptions (Thread[#335,Execution worker,5,main]) started.
:jarSearchableOptions (Thread[#335,Execution worker,5,main]) started.

> Task :jarSearchableOptions
Build cache key for task ':jarSearchableOptions' is b9d2c866fc9b86b9a02d0902f047273b
Task ':jarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':jarSearchableOptions' with cache key b9d2c866fc9b86b9a02d0902f047273b
Resolve mutations for :buildPlugin (Thread[#336,Execution worker Thread 2,5,main]) started.
:buildPlugin (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
  Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
  Executed with '--rerun-tasks'.
Resolve mutations for :verifyPlugin (Thread[#335,Execution worker,5,main]) started.
:verifyPlugin (Thread[#336,Execution worker Thread 2,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
  Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.405\ff594280b5bcae48d0e09a2b78066ee019e50081\verifier-cli-1.405-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/pluginVerifier -runtime-dir C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dd5d62316fa52b52b0438f4badabdb70/transformed/ideaIU-2025.2-win/jbr -subsystems-to-check all -verification-reports-formats plain,html C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/distributions/WindowAccent-1.0.4.zip C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.405
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier
2026-05-22T05:56:21 [main] INFO  verification - Reading IDE C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win
2026-05-22T05:56:21 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\e8f601c5bfb3bf33d020dbc7a07f067e\transformed\ideaIU-2025.2.6.2-win
2026-05-22T05:56:21 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dd5d62316fa52b52b0438f4badabdb70\transformed\ideaIU-2025.2-win2026-05-22T05:56:21 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'intellij.android.gradle.declarative.lang.flags' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.flags.jar'
Layout component 'intellij.android.gradle.declarative.lang.sync' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.sync.jar'
Layout component 'intellij.clouds.kubernetes' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.jar'
Layout component 'intellij.clouds.kubernetes.libraries' has some nonexistent 'classPath' elements: 'plugins\clouds-kubernetes\lib\modules\intellij.clouds.kubernetes.libraries.jar'
Layout component 'intellij.database' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.jar'
Layout component 'intellij.database.core.impl' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.core.impl.jar'
Layout component 'intellij.database.dialects.base' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.base.jar'
Layout component 'intellij.database.dialects.base.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.base.ex.jar'
Layout component 'intellij.database.dialects.generic' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.generic.jar'
Layout component 'intellij.database.dialects.maria' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.maria.jar'
Layout component 'intellij.database.dialects.mssql' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mssql.jar'
Layout component 'intellij.database.dialects.mssql.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mssql.ex.jar'
Layout component 'intellij.database.dialects.mssql.impl' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mssql.impl.jar'
Layout component 'intellij.database.dialects.mysql' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mysql.jar'
Layout component 'intellij.database.dialects.mysqlbase' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mysqlbase.jar'
Layout component 'intellij.database.dialects.mysqlbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mysqlbase.ex.jar'
Layout component 'intellij.database.dialects.mysqlbase.impl' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.mysqlbase.impl.jar'
Layout component 'intellij.database.dialects.oracle' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.oracle.jar'
Layout component 'intellij.database.dialects.oracle.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.oracle.ex.jar'
Layout component 'intellij.database.dialects.postgres' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgres.jar'
Layout component 'intellij.database.dialects.postgres.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgres.ex.jar'
Layout component 'intellij.database.dialects.postgresbase' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresbase.jar'
Layout component 'intellij.database.dialects.postgresbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresbase.ex.jar'
Layout component 'intellij.database.dialects.postgresgreenplumbase' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresgreenplumbase.jar'
Layout component 'intellij.database.dialects.postgresgreenplumbase.ex' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.postgresgreenplumbase.ex.jar'
Layout component 'intellij.database.dialects.redis' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.redis.jar'
Layout component 'intellij.database.dialects.redis.backend' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.redis.backend.jar'
Layout component 'intellij.database.dialects.sql92' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.sql92.jar'
Layout component 'intellij.database.dialects.sqlite' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.dialects.sqlite.jar'
Layout component 'intellij.database.sql' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.sql.jar'
Layout component 'intellij.database.sql.backend.core' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.sql.backend.core.jar'
Layout component 'intellij.database.sql.common.core' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.sql.common.core.jar'
Layout component 'intellij.database.sql.common.impl' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.sql.common.impl.jar'
Layout component 'intellij.database.sql.core.impl' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.sql.core.impl.jar'
Layout component 'intellij.database.util' has some nonexistent 'classPath' elements: 'plugins\DatabaseTools\lib\modules\intellij.database.util.jar'
Layout component 'intellij.driver.client' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.client.jar'
Layout component 'intellij.driver.impl' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.impl.jar'
Layout component 'intellij.driver.model' has some nonexistent 'classPath' elements: 'plugins\performanceTesting\lib\modules\intellij.driver.model.jar'
Layout component 'intellij.java.debugger.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.debugger.impl.shared.jar'
Layout component 'intellij.java.execution.impl.shared' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.execution.impl.shared.jar'
Layout component 'intellij.java.frontback.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.impl.jar'
Layout component 'intellij.java.frontback.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.jar'
Layout component 'intellij.java.frontback.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.frontback.psi.impl.jar'
Layout component 'intellij.java.psi' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.jar'
Layout component 'intellij.java.psi.impl' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.psi.impl.jar'
Layout component 'intellij.java.syntax' has some nonexistent 'classPath' elements: 'plugins\java\lib\modules\intellij.java.syntax.jar'
Layout component 'intellij.javascript.debugger.ui' has some nonexistent 'classPath' elements: 'plugins\javascript-debugger\lib\modules\intellij.javascript.debugger.ui.jar'
Layout component 'intellij.javascript.debugger.ui.shared' has some nonexistent 'classPath' elements: 'plugins\javascript-debugger\lib\modules\intellij.javascript.debugger.ui.shared.jar'
Layout component 'intellij.javascript.wip.backend' has some nonexistent 'classPath' elements: 'plugins\javascript-debugger\lib\modules\intellij.javascript.wip.backend.jar'
Layout component 'intellij.javascript.wip.protocol' has some nonexistent 'classPath' elements: 'plugins\javascript-debugger\lib\modules\intellij.javascript.wip.protocol.jar'
Layout component 'intellij.json' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.jar'
Layout component 'intellij.markdown' has some nonexistent 'classPath' elements: 'plugins\markdown\lib\modules\intellij.markdown.jar'
Layout component 'intellij.platform.debugger.impl.rpc' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.debugger.impl.rpc.jar'
Layout component 'intellij.platform.debugger.impl.shared' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.debugger.impl.shared.jar'
Layout component 'intellij.platform.eel.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.eel.impl.jar'
Layout component 'intellij.platform.find' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.find.jar'
Layout component 'intellij.platform.managed.cache' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.managed.cache.jar'
Layout component 'intellij.platform.polySymbols' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.polySymbols.jar'
Layout component 'intellij.platform.polySymbols.backend' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.polySymbols.backend.jar'
Layout component 'intellij.platform.scopes' has some nonexistent 'classPath' elements: 'lib\modules\intellij.platform.scopes.jar'
Layout component 'intellij.properties' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.jar'
Layout component 'intellij.properties.psi' has some nonexistent 'classPath' elements: 'plugins\properties\lib\modules\intellij.properties.psi.jar'
Layout component 'intellij.qodana.sarif' has some nonexistent 'classPath' elements: 'plugins\qodana\lib\modules\intellij.qodana.sarif.jar'
Layout component 'intellij.toml.core' has some nonexistent 'classPath' elements: 'plugins\toml\lib\modules\intellij.toml.core.jar'
Layout component 'intellij.vcs.git.shared' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git.shared.jar'
Layout component 'intellij.vcs.git/localHistory' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git\localHistory.jar'
Layout component 'intellij.vcs.github' has some nonexistent 'classPath' elements: 'plugins\vcs-github-IU\lib\modules\intellij.vcs.github.jar'
Layout component 'intellij.vcs.gitlab' has some nonexistent 'classPath' elements: 'plugins\vcs-gitlab-IU\lib\modules\intellij.vcs.gitlab.jar'
Layout component 'intellij.xml.analysis' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.analysis.jar'
Layout component 'intellij.xml.dom' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.dom.jar'
Layout component 'intellij.xml.dom.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.dom.impl.jar'
Layout component 'intellij.xml.frontback.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.frontback.impl.jar'
Layout component 'intellij.xml.psi' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.psi.jar'
Layout component 'intellij.xml.psi.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.psi.impl.jar'
Layout component 'intellij.xml.structureView' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.structureView.jar'
Layout component 'intellij.xml.structureView.impl' has some nonexistent 'classPath' elements: 'lib\modules\intellij.xml.structureView.impl.jar'
Layout component 'intellij.yaml' has some nonexistent 'classPath' elements: 'plugins\yaml\lib\modules\intellij.yaml.jar'
2026-05-22T05:56:21 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\distributions\WindowAccent-1.0.4.zip
2026-05-22T05:56:27 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:1.0.4 against IU-252.28539.54

2026-05-22T05:58:24 [main] INFO  verification - Finished 1 of 1 verifications (in 117,4 s): IU-252.28539.54 against WindowAccent:1.0.4: Compatible
Plugin WindowAccent:1.0.4 against IU-252.28539.54: Compatible
Dynamic Plugin Eligibility:
    Plugin can probably be enabled or disabled without IDE restart

2026-05-22T05:58:25 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 51 s 794 ms
2026-05-22T05:58:25 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 139,40 MB
2026-05-22T05:58:25 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,85 GB
2026-05-22T05:58:25 [main] INFO  verification - Verification reports for WindowAccent:1.0.4 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier\IU-252.28539.54
2026-05-22T05:58:25 [main] INFO  verification - Total time spent in plugin verification: 2 m 3 s 717 ms
Build 679bdb00-ef9b-4573-bc8c-ca45c175a4fa is started
Build 679bdb00-ef9b-4573-bc8c-ca45c175a4fa is closed

See the complete report at file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/configuration-cache/h54o76ni36nhctwkb5zfwgmi/b98xmaqs3tt6alrxo3966zkt8/configuration-cache-report.html

BUILD SUCCESSFUL in 10m 31s
14 actionable tasks: 14 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent]
Configuration cache entry stored.
```