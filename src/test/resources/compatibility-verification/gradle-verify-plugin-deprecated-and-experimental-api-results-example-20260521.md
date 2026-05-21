Command used to generate the results: `./gradlew verifyPlugin --rerun-tasks --info`

Results before code changes in v1.0.2:

```java
...
2026-05-20T18:46:30 [main] INFO verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\distributions\WindowAccent-1.0.2.z2026-05-20T18:46:38 [main] INFO verification - Task check-plugin parameters: 
Scheduled verifications (1): 
WindowAccent:1.0.2 against IU-253.33514.17 

2026-05-20T18:49:11 [main] INFO verification - Finished 1 of 1 verifications (in 153,0 s): IU-253.33514.17 against WindowAccent:1.0.2: Compatible. 
4 usages of deprecated API. 6 usages of experimental API 
Plugin WindowAccent:1.0.2 against IU-253.33514.17: Compatible. 

4 usages of deprecated API. 6 usages of experimental API 
Deprecated API usages (4): 
...
Experimental API usages (6): 
...
Dynamic Plugin Eligibility: 
	Plugin can probably be enabled or disabled without IDE restart 

2026-05-20T18:49:12 [main] INFO verification - Total time spent downloading plugins and their dependencies: 0 ms 
2026-05-20T18:49:12 [main] INFO verification - Total amount of plugins and dependencies downloaded: 0 B 
2026-05-20T18:49:12 [main] INFO verification - Total amount of space used for plugins and dependencies: 2,72 GB 
2026-05-20T18:49:12 [main] INFO verification - Verification reports for WindowAccent:1.0.2 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\reports\pluginVerifier\IU-253.33514.17 

2026-05-20T18:49:12 [main] INFO verification - Total time spent in plugin verification: 2 m 42 s 564 ms 
Build 6b05415b-190b-43f0-81f7-ede6e333f521 is closed 

[Incubating] Problems report is available at: file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/reports/problems/problems-report.html
...
```

Results after code changes in v1.0.2:

```java
...
        2026-05-21T05:01:48 [main] INFO  verification - Reading plugin to check from C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\distributions\WindowAccent-1.0.2.z2026-05-21T05:01:55 [main] INFO  verification - Task check-plugin parameters:
Scheduled verifications (1):
WindowAccent:1.0.2 against IU-253.33514.17

        2026-05-21T05:04:13 [main] INFO  verification - Finished 1 of 1 verifications (in 138,2 s): IU-253.33514.17 against WindowAccent:1.0.2: Compatible
Plugin WindowAccent:1.0.2 against IU-253.33514.17: Compatible
Dynamic Plugin Eligibility:
Plugin can probably be enabled or disabled without IDE restart

2026-05-21T05:04:13 [main] INFO  verification - Total time spent downloading plugins and their dependencies: 0 ms
2026-05-21T05:04:13 [main] INFO  verification - Total amount of plugins and dependencies downloaded: 0 B
2026-05-21T05:04:13 [main] INFO  verification - Total amount of space used for plugins and dependencies: 2,72 GB
2026-05-21T05:04:13 [main] INFO  verification - Verification reports for WindowAccent:1.0.2 saved to C:\myworkbench\workspace_personal\github\alexBlakeGoudemond\jetbrains-window-color-panel\build\reports\pluginVerifier\IU-253.33514.17
        2026-05-21T05:04:13 [main] INFO  verification - Total time spent in plugin verification: 2 m 25 s 268 ms
Build 117c02ef-604e-491e-8e88-5a53827e0abe is closed

[Incubating] Problems report is available at: file:///C:/myworkbench/workspace_personal/github/alexBlakeGoudemond/jetbrains-window-color-panel/build/reports/problems/problems-report.html
...
```
