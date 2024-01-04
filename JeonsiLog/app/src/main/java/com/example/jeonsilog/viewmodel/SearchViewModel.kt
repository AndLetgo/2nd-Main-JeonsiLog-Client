package com.example.jeonsilog.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem
import com.example.jeonsilog.data.remote.dto.ExhibitionRandom
import com.example.jeonsilog.data.remote.dto.UserSearchItem

class SearchViewModel  : ViewModel() {




    val search_record_title = ObservableField<String>()
    // MutableLiveData를 사용하여 변경 가능한 리스트를 LiveData로 감싸기
    private val itemListLiveData = MutableLiveData<List<String>>()
    // ViewModel 밖에서는 LiveData로만 접근 가능하도록 노출
    val itemlist: LiveData<List<String>> get() = itemListLiveData

    // 리스트 업데이트하는 메서드
    fun updateItemList(newItemList: List<String>) {
        itemListLiveData.value = newItemList
    }
    fun updateText(newText: String) {
        search_record_title.set(newText)
    }
    var randomExhibitionList= arrayListOf<ExhibitionRandom>()
    var unsplashUrl01 = ""
    var unsplashUrl02 = ""
    fun setRandomList(){
        val random = java.util.Random()
        var rand01=random.nextInt(50)
        var rand02=random.nextInt(50)
        unsplashUrl01 = "https://picsum.photos/id/${rand01}/200/300"
        unsplashUrl02 = "https://picsum.photos/id/${rand02}/200/300"
    }

    var exhibitionlist= listOf(
        ExhibitionInfoItem(1,"Becoming Winter : [ ]","서울특별시 서초구","크리스앤코 갤러리","전시 중",null),
        ExhibitionInfoItem(2,"우린 모두 다른 우주에서","서울특별시 강남구","아르떼케이","시작전",null),
        ExhibitionInfoItem(3,"프레리 : Happiness is Everywhere","서울특별시 종로구","나인원갤러리 / 착한갤러리 리세일","시작 전","무료"),
        ExhibitionInfoItem(4,"2023 춘천 모두의 미술","강원도 춘천","춘천문화예술회관","전시 중","무료"),
    )
    var exhibitionplacelist= listOf(
        ExhibitionPlaceItem(1,"그라운드시소 성수","서울특별시 서초구"),
        ExhibitionPlaceItem(2,"갤러리구조","서울특별시 강남구"),
        ExhibitionPlaceItem(3,"에이원플랜","서울특별시 종로구"),
        ExhibitionPlaceItem(4,"전시가벽","춘천문화예술회관"),
        ExhibitionPlaceItem(5,"프라움악기박물관","서울특별시 강동구"),
        ExhibitionPlaceItem(6,"MUSEUM 209",null)

    )
    var userlist=listOf(
        UserSearchItem(1,"김성수"),
        UserSearchItem(2,"성수림"),
        UserSearchItem(3,"성수티비"),
        UserSearchItem(4,"지성수박"),
        UserSearchItem(5,"침착맨"),
        UserSearchItem(6,"매직박"),
        UserSearchItem(7,"김풍"),
    )


}
