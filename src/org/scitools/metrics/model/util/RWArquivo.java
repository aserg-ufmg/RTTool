/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 *
 * @author Fernando
 */
public class RWArquivo {

    private String url = "";

    public void setUrl(String urlArquivo) {
        url = urlArquivo;
    }

    public String getUrl() {
        return url;
    }
    File arquivo1;
    FileOutputStream fos1;
    BufferedWriter x;

    public void criaArq() throws Exception {


        arquivo1 = new File(url);
        fos1 = new FileOutputStream(arquivo1);
    }

    
//    public void randVetor(int[] vet, File file) {
//        try {
//            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
//            for (int i = 0; i < vet.length; i++) {
//                buffer.write(vet[i] + " ");
//                buffer.newLine();
//            }
//            buffer.close();
//            JOptionPane.showMessageDialog(null, "Arquivo gravado com "
//                    + "sucesso", "Concluído", JOptionPane.INFORMATION_MESSAGE);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage(),
//                    "Atenção", JOptionPane.WARNING_MESSAGE);
//        }
//    }

    public void escreveLinha(String linha) throws Exception {
        fos1.write(linha.getBytes());
    }

    public void fechaArq() throws Exception {
        fos1.close();
    }

    public void escreveArquivo(String texto) {
        try {
            // Gravando no arquivo
            File arquivo;

            arquivo = new File(url);
            FileOutputStream fos = new FileOutputStream(arquivo);
            fos.write(texto.getBytes());
            //texto = "\nquero gravar este texto AQUI TAMBEM";
            //fos.write(texto.getBytes());
            fos.close();
        } catch (Exception ee) {
            System.err.println("Erro ao criar arquivo RWArquivo:"+ee);
            //ee.printStackTrace();
        }
    }

    public String getTexto() {
        // Lendo do arquivo
        String resp = "";
        try {
            File arquivo = new File(url);
            FileInputStream fis = new FileInputStream(arquivo);

            int ln;
            while ((ln = fis.read()) != -1) {
                resp += (char) ln;
            }

            fis.close();
        } catch (Exception x) {
            System.err.println("Erro no RWAqruivo:"+x);
            //x.printStackTrace();
        }
        return resp;
    }
}
