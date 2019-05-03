package com.tbhero.application.models

data class Chat(
    var id: String,
    var senderId: String,
    var name: String,
    var recentMessage: String,
    var notifCount: Int,
    var timestamp: Long
)