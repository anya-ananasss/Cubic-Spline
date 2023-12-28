package com.example.cubicsplinefxapp;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CubicSpline2DTest {
    @Test
    public void calculateT (){
        CubicSpline cubicSpline = new CubicSpline();
        CubicSpline2D cubicSpline2D = new CubicSpline2D();
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        cubicSpline.setxList(xList);
        cubicSpline.setyList(yList);


        xList.add(1.0);
        xList.add(2.0);
        xList.add(4.0);
        xList.add(7.0);


        yList.add(2.0);
        yList.add(3.0);
        yList.add(1.0);
        yList.add(4.0);

        double [] expected = new double [] {0.0, Math.sqrt(2), Math.sqrt(2)+Math.sqrt(8), Math.sqrt(2)+Math.sqrt(8)+Math.sqrt(18)};
        double [] actual = new double[4];
        List <Double> auxList = cubicSpline2D.calculateT(cubicSpline.getxList(), cubicSpline.getyList());
        for (int i = 0; i < 4 ; i++) {
            actual[i]=auxList.get(i);
        }
        Assert.assertArrayEquals(expected, actual, 0);
    }

}