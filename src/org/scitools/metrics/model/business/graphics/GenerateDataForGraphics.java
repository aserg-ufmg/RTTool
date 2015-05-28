package org.scitools.metrics.model.business.graphics;

import java.util.ArrayList;
import  org.scitools.metrics.model.business.*;
import org.scitools.metrics.model.util.RWArquivoNew;

/**
 *
 * @author fernando
 */
public class GenerateDataForGraphics {

    public void geraDataGraphicsPercentiles(String path, ArrayList<String> nomesArquivos, String pathDestino) {
        ReadCSV rcsv = new ReadCSV();
        int nColunasArqPre = 21;

        for (String nomeArquivo : nomesArquivos) {
            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            int nLinhasArqPre = 0;
            for (int c = 1; c < nColunasArqPre; c++) {
                ArrayList<String> colunaArquivoPre = rcsv.getColuna(c, ReadCSV.SEPARADOR_VIGULA);
                arquivoPre.add(colunaArquivoPre);
                //numeros de linhas usados posteriormente
                if (nLinhasArqPre == 0) {
                    nLinhasArqPre = colunaArquivoPre.size();
                }
            }
            StringBuffer saidaNomesSistemas = new StringBuffer();
            ArrayList<String> colunaNomesSistemas = rcsv.getColuna(0, ReadCSV.SEPARADOR_VIGULA);
            //saidaNomesSistemas.append("SystemNames");
            boolean imprimeVirguala = false;
            for (String nomeSistema : colunaNomesSistemas) {
                if (imprimeVirguala) {
                    saidaNomesSistemas.append(",");
                }
                imprimeVirguala = true;
                saidaNomesSistemas.append(nomeSistema);
            }

            StringBuffer saidaDataGraphic = new StringBuffer();
            int contP = 5;
            for (int c = 0; c < nColunasArqPre - 1; c++) { //menos 1, pois, tiramos a primeira coluna
                saidaDataGraphic.append(contP);
                saidaDataGraphic.append(" ");
                contP += 5;
                for (int l = 1; l < nLinhasArqPre; l++) { // quero imprimir as linhas em colunas(transpor a tabela percentis)
                    saidaDataGraphic.append(arquivoPre.get(c).get(l));
                    saidaDataGraphic.append(" ");
                }
                saidaDataGraphic.append("\n");
            }

            //gravar arquivos de data
            RWArquivoNew rw = new RWArquivoNew();
            rw.setUrl(pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + "_DATA.csv");
            try {
                rw.criaArq();
                rw.escreveLinha(saidaDataGraphic);
                rw.fechaArq();
            } catch (Exception x) {
            }
            rw.setUrl(pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + "_HEAD.csv");
            try {
                rw.criaArq();
                rw.escreveLinha(saidaNomesSistemas);
                rw.fechaArq();
            } catch (Exception x) {
            }
        }// fim roda arquivos

    }

    public void geraDataGraphicsComplianceRatePenality(String path, ArrayList<String> nomesArquivos, String pathDestino) {
        ReadCSV rcsv = new ReadCSV();
        int nColunasArqPre = 21;
        for (String nomeArquivo : nomesArquivos) {
            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            int nLinhasArqPre = 0;
            for (int c = 0; c < nColunasArqPre; c++) {
                ArrayList<String> colunaArquivoPre = rcsv.getColuna(c, ReadCSV.SEPARADOR_VIGULA);
                arquivoPre.add(colunaArquivoPre);
                //numeros de linhas usados posteriormente
                if (nLinhasArqPre == 0) {
                    nLinhasArqPre = colunaArquivoPre.size();
                }
            }

            StringBuffer saidaDataGraphic = new StringBuffer();
            int contP = 5;
            for (int l = 2; l < nLinhasArqPre - 1; l++) { // não transpõe
                //saidaDataGraphic.append(contP);
                //saidaDataGraphic.append(" ");
                contP += 5;
                for (int c = 0; c < nColunasArqPre; c++) { //aqui não se tira a primeira coluna
                    saidaDataGraphic.append(arquivoPre.get(c).get(l));
                    saidaDataGraphic.append(" ");
                }
                saidaDataGraphic.append("\n");
            }

            //gravar arquivos de data
            RWArquivoNew rw = new RWArquivoNew();
            rw.setUrl(pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + "_DATW.csv");
            try {
                rw.criaArq();
                rw.escreveLinha(saidaDataGraphic);
                rw.fechaArq();
            } catch (Exception x) {
            }

        }// fim roda arquivos

    }

    public void geraDataGraphicsComplianceRate(String path, ArrayList<String> nomesArquivos, String pathDestino) {
        ReadCSV rcsv = new ReadCSV();
        int nColunasArqPre = 21;
        for (String nomeArquivo : nomesArquivos) {
            ArrayList<ArrayList<String>> arquivoPre = new ArrayList();
            rcsv.setUrlArquivo(path + "//" + nomeArquivo);
            int nLinhasArqPre = 0;
            for (int c = 0; c < nColunasArqPre; c++) {
                ArrayList<String> colunaArquivoPre = rcsv.getColuna(c, ReadCSV.SEPARADOR_VIGULA);
                arquivoPre.add(colunaArquivoPre);
                //numeros de linhas usados posteriormente
                if (nLinhasArqPre == 0) {
                    nLinhasArqPre = colunaArquivoPre.size();
                }
            }

            StringBuffer saidaDataGraphic = new StringBuffer();
            for (int l = 2; l < nLinhasArqPre - 1; l++) { 
                for (int c = 0; c < nColunasArqPre; c++) {
                    saidaDataGraphic.append(arquivoPre.get(c).get(l));
                    saidaDataGraphic.append(" ");
                }
                saidaDataGraphic.append("\n");
            }

            //gravar arquivos de data
            RWArquivoNew rw = new RWArquivoNew();
            rw.setUrl(pathDestino + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + "_DATC.csv");
            try {
                rw.criaArq();
                rw.escreveLinha(saidaDataGraphic);
                rw.fechaArq();
            } catch (Exception x) {
            }
        }// fim roda arquivos
    }
}