package com.amap.map3d.demo.overlay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.map3d.demo.R;
import com.amap.map3d.demo.util.Constants;
import com.amap.map3d.demo.util.ToastUtil;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class CustomMarkerActivity extends Activity implements OnMarkerClickListener,
		OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,
		OnClickListener, InfoWindowAdapter {
	private MarkerOptions markerOption;
	private TextView markerText;
	private RadioGroup radioOption;
	private AMap aMap;
	private MapView mapView;
	private Marker marker2;// 有跳动效果的marker对象
	private Marker marker3;// 从地上生长的marker对象
	private LatLng latlng = new LatLng(36.061, 103.834);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custommarker_activity);
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		markerText = (TextView) findViewById(R.id.mark_listenter_text);
		radioOption = (RadioGroup) findViewById(R.id.custom_info_window_options);
		Button clearMap = (Button) findViewById(R.id.clearMap);
		clearMap.setOnClickListener(this);
		Button resetMap = (Button) findViewById(R.id.resetMap);
		resetMap.setOnClickListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker
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

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		// 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度
		TextOptions textOptions = new TextOptions()
				.position(Constants.BEIJING)
				.text("Text")
				.fontColor(Color.BLACK)
				.backgroundColor(Color.BLUE)
				.fontSize(30)
				.rotate(20)
				.align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
				.zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
		aMap.addText(textOptions);

		Marker marker = aMap.addMarker(new MarkerOptions()

				.title("好好学习")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		marker.setRotateAngle(90);// 设置marker旋转90度
		marker.setPositionByPixels(400, 400);
		marker.showInfoWindow();// 设置默认显示一个infowinfow

		markerOption = new MarkerOptions();
		markerOption.position(Constants.XIAN);
		markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

		markerOption.draggable(true);
		markerOption.icon(
		// BitmapDescriptorFactory
		// .fromResource(R.drawable.location_marker)
				BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);

		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
				.position(Constants.CHENGDU).title("成都市")
				.snippet("成都市:30.679879, 104.064855").icons(giflist)
				.draggable(true).period(10);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		markerOptionlst.add(markerOption1);
		List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
		marker2 = markerlst.get(0);

		marker3 = aMap.addMarker(new MarkerOptions().position(
				Constants.ZHENGZHOU).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (aMap != null) {
			if (marker.equals(marker2)) {
				jumpPoint(marker);
			} else if (marker.equals(marker3)) {
				growInto(marker);
			}

		}
		markerText.setText("你点击的是" + marker.getTitle());

		return false;
	}

	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(Constants.XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * Constants.XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * Constants.XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	/**
	 * marker 必须有设置图标，否则无效果
	 * 
	 * @param marker
	 */
	private void dropInto(final Marker marker) {

		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng markerLatlng = marker.getPosition();
		Projection proj = aMap.getProjection();
		Point markerPoint = proj.toScreenLocation(markerLatlng);
		Point startPoint = new Point(markerPoint.x, 0);// 从marker的屏幕上方下落
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 800;// 动画总时长

		final Interpolator interpolator = new AccelerateInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * markerLatlng.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * markerLatlng.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	private int count = 1;
	Bitmap lastMarkerBitMap = null;

	/**
	 * 从地上生长效果，实现思路
	 * 在较短的时间内，修改marker的图标大小，从而实现动画<br>
	 * 1.保存原始的图片；
	 * 2.在原始图片上缩放得到新的图片，并设置给marker；
	 * 3.回收上一张缩放后的图片资源；
	 * 4.重复2，3步骤到时间结束；
	 * 5.回收上一张缩放后的图片资源，设置marker的图标为最原始的图片；
	 * 
	 * 其中时间变化由AccelerateInterpolator控制
	 * @param marker
	 */
	private void growInto(final Marker marker) {
		marker.setVisible(false);
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final long duration = 250;// 动画总时长
		final Bitmap bitMap = marker.getIcons().get(0).getBitmap();// BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
		final int width = bitMap.getWidth();
		final int height = bitMap.getHeight();

		final Interpolator interpolator = new AccelerateInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);

				if (t > 1) {
					t = 1;
				}

				// 计算缩放比例
				int scaleWidth = (int) (t * width);
				int scaleHeight = (int) (t * height);
				if (scaleWidth > 0 && scaleHeight > 0) {

					// 使用最原始的图片进行大小计算
					marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap
							.createScaledBitmap(bitMap, scaleWidth,
									scaleHeight, true)));
					marker.setVisible(true);

					// 因为替换了新的图片，所以把旧的图片销毁掉，注意在设置新的图片之后再销毁
					if (lastMarkerBitMap != null
							&& !lastMarkerBitMap.isRecycled()) {
						lastMarkerBitMap.recycle();
					}
					
					//第一次得到的缩放图片，在第二次回收，最后一次的缩放图片，在动画结束时回收
					ArrayList<BitmapDescriptor> list = marker.getIcons();
					if (list != null && list.size() > 0) {
						// 保存旧的图片
						lastMarkerBitMap = marker.getIcons().get(0).getBitmap();
					}

				}

				if (t < 1.0 && count < 10) {
					handler.postDelayed(this, 16);
				} else {
					// 动画结束回收缩放图片，并还原最原始的图片
					if (lastMarkerBitMap != null
							&& !lastMarkerBitMap.isRecycled()) {
						lastMarkerBitMap.recycle();
					}
					marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitMap));
					marker.setVisible(true);
				}
			}
		});
	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		ToastUtil.show(this, "你点击了infoWindow窗口" + marker.getTitle());
		ToastUtil.show(CustomMarkerActivity.this, "当前地图可视区域内Marker数量:"
				+ aMap.getMapScreenMarkers().size());
	}

	/**
	 * 监听拖动marker时事件回调
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
				+ marker.getPosition().latitude + ","
				+ marker.getPosition().longitude + ")";
		markerText.setText(curDes);
	}

	/**
	 * 监听拖动marker结束事件回调
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {
		markerText.setText(marker.getTitle() + "停止拖动");
	}

	/**
	 * 监听开始拖动marker事件回调
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
		markerText.setText(marker.getTitle() + "开始拖动");
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(Constants.XIAN).include(Constants.CHENGDU)
				.include(Constants.BEIJING).include(latlng).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
			return null;
		}
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
			return null;
		}
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
			((ImageView) view.findViewById(R.id.badge))
					.setImageResource(R.drawable.badge_sa);
		} else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
			ImageView imageView = (ImageView) view.findViewById(R.id.badge);
			imageView.setImageResource(R.drawable.badge_wa);
		}
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 清空地图上所有已经标注的marker
		 */
		case R.id.clearMap:
			if (aMap != null) {
				aMap.clear();
			}
			break;
		/**
		 * 重新标注所有的marker
		 */
		case R.id.resetMap:
			if (aMap != null) {
				aMap.clear();
				addMarkersToMap();
			}
			break;
		default:
			break;
		}
	}
}
