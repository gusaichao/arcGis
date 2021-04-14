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

    private val mapView: MapView by lazy {
        activityMainBinding.mapView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setupMap(BasemapStyle.ARCGIS_NOVA)
    }

    private fun setupMap(arcgisNova: BasemapStyle) {
        val map = ArcGISMap(arcgisNova)
        mapView.map = map
        mapView.setViewpoint(Viewpoint(36.63452830082989, 114.85786988403322, 72000.0))
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