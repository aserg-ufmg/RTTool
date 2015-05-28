/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Fernando
 */
public class RWArquivoNew {

    private String url = "";

    public void setUrl(String urlArquivo) {
        url = urlArquivo;
    }

    public String getUrl() {
        return url;
    }
    private File file;
    private FileWriter fw;
    private PrintWriter pw;

    public void criaArq() throws Exception {

        url = url.replace("\\", "//");
        url = url.replace("//", "/");
        
        file = new File(url);
        //file.mkdir();
        System.out.println(url+"\n");
        if (!file.exists()) {
            file.createNewFile();
            //System.out.println("Arquivo criado com sucesso");
        } else {
            System.out.println("!");
            //System.out.println("Arquivo ja existe");
        }

        pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        //arquivo1 = new File(url);
        //fos1 = new FileOutputStream(arquivo1);
    }

    public void escreveLinha(StringBuffer linha) throws Exception {
        pw.println(linha.toString());
    }

    public void fechaArq() throws Exception {
        pw.flush();
        pw.close();
    }

    public int getLinhas(String url) {
        int nLinhas = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(url));
            String str;
            while ((str = in.readLine()) != null) {
                nLinhas++;
            }
            in.close();
            //System.out.printf("Numero de linhas: %d %n", nLinhas);
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return nLinhas;
    }

    public StringBuffer getTexto(String url) {
        StringBuffer nLinhas = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(url));
            String str;
            while ((str = in.readLine()) != null) {
                nLinhas.append(str);
            }
            in.close();
            //System.out.printf("Numero de linhas: %d %n", nLinhas);
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }

        return nLinhas;
    }
}
