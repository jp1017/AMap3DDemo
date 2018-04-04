package com.amap.map3d.demo

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView

import com.amap.api.maps.MapsInitializer
import com.amap.map3d.demo.basic.Animate_CameraActivity
import com.amap.map3d.demo.basic.BasicMapActivity
import com.amap.map3d.demo.basic.CameraActivity
import com.amap.map3d.demo.basic.EventsActivity
import com.amap.map3d.demo.basic.GestureSettingsActivity
import com.amap.map3d.demo.basic.HeatMapActivity
import com.amap.map3d.demo.basic.LayersActivity
import com.amap.map3d.demo.basic.LimitBoundsActivity
import com.amap.map3d.demo.basic.LogoSettingsActivity
import com.amap.map3d.demo.basic.MapOptionActivity
import com.amap.map3d.demo.basic.MinMaxZoomLevelActivity
import com.amap.map3d.demo.basic.PoiClickActivity
import com.amap.map3d.demo.basic.ScreenShotActivity
import com.amap.map3d.demo.basic.TwoMapActivity
import com.amap.map3d.demo.basic.UiSettingsActivity
import com.amap.map3d.demo.basic.ViewPagerWithMapActivity
import com.amap.map3d.demo.basic.ZoomActivity
import com.amap.map3d.demo.basic.map.MapImpMethodActivity
import com.amap.map3d.demo.busline.BusStationActivity
import com.amap.map3d.demo.busline.BuslineActivity
import com.amap.map3d.demo.cloud.CloudActivity
import com.amap.map3d.demo.district.DistrictActivity
import com.amap.map3d.demo.district.DistrictWithBoundaryActivity
import com.amap.map3d.demo.geocoder.GeocoderActivity
import com.amap.map3d.demo.geocoder.ReGeocoderActivity
import com.amap.map3d.demo.indoor.IndoorMapActivity
import com.amap.map3d.demo.inputtip.InputtipsActivity
import com.amap.map3d.demo.location.CameraChangeActivity
import com.amap.map3d.demo.location.CustomLocationActivity
import com.amap.map3d.demo.location.LocationModeSourceActivity
import com.amap.map3d.demo.offlinemap.OfflineMapActivity
import com.amap.map3d.demo.opengl.OpenglActivity
import com.amap.map3d.demo.overlay.ArcActivity
import com.amap.map3d.demo.overlay.CircleActivity
import com.amap.map3d.demo.overlay.ColourfulPolylineActivity
import com.amap.map3d.demo.overlay.CustomMarkerActivity
import com.amap.map3d.demo.overlay.GeodesicActivity
import com.amap.map3d.demo.overlay.GroundOverlayActivity
import com.amap.map3d.demo.overlay.InfoWindowActivity
import com.amap.map3d.demo.overlay.MarkerActivity
import com.amap.map3d.demo.overlay.MarkerAnimationActivity
import com.amap.map3d.demo.overlay.MarkerClickActivity
import com.amap.map3d.demo.overlay.NavigateArrowOverlayActivity
import com.amap.map3d.demo.overlay.PolygonActivity
import com.amap.map3d.demo.overlay.PolylineActivity
import com.amap.map3d.demo.overlay.TileOverlayActivity
import com.amap.map3d.demo.poisearch.PoiAroundSearchActivity
import com.amap.map3d.demo.poisearch.PoiIDSearchActivity
import com.amap.map3d.demo.poisearch.PoiKeywordSearchActivity
import com.amap.map3d.demo.poisearch.SubPoiSearchActivity
import com.amap.map3d.demo.route.BusRouteActivity
import com.amap.map3d.demo.route.DriveRouteActivity
import com.amap.map3d.demo.route.RideRouteActivity
import com.amap.map3d.demo.route.RouteActivity
import com.amap.map3d.demo.route.WalkRouteActivity
import com.amap.map3d.demo.routepoi.RoutePOIActivity
import com.amap.map3d.demo.share.ShareActivity
import com.amap.map3d.demo.smooth.SmoothMoveActivity
import com.amap.map3d.demo.tools.CalculateDistanceActivity
import com.amap.map3d.demo.tools.ContainsActivity
import com.amap.map3d.demo.tools.CoordConverActivity
import com.amap.map3d.demo.tools.GeoToScreenActivity
import com.amap.map3d.demo.trace.TraceActivity
import com.amap.map3d.demo.trace.TraceReloadActivity
import com.amap.map3d.demo.util.AMapUtil
import com.amap.map3d.demo.view.FeatureView
import com.amap.map3d.demo.weather.WeatherSearchActivity

