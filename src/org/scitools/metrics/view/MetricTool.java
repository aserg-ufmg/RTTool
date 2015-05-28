/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.view;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.scitools.metrics.control.CGAllFInalResults;
import org.scitools.metrics.control.CGAllFilesComplianceAll;
import org.scitools.metrics.control.CGAllMetricsPercentiles;
import org.scitools.metrics.control.CGAllProjectsForXML;
import org.scitools.metrics.model.beans.FinalResult;
import org.scitools.metrics.model.beans.datain_xml.Metrics;

/**
 *
 * @author fernando
 */
public class MetricTool extends javax.swing.JFrame implements PropertyChangeListener {

    private GuiMetricsAllProjectsXML gapx = new GuiMetricsAllProjectsXML();
    private GuiMetricsAllProjectsCSVPercentile gmapc = new GuiMetricsAllProjectsCSVPercentile();
    private GuiMetricsAllProjectsCSVCompliance gmapcc = new GuiMetricsAllProjectsCSVCompliance();
    private GuiMetricsAllProjectsCSVComplianceAll gmapcca = new GuiMetricsAllProjectsCSVComplianceAll();
    private GuiMetricsAllProjectsCSVFinalResult gmapcfr = new GuiMetricsAllProjectsCSVFinalResult();
    private GuiMetricsAllGraphics gmag = new GuiMetricsAllGraphics();

    /**
     * Creates new form MetricTool
     */
    public MetricTool() {
        initComponents();
    }
    //private ArrayList<Metrics> listMetrics = new ArrayList();
    //private String nomePrimeiroArquivo = "";
    private ArrayList<String> filesNames;
    private ArrayList<String> filesNamesOriginal;
    private String pathOrigem = "";
    private String pathOrigemOriginal = "";
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */

        private String pathOrigemOld = "";
        private ArrayList<String> filesNamesOld;

