package com.example.cubicsplinefxapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CubicSpline2D {

    public List[] initSplineForAxis(List<Double> xList, List<Double> yList, String func) {
        CubicSpline cubicSpline;
        List<Double> tList = calculateT(xList, yList);
        if (Objects.equals(func, "y")) {
            cubicSpline = new CubicSpline(tList, yList);
        } else {
            cubicSpline = new CubicSpline(tList, xList);
        }
        double[][] shuttleMatrix = cubicSpline.initShuttleMatrix();

        return cubicSpline.getSplineCoeffs(cubicSpline.getxList(), cubicSpline.getyList(), shuttleMatrix);

    }

    protected List<Double> calculateT(List<Double> x, List<Double> y) {

        int size = x.size();
        double dx, dy;

        List<Double> dts = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            dx = x.get(i + 1) - x.get(i);
            dy = y.get(i + 1) - y.get(i);
            dts.add(Math.sqrt(dx * dx + dy * dy));
        }
        List<Double> tS = new ArrayList<>();
        tS.add(0, 0.0);
        for (int i = 1; i < dts.size() + 1; i++) {
            tS.add(i, tS.get(i - 1) + dts.get(i - 1));
        }
        return tS;
    }
}
