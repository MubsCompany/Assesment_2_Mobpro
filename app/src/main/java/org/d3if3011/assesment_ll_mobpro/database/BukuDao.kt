package org.d3if3011.assesment_ll_mobpro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3011.assesment_ll_mobpro.model.Buku

@Dao
interface BukuaDao {
    @Insert
    suspend fun insert(buku: Buku)

    @Update
    suspend fun update(buku: Buku)

    @Query("SELECT * FROM buku ORDER BY genreBuku,judulBuku ASC")
    fun getBuku(): Flow<List<Buku>>

    @Query("SELECT * FROM buku WHERE id = :id")
    suspend fun getBukuById(id: Long):Buku?

    @Query("DELETE FROM buku WHERE id = :id")
    suspend fun deleteById(id:Long)

}