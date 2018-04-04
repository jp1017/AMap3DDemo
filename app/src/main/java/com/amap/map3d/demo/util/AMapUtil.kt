/**
 *
 */
package com.amap.map3d.demo.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

import android.content.Context
import android.telephony.CellIdentityCdma
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityWcdma
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrengthCdma
import android.telephony.CellSignalStrengthGsm
import android.telephony.CellSignalStrengthLte
import android.telephony.CellSignalStrengthWcdma
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.EditText

import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.BusPath
import com.amap.api.services.route.BusStep
import com.amap.api.services.route.RouteBusLineItem
import com.amap.api.services.route.RouteRailwayItem
import com.amap.map3d.demo.R

object AMapUtil {

    val HtmlBlack = "#000000"
    val HtmlGray = "#808080"

    //获取基站信息: https://www.jianshu.com/p/11fcb8bbedcc
    fun getTowerInfo(context: Context): List<String>? {
        var mcc = -1
        var mnc = -1
        var lac = -1
        var cellId = -1
        var rssi = -1
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = tm.networkOperator
        mcc = Integer.parseInt(operator.substring(0, 3))
        Log.w("基站", "$mcc, operator: $operator")

        var list: MutableList<String> = ArrayList()
        val infos = tm.allCellInfo ?: return null

        for (info in infos) {
            if (info is CellInfoCdma) {
                val cellIdentityCdma = info.cellIdentity
                mnc = cellIdentityCdma.systemId
                lac = cellIdentityCdma.networkId
                cellId = cellIdentityCdma.basestationId
                val cellSignalStrengthCdma = info.cellSignalStrength
                rssi = cellSignalStrengthCdma.cdmaDbm
            } else if (info is CellInfoGsm) {
                val cellIdentityGsm = info.cellIdentity
                mnc = cellIdentityGsm.mnc
                lac = cellIdentityGsm.lac
                cellId = cellIdentityGsm.cid
                val cellSignalStrengthGsm = info.cellSignalStrength
                rssi = cellSignalStrengthGsm.dbm
            } else if (info is CellInfoLte) {
                val cellIdentityLte = info.cellIdentity
                mnc = cellIdentityLte.mnc
                lac = cellIdentityLte.tac
                cellId = cellIdentityLte.ci
                val cellSignalStrengthLte = info.cellSignalStrength
                rssi = cellSignalStrengthLte.dbm
            } else if (info is CellInfoWcdma) {
                var cellIdentityWcdma: CellIdentityWcdma? = null
                var cellSignalStrengthWcdma: CellSignalStrengthWcdma? = null
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    cellIdentityWcdma = info.cellIdentity
                    mnc = cellIdentityWcdma!!.mnc
                    lac = cellIdentityWcdma.lac
                    cellId = cellIdentityWcdma.cid
                    cellSignalStrengthWcdma = info.cellSignalStrength
                    rssi = cellSignalStrengthWcdma!!.dbm
                }
            } else {
                Log.e("获取基站失败", "get CellInfo error")
                return null
            }
            val tower = (mcc.toString() + "#" + mnc.toString() + "#" + lac.toString()
                    + "#" + cellId.toString() + "#" + rssi.toString())

            Log.w("基站", tower)

            list.add(tower)
        }
        if (list.size > 6) {
            list = list.subList(0, 5)
        } else if (list.size < 3) {
            val need = 3 - list.size
            for (i in 0 until need) {
                list.add("")
            }
        }
        return list

    }

    /**
     * 判断edittext是否null
     */
    fun checkEditText(editText: EditText?): String {
        return if (editText != null && editText.text != null
                && editText.text.toString().trim { it <= ' ' } != "") {
            editText.text.toString().trim { it <= ' ' }
        } else {
            ""
        }
    }

    fun stringToSpan(src: String?): Spanned? {
        return if (src == null) null else Html.fromHtml(src.replace("\n", "<br />"))
    }

    fun colorFont(src: String, color: String): String {
        val strBuf = StringBuffer()

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>")
        return strBuf.toString()
    }

    fun makeHtmlNewLine(): String {
        return "<br />"
    }

    fun makeHtmlSpace(number: Int): String {
        val space = "&nbsp;"
        val result = StringBuilder()
        for (i in 0 until number) {
            result.append(space)
        }
        return result.toString()
    }

    fun getFriendlyLength(lenMeter: Int): String {
        if (lenMeter > 10000)
        // 10 km
        {
            val dis = lenMeter / 1000
            return dis.toString() + ChString.Kilometer
        }

        if (lenMeter > 1000) {
            val dis = lenMeter.toFloat() / 1000
            val fnum = DecimalFormat("##0.0")
            val dstr = fnum.format(dis.toDouble())
            return dstr + ChString.Kilometer
        }

        if (lenMeter > 100) {
            val dis = lenMeter / 50 * 50
            return dis.toString() + ChString.Meter
        }

        var dis = lenMeter / 10 * 10
        if (dis == 0) {
            dis = 10
        }

        return dis.toString() + ChString.Meter
    }

    fun IsEmptyOrNullString(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.length == 0
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    fun convertToLatLonPoint(latlon: LatLng): LatLonPoint {
        return LatLonPoint(latlon.latitude, latlon.longitude)
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    fun convertToLatLng(latLonPoint: LatLonPoint): LatLng {
        return LatLng(latLonPoint.latitude, latLonPoint.longitude)
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    fun convertArrList(shapes: List<LatLonPoint>): ArrayList<LatLng> {
        val lineShapes = ArrayList<LatLng>()
        for (point in shapes) {
            val latLngTemp = AMapUtil.convertToLatLng(point)
            lineShapes.add(latLngTemp)
        }
        return lineShapes
    }

    /**
     * long类型时间格式化
     */
    fun convertToTime(time: Long): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(time)
        return df.format(date)
    }

    fun getFriendlyTime(second: Int): String {
        if (second > 3600) {
            val hour = second / 3600
            val miniate = second % 3600 / 60
            return hour.toString() + "小时" + miniate + "分钟"
        }
        if (second >= 60) {
            val miniate = second / 60
            return miniate.toString() + "分钟"
        }
        return second.toString() + "秒"
    }

    //路径规划方向指示和图片对应
    fun getDriveActionID(actionName: String?): Int {
        if (actionName == null || actionName == "") {
            return R.drawable.dir3
        }
        if ("左转" == actionName) {
            return R.drawable.dir2
        }
        if ("右转" == actionName) {
            return R.drawable.dir1
        }
        if ("向左前方行驶" == actionName || "靠左" == actionName) {
            return R.drawable.dir6
        }
        if ("向右前方行驶" == actionName || "靠右" == actionName) {
            return R.drawable.dir5
        }
        if ("向左后方行驶" == actionName || "左转调头" == actionName) {
            return R.drawable.dir7
        }
        if ("向右后方行驶" == actionName) {
            return R.drawable.dir8
        }
        if ("直行" == actionName) {
            return R.drawable.dir3
        }
        return if ("减速行驶" == actionName) {
            R.drawable.dir4
        } else R.drawable.dir3
    }

    fun getWalkActionID(actionName: String?): Int {
        if (actionName == null || actionName == "") {
            return R.drawable.dir13
        }
        if ("左转" == actionName) {
            return R.drawable.dir2
        }
        if ("右转" == actionName) {
            return R.drawable.dir1
        }
        if ("向左前方" == actionName || "靠左" == actionName || actionName.contains("向左前方")) {
            return R.drawable.dir6
        }
        if ("向右前方" == actionName || "靠右" == actionName || actionName.contains("向右前方")) {
            return R.drawable.dir5
        }
        if ("向左后方" == actionName || actionName.contains("向左后方")) {
            return R.drawable.dir7
        }
        if ("向右后方" == actionName || actionName.contains("向右后方")) {
            return R.drawable.dir8
        }
        if ("直行" == actionName) {
            return R.drawable.dir3
        }
        if ("通过人行横道" == actionName) {
            return R.drawable.dir9
        }
        if ("通过过街天桥" == actionName) {
            return R.drawable.dir11
        }
        return if ("通过地下通道" == actionName) {
            R.drawable.dir10
        } else R.drawable.dir13

    }

    fun getBusPathTitle(busPath: BusPath?): String {
        if (busPath == null) {
            return ""
        }
        val busSetps = busPath.steps ?: return ""
        val sb = StringBuffer()
        for (busStep in busSetps) {
            val title = StringBuffer()
            if (busStep.busLines.size > 0) {
                for (busline in busStep.busLines) {
                    if (busline == null) {
                        continue
                    }

                    val buslineName = getSimpleBusLineName(busline.busLineName)
                    title.append(buslineName)
                    title.append(" / ")
                }
                //					RouteBusLineItem busline = busStep.getBusLines().get(0);

                sb.append(title.substring(0, title.length - 3))
                sb.append(" > ")
            }
            if (busStep.railway != null) {
                val railway = busStep.railway
                sb.append(railway.trip + "(" + railway.departurestop.name
                        + " - " + railway.arrivalstop.name + ")")
                sb.append(" > ")
            }
        }
        return sb.substring(0, sb.length - 3)
    }

    fun getBusPathDes(busPath: BusPath?): String {
        if (busPath == null) {
            return ""
        }
        val second = busPath.duration
        val time = getFriendlyTime(second.toInt())
        val subDistance = busPath.distance
        val subDis = getFriendlyLength(subDistance.toInt())
        val walkDistance = busPath.walkDistance
        val walkDis = getFriendlyLength(walkDistance.toInt())
        return "$time | $subDis | 步行$walkDis"
    }

    fun getSimpleBusLineName(busLineName: String?): String {
        return busLineName?.replace("\\(.*?\\)".toRegex(), "") ?: ""
    }


}
