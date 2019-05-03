package com.tbhero.application.models

data class Message(
    var id: String,
    var senderId: String,
    var text: String,
    var timestamp: Long
)