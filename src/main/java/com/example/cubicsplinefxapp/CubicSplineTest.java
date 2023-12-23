package com.example.cubicsplinefxapp;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CubicSplineTest {


    @Test
    public void initShuttleMatrix() {
        CubicSpline cubicSpline = new CubicSpline();
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


        double[][] expected = new double[][]{{6, 2, 0, -6},
                {2, 10, 3, 6},};

        double[][] actual = cubicSpline.initShuttleMatrix();


        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void getSplineCoeffs() {
        CubicSpline cubicSpline = new CubicSpline();
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


        int[][] expected = new int[][]{ {2, 3, 1},
                {1, 0, 0},
                {0, -1, 0},
                {0, 0, 0}};


        int[][] actual = new int[4][3];


        double[][] shuttleMatrix;
        shuttleMatrix = cubicSpline.initShuttleMatrix();

        List <Double> [] auxList = cubicSpline.getSplineCoeffs(xList, yList, shuttleMatrix);


        for (int y = 0; y < auxList.length; y++) {
            for (int x = 0; x < auxList[0].size(); x++) {
                actual[y][x] = (int) Double.parseDouble(auxList[y].get(x).toString());
            }
        }

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void findAlphas() {
        CubicSpline cubicSpline = new CubicSpline();
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

        double[] expected = new double[]{-1/3.0, 0};

        List <Double> aux = cubicSpline.findAlphas(cubicSpline.initShuttleMatrix());

        double []actual = new double[2];

        for (int i = 0; i < aux.size(); i++) {
            actual[i] = aux.get(i);
        }

        Assert.assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void findBetas (){
        CubicSpline cubicSpline = new CubicSpline();
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

        double[] expected = new double[]{-1.0, 6/7.0};

        List <Double> aux = cubicSpline.findBetas(cubicSpline.initShuttleMatrix());

        double []actual = new double[2];

        for (int i = 0; i < aux.size(); i++) {
            actual[i] = aux.get(i);
        }
        Assert.assertArrayEquals(expected, actual, 0);

    }
}