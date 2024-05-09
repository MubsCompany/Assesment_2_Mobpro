package org.d3if3011.assesment_ll_mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3011.assesment_ll_mobpro.database.BukuaDao
import org.d3if3011.assesment_ll_mobpro.model.Buku

class MainViewModel(dao: BukuaDao) : ViewModel() {

    val data : StateFlow<List<Buku>> = dao.getBuku().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

}