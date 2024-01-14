package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
//import com.example.jeonsilog.viewmodel.ExhibitionPosterViewModel

class AdminPosterVpAdapter(private val posterList: List<Int>,private val  context: Context):PagerAdapter() {
    private var listener: CountListener? = null

    override fun getCount(): Int = posterList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imageView = ImageView(context)
        imageView.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageResource(posterList[position])
//            Glide.with(context)
//                .load(posterList[position]) //List<String>으로 변경
//                .transform(CenterCrop())
//                .into(imageView)
        }
        container.addView(imageView)
        return imageView
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener{
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {}

        override fun onPageSelected(position: Int) {
            listener?.setCount(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    interface CountListener {
        fun setCount(position: Int)
    }

    fun setCountListener(listener: CountListener){
        this.listener = listener
    }

    fun setOnPageChangeListener(viewPager: ViewPager){
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }
}