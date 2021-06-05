package `in`.technowolf.ipscanner.ui

import `in`.technowolf.ipscanner.data.IpDetailRS
import `in`.technowolf.ipscanner.data.IpScannerService
import `in`.technowolf.ipscanner.data.PublicIpService
import `in`.technowolf.ipscanner.utils.Extensions.readOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val ipScannerService: IpScannerService,
    private val publicIpService: PublicIpService
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
                _ipDetail.value = ipScannerService.getIpDetails(currentIpAddress)
            } else {
                _ipDetail.value = ipScannerService.getIpDetails(ipAddress)
            }
        }
    }

}