package com.example.jeonsilog.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.jeonsilog.R
import com.example.jeonsilog.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessagingService : FirebaseMessagingService() {

    //FCM 메시지를 수신했을 때 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 메시지가 데이터 페이로드를 포함하는 경우에는 이를 처리하고, 긴 작업이 필요한지 확인하여 작업을 예약하거나 즉시 처리

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("myTag", "Message data payload: ${remoteMessage.data}")

            // Check if data needs to be processed by long running job
            if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {

            }
        }
        remoteMessage.notification?.let {
            Log.d("myTag", "Message Notification Body: ${it}")
            sendNotification(it!!)
        }
    }
    // [END receive_message]

    private fun needsToBeScheduled() = true

    //FCM 등록 토큰이 갱신될 때 호출
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        //서버에 토큰을 전송하는 sendRegistrationToServer 메서드를 호출
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    private fun scheduleJob() {
        //WorkManager를 사용하여 백그라운드에서 실행되는 작업을 예약
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
        // [END dispatch_job]
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    //알림을 생성하고 표시
    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "fcm_default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.fcm_logo_jeonsilog) // 아이콘 지정하는 부분
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // 알림을 클릭했을 때 액티비티를 시작

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //알림 채널을 생성하여 적절한 중요도를 지정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    //백그라운드에서 실행되는 작업을 처리하는 내부 클래스
    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}