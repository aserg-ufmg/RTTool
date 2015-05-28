/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.control;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.datain_xml.Metrics;
import org.scitools.metrics.model.business.GenerateTableMethodPerType;
import org.scitools.metrics.model.business.GenerateTablePerMethod;
import org.scitools.metrics.model.business.GenerateTablePerPackages;
import org.scitools.metrics.model.business.GenerateTablePerType;
import org.scitools.metrics.model.business.ReadXML;
import org.scitools.metrics.model.util.ManipulaDir;
import org.scitools.metrics.model.util.RWArquivoNew;

/**
 *
 * @author fernando
 */
public class CGAllProjectsForXML {

    public static String PER_PACKAGE = "pkg";
    public static String PER_TYPE = "typ";
    public static String PER_METHOD = "met";
    public static String PER_TYPE_METHOD = "mtt";
    private ArrayList<String> listFilesNames;
    private String pathRead, pathWrite;
    private ArrayList<ArrayList<String>> listProjectsFilesNames;

    public CGAllProjectsForXML(ArrayList<String> listFilesNames, String pathRead, String pathWrite) {
        this.listFilesNames = listFilesNames;
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
    }

    private void geraTabelas(String nomePrimeiroArquivo, ArrayList<Metrics> listMetrics, String PER_X) {
        GenerateTablePerPackages gtp;
        GenerateTablePerType gtt;
        GenerateTablePerMethod gtm;
        GenerateTableMethodPerType gttm;
        ManipulaDir md = new ManipulaDir();
        md.criaDiretorio(pathWrite, "//data-default");

        RWArquivoNew rw = new RWArquivoNew();
        rw.setUrl(pathWrite + "//data-default" + "//" + nomePrimeiroArquivo + "_" + PER_X + ".csv");
        try {
            rw.criaArq();
            switch (PER_X) {
                case "pkg":
                    gtp = new GenerateTablePerPackages();
                    rw.escreveLinha(gtp.geraTabelaPacote(listMetrics));
                    break;
                case "typ":
                    gtt = new GenerateTablePerType();
                    rw.escreveLinha(gtt.geraTabelaType(listMetrics));
                    break;
                case "met":
                    gtm = new GenerateTablePerMethod();
                    rw.escreveLinha(gtm.geraTabelaMethod(listMetrics));
                    break;
                case "mtt":
                    gttm = new GenerateTableMethodPerType();
                    rw.escreveLinha(gttm.geraTabelaType(listMetrics));
                    break;
            }
            rw.fechaArq();
        } catch (Exception x) {
            //System.err.println("Generating file error: " + x);
            //x.printStackTrace();
        }
        //System.out.println("Arquivo gerado:" + PER_X);
        System.gc();
    }

    public void readXmlProject(ArrayList<String> listNamesForProject, boolean ifNotText) {
        ArrayList<Metrics> listMetrics = new ArrayList();
        Metrics x;
        for (int i = 0; i < listNamesForProject.size(); i++) {
            x = new Metrics();
            ReadXML.readXML(pathRead + "//" + listNamesForProject.get(i), x, ifNotText);
            listMetrics.add(x);
        }
        //System.out.println();
        //System.out.println(listNamesForProject.get(0) + ", " + PER_TYPE);
        geraTabelas(listNamesForProject.get(0), listMetrics, PER_TYPE);
        //System.out.println();
        //System.out.println(listNamesForProject.get(0) + ", " + PER_PACKAGE);
        geraTabelas(listNamesForProject.get(0), listMetrics, PER_PACKAGE);
        //System.out.println();
        //System.out.println(listNamesForProject.get(0) + ", " + PER_METHOD);
        geraTabelas(listNamesForProject.get(0), listMetrics, PER_METHOD);
        //System.out.println();
        //System.out.println(listNamesForProject.get(0) + ", " + PER_TYPE_METHOD);
        geraTabelas(listNamesForProject.get(0), listMetrics, PER_TYPE_METHOD);
    }

    public ArrayList<ArrayList<String>> getListProjectsFilesNames() {
        return listProjectsFilesNames;
    }

    public CGAllProjectsForXML(ArrayList<String> listFilesNames) {
        this.listFilesNames = listFilesNames;
    }

    public void applyFilesNamesFilter(String subStringOfEliminate) {
        //System.out.println("Lista de arquivos desprezados");
        ArrayList<String> saida = new ArrayList();
        for (String x : listFilesNames) {
            if (!x.contains(subStringOfEliminate)) {
                saida.add(x);
            } else {
               // System.out.println(x);
            }
        }
        this.listFilesNames = saida;
        //System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
    }

    public int applyProjectsNamesList() {
        int qtdFiles = 0, qtdProjects = 0, tam = listFilesNames.size();
        listProjectsFilesNames = new ArrayList();
        ArrayList<String> saida = new ArrayList();
        String aux = "-1", auxTotal = "-1";
        for (String x : listFilesNames) {
            if (!x.substring(0, 7).equalsIgnoreCase(aux) && !aux.equalsIgnoreCase("-1")) {
                saida.add(auxTotal);
                listProjectsFilesNames.add(saida);
                qtdFiles++;
                qtdProjects++;
                if (qtdFiles >= listFilesNames.size()) {
                    saida.add(x);
                    listProjectsFilesNames.add(saida);
                }
                saida = new ArrayList();
            } else {
                //verificação marco tulio
                //if (auxTotal.equalsIgnoreCase("-1")) {
                //    saida.add(auxTotal);     
                //}
                qtdFiles++;
                if (qtdFiles >= tam) {
                    qtdProjects++;
                    saida.add(x);
                    listProjectsFilesNames.add(saida);
                }
            }
            aux = x.substring(0, 7);
            auxTotal = x;
        }
        this.listFilesNames = saida;
        //System.out.println("n Proj: " + qtdProjects);
        return qtdProjects;
    }
}
