package cn.zhumingwu.influxdb.util;

import cn.zhumingwu.influxdb.model.Box;
import cn.zhumingwu.influxdb.model.Circle;
import cn.zhumingwu.influxdb.model.Polygon;
import com.google.common.geometry.*;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public enum S2Util {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 地球半径：
     * 两极半径 - 从地心到北极或南极的距离，大约6356.9088千米
     * 赤道半径 - 从地心到赤道的距离，大约6377.830千米
     * 平均半径 - 从地心到地球表面所有各点距离的平均值，大约6371.393千米
     */
    private final static double EARTH_RADIUS = 6371393;
    private final static S2RegionCoverer COVERER = new S2RegionCoverer();

    static {
        defaultSetting();
    }

    private static void defaultSetting() {
        COVERER.setMinLevel(10);  //大约10km
        COVERER.setMaxLevel(20);  //大约10m
        COVERER.setMaxCells(100);
    }

    /**
     * 根据region获取cellId列表
     *
     * @param region
     * @return List
     */
    public static List<S2CellId> getCellIdList(S2Region region) {
        return COVERER.getCovering(region).cellIds();
    }


    /**
     * 根据region获取cellId列表
     *
     * @param region
     * @return
     */
    public static List<S2CellId> getCellIdList(S2Region region, int minLevel, int maxLevel, int maxCells) {
        if (minLevel > 0 && minLevel < 31) {
            COVERER.setMinLevel(minLevel);
        }
        if (maxLevel > 0 && minLevel < 31) {
            COVERER.setMaxLevel(maxLevel);
        }
        if (maxCells > 0 && maxCells < 1000) {
            COVERER.setMaxCells(maxCells);
        }
        var result = getCellIdList(region);
        defaultSetting();
        return result;
    }

    public static S2Region getS2RegionByCircle(Circle circle) {
        double capHeight = circle.getRadius() / EARTH_RADIUS;
        S2Cap cap = S2Cap.fromAxisHeight(S2LatLng.fromDegrees(circle.getLat(), circle.getLon()).toPoint(), capHeight * capHeight / 2);
        return cap;
    }

    public static double getDistance(S2LatLng loc1, S2LatLng loc2) {
        double radLat1 = loc1.latDegrees() * S2.M_PI / 180.0;
        double radLat2 = loc2.latDegrees() * S2.M_PI / 180.0;
        double a = (loc1.latDegrees() - loc2.latDegrees()) * S2.M_PI / 180.0;
        double b = (loc1.lngDegrees() - loc2.lngDegrees()) * S2.M_PI / 180.0;
        double s = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return Math.round(s * 10000) / 10000;
    }

    public static S2Region getS2RegionByBox(Box box) {
        List<S2Point> pointList = new LinkedList<>();
        pointList.add(S2LatLng.fromDegrees(box.getMinLat(), box.getMinLon()).toPoint());
        pointList.add(S2LatLng.fromDegrees(box.getMaxLat(), box.getMaxLon()).toPoint());
        return getS2RegionByPoints(pointList);
    }

    public static S2Region getS2RegionByPolygon(Polygon polygon) {
        List<S2Point> pointList = Arrays.stream(polygon.getPoints()).map(location -> {
            return S2LatLng.fromDegrees(location.getLat(), location.getLon()).toPoint();
        }).collect(Collectors.toList());
        return getS2RegionByPoints(pointList);
    }

    private static S2Region getS2RegionByPoints(List<S2Point> pointList) {
        S2Loop s2Loop = new S2Loop(pointList);
        S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
        builder.addLoop(s2Loop);
        return builder.assemblePolygon();
    }
}

