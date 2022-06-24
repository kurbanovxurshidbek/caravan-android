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
import com.caravan.caravan.model.home.HomeGuide
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.ui.fragment.main.HomeFragment

class GuideHomeAdapter(private val context: BaseFragment, private val list: List<HomeGuide>)
    : RecyclerView.Adapter<GuideHomeAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemGuideHomeBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(guide: HomeGuide){
            Glide.with(context).load(guide.profilePhoto).placeholder(R.drawable.user).into(itemBinding.ivProfilePhoto)
            itemBinding.tvName.text = guide.name
            itemBinding.tvName.isSelected = true
            itemBinding.tvPrice.text = price(guide)
            itemBinding.tvProvince.text = provinces(guide)

            itemView.setOnClickListener {
                if(context is HomeFragment){
                    context.goToDetailsActivityFromHome(list[adapterPosition])
                }
            }
        }

        @SuppressLint("ResourceAsColor")
        private fun price(guide: HomeGuide) : Spannable {
            val text = "$${guide.price.cost.toInt()}"
            val endIndex = text.length

            val outPutColoredText: Spannable = SpannableString("$text/${guide.price.type}")
            outPutColoredText.setSpan(RelativeSizeSpan(1.2f),0, endIndex, 0)
            outPutColoredText.setSpan(ForegroundColorSpan(Color.parseColor("#167351")), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return outPutColoredText
        }

        private fun provinces(guide: HomeGuide) : Spannable{
            val province = guide.travelLocations[0]
            val numberOfProvince = guide.travelLocations.size

            return if(numberOfProvince > 1){
                val text = "${province.provence} and ${numberOfProvince - 1} more"
                val endIndex = province.provence.length
                colorMyText(text, 0, endIndex)
            }else{
                colorMyText(province.provence, 0, province.provence.length)
            }
        }

        private fun colorMyText(inputText:String, startIndex :Int, endIndex:Int):Spannable{
            val outPutColoredText: Spannable = SpannableString(inputText)
            outPutColoredText.setSpan(ForegroundColorSpan(Color.parseColor("#167351")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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