package main.PrimsAlgorithm;

import main.utils.GraphVisual.EdgeNode;

import java.lang.*;
import java.util.ArrayList;

public class Prim {

    //Функция поиска минимальной вершины вне дерева
    private static int minKey(int[] key, Boolean[] minTreeSet, int vertexes)
    {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < vertexes; i++) {
            if (!minTreeSet[i] && key[i] < min) {
                min = key[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    //Печать дерева
    private static void printMinTree(int[] parent, int[][] graph, int vertexes,
                                     ArrayList<EdgeNode> edgeList)
    {
        System.out.println("\n\nМинимальное остовное дерево: ");
        System.out.println("Ребро \tВес");
        for (int i = 1; i < vertexes; i++) {
            //Печать
            System.out.println((parent[i] + 1) + " - " +
                    (i + 1) + "\t" + graph[i][parent[i]]);

            //Добавление ребра в список на визуализацию
            edgeList.add(new EdgeNode((parent[i] + 1), (i + 1),
                    graph[i][parent[i]], "edgeArrow=none;"));
        }
    }

    //Конструирование минимального остовного дерева
    private static void constructMinTree(int[][] graph, int vertexes,
                                         ArrayList<EdgeNode> edgeList)
    {
        //Массив для хранения дерева
        int[] parent = new int[vertexes];

        //Массив ключей для выбора минимального веса
        int[] key = new int[vertexes];

        //Массив включенных в дерево вершин
        Boolean[] minTreeSet = new Boolean[vertexes];

        //Инициализация ключей бесконечностью
        for (int i = 0; i < vertexes; i++) {
            key[i] = Integer.MAX_VALUE;
            minTreeSet[i] = false;
        }

        //Первая вершина всегда входит в дерево
        //Выбираем её в качестве первой
        key[0] = 0;
        //Первый узел всегда корень дерева
        parent[0] = -1;

        //В минимальном остовном дереве будут все вершины
        for (int vertex = 0; vertex < vertexes - 1; vertex++) {
            //Получаем индекс очередной минимальной
            // и не включенной в дерево вершины
            int minVertIndex = minKey(key, minTreeSet, vertexes);

            //Добавляем полученную вершину в дерево
            minTreeSet[minVertIndex] = true;

            //Обновляем веса и сохраненные индексы в соответствии
            // с новой вершиной
            for (int i = 0; i < vertexes; i++)

                //Вес ребра не должен быть 0, иначе в вершину нельзя прийти
                //Вершина должна быть еще не включена в дерево
                //Обновление только если вес меньше, чем текущий
                if (graph[minVertIndex][i] != 0 && !minTreeSet[i]
                        && graph[minVertIndex][i] < key[i]) {
                    parent[i] = minVertIndex;
                    key[i] = graph[minVertIndex][i];
                }
        }

        //Печать сконструированного дерева
        printMinTree(parent, graph, vertexes, edgeList);
    }

    //Вызов алгоритма Прима
    public void printPrim(int[][] graph, int vertexes,
                          ArrayList<EdgeNode> edgeList) {
        constructMinTree(graph, vertexes, edgeList);
    }
}
