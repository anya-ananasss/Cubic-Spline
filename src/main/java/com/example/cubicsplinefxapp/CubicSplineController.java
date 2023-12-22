package com.example.cubicsplinefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class CubicSplineController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();


    CubicSpline cubicSpline = new CubicSpline();
    CubicSpline xSpline = new CubicSpline();
    CubicSpline ySpline = new CubicSpline();

    ArrayList<Double> xCoordinates = new ArrayList<>();
    ArrayList<Double> yCoordinates = new ArrayList<>();
    ArrayList <Double> tCoordinates = new ArrayList<>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());


        xCoordinates.add(clickPoint.getX());
        yCoordinates.add(clickPoint.getY());

        cubicSpline.setxList(xCoordinates);
        cubicSpline.setyList(yCoordinates);

        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS,
                2 * POINT_RADIUS, 2 * POINT_RADIUS);


        if (points.size() > 0) {
            final Point2D lastPoint = points.get(points.size() - 1);


            if (points.size() == 1) {
                graphicsContext.strokeLine(lastPoint.getX(), lastPoint.getY(),
                        clickPoint.getX(), clickPoint.getY());

            } else {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


                double[][] shuttleMatrix = cubicSpline.initShuttleMatrix();
                int POINTSAMOUNT = cubicSpline.getPointsAmount();



                List[] splineCoeffs = cubicSpline.getSplineCoeffs(cubicSpline.getxList(), cubicSpline.getyList(), shuttleMatrix);

                List<Double> aList = splineCoeffs[0];
                List<Double> bList = splineCoeffs[1];
                List<Double> cList = splineCoeffs[2].subList(0, splineCoeffs[2].size()-1);
                List<Double> dList = splineCoeffs[3];






                double x0, x1, y0, y1;
                for (int i = 0; i < POINTSAMOUNT - 1; i++) {
                    x0 = cubicSpline.getxList().get(i);
                    y0 = cubicSpline.getyList().get(i);
                    x1 = cubicSpline.getxList().get(i + 1);
                    y1 = cubicSpline.getyList().get(i + 1);
                    graphicsContext.fillOval(
                            x0 - POINT_RADIUS, y0 - POINT_RADIUS,
                            2 * POINT_RADIUS, 2 * POINT_RADIUS);

                    graphicsContext.fillOval(
                            x1 - POINT_RADIUS, y1 - POINT_RADIUS,
                            2 * POINT_RADIUS, 2 * POINT_RADIUS);

                    if (x0 < x1) {
                        for (double x = (int) x0; x < x1; x++) {

                            graphicsContext.strokeLine(x, Sx(aList.get(i), bList.get(i), cList.get(i), dList.get(i), x, x0),
                                    x + 1, Sx(aList.get(i), bList.get(i), cList.get(i), dList.get(i), x + 1, x0));


                        }
                    } else  {
                        for (double x = (int) x0; x > x1; x--) {

                            graphicsContext.strokeLine(x, Sx(aList.get(i), bList.get(i), cList.get(i), dList.get(i), x, x0),
                                    x - 1, Sx(aList.get(i), bList.get(i), cList.get(i), dList.get(i), x - 1, x0));


                        }
                    }
                }
            }
        }
        points.add(clickPoint);
    }

    private double Sx(double a, double b, double c, double d, double x, double x0) {
        return (a + b * (x - x0) + c * Math.pow((x - x0), 2.0) + d * Math.pow((x - x0), 3.0));
    }





}