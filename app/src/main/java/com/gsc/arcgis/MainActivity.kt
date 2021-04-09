package com.gsc.arcgis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView
import com.gsc.arcgis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mapView:MapView by lazy {
        activityMainBinding.mapView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setupMap()
    }

    private fun setupMap(){
        ArcGISRuntimeEnvironment.setApiKey("AAPK02af9b9cdd3242039c7a0c5b39d39aabE8Qx-D6ZBpXN0wwJETrkfxA5m8rUtl6pE-X1kYupfxLNsb8_UgT4NevdrmQYIaSx")

        val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)
        mapView.map = map
        mapView.setViewpoint(Viewpoint(118.79647, 32.05838, 72000.0))
    }

    override fun onPause() {
        mapView.pause()
        super.onPause()
    }

    override fun onResume() {
        mapView.resume()
        super.onResume()
    }

    override fun onDestroy() {
        mapView.dispose()
        super.onDestroy()
    }
}