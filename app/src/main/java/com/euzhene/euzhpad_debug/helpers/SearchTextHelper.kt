package com.euzhene.euzhpad_debug.helpers

class SearchTextHelper {
    var startIndex: Int = 0
        private set
    var endIndex: Int = 0
        private set
    var previousText: String = ""
        private set

    fun nextOccurrence(sText: String, searchText: String) {
        startIndex = sText.indexOf(searchText, startIndex = startIndex + 1, ignoreCase = true)
        endIndex = startIndex + searchText.length
        if (startIndex == -1) {
            startIndex = 0
            endIndex = 0
        }
        previousText = searchText
    }

    fun reset() {
        startIndex = 0
        endIndex = 0
        previousText = ""
    }
}