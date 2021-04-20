package com.gsc.arcgis

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.PolygonBuilder
import com.esri.arcgisruntime.geometry.PolylineBuilder
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.gisinfo.android.core.base.util.ToastUtils
import com.gisinfo.android.core.esri.ArcMapHelper
import com.gisinfo.android.core.esri.BaseMapOnTouchListener
import com.gisinfo.android.core.esri.DrawHelper
import com.gisinfo.android.core.esri.MapHelper
import com.gisinfo.android.core.esri.layer.ThirdMapLayer
import com.gisinfo.android.core.esri.layer.amap.AmapLayerType
import com.gisinfo.android.lib.base.BaseActivity
import com.gisinfo.android.lib.base.permission.OnCheckPermissionListener
import com.gsc.arcgis.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), View.OnClickListener {

    companion object {

        /**
         * 清单文件中，esri的lic的配置key
         */
        private const val KEY_LICENSE = "com.gisinfo.android.esri.license"

        /**
         * 基础地图
         */
        const val MAP_NAME_BASE_MAP = "base_map"

        /**
         * 影像图
         */
        const val MAP_NAME_RASTER = "raster"

        /**
         * 采集覆盖层的名称
         */
        const val OVERLAY_NAME_COLLECT = "collect"
    }

    lateinit var mapHelper: MapHelper

    private lateinit var binding: ActivityMainBinding

    private lateinit var daoUtils: DaoUtils

    private val locationDisplay: LocationDisplay?
        get() {
            val locationDisplay = binding.mapView.locationDisplay
            return locationDisplay
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // 去掉Esri的水印
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud7659408794,none,ZZ0RJAY3FY0GEBZNR002")
        //初始化数据库
        daoUtils = DaoUtils.getInstance(mContext)

        binding.mapView.isAttributionTextVisible = false
        mapHelper = MapHelper(binding.mapView) {
        }
        val locationDisplay = locationDisplay
        locationDisplay?.isShowLocation = true
        locationDisplay?.isShowPingAnimation = true
        locationDisplay?.isShowAccuracy = true
        locationDisplay?.autoPanMode = LocationDisplay.AutoPanMode.RECENTER

        // 动态权限申请
        checkPermission(
            object : OnCheckPermissionListener {
                override fun permissionDenied() {
                    finish()
                }

                override fun permissionGranted() {
                    initMap()
                    if (!LocationUtils.isLocServiceEnable(mContext)) {
                        ToastUtils.showLongToast(mContext, "系统定位服务未开始，无法定位")
                        return
                    }
                }
            },
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        binding.imageLocation.setOnClickListener {
            locationDisplay?.startAsync()
            initListener()
        }

        binding.mapView.addLayerViewStateChangedListener {
            locationDisplay?.startAsync()
            initListener()
        }

        binding.imageZhinanzhen.setOnClickListener {
            binding.mapView.setViewpointRotationAsync(0.0)
        }
        binding.btnClose.setOnClickListener(this)
        binding.btnDian.setOnClickListener(this)
        binding.btnXian.setOnClickListener(this)
        binding.btnMian.setOnClickListener(this)
        binding.tvStorage.setOnClickListener(this)
        binding.btnData.setOnClickListener(this)

        fillData()
    }

    fun fillData() {
    }

    private fun initListener() {
        val mapLocation = locationDisplay?.mapLocation
        Log.e("MainActivity--地图坐标系的坐标", mapLocation.toString())
        val wgs84Point = locationDisplay?.location?.position
        Log.e("Mainactivity--wgs84", wgs84Point.toString())
    }

    // 初始化地图
    private fun initMap() {
        // 基础地图
        val basemap = ThirdMapLayer.creatorOfAmap()
            .setCache(true)
            .setLayerType(AmapLayerType.VEC)
            .create()
        basemap.name = MAP_NAME_BASE_MAP

        // 加载地图
        mapHelper.create(ArcMapHelper())
        mapHelper.arcMapHelper.addBaseMapLayer(basemap)
        val graphicsOverlay = GraphicsOverlay()
        binding.mapView.graphicsOverlays.add(graphicsOverlay)

        binding.mapView.onTouchListener =
            object : BaseMapOnTouchListener(mapHelper) {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    val point = android.graphics.Point(e.x.toInt(), e.y.toInt())
                    val mapPoint: Point = binding.mapView.screenToLocation(point)
                    creadBuild(mapPoint, graphicsOverlay)
                    return super.onSingleTapConfirmed(e)
                }
            }
    }

    //创建一个用于绘制线的储存点list
    private val linePoints by lazy { ArrayList<Point>() }

    //创建一个用于绘制面的储存点list
    private val areaPoints by lazy { ArrayList<Point>() }

    fun creadBuild(point: Point, graphicsOverlay: GraphicsOverlay) {

        var graphic: Graphic? = null
        if (creadBuilding == 0) {
            return
        } else if (creadBuilding == DrawHelper.TYPE_POINT) {
            val simpleMarkerSymbol =
                SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 15f)
            //创建Graphic
            graphic = Graphic(point, simpleMarkerSymbol)
            //可以给graphic添加一些属性
            graphic.attributes["value"] = "Graphic0"
            //添加graphic到 GraphicsOverlay上
        } else if (creadBuilding == DrawHelper.TYPE_POLYLINE) {
            linePoints.add(point)
            if (linePoints.size == 1) {
                val simpleMarkerSymbol =
                    SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 15f)
                graphic = Graphic(point, simpleMarkerSymbol)
            } else {
//                graphicsOverlay.graphics.clear()
                //创建线的路径
                val polylineBuilder = PolylineBuilder(binding.mapView.spatialReference)
                //添加触摸的点作为路径
                polylineBuilder.addPoints(linePoints)
                //线的样式
                val simpleLineSymbol =
                    SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5f)
                graphic = Graphic(polylineBuilder.toGeometry(), simpleLineSymbol)
            }

        } else if (creadBuilding == DrawHelper.TYPE_POLYGON) {
            areaPoints.add(point)
            if (areaPoints.size == 1) {
                val simpleMarkerSymbol =
                    SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 5f)
                graphic = Graphic(point, simpleMarkerSymbol)
            } else if (areaPoints.size == DrawHelper.TYPE_POINT) {
                val polylineBuilder = PolylineBuilder(binding.mapView.spatialReference)
                polylineBuilder.addPoints(areaPoints)
                val simpleLineSymbol =
                    SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f)
                graphic = Graphic(polylineBuilder.toGeometry(), simpleLineSymbol)
            } else {
//                graphicsOverlay.graphics.clear()
                //创建面路径
                val polygonBuilder = PolygonBuilder(binding.mapView.spatialReference)
                polygonBuilder.addPoints(areaPoints)
                //线的样式
                val simpleLineSymbol =
                    SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f)
                val simpleFillSymbol =
                    SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, simpleLineSymbol)
                graphic = Graphic(polygonBuilder.toGeometry(), simpleFillSymbol)
            }
        }
        mapHelper.drawHelper.addPoint(point)
        graphicsOverlay.graphics.add(graphic)

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_close ->
                setview(0, "关闭创建")
            R.id.btn_dian ->
                setview(DrawHelper.TYPE_POINT, "创建点")
            R.id.btn_xian ->
                setview(DrawHelper.TYPE_POLYLINE, "创建线")
            R.id.btn_mian ->
                setview(DrawHelper.TYPE_POLYGON, "创建面")
            R.id.tv_storage ->
                storage()
            R.id.btn_data ->
                goActivity()
        }
    }

    fun storage() {
        val toJson = mapHelper.drawHelper.result.toJson()
        var intent: Intent? = Intent(this, KeepDataActivity::class.java)
        var dataBean = DataBean(creadBuilding, toJson);
        intent?.putExtra("info", dataBean)
        startActivity(intent)

    }

    fun goActivity() {
        var intent
                : Intent
        ? = Intent(this, AllDataActivity::class.java)
        startActivity(intent)
    }

    private var creadBuilding: Int = 0
    private fun setview(id: Int, name: String) {
        ToastUtils.showShortToast(mContext, name)
        mapHelper.drawHelper.startDraw(id)
        binding.tvBuilding.setText("正在$name")
        if (id == 0) {
            binding.tvBuilding.visibility = View.GONE
            binding.tvStorage.visibility = View.GONE
        } else {
            binding.tvBuilding.visibility = View.VISIBLE
            binding.tvStorage.visibility = View.VISIBLE
        }
        creadBuilding = id
    }

    override fun onResume() {
        super.onResume()
        mapHelper.resume()
    }

    override fun onPause() {
        super.onPause()
        mapHelper.pause()
    }

    override fun onDestroy() {
        mapHelper.destroy()
        super.onDestroy()
    }

}