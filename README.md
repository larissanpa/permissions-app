# Permissions App

This sample provides an Android sample application for reproducing a bug found on Android 11.

### Bug Description
---

In Android 11, new one-time permissions have been introduced for Location, Microphone and Camera permissions. With this option, as soon as the user leaves the application, the permission is revoked (more details can be found [here](https://developer.android.com/preview/privacy/permissions#one-time)). The problem is that after a short amount of time after the app is no longer in the foreground (it is not necessary to kill the app, just minimizing is enough), all future scheduled alarms or jobs are removed, as if the app was force killed. This only happens with this level of permission. Denying or providing other levels keep the previously scheduled alarms or jobs, as expected.

### App Description
---

This single activity app schedules an alarm to ring in the next five minutes, as well as a job to trigger in between 5-6 minutes. The screen has three buttons, each triggering the corresponding permission request. The JobService and BroadcastReceiver classes only log that they have been triggered.

### Reproducing the bug
---

We have reproduced this in the Beta 3 build, in a Pixel 2 emulator with the RPB3.200720.005 build number. It can be reproduced after the following steps:
  1. Whenever the app is started, it is possible to run both `adb shell dumpsys alarm | grep com.example.permissions.app` and `adb shell dumpsys jobscheduler | grep com.example.permissions.app` to see that both the alarm and the job are scheduled;
  2. Click in any of the buttons and grant the one-time permission level;
  3. Minimize the app (you can go to the home screen or open other app);
  4. After around a minute, run both `adb shell dumpsys alarm | grep com.example.permissions.app` and `adb shell dumpsys jobscheduler | grep com.example.permissions.app`. The alarm and job will no longer appear;
  5. Waiting the original scheduled times for both job and alarm (with lenience for system delays) will show that they won't be triggered.
