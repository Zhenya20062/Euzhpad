package com.euzhene.euzhpad.domain.entity

data class NoteItem(
    val id: Int = UNKNOWN_ID,
    val title: String,
    val content: String,
    val lastEditTime: String
) {
    companion object {
        const val UNKNOWN_ID = 0
    }
}

