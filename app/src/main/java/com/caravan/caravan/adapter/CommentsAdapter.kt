package com.caravan.caravan.adapter

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemCommentBinding
import com.caravan.caravan.model.Comment

class CommentsAdapter(var items: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    inner class ViewHolder(private val commentBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(commentBinding.root) {
        fun onBind(comment: Comment) {
            Glide.with(commentBinding.ivCommentsUserProfile).load(comment.from.photo)
                .placeholder(R.drawable.user)
                .into(commentBinding.ivCommentsUserProfile)
            commentBinding.tvCommentsUserFullname.text =
                comment.from.name.plus(" ").plus(" ").plus(comment.from.surname)
            commentBinding.tvCommentsUserLocaldate.text = comment.reviewTime.substring(0, 10)
            commentBinding.ratingBarCommentUser.rating = comment.rate.toFloat()
            commentBinding.tvCommentsUserRate.text = "(${comment.rate.toString().toInt()})"
            commentBinding.tvCommentsContent.text = comment.reviewContent

            // Guide`s answer here
            if (comment.answerContent != null) {
                commentBinding.llAnswerPage.visibility = View.GONE
                Glide.with(commentBinding.ivCommentsGuideProfile).load(comment.guide?.photo)
                    .placeholder(R.drawable.user)
                    .into(commentBinding.ivCommentsGuideProfile)
                commentBinding.tvCommentsGuideFullname.text =
                    comment.guide?.name.plus(" ").plus(comment.guide?.surname)
                commentBinding.tvCommentsGuideLocaldate.text = comment.answerTime?.substring(0, 10)
                commentBinding.tvCommentsGuideAnswer.text = comment.answerContent
            } else {
                commentBinding.llAnswerPage.visibility = View.GONE
            }

        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size
}