        @Override
        public Void doInBackground() {
            deleteDir(new File(jTextField1.getText() + "//data-default"));
            deleteDir(new File(jTextField1.getText() + "//data-percentiles"));
            deleteDir(new File(jTextField1.getText() + "//data-compliance"));
            jTable1.removeAll();
            jTextArea1.setText("Log process:\n");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setNumRows(0);
            pathOrigemOriginal = pathOrigem;
            pathOrigemOld = pathOrigem;
            filesNamesOld = new ArrayList();
            filesNamesOld.addAll(filesNames);
            filesNamesOriginal= new ArrayList();
            filesNamesOriginal.addAll(filesNames);
            jButton7.setEnabled(false);
            jButton1.setEnabled(false);
            jButton4.setEnabled(false);
            jrbCsv.setEnabled(false);
            jrbXml.setEnabled(false);
            if (jrbXml.isSelected()) {
                int progress = 0;
                CGAllProjectsForXML gap = new CGAllProjectsForXML(filesNames, pathOrigem, jTextField1.getText());
                setProgress(0);
                //filtra nomes de arquivos 
                //if (jCheckBox2.isSelected()) {
                gap.applyFilesNamesFilter("test");
                //}
                int qtdProj = gap.applyProjectsNamesList(), cont = 0;
                jTextArea1.append("Number of identify projects: " + qtdProj + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                ArrayList<ArrayList<String>> listProjectsFilesNames = gap.getListProjectsFilesNames();
                for (int i = 0; i < qtdProj; i++) {
                    ArrayList<String> x = listProjectsFilesNames.get(i);
                    System.out.println("Project in course: " + x.get(0) + ", *_" + i + "_");
                    jTextArea1.append("Project in course: " + x.get(0) + "\n");
                    jTextArea1.requestFocus();
                    jTextArea1.setCaretPosition(jTextArea1.getText().length());
                    gap.readXmlProject(x, true);
                    int progresso = Math.round((float) 100 / qtdProj) * i;
                    if (progresso >= 100) {
                        progresso = 99;
                    }
                    setProgress(progresso);
                }
                jTextArea1.append("CSV-default generated.\n");


                try {
                    Thread.sleep(1000);
                } catch (Exception x) {
                }

                pathOrigemOld = jTextField1.getText() + "//data-default";
                pathOrigem = jTextField1.getText() + "//data-default";
                // gerar a lista de arquivos .csv default na coleção filenames**************
                File file = new File(jTextField1.getText() + "//data-default//");
                File afile[] = file.listFiles();
                int i = 0;
                filesNames.removeAll(filesNames);
                filesNames.clear();
                filesNamesOld.removeAll(filesNamesOld);
                filesNamesOld.clear();
                for (int j = afile.length; i < j; i++) {
                    File arquivos = afile[i];
                    System.out.println(arquivos.getName());
                    filesNames.add(arquivos.getName());
                    filesNamesOld.add(arquivos.getName());
                }
            }


            int progress = 0;
            CGAllMetricsPercentiles gamp = new CGAllMetricsPercentiles();
            setProgress(0);
            filesNames = gamp.getNamesFilter("_mtt", filesNames);
            ArrayList<String> metricNames = gamp.getMetricasEmArquivos(pathOrigem, filesNames, jrbCsv.isSelected());
            int qtdMetrics = metricNames.size(), cont = 0;
            jTextArea1.append("Number of identify metrics: " + qtdMetrics + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            for (String x : metricNames) {
                jTextArea1.append("Metric in course: " + x + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                gamp.geraPercentis(x, pathOrigem, jTextField1.getText(), filesNames, jrbCsv.isSelected());
                //setProgress(Math.round((float) 100 / qtdMetrics) * ++cont);
                int progresso = Math.round((float) 100 / qtdMetrics) * cont++;
                if (progresso >= 100) {
                    progresso = 99;
                }
                setProgress(progresso);
            }
            jTextArea1.append("Percentile generated.\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            try {
                Thread.sleep(2000);
            } catch (Exception x) {
            }
            // gerar a lista de arquivos .csv percentile na coleção filenames**************
            File file = new File(jTextField1.getText() + "//data-percentiles//");
            File afile[] = file.listFiles();
            int i = 0;
            filesNames.removeAll(filesNames);
            filesNames.clear();
            for (int j = afile.length; i < j; i++) {
                filesNames.add(afile[i].getName());
            }

            CGAllFilesComplianceAll gafc = new CGAllFilesComplianceAll();
            setProgress(0);
            int qtd = filesNames.size();
            cont = 0;
            jTextArea1.append("Number of files: " + qtd + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            for (String x : filesNames) {
                jTextArea1.append(" File in course: " + x + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                gafc.geraCompilances(jTextField1.getText() + "//data-percentiles", jTextField1.getText(), x, jSlider2.getValue(), jSlider1.getValue(), 0, 0);
                //setProgress(Math.round((float) 100 / qtd) * ++cont);
                int progresso = Math.round((float) 100 / qtd) * cont++;
                if (progresso >= 100) {
                    progresso = 99;
                }
                setProgress(progresso);
            }
            jTextArea1.append("Compliance generated.\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());

            // gerar a lista de arquivos .csv weigth na coleção filenames**************
            File dir = new File(jTextField1.getText() + "//data-compliance//");
            File files[] = dir.listFiles();
            i = 0;
            filesNames.removeAll(filesNames);
            filesNames.clear();
            for (int j = files.length; i < j; i++) {
                filesNames.add(files[i].getName());
            }
            filesNames = getNamesFilter("weigth", ".csv", filesNames);

            jTextArea1.append("Final Result...\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            CGAllFInalResults gafr = new CGAllFInalResults();
            ArrayList<FinalResult> fr = gafr.geraFinalResults(jTextField1.getText() + "//data-compliance", filesNames);
            jTable1.removeAll();
            dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setNumRows(0);
            String caminhoDefault = "";
            ArrayList<String> arquivos = null;

            ArrayList<StringBuffer> outlayers = null;
            outlayers = gafr.geraOutlayers(pathOrigemOld, filesNamesOld, fr, jrbCsv.isSelected());


            for (i = 0; i < fr.size(); i++) {
                dtm.addRow(new Object[]{fr.get(i).getMetrica(), fr.get(i).getPercentil() + "%", fr.get(i).getK() + "", outlayers.get(i).toString()});
            }


            //volta tudo
            filesNames.removeAll(filesNames);
            filesNames.clear();
            filesNames.addAll(filesNamesOriginal);
            pathOrigem = pathOrigemOriginal;
            
            return null;
        }

        public ArrayList<String> getNamesFilter(String subStringPrefix, String subStringSufix, ArrayList<String> listFilesNames) {
            System.out.println("Lista de arquivos desprezados");
            ArrayList<String> saida = new ArrayList();
            for (String x : listFilesNames) {
                if (x.startsWith(subStringPrefix) && x.endsWith(subStringSufix)) {
                    saida.add(x);
                } else {
                    System.out.println(x);
                }
            }

            System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
            return saida;
        }
        /*
         * Executed in event dispatching thread
         */

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Finish!");
            setProgress(100);
            jTextArea1.append("\nFinish generate.");
            jButton7.setEnabled(true);
            jButton1.setEnabled(true);
            jButton4.setEnabled(true);
            jrbCsv.setEnabled(true);
            jrbXml.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jrbCsv = new javax.swing.JRadioButton();
        jrbXml = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jSlider2 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MetricTool");
        setPreferredSize(new java.awt.Dimension(800, 600));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "/home/fernando/exemplo1.xml" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jTextField1.setEditable(false);
        jTextField1.setText("/home/fernando/");

        jButton1.setText("Select Files");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Select Folder for write");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setToolTipText("");

        buttonGroup1.add(jrbCsv);
        jrbCsv.setText("csv");
        jrbCsv.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jrbCsvStateChanged(evt);
            }
        });
        jrbCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbCsvActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbXml);
        jrbXml.setSelected(true);
        jrbXml.setText("xml");
        jrbXml.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jrbXmlStateChanged(evt);
            }
        });
        jrbXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbXmlActionPerformed(evt);
            }
        });

        jLabel4.setText("Input Files:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(31, 31, 31)
                .addComponent(jrbXml)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jrbCsv)
                .addGap(24, 24, 24))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jrbXml)
                    .addComponent(jrbCsv))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSlider1.setMajorTickSpacing(5);
        jSlider1.setMinimum(5);
        jSlider1.setMinorTickSpacing(5);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setToolTipText("Percentile");
        jSlider1.setValue(90);
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tail Percentile for Median"));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Log steps:");
        jScrollPane2.setViewportView(jTextArea1);

        jSlider2.setMajorTickSpacing(5);
        jSlider2.setMinimum(5);
        jSlider2.setMinorTickSpacing(5);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        jSlider2.setToolTipText("Percentile");
        jSlider2.setValue(90);
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder("MIN"));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jLabel2.setText("90");

        jButton7.setText("Run");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Metric", "Percentile%", "k", "Outlayers"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        jMenu1.setText("Precess");

        jMenu3.setText("Genarators");

        jMenuItem1.setText("XML to .csv(default)");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText(".csf(defalut) to .csv(Percentiles)");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem7.setText("All .csv(Percentile) to .csv(Compliance)");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setText("FinalResults");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem9.setText("Generate Graphics");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenu1.add(jMenu3);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("About");

        jMenuItem4.setText("Help");
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Credits");
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTextField1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // Agora o diretório está vazio, restando apenas deletá-lo.
        return dir.delete();
    }
