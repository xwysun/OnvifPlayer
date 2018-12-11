package com.xwymodule.onvif.db

import android.content.Context
import androidx.room.*
import com.xwymodule.onvif.OnvifDevice

@Database(entities = arrayOf(OnvifDevice::class), version = 1)
abstract class OnvifDatabase : RoomDatabase() {
    abstract fun onvifDao(): OnvifDao
    companion object {
        var INSTANCE:OnvifDatabase?=null
        fun createDb(applicationContext:Context):OnvifDatabase?{
            if (INSTANCE==null){
                INSTANCE=Room.databaseBuilder(
                        applicationContext,
                        OnvifDatabase::class.java, "onvif"
                ).build()
            }
            return INSTANCE
        }
        fun closeDb(){
            INSTANCE?.close()
            INSTANCE=null
        }
    }
}

@Dao
interface OnvifDao {
    @Query("SELECT * FROM onvif")
    fun getAll(): List<OnvifDevice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: OnvifDevice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg devices:OnvifDevice )

    @Delete
    fun delete(device: OnvifDevice)

    @Query("DELETE FROM onvif")
    fun deleteAll()
}
