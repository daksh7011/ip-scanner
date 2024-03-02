package `in`.technowolf.ipscanner.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.technowolf.ipscanner.data.local.IpDetailsDao
import `in`.technowolf.ipscanner.data.local.IpDetailsEntity

@Database(
    entities = [
        IpDetailsEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class IpScannerDatabase : RoomDatabase() {
    abstract fun getIpDetailsDao(): IpDetailsDao
}
