package com.example.cubicsplinefxapp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CubicSplineController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    @FXML
    Button clearCanvasButton = new Button();
    @FXML
    protected void onClearCanvasButtonClick() {
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        this.xCoordinates = new ArrayList<>();
        this.yCoordinates = new ArrayList<>();
        this.points = new ArrayList<>();
    }
    @FXML
    Button closeWindowButton = new Button();
    @FXML
    private void onCloseWindowButtonClick() {
        Stage stage = (Stage) closeWindowButton.getScene().getWindow();
        stage.close();
    }

    ArrayList<Point2D> points = new ArrayList<>();


    CubicSpline cubicSpline = new CubicSpline();


    ArrayList<Double> xCoordinates = new ArrayList<>();
    ArrayList<Double> yCoordinates = new ArrayList<>();


    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handlePrimaryClick(canvas.getGraphicsContext2D(), event);
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

                CubicSpline2D cubicSpline2D = new CubicSpline2D();

                List<Double> axList, bxList, cxList, dxList,
                        ayList, byList, cyList, dyList, xList, yList;

                xList = cubicSpline.getxList();
                yList = cubicSpline.getyList();

                List <Double> [] xSplineCoeffs = cubicSpline2D.initSplineForAxis(xList, yList, "x");
                axList = xSplineCoeffs[0];
                bxList = xSplineCoeffs[1];
                cxList = xSplineCoeffs[2].subList(0, xSplineCoeffs[2].size() - 1);
                dxList = xSplineCoeffs[3];


                List <Double> [] ySplineCoeffs = cubicSpline2D.initSplineForAxis(xList, yList, "y");
                ayList = ySplineCoeffs[0];
                byList = ySplineCoeffs[1];
                cyList = ySplineCoeffs[2].subList(0, ySplineCoeffs[2].size() - 1);
                dyList = ySplineCoeffs[3];


                List<Double> tList = cubicSpline2D.calculateT(xList, yList);


                double xt, yt, prevxt, prevyt;
                prevxt = 0;
                prevyt = 0;
                for (int i = 0; i < tList.size() - 1; i++) {//внешний цикл - идем от t(0) к t (n)
                    for (double t = tList.get(i); t < tList.get(i + 1); t = t + 0.01) {//внутренний цикл - идем от t(i) к t(i+1) с маленьким шагом, на каждом шаге рисуем минилинию
                        xt = S(axList.get(i), bxList.get(i), cxList.get(i), dxList.get(i), t, tList.get(i));
                        yt = S(ayList.get(i), byList.get(i), cyList.get(i), dyList.get(i), t, tList.get(i));
                        if (t == tList.get(i)) {
                            graphicsContext.strokeLine(xt, yt, xt, yt);
                            graphicsContext.fillOval(
                                    xt - POINT_RADIUS, yt - POINT_RADIUS,
                                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
                        } else {
                            graphicsContext.strokeLine(xt, yt, prevxt, prevyt);
                        }
                        prevxt = xt;
                        prevyt = yt;
                    }
                    graphicsContext.fillOval(
                            prevxt - POINT_RADIUS, prevyt - POINT_RADIUS,
                            2 * POINT_RADIUS, 2 * POINT_RADIUS);
                }
            }
        }
        points.add(clickPoint);
    }

    private double S(double a, double b, double c, double d, double x, double x0) {
        return (a + b * (x - x0) + c * Math.pow((x - x0), 2.0) + d * Math.pow((x - x0), 3.0));
    }


}