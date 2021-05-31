package main;

import main.DijkstraAlgorithm.Dijkstra;
import main.FloydWarshallAlgorithm.FloydWarshall;
import main.PrimsAlgorithm.Prim;
import main.utils.GraphVisual.EdgeNode;
import main.utils.GraphVisual.GraphVisualization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //Получение количества вершин
        int DIM = getMaxVertexIndex();

        //Создание списка ребер графа с вершинами
        ArrayList<EdgeNode> edgeList = new ArrayList<>();

        //Создание матрицы весов из файла
        int[][] weights = new int[DIM][DIM];
        int[] pathVertexes = new int[2];
        createWeightMatrixFromFile(weights, pathVertexes, edgeList);

        //Клонирование матриц
        int[][] clonedWeights = new int[DIM][DIM];
        cloneMatrices(weights, clonedWeights, DIM);

        //Визуализация полученного графа
        GraphVisualization.visualGraph(DIM, edgeList,
                "graph", 0.8, 0.001);

        //Очистка списка ребер после визуализации
        edgeList.clear();

        //Создание объектов алгоритмов
        FloydWarshall floydWarshall = new FloydWarshall();
        Dijkstra dijkstra = new Dijkstra();
        Prim prim = new Prim();

        //Печать результатов
        floydWarshall.printFloydWarshall(pathVertexes[0], pathVertexes[1], weights, DIM);
        cloneMatrices(clonedWeights, weights, DIM);
        dijkstra.printDijkstra(pathVertexes[0], pathVertexes[1], weights, DIM);
        cloneMatrices(clonedWeights, weights, DIM);
        prim.printPrim(weights, DIM, edgeList);

        //Визуализация минимального остовного дерева
        GraphVisualization.visualGraph(DIM, edgeList,
                "Prim-graph", 0.8, 0.000001);
    }

    //Клонирование матриц
    private static void cloneMatrices(int[][] sourceMatrix, int[][] distMatrix, int DIM) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                distMatrix[i][j] = sourceMatrix[i][j];
            }
        }
    }

    //Получение максимального индекса вершины
    private static int getMaxVertexIndex() {
        int maxVertexIndex = 0;
        try {
            FileReader fileReader = new FileReader("src/resources/matrix.txt");
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            boolean runFlag = true;
            while (runFlag) {
                if (line.endsWith(".")) {
                    runFlag = false;
                }
                int firstVertex = Integer.parseInt(line.substring(4, 5));
                int secondVertex = Integer.parseInt(line.substring(7, 8));
                if (firstVertex > maxVertexIndex) {
                    maxVertexIndex = firstVertex;
                }
                if (secondVertex > maxVertexIndex) {
                    maxVertexIndex = secondVertex;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxVertexIndex;
    }

    //Создание матрицы весов из файла
    private static void createWeightMatrixFromFile(int[][] matrix, int[] pathVariables,
                                                   ArrayList<EdgeNode> edgeList) {
        try {
            //Чтение весов ребер матрицы из файла
            FileReader fr = new FileReader("src/resources/matrix.txt");
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            boolean runFlag = true;
            while (runFlag) {
                if (line.endsWith(".")) {
                    runFlag = false;
                }
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

                //Добавление ребра в список на визуализацию
                edgeList.add(new EdgeNode(firstVertex + 1, secondVertex + 1,
                        weight, "endArrow=none;"));
                line = reader.readLine();
            }
            setMatrixInfinity(matrix);
            //Чтение варианта и точек начала и конца пути
            String varStr = line.substring(4);
            int variant = Integer.parseInt(varStr.substring(0, varStr.indexOf(" ")));
            //Определение начала и конца по варианту
            if (variant < 10) {
                pathVariables[0] = Integer.parseInt(varStr.substring(5, 6));
                pathVariables[1] = Integer.parseInt(varStr.substring(13, 14));
            } else if (variant < 100) {
                pathVariables[0] = Integer.parseInt(varStr.substring(6, 7));
                pathVariables[1] = Integer.parseInt(varStr.substring(14, 15));
            } else {
                pathVariables[0] = Integer.parseInt(varStr.substring(7, 8));
                pathVariables[1] = Integer.parseInt(varStr.substring(15, 16));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Переназначение весов-бесконечностей
    private static void setMatrixInfinity(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0 && i != j) {
                    matrix[i][j] = 100;
                }
            }
        }
    }

}
