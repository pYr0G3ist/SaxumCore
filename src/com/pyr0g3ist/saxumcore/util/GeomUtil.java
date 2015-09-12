package com.pyr0g3ist.saxumcore.util;

import java.awt.geom.Point2D;

public class GeomUtil {

    public static Point2D.Double getPointOnRadiusToPoint(
            double centerX, double centerY, int radius, double destinationX, double destinationY) {

        double phi = Math.atan2(destinationY - centerY, destinationX - centerX);
        return new Point2D.Double(
                centerX + radius * Math.cos(phi),
                centerY + radius * Math.sin(phi));
    }

    public static double getDistance(double aX, double aY, double bX, double bY) {
        return Math.abs(Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));
    }

    public static double getDistance(Point2D.Double a, Point2D.Double b) {
        return getDistance(a.x, a.y, b.x, b.y);
    }

}
