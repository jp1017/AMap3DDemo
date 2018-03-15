package com.amap.map3d.demo.trace;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.map3d.demo.R;

import java.util.ArrayList;

/**
 * AMap 轨迹回放demo
 */

public class TraceReloadActivity extends FragmentActivity {
    private static final LatLng marker1 = new LatLng(38.66666, 100.88888888);
    private static final LatLng marker2 = new LatLng(38.66666, 102.88888888);
    private static final LatLng marker3 = new LatLng(38.66666, 103.88888888);
    private static final LatLng marker4 = new LatLng(38.66666, 110.88888888);
    private static final LatLng marker5 = new LatLng(38.66666, 112.88888888);
    private static final LatLng marker6 = new LatLng(37.66666, 100.88888888);
    private static final LatLng marker7 = new LatLng(36.66666, 102.88888888);
    private static final LatLng marker8 = new LatLng(36.66666, 104.88888888);
    private static final LatLng marker9 = new LatLng(34.66666, 108.88888888);
    private static final LatLng marker10 = new LatLng(33.66666, 106.18322);

    private AMap aMap;
    private Button mButton;
    private SeekBar mSeekBar;
    private Marker mMarker = null;
    // 存放所有坐标的数组
    private final ArrayList<LatLng> mLatLngs = new ArrayList<>();
    private final ArrayList<LatLng> mTraceLatLngs = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int pro = mSeekBar.getProgress();
                if (pro != mSeekBar.getMax()) {
                    mSeekBar.setProgress(pro + 1);
                    mHandler.sendEmptyMessageDelayed(1, 800);
                } else {
                    Button button = (Button) findViewById(R.id.btn_replay);
                    button.setText(" 回放 ");// 已执行到最后一个坐标 停止任务
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_reload);

        initAmap();

        float f = 0;
        for (int i = 0; i < mLatLngs.size() - 1; i++) {
            f += AMapUtils.calculateLineDistance(mLatLngs.get(i), mLatLngs.get(i + 1));
        }

    }

    /**
     * 初始化AMap对象
     */
    private void initAmap() {
        mButton = findViewById(R.id.btn_replay);
        mSeekBar = findViewById(R.id.process_bar);
        mSeekBar.setSelected(false);

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mTraceLatLngs.clear();
                if (progress != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        mTraceLatLngs.add(mLatLngs.get(i));
                    }
                    drawLine(mTraceLatLngs, progress);
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mTraceLatLngs.clear();
                int current = seekBar.getProgress();
                if (current != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        mTraceLatLngs.add(mLatLngs.get(i));
                    }
                    drawLine(mTraceLatLngs, current);
                }
            }
        });

        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (aMap != null) {
                setUpMap();
            }
        }
    }

    private void drawLine(ArrayList<LatLng> list,int current) {
        aMap.clear();
        LatLng replayGeoPoint = mLatLngs.get(current - 1);
        if (mMarker != null) {
            mMarker.destroy();
        }

        // 添加车辆位置
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(replayGeoPoint)
                .title("起点")
                .snippet(" ")
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(), R.drawable.car)))
                .anchor(0.5f, 0.5f);
        mMarker = aMap.addMarker(markerOptions);

        // 增加起点位置
        aMap
                .addMarker(new MarkerOptions()
                .position(mLatLngs.get(0))
                .title("起点")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(
                                getResources(),
                                R.drawable.nav_route_result_start_point))));


        if (mTraceLatLngs.size() > 1) {
            PolylineOptions polylineOptions = (new PolylineOptions())
                    .addAll(mTraceLatLngs)
                    .color(Color.GREEN).width(8.0f);
            aMap.addPolyline(polylineOptions);
        }

        if (mTraceLatLngs.size() == mLatLngs.size()) {
            aMap
                    .addMarker(new MarkerOptions()
                    .position(mLatLngs.get(mLatLngs.size() - 1))
                    .title("终点")
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.nav_route_result_end_point))));
        }
    }


    private void setUpMap() {
        // 加入坐标
        mLatLngs.add(marker1);
        mLatLngs.add(marker2);
        mLatLngs.add(marker3);
        mLatLngs.add(marker4);
        mLatLngs.add(marker5);
        mLatLngs.add(marker6);
        mLatLngs.add(marker7);
        mLatLngs.add(marker8);
        mLatLngs.add(marker9);
        mLatLngs.add(marker10);

        // 设置进度条最大长度为数组长度
        mSeekBar.setMax(mLatLngs.size());
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngs.get(0), 4));
    }

    public void btn_replay_click(View v) {
        // 根据按钮上的字判断当前是否在回放
        if (mButton.getText().toString().contains("回放")) {
            if (mLatLngs.size() > 0) {
                // 假如当前已经回放到最后一点 置0
                if (mSeekBar.getProgress() == mSeekBar.getMax()) {
                    mSeekBar.setProgress(0);
                }
                // 将按钮上的字设为"停止" 开始调用定时器回放
                mButton.setText(" 停止 ");
                mHandler.sendEmptyMessage(1);
            }
        } else {
            // 移除定时器的任务
            mHandler.removeMessages(1);
            mButton.setText(" 回放 ");
        }
    }

}