package com.gsc.arcgis

import android.app.Application
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.gisinfo.android.lib.base.BaseAppConfig
import com.gisinfo.android.lib.base.BaseApplication

class App: BaseApplication() {

    override fun getAppConfig(): BaseAppConfig {
        return super.getAppConfig()
    }
    override fun onCreate() {
        super.onCreate()
        ArcGISRuntimeEnvironment.setApiKey("AAPK02af9b9cdd3242039c7a0c5b39d39aabE8Qx-D6ZBpXN0wwJETrkfxA5m8rUtl6pE-X1kYupfxLNsb8_UgT4NevdrmQYIaSx")
    }
}