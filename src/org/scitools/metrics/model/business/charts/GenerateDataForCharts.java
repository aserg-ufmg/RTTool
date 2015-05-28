package org.scitools.metrics.model.business.charts;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.CoordXY;
import org.scitools.metrics.model.beans.Linha;
import org.scitools.metrics.model.beans.MetricaLinhas;
import org.scitools.metrics.model.util.RWArquivoNew;
import  org.scitools.metrics.model.business.*;

/**
 *
 * @author fernando
 */
public class GenerateDataForCharts {

    public ArrayList<MetricaLinhas> geraDataChartsComplianceRatePenality(String path, ArrayList<String> nomesArquivos, int limiteInferior) {
        ReadCSV rcsv = new ReadCSV();


        ArrayList<MetricaLinhas> mls = new ArrayList();
        int nColunasArqPre = 21;
        for (String nomeArquivo : nomesArquivos) { //roda arquivos métricas
            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();

            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            int nLinhasArqPre = 0;
            for (int c = 0; c < nColunasArqPre; c++) {//roda colunas dos arqui os
                ArrayList<String> colunaArquivoPre = rcsv.getColuna(c, ReadCSV.SEPARADOR_VIGULA);
                arquivoPre.add(colunaArquivoPre);
                //numeros de linhas usados posteriormente
                if (nLinhasArqPre == 0) {
                    nLinhasArqPre = colunaArquivoPre.size();
                }
            }

            ArrayList<Linha> linhas = new ArrayList();
            int contP = 5;
            for (int c = limiteInferior; c < nColunasArqPre - 1; c++) { //anda coluna por coluna
                Linha linha = new Linha();
                ArrayList<CoordXY> la = new ArrayList();
                for (int l = 2; l < nLinhasArqPre - 1; l++) { //anda linhas na coluna
                    contP += 5;
                    CoordXY coo = new CoordXY();
                    coo.setXY(Float.parseFloat(arquivoPre.get(0).get(l)), Float.parseFloat(arquivoPre.get(c).get(l)));
                    la.add(coo);
                }
                linha.setLinha(la); //adiciona acoluna inteira
                linha.setNomeLinha(arquivoPre.get(c).get(1));//adiciona qual a porcentagem
                linhas.add(linha);
            }
            MetricaLinhas ml = new MetricaLinhas();
            ml.setLinhas(linhas);
            ml.setNomeMetrica(nomeArquivo);
            mls.add(ml);
        }// fim roda arquivos

        return mls;
    }

    public ArrayList<MetricaLinhas> geraDataChartsComplianceRate(String path, ArrayList<String> nomesArquivos, int limiteInferior) {
        ReadCSV rcsv = new ReadCSV();

        ArrayList<MetricaLinhas> mls = new ArrayList();
        int nColunasArqPre = 21;
        for (String nomeArquivo : nomesArquivos) { //roda arquivos métricas
            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();

            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            int nLinhasArqPre = 0;
            for (int c = 0; c < nColunasArqPre; c++) {//roda colunas dos arqui os
                ArrayList<String> colunaArquivoPre = rcsv.getColuna(c, ReadCSV.SEPARADOR_VIGULA);
                arquivoPre.add(colunaArquivoPre);
                //numeros de linhas usados posteriormente
                if (nLinhasArqPre == 0) {
                    nLinhasArqPre = colunaArquivoPre.size();
                }
            }

            ArrayList<Linha> linhas = new ArrayList();
            int contP = 5;
            for (int c = limiteInferior; c < nColunasArqPre - 1; c++) { //anda coluna por coluna
                Linha linha = new Linha();
                ArrayList<CoordXY> la = new ArrayList();
                for (int l = 2; l < nLinhasArqPre - 1; l++) { //anda linhas na coluna
                    contP += 5;
                    CoordXY coo = new CoordXY();
                    coo.setXY(Float.parseFloat(arquivoPre.get(0).get(l)), Float.parseFloat(arquivoPre.get(c).get(l)));
                    la.add(coo);
                }
                linha.setLinha(la); //adiciona acoluna inteira
                linha.setNomeLinha(arquivoPre.get(c).get(1));//adiciona qual a porcentagem
                linhas.add(linha);
            }
            MetricaLinhas ml = new MetricaLinhas();
            ml.setLinhas(linhas);
            ml.setNomeMetrica(nomeArquivo);
            mls.add(ml);
        }// fim roda arquivos

        return mls;
    }

    public ArrayList<MetricaLinhas> geraDataChartsPercentile(String path, ArrayList<String> nomesArquivos) {
        ReadCSV rcsv = new ReadCSV();

        ArrayList<MetricaLinhas> mls = new ArrayList();
        int colunas = 21;
        for (String nomeArquivo : nomesArquivos) { //roda arquivos métricas
            ArrayList<Linha> linhas = new ArrayList();

            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            ArrayList<String> colunaArquivoPre = rcsv.getColuna(0, ReadCSV.SEPARADOR_VIGULA);
            int qtdLinhas = colunaArquivoPre.size();
            String[] linhaString0 = rcsv.getLinha(0, ReadCSV.SEPARADOR_VIGULA);
            for (int l = 1; l < qtdLinhas - 1; l++) {
                Linha linha = new Linha();
                ArrayList<CoordXY> la = new ArrayList();
                String[] linhaString = rcsv.getLinha(l, ReadCSV.SEPARADOR_VIGULA);
                for (int c = 1; c < colunas; c++) {
                    CoordXY coo = new CoordXY();
                    coo.setXY(c * 5, Float.parseFloat(linhaString[c]));
                    la.add(coo);
                }
                linha.setLinha(la); //adiciona acoluna inteira
                linha.setNomeLinha(colunaArquivoPre.get(l));//adiciona o nome do sistema
                linhas.add(linha);
            }
            MetricaLinhas ml = new MetricaLinhas();
            ml.setLinhas(linhas);
            ml.setNomeMetrica(nomeArquivo);
            mls.add(ml);
        }// fim roda arquivos

        return mls;
    }
}