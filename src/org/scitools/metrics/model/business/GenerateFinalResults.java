package org.scitools.metrics.model.business;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.FinalResult;
import org.scitools.metrics.model.beans.IdentifyNamesNewPattern;

public class GenerateFinalResults {

    public ArrayList<StringBuffer> getOutlayers(String path, ArrayList<String> nomeArquivo, ArrayList<FinalResult> resultadosMetricas, boolean isNewPattern) {
        ReadCSV rcsv = new ReadCSV();
        //ArrayList<FinalResult> saida = new ArrayList();
        ArrayList<StringBuffer> resp = new ArrayList();

        for (FinalResult resultado : resultadosMetricas) {//enquanto métricas finais
            StringBuffer sisOutlayers = new StringBuffer();
            for (int w = 0; w < nomeArquivo.size(); w++) {                              //roda todos arquivos weitghs
                int contResMenorAllArqs = 0;
                int contElementsAllArqs = 0;

                FinalResult fr = null;
                rcsv.setUrlArquivo(path + "//" + nomeArquivo.get(w));
                //pega a coluna da métrica
                ArrayList<String> coluna = null;
                if (isNewPattern) {
                    coluna = rcsv.getColuna(IdentifyNamesNewPattern.getMetricOfMnemonic(resultado.getMetrica()), ReadCSV.SEPARADOR_PONTOVIGULA);
                } else {
                    coluna = rcsv.getColuna(resultado.getMetrica(), ReadCSV.SEPARADOR_VIGULA);
                }
                if (coluna.size() > 0) { //o arquivo possui aquela métrica
                    for (String y : coluna) {
                        try {
                            if (Float.parseFloat(y) <= resultado.getK()) {
                                contResMenorAllArqs++;
                            }
                            contElementsAllArqs++;
                        } catch (Exception x) {
                            //System.err.println("erro ao converter: " + y);
                        }
                    }
                    float p = (float) contResMenorAllArqs / ((float) contElementsAllArqs / 100);
                    //p=vp/(n/100)

                    //se p for maior que perce/ntil o sistema é outlayer
                    if (p < resultado.getPercentil()) {
                        //mostrar sistemas outlayer
                        String nomeOutlayer = nomeArquivo.get(w);
                        sisOutlayers.append(nomeOutlayer.substring(0, nomeOutlayer.length() - 4));
                        sisOutlayers.append(";");
                    }
                }
            }
            resp.add(sisOutlayers);
        }
        return resp;
    }

    public ArrayList<StringBuffer> getOutlayersNew(String path, ArrayList<String> nomeArquivoPercentil, ArrayList<FinalResult> resultadosMetricas) {

        ArrayList<StringBuffer> resp = new ArrayList();
        for (FinalResult resultado : resultadosMetricas) {//roda as métricas
            StringBuffer sisOutlayers = new StringBuffer();
            for (int w = 0; w < nomeArquivoPercentil.size(); w++) {//roda todos arquivos percentile
                if (nomeArquivoPercentil.get(w).contains(resultado.getMetrica())) {
                    FinalResult fr = null;
                    ReadCSV rcsv = new ReadCSV();
                    rcsv.setUrlArquivo(path + "//" + nomeArquivoPercentil.get(w));

                    ArrayList<String> coluna = null;
                    //pega a coluna do percentil encontrado
                    coluna = rcsv.getColuna(((int) resultado.getPercentil()) + "%", ReadCSV.SEPARADOR_VIGULA);

                    ArrayList coluna0 = rcsv.getColuna(0, ReadCSV.SEPARADOR_VIGULA);
                    if (coluna.size() > 0) { //o arquivo possui aquela métrica
                        int l = 1;
                        for (String y : coluna) { //roda a coluna
                            try {
                                //feito depois 
                                float aux;
                                if (Float.parseFloat(y) < 1) {
                                    aux = (float) 0.1;
                                } else {
                                    aux = 1;
                                }
                                if (Float.parseFloat(y) > resultado.getK() + aux) {
                                    sisOutlayers.append(coluna0.get(l).toString());//adiciona o nome do sistema
                                    sisOutlayers.append(";");
                                }
                            } catch (Exception x) {
                                //System.err.println("erro ao converter: " + y);
                            }
                            l++;
                        }

                    }
                }
            }
            resp.add(sisOutlayers);
        }
        return resp;
    }

