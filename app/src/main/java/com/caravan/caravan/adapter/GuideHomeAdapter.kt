package com.caravan.caravan.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemGuideHomeBinding
import com.caravan.caravan.model.GuideProfile

class GuideHomeAdapter(private val list: List<GuideProfile>)
    : RecyclerView.Adapter<GuideHomeAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemGuideHomeBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(guide: GuideProfile){
//            Glide.with(itemBinding.ivProfilePhoto).load(guide.profile.profilePhoto).into(itemBinding.ivProfilePhoto)
            itemBinding.tvName.text = guide.profile.name
            itemBinding.tvName.isSelected = true
            itemBinding.tvPrice.text = price(guide)
//            itemBinding.tvProvince.text = provinces(guide)

            itemView.setOnClickListener {
                //When item clicked
            }
        }

        @SuppressLint("ResourceAsColor")
        private fun price(guide: GuideProfile) : Spannable {
            val text = "$${guide.price.price.toInt()}"
            val endIndex = text.length

            val outPutColoredText: Spannable = SpannableString("$text/${guide.price.option}")
            outPutColoredText.setSpan(RelativeSizeSpan(1.2f),0, endIndex, 0)
            outPutColoredText.setSpan(ForegroundColorSpan(Color.parseColor("#167351")), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return outPutColoredText
        }

        private fun provinces(guide: GuideProfile) : Spannable{
            val province = guide.travelLocations[0]
            val numberOfProvince = guide.travelLocations.size

            return if(numberOfProvince > 1){
                val text = "${province.province} and ${numberOfProvince - 1} more"
                val endIndex = province.province.length
                colorMyText(text, 0, endIndex, R.color.main_color2)
            }else{
                colorMyText(province.province, 0, province.province.length, R.color.main_color2)
            }
        }

        private fun colorMyText(inputText:String, startIndex :Int, endIndex:Int, textColor:Int):Spannable{
            val outPutColoredText: Spannable = SpannableString(inputText)
            outPutColoredText.setSpan(ForegroundColorSpan(textColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return outPutColoredText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGuideHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}