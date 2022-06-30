package com.caravan.caravan.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.databinding.ItemGuideCommentBinding
import com.caravan.caravan.model.Comment

class GuideCommentAdapter :
    PagingDataAdapter<Comment, GuideCommentAdapter.ViewHolder>(CommentDiffCallback()) {


    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val guideCommentBinding: ItemGuideCommentBinding) :
        RecyclerView.ViewHolder(guideCommentBinding.root) {
        fun onBind(comment: Comment?) {

            comment?.let {
                Glide.with(guideCommentBinding.ivCommentProfile).load(comment.from.photo)
                    .into(guideCommentBinding.ivCommentProfile)
                guideCommentBinding.tvCommentFullname.text =
                    comment.from.name + " " + comment.from.surname
                guideCommentBinding.tvCommentForGuide.text = comment.reviewContent
                guideCommentBinding.ratingBarComment.rating = comment.rate.toFloat()
                guideCommentBinding.tvRatingCount.text = comment.rate.toString()
                if (comment.answerContent != null) {
                    guideCommentBinding.tvIsAnswered.text = colorMyText("Answered", 0, 6, "#99000000")
                } else {
                    guideCommentBinding.tvIsAnswered.text =
                        colorMyText("Not answered", 0, 11, "#167351")
                }

                guideCommentBinding.llComment.setOnClickListener {
                    onItemClickListener?.invoke(comment)
                }
            }

        }

    }

    private var onItemClickListener: ((Comment) -> Unit)? = null

    fun setOnItemClickListener(listener: (Comment) -> Unit) {
        onItemClickListener = listener
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
        holder.onBind(getItem(position))

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