    public ArrayList<StringBuffer> getOutlayersNewPattern(String path, ArrayList<String> nomeArquivo, ArrayList<FinalResult> resultadosMetricas) {
        ReadCSV rcsv = new ReadCSV();
        //ArrayList<FinalResult> saida = new ArrayList();
        ArrayList<StringBuffer> resp = new ArrayList();

        for (FinalResult resultado : resultadosMetricas) {//enquanto métricas finais
            StringBuffer sisOutlayers = new StringBuffer();
            for (int w = 0; w < nomeArquivo.size(); w++) {                              //roda todos arquivos weitghs
                int contResMenorAllArqs = 0;
                int contElementsAllArqs = 0;

                FinalResult fr = null;
                rcsv.setUrlArquivo(path + "//" + nomeArquivo.get(w));
                //pega a coluna da métrica
                ArrayList<String> coluna = rcsv.getColuna(resultado.getMetrica(), ReadCSV.SEPARADOR_VIGULA);
                if (coluna.size() > 0) { //o arquivo possui aquela métrica
                    float auxSoma = 1;
                    if (resultado.getK() < 1) {
                        auxSoma = (float) 0.1;
                    }
                    for (String y : coluna) {
                        try {
                            if (Float.parseFloat(y) <= (resultado.getK() + auxSoma)) {
                                contResMenorAllArqs++;
                            }
                            contElementsAllArqs++;
                        } catch (Exception x) {
                            //System.err.println("erro ao converter: " + y);
                        }
                    }
                    float p = (float) contResMenorAllArqs / ((float) contElementsAllArqs / 100);
                    //p=vp/(n/100)

                    //se p for maior que perce/ntil o sistema é outlayer
                    if (p < resultado.getPercentil()) {
                        //mostrar sistemas outlayer
                        String nomeOutlayer = nomeArquivo.get(w);
                        sisOutlayers.append(nomeOutlayer.substring(0, nomeOutlayer.length() - 4));
                        sisOutlayers.append(";");
                    }
                }
            }
            resp.add(sisOutlayers);
        }
        return resp;
    }

    public ArrayList<FinalResult> getTabelaFinalResults(String path, ArrayList<String> nomeArquivo) {
        ReadCSV rcsv = new ReadCSV();
        ArrayList<FinalResult> saida = new ArrayList();
        for (int w = 0; w < nomeArquivo.size(); w++) {                              //roda todos arquivos weitghs
            FinalResult fr = null;
            rcsv.setUrlArquivo(path + "//" + nomeArquivo.get(w));
            int qtdColunas = rcsv.getQtdColunas(ReadCSV.SEPARADOR_VIGULA);
            boolean flagContinua = true;
            for (int i = qtdColunas - 1; i >= 0 && flagContinua; i--) {             // de tras pra frente percorre as colunas
                ArrayList<String> colunaPercentil = rcsv.getColuna(i, ReadCSV.SEPARADOR_VIGULA);
                for (int j = 0; j < colunaPercentil.size() && flagContinua; j++) {  // varre a coluna de cima pra baixo
                    if ((colunaPercentil.get(j).equalsIgnoreCase("0") || colunaPercentil.get(j).equalsIgnoreCase("0.0")) && j != 0) {   // verifica o primeiro 0 da coluna
                        if (!colunaPercentil.get(1).equalsIgnoreCase("k") && !colunaPercentil.get(1).equalsIgnoreCase("i")) {
                            fr = new FinalResult();
                            //fr.setK(j-2);
                            fr.setK(Float.parseFloat(rcsv.getColuna(0, ReadCSV.SEPARADOR_VIGULA).get(j)));
                            fr.setPercentil(Float.parseFloat(colunaPercentil.get(1).replace("%", "")));
                            fr.setMetrica(nomeArquivo.get(w).substring(14, nomeArquivo.get(w).length() - 4));
                        } else {
                            fr = new FinalResult();
                            fr.setMetrica(nomeArquivo.get(w).substring(14, nomeArquivo.get(w).length() - 4));
                        }
                        flagContinua = false;
                    }
                }
            }
            saida.add(fr);
        }
        return saida;
    }
}
