package com.example.cubicsplinefxapp;

import java.util.*;

public class CubicSpline {
    private List<Double> xList;
    private List<Double> yList;

    public CubicSpline() {
        this.xList = new ArrayList<>();
        this.yList = new ArrayList<>();
    }

    public CubicSpline(List<Double> argsList, List<Double> funcList) {
        this.xList = argsList;
        this.yList = funcList;
    }

    public void setxList(List<Double> xList) {
        this.xList = xList;
    }

    public void setyList(List<Double> yList) {
        this.yList = yList;
    }

    public List<Double> getxList() {
        return xList;
    }

    public List<Double> getyList() {
        return yList;
    }

    public double[][] initShuttleMatrix() {
        List<Double> x = this.xList;
        List<Double> y = this.yList;
        int unkVarsAmount = x.size() - 2;
        double[][] matrix = new double[unkVarsAmount][unkVarsAmount + 2];
        int shiftParam = -1;
        double[] argsArray;
        for (int line = 0; line < unkVarsAmount; line++) {
            int column = 0;
            argsArray = coeffsForShuttleMatrix(x.get(line), x.get(line + 1), x.get(line + 2), y.get(line), y.get(line + 1), y.get(line + 2));

            if (line == 0) {
                matrix[line][column] = argsArray[0];
                column++;
                matrix[line][column] = argsArray[1];
            } else {
                matrix[line][column + shiftParam] = argsArray[2];
                column++;
                matrix[line][column + shiftParam] = argsArray[0];
                if (line < unkVarsAmount - 1 || matrix.length < 3) { //???
                    column++;
                    matrix[line][column + shiftParam] = argsArray[1];
                }
            }
            matrix[line][unkVarsAmount + 1] = argsArray[3];
            shiftParam++;

        }
        return matrix;
    }


    protected List<Double> findAlphas(double[][] matrix) {
        int n = 0;
        double a, b;
        a = matrix[0][0];
        b = matrix[0][1];
        List<Double> alphas = new ArrayList<>();
        alphas.add(-b / a);
        if (matrix.length > 1) {
            calculateCurrAlpha(n + 1, alphas, matrix);
        }
        return alphas;
    }

    private void calculateCurrAlpha(int currLine, List<Double> alphas, double[][] matrix) {
        //объявляем коэффициенты
        double a, b, c;
        a = matrix[currLine][currLine];
        if (currLine == matrix.length - 1) {
            b = 0;
        } else {
            b = matrix[currLine][currLine + 1];
        }

        c = matrix[currLine][currLine - 1];

        //

        alphas.add(currLine, (-b) / (c * alphas.get(currLine - 1) + a));
        if (currLine < matrix.length - 1) {
            calculateCurrAlpha(currLine + 1, alphas, matrix);
        }
    }


    protected List<Double> findBetas(double[][] matrix) {
        int n = 0;
        double a, d;
        a = matrix[0][0];
        d = matrix[0][matrix[0].length - 1];

        List<Double> alphas = findAlphas(matrix);
        List<Double> betas = new ArrayList<>();

        betas.add(d / a); //пока ищем коэффициенты прогонки, идем сверху вниз; когда находим значения c - снизу вверх
        if (matrix.length > 1) {
            calculateCurrBeta(n + 1, betas, matrix, alphas);
        }
        return betas;
    }

    private void calculateCurrBeta(int currLine, List<Double> betas, double[][] matrix, List<Double> alphas) {
        double a, c, d;
        a = matrix[currLine][currLine];
        d = matrix[currLine][matrix[currLine].length - 1];
        c = matrix[currLine][currLine - 1];


        betas.add(currLine, (d - c * betas.get(currLine - 1)) / (c * alphas.get(currLine - 1) + a));
        if (currLine < matrix.length - 1) {
            calculateCurrBeta(currLine + 1, betas, matrix, alphas);
        }
    }


    private double[] coeffsForShuttleMatrix(double x1, double x2, double x3,
                                            double y1, double y2, double y3) { //1=i; 2=i+1; 3=i+2
        double Ac = 2 * (x3 - x1);
        double Bc = x3 - x2;//нет в последней строке
        double Cc = x2 - x1;
        double Dc = 3 * ((y3 - y2) / (x3 - x2) - (y2 - y1) / (x2 - x1));

        return new double[]{Ac, Bc, Cc, Dc};
    }

    private List<Double> shuttleMethod(double[][] matrix) {
        List<Double> alphas = findAlphas(matrix);
        List<Double> betas = findBetas(matrix);

        List<Double> cList = new ArrayList<>();
        List<Double> invertedCs = new ArrayList<>();

        for (int i = matrix.length + 1; i > -1; i--) {
            if (i == 0 || i == matrix.length + 1) {
                invertedCs.add(0.0);
            } else {
                invertedCs.add(alphas.get(i - 1) * invertedCs.get(matrix.length - i) + betas.get(i - 1));
            }
        }

        for (int i = 0; i < invertedCs.size(); i++) {
            cList.add(i, invertedCs.get(invertedCs.size() - 1 - i));
        }
        return cList;
    }

    public List[] getSplineCoeffs(List<Double> xList, List<Double> yList, double[][] shuttleMatrix) {
        List<Double> cList = shuttleMethod(shuttleMatrix);
        List<Double> aList = new ArrayList<>();
        List<Double> bList = new ArrayList<>();
        List<Double> dList = new ArrayList<>();
        for (int i = 0; i < yList.size() - 1; i++) {
            aList.add(i, yList.get(i));
            bList.add(i, ((yList.get(i + 1) - yList.get(i)) / (xList.get(i + 1) - xList.get(i))) -
                    ((cList.get(i + 1) + 2 * cList.get(i)) * ((xList.get(i + 1) - xList.get(i)) / 3.0)));
            dList.add(i, (cList.get(i + 1) - cList.get(i)) / (3 * (xList.get(i + 1) - xList.get(i))));

        }
        return new List[]{aList, bList, cList, dList};
    }
}