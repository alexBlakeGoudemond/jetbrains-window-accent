# HELP

During the creation of this project, some challenges were encountered. Below are some short descriptions aiming to
assist the reader if the same problems are encountered.

## Changing code leads to JDK error message (/Packages)

If you change the code and then try to run the program, and the logs say something like 
`C:\myprograms\java\jdk\jdk21.0.7\Packages does not exist.` - consider closing IntelliJ IDEA,
deleting the `.idea`, `.gradle`, `.intellijPlatform`, `build` folders and restarting the IDE.
Also check the Project Settings to ensure that Java21 is used everywhere (including the Gradle Plugin)

Also check the SDK loaded in the Project Settings - it should be Java21. Remove and add again if needed