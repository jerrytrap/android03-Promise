package com.boosters.promise.service.locationupload

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import javax.inject.Inject

class LocationUploadServiceConnection @Inject constructor() : ServiceConnection {

    var locationUploadService: LocationUploadService? = null
        private set

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        locationUploadService = (binder as? LocationUploadServiceBinder)?.service
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        locationUploadService = null
    }

}