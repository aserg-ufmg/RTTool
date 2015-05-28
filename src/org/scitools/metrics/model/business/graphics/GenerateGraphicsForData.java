/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.business.graphics;


import java.util.ArrayList;
import  org.scitools.metrics.model.business.*;
import modelo.externos.GnuPlotComandos;
import org.scitools.metrics.model.util.RWArquivoNew;

/**
 *
 * @author fernando
 */
public class GenerateGraphicsForData {

    public void geraGraphicsPercentiles(String path, ArrayList<String> nomesArquivos, String pathDestino, String pathGnuplot) {
        ReadCSV rcsv = new ReadCSV();
        GnuPlotComandos gpc = new GnuPlotComandos(pathGnuplot);
        for (String nomeArquivo : nomesArquivos) {
            rcsv.setUrlArquivo(path + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + "HEAD.csv");
            ArrayList<String> cabecalhos = rcsv.getCabecalhos(ReadCSV.SEPARADOR_VIGULA);
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            String[] ultimaLinha = rcsv.getLinha(19, ' ');
            float maior = -100;
            for (int i = 0; i < ultimaLinha.length; i++) {
                if (Float.parseFloat(ultimaLinha[i]) > maior) {
                    maior = Float.parseFloat(ultimaLinha[i]);
                }
            }
            ArrayList<String> c = new ArrayList();
            c.add("reset\n");
//            c.add("set term png \n");
//            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".png\"\n");

            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".eps\" ");
            c.add("set terminal postscript color portrait");

            c.add("set isosamples x_rate, y_rate");//melhorar a resolução do gráfico
            c.add("set isosamples 100, 100");// o default é 10
            c.add("set title \""+nomeArquivo.substring(7, nomeArquivo.length()-9)+"\"");
            c.add("unset key");// tira legendas
            c.add("set xlabel \"Percentile\"");
            c.add("set ylabel \"" + nomeArquivo.substring(7, nomeArquivo.length() - 9) + "\"");
            //c.add("set yrange [0:" + maior + "]\n");
            c.add("set xrange [0:100]\n");
            String comando = "plot '" + path + "//" + nomeArquivo + "' using 1:2 with lines ";
            for (int col = 3; col < cabecalhos.size(); col++) {
                comando += (", '" + path + "//" + nomeArquivo + "' using 1:" + col + " with lines");
            }
            comando += " \n";
            c.add(comando);
            //c.add("unset multiplot\n");
            c.add("exit");
            gpc.exec(c);
        }
    }

    public void geraGraphicsCRatePenality(String path, ArrayList<String> nomesArquivos, String pathDestino, String pathGnuplot,int valorPorcentagemInicial) {
        ReadCSV rcsv = new ReadCSV();
        GnuPlotComandos gpc = new GnuPlotComandos(pathGnuplot);
        for (String nomeArquivo : nomesArquivos) {
//            rcsv.setUrlArquivo(path + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + "HEAD.csv");
//            ArrayList<String> cabecalhos = rcsv.getCabecalhos(ReadCSV.SEPARADOR_VIGULA);
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            String[] primeiraLinha = rcsv.getLinha(0, ' ');
                        
            ArrayList<String> c = new ArrayList();
            c.add("reset\n");
//            c.add("set term png \n");
//            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".png\"\n");

            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".eps\" ");
            c.add("set terminal postscript color portrait");
            c.add("set title \""+nomeArquivo.substring(14, nomeArquivo.length()-9)+"\"");
            c.add("set isosamples x_rate, y_rate");//melhorar a resolução do gráfico
            c.add("set isosamples 100, 100");// o default é 10
           // c.add("unset key");// tira legendas
            c.add("set xlabel \"k\"");
            c.add("set ylabel \"Compliance Rate Penality\"");
            //c.add("set yrange [0:" + maior + "]\n");
            //c.add("set xrange [0:100]\n");
        
            String comando = "plot '" + path + "//" + nomeArquivo + "' using 1:"+valorPorcentagemInicial/5+" t\""+valorPorcentagemInicial+"%\" with lines ";
            for (int col = (valorPorcentagemInicial/5+1); col < primeiraLinha.length; col++) {
                comando += (", '" + path + "//" + nomeArquivo + "' using 1:" + col + " t\"" + ((col) * 5) + "%\" with lines");
            }
            comando += " \n";
            c.add(comando);
            //c.add("unset multiplot\n");
            c.add("exit");
            gpc.exec(c);
        }
    }
    
    public void geraGraphicsCRate(String path, ArrayList<String> nomesArquivos, String pathDestino, String pathGnuplot,int valorPorcentagemInicial) {
        ReadCSV rcsv = new ReadCSV();
        GnuPlotComandos gpc = new GnuPlotComandos(pathGnuplot);
        for (String nomeArquivo : nomesArquivos) {
//            rcsv.setUrlArquivo(path + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + "HEAD.csv");
//            ArrayList<String> cabecalhos = rcsv.getCabecalhos(ReadCSV.SEPARADOR_VIGULA);
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            String[] primeiraLinha = rcsv.getLinha(0, ' ');
            
            ArrayList<String> c = new ArrayList();
            c.add("reset\n");
//            c.add("set term png \n");
//            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".png\"\n");

            c.add("set output \"" + pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 8) + ".eps\" ");
            c.add("set terminal postscript color portrait");

            c.add("set isosamples x_rate, y_rate");//melhorar a resolução do gráfico
            c.add("set isosamples 100, 100");// o default é 10
            //c.add("unset key");// tira legendas
            c.add("set title \""+nomeArquivo.substring(14, nomeArquivo.length()-9)+"\"");
            c.add("set xlabel \"k\"");
            c.add("set ylabel \"Compliance Rate\"");
            //c.add("set yrange [0:" + maior + "]\n");
            //c.add("set xrange [0:100]\n");
            String comando = "plot '" + path + "//" + nomeArquivo + "' using 1:"+valorPorcentagemInicial/5+" t\""+valorPorcentagemInicial+"%\" with lines ";
            for (int col = valorPorcentagemInicial/5+1; col < primeiraLinha.length; col++) {
                comando += (", '" + path + "//" + nomeArquivo + "' using 1:" + col + " t\"" + ((col) * 5) + "%\" with lines");
            }
            comando += " \n";
            c.add(comando);
            //c.add("unset multiplot\n");
            c.add("exit");
            gpc.exec(c);
        }
    }
}