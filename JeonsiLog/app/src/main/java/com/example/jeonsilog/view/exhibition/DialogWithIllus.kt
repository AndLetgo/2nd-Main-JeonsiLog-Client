package com.example.jeonsilog.view.exhibition

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.databinding.DialogWithIllusBinding
import com.example.jeonsilog.repository.reply.ReplyRepositoryImpl
import com.example.jeonsilog.repository.report.ReportRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class DialogWithIllus(
    private val type:String, private val contentId:Int, private val reviewSide:Int, private val position: Int, private val dialogWithIllusInterface: DialogWithIllusInterface):
    DialogFragment(){
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private var _binding: DialogWithIllusBinding? = null
    private val binding get() = _binding!!
    val TAG = "Dialog"

    override fun onStart() {
        super.onStart()

        val widthInDp = 324

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_with_illus, container, false)

        when(type){
            "신고_감상평" -> { setReportReview() }
            "신고_댓글" -> { setReportReply() }
            "삭제_감상평" -> { setDeleteReview() }
            "삭제_댓글" -> { setDeleteReply() }
            "감상평" -> { setCancelReview() }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        return binding.root
    }

    private fun setReportReview(){
        binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_report) })
        binding.tvDialogMessage.text = getString(R.string.dialog_report, "감상평")
        binding.btnConfirm.text = getString(R.string.btn_report)
        binding.btnConfirm.setOnClickListener{
            reportReview("REVIEW")
            dismiss()
        }
    }
    private fun setReportReply(){
        binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_report) })
        binding.tvDialogMessage.text = getString(R.string.dialog_report, "댓글")
        binding.btnConfirm.text = getString(R.string.btn_report)
        binding.btnConfirm.setOnClickListener{
            reportReview("REPLY")
            dismiss()
        }
    }
    private fun reportReview(reportType:String){
        runBlocking(Dispatchers.IO){
            val body = PostReportRequest(reportType, contentId)
            val response = ReportRepositoryImpl().postReport(encryptedPrefs.getAT(), body)
            if(response.isSuccessful && response.body()!!.check){
                Log.d("interest", "init: 등록 성공")
            }
        }
        Toast.makeText(context, getString(R.string.toast_report_success), Toast.LENGTH_SHORT).show()
    }
    private fun setDeleteReview(){
        binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_delete) })
        binding.tvDialogMessage.text = getString(R.string.dialog_delete, "감상평")
        binding.btnConfirm.text = getString(R.string.btn_delete_dialog)
        binding.btnConfirm.setOnClickListener{
            deleteReview()
            if(extraActivityReference==1){
                activity?.finish()
            }else{
                if(reviewSide == 1){
                    exhibitionViewModel.setCheckReviewDelte(true)
                    parentFragment?.view?.let { it1 -> Navigation.findNavController(it1).popBackStack() }
                }else{
                    dialogWithIllusInterface.confirmButtonClick(position)
                }
            }
            dismiss()
        }
    }
    private fun deleteReview(){
        runBlocking(Dispatchers.IO) {
            ReviewRepositoryImpl().deleteReview(encryptedPrefs.getAT(), contentId)
        }
        Toast.makeText(context, getString(R.string.toast_delete_review), Toast.LENGTH_SHORT).show()
    }
    private fun setDeleteReply(){
        binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_delete) })
        binding.tvDialogMessage.text = getString(R.string.dialog_delete, "댓글")
        binding.btnConfirm.text = getString(R.string.btn_delete_dialog)
        binding.btnConfirm.setOnClickListener{
            deleteReply()
            dialogWithIllusInterface.confirmButtonClick(position)
            dismiss()
        }
    }
    private fun deleteReply(){
        runBlocking(Dispatchers.IO){
            ReplyRepositoryImpl().deleteReply(encryptedPrefs.getAT(), contentId)
        }
        Toast.makeText(context, getString(R.string.toast_delete_reply), Toast.LENGTH_SHORT).show()
    }
    private fun setCancelReview(){
        Log.d(TAG, "onCreateView: 감상평")
        binding.ivDialogIllus.isVisible = false
        binding.tvDialogMessage.text = getString(R.string.dialog_close_review)
        binding.btnConfirm.text = getString(R.string.btn_wrting_review_close)
        binding.btnConfirm.setOnClickListener{
            parentFragment?.view?.let { it1 -> Navigation.findNavController(it1).popBackStack() }
            dismiss()
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}

interface DialogWithIllusInterface{
    fun confirmButtonClick(position: Int)
}