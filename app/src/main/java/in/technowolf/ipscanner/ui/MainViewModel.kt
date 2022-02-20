package `in`.technowolf.ipscanner.ui

import `in`.technowolf.ipscanner.data.local.IpDetailsDao
import `in`.technowolf.ipscanner.data.remote.IpDetailRS
import `in`.technowolf.ipscanner.data.remote.IpScannerService
import `in`.technowolf.ipscanner.data.remote.PublicIpService
import `in`.technowolf.ipscanner.utils.Extensions.readOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val ipScannerService: IpScannerService,
    private val publicIpService: PublicIpService,
    private val ipDetailsDao: IpDetailsDao,
) : ViewModel() {

    init {
        getIpDetails()
    }

    private val _ipDetail: MutableLiveData<IpDetailRS> = MutableLiveData()
    val ipDetails = _ipDetail.readOnly()

    fun getIpDetails(ipAddress: String = "") {
        viewModelScope.launch {
            if (ipAddress.isEmpty()) {
                val currentIpAddress = publicIpService.getPublicIp().ip
                _ipDetail.value = retrieveIpDetails(currentIpAddress)
            } else {
                _ipDetail.value = retrieveIpDetails(ipAddress)
            }
        }
    }

    private suspend fun retrieveIpDetails(ipAddress: String): IpDetailRS {
        return ipDetailsDao.getCachedIpAddress(ipAddress)?.toIpDetailRS()
            ?: getIpDetailsFromServer(ipAddress)
    }

    private suspend fun getIpDetailsFromServer(ipAddress: String): IpDetailRS {
        val ipDetails = ipScannerService.getIpDetails(ipAddress)
        ipDetailsDao.saveIpDetails(ipDetails.toIpDetailsEntity())
        return ipDetails
    }

}