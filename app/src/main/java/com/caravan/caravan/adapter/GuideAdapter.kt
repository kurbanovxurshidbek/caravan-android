package com.caravan.caravan.adapter

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
import com.caravan.caravan.databinding.ItemGuideBinding
import com.caravan.caravan.model.Language
import com.caravan.caravan.model.search.SearchGuide
import com.caravan.caravan.ui.fragment.BaseFragment

class GuideAdapter(var context: BaseFragment, var items: ArrayList<SearchGuide>) :
    RecyclerView.Adapter<GuideAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemGuideBinding: ItemGuideBinding) :
        RecyclerView.ViewHolder(itemGuideBinding.root) {

        fun onBind(guide: SearchGuide) {

            Glide.with(itemGuideBinding.ivGuide).load(guide.profilePhoto)
                .placeholder(R.drawable.user)
                .into(itemGuideBinding.ivGuide)
            itemGuideBinding.tvGuidesFullname.text =
                guide.name.plus(" ").plus(guide.surname)
            itemGuideBinding.tvGuidesCities.text = provinces(guide)
            itemGuideBinding.tvGuidePrice.text = price(guide)
            itemGuideBinding.tvGuidesLanguages.text = getLanguages(guide.languages)
            itemGuideBinding.ratingBarGuide.rating = guide.rate.toFloat()
            itemGuideBinding.tvGuidesCommentsCount.text =
                "(".plus(guide.reviewCount.toString()).plus(")")


            itemView.setOnClickListener {
                context.goToDetailsActivity(items[adapterPosition])
            }

        }

        private fun getLanguages(languages: ArrayList<Language>?): String {
            var text = ""
            languages?.let {
                for (language in 0..languages.size - 2) {
                    text += "${languages[language].name} "
                    text += ","
                }
                text += languages[languages.size - 1].name
            }

            return text
        }

        private fun price(guide: SearchGuide): Spannable {
            val text = "$${guide.price.cost.toInt()}"
            val endIndex = text.length

            val outPutColoredText: Spannable = SpannableString("$text/${guide.price.type}")
            outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
            outPutColoredText.setSpan(
                ForegroundColorSpan(Color.parseColor("#167351")),
                0,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return outPutColoredText
        }

        private fun provinces(guide: SearchGuide): Spannable {
            var text = ""
            for (province in guide.travelLocations) {
                text += "${province.district} "
            }
            return colorMyText(text, 0, text.length, "#167351")
        }

        private fun colorMyText(
            inputText: String,
            startIndex: Int,
            endIndex: Int,
            textColor: String
        ): Spannable {
            val outPutColoredText: Spannable = SpannableString(inputText)
            outPutColoredText.setSpan(
                Color.parseColor(textColor),
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