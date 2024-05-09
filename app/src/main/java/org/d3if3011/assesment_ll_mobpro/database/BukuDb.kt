package org.d3if3011.assesment_ll_mobpro.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import org.d3if3011.assesment_ll_mobpro.model.Buku

@Database(entities = [Buku::class], version = 1, exportSchema = false)
abstract class BukuDb: RoomDatabase() {

    abstract val dao:BukuaDao

    companion object{
        @Volatile
        private var INSTANCE: BukuDb? = null

        fun getInstance(context: Context):BukuDb{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = databaseBuilder(
                        context.applicationContext,
                        BukuDb::class.java,
                        "mahasiswa.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }

}