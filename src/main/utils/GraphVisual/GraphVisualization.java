package main.utils.GraphVisual;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GraphVisualization extends JFrame {

    //Визуализация графа
    public static void visualGraph(int vertexes, ArrayList<EdgeNode> edgeList,
                                   String saveFileName, double zoomFactor, double lengthFactor) {
        //Создание
        mxGraph graph = new mxGraph();

        //Родитель
        Object parent = graph.getDefaultParent();

        //Список вершин
        ArrayList<Object> vertexList = new ArrayList<>();

        //Создание тела графа из списка ребер и вершин
        mxGraphComponent graphComponent =
                createMxGraphComponent(graph, parent, vertexes, edgeList, vertexList);

        //Оптимизация перекрытий ребер, выравнивание и сохранение в файл
        optimizeOverlapAndSave(graph, parent, graphComponent, saveFileName, zoomFactor, lengthFactor, vertexList);

        //Создание окна
        JFrame frame = new JFrame(saveFileName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Добавление тела графа
        frame.add(BorderLayout.CENTER, graphComponent);

        //Выравнивание окна по размеру экрана пользователя
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int jFrameWidth = screenDimension.width / 2;
        int jFrameHeight = screenDimension.height - 100;
        frame.getContentPane().setPreferredSize(new Dimension(jFrameWidth, jFrameHeight));

        //Запаковка и отображение
        frame.pack();
        frame.setVisible(true);
    }

    //Создание тела графа из списка ребер и вершин
    private static mxGraphComponent createMxGraphComponent(mxGraph graph,
                                                           Object parent, int vertexes,
                                                           ArrayList<EdgeNode> edgeList,
                                                           ArrayList<Object> vertexList) {
        //Начало обновления графа
        graph.getModel().beginUpdate();
        try {
            //Заполнение списка вершин
            for (int i = 0; i < vertexes; i++) {
                Object currentVertex = graph.insertVertex(parent, null, "v" + (i + 1),  i * 10,  i * 10, 30, 30);
                vertexList.add(currentVertex);
            }

            //Создание и добавление ребер между вершинами по списку ребер
            for (EdgeNode currentEdge : edgeList) {
                graph.insertEdge(parent, null, currentEdge.getEdgeWeight(),
                        vertexList.get(currentEdge.getFirstVertex() - 1),
                        vertexList.get(currentEdge.getSecondVertex() - 1),
                        currentEdge.getEdgeStyle());
            }

            //Установка стиля вершин
            Map<String, Object> vertexStyle = graph.getStylesheet().getDefaultVertexStyle();
            vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
            vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
            vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "black");
            vertexStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
            vertexStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);

            //Установка стиля ребер
            Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
            edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "black");
            edgeStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
            edgeStyle.put(mxConstants.STYLE_FONTSIZE, "14");
        } finally {
            //Конец обновления графа
            graph.getModel().endUpdate();
        }
        return new mxGraphComponent(graph);
    }

    //Оптимизация перекрытий ребер, выравнивание и сохранение в файл
    private static void optimizeOverlapAndSave(mxGraph graph, Object parent,
                                               mxGraphComponent graphComponent,
                                               String saveFileName, double zoomFactor,
                                               double lengthFactor, ArrayList<Object> vertexList) {
        //Создание панели для выравнивания
        mxOrganicLayout layout = new mxOrganicLayout(graph);

        //Установка параметров распределения и перекрытий
        layout.setNodeDistributionCostFactor(100);
        layout.setEdgeLengthCostFactor(lengthFactor);
        layout.setOptimizeEdgeCrossing(true);
        layout.setOptimizeBorderLine(true);
        layout.setMaxIterations(10000);

        //Начало обновления графа
        graph.getModel().beginUpdate();
        try {
            //Выгрузка панели в граф
            layout.execute(parent);
        } finally {
            //Перераспределение вершин и ребер
            mxMorphing morph = new mxMorphing(graphComponent, 100, 1.2, 1);
            morph.addListener(mxEvent.DONE, (arg0, arg1) ->
            {
                //Конец обновления графа
                graph.getModel().endUpdate();

                //Текущее тело графа
                mxIGraphModel currentGraph = graphComponent.getGraph().getModel();

                //Оптимальный сдвиг окна
                Optimal optimal = getOptimalGeometry(currentGraph, vertexList);

                //Установка масштаба
                graphComponent.zoom(zoomFactor);
                for (Object currentVertex : vertexList) {
                    mxGeometry currentGeometry = currentGraph.getGeometry(currentVertex);

                    //Перераспределение вершин
                    if (optimal.isDifference()) {
                        currentGraph.setGeometry(currentVertex,
                                new mxGeometry(currentGeometry.getX() + optimal.getDiffX(),
                                        currentGeometry.getY() + optimal.getDiffY(),
                                        currentGeometry.getWidth(), currentGeometry.getHeight()));
                    }
                }
                graphComponent.scrollToCenter(true);

                //Выравнивание окна по размеру экрана пользователя
                Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
                int frameWidth = Integer.min(screenDimension.width / 2,
                        (int) (optimal.getMaxCordX() + optimal.getDiffX()));
                int frameHeight = Integer.min(screenDimension.height - 100,
                        (int) (optimal.getMaxCordY() + optimal.getDiffY()));

                //Сохранение графа в виде файла
                BufferedImage image = new BufferedImage(frameWidth, frameHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = image.createGraphics();
                graphComponent.paint(graphics2D);

                try {
                    ImageIO.write(image,"jpeg",
                            new File("src/resources/" + saveFileName + ".jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            //Перераспределение ребер
            morph.startAnimation();
        }
    }

    //Оптимальный сдвиг окна
    private static Optimal getOptimalGeometry(mxIGraphModel currentGraph,
                                             ArrayList<Object> vertexList) {
        double maxCordX = 0;
        double minCordX = Double.MAX_VALUE;
        double maxCordY = 0;
        double minCordY = Double.MAX_VALUE;
        for (Object currentVertex : vertexList) {

            mxGeometry currentGeometry = currentGraph.getGeometry(currentVertex);

            double currentX = currentGeometry.getX();
            double currentY = currentGeometry.getY();

            maxCordX = Double.max(maxCordX, currentX);
            maxCordY = Double.max(maxCordY, currentY);
            minCordX = Double.min(minCordX, currentX);
            minCordY = Double.min(minCordY, currentY);
        }

        boolean difference = false;
        double diffX = 0;
        if (maxCordX > 500) {
            diffX = -minCordX + 10;
            difference = true;
        }
        double diffY = 0;
        if (maxCordY > 400) {
            diffY = -minCordY + 10;
            difference = true;
        }

        return new Optimal(diffX, diffY, difference, maxCordX, maxCordY);
    }
}

