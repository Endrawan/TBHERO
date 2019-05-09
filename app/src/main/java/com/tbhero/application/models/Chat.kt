package com.tbhero.application.models

data class Chat(
    var id: String? = null,
    var recipientId: String? = null,
    var recipientName: String? = null,
    var senderId: String? = null,
    var senderName: String? = null,
    var recentMessage: String? = null,
    var notifCount: Int? = null,
    var timestamp: Long? = null,
    var reverseTimestamp: Long? = null
) {
    fun newMessage(message: Message) {
        senderId = message.senderId
        senderName = message.senderName
        recentMessage = message.text
        timestamp = message.timestamp
        reverseTimestamp = -1 * timestamp!!

        if (recipientId == senderId) notifCount = notifCount?.plus(1)
    }
}