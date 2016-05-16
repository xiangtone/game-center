/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hykj.gamecenter.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * 在使用一些产品列如微信、QQ之类的，如果有新消息来时， 手机屏幕即使在锁屏状态下也会亮起并提示声音，
 * 这时用户就知道有新消息来临了。但是，一般情况下手机锁屏后 ，Android系统为了省电以及减少CPU消耗， 在一段时间后会使系统进入休眠状态，
 * 这时，Android系统中CPU会保持在一个相对较低的功耗状态。 针对前面的例子，收到新消息必定有网络请求，
 * 而网络请求是消耗CPU的操作，那么如何在锁屏状态乃至系统进入休眠后， 仍然保持系统的网络状态以及通过程序唤醒手机呢？
 * 答案就是Android中的WakeLock机制。 1、PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
 * 2、SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
 * 3、SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，允许关闭键盘灯
 * 4、FULL_WAKE_LOCK：保持CPU运转，保持屏幕高亮显示，键盘灯也保持亮度
 * 5、ACQUIRE_CAUSES_WAKEUP：不会唤醒设备，强制屏幕马上高亮显示，键盘灯开启。有一个例外，如果有notification弹出的话
 * ，会唤醒设备。 6、ON_AFTER_RELEASE：WakeLock 被释放后，维持屏幕亮度一小段时间，减少WakeLock 循环时的闪烁情况
 */

public class ServiceWakeLock {
    private static final String TAG = "ServiceWakeLock";
    private static PowerManager.WakeLock sCpuWakeLock;

    public static PowerManager.WakeLock createPartialWakeLock(Context context) {
        PowerManager pm =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    /* public static void acquireCpuWakeLock(Context context) {
         if (sCpuWakeLock != null) {
             return;
         }

         sCpuWakeLock = createPartialWakeLock(context);
         sCpuWakeLock.acquire();
     }

     public static void acquireScreenCpuWakeLock(Context context) {
         if (sCpuWakeLock != null) {
             return;
         }
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         sCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                 | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
         sCpuWakeLock.acquire();
     }

     public static void releaseCpuLock() {
         if (sCpuWakeLock != null) {
             sCpuWakeLock.release();
             sCpuWakeLock = null;
         }
     }*/
}
