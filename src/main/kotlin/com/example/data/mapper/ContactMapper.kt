package com.example.data.mapper

import com.example.data.database.exposed.entity.ContactRequestEntity
import com.example.data.model.contact.ContactRequest
import com.example.data.response.ContactRequestResponse
import com.example.util.toLong

fun ContactRequestEntity?.toContactRequest(): ContactRequest? {

    if (this == null) {
        return null
    }

    return ContactRequest(
        id = this.id.value,
        sender = this.sender.toUser(),
        recipient = this.recipient.toUser(),
        status = this.status
    )
}

fun ContactRequest.toContactRequestResponse(): ContactRequestResponse = ContactRequestResponse(
    id = this.id,
    sender = this.sender.toSimpleUserResponse(),
    recipient = this.recipient.toSimpleUserResponse(),
    status = this.status,
    createdAt = this.createdAt.toLong()
)




