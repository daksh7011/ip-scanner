package `in`.technowolf.ipscanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IpDetailsDao {
    @Query("select * from IpDetails")
    suspend fun getAllCachedIpAddress(): List<IpDetailsEntity?>

    @Query("select * from IpDetails where ipAddress=:ipAddress")
    suspend fun getCachedIpAddress(ipAddress: String): IpDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveIpDetails(ipDetailsEntity: IpDetailsEntity)
}
