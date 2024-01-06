package com.example.jeonsilog.view

import android.Manifest
import android.content.Intent
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityMainBinding
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.view.home.HomeFragment
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFinish
import com.kakao.sdk.user.UserApiClient
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.view.photocalendar.PhotoCalendarFragment
import com.example.jeonsilog.view.notification.NotificationFragment
import com.example.jeonsilog.view.otheruser.OtherUserFragment
import com.example.jeonsilog.view.search.RecordSearchFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.view.search.SearchResultFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId


class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {
    private val tag = this.javaClass.simpleName
    private var backPressedTime: Long = 0L

    private val REQUIRED_PERMISSONS = if(Build.VERSION.SDK_INT < 33){
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(supportFragmentManager.backStackEntryCount != 0){
                supportFragmentManager.popBackStack()
            } else {
                if (System.currentTimeMillis() - backPressedTime <= 2000) {
                    finish()
                } else {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(applicationContext, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun init() {
        this.onBackPressedDispatcher.addCallback(this, callback)
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()
                }
                R.id.item_search->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,
                        RecordSearchFragment()).setReorderingAllowed(true).commitAllowingStateLoss()
                }
                R.id.item_photoCalendar->{

                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,
                        PhotoCalendarFragment()
                    ).commit()
                }
                R.id.item_notification->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,
                        NotificationFragment()
                    ).commit()
                }
                R.id.item_mypage->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,
                        MyPageFragment()
                    ).commit()
                }
            }
            true
        }

        isFinish.observe(this){
            Log.d(tag, "isFinish: $it")
            if(it){kakaoLogOut("RefreshToken 만료로 인한")}
        }
    }

    fun setStateBn(isVisible:Boolean){
        if(isVisible){
            binding.bnvMain.visibility = View.VISIBLE
        }else{
            binding.bnvMain.visibility = View.GONE
        }
    }

    private fun kakaoLogOut(msg: String){
        UserApiClient.instance.logout { error ->
            if(error != null){
                Log.e(tag, "$msg 로그아웃 실패")
            } else {
                Log.d(tag, "$msg 로그아웃 진행")
                val intent = Intent(this@MainActivity, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                isFinish.value = false
                finish()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    fun loadExtraActivity(type:Int, newExhibitionId:Int){
        extraActivityReference = type
        exhibitionId = newExhibitionId
        val intent = Intent(this, ExtraActivity::class.java)
        startActivity(intent)

    }

    fun checkPermissions(context: Context): Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(REQUIRED_PERMISSONS, 100)
            }
        } else {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(REQUIRED_PERMISSONS, 101)
            }
        }

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            !(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        }
    }


    // 타 유저 프로필로 이동(해당 유저 아이디 필요)
    fun moveOtherUserProfile(otherUserId: Int, otherUserNick: String){
        if(otherUserId == encryptedPrefs.getUI()){
            binding.bnvMain.selectedItemId = R.id.item_mypage

            val fragment = MyPageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            val fragment = OtherUserFragment(otherUserId, otherUserNick)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    fun moveSearchResultFrament(str :String){
        val fragment = SearchResultFragment(str)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .addToBackStack(null)
            .commit()
    }

}