//    Leia mais em
  //  : Veja como deletar um diretório http

//    ://www.devmedia.com.br/veja-como-deletar-um-diretorio/1434#ixzz2lyctiexk

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JDialog jd = new JDialog();
        jd.setModal(true);
        jd.setBounds(gapx.getBounds());
        jd.setTitle("XML to CSV (Default)");
        jd.setContentPane(gapx.getContentPane());
        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JDialog jd = new JDialog(gmapc);
        jd.setBounds(gmapc.getBounds());
        jd.setModal(true);
        jd.setTitle("CSV (Default) to CSV (Percentile)");
        jd.setContentPane(gmapc.getContentPane());
        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        JDialog jd = new JDialog(gmapcca);
        jd.setBounds(gmapcca.getBounds());
        jd.setModal(true);
        jd.setTitle("CSV (Percentile) to CSV(Compilance) All");
        jd.setContentPane(gmapcca.getContentPane());
        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        JDialog jd = new JDialog(gmapcfr);
        jd.setBounds(gmapcfr.getBounds());
        jd.setModal(true);
        jd.setTitle("CSV (Compliance Rate Penality) to Final Result");
        jd.setContentPane(gmapcfr.getContentPane());
        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        JDialog jd = new JDialog(gmag);
        jd.setBounds(gmag.getBounds());
        jd.setModal(true);
        jd.setTitle("Generate data graphics and graphics");
        jd.setContentPane(gmag.getContentPane());
        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    //alterado
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser arquivo = new JFileChooser();
        arquivo.setMultiSelectionEnabled(true);
        if (jrbCsv.isSelected()) {
            arquivo.setFileFilter(new javax.swing.filechooser.FileFilter() {
                //Filtro, converte as letras em minúsculas antes de comparar
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
                }
                //Texto que será exibido para o usuário

                public String getDescription() {
                    return "Files (.csv)";
                }
            });
        } /*else if (jrbXml.isSelected()) { //se for selecionado um arquivo xml
         arquivo.setFileFilter(new javax.swing.filechooser.FileFilter() {
         //Filtro, converte as letras em minúsculas antes de comparar
         public boolean accept(File f) {
         return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
         }
         //Texto que será exibido para o usuário

         public String getDescription() {
         return "Files (.xml)";
         }
         });
         }*/

        int retorno = arquivo.showOpenDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File[] files = arquivo.getSelectedFiles();
            filesNames = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                filesNames.add(files[i].getName());
            }
            pathOrigem = files[0].getParent();
            jList1.setListData(filesNames.toArray());
        } else {
            JOptionPane.showMessageDialog(null, "Invalid File");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser arquivo = new JFileChooser();
        arquivo.setMultiSelectionEnabled(false);
        arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retorno = arquivo.showOpenDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            jTextField1.setText(arquivo.getSelectedFile().getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(null, "Inválid Folder");
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            jProgressBar1.setValue(progress);
        }
    }
    private void jrbXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbXmlActionPerformed
       jList1.removeAll();
       jList1.setListData(new Vector());
    }//GEN-LAST:event_jrbXmlActionPerformed

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        jLabel2.setText(jSlider2.getValue() + ""); // TODO add your handling code here:
    }//GEN-LAST:event_jSlider2StateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jProgressBar1.setValue(0);
        Task task = new Task();
        task.addPropertyChangeListener(this);//eu coloquei esse aqui
        task.execute();

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jrbXmlStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jrbXmlStateChanged
        //jList1.removeAll();
        //jList1.setListData(new Vector());
    }//GEN-LAST:event_jrbXmlStateChanged

    private void jrbCsvStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jrbCsvStateChanged
        //jList1.removeAll();
        //jList1.setListData(new Vector());
    }//GEN-LAST:event_jrbCsvStateChanged

    private void jrbCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbCsvActionPerformed
        jList1.removeAll();
        jList1.setListData(new Vector());
    }//GEN-LAST:event_jrbCsvActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MetricTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MetricTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MetricTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MetricTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MetricTool().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JRadioButton jrbCsv;
    private javax.swing.JRadioButton jrbXml;
    // End of variables declaration//GEN-END:variables
}
