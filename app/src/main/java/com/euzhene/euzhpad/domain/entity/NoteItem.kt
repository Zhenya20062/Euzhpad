package com.euzhene.euzhpad.domain.entity

data class NoteItem(
    val id: Int = UNKNOWN_ID,
    val title: String,
    val content: String,
    val createDate: String,
    val lastEditTime: String,
    val password: String? = null
) {
    companion object {
        const val UNKNOWN_ID = 0
    }
}

