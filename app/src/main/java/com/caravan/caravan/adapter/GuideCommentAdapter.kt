package com.caravan.caravan.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.databinding.ItemGuideCommentBinding
import com.caravan.caravan.model.Comment
import com.caravan.caravan.ui.fragment.guideOption.FeedbackListFragment

class GuideCommentAdapter(var context: FeedbackListFragment, var items: ArrayList<Comment>) :
    RecyclerView.Adapter<GuideCommentAdapter.ViewHolder>() {
    inner class ViewHolder(private val guideCommentBinding: ItemGuideCommentBinding) :
        RecyclerView.ViewHolder(guideCommentBinding.root) {
        fun onBind(comment: Comment) {
            Glide.with(guideCommentBinding.ivCommentProfile).load(comment.from.photo)
                .into(guideCommentBinding.ivCommentProfile)
            guideCommentBinding.tvCommentFullname.text =
                comment.from.name + " " + comment.from.surname
            guideCommentBinding.tvCommentForGuide.text = comment.reviewContent
            guideCommentBinding.ratingBarComment.rating = comment.rate.toFloat()
            guideCommentBinding.tvRatingCount.text = comment.rate.toString()
            if (comment.answerContent != null) {
                guideCommentBinding.tvIsAnswered.text = colorMyText("Aswered", 0, 6, "#99000000")
            } else {
                guideCommentBinding.tvIsAnswered.text =
                    colorMyText("Not answered", 0, 11, "#167351")
            }

            guideCommentBinding.llComment.setOnClickListener {
                context.navigateToFeedbackResponse(comment)
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