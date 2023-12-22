package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyPageViewModel: ViewModel() {
    private var _nick = MutableLiveData<String>()
    val nick: LiveData<String>
        get() = _nick

    private var _profileImg = MutableLiveData<String>()
    val profileImg: LiveData<String>
        get() = _profileImg

    private var _following = MutableLiveData<String>()
    val following: LiveData<String>
        get() = _following

    private var _follower = MutableLiveData<String>()
    val follower: LiveData<String>
        get() = _follower

    fun setNick(nick: String){
        _nick.value = nick
    }

    fun setProfileImg(url: String){
        _profileImg.value = url
    }

    private var _flag = MutableLiveData<Boolean>(false)
    val flag: LiveData<Boolean>
        get() = _flag

    val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNzAzMDg2NjI1LCJleHAiOjE3MDMwOTAyMjV9.ZOl6t8gdbOb0Fzry4CXB2rOka4ts4pWh6kvl7i0r6sQ0GBOgHHkp9yntotUDt-GI9RcCtN-KhizL_01EqhhJaQ"


    fun getMyInfo(){
        Log.d("vm", "getMyInfo")
        viewModelScope.launch(Dispatchers.IO) {
//          UserRepositoryImpl().getMyInfo(encryptedPrefs.getAT()!!)
            runBlocking {
                UserRepositoryImpl().getMyInfo(token)
            }
            encryptedPrefs.setURL("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAOVBMVEXMzMySkpLPz8+NjY2hoaHKysqTk5Obm5uMjIzExMSWlpa9vb3CwsKoqKjHx8exsbG5ubmlpaW0tLRxlrMGAAAFD0lEQVR4nO2biZKsKgyGJcEFRVze/2FvAmh3qzNnreNy/69qprrFdPEbQgChKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgKtCRfmGfH0YFKz5wPqHaXRmR3d2nf4qVO8VVmdX6q/iea+Qx7Nr9Rehfi9QQvH2kUi09JyhOlLIYSmnm2odDnUd0p9d19+B7EHsfQXXZ1f316HxFwSKxOF2DZXsj2W9U99P4UEG/I4KCi/HoUJu7TTZ9ihCn6Gwd5IjJfe5gwHAAxSyDasIKqetH++vMKYDKvw4ep0b0sAPU8i9KugrFkxf0k7i7RVKuiNXraJk+rtJmHdXyO5zEswiMfCTFOpEqf0QVG6mVDdXKJNdmj9cph1Px89RaMKub23llgcpFDnFJjuwiG4fpHDbr2hf83kLFF6Pz+pvgk6RVlo9R6Gkw+2MWIYA5YN6ml1ukPxBn+scN1eogUgf0wm7HbbdXaG4rCjql0TNht2jRm3GlHJt8SJbveNjFPcAhZr0yU+t4XbqaL8Wd3+Fpg5xClzEf1Ruix+g0Jh5efFLxbhb8H+EQm77TsKx9MPBatsjFKpIrio+XO5/isKvgcLrQdOvKbzhC+GjV/dfwzfcmbFZmPmBwPu9PhTI9fYn6e+6fYh+mrNrCgAAAAAAAAAAgP8Nug6xWYx47bWkXdk3dvRTdv8aclPVmHooX/Wh0fr8sW+Z68PFNPI22r2d1aPZhvSpFDtTz5eQSCM3dujbhl/1CcsSaFk1tZYdnKigoeFo93aOzXPj8g+oXdVcYqnYs/G6YtZVa3WozgrJcq9lNu0w/aDjymnZaNYyauM+Rv0BHnQRruYLeFFqP6ZXn6VbLg3cJoWO21RDY3Z2ckuyC2G51Itd/JGO0wmMwBd4oxEWES88T31UKFJTBFL8vnYc+sHvWiB1TT9FhXJ7fmq28cXJ0KwiPpd1W1NkhZZzBUdtpraOXqOurksRP2/sStOKQVRYm+TXdNe5qHcozMPQrQ7qm46ywprLdFHcSoVrUsWN3EAT+2i3LumLv1xWWFTLEdPuIID/MVKnMDRKnR+7tDXKCouWc/2CBhaN2qdS3ajXay76ZJceAs1yOStUbya7+GTORXu9ZggUek7PPdbuWKG6d4p/saMRu5KczWfz0jPYKnR8er7Qt70xVERUPFQRQ+8LhVo6cRvt2rRVSi+pfQq9Syq0cTtQsThB21qOzkK3euXb1pq2poqtWRTlI3leiyT9j+n5OL16qTjs10hRhznTet91fuLZ6y5ZzrluremqcB0ERId5rqNdzaPatSYH53j+21PJFvZN4dhw7D7YcNPGvJbuyr2+6LK5vQ7vCnUEF+2Mka4ndrTJrr/AqfY148dWGrqE5aHrNA0mB4szUyaXNJ+d7pq1lWoiyXbiw87rY0vypT86R9U74pbYkOS5v5LzEodFFeNKGlvsSsemXbKFil56mvH9x2IclsbEjXDz+clCCBX3rnTT+4nspS/VncBjCDPHVufYpJ4yyneGB1d6+z56WzK+SKu6MgxsQnEBgpWwa9K20QxNTZoSycyhYW4qlSQzouS1jlUH+TraTe92Ns+eaI527UU2MZCfh9l91CW4PFyjcpQRXfxYunzKkoILi90YNnbFy26+0DabbzdUfFP2u3YAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD4I/4DNVgrlDTUNE4AAAAASUVORK5CYII=")
        }
        setInfo()
    }

    private fun setInfo(){
        Log.d("vm", "setInfo")
        _nick.value = encryptedPrefs.getNN()
        _profileImg.value = encryptedPrefs.getURL()
        _following.value = encryptedPrefs.getNumFollowing().toString()
        _follower.value = encryptedPrefs.getNumFollower().toString()

    }
}