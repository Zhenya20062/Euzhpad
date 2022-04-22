package com.euzhene.euzhpad.domain.entity

data class NoteItem(
    val id: Int = 0,
    val title: String,
    val content: String,
    val lastEditTime: String
)