package com.euzhene.euzhpad.domain.usecase

import com.euzhene.euzhpad.domain.entity.Filter
import com.euzhene.euzhpad.domain.repository.NoteListRepository
import javax.inject.Inject

class GetDefaultFilterUseCase @Inject constructor(
    private val repository: NoteListRepository
) {
    operator fun invoke():Filter = repository.getDefaultFilter()
}