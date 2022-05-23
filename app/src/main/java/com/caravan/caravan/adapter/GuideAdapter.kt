package com.caravan.caravan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemGuideBinding
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.ui.fragment.main.SearchFragment

class GuideAdapter(private val context: Fragment, var items: ArrayList<GuideProfile>) :
    RecyclerView.Adapter<GuideAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemGuideBinding: ItemGuideBinding) : RecyclerView.ViewHolder(itemGuideBinding.root) {

        fun onBind(guideProfile: GuideProfile) {

            Glide.with(itemGuideBinding.ivGuide).load(guideProfile.profile.profilePhoto).into(itemGuideBinding.ivGuide)
            itemGuideBinding.tvGuidesFullname.text = guideProfile.profile.name + " " + guideProfile.profile.surname
            itemGuideBinding.tvGuidesCities.text = provinces(guideProfile)
            itemGuideBinding.tvGuidePrice.text = price(guideProfile)
            itemGuideBinding.tvGuidesLanguages.text = getLanguages(guideProfile)
            itemGuideBinding.ratingBarGuide.rating = guideProfile.rate.toFloat()
            itemGuideBinding.tvGuidesCommentsCount.text = "(${guideProfile.comments.size.toString()})"

            itemView.setOnClickListener {
                    // When Item Clicked
            }

        }

        fun getLanguages(guide: GuideProfile): String {
            var text = ""
            for (language in guide.languages) {
                text += "${language} "
            }
            return text
        }

        @SuppressLint("ResourceAsColor")
        private fun price(guide: GuideProfile): Spannable {
            val text = "$${guide.price.price.toInt()}"
            val endIndex = text.length

            val outPutColoredText: Spannable = SpannableString("$text/${guide.price.option}")
            outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
            outPutColoredText.setSpan(
                ForegroundColorSpan(R.color.main_color),
                0,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return outPutColoredText
        }

        private fun provinces(guide: GuideProfile): Spannable {
            var text = ""
            for (province in guide.travelLocations) {
                text += "$province "
            }
            return colorMyText(text, 0, text.length, R.color.main_color)
        }

        private fun colorMyText(
            inputText: String,
            startIndex: Int,
            endIndex: Int,
            textColor: Int
        ): Spannable {
            val outPutColoredText: Spannable = SpannableString(inputText)
            outPutColoredText.setSpan(
                ForegroundColorSpan(textColor),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return outPutColoredText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGuideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])

    }

    override fun getItemCount(): Int = items.size
}