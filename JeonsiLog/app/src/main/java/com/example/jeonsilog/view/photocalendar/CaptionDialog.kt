import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.DeletePhotoRequest
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromPosterRequest
import com.example.jeonsilog.data.remote.dto.calendar.UploadImageReqEntity
import com.example.jeonsilog.databinding.DialogCaptionBinding
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.view.photocalendar.CommunicationListener
import com.example.jeonsilog.view.photocalendar.LoadBottomDialog
import com.example.jeonsilog.view.photocalendar.LoadPageDialog
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CaptionDialog(
    private val localDate: LocalDate,
    private val listener: CommunicationListener
) : DialogFragment() {
    private val MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 2
    private lateinit var binding: DialogCaptionBinding
    private lateinit var list: List<GetPhotoInformation>
    private var imageUri: Uri? = null
    private var index: Int? =null
    private var dimState = true //true:투명 false: 딤
    private var editState = true //true:없음 false:
    private var dismissListener: LoadBottomDialog.OnDismissListener? = null
    private var userInput: String=""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCaptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userInput=""
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams

        dimState = true
        editState = true
        checkEdit()
        checkDim()

        var date =yearMonthFromDate(localDate)
        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().getMyPhotoMonth(GlobalApplication.encryptedPrefs.getAT(),date)
            if(response.isSuccessful && response.body()!!.check){
                list= response.body()!!.information
            } else {
                list= response.body()!!.information
            }
        }

        binding.cvEdit.isGone=true


        var checkDate = yearMonthDayFromDate(localDate)
        index = list.indexOfFirst { it.date == checkDate }
        var imgUrl=list[index!!].imgUrl
        userInput=list[index!!].caption

        var myDate=list[index!!].date
        val inputDate = LocalDate.parse(myDate)
        val outputFormat = "yyyy/MM/dd (E)"
        val outputDateString = inputDate.format(DateTimeFormatter.ofPattern(outputFormat, Locale.KOREA))
        binding.tvDate.text=outputDateString
        Log.d("requireContext", "$list")
        GlideApp.with(requireContext())
            .load(imgUrl)
            .transform(CenterCrop())
            .into(binding.ivPhoto)

        binding.btEdit.setOnClickListener {
            editState=!editState
            checkEdit()

        }
        binding.btText.setOnClickListener {
            dimState=!dimState
            editState=true
            checkDim()
            checkEdit()
        }

        binding.btSave.setOnClickListener {
            var date =list[index!!].date
            var url =list[index!!].imgUrl
            var captionStr=userInput
            runBlocking(Dispatchers.IO) {
                val body= DeletePhotoRequest(DateUtil().monthYearFromDate(localDate))
                val response = CalendarRepositoryImpl().deletePhoto(GlobalApplication.encryptedPrefs.getAT(),body)
                if(response.isSuccessful && response.body()!!.check){
                }
            }
            runBlocking(Dispatchers.IO) {
                val body= PostPhotoFromPosterRequest(date,url,captionStr)
                val response = CalendarRepositoryImpl().postPhotoFromPoster(
                    GlobalApplication.encryptedPrefs.getAT(),body)
                Log.d("tag", "$response")
                if(response.isSuccessful && response.body()!!.check){
                    Log.d("UpCaption", "Image uploaded successfully : ${response.body()}")

                } else {
                    Log.e("Upload", "Image upload failed")
                }
            }
            val inputMethodManager = getSystemService(requireContext(), InputMethodManager::class.java)
            inputMethodManager?.hideSoftInputFromWindow(binding.captionEdittext.windowToken, 0)

            binding.captionEdittext.clearFocus()
            binding.btSave.isGone=true
        }

        binding.tvLoadPoster.setOnClickListener {
            // 새로운 다이얼로그 열기
            val loadPageDialog = LoadPageDialog(localDate,listener,"")
            loadPageDialog.show(parentFragmentManager, "LoadPageDialogTag")
            dismiss()
        }

        binding.tvLoadGallery.setOnClickListener {
            //갤러리 이미지 불러오기
            checkPermissionAndOpenGallery()

        }
        binding.tvDelete.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                val body= DeletePhotoRequest(DateUtil().monthYearFromDate(localDate))
                val response = CalendarRepositoryImpl().deletePhoto(GlobalApplication.encryptedPrefs.getAT(),body)
                if(response.isSuccessful && response.body()!!.check){
                }
            }
            dismiss()
            listener.onRecyclerViewItemClick(0)
        }


        binding.deleteBt.setOnClickListener {
            dismiss()
        }

    }

    private fun yearMonthFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMM")
        return date.format(formatter)
    }

    private fun yearMonthDayFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(formatter)
    }

    private fun checkEdit() {
        binding.cvEdit.isGone = editState
    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    private fun checkDim() {
        binding.btSave.isGone=dimState
        if (dimState) {
            binding.captionEdittext.isGone=true
            binding.ivPhoto.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.transparent
                ), android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        } else {
            binding.captionEdittext.setText(userInput)
            binding.captionEdittext.isGone=false
            binding.captionEdittext.isFocusable = true // EditText에 포커스를 설정할 수 있도록 합니다.
            binding.captionEdittext.requestFocus()
            binding.captionEdittext.setTextColor(ContextCompat.getColor(requireContext(), R.color.basic_white))
            binding.captionEdittext.background = null
            setEditTextMaxLength(binding.captionEdittext, 150)
            binding.captionEdittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // 입력이 변경되기 전에 호출됩니다. 사용하지 않습니다.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 입력이 변경될 때 호출됩니다.
                    userInput = s.toString()
                    Log.d("userInputuserInputuserInput", "$userInput")
                    // 변경된 텍스트를 처리합니다.
                    // 예를 들어, 변경된 텍스트를 저장하거나 다른 동작을 수행할 수 있습니다.
                }

                override fun afterTextChanged(s: Editable?) {
                    // 입력이 변경된 후에 호출됩니다. 사용하지 않습니다.
                }
            })
            binding.ivPhoto.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dim_zone_color
                ), android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        }
    }





    private fun checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                requestStoragePermission()
                Log.d("TAG01", "checkPermissionAndOpenGallery: ")
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                accessGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                requestStoragePermission()

            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                accessGallery()
            }
        }

    }

    private fun requestStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                // 권한을 이전에 거부한 경우
                showPermissionRationaleDialog()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES
                )
            }
        }else{

        }
    }

    private fun showPermissionRationaleDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.permission_denied))
            .setPositiveButton("이동") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun openAppSettings() {
        Log.d("openAppSettings", "openAppSettings: ")
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = android.net.Uri.parse("package:" +requireContext() .packageName)
        startActivity(intent)
    }

    private fun accessGallery(){
        // 갤러리 열기 Intent 생성
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)

    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data!!.data
            if (imageUri != null) {
                try {
                    patchMyPhotoCalendarImg(imageUri!!)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun patchMyPhotoCalendarImg(uri: Uri) {
        val file = File(ImageUtil().absolutelyPath(requireContext(), uri))
        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("img", file.name, imageRequestBody)

        val uploadImageReq = UploadImageReqEntity(DateUtil().monthYearFromDate(localDate),"")
        val requestJson = Gson().toJson(uploadImageReq)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(),requestJson)

        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().postPhotoFromGallery(GlobalApplication.encryptedPrefs.getAT(), requestBody, filePart)
            if(response.isSuccessful && response.body()!!.check){
                CoroutineScope(Dispatchers.Main).launch {
                    listener.onDialogButtonClick("Data to pass")
                }
                Log.d("gallery", "patchMyPhotoCalendarImg: ${response.body()!!.informationEntity.message}")
            }
            dismiss()
            dismissListener?.onDismiss()
        }
    }

    fun setOnDismissListener(listener: LoadBottomDialog.OnDismissListener) {
        dismissListener = listener
    }

    fun setEditTextMaxLength(editText: EditText, maxLength: Int) {
        val inputFilters = arrayOfNulls<InputFilter>(1)
        inputFilters[0] = InputFilter.LengthFilter(maxLength)
        editText.filters = inputFilters
    }

}
