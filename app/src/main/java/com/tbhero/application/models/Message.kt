package com.tbhero.application.models

data class Message(
    var id: String? = null,
    var senderId: String? = null,
    var senderName: String? = null,
    var text: String? = null,
    var timestamp: Long? = null
)