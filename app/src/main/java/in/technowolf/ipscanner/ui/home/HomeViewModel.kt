package `in`.technowolf.ipscanner.ui.home

import `in`.technowolf.ipscanner.data.local.IpDetailsDao
import `in`.technowolf.ipscanner.data.remote.IpDetailRS
import `in`.technowolf.ipscanner.data.remote.IpScannerService
import `in`.technowolf.ipscanner.data.remote.PublicIpService
import `in`.technowolf.ipscanner.utils.Extensions.readOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val ipScannerService: IpScannerService,
    private val publicIpService: PublicIpService,
    private val ipDetailsDao: IpDetailsDao,
) : ViewModel() {

    private val _ipDetail: MutableLiveData<IpDetailRS> = MutableLiveData()
    val ipDetails = _ipDetail.readOnly()

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData = _errorLiveData.readOnly()

    fun getIpDetails(ipAddress: String = "") {
        viewModelScope.launch {
            if (ipAddress.isEmpty()) {
                val response = publicIpService.getPublicIp()
                if (response.isSuccessful) {
                    _ipDetail.value = retrieveIpDetails(response.body()?.ip.orEmpty())
                } else {
                    _errorLiveData.value = "Could not fetch your public IP."
                }
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
