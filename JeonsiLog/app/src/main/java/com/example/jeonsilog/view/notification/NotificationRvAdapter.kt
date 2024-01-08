package com.example.jeonsilog.view.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.alarm.AlarmInformation
import com.example.jeonsilog.databinding.ItemNotiActivityBinding
import com.example.jeonsilog.databinding.ItemNotiExhibitionBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationRvAdapter(private val notiList: List<AlarmInformation>, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class TypeActivityViewHolder(private val binding: ItemNotiActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AlarmInformation){
            if (data.isChecked) {
                itemView.alpha = 0.4f
            } else {
                itemView.alpha = 1.0f
            }

            GlideApp.with(binding.ivNotiProfile)
                .load(data.imgUrl)
                .circleCrop()
                .into(binding.ivNotiProfile)

            val (nick, contents) = extractNickAndContents(data.contents)

            binding.tvNotiTitle.text = nick
            binding.tvNotiContent.text = contents
            binding.tvNotiTime.text = formatElapsedTime(data.dateTime)

            // 클릭리스너 - 해당 유저 프로필로 이동
            itemView.setOnClickListener {
                if(!data.isChecked){
                    patchChecked(itemView, data.alarmId)
                }

                when(data.alarmType){
                    "REVIEW" -> {
                        // 해당 전시회 댓글 페이지로 이동
                    }
                    "REPLY" -> {
                        // 해당 전시회 댓글 페이지로 이동
                    }
                    "RATING" -> {
                        // 해당 전시회 상세 정보 페이지
                    }
                    "FOLLOW" -> {
                        // 해당 유저 프로필 페이지
                        (context as MainActivity).moveOtherUserProfile(otherUserId = data.clickId, otherUserNick = nick)
                    }
                }
            }
        }
    }

    inner class TypeExhibitionViewHolder(private val binding: ItemNotiExhibitionBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: AlarmInformation){
            if (data.isChecked) {
                itemView.alpha = 0.4f
            } else {
                itemView.alpha = 1.0f
            }

            GlideApp.with(binding.ivNotiExhibition)
                .load(data.imgUrl)
                .circleCrop()
                .into(binding.ivNotiExhibition)

            val (title, contents) = extractTitleAndContents(data.contents)

            binding.tvNotiTitle.text = title
            binding.tvNotiContent.text = contents
            binding.tvNotiTime.text = formatElapsedTime(data.dateTime)

            itemView.setOnClickListener {
                if(!data.isChecked){
                    patchChecked(itemView, data.alarmId)
                }
//                (context as MainActivity).loadExtraActivity(type = 0, newExhibitionId = data)
                // 전시회 아이디 필요 -> 검색을 통해? or Response에 값 추가
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeActivityViewHolder -> {
                holder.bind(notiList[position])
            }
            is TypeExhibitionViewHolder -> {
                holder.bind(notiList[position])
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }

    }

    override fun getItemCount(): Int = notiList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return TypeActivityViewHolder(
                    ItemNotiActivityBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            1 -> {
                return TypeExhibitionViewHolder(
                    ItemNotiExhibitionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("유효하지 않은 뷰 홀더  타입")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(notiList[position].alarmType){
            "FOLLOW" -> 0
            "EXHIBITION" -> 1
            "RATING" -> 0
            "REVIEW" -> 0
            "REPLY" -> 0
            else -> throw IllegalArgumentException("유효하지 않은 알림 타입")
        }
    }

    private fun extractTitleAndContents(input: String): Pair<String, String> {
        val startIndex = input.indexOf('[')
        val endIndex = input.indexOf(']')

        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            // '['나 ']'가 없거나 올바르지 않은 위치에 있을 경우 예외 처리
            throw IllegalArgumentException("Invalid input format")
        }

        val title = input.substring(startIndex + 1, endIndex).trim()
        val contents = input.substring(endIndex + 1).trim()

        return title to contents
    }

    private fun formatElapsedTime(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val pastDateTime = LocalDateTime.parse(dateTimeString, formatter)
        val currentDateTime = LocalDateTime.now()

        val elapsedMinutes = ChronoUnit.MINUTES.between(pastDateTime, currentDateTime)
        val elapsedHours = ChronoUnit.HOURS.between(pastDateTime, currentDateTime)
        val elapsedDays = ChronoUnit.DAYS.between(pastDateTime, currentDateTime)

        return when {
            elapsedMinutes < 60 -> "지금"
            elapsedHours < 24 -> "${elapsedHours}시간 전"
            elapsedHours < 48 -> "어제"
            elapsedDays < 7 -> "${elapsedDays}일 전"
            else -> LocalDateTime.parse(dateTimeString).format(DateTimeFormatter.ofPattern("MM.dd"))
        }
    }

    private fun extractNickAndContents(input: String): Pair<String, String> {
        val spaceIndex = input.indexOf(' ')

        if (spaceIndex == -1) {
            // 공백이 없는 경우 예외 처리
            throw IllegalArgumentException("Invalid input format")
        }

        val nick = input.substring(0, spaceIndex).trim()
        val contents = input.substring(spaceIndex + 1).trim()

        return nick to contents
    }

    private fun patchChecked(itemView: View, alarmId: Int){
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = AlarmRepositoryImpl().patchAlramChecked(encryptedPrefs.getAT(), alarmId)
//            if(response.isSuccessful && response.body()!!.check){
//                launch(Dispatchers.Main){
//                    itemView.alpha = 0.4f
//                }
//            }
//        }

        // 테스트용 코드
        itemView.alpha = 0.4f
    }
}