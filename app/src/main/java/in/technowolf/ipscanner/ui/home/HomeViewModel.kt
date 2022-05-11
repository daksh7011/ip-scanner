package `in`.technowolf.ipscanner.ui.home

import `in`.technowolf.ipscanner.data.local.IpDetailsDao
import `in`.technowolf.ipscanner.data.remote.IpDetailRS
import `in`.technowolf.ipscanner.data.remote.IpScannerService
import `in`.technowolf.ipscanner.data.remote.PublicIpService
import `in`.technowolf.ipscanner.utils.Extensions.readOnly
import `in`.technowolf.ipscanner.utils.safeCall
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val ipScannerService: IpScannerService,
    private val publicIpService: PublicIpService,
    private val ipDetailsDao: IpDetailsDao,
) : ViewModel() {

    private val _ipDetail: MutableLiveData<IpDetailRS?> = MutableLiveData()
    val ipDetails = _ipDetail.readOnly()

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData = _errorLiveData.readOnly()

    fun getIpDetails(ipAddress: String = "") {
        viewModelScope.launch {
            if (ipAddress.isEmpty()) {
                safeCall(
                    {
                        val response = publicIpService.getPublicIp()
                        if (response.isSuccessful) {
                            _ipDetail.value = retrieveIpDetails(response.body()?.ip.orEmpty())
                        } else {
                            _errorLiveData.value = "Could not fetch your public IP."
                        }
                    }, {
                        _errorLiveData.value = it.localizedMessage
                    }
                )
            } else {
                _ipDetail.value = retrieveIpDetails(ipAddress)
            }
        }
    }

    private suspend fun retrieveIpDetails(ipAddress: String): IpDetailRS? {
        return ipDetailsDao.getCachedIpAddress(ipAddress)?.toIpDetailRS()
            ?: getIpDetailsFromServer(ipAddress)
    }

    private suspend fun getIpDetailsFromServer(ipAddress: String): IpDetailRS? {
        return safeCall(
            {
                val response = ipScannerService.getIpDetails(ipAddress)
                if (response.isSuccessful) {
                    response.body()?.let {
                        delay(250L)
                        ipDetailsDao.saveIpDetails(it.toIpDetailsEntity())
                        it
                    }
                } else {
                    _errorLiveData.value =
                        "Could not fetch details. Please try again after a moment."
                    null
                }
            }, {
                _errorLiveData.value = it.localizedMessage
            }
        )
    }
}
