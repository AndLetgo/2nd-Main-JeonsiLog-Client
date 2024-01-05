package com.example.jeonsilog.view.login

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivitySignupBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs

class SignUpActivity: BaseActivity<ActivitySignupBinding>({ ActivitySignupBinding.inflate(it)}) {
    private val viewModel: SignUpViewModel by viewModels()
    private val tag = this.javaClass.simpleName
    private var backPressedTime: Long = 0L


    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(applicationContext, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun init() {
        if(prefs.getSignUpFinished()){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        this.onBackPressedDispatcher.addCallback(this, callback)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_sign_up, TosFragment())
            .commit()
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

    fun requestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (shouldShowRequestPermissionRationale(permission[0])) {
            Log.d(tag, "in True")
            if(viewModel.firstRequest.value!! > 2){
                showPermissionRationale("거부된 권한이 있습니다")
                viewModel.changeFirstRequest(0)
            } else {
                viewModel.changeFirstRequest(viewModel.firstRequest.value!! + 1)
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, 112)
        }

        viewModel.setUpdateFlag(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d(tag, "거부")

        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(viewModel.tosIsCheckedPermissionPhoto.value!!){
                showPermissionRationale("권한을 수정하려면 설정으로 이동해야 합니다")
            }
        } else {
            if(viewModel.firstRequest.value!! > 2){
                showPermissionRationale("거부된 권한이 있습니다.")
                viewModel.changeFirstRequest(0)
            } else {
                viewModel.changeFirstRequest(viewModel.firstRequest.value!! + 1)
            }
        }

        viewModel.setUpdateFlag(true)
    }

    fun checkPermission(): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED){
                viewModel.changeTosPhoto(true)
                true
            } else {
                requestPermission()
                false
            }
        } else {
            return if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED){
                viewModel.changeTosPhoto(true)
                true
            } else {
                requestPermission()
                false
            }
        }
    }

    private fun showPermissionRationale(msg: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("확인") { _, _ ->

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialog.setNegativeButton("취소") { _, _ ->
            viewModel.setUpdateFlag(true)
        }

        alertDialog.show()
    }
}