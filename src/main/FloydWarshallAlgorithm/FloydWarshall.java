package main.FloydWarshallAlgorithm;

import java.io.*;

public class FloydWarshall {
    static int DIM = 8;

    public static void main(String[] args) {
        int[][] weights = new int[DIM][DIM];
        int[][] vertexes = new int[DIM][DIM];

        createWeightMatrixFromFile(weights);
        printMatrix(weights, "Weights Matrix");

        createVertexMatrix(weights, vertexes);
        printMatrix(vertexes, "Vertexes Matrix");

        int[][] stepWeights = new int[DIM][DIM];
        int[][] stepVertexes = new int[DIM][DIM];

        startFloydWarshall(weights, vertexes, stepWeights, stepVertexes);

        System.out.println("Результат:");
        printMatrix(weights, "Weights");
        printMatrix(vertexes, "Vertexes");

        printGraphConnection(weights);

        int pathStart = 3;
        int pathFinish = 8;
        printPath(pathStart, pathFinish, vertexes);
    }

    private static void printPath(int startVertex, int finishVertex, int[][] vertexes) {
        if (vertexes[startVertex - 1][finishVertex - 1] == 100) {
            System.out.println("Нет пути!");
            return;
        }
        System.out.println("\nПуть из " + startVertex + " в " + finishVertex + ": ");
        System.out.print(startVertex);
        while (startVertex != finishVertex)
        {
            startVertex = vertexes[startVertex - 1][finishVertex - 1];
            System.out.print("-->" + startVertex);
        }
    }

    private static void printGraphConnection(int[][] matrix) {
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

    private static void startFloydWarshall(int[][] weights, int[][] vertexes,
                                           int[][] stepWeights, int[][] stepVertexes) {
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
            System.out.print("Vertexes:");
            for (int i = 0; i < DIM; i++) {
                printChangedWeights(weights, stepWeights, i);
                System.out.print("      ");
                printChangedVertexes(vertexes, stepVertexes, i);
                System.out.println();
            }
            System.out.println();

            resetMatrix(stepWeights);
            resetMatrix(stepVertexes);
        }
    }

    private static void resetMatrix(int[][] matrix) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    private static void printChangedVertexes(int[][] vertexes,
                                             int[][] stepVertexes, int i) {
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
                                            int[][] stepWeights, int i) {
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

    private static void createWeightMatrixFromFile(int[][] matrix) {
        try {
            FileReader fr = new FileReader("src/resources/matrix.txt");
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                int firstVertex = Integer.parseInt(line.substring(4, 5)) - 1;
                int secondVertex = Integer.parseInt(line.substring(7, 8)) - 1;
                int weight = 0;
                if (line.length() < 14) {
                    weight = Integer.parseInt(line.substring(11, 12));
                } else if (line.length() == 14) {
                    weight = Integer.parseInt(line.substring(11, 13));
                }
                matrix[firstVertex][secondVertex] = weight;
                matrix[secondVertex][firstVertex] = weight;
                line = reader.readLine();
            }
            setMatrixInfinity(matrix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setMatrixInfinity(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0 && i != j) {
                    matrix[i][j] = 100;
                }
            }
        }
    }

    private static void printMatrix(int[][] matrix, String name) {
        System.out.println(name + ": ");
        for (int[] rows : matrix) {
            for (int cols : rows) {
                if (cols < 10) {
                    System.out.print("  " + cols + " ");
                } else if (cols == 100) {
                    System.out.print("inf ");
                } else {
                    System.out.print(" " + cols + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
