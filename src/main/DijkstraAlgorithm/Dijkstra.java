package main.DijkstraAlgorithm;

public class Dijkstra {

    private static final int NO_PARENT = -1;

    private static void dijkstra(int[][] weights, int startVertex,
                                 int endVertex) {

        int vertexes = weights[0].length;
        int[] smallestWeights = new int[vertexes];
        boolean[] added = new boolean[vertexes];

        for (int vertexIndex = 0; vertexIndex < vertexes; vertexIndex++) {
            smallestWeights[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }
        smallestWeights[startVertex] = 0;

        int[] parents = new int[vertexes];
        parents[startVertex] = NO_PARENT;

        for (int i = 1; i < vertexes; i++) {
            int nearestVertex = -1;
            int smallestWeight = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < vertexes; vertexIndex++) {
                if (!added[vertexIndex] && smallestWeights[vertexIndex] <
                                smallestWeight) {
                    nearestVertex = vertexIndex;
                    smallestWeight = smallestWeights[vertexIndex];
                }
            }
            added[nearestVertex] = true;

            for (int vertexIndex = 0; vertexIndex < vertexes; vertexIndex++) {
                int edgeWeight = weights[nearestVertex][vertexIndex];

                if (edgeWeight > 0 && ((smallestWeight + edgeWeight) <
                        smallestWeights[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    smallestWeights[vertexIndex] = smallestWeight +
                            edgeWeight;
                }
            }
        }

        printSolution(startVertex, smallestWeights, parents);
        printPathBetweenVertexes(startVertex, endVertex, smallestWeights, parents);
    }

    private static void printPathBetweenVertexes(int startVertex, int endVertex,
                                                 int[] distances, int[] parents) {
        System.out.println("\n\nПуть между " + (startVertex + 1) + " и " +
                (endVertex + 1) + ": ");
        System.out.println("Вершины   Вес   Путь");
        System.out.print((startVertex + 1) + " -> ");
        System.out.print((endVertex + 1) + "    ");
        if (distances[endVertex] < 10) {
            System.out.print(distances[endVertex] + "  \t");
        } else {
            System.out.print(distances[endVertex] + " \t");
        }
        printPath(endVertex, parents);
    }

    private static void printSolution(int startVertex, int[] distances, int[] parents)
    {
        int nVertices = distances.length;
        System.out.print("\n\nВершины   Вес   Путь");

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            System.out.print("\n" + (startVertex + 1) + " -> ");
            System.out.print((vertexIndex + 1) + "    ");
            if (distances[vertexIndex] < 10) {
                System.out.print(distances[vertexIndex] + "  \t");
            } else {
                System.out.print(distances[vertexIndex] + " \t");
            }
            printPath(vertexIndex, parents);
        }
    }

    private static void printPath(int currentVertex, int[] parents) {

        if (currentVertex == NO_PARENT) {
            return;
        }
        printPath(parents[currentVertex], parents);
        System.out.print((currentVertex + 1) + " ");
    }

    public void printDijkstra(int start, int end, int[][] weights, int DIM) {
        dijkstra(weights, start - 1, end - 1);
    }
}