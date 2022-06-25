package dev.nguyen.crewtalk.models

class Chats {
    private var senderID: String = ""
    private var receiverID: String = ""
    private var message: String= ""

    constructor()
    constructor(senderId: String, receiverId: String, message: String) {
        this.senderID = senderId
        this.receiverID = receiverId
        this.message = message
    }
    fun getSenderId(): String = this.senderID
    fun getReceiverId(): String = this.receiverID
    fun getMessage(): String = this.message
}