package com.caravan.caravan.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemGuideCommentBinding
import com.caravan.caravan.model.Comment

class GuideCommentAdapter(var items: ArrayList<Comment>) :
    RecyclerView.Adapter<GuideCommentAdapter.ViewHolder>() {
    inner class ViewHolder(private val guideCommentBinding: ItemGuideCommentBinding) :
        RecyclerView.ViewHolder(guideCommentBinding.root) {
        fun onBind(comment: Comment) {
            Glide.with(guideCommentBinding.ivCommentProfile).load(comment.from.profilePhoto)
                .into(guideCommentBinding.ivCommentProfile)
            guideCommentBinding.tvCommentFullname.text =
                comment.from.name + " " + comment.from.surname
            guideCommentBinding.tvCommentForGuide.text = comment.info
            guideCommentBinding.ratingBarComment.rating = comment.rate.toFloat()
            guideCommentBinding.tvRatingCount.text = comment.rate.toString()
            if (comment.isAnswered) {
                guideCommentBinding.tvIsAnswered.text = colorMyText("Aswered", 0, 6, 99000000)
            } else {
                guideCommentBinding.tvIsAnswered.text =
                    colorMyText("Not answered", 0, 11, R.color.main_color)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGuideCommentBinding.inflate(
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