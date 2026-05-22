```markdown
$ ./gradlew verifyPlugin --rerun-tasks --info
Initialized native services in: C:\Users\alexander.goudemond\.gradle\native
Initialized jansi services in: C:\Users\alexander.goudemond\.gradle\native
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
MINGW support requires --add-opens java.base/java.lang=ALL-UNNAMED
Removing 0 daemon stop events from registry
Previous Daemon (32168) stopped at Fri May 22 05:12:32 SAST 2026 by user or operating system
Starting a Gradle Daemon, 1 busy and 1 stopped Daemons could not be reused, use --status for details
Starting process 'Gradle build daemon'. Working directory: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0 Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.xml/javax.xml.namespace=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\wrapper\dists\gradle-9.5.0-bin\bvnork1r7n8i6kp5cnkibsc9q\gradle-9.5.0\lib\gradle-daemon-main-9.5.0.jar -javaagent:C:\Users\alexander.goudemond\.gradle\wrapper\dists\gradle-9.5.0-bin\bvnork1r7n8i6kp5cnkibsc9q\gradle-9.5.0\lib\agents\gradle-instrumentation-agent-9.5.0.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 9.5.0
Successfully started process 'Gradle build daemon'
An attempt to start the daemon took 1.048 secs.
The client will now receive all logging from the daemon (pid: 6892). The daemon log file: C:\Users\alexander.goudemond\.gradle\daemon\9.5.0\daemon-6892.out.log
Starting build in new daemon [memory: 7.9 GiB]
Using 16 worker leases.
Operational build model parameters: {cachingModelBuilding=false, configurationCache=true, configurationCacheDisabledReason=null, configurationCacheParallelLoad=true, configurationCacheParallelStore=false, configureOnDemand=false, invalidateCoupledProjects=false, isolatedProjects=false, modelAsProjectDependency=false, modelBuilding=false, parallelModelBuilding=false, parallelProjectConfiguration=false, parallelProjectExecution=true, resilientModelBuilding=false}
Received JVM installation metadata from 'C:\myprograms\java\jdk\jdk21.0.7': {JAVA_HOME=C:\myprograms\java\jdk\jdk21.0.7, JAVA_VERSION=21.0.7, JAVA_VENDOR=Microsoft, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=21.0.7+6-LTS, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=21.0.7+6-LTS, VM_VENDOR=Microsoft, OS_ARCH=amd64}
Encryption key source: default Gradle keystore (pkcs12)
Calculating task graph as configuration cache cannot be reused because file 'build.gradle.kts' has changed.
Watching the file system is configured to be enabled if available
Now considering [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent] as hierarchies to watch
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
Build 8cc57b4b-e016-4df7-8bcc-12619986df93 is started
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
Directory 'C:\myprograms\jdk\jdk-11.0.17' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jdk1.6.0_45' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre6' (Windows Registry) auto-detected used for java installations does not exist
Directory 'C:\myprograms\jdk\jre8' (Windows Registry) auto-detected used for java installations does not exist
Starting process 'command 'C:\Users\alexander.goudemond\.jdks\corretto-24.0.2\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\.tmp\tmp-jvm4089705430464345603probe Command: C:\Users\alexander.goudemond\.jdks\corretto-24.0.2\bin\java.exe -Xmx32m -Xms32m -cp . JavaProbe
Successfully started process 'command 'C:\Users\alexander.goudemond\.jdks\corretto-24.0.2\bin\java.exe''
Received JVM installation metadata from 'C:\Users\alexander.goudemond\.jdks\corretto-24.0.2': {JAVA_HOME=C:\Users\alexander.goudemond\.jdks\corretto-24.0.2, JAVA_VERSION=24.0.2, JAVA_VENDOR=Amazon.com Inc., RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=24.0.2+12-FR, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=24.0.2+12-FR, VM_VENDOR=Amazon.com Inc., OS_ARCH=amd64}
Starting process 'command 'C:\Users\alexander.goudemond\.jdks\ms-21.0.7\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\.tmp\tmp-jvm13735167232342804081probe Command: C:\Users\alexander.goudemond\.jdks\ms-21.0.7\bin\java.exe -Xmx32m -Xms32m -cp . JavaProbe
Successfully started process 'command 'C:\Users\alexander.goudemond\.jdks\ms-21.0.7\bin\java.exe''
Received JVM installation metadata from 'C:\Users\alexander.goudemond\.jdks\ms-21.0.7': {JAVA_HOME=C:\Users\alexander.goudemond\.jdks\ms-21.0.7, JAVA_VERSION=21.0.7, JAVA_VENDOR=Microsoft, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=21.0.7+6-LTS, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=21.0.7+6-LTS, VM_VENDOR=Microsoft, OS_ARCH=amd64}
Starting process 'command 'C:\Users\alexander.goudemond\.jdks\openjdk-24.0.2+12-54\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\.tmp\tmp-jvm4877893220636536213probe Command: C:\Users\alexander.goudemond\.jdks\openjdk-24.0.2+12-54\bin\java.exe -Xmx32m -Xms32m -cp . JavaProbe
Successfully started process 'command 'C:\Users\alexander.goudemond\.jdks\openjdk-24.0.2+12-54\bin\java.exe''
Received JVM installation metadata from 'C:\Users\alexander.goudemond\.jdks\openjdk-24.0.2+12-54': {JAVA_HOME=C:\Users\alexander.goudemond\.jdks\openjdk-24.0.2+12-54, JAVA_VERSION=24.0.2, JAVA_VENDOR=Oracle Corporation, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=24.0.2+12-54, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=24.0.2+12-54, VM_VENDOR=Oracle Corporation, OS_ARCH=amd64}
Starting process 'command 'C:\myprograms\java\jdk\jdk17.0.11_9\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\.tmp\tmp-jvm9681782754623853332probe Command: C:\myprograms\java\jdk\jdk17.0.11_9\bin\java.exe -Xmx32m -Xms32m -cp . JavaProbe
Successfully started process 'command 'C:\myprograms\java\jdk\jdk17.0.11_9\bin\java.exe''
Received JVM installation metadata from 'C:\myprograms\java\jdk\jdk17.0.11_9': {JAVA_HOME=C:\myprograms\java\jdk\jdk17.0.11_9, JAVA_VERSION=17.0.11, JAVA_VENDOR=Amazon.com Inc., RUNTIME_NAME=OpenJDK Runtime EnvStarting process 'command 'C:\Users\alexander.goudemond\.jdks\ms-21.0.9\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\.tmp\tmp-jvm10542848221110363801probe Command: C:\Users\alexander.goudemond\.jdks\ms-21.0.9\bin\java.exe -Xmx32m -Xms32m -cp . JavaProbe
Successfully started process 'command 'C:\Users\alexander.goudemond\.jdks\ms-21.0.9\bin\java.exe''
Received JVM installation metadata from 'C:\Users\alexander.goudemond\.jdks\ms-21.0.9': {JAVA_HOME=C:\Users\alexander.goudemond\.jdks\ms-21.0.9, JAVA_VERSION=21.0.9, JAVA_VENDOR=Microsoft, RUNTIME_NAME=OpenJDK Resource missing. [HTTP GET: https://download-cdn.jetbrains.com/idea/ideaIU-2025.1-win.zip]
Downloading https://download.jetbrains.com/idea/ideaIU-2025.1.win.zip to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download7689267534645546727bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\md5-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha256-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha1-checksums.bin
Invalidating in-memory cache of C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\9.5.0\checksums\sha512-checksums.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\journal-1\file-access.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\kotlinDslCompileAvoidanceClasspathHashCache.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\fileHashes.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\KotlinMetadataCompatibilityCache.bin
Invalidating in-memory cache of C:\Users\alexander.goudemond\.gradle\caches\9.5.0\fileHashes\resourceHashesCache.bin
[org.jetbrains.intellij.platform] LocalIvyArtifactPathComponentMetadataRule has been registered.
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/idea/ideaIU/2025.1.7.1/ideaIU-2025.1.7.1.pom]
Resource missing. [HTTP HEAD: https://download-cdn.jetbrains.com/idea/ideaIU-2025.1.7.1-win.zip]
Resource missing. [HTTP GET: https://d2cico3c979uwg.cloudfront.net/idea/ideaIU/2025.1.7.1/ideaIU-2025.1.7.1.pom]
Resource missing. [HTTP GET: https://d2s4y8xcwt8bet.cloudfront.net/idea/ideaIU/2025.1.7.1/ideaIU-2025.1.7.1.pom]
Resource missing. [HTTP GET: https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/idea/ideaIU/2025.1.7.1/ideaIU-2025.1.7.1.pom]
Resource missing. [HTTP GET: https://artifacts-caching-proxy.aws.intellij.net/plugins.jetbrains.com/maven/idea/ideaIU/2025.1.7.1/ideaIU-2025.1.7.1.pom]
Resource missing. [HTTP GET: https://download-cdn.jetbrains.com/idea/ideaIU-2025.1.7.1-win.zip]
Downloading https://download.jetbrains.com/idea/ideaIU-2025.1.7.1.win.zip to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download15838395783374563968bin
Caching disabled for ExtractorTransformer: C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\idea\ideaIU\2025.1.7.1\5148e5a5023d00c8fddba5f874adcaf031950778\ideaIU-2025.1.7.1-win.zip because:
  Caching not enabled.
[org.jetbrains.intellij.platform] Extracting archive 'C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\idea\ideaIU\2025.1.7.1\5148e5a5023d00c8fddba5f874adcaf031950778\ideaIU-2025.1.7.1-win.zip' t[org.jetbrains.intellij.platform] Resolving the content directory in 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win'.
[org.jetbrains.intellij.platform] The content directory is 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win'.
[org.jetbrains.intellij.platform] Extracting to 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win' completed.
Starting process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/jbr/bin/java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/jbr/bin/java.Successfully started process 'command 'C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/jbr/bin/java.exe''
Tasks to be executed: [task ':initializeIntellijPlatformPlugin', task ':patchPluginXml', task ':generateManifest', task ':checkKotlinGradlePluginConfigurationErrors', task ':compileKotlin', task ':compileJava', task ':processResources', task ':classes', task ':instrumentCode', task ':jar', task ':instrumentedJar', task ':composedJar', task ':prepareSandbox', task ':buildSearchableOptions', task ':prepareJarSearchableOptions', task ':jarSearchableOptions', task ':buildPlugin', task ':verifyPlugin']
Tasks that were excluded: []
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-ant-tasks/251.23774.435/java-compiler-ant-tasks-251.23774.435.pom to CResource missing. [HTTP HEAD: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-ant-tasks/251.23774.435/java-compiler-ant-tasks-251.23774.435.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-instrumentation-util-java8/251.23774.435/java-compiler-instrumentation-util-java8-251.23774.435.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-gui-forms-compiler/251.23774.435/java-gui-forms-compiler-251.23774.435.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-compiler-instrumentation-util/251.23774.435/java-compiler-instrumentation-util-251.23774.435.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/java/java-gui-forms-rt/251.23774.435/java-gui-forms-rt-251.23774.435.pom]
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util/251.23774.435/java-compiler-instrumentation-util-Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-gui-forms-compiler/251.23774.435/java-gui-forms-compiler-251.23774.435.pom to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download15186116964727202569bin
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-gui-forms-rt/251.23774.435/java-gui-forms-rt-251.23774.435.pom to C:\Users\alexResource missing. [HTTP GET: https://repo.maven.apache.org/maven2/com/jetbrains/intellij/platform/util-jdom/251.23774.435/util-jdom-251.23774.435.pom]
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/platform/util-jdom/251.23774.435/util-jdom-251.23774.435.pom to C:\Users\alexander.goudemDownloading https://repo.maven.apache.org/maven2/org/jetbrains/annotations/24.0.0/annotations-24.0.0.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download8973636135385687193bin
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-ant-tasks/251.23774.435/java-compiler-ant-tasks-251.23774.435.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download17682474736973526730bin
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util-java8/251.23774.435/java-compiler-instrumentation-util-java8-251.23774.435.jar to C:\Users\alexander.goudemond\.gradle\.tmp\gradle_download10153137783776295478bin
Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-compiler-instrumentation-util/251.23774.435/java-compiler-instrumentation-util-Downloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/java/java-gui-forms-compiler/251.23774.435/java-gui-forms-compiler-251.23774.435.jar to CDownloading https://cache-redirector.jetbrains.com/www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/platform/util-jdom/251.23774.435/util-jdom-251.23774.435.jar to C:\Users\alexander.goudemBuild 8cc57b4b-e016-4df7-8bcc-12619986df93 is closed
Using local directory build cache for the root build (location = C:\Users\alexander.goudemond\.gradle\caches\build-cache-1, remove unused entries = after 7 days).
Ignoring listeners of task graph ready event, as this build (:) has already executed work.
Resolve mutations for :initializeIntellijPlatformPlugin (Thread[#122,Execution worker Thread 3,5,main]) started.
Resolve mutations for :checkKotlinGradlePluginConfigurationErrors (Thread[#134,Execution worker Thread 15,5,main]) started.
:checkKotlinGradlePluginConfigurationErrors (Thread[#134,Execution worker Thread 15,5,main]) started.
:initializeIntellijPlatformPlugin (Thread[#122,Execution worker Thread 3,5,main]) started.

> Task :checkKotlinGradlePluginConfigurationErrors SKIPPED
Skipping task ':checkKotlinGradlePluginConfigurationErrors' as task onlyIf 'errorDiagnostics are present' is false.
Resolve mutations for :compileKotlin (Thread[#134,Execution worker Thread 15,5,main]) started.
:compileKotlin (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :initializeIntellijPlatformPlugin
Caching disabled for task ':initializeIntellijPlatformPlugin' because:
  Task is untracked because: Should always run
Task ':initializeIntellijPlatformPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Resolve mutations for :patchPluginXml (Thread[#119,included builds,5,main]) started.
Resolve mutations for :generateManifest (Thread[#122,Execution worker Thread 3,5,main]) started.
:patchPluginXml (Thread[#119,included builds,5,main]) started.
:generateManifest (Thread[#122,Execution worker Thread 3,5,main]) started.

> Task :generateManifest
Build cache key for task ':generateManifest' is 2a3c1aabcedd47e8f04a384cf8d35f64
Task ':generateManifest' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':generateManifest' with cache key 2a3c1aabcedd47e8f04a384cf8d35f64

> Task :patchPluginXml
Build cache key for task ':patchPluginXml' is 4e56f66039b598b4fa38104207ab88ea
Task ':patchPluginXml' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':patchPluginXml' with cache key 4e56f66039b598b4fa38104207ab88ea
Resolve mutations for :processResources (Thread[#119,included builds,5,main]) started.
:processResources (Thread[#119,included builds,5,main]) started.

> Task :processResources
Caching disabled for task ':processResources' because:
  Not worth caching
Task ':processResources' is not up-to-date because:
  Executed with '--rerun-tasks'.

> Task :compileKotlin
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\platform-loader.jar is b673abc1c69a5c6acc726cff20518f57
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\stats.jar is 85da20eed70038d9d6a0236a7753b533
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util_rt.jar is de6f265f21c92d0ca9fe133e69d213b6
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\trove.jar is 34fd830cac33b0f6cee8fc9334a23b63
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\opentelemetry.jar is 2a35725d1b3832f3aa21414d62452b84
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app.jar is 2b268ff2a9f4083f3f401ade9211ae85
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util-8.jar is f9316012785424a9d328af9eb6404f99
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app-client.jar is e08076a05dd9571b9d4e28ed423046d8
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util.jar is b5f5975a284be02530dbbdadf6ffd17f
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib-client.jar is cf0cda2b624445d195895b825d651857
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\jps-model.jar is 240186d03775e51089d37077f69c86b2
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\rd.jar is a15ee18594e144cbd88ea896568514fc
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product.jar is e9d21afa76c86f73cae4ee35b0cdf7fd
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\protobuf.jar is 9786c4465b4bed432164f301bd85343e
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\bouncy-castle.jar is 071d94c2ca8a93e6f498381d832523d6
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\intellij-test-discovery.jar is cc15d8a14fa9138664d2df6b56e61ddc
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\platform-loader.jar with cache key b673abc1c69a5c6acc726cff20518f57
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\trove.jar with cache key 34fd830cac33b0f6cee8fc9334a23b63
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util_rt.jar with cache key de6f265f21c92d0ca9fe133e69d213b6
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\intellij-test-discovery.jar with cache key cc15d8a14fa9138664d2df6b56e61ddc
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\forms_rt.jar is 91a78153947bde79944855cebf29aa02
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib.jar is 98b44748fafc87f42a9e9205ccd003de
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\jps-model.jar with cache key 240186d03775e51089d37077f69c86b2
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\externalProcess-rt.jar is 4cf62248bfd8391e2cf8dfdfccdb2f81
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\opentelemetry.jar with cache key 2a35725d1b3832f3aa21414d62452b84
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\groovy.jar is 82257d51bd9a8c4c5915b8ee993ba5df
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\annotations.jar is 50ea5feebc65fd9ddb30f01f742ddfe4
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\protobuf.jar with cache key 9786c4465b4bed432164f301bd85343e
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\idea_rt.jar is 3fa8e732e71f9e349f01a7a0476406fa
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\forms_rt.jar with cache key 91a78153947bde79944855cebf29aa02
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\kotlinx-coroutines-slf4j-1.8.0-intellij.jar is a03e310f4abbbc8e06517732615ef483
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\externalProcess-rt.jar with cache key 4cf62248bfd8391e2cf8dfdfccdb2f81
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product-client.jar is 5b68fd651d30d432a5848a45d1172444
Build cache key for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\nio-fs.jar is 9d9fec5441e7f65bce847dcb48087cbd
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\annotations.jar with cache key 50ea5feebc65fd9ddb30f01f742ddfe4
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\idea_rt.jar with cache key 3fa8e732e71f9e349f01a7a0476406fa
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\nio-fs.jar with cache key 9d9fec5441e7f65bce847dcb48087cbd
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product-client.jar with cache key 5b68fd651d30d432a5848a45d1172444
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\bouncy-castle.jar with cache key 071d94c2ca8a93e6f498381d832523d6
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\kotlinx-coroutines-slf4j-1.8.0-intellij.jar with cache key a03e310f4abbbc8e06517732615ef483
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\groovy.jar with cache key 82257d51bd9a8c4c5915b8ee993ba5df
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util.jar with cache key b5f5975a284be02530dbbdadf6ffd17f
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\stats.jar with cache key 85da20eed70038d9d6a0236a7753b533
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product.jar with cache key e9d21afa76c86f73cae4ee35b0cdf7fd
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\rd.jar with cache key a15ee18594e144cbd88ea896568514fc
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib.jar with Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app.jar with Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib-client.jar with cache key cf0cda2b624445d195895b825d651857
Stored cache entry for BuildToolsApiClasspathEntrySnapshotTransform: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app-client.jar with cache key e08076a05dd9571b9d4e28ed423046d8
Build cache key for task ':compileKotlin' is 056eb192d4f754ffaa942bad98333b34
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
Options for KOTLIN DAEMON: IncrementalCompilationOptions(super=CompilationOptions(compilerMode=INCREMENTAL_COMPILER, targetPlatform=JVM, reportCategories=[0, 3], reportSeverity=2, requestedCompilationResults=[0], kotlinScriptExtensions=[]), sourceChanges=org.jetbrains.kotlin.buildtools.api.SourcesChanges$Unknown@75daebe3, classpathChanges=NotAvailableForNonIncrementalRun, workingDir=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\kotlin\compileKotlin\cacheable, multiModuleICSettings=MultiModuleICSettings(buildHistoryFile=C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jFinished executing kotlin compiler using DAEMON strategy
Stored cache entry for task ':compileKotlin' with cache key 056eb192d4f754ffaa942bad98333b34
Resolve mutations for :compileJava (Thread[#134,Execution worker Thread 15,5,main]) started.
:compileJava (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :compileJava NO-SOURCE
Skipping task ':compileJava' as it has no source files and no previous output files.
Resolve mutations for :classes (Thread[#134,Execution worker Thread 15,5,main]) started.
:classes (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
Resolve mutations for :instrumentCode (Thread[#134,Execution worker Thread 15,5,main]) started.
:instrumentCode (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :instrumentCode SKIPPED
Skipping task ':instrumentCode' as task onlyIf 'Task is enabled' is false.
Resolve mutations for :jar (Thread[#134,Execution worker Thread 15,5,main]) started.
:jar (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Not worth caching
Task ':jar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\classes\java\main', not found
Resolve mutations for :instrumentedJar (Thread[#134,Execution worker Thread 15,5,main]) started.
:instrumentedJar (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :instrumentedJar
Build cache key for task ':instrumentedJar' is e0821d031d09529156b43cf85438bf0a
Task ':instrumentedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
file or directory 'C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\instrumented\instrumentCode', not found
Stored cache entry for task ':instrumentedJar' with cache key e0821d031d09529156b43cf85438bf0a
Resolve mutations for :composedJar (Thread[#134,Execution worker Thread 15,5,main]) started.
:composedJar (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :composedJar
Build cache key for task ':composedJar' is 03d65745e6b5fca448b8dbac2d4f93a9
Task ':composedJar' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':composedJar' with cache key 03d65745e6b5fca448b8dbac2d4f93a9
Resolve mutations for :prepareSandbox (Thread[#134,Execution worker Thread 15,5,main]) started.
:prepareSandbox (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :prepareSandbox
Caching disabled for task ':prepareSandbox' because:
  Not worth caching
Task ':prepareSandbox' is not up-to-date because:
  Executed with '--rerun-tasks'.
[org.jetbrains.intellij.platform] Preparing sandbox
[org.jetbrains.intellij.platform] sandboxConfigDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.1\config
[org.jetbrains.intellij.platform] sandboxPluginsDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.1\plugins
[org.jetbrains.intellij.platform] sandboxLogDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.1\log
[org.jetbrains.intellij.platform] sandboxSystemDirectory = C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\idea-sandbox\IU-2025.1\system
[org.jetbrains.intellij.platform] testSandbox = false
Resolve mutations for :buildSearchableOptions (Thread[#120,Execution worker,5,main]) started.
:buildSearchableOptions (Thread[#120,Execution worker,5,main]) started.

> Task :buildSearchableOptions
Build cache key for task ':buildSearchableOptions' is db274cb90e7c9a812022bc7dc7c80fba
Task ':buildSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Starting process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\jbr\bin\java.exe''. Working directory: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win Command: C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\jbr\bin\java.exe -Daether.connector.resumeDownloads=false -Dcompose.swing.render.on.graphics=true -Dide.native.launcher=false -Didea.config.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.1/config -Didea.kotlin.plugin.use.k2=true -Didea.l10n.keys=only -Didea.log.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.1/log -Didea.paths.selector=IntelliJIdea2025.1 -Didea.plugins.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.1/plugins -Didea.required.plugins.id=WindowAccent -Didea.system.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.1/system -Didea.vendor.name=JetBrains -Dintellij.platform.runtime.repository.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/modules/module-descriptors.jar -Dio.netty.allocator.type=pooled -Djava.nio.file.spi.DefaultFileSystemProvider=com.intellij.platform.core.nio.fs.MultiRoutingFileSystemProvider -Djava.system.class.loader=com.intellij.util.lang.PathClassLoader -Djava.util.zip.use.nio.for.zip.file.access=true -Djbr.catch.SIGABRT=true -Djdk.attach.allowAttachSelf=true -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.module.illegalAccess.silent=true -Djdk.nio.maxCachedBufferSize=2097152 -Djna.boot.library.path=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/lib/jna/amd64 -Djna.noclasspath=true -Djna.nosys=true -Dplugin.path=C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/idea-sandbox/IU-2025.1/plugins/WindowAccent -Dpty4j.preferred.native.folder=C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/lib/pty4j -Dsplash=true -Dsun.io.useCanonCaches=false -Dsun.java2d.metal=true -Dwsl.use.remote.agent.for.nio.filesystem=true -XX:ReservedCodeCacheSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -XX:CICompilerCount=2 -XX:+IgnoreUnrecognizedVMOptions -XX:+UnlockDiagnosticVMOptions -XX:TieredOldPercentage=100000 -javaagent:C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.intellijPlatform\coroutines-javaagent-legacy.jar -Xbootclasspath/a:C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/lib/nio-fs.jar --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.ref=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED --add-opens=java.base/sun.net.dns=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.fs=ALL-UNNAMED --add-opens=java.base/sun.security.ssl=ALL-UNNAMED --add-opens=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.desktop/com.sun.java.swing=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED --add-opens=java.desktop/java.awt.event=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED --add-opens=java.desktop/java.awt.image=ALL-UNNAMED --add-opens=java.desktop/java.awt.peer=ALL-UNNAMED --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text=ALL-UNNAMED --add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED --add-opens=java.desktop/sun.font=ALL-UNNAMED --add-opens=java.desktop/sun.java2d=ALL-UNNAMED --add-opens=java.desktop/sun.swing=ALL-UNNAMED --add-opens=java.management/sun.management=ALL-UNNAMED --add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED -Xms128m -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -ea -cp C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\platform-loader.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util-8.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\util_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\trove.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\app.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\opentelemetry.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\jps-model.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\stats.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\rd.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\external-system-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\protobuf.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\bouncy-castle.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\intellij-test-discovery.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\forms_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\lib.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\externalProcess-rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\groovy.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\annotations.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\idea_rt.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\jsch-agent.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\kotlinx-coroutines-slf4j-1.8.0-intellij.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\lib\product-client.jar;C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\jbr\lib\tools com.intellij.idea.Main traverseUI C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/tmp/buildSearchableOptions true
Successfully started process 'command 'C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win\jbr\bin\java.exe''
2026-05-22 06:24:37,246 [   5373]   WARN - #c.i.i.u.UIThemeBean - Unknown field: CombinedDiff
2026-05-22 06:24:37,247 [   5374]   WARN - #c.i.i.u.UIThemeBean - Unknown field: BlockBorder
2026-05-22 06:24:46,251 [  14378]   WARN - #c.i.u.j.JBCefApp - JCEF is manually disabled in headless env via 'ide.browser.jcef.headless.enabled=false'
2026-05-22 06:24:47,006 [  15133]   WARN - #c.i.o.p.i.JavaHomeFinderWindows - Failed to scan PATH for JDKs. `%IntelliJ IDEA%`: Not a valid absolute path
com.intellij.platform.eel.path.EelPathException: `%IntelliJ IDEA%`: Not a valid absolute path
        at com.intellij.platform.eel.path.EelPath$Companion.parse(EelPath.kt:23)
        at com.intellij.openapi.projectRoots.impl.EelSystemInfoProvider.getPath(JavaHomeFinderEel.kt:33)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findInPATH(JavaHomeFinderBasic.java:146)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findExistingJdks(JavaHomeFinderBasic.java:107)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic.findExistingJdkEntries(JavaHomeFinderBasic.java:122)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.findJdks(JavaHomeFinder.java:133)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.findJdks(JavaHomeFinder.java:129)
        at com.intellij.openapi.projectRoots.impl.JavaHomeFinder.suggestHomePaths(JavaHomeFinder.java:100)
        at com.intellij.openapi.projectRoots.impl.JavaSdkImpl.suggestHomePaths(JavaSdkImpl.java:207)
        at com.intellij.openapi.projectRoots.impl.JavaSdkImpl.suggestHomePaths(JavaSdkImpl.java:202)
        at com.intellij.openapi.projectRoots.impl.DefaultJdkConfiguratorImpl.guessJavaHome(DefaultJdkConfiguratorImpl.java:13)
        at com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl.guessJdk(JavaAwareProjectJdkTableImpl.java:59)
        at com.intellij.openapi.progress.impl.CoreProgressManager$1.run(CoreProgressManager.java:319)
        at com.intellij.openapi.progress.impl.CoreProgressManager.startTask(CoreProgressManager.java:497)
        at com.intellij.openapi.progress.impl.ProgressManagerImpl.startTask(ProgressManagerImpl.java:118)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcessWithProgressSynchronously$10(CoreProgressManager.java:587)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$new$0(ProgressRunner.java:88)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$submit$4(ProgressRunner.java:252)
        at com.intellij.openapi.progress.ProgressManager.lambda$runProcess$0(ProgressManager.java:98)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcess$1(CoreProgressManager.java:229)
        at com.intellij.platform.diagnostic.telemetry.helpers.TraceKt.use(trace.kt:43)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$runProcess$2(CoreProgressManager.java:228)
        at com.intellij.openapi.progress.impl.CoreProgressManager.lambda$executeProcessUnderProgress$14(CoreProgressManager.java:680)
        at com.intellij.openapi.progress.impl.CoreProgressManager.registerIndicatorAndRun(CoreProgressManager.java:755)
        at com.intellij.openapi.progress.impl.CoreProgressManager.computeUnderProgress(CoreProgressManager.java:711)
        at com.intellij.openapi.progress.impl.CoreProgressManager.executeProcessUnderProgress(CoreProgressManager.java:679)
        at com.intellij.openapi.progress.impl.ProgressManagerImpl.executeProcessUnderProgress(ProgressManagerImpl.java:77)
        at com.intellij.openapi.progress.impl.CoreProgressManager.runProcess(CoreProgressManager.java:209)
        at com.intellij.openapi.progress.ProgressManager.runProcess(ProgressManager.java:98)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$submit$5(ProgressRunner.java:252)
        at com.intellij.openapi.progress.impl.ProgressRunner$ProgressRunnable.run(ProgressRunner.java:513)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$launchTask$18(ProgressRunner.java:478)
        at com.intellij.util.concurrency.ChildContext$runInChildContext$1.invoke(propagation.kt:102)
        at com.intellij.util.concurrency.ChildContext$runInChildContext$1.invoke(propagation.kt:102)
        at com.intellij.util.concurrency.ChildContext.runInChildContext(propagation.kt:108)
        at com.intellij.util.concurrency.ChildContext.runInChildContext(propagation.kt:102)
        at com.intellij.openapi.progress.impl.ProgressRunner.lambda$launchTask$19(ProgressRunner.java:474)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1$1.run(Executors.java:735)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1$1.run(Executors.java:732)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:400)
        at java.base/java.util.concurrent.Executors$PrivilegedThreadFactory$1.run(Executors.java:732)
        at java.base/java.lang.Thread.run(Thread.java:1583)
2026-05-22 06:24:47,813 [  15940]   WARN - #c.i.o.u.BrowseFolderRunnable - multiple selection not supported
Found 391 configurables
save to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\tmp\buildSearchableOptions
Stored cache entry for task ':buildSearchableOptions' with cache key db274cb90e7c9a812022bc7dc7c80fba
Resolve mutations for :prepareJarSearchableOptions (Thread[#120,Execution worker,5,main]) started.
:prepareJarSearchableOptions (Thread[#120,Execution worker,5,main]) started.

> Task :prepareJarSearchableOptions
Build cache key for task ':prepareJarSearchableOptions' is 18b4d901da1e5da0dd06a5642764678c
Task ':prepareJarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':prepareJarSearchableOptions' with cache key 18b4d901da1e5da0dd06a5642764678c
Resolve mutations for :jarSearchableOptions (Thread[#120,Execution worker,5,main]) started.
:jarSearchableOptions (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :jarSearchableOptions
Build cache key for task ':jarSearchableOptions' is b9d2c866fc9b86b9a02d0902f047273b
Task ':jarSearchableOptions' is not up-to-date because:
  Executed with '--rerun-tasks'.
Stored cache entry for task ':jarSearchableOptions' with cache key b9d2c866fc9b86b9a02d0902f047273b
Resolve mutations for :buildPlugin (Thread[#134,Execution worker Thread 15,5,main]) started.
:buildPlugin (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :buildPlugin
Caching disabled for task ':buildPlugin' because:
  Zip based tasks do not benefit from caching
Task ':buildPlugin' is not up-to-date because:
  Executed with '--rerun-tasks'.
Resolve mutations for :verifyPlugin (Thread[#134,Execution worker Thread 15,5,main]) started.
:verifyPlugin (Thread[#134,Execution worker Thread 15,5,main]) started.

> Task :verifyPlugin
Caching disabled for task ':verifyPlugin' because:
  Task is untracked because: Should always run
Task ':verifyPlugin' is not up-to-date because:
  Task is untracked because: Should always run
Starting process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''. Working directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent Command: C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe -Dfile.encoding=UTF-8 -Duser.country=ZA -Duser.language=en -Duser.variant -cp C:\Users\alexander.goudemond\.gradle\caches\modules-2\files-2.1\org.jetbrains.intellij.plugins\verifier-cli\1.405\ff594280b5bcae48d0e09a2b78066ee019e50081\verifier-cli-1.405-all.jar com.jetbrains.pluginverifier.PluginVerifierMain check-plugin -verification-reports-dir C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/pluginVerifier -runtime-dir C:/Users/alexander.goudemond/.gradle/caches/9.5.0/transforms/dc92de66cbd583b708f233ab4d8de54d/transformed/ideaIU-2025.1-win/jbr -subsystems-to-check all -verification-reports-formats plain,html C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/distributions/WindowAccent-1.0.4.zip C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win
Successfully started process 'command 'C:\myprograms\java\jdk\jdk21.0.7\bin\java.exe''
Starting the IntelliJ Plugin Verifier 1.405
2026-05-22T06:24:53 [main] INFO  c.j.p.options.OptionsParser - The verification directory C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier is being deleted because it is not empty.
Verification reports directory: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier
2026-05-22T06:24:57 [main] INFO  verification - Reading IDE C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win
2026-05-22T06:24:57 [main] INFO  c.j.p.options.OptionsParser - Reading IDE from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\638c3bd3fb4fd507cf9dd78f2950dee2\transformed\ideaIU-2025.1.7.1-win
2026-05-22T06:24:57 [main] INFO  c.j.p.options.OptionsParser - Using Java runtime from C:\Users\alexander.goudemond\.gradle\caches\9.5.0\transforms\dc92de66cbd583b708f233ab4d8de54d\transformed\ideaIU-2025.1-win2026-05-22T06:24:57 [main] WARN  c.j.p.s.i.r.ValidatingLayoutComponentsProvider - Layout component 'intellij.android.gradle.declarative.lang.sync' has some nonexistent 'classPath' elements: 'plugins\android-gradle-declarative-lang-ide\lib\modules\intellij.android.gradle.declarative.lang.sync.jar'
Layout component 'intellij.json' has some nonexistent 'classPath' elements: 'plugins\json\lib\modules\intellij.json.jar'
Layout component 'intellij.qodana.sarif' has some nonexistent 'classPath' elements: 'plugins\qodana\lib\modules\intellij.qodana.sarif.jar'
Layout component 'intellij.vcs.git/localHistory' has some nonexistent 'classPath' elements: 'plugins\vcs-git\lib\modules\intellij.vcs.git\localHistory.jar'
Layout component 'intellij.vcs.github' has some nonexistent 'classPath' elements: 'plugins\vcs-github-IU\lib\modules\intellij.vcs.github.jar'
Layout component 'intellij.yaml' has some nonexistent 'classPath' elements: 'plugins\yaml\lib\modules\intellij.yaml.jar'
Layout component 'org.jetbrains.plugins.emojipicker' has some nonexistent 'classPath' elements: 'plugins\emojipicker\lib\emojipicker.jar'
2026-05-22T06:24:57 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\distributions\WindowAccent-1.0.4.zip
2026-05-22T06:25:03 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:1.0.4 against IU-251.29188.36

2026-05-22T06:25:42 [main] INFO  verification - Finished 1 of 1 verifications (in 38,6 s): IU-251.29188.36 against WindowAccent:1.0.4: Compatible
Plugin WindowAccent:1.0.4 against IU-251.29188.36: Compatible
Dynamic Plugin Eligibility:
    Plugin can probably be enabled or disabled without IDE restart

2026-05-22T06:25:42 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 11 s 585 ms
2026-05-22T06:25:42 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 52,38 MB
2026-05-22T06:25:42 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,91 GB
2026-05-22T06:25:42 [main] INFO  verification - Verification reports for WindowAccent:1.0.4 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\build\reports\pluginVerifier\IU-251.29188.36
2026-05-22T06:25:42 [main] INFO  verification - Total time spent in plugin verification: 45 s 107 ms
Build 965e31d4-ed8c-4b18-915f-9b19989728a0 is started
Build 965e31d4-ed8c-4b18-915f-9b19989728a0 is closed

See the complete report at file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-accent/build/reports/configuration-cache/h54o76ni36nhctwkb5zfwgmi/dubvvj20i4h7rq1e6lj186u77/configuration-cache-report.html

BUILD SUCCESSFUL in 16m 5s
14 actionable tasks: 14 executed
Watched directory hierarchies: [C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent]
Configuration cache entry stored.
Configuration Cache (C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache) [subdir: C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-accent\.gradle\configuration-cache\726b397a-ead3-4463-8559-2b17d5e24c43] cleanup deleted 8 files/directories.
```