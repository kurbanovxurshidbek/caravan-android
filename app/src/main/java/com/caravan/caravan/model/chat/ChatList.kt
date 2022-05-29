package com.caravan.caravan.model.chat

data class ChatList(
    val profilePhoto: String,
    val guideId: String,
    val guideFullName: String,
    val latestMessage: String,
    val agreed: Boolean,
    val unSeenMessage: Boolean
)
