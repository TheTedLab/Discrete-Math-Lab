package main.utils.GraphVisual;

//Класс ребра графа с вершинами
public class EdgeNode {
    private final int firstVertex;
    private final int secondVertex;
    private final int edgeWeight;
    private String edgeStyle;

    public EdgeNode(int firstVertex, int secondVertex, int edgeWeight, String edgeStyle) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.edgeWeight = edgeWeight;
        this.edgeStyle = edgeStyle;
    }

    public int getFirstVertex() {
        return firstVertex;
    }

    public int getSecondVertex() {
        return secondVertex;
    }

    public int getEdgeWeight() {
        return edgeWeight;
    }

    public String getEdgeStyle() {
        return edgeStyle;
    }

    public void setEdgeStyle(String edgeStyle) {
        this.edgeStyle = edgeStyle;
    }

    @Override
    public String toString() {
        return "EdgeNode{" +
                "firstVertex=" + firstVertex +
                ", secondVertex=" + secondVertex +
                ", edgeWeight=" + edgeWeight +
                '}';
    }
}
