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
        Log.d("onMessageReceivedRTag", "Message data payload: ${remoteMessage}")
        sendNotification(remoteMessage)
        if (needsToBeScheduled()) {
            // For long-running tasks (10 seconds or more) use WorkManager.
            scheduleJob()
        } else {

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



    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    //알림을 생성하고 표시
    private fun sendNotification(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {//fcm 형식이 notification인경우
            // Handle notification payload.
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            // You can customize the notification using title and body.
            showNotification(title, body)
        } else if (remoteMessage.data.isNotEmpty()) {//fcm 형식이 data인 경우
            // Handle data payload.
            val dataTitle = remoteMessage.data["title"]
            val dataBody = remoteMessage.data["body"]
            // You can customize the notification using dataTitle and dataBody.
            showNotification(dataTitle, dataBody)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        //알림페이지로 이동
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
            .setSmallIcon(R.drawable.fcm_logo_jeonsilog)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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