package com.gsc.arcgis

import android.graphics.Outline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
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

        setupMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)

        addGraphics()
    }

    private fun setupMap(arcgisNova: BasemapStyle) {
//        val map = ArcGISMap(arcgisNova)
//        mapView.map = map
//        mapView.setViewpoint(Viewpoint(36.63452830082989, 114.85786988403322, 72000.0))
        val portal = Portal("http://192.168.0.25:6080/arcgis/rest/services/FORESTAPP/BaseMap/MapServer", false)
        val itemId = "41281c51f9de45edaf1c8ed44bb10e30"
        val portalItem = PortalItem(portal, itemId)
        val map = ArcGISMap(portalItem)
        mapView.map = map

        ArcGISRuntimeEnvironment.setLicense("")
    }

    private fun addGraphics() {
        val graphicsOverlay = GraphicsOverlay()
        mapView.graphicsOverlays.add(graphicsOverlay)
//        val point = Point(114.85786, 36.63452, SpatialReferences.getWgs84())
//        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, -0xa8cd, 10f)
        val simpleLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, -0xff9c01, 2f)
//        simpleMarkerSymbol.outline = simpleLineSymbol
//
//        val graphic = Graphic(point, simpleMarkerSymbol)
//        graphicsOverlay.graphics.add(graphic)
//
//        val polylinepoints = PointCollection(SpatialReferences.getWgs84()).apply {
//            add(114.82786, 36.65452)
//            add(114.88786, 36.69452)
//            add(114.87786, 36.60452)
//        }
//        val polyline = Polyline(polylinepoints)
//        val polyLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, -0xff9c01, 3f)
//        val polylinegraphic = Graphic(polyline, polyLineSymbol)
//        graphicsOverlay.graphics.add(polylinegraphic)


        val polygonPoints = PointCollection(SpatialReferences.getWgs84()).apply {
            add(114.81786, 36.61452)
            add(114.82786, 36.62452)
            add(114.83786, 36.63452)
            add(114.84786, 36.64452)
            add(114.85786, 36.65452)
        }
        val polygon = Polygon(polygonPoints)
        val polygonFillSymbol =
                SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, -0xff9c01, simpleLineSymbol)

        val polygonGraphic = Graphic(polygon, polygonFillSymbol)
        graphicsOverlay.graphics.add(polygonGraphic)
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