/**
 * AMapV2地图demo总汇
 */
class MainActivity : ListActivity() {
    private class DemoDetails(val titleId: Int, val descriptionId: Int,
                              val activityClass: Class<out android.app.Activity>?)

    private class CustomArrayAdapter(context: Context, demos: Array<DemoDetails>) : ArrayAdapter<DemoDetails>(context, R.layout.feature, R.id.title, demos) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val featureView: FeatureView
            if (convertView is FeatureView) {
                featureView = convertView
            } else {
                featureView = FeatureView(context)
            }
            val demo = getItem(position)
            featureView.setTitleId(demo!!.titleId, demo.activityClass != null)
            return featureView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        title = "3D地图Demo" + MapsInitializer.getVersion()
        val adapter = CustomArrayAdapter(
                this.applicationContext, demos)
        listAdapter = adapter

        //获取基站信息
        AMapUtil.getTowerInfo(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val demo = listAdapter.getItem(position) as DemoDetails
        if (demo.activityClass != null) {
            Log.i("MY", "demo!=null")
            startActivity(Intent(this.applicationContext,
                    demo.activityClass))
        }

    }

    companion object {

        private val demos = arrayOf(
                //		            创建地图
                DemoDetails(R.string.map_create, R.string.blank, null),
                //			显示地图
                DemoDetails(R.string.basic_map, R.string.basic_description,
                        BasicMapActivity::class.java),
                //			显示地图
                DemoDetails(R.string.basic_camera_change, R.string.blank,
                        CameraChangeActivity::class.java),

                //			6种实现地图方式
                DemoDetails(R.string.basic_map_6, R.string.basic_description_temp,
                        MapImpMethodActivity::class.java),
                //			Fragment创建地图
                //			new DemoDetails(R.string.base_fragment_map, R.string.base_fragment_description,
                //					BaseMapSupportFragmentActivity.class),
                //			new DemoDetails(R.string.basic_texturemapview, R.string.basic_texturemapview_description,
                //					TextureMapViewActivity.class),
                DemoDetails(R.string.viewpager_map, R.string.viewpger_map_description,
                        ViewPagerWithMapActivity::class.java),
                //			地图多实例
                DemoDetails(R.string.multi_inst, R.string.blank,
                        TwoMapActivity::class.java),
                //		           室内地图
                DemoDetails(R.string.indoormap_demo, R.string.indoormap_description,
                        IndoorMapActivity::class.java),
                //		    amapoptions实现地图
                DemoDetails(R.string.mapOption_demo,
                        R.string.mapOption_description, MapOptionActivity::class.java),
                //-----------与地图交互-----------------------------------------------------------------------------------------------
                DemoDetails(R.string.map_interactive, R.string.blank, null),
                //缩放控件、定位按钮、指南针、比例尺等的添加
                DemoDetails(R.string.uisettings_demo,
                        R.string.uisettings_description, UiSettingsActivity::class.java),
                //地图logo位置改变
                DemoDetails(R.string.logo,
                        R.string.uisettings_description, LogoSettingsActivity::class.java),
                //地图图层
                DemoDetails(R.string.layers_demo, R.string.layers_description,
                        LayersActivity::class.java),
                //缩放、旋转、拖拽和改变仰角操作地图
                DemoDetails(R.string.gesture,
                        R.string.uisettings_description, GestureSettingsActivity::class.java),
                //监听点击、长按、拖拽地图等事件
                DemoDetails(R.string.events_demo, R.string.events_description,
                        EventsActivity::class.java),
                //地图POI点击
                DemoDetails(R.string.poiclick_demo,
                        R.string.poiclick_description, PoiClickActivity::class.java),
                //改变地图中心点
                DemoDetails(R.string.camera_demo, R.string.camera_description,
                        CameraActivity::class.java),
                //地图动画效果
                DemoDetails(R.string.animate_demo, R.string.animate_description,
                        Animate_CameraActivity::class.java),
                //改变缩放级别
                DemoDetails(R.string.map_zoom, R.string.blank, ZoomActivity::class.java),

                //地图截屏
                DemoDetails(R.string.screenshot_demo,
                        R.string.screenshot_description, ScreenShotActivity::class.java),

                //自定义最小最大缩放级别
                DemoDetails(R.string.set_min_max_zoomlevel,
                        R.string.set_min_max_zoomlevel_description, MinMaxZoomLevelActivity::class.java),

                //自定义地图显示区域
                DemoDetails(R.string.limit_bounds,
                        R.string.limit_bounds_description, LimitBoundsActivity::class.java),
                //----------------------------------------------------------------------------------------------------------------------------------------
                //在地图上绘制
                DemoDetails(R.string.map_overlay, R.string.blank, null),
                //绘制点
                DemoDetails(R.string.marker_demo, R.string.marker_description,
                        MarkerActivity::class.java),
                //marker点击回调
                DemoDetails(R.string.marker_click, R.string.marker_click,
                        MarkerClickActivity::class.java),
                //Marker 动画功能
                DemoDetails(R.string.marker_animation_demo, R.string.marker_animation_description,
                        MarkerAnimationActivity::class.java),
                //绘制地图上的信息窗口
                DemoDetails(R.string.infowindow_demo, R.string.infowindow_demo, InfoWindowActivity::class.java),
                //绘制自定义点
                DemoDetails(R.string.custommarker_demo, R.string.blank,
                        CustomMarkerActivity::class.java),
                //绘制默认定位小蓝点
                DemoDetails(R.string.locationmodesource_demo, R.string.locationmodesource_description, LocationModeSourceActivity::class.java),
                //绘制自定义定位小蓝点图标
                DemoDetails(R.string.customlocation_demo, R.string.customlocation_demo, CustomLocationActivity::class.java),
                //绘制实线、虚线
                DemoDetails(R.string.polyline_demo,
                        R.string.polyline_description, PolylineActivity::class.java),
                //多彩线
                DemoDetails(R.string.colourline_demo,
                        R.string.colourline_description, ColourfulPolylineActivity::class.java),
                //大地曲线
                DemoDetails(R.string.geodesic_demo, R.string.geodesic_description,
                        GeodesicActivity::class.java),
                //			绘制弧线
                DemoDetails(R.string.arc_demo, R.string.arc_description,
                        ArcActivity::class.java),
                //绘制带导航箭头的线
                DemoDetails(R.string.navigatearrow_demo,
                        R.string.navigatearrow_description,
                        NavigateArrowOverlayActivity::class.java),
                //绘制圆
                DemoDetails(R.string.circle_demo, R.string.circle_description,
                        CircleActivity::class.java),
                //矩形、多边形
                DemoDetails(R.string.polygon_demo,
                        R.string.polygon_description, PolygonActivity::class.java),
                //绘制热力图
                DemoDetails(R.string.heatmap_demo,
                        R.string.heatmap_description, HeatMapActivity::class.java),
                //绘制groundoverlay
                DemoDetails(R.string.groundoverlay_demo,
                        R.string.groundoverlay_description, GroundOverlayActivity::class.java),
                //绘制opengl
                DemoDetails(R.string.opengl_demo, R.string.opengl_description,
                        OpenglActivity::class.java),
                //绘制tileOverlay
                DemoDetails(R.string.tileoverlay_demo, R.string.tileoverlay_demo,
                        TileOverlayActivity::class.java),
                //-----------------------------------------------------------------------------------------------------------------------------------------------------
                //获取地图数据
                DemoDetails(R.string.search_data, R.string.blank, null),
                //关键字检索
                DemoDetails(R.string.poikeywordsearch_demo,
                        R.string.poikeywordsearch_description,
                        PoiKeywordSearchActivity::class.java),
                //周边搜索
                DemoDetails(R.string.poiaroundsearch_demo,
                        R.string.poiaroundsearch_description,
                        PoiAroundSearchActivity::class.java),
                //			ID检索
                DemoDetails(R.string.poiidsearch_demo,
                        R.string.poiidsearch_demo,
                        PoiIDSearchActivity::class.java),
                //沿途搜索
                DemoDetails(R.string.routepoisearch_demo,
                        R.string.routepoisearch_demo,
                        RoutePOIActivity::class.java),
                //			输入提示查询
                DemoDetails(R.string.inputtips_demo, R.string.inputtips_description,
                        InputtipsActivity::class.java),
                //			POI父子关系
                DemoDetails(R.string.subpoi_demo, R.string.subpoi_description,
                        SubPoiSearchActivity::class.java),
                //			天气查询
                DemoDetails(R.string.weather_demo,
                        R.string.weather_description, WeatherSearchActivity::class.java),
                //			地理编码
                DemoDetails(R.string.geocoder_demo,
                        R.string.geocoder_description, GeocoderActivity::class.java),
                //			逆地理编码
                DemoDetails(R.string.regeocoder_demo,
                        R.string.regeocoder_description, ReGeocoderActivity::class.java),
                //			行政区划查询
                DemoDetails(R.string.district_demo,
                        R.string.district_description, DistrictActivity::class.java),
                //			行政区边界查询
                DemoDetails(R.string.district_boundary_demo,
                        R.string.district_boundary_description,
                        DistrictWithBoundaryActivity::class.java),
                //			公交路线查询
                DemoDetails(R.string.busline_demo,
                        R.string.busline_description, BuslineActivity::class.java),
                //			公交站点查询
                DemoDetails(R.string.busstation_demo,
                        R.string.blank, BusStationActivity::class.java),
                //			云图
                DemoDetails(R.string.cloud_demo, R.string.cloud_description,
                        CloudActivity::class.java),
                //			出行路线规划
                DemoDetails(R.string.search_route, R.string.blank, null),
                //			驾车出行路线规划
                DemoDetails(R.string.route_drive, R.string.blank, DriveRouteActivity::class.java),
                //			步行出行路线规划
                DemoDetails(R.string.route_walk, R.string.blank, WalkRouteActivity::class.java),
                //			公交出行路线规划
                DemoDetails(R.string.route_bus, R.string.blank, BusRouteActivity::class.java),
                //			骑行出行路线规划
                DemoDetails(R.string.route_ride, R.string.blank, RideRouteActivity::class.java),
                //			route综合demo
                DemoDetails(R.string.route_demo, R.string.route_description,
                        RouteActivity::class.java),
                //			短串分享
                DemoDetails(R.string.search_share, R.string.blank, null), DemoDetails(R.string.share_demo, R.string.share_description,
                ShareActivity::class.java),

                //			离线地图
                DemoDetails(R.string.map_offline, R.string.blank, null), DemoDetails(R.string.offlinemap_demo,
                R.string.offlinemap_description, OfflineMapActivity::class.java),

                //			地图计算工具
                DemoDetails(R.string.map_tools, R.string.blank, null),

                //			其他坐标系转换为高德坐标系
                DemoDetails(R.string.coordconvert_demo, R.string.coordconvert_demo, CoordConverActivity::class.java),
                //			地理坐标和屏幕像素坐标转换
                DemoDetails(R.string.convertgeo2point_demo, R.string.convertgeo2point_demo, GeoToScreenActivity::class.java),
                //			两点间距离计算
                DemoDetails(R.string.calculateLineDistance, R.string.calculateLineDistance, CalculateDistanceActivity::class.java),
                //			判断点是否在多边形内
                DemoDetails(R.string.contains_demo, R.string.contains_demo, ContainsActivity::class.java),


                //			地图计算工具
                DemoDetails(R.string.map_expand, R.string.blank, null),

                //轨迹回放
                DemoDetails(R.string.trace_reload_demo, R.string.trace_reload_demo, TraceReloadActivity::class.java),

                //			轨迹纠偏
                DemoDetails(R.string.trace_demo, R.string.trace_description, TraceActivity::class.java),
                //			平滑移动
                DemoDetails(R.string.smooth_move_demo, R.string.smooth_move_description, SmoothMoveActivity::class.java))
    }
}
