
package com.hykj.gamecenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UpdateUtils;

/*binder机制是贯穿整个android系统的进程间访问机制，经常被用来访问service。
 service 你可以理解成没有的界面的activity(MVC的C)，它是跑在后台的程序，
 所谓后台是相对于可以被看得到的程序的，后台程序是不能直接交互的程序。
 binder主要是用来进程间通信的，但也可用在和本地service通信。*/

//MonitorAppsUpdateServiceManagerReceiver 是 查询当前是否有 "回复 用户反馈" 的服务 
public class MonitorAppsUpdateService extends Service {
    private static final String TAG = "MonitorAppsUpdateService";
    private final IBinder binder = new MonitorAppsUpdateService.MonitorAppsUpdateBinder();
    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    /*
     * private static boolean bRuningThread=true; private static Thread
     * thread=null;
     */
    private static Object threadSynchrolock = new Object(); // 多线程时用，本例只是没有用多个子线程竞争资源，可以不加锁

    // wait的目的就在于暴露出对象锁，让其他线程可以通过对象的notify叫醒等待在该对象的等该池里的线程。同样notify也会释放对象锁，在调用之前必须获得对象的锁，不然也会报异常。
    @Override
    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    // 处理从线程收到的消息们
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            // 通常我们在这里做一些工作比如下载一个文件
            // 在我们的例子中，仅仅是睡５秒钟．
            /*
             * long endTime=System.currentTimeMillis() + 5*1000; while
             * (System.currentTimeMillis()< endTime) { synchronized (this) { try
             * { wait(endTime - System.currentTimeMillis()); } catch (Exception
             * e) { } } }
             */
            /*
             * //这里必须同步访问 synchronized (threadSynchrolock) { //查询是否有用户反馈的回复
             * FeedbackInteractAgent feedbackInteractAgent=new
             * FeedbackInteractAgent();
             * feedbackInteractAgent.queryHaveNewFeedbackReply();
             * Log.e(TAG,"查询反馈信息");
             * 
             * 
             * 
             * 此时如注册alarmManager管理,3秒后会受到广播， 在广播中再启动服务重复该操作
             * (注册一次查询一次，查询完启动服务，到时间重复该操作， 第一次启动由软件运行或系统开机启动)
             * 
             * AlarmManagerService.startAlarmRepeating(FeedBackApplication.
             * getAppContext(),(long)3000);
             * //使用startId停止服务，从而使我们不会在处理另一个工作的中间停止service stopSelf(msg.arg1); }
             */

        }
    }

    @Override
    public void onCreate() {
        // 启动运行service的线程．注意我创建了一个
        // 分离的线程，因为service通常都是在进程的
        // 主线程中运行，但我们不想让主线程阻塞．我们还把新线程
        // 搞成后台级的优先级，从而减少对UI线程（主线程的影响)．
        /*
         * HandlerThread thread=new HandlerThread("ServiceStartArguments");
         * thread.start();
         * 
         * // Get the HandlerThread\'s Looper and use it for our Handler
         * mServiceLooper=thread.getLooper(); mServiceHandler=new
         * ServiceHandler(mServiceLooper);
         */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final int _startId = startId;
        /*
         * 因为有了是否运行的服务判断，所以一旦服务正在运行，就不会调用startService， 而调用一次startService
         * 就会执行一次该函数onStartCommand，所以这里必须用一个循环来执行， 避免服务运行是只执行一次查询反馈回复任务
         */
        /*
         * bRuningThread=true; if(thread == null) { thread=new Thread() {
         * 
         * @Override public void run() {
         * 
         * while (bRuningThread) { //多线程时用，本例只是没有用多个子线程竞争资源，可以不使用wait，notify
         * 和不加锁 synchronized (Thread.currentThread()) { try { //
         * 对于每个开始请求，发送一消息来开始一次工作，并且把 // start ID也传过去，所以当完成一个工作时，我们才知道要停止哪个请求．
         * Message msg=mServiceHandler.obtainMessage(); msg.arg1=_startId;
         * mServiceHandler.sendMessage(msg);
         * 
         * Thread.sleep(1000 * 5);//5秒查询一次反馈消息
         * Thread.sleep()与Object.wait()二者都可以暂停当前线程，
         * 释放CPU控制权，主要的区别在于Object.wait() 在释放CPU同时，释放了对象锁的控制。
         * 
         * thread.wait(1000 * 5);//5秒查询一次反馈消息 } catch (InterruptedException e) {
         * e.printStackTrace(); } } } } }; } thread.start();
         */

        // 对于每个开始请求，发送一消息来开始一次工作，并且把
        // start ID也传过去，所以当完成一个工作时，我们才知道要停止哪个请求．
        /*
         * Message msg=mServiceHandler.obtainMessage(); msg.arg1=_startId;
         * mServiceHandler.sendMessage(msg);
         */

        // 这里必须同步访问
        synchronized (threadSynchrolock) {

            // 是否是应用主界面没有启动时从通知进入的该界面
            NotificationCenter.setIsHomeNotRunFromNotification(true);
            // 查询是否有应用更新

            ApkInstalledManager.getInstance().loadApps();
            Logger.e("MonitorAppsUpdateServiceManagerReceiver", "查询是否有应用更新");

            /*
             * 此时如注册alarmManager管理,3秒后会受到广播， 在广播中再启动服务重复该操作
             * (注册一次查询一次，查询完启动服务，到时间重复该操作， 第一次启动由软件运行或系统开机启动)
             */
            // 间隔6小时查询一次
            Logger.i("MonitorAppsUpdateServiceManagerReceiver", "onStartCommand " + "time " + UpdateUtils.getUpdateTimeLap(), "oddshou");
            AlarmManagerServiceManager.startAlarmRepeatingOnce(
                    App.getAppContext(), UpdateUtils.getUpdateTimeLap());
            // 使用startId停止服务，从而使我们不会在处理另一个工作的中间停止service
            stopSelf(_startId);
        }
        return START_STICKY; // START_STICKY是service被kill掉后自动重启之
    }

    @Override
    public void onDestroy() {
        Logger.e("MonitorAppsUpdateServiceManagerReceiver", "The service is stop");

        /*
         * bRuningThread=false; synchronized (Thread.currentThread()) { //唤醒线程
         * thread.notify(); } try { thread.join(); } catch (InterruptedException
         * e) { // TODO Auto-generated catch block e.printStackTrace(); }
         * thread=null;
         */
        /*
         * 让onStartCommand（）函数的返回值为START_STICKY，同时在onDestroy（）中重启Service
         * 当返回值为该值时，Service被kill之后会被系统自动重启。
         * 同时，在Service的onDestroy（）中重启Service，可以给Service的重启做双重保证。
         * 但是显然的缺点是，当APP的进程被kill后，这个方案就会失效。
         */
        // 改用alarmManager管理，alarmManager是系统底层的，进程被杀了仍然会收到广播
        // 关闭时重启service (检查和启动 查询回复反馈的服务)
        // MonitorAppsUpdateServiceHelper.MonitorAppsUpdateService(FeedBackApplication.getAppContext());
    }

    public class MonitorAppsUpdateBinder extends Binder {
        public MonitorAppsUpdateService getService() {

            return MonitorAppsUpdateService.this;
        }
    }

}
