package org.d3if3011.assesment_ll_mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3011.assesment_ll_mobpro.database.BukuaDao
import org.d3if3011.assesment_ll_mobpro.model.Buku

class DetailViewModel (private val dao: BukuaDao): ViewModel() {

    fun insert(judulBuku:String, penulisBuku:String, genreBuku: String){
        val buku = Buku (
            judulBuku = judulBuku,
            penulisBuku = penulisBuku,
            genreBuku = genreBuku
        )
        viewModelScope.launch (Dispatchers.IO){
            dao.insert(buku)
        }
    }

    suspend fun getBuku(id:Long): Buku?{
        return dao.getBukuById(id)
    }

    fun  update(id: Long, judulBuku: String, penulisBuku: String, genreBuku: String){
        val buku = Buku(
            id = id,
            judulBuku = judulBuku,
            penulisBuku = penulisBuku,
            genreBuku = genreBuku
        )
        viewModelScope.launch (Dispatchers.IO){
            dao.update(buku)
        }

    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteById(id)
        }
    }

}