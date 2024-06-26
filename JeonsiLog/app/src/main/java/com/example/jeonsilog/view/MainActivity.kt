package com.example.jeonsilog.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.navigation.findNavController
import com.example.jeonsilog.view.admin.AdminManagingFragment
import com.example.jeonsilog.view.admin.AdminReportFragment
import com.example.jeonsilog.view.home.HomeFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityMainBinding
import com.example.jeonsilog.fcm.services.FcmDialog
import com.example.jeonsilog.view.admin.AdminSearchFragment
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFinish
import com.kakao.sdk.user.UserApiClient
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.view.photocalendar.PhotoCalendarFragment
import com.example.jeonsilog.view.notification.NotificationFragment
import com.example.jeonsilog.view.otheruser.OtherUserFragment
import com.example.jeonsilog.view.search.RecordSearchFragment
import com.example.jeonsilog.view.search.SearchResultFragment
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.extension.NetworkDialog
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.networkState
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReviewId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {
    private val tag = this.javaClass.simpleName
    private var networkDialog: NetworkDialog? = null
    private var backPressedTime: Long = 0L
    private var alertDialog: AlertDialog.Builder? = null
    private val adminViewModel: AdminViewModel by viewModels()
    private var isPermissionDenied = false
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(supportFragmentManager.backStackEntryCount != 0){
                supportFragmentManager.popBackStack()
            }else if(isAdminExhibitionOpen){
                Navigation.findNavController(binding.fcvNavAdmin).popBackStack()
            }else{
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
        //admin 계정 체크
        if(encryptedPrefs.getCheckAdmin()){
            adminViewModel.setIsAdminPage(true)
            binding.scSwitchAdminUser.visibility = View.VISIBLE
        }else{
            binding.scSwitchAdminUser.visibility = View.GONE
        }
        checkAdmin(encryptedPrefs.getCheckAdmin())

        this.onBackPressedDispatcher.addCallback(this, callback)

        networkState.observe(this) {
            if(!it) {
                networkDialog = if(networkDialog != null) {
                    null
                } else {
                    NetworkDialog()
                }

                networkDialog?.show(supportFragmentManager, "NetworkDialog")
            }
        }

        //Main Bottom Nav
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
                        NotificationFragment("main")
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

        //Admin Bottom Nav
        binding.bnvAdmin.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_admin_home->{
                    setStateFcm(true)
                    val navController = findNavController(R.id.fcv_nav_admin)
                    navController.navigate(R.id.homeFragment)
                }
                R.id.item_admin_search->{
                    setStateFcm(false)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,AdminSearchFragment()).commit()
                }
                R.id.item_admin_report->{
                    setStateFcm(false)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,AdminReportFragment()).commit()
                }
                R.id.item_admin_managing->{
                    setStateFcm(false)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,AdminManagingFragment()).commit()
                }
            }
            true
        }

        isFinish.observe(this){
            Log.d(tag, "isFinish: $it")
            if(it){kakaoLogOut("RefreshToken 만료로 인한")}
        }
        isPermissionDenied=prefs.getIsAllowNotify()
        askNotificationPermission()
        getToken()

        setSwitchAdmin()
    }

    fun setStateBn(isVisible:Boolean, type:String){
        var view = binding.bnvMain
        //관리자 계정 체크
        when(type){
            "user" -> view = binding.bnvMain
            "admin" -> view = binding.bnvAdmin
        }
        view.isVisible = isVisible
    }
    fun setStateFcm(isVisible: Boolean){
        if(isVisible){
            binding.fcvNavAdmin.visibility = View.VISIBLE
            binding.flMain.visibility = View.GONE
        }else{
            binding.fcvNavAdmin.visibility = View.GONE
            binding.flMain.visibility = View.VISIBLE
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val extras = intent.extras
            val targetFragment = extras?.getString("action")
            Log.d("GGGGGtargetFragmenttargetFragment", "${targetFragment}: ")
            if (targetFragment != null) {
                binding.bnvMain.selectedItemId = R.id.item_notification
                moveNotificationFragment(targetFragment.toString())
            }
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
    //admin 계정 체크
    private fun checkAdmin(check:Boolean){
        if(check){
            //관리자
            binding.bnvMain.visibility = View.GONE
            binding.bnvAdmin.visibility = View.VISIBLE
            setStateFcm(true)
        }else{
            //일반 유저
            binding.bnvMain.visibility = View.VISIBLE
            binding.bnvAdmin.visibility = View.GONE
            setStateFcm(false)
            supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()
        }
        adminViewModel.setIsAdminPage(check)
    }

    fun loadExtraActivity(type:Int, newTargetId:Int){
        extraActivityReference = type
        when(type){
            0 -> exhibitionId = newTargetId
            1 -> newReviewId = newTargetId
            4 -> newReviewId = newTargetId
        }
        val intent = Intent(this, ExtraActivity::class.java)
        startActivity(intent)
    }

    fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (shouldShowRequestPermissionRationale(permission[0])) {
            showPermissionRationale(getString(R.string.permission_denied))
        } else {
            ActivityCompat.requestPermissions(this, permission, 100)
        }
    }

    private fun showPermissionRationale(msg: String) {
        alertDialog = AlertDialog.Builder(this)
        alertDialog?.setMessage(msg)
        alertDialog?.setPositiveButton("확인") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialog?.setNegativeButton("취소") { _, _ ->
        }

        alertDialog?.show()

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
    private fun moveNotificationFragment(action:String){
        val fragment = NotificationFragment(action)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .addToBackStack(null)
            .commit()
    }
    fun moveSearchResultFrament(str :String){
        val fragment = SearchResultFragment(str)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun refreshFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .commit()
    }
    fun refreshFragmentInAdmin(fragmentId: Int) {
        val navController = Navigation.findNavController(binding.fcvNavAdmin)
        navController.popBackStack()
        navController.navigate(fragmentId)
    }
    fun setBottomNavCurrentItem(index:Int){
        binding.bnvMain.menu.getItem(index).isChecked = true
    }
    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("myFcmToken", msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    //registerForActivityResult

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("requestPermissionLauncher", "권한이 승인된 경우")
            // 권한이 승인된 경우
        } else {
            Log.d("requestPermissionLauncher", "권한이 거부된 경우")
            // 권한이 거부된 경우
        }
    }
    //알림 권한 확인 함수
    private fun askNotificationPermission() {
        // Android API level이 TIRAMISU (33) 이상인 경우에만 실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // 이미 권한이 부여된 경우

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // 사용자에게 이미 거부한 이력이 있는 경우
                if (!isPermissionDenied){
                    Log.d("requestPermissionLauncher", "사용자에게 이미 거부한 이력이 있는 경우")
                    //다이얼로그 띄우기
                    showFcmDialog(supportFragmentManager)
                    prefs.setIsAllowNotify(true)
                }
            } else {
                // 권한을 요청하는 경우
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }else{
            Log.d("requestPermissionLauncher", "뭐지뭐지")
        }
    }
    private fun showFcmDialog(fragmentManager: FragmentManager) {
        val dialog = FcmDialog()
        dialog.show(fragmentManager, "FcmDialog")
    }

    private fun setSwitchAdmin(){
        binding.scSwitchAdminUser.setOnCheckedChangeListener { _, isChecked ->
            when{
                isChecked -> {
                    //user 활성화
                    checkAdmin(false)
                    binding.tvSwitchAdmin.setTextColor(getColor(R.color.gray_medium))
                    binding.tvSwitchUser.setTextColor(getColor(R.color.basic_white))
                }
                else -> {
                    //admin 활성화
                    checkAdmin(true)
                    binding.tvSwitchAdmin.setTextColor(getColor(R.color.basic_white))
                    binding.tvSwitchUser.setTextColor(getColor(R.color.gray_medium))
                }
            }
        }
    }
    fun setStateToolBar(state:Boolean){
        when{
            state -> binding.toolbar.isVisible = true
            else -> binding.toolbar.isVisible = false
        }
    }
}