package com.amap.map3d.demo.location;


import android.app.Activity;
import android.ddm.DdmHandleViewDebug;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.map3d.demo.R;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.mapcore.util.cs.i;

/**
 * AMapV2地图中实时轨迹
 */
public class RealTimeRouteActivity extends Activity implements AMapLocationListener {
	private static final String TAG = RealTimeRouteActivity.class.getName();

	private AMap mAMap;
	private MapView mapView;

	private MyLocationStyle mLocationStyle;

	private Handler mHandler;

	private static boolean isRealTime;
	private double mLat;
	private double mLng;
	private double tmpLat;
	private double tmpLng;
	private List<LatLng> mLatLngs = new ArrayList<>();
	private PolylineOptions mPolylineOptions;
	private Polyline mPolyline;

	private AMapLocationClient mlocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.activity_realtime_route);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();

		initHandler();
		mHandler.sendEmptyMessage(0);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				isRealTime = !isRealTime;

				sendEmptyMessageDelayed(0, 30 * 1000);

			}
		};
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (mAMap == null) {
			mAMap = mapView.getMap();
		}

		//实时轨迹
		mPolylineOptions = new PolylineOptions();
		mPolylineOptions.color(Color.RED).width(6);//此处颜色不能用R.color.red
		mPolyline = mAMap.addPolyline(mPolylineOptions);

		initAMapLocation();

	}

	//高德定位跟随模式
	public static final int AMAP_DEFAULT_LOCATION_TYPE = MyLocationStyle.LOCATION_TYPE_FOLLOW;
	//高德定位经度
	public static final AMapLocationClientOption.AMapLocationMode AMAP_DEFAULT_LOCATION_MODE =
			AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

	/**
	 * 初始化高德定位参数
	 */
	private void initAMapLocation() {
		//自定义定位图标
		mLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标

		mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种, 可动态设置
		mLocationStyle.myLocationType(AMAP_DEFAULT_LOCATION_TYPE);

		// 自定义精度范围的圆形边框颜色
		mLocationStyle.strokeColor(STROKE_COLOR);
		//自定义精度范围的圆形边框宽度
		mLocationStyle.strokeWidth(1);
		// 设置圆形的填充颜色
		mLocationStyle.radiusFillColor(FILL_COLOR);
		// 将自定义的 mLocationStyle 对象添加到地图上
		mAMap.setMyLocationStyle(mLocationStyle);

		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(getApplicationContext());
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置定位参数
			mlocationClient.setLocationOption(getDefaultOption());
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	//围栏外围宽度
	private static final float STROKE_WIDTH = 2;
	private static final float POLY_WIDTH = 6;
	//定位蓝点小边框, 蓝色
	private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	//地理围栏边框, 红色
	private static final int STROKE_COLOR_FENCE = Color.argb(180, 255, 0, 0);
	//填充颜色, 浅蓝
	private static final int FILL_COLOR = Color.argb(50, 0, 0, 180);

	/**
	 * 获取高德定位参数
	 * @return
	 */
	private AMapLocationClientOption getDefaultOption(){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMAP_DEFAULT_LOCATION_MODE);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(1);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(false); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
		return mOption;
	}


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}



	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null) {
			if (aMapLocation.getErrorCode() == 0) {

				//打印定位数据
				Log.w(TAG, "获取到经纬度数据: 来自高德: " + isRealTime);

				//获取当前位置信息
				mLat = aMapLocation.getLatitude();
				mLng = aMapLocation.getLongitude();

				//重车时实时轨迹
				if (isRealTime) {
					Log.w(TAG, "实时轨迹: begin");
					//实时绘制轨迹
					//经纬度不一样时, 再添加进去
					if (Math.abs(tmpLat - mLat) > 1e-6 || Math.abs(tmpLng - mLng) > 1e-6) {
						Log.w(TAG, "实时轨迹: 开始添加—— tmplat: " + tmpLat + ", mLat: " + mLat);
						tmpLat = mLat;
						tmpLng = mLng;
						mLatLngs.add(new LatLng(mLat, mLng));
						mPolyline.setPoints(mLatLngs);
					}
				} else {
					Log.w(TAG, "实时轨迹: clear");
					tmpLat = 0;
					tmpLng = 0;
					if (mLatLngs.size() > 0) {
						Log.w(TAG, "实时轨迹: 已成功 clear");
						mLatLngs.clear();
                        //不能用remove, 否则就不显示了.
                        mPolyline.setPoints(mLatLngs);

                    }
				}

			}

		}
	}
}
