package com.example.jeonsilog.fcm.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.user.ChangeFcmTokenRequest
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class FirebaseMessagingService : FirebaseMessagingService() {
    var NOTIFICATION_CHANNEL= arrayListOf("fcm_default_channel","","")

    //FCM 메시지를 수신했을 때 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 메시지가 데이터 페이로드를 포함하는 경우에는 이를 처리하고, 긴 작업이 필요한지 확인하여 작업을 예약하거나 즉시 처리
        Log.d("onMessageReceivedRTag", "Message data payload: ${remoteMessage}")
        sendNotification(remoteMessage)
        if (needsToBeScheduled()) {
            scheduleJob()
        }
    }
    // [END receive_message]

    private fun needsToBeScheduled() = true

    //FCM 등록 토큰이 갱신될 때 호출
    override fun onNewToken(token: String) {
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
        //FCM 등록 토큰이 변경될 때마다 호출되며, 따라서 앱이 새로 설치되거나 사용자가 앱을 삭제하고 다시 설치하는 등의 상황에서도 호출
        if (token!=null){
            CoroutineScope(Dispatchers.IO).launch {
                //서버에 주소 전달
                UserRepositoryImpl().changeFcmToken(encryptedPrefs.getAT(), ChangeFcmTokenRequest(token))
            }
        }
    }

    //알림을 생성하고 표시
    private fun sendNotification(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {//fcm 형식이 notification인경우
            Log.d("sendNotification", "notification: ")
            // Handle notification payload.
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            // You can customize the notification using title and body.
            showNotification(title, body,)
        } else if (remoteMessage.data.isNotEmpty()) {//fcm 형식이 data인 경우
            Log.d("sendNotification", "data: ")
            // Handle data payload.
            val dataTitle = remoteMessage.data["title"]
            val dataBody = remoteMessage.data["body"]

            // You can customize the notification using dataTitle and dataBody.
            showNotification(dataTitle, dataBody)
        }

    }

    private fun showNotification(title: String?, body: String?) {
        //알림페이지로 이동
        val intent: Intent
        Log.d("bodybodybody", "$body: ")

        if(body==null){
            intent = Intent("your_special_action").apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("action", "your_special_action") // 특정 동작을 나타내는 데이터 추가
                action = "your_special_action"
            }
        }else{
            intent = Intent("your_special_exhibition").apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("action", "your_special_exhibition") // 특정 동작을 나타내는 데이터 추가
                action = "your_special_exhibition"
            }

        }

        Log.d("GGGGGshowNotification", "${intent.extras}||$title")



        val pendingIntent:PendingIntent =
            PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE) //@@
        val myBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.notification_logo)
        //val channelId = "fcm_default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL[0])
            .setSmallIcon(R.drawable.fcm_logo_jeonsilog)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(null) // 소리 끄기
            .setContentIntent(pendingIntent)
            .setLargeIcon(myBitmap)



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL[0],
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    //백그라운드에서 실행되는 작업을 처리하는 내부 클래스
    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}