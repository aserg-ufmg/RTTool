package org.scitools.metrics.view.util;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.scitools.metrics.model.beans.CoordXY;
import org.scitools.metrics.model.beans.Linha;

public class Chart extends ApplicationFrame {

    {
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true));
    }
    private JFreeChart jfreechart;

    public Chart(float k, String titleBarra, int larg, int alt, ArrayList<Linha> linhas, String titulo, String x, String y, boolean isCRP, boolean isP) {
        super(titleBarra);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel(k, linhas, titulo, x, y, isCRP, isP);
        chartPanel.setPreferredSize(new java.awt.Dimension(larg, alt));
        setContentPane(chartPanel);
    }

    private static JFreeChart createChart(float k, XYDataset dataset, String titulo, String nomeEixoX, String nomeEixoY, boolean isCRP, boolean isP) {
        JFreeChart chart;

        if (isP) {//se é percentile, tira a legenda
            chart = ChartFactory.createXYLineChart(
                    titulo, // title
                    nomeEixoX, // x-axis label
                    nomeEixoY, // y-axis label
                    dataset,
                    PlotOrientation.VERTICAL, false, false, false);

        } else {
            chart = ChartFactory.createXYLineChart(
                    titulo, // title
                    nomeEixoX, // x-axis label
                    nomeEixoY, // y-axis label
                    dataset);
        }
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            //renderer.setBaseShapesVisible(true);
            //renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setDrawOutlines(true);
            if (isP) {
                for (int i = 0; i < dataset.getSeriesCount(); i++) {
                    renderer.setSeriesPaint(i, java.awt.Color.BLACK);
                }
            }
        }

        if (isCRP) {
            //fazendo o render do ponto k
            XYSeries s1 = new XYSeries("ref: " + k);//cria série(linha)
            s1.add(k, 0);//preenche linha ponto a ponto
            XYSeriesCollection datasetX = new XYSeriesCollection();
            datasetX.addSeries(s1);//adiciona série ao dataset

            plot.setDataset(1, datasetX);
            XYLineAndShapeRenderer r2 = new XYLineAndShapeRenderer();//true, false);
            plot.setRenderer(1, r2);
            if (r2 instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r2;
                renderer.setBaseShapesVisible(true);
                renderer.setBaseShapesFilled(true);
                renderer.setDrawSeriesLineAsPath(true);
                renderer.setDrawOutlines(true);
                renderer.setSeriesPaint(0, java.awt.Color.BLACK);
            }
        }
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRangeWithMargins(range.getRange());
        return chart;
    }

    private static XYDataset createDataset(ArrayList<Linha> w) {
        XYSeries s1;
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Linha l : w) {//várias linhas
            s1 = new XYSeries(l.getNomeLinha());//cria série(linha)
            for (CoordXY z : l.getLinha()) { //vários pontos para cada linha
                s1.add(z.getX(), z.getY());//preenche linha ponto a ponto
            }
            dataset.addSeries(s1);//adiciona série ao dataset
        }
        return dataset;
    }

    private static XYDataset createDatasetPonto(ArrayList<Linha> w) {
        //fazendo o render do ponto k
        XYSeries s1 = new XYSeries("");//cria série(linha)
        s1.add(w.get(0).getK(), 0);//preenche linha ponto a ponto
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);//adiciona série ao dataset
        return dataset;
    }

    public JPanel createDemoPanel(float k, ArrayList<Linha> linhas, String titulo, String x, String y, boolean isCRP, boolean isP) {
        //remover isso quando passar por parâmentro daqui pra cima.
        JFreeChart chart = createChart(k, createDataset(linhas), titulo, x, y, isCRP, isP);
        jfreechart = chart;
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);

        return panel;
    }

    public JFreeChart getJFreechart(float k, ArrayList<Linha> linhas, String titulo, String x, String y, boolean isCRP, boolean isP) {
        JFreeChart chart = createChart(k, createDataset(linhas), titulo, x, y, isCRP, isP);
        return chart;
    }

    public JFreeChart getJFreechart() {
        return jfreechart;
    }

    public static void main(String[] args) {
        ArrayList<CoordXY> la = new ArrayList();
        la.add(new CoordXY().setXY(2, (float) 129.6));
        la.add(new CoordXY().setXY(3, (float) 123.2));
        la.add(new CoordXY().setXY(4, (float) 117.2));
        la.add(new CoordXY().setXY(5, (float) 124.1));
        la.add(new CoordXY().setXY(6, (float) 122.6));

        ArrayList<CoordXY> lb = new ArrayList();
        lb.add(new CoordXY().setXY(2, (float) 109.6));
        lb.add(new CoordXY().setXY(3, (float) 103.2));
        lb.add(new CoordXY().setXY(4, (float) 9.2));
        lb.add(new CoordXY().setXY(5, (float) 104.1));
        lb.add(new CoordXY().setXY(6, (float) 102.6));

        Linha l1 = new Linha();
        l1.setNomeLinha("linha1");
        l1.setLinha(la);
        Linha l2 = new Linha();
        l2.setNomeLinha("linha2");
        l2.setLinha(lb);

        ArrayList<Linha> linhas = new ArrayList();
        linhas.add(l1);
        linhas.add(l2);
    }
}
