package main.FloydWarshallAlgorithm;

import main.utils.Utils;

import java.io.*;

public class FloydWarshall {

    public void printFloydWarshall(int pathStart, int pathFinish,
                                   int[][] weights, int DIM) {
        printMatrix(weights, "Weights Matrix");

        //Создание матрицы вершин
        int[][] vertexes = new int[DIM][DIM];
        createVertexMatrix(weights, vertexes);
        printMatrix(vertexes, "Vertexes Matrix");

        int[][] stepWeights = new int[DIM][DIM];
        int[][] stepVertexes = new int[DIM][DIM];

        //Алгоритм Флойда-Уоршалла
        startFloydWarshall(weights, vertexes, stepWeights,
                stepVertexes, DIM);

        System.out.println("Результат:");
        printMatrix(weights, "Weights");
        printMatrix(vertexes, "Vertexes");

        //Проверка на связность графа
        printGraphConnection(weights, DIM);

        //Печать пути между 2 вершинами
        printPath(pathStart, pathFinish, vertexes);
    }

    private static void printPath(int startVertex, int finishVertex,
                                  int[][] vertexes) {
        if (vertexes[startVertex - 1][finishVertex - 1] == 100) {
            System.out.println("Нет пути!");
            return;
        }
        System.out.println("\nПуть из " + startVertex + " в " +
                finishVertex + ": ");
        System.out.print(startVertex);
        while (startVertex != finishVertex)
        {
            startVertex = vertexes[startVertex - 1][finishVertex - 1];
            System.out.print("-->" + startVertex);
        }
    }

    private static void printGraphConnection(int[][] matrix, int DIM) {
        System.out.print("Связность: ");
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (matrix[i][j] == 100) {
                    System.out.println("Граф не связный!");
                    return;
                }
            }
        }
        System.out.println("Граф связный");


    }

    private static void startFloydWarshall(int[][] weights,
                                           int[][] vertexes,
                                           int[][] stepWeights,
                                           int[][] stepVertexes,
                                           int DIM) {
        for (int k = 0; k < DIM; k++) {
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    if (weights[i][j] > (weights[i][k] + weights[k][j]))
                    {
                        weights[i][j] = weights[i][k] + weights[k][j];
                        vertexes[i][j] = vertexes[i][k];

                        stepWeights[i][j] = 1;
                        stepVertexes[i][j] = 1;
                    }
                }
            }
            System.out.println("Step = " + (k + 1));
            System.out.print("Weights:");
            System.out.print("                                      ");
            System.out.print("Vertexes:\n");
            for (int i = 0; i < DIM; i++) {
                printChangedWeights(weights, stepWeights, i, DIM);
                System.out.print("      ");
                printChangedVertexes(vertexes, stepVertexes, i, DIM);
                System.out.println();
            }
            System.out.println();

            resetMatrix(stepWeights, DIM);
            resetMatrix(stepVertexes, DIM);
        }
    }

    private static void resetMatrix(int[][] matrix, int DIM) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    private static void printChangedVertexes(int[][] vertexes,
                                             int[][] stepVertexes,
                                             int i, int DIM) {
        for (int j = 0; j < DIM; j++) {
            if (stepVertexes[i][j] == 1) {
                if (vertexes[i][j] < 10) {
                    System.out.print(" [" + vertexes[i][j] + "] ");
                } else {
                    System.out.print("[" + vertexes[i][j] + "] ");
                }
            } else {
                if (vertexes[i][j] < 10) {
                    System.out.print("   " + vertexes[i][j] + " ");
                } else {
                    System.out.print("  " + vertexes[i][j] + " ");
                }
            }
        }
    }

    private static void printChangedWeights(int[][] weights,
                                            int[][] stepWeights,
                                            int i, int DIM) {
        for (int j = 0; j < DIM; j++) {
            if (stepWeights[i][j] == 1) {
                if (weights[i][j] < 10) {
                    System.out.print(" [" + weights[i][j] + "] ");
                } else {
                    System.out.print("[" + weights[i][j] + "] ");
                }
            } else {
                if (weights[i][j] < 10) {
                    System.out.print("   " + weights[i][j] + " ");
                } else if (weights[i][j] == 100) {
                    System.out.print(" inf ");
                } else {
                    System.out.print("  " + weights[i][j] + " ");
                }
            }
        }
    }

    private static void createVertexMatrix(int[][] weights, int[][] vertexes) {
        for (int i = 0; i < vertexes.length; i++) {
            for (int j = 0; j < vertexes[i].length; j++) {
                if (weights[i][j] != 100) {
                    vertexes[i][j] = j + 1;
                } else {
                    vertexes[i][j] = 0;
                }
            }
        }
    }

    public void printMatrix(int[][] matrix, String name) {
        Utils.printMatrixUtils(matrix, name);
    }
}
