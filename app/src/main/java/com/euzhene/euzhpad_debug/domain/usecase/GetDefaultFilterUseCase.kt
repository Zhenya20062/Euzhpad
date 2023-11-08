package com.euzhene.euzhpad_debug.domain.usecase

import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.repository.NoteListRepository
import javax.inject.Inject

class GetDefaultFilterUseCase @Inject constructor(
    private val repository: NoteListRepository
) {
    operator fun invoke():Filter = repository.getDefaultFilter()
}