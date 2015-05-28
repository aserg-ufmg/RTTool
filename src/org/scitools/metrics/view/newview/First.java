package org.scitools.metrics.view.newview;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.scitools.metrics.control.CGAllFInalCharts;
import org.scitools.metrics.control.CGAllFInalResults;
import org.scitools.metrics.control.CGAllFilesComplianceAll;
import org.scitools.metrics.control.CGAllMetricsPercentiles;
import org.scitools.metrics.control.CGAllProjectsForXML;
import org.scitools.metrics.model.beans.*;
import org.scitools.metrics.model.util.ManipulaDir;
import org.scitools.metrics.model.util.RWArquivoNew;
import org.scitools.metrics.view.util.Chart;

/**
 *
 * @author fernando
 */
public class First extends javax.swing.JFrame implements PropertyChangeListener {

    /**
     * Creates new form MetricTool
     */
    public First() {
        initComponents();
    }
    private ArrayList<String> filesNames;
    private ArrayList<String> filesNamesOriginal;
    private String pathOrigem = "";
    private String pathOrigemOriginal = "";
    private ArrayList<String> metricNamesGlobal;
    private String pathOrigemOld = "";
    private ArrayList<String> filesNamesOld;
    private ArrayList<Linha> linhas;
    private ArrayList<FinalResult> fr;

    private long tempoProcessamento1;
    private long tempoProcessamento2;
    private long tempoProcessamento3;

    class Task1 extends SwingWorker<Void, Void> {

        private long tempoInicial;
        private long tempoFinal;

        @Override
        public Void doInBackground() {

            deleteDir(new File(jTextField1.getText() + "//data-default"));
            deleteDir(new File(jTextField1.getText() + "//data-percentiles"));
            deleteDir(new File(jTextField1.getText() + "//data-compliance"));

            pathOrigemOriginal = pathOrigem;
            pathOrigemOld = pathOrigem;
            filesNamesOld = new ArrayList();
            filesNamesOld.addAll(filesNames);
            filesNamesOriginal = new ArrayList();
            filesNamesOriginal.addAll(filesNames);
            tempoInicial = System.currentTimeMillis();
            if (jrbXml.isSelected()) {
                int progress = 0;
                CGAllProjectsForXML gap = new CGAllProjectsForXML(filesNames, pathOrigem, jTextField1.getText());
                setProgress(0);
                //filtra nomes de arquivos 
                gap.applyFilesNamesFilter("test");
                int qtdProj = gap.applyProjectsNamesList(), cont = 0;
                jTextArea1.append("Number of identify projects: " + qtdProj + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                ArrayList<ArrayList<String>> listProjectsFilesNames = gap.getListProjectsFilesNames();
                for (int i = 0; i < qtdProj; i++) {
                    ArrayList<String> x = listProjectsFilesNames.get(i);
                    //System.out.println("Project in course: " + x.get(0) + ", *_" + i + "_");
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
                    //System.out.println(arquivos.getName());
                    filesNames.add(arquivos.getName());
                    filesNamesOld.add(arquivos.getName());
                }
                System.gc();
            }
            int progress = 0;
            CGAllMetricsPercentiles gamp = new CGAllMetricsPercentiles();
            setProgress(0);
            filesNames = gamp.getNamesFilter("_mtt", filesNames);
            metricNamesGlobal = gamp.getMetricasEmArquivos(pathOrigem, filesNames, jrbCsv.isSelected());
            jList2.setListData(metricNamesGlobal.toArray());
            for (String x : metricNamesGlobal) {
                listaA.add(x);
            }
            //jLabel1.setText("Total Metrics Found: " + metricNamesGlobal.size() + ".");
            jLabel15.setText(metricNamesGlobal.size() + ".");
            jLabel16.setText("0.");

            jTextArea1.append("Number of selected files: " + filesNamesOriginal.size() + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());

            return null;
        }

        @Override
        public void done() {
            tempoFinal = System.currentTimeMillis();
            tempoProcessamento1 = tempoFinal - tempoInicial;
            //jTextArea1.append("Runtime: " + tempoProcessamento1 + " milliseconds\n");
            //jTextArea1.requestFocus();
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());

            Toolkit.getDefaultToolkit().beep();
            setProgress(100);
            //JOptionPane.showMessageDialog(null, "Next step.");
            setProgress(0);
            System.gc();
            jTabbedPane1.setSelectedIndex(2);
            jButton3.setEnabled(true);
            jButton5.setEnabled(true);
            jButton6.setEnabled(true);
            jButton1.setEnabled(true);
            jButton4.setEnabled(true);
        }
    }

    class Task2 extends SwingWorker<Void, Void> {
        /* módulo criado para gerar somente os percentils]
         * 
         */

        private String pathOrigemOldX = "";
        private String pathOrigemOriginalX = "";
        private ArrayList<String> filesNamesX;
        private ArrayList<String> filesNamesPercentiles;
        //private ArrayList<String> filesNamesOld;
        private long tempoInicial;
        private long tempoFinal;

        @Override
        public Void doInBackground() {

            jButton8.setEnabled(false);
            jButton9.setEnabled(false);
            jButton10.setEnabled(false);
            jButton11.setEnabled(false);
            jButton12.setEnabled(false);
            jButton13.setEnabled(false);
            jButton14.setEnabled(false);
            jList2.setEnabled(false);
            jList3.setEnabled(false);

            //deleteDir(new File(jTextField1.getText() + "//data-default")); //verificação marco túlio
            deleteDir(new File(jTextField1.getText() + "//data-percentiles"));
            deleteDir(new File(jTextField1.getText() + "//data-compliance"));

            pathOrigemOriginalX = pathOrigem;
            pathOrigemOldX = pathOrigem;
            tempoInicial = System.currentTimeMillis();

            int progress = 0;
            CGAllMetricsPercentiles gamp = new CGAllMetricsPercentiles();
            setProgress(0);
            this.filesNamesX = gamp.getNamesFilter("_mtt", filesNames);
            //ArrayList<String> metricNames = gamp.getMetricasEmArquivos(pathOrigem, filesNames, jrbCsv.isSelected());
            int qtdMetrics = listaB.size(), cont = 0;

            jTextArea1.append("Number of identify metrics: " + qtdMetrics + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            
            for (String x : listaB) {
                x = x.replaceAll("\"", "");
                jTextArea1.append("Computing relative threshold for: " + x + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                gamp.geraPercentis(x, pathOrigem, jTextField1.getText(), filesNamesX, jrbCsv.isSelected());
                //setProgress(Math.round((float) 100 / qtdMetrics) * ++cont);
                int progresso = Math.round((float) 100 / qtdMetrics) * cont++;
                if (progresso >= 100) {
                    progresso = 99;
                }
                setProgress(progresso);
            }
            //jTextArea1.append("Quantil funtion generated.\n");
            //jTextArea1.requestFocus();
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());
            try {
                Thread.sleep(2000);
            } catch (Exception x) {
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            tempoFinal = System.currentTimeMillis();
            tempoProcessamento2 = tempoFinal - tempoInicial;
            //jTextArea1.append("Runtime: " + tempoProcessamento2 + " milliseconds\n");
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());

            setProgress(100);
            jButton8.setEnabled(true);
            jButton9.setEnabled(true);
            jButton10.setEnabled(true);
            jButton11.setEnabled(true);
            jButton12.setEnabled(true);
            jButton13.setEnabled(true);
            jButton14.setEnabled(true);
            jList2.setEnabled(true);
            jList3.setEnabled(true);

            jButton7ActionPerformed(null);
            System.gc();
        }
    }

    class Task3 extends SwingWorker<Void, Void> {

        private String pathOrigemOldX = "";
        private String pathOrigemOriginalX = "";
        private ArrayList<String> filesNamesX = new ArrayList();
        private ArrayList<String> filesNamesPercentiles;
        private long tempoInicial;
        private long tempoFinal;

        @Override
        public Void doInBackground() {

            deleteDir(new File(jTextField1.getText() + "//data-compliance"));

            pathOrigemOriginalX = pathOrigem;
            pathOrigemOldX = pathOrigem;

            jTable1.removeAll();

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setNumRows(0);
            //int progress = 0;
            setProgress(0);
            tempoInicial = System.currentTimeMillis();
            int qtdMetrics = listaB.size(), cont = 0;

            // gerar a lista de arquivos .csv percentile na coleção filenames**************
            File file = new File(jTextField1.getText() + "//data-percentiles//");
            File afile[] = file.listFiles();
            int i = 0;
            for (int j = afile.length; i < j; i++) {
                filesNamesX.add(afile[i].getName());
            }
            CGAllFilesComplianceAll gafc = new CGAllFilesComplianceAll();
            setProgress(0);
            int qtd = filesNamesX.size();
            cont = 0;
            jTextArea1.append("Number of files: " + qtd + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            for (String x : filesNamesX) {
                jTextArea1.append(" File in course: " + x + "\n");
                jTextArea1.requestFocus();
                jTextArea1.setCaretPosition(jTextArea1.getText().length());
                gafc.geraCompilances(jTextField1.getText() + "//data-percentiles", jTextField1.getText(), x, jSlider2.getValue(), jSlider1.getValue(), 0, 0);
                int progresso = Math.round((float) 100 / qtd) * cont++;
                if (progresso >= 100) {
                    progresso = 99;
                }
                setProgress(progresso);
            }
            //jTextArea1.append("Compliance generated.\n");
            //jTextArea1.requestFocus();
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());

            // gerar a lista de arquivos .csv weigth na coleção filenames**************
            File dir = new File(jTextField1.getText() + "//data-compliance//");
            File files[] = dir.listFiles();
            i = 0;
            filesNamesX.removeAll(filesNamesX);
            filesNamesX.clear();
            for (int j = files.length; i < j; i++) {
                filesNamesX.add(files[i].getName());
            }
            filesNamesX = getNamesFilter("weigth", ".csv", filesNamesX);

            //jTextArea1.append("Final Result...\n");
            //jTextArea1.requestFocus();
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());
            CGAllFInalResults gafr = new CGAllFInalResults();
            fr = gafr.geraFinalResults(jTextField1.getText() + "//data-compliance", filesNamesX);
            jTable1.removeAll();
            dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setNumRows(0);
            String caminhoDefault = "";
            ArrayList<String> arquivos = null;

            //jTextArea1.append("Generate Outlier...\n");
            //jTextArea1.requestFocus();
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());
            ArrayList<StringBuffer> outlayers = null;
            setProgress(50);
            // gerar a lista de arquivos .csv percentile na coleção filenames**************
            file = new File(jTextField1.getText() + "//data-percentiles//");
            afile = file.listFiles();
            i = 0;
            filesNamesX.removeAll(filesNamesX);
            filesNamesX.clear();
            for (int j = afile.length; i < j; i++) {
                filesNamesX.add(afile[i].getName());
            }
            outlayers = gafr.geraOutlayersNew(jTextField1.getText() + "//data-percentiles", filesNamesX, fr);
            setProgress(100);

            for (i = 0; i < fr.size(); i++) {
                int g;
                if (outlayers.get(i).toString().split(";")[0].isEmpty()) {
                    g = 0;
                } else {
                    g = outlayers.get(i).toString().split(";").length;
                }
                dtm.addRow(new Object[]{fr.get(i).getMetrica(), fr.get(i).getPercentil() + "%", fr.get(i).getK() + "", g, outlayers.get(i).toString().replace(";", "; ")});
            }

            //volta tudo
            filesNamesX.removeAll(filesNamesX);
            filesNamesX.clear();
            filesNamesX.addAll(filesNamesOriginal);

            //Grava automáticamente o resultado final, assim que ele é obtido
            StringBuffer saida = new StringBuffer();
            saida.append("metric,percentile,k,outliers\n");
            for (int iz = 0; iz < dtm.getRowCount(); iz++) {
                saida.append(dtm.getValueAt(iz, 0));
                saida.append(",");
                saida.append(dtm.getValueAt(iz, 1));
                saida.append(",");
                saida.append(dtm.getValueAt(iz, 2));
                saida.append(",");
                saida.append(dtm.getValueAt(iz, 3));
                saida.append(",");
                saida.append(dtm.getValueAt(iz, 4));
                saida.append("\n");
            }
            RWArquivoNew rw = new RWArquivoNew();
            rw.setUrl(jTextField1.getText() + "//FinalResult.csv");
            try {
                rw.criaArq();
                rw.escreveLinha(saida);
                rw.fechaArq();
            } catch (IOException x) {
                System.err.println("Erro na gravação");
                JOptionPane.showMessageDialog(null, "Error of file.");
            } catch (Exception x) {
                JOptionPane.showMessageDialog(null, "Error of file.");
                //x.printStackTrace();
            }

            jTextArea1.append("Final results saved in output directory: " + jTextField1.getText() + "\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());

            DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
            DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
            DefaultTableCellRenderer direita = new DefaultTableCellRenderer();

            esquerda.setHorizontalAlignment(SwingConstants.LEFT);
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            direita.setHorizontalAlignment(SwingConstants.RIGHT);

            //Final results saved in output directory:
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(435);
            ((DefaultTableCellRenderer) jTable1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            jTable1.getColumnModel().getColumn(0).setCellRenderer(centralizado);
            jTable1.getColumnModel().getColumn(1).setCellRenderer(centralizado);
            jTable1.getColumnModel().getColumn(3).setCellRenderer(centralizado);
            jTable1.getTableHeader().getTable().getColumnModel().getColumn(2).setCellRenderer(direita);

            dtm.fireTableDataChanged();
            return null;
        }

        public ArrayList<String> getNamesFilter(String subStringPrefix, String subStringSufix, ArrayList<String> listFilesNames) {
            //System.out.println("Lista de arquivos desprezados");
            ArrayList<String> saida = new ArrayList();
            for (String x : listFilesNames) {
                if (x.startsWith(subStringPrefix) && x.endsWith(subStringSufix)) {
                    saida.add(x);
                } else {
                    //System.out.println(x);
                }
            }

            //System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
            return saida;
        }
        /*
         * Executed in event dispatching thread
         */

        @Override
        public void done() {
            tempoFinal = System.currentTimeMillis();
            tempoProcessamento3 = tempoFinal - tempoInicial;
            //jTextArea1.append("Runtime: " + tempoProcessamento3 + " milliseconds\n");
            //jTextArea1.setCaretPosition(jTextArea1.getText().length());

            jTextArea1.append("Runtime: " + (tempoProcessamento1 + tempoProcessamento2 + tempoProcessamento3) + " milliseconds\n");
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
            setProgress(100);
            jTextArea1.append("\nConcluded.");
            jButton7.setEnabled(true);
            jButton16.setEnabled(true);
            jButton15.setEnabled(true);
            jrbCsv.setEnabled(true);
            jrbXml.setEnabled(true);
            jTabbedPane1.setSelectedIndex(4);
            System.gc();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jrbCsv = new javax.swing.JRadioButton();
        jrbXml = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jButton7 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jButton21 = new javax.swing.JButton();
        jSlider3 = new javax.swing.JSlider();
        jButton22 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RTTool");
        setResizable(false);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setEnabled(false);
        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.setRequestFocusEnabled(false);
        jTabbedPane1.setVerifyInputWhenFocusTarget(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setToolTipText("");

        buttonGroup1.add(jrbCsv);
        jrbCsv.setSelected(true);
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jrbXml)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jrbCsv)
                .addGap(24, 24, 24))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbXml)
                    .addComponent(jrbCsv))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/next.png"))); // NOI18N
        jButton2.setText("Start");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Select input format:");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/help.png"))); // NOI18N
        jLabel8.setToolTipText("Select format of input files");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(13, 13, 13)
                .addComponent(jButton2)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Select Input Format", jPanel1);

        jScrollPane1.setViewportView(jList1);

        jTextField1.setEditable(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/SelectetFiles.png"))); // NOI18N
        jButton1.setText("Select Input Files");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/SelectOutputFOlder.png"))); // NOI18N
        jButton4.setText("Select Output Folder");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/next.png"))); // NOI18N
        jButton3.setText("Next");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/back.png"))); // NOI18N
        jButton5.setText("Back");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/cancel.png"))); // NOI18N
        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/help.png"))); // NOI18N
        jLabel11.setToolTipText("Selection of input files for processing");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/help.png"))); // NOI18N
        jLabel12.setToolTipText("Select directory to write files processed");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton5)
                            .addComponent(jButton6))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Select Input Files", jPanel2);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/next.png"))); // NOI18N
        jButton8.setText("Next");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/back.png"))); // NOI18N
        jButton9.setText("Back");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/cancel.png"))); // NOI18N
        jButton10.setText("Cancel");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(jList2);

        jButton11.setText(">");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText(">>");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("<<");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("<");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(jList3);

        jLabel3.setText("Available Metrics");
        jLabel3.setToolTipText("");

        jLabel4.setText("Selected Metrics");

        jLabel15.setText("0");

        jLabel16.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)
                        .addGap(0, 121, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addGap(11, 11, 11))
        );

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/help.png"))); // NOI18N
        jLabel9.setToolTipText("Select metrics for precessing");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(jLabel9)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Select Metrics", jPanel3);

        jPanel4.setEnabled(false);
        jPanel4.setFocusable(false);
        jPanel4.setRequestFocusEnabled(false);
        jPanel4.setVerifyInputWhenFocusTarget(false);

        jSlider1.setMajorTickSpacing(5);
        jSlider1.setMinimum(5);
        jSlider1.setMinorTickSpacing(5);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setToolTipText("");
        jSlider1.setValue(90);
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tail", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 10))); // NOI18N

        jSlider2.setMajorTickSpacing(5);
        jSlider2.setMinimum(5);
        jSlider2.setMinorTickSpacing(5);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        jSlider2.setToolTipText("Percentile");
        jSlider2.setValue(90);
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MIN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 10))); // NOI18N
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/run.png"))); // NOI18N
        jButton7.setText("Run");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/cancel.png"))); // NOI18N
        jButton15.setText("Cancel");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/back.png"))); // NOI18N
        jButton16.setText("Back");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/help.png"))); // NOI18N
        jLabel10.setToolTipText("setting properties for processing");

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/Graph.png"))); // NOI18N
        jButton24.setText("Graphics p%");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(297, 679, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addComponent(jSlider2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton24)
                .addGap(130, 130, 130)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 78, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jButton15)
                    .addComponent(jButton7)
                    .addComponent(jButton24))
                .addContainerGap())
        );

        jTabbedPane1.addTab("", null, jPanel4, "Choose Input Parameters");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Metric", "p", "k", "# Outliers", "Systems"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane3.setViewportView(jTable1);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/reset.png"))); // NOI18N
        jButton17.setText("Reset");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/Graph.png"))); // NOI18N
        jButton19.setText("View Graphics");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/save.png"))); // NOI18N
        jButton20.setText("Save as");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jButton20.setVisible(false);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/back.png"))); // NOI18N
        jButton23.setText("Back");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton20)
                .addGap(148, 148, 148)
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton19)
                    .addComponent(jButton20)
                    .addComponent(jButton23))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Final Results", jPanel8);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/reset.png"))); // NOI18N
        jButton18.setText("Reset");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jScrollPane6.setViewportView(jTabbedPane2);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/save.png"))); // NOI18N
        jButton21.setText("Save");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jSlider3.setMajorTickSpacing(5);
        jSlider3.setMinimum(5);
        jSlider3.setMinorTickSpacing(5);
        jSlider3.setOrientation(javax.swing.JSlider.VERTICAL);
        jSlider3.setPaintLabels(true);
        jSlider3.setPaintTicks(true);
        jSlider3.setSnapToTicks(true);
        jSlider3.setToolTipText("Percentile");
        jSlider3.setValue(70);
        jSlider3.setBorder(javax.swing.BorderFactory.createTitledBorder("p%"));
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/imgs/back.png"))); // NOI18N
        jButton22.setText("Back");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Compliance Rate Penalty", "Compliance Rate", "Quantil Function" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton21)
                        .addGap(275, 275, 275)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSlider3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton22)
                    .addComponent(jButton21)
                    .addComponent(jButton18))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Graphics", jPanel9);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/logoRT.jpg"))); // NOI18N
        jLabel5.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/logoRT.jpg"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Relative Thresholds Tool");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scitools/metrics/view/newview/logoG.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel5))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addContainerGap())))
        );

        jMenu1.setText("Precess");

        jMenu3.setText("Genarators");
        jMenu3.setEnabled(false);

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

        jMenu3.setVisible(false);

        jMenu1.add(jMenu3);

        jMenuItem6.setText("Batch Process");
        jMenuItem6.setEnabled(false);
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenuItem6.setVisible(false);
        jMenu1.add(jMenuItem6);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenu1.setVisible(false);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("About");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem4.setText("Description");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTabbedPane1)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
//        JDialog jd = new JDialog();
//        jd.setModal(true);
//        jd.setBounds(gapx.getBounds());
//        jd.setTitle("XML to CSV (Default)");
//        jd.setContentPane(gapx.getContentPane());
//        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
//        JDialog jd = new JDialog(gmapc);
//        jd.setBounds(gmapc.getBounds());
//        jd.setModal(true);
//        jd.setTitle("CSV (Default) to CSV (Percentile)");
//        jd.setContentPane(gmapc.getContentPane());
//        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
//        JDialog jd = new JDialog(gmapcca);
//        jd.setBounds(gmapcca.getBounds());
//        jd.setModal(true);
//        jd.setTitle("CSV (Percentile) to CSV(Compilance) All");
//        jd.setContentPane(gmapcca.getContentPane());
//        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
//        JDialog jd = new JDialog(gmapcfr);
//        jd.setBounds(gmapcfr.getBounds());
//        jd.setModal(true);
//        jd.setTitle("CSV (Compliance Rate Penality) to Final Result");
//        jd.setContentPane(gmapcfr.getContentPane());
//        jd.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
//        JDialog jd = new JDialog(gmag);
//        jd.setBounds(gmag.getBounds());
//        jd.setModal(true);
//        jd.setTitle("Generate data graphics and graphics");
//        jd.setContentPane(gmag.getContentPane());
//        jd.setVisible(true);
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
        }

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
        //jLabel2.setText(jSlider2.getValue() + ""); // TODO add your handling code here:
    }//GEN-LAST:event_jSlider2StateChanged

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //System.out.println("java -jar -Xincgc -Xmx1024M " + System.getProperty("user.dir") + "//RTTool.jar");
        jMenuItem6.setEnabled(false);
        jTabbedPane1.setSelectedIndex(1);
        if (jrbCsv.isSelected()) {
            jTextArea1.append("\nCSV format selected\n");
        } else {
            jTextArea1.append("\nXML format selected\n");
        }
        jTextArea1.requestFocus();
        jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTextField1.getText().isEmpty() || jTextField1.getText().equalsIgnoreCase("")) {
            jTextField1.setText(pathOrigem);
           // jTextArea1.append("Output directory is not selected:"+pathOrigem+"\n");
        } 
        
        jProgressBar1.setValue(0);
        listaA.clear();
        listaB.clear();

        jList2.setListData(new ArrayList().toArray());
        jList3.setListData(new ArrayList().toArray());
        jButton3.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jButton1.setEnabled(false);
        jButton4.setEnabled(false);
        Task1 task = new Task1();
        task.addPropertyChangeListener(this);//eu coloquei esse aqui
        task.execute();
        if (jrbCsv.isSelected()) {
            jTextArea1.append("Input files and output folder selected\n");
        } else {
            jTextArea1.append("XML Files analyseds\nSelcted input files and output directory\n");
        }
        jTextArea1.requestFocus();
        jTextArea1.setCaretPosition(jTextArea1.getText().length());

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (!listaB.isEmpty()) {
            //jTabbedPane1.setSelectedIndex(3);
            jTextArea1.append("Selected Metrics\n");
            for (String x : listaB) {
                jTextArea1.append(" | " + x);
            }
            jTextArea1.append(".\n");
            jTextArea1.requestFocus();
            jTextArea1.setCaretPosition(jTextArea1.getText().length());

            jProgressBar1.setValue(0);
            Task2 task = new Task2();
            task.addPropertyChangeListener(this);
            task.execute();

        } else {
            JOptionPane.showMessageDialog(null, "Select metrics!");
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jProgressBar1.setValue(0);
        Task3 task = new Task3();
        task.addPropertyChangeListener(this);
        task.execute();

        jButton7.setEnabled(false);
        jButton16.setEnabled(false);
        jButton15.setEnabled(false);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTextArea1.setText("Log process:\n");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTextArea1.setText("Log process:\n");
        jMenuItem6.setEnabled(true);
        System.gc();

        if (!jrbCsv.isSelected()) {
            try {

                Process p = Runtime.getRuntime().exec("java -jar -Xincgc -Xmx1512M " + System.getProperty("user.dir") + "//RTTool.jar");
                //p = Runtime.getRuntime().exec("dir");
                //p.getInputStream().;
            } catch (Exception l) {
                System.err.println(l);
//l.printStackTrace();
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTextArea1.setText("Log process:\n");
        jMenuItem6.setEnabled(true);
        System.gc();

        if (!jrbCsv.isSelected()) {
            try {

                Process p = Runtime.getRuntime().exec("java -jar -Xincgc -Xmx1512M " + System.getProperty("user.dir") + "//RTTool.jar");
                //p = Runtime.getRuntime().exec("dir");
                //p.getInputStream().;
            } catch (Exception l) {
                //l.printStackTrace();
                System.err.println(l);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTextArea1.setText("Log process:\n");
    }//GEN-LAST:event_jButton6ActionPerformed
    private ArrayList<String> listaA = new ArrayList(), listaB = new ArrayList();

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (!jList2.isSelectionEmpty()) {
            listaB.addAll(jList2.getSelectedValuesList());
            jList3.setListData(listaB.toArray());
            listaA.removeAll(jList2.getSelectedValuesList());
            jList2.setListData(listaA.toArray());
            //jLabel15.setText(listaA.size() + ".");
            jLabel16.setText(listaB.size() + ".");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (!metricNamesGlobal.isEmpty()) {
            listaB.clear();
            listaA.clear();
            for (String x : metricNamesGlobal) {
                listaB.add(x);
            }
            jList3.setListData(metricNamesGlobal.toArray());
            jList2.setListData(new ArrayList().toArray());
            //jLabel15.setText(listaA.size() + ".");
            jLabel16.setText(listaB.size() + ".");
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (!metricNamesGlobal.isEmpty()) {
            listaB.clear();
            listaA.clear();
            for (String x : metricNamesGlobal) {
                listaA.add(x);
            }
            jList2.setListData(metricNamesGlobal.toArray());
            jList3.setListData(new ArrayList().toArray());
            //jLabel15.setText(listaA.size() + ".");
            jLabel16.setText(listaB.size() + ".");

        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (!jList3.isSelectionEmpty()) {
            listaA.addAll(jList3.getSelectedValuesList());
            jList2.setListData(listaA.toArray());
            listaB.removeAll(jList3.getSelectedValuesList());
            jList3.setListData(listaB.toArray());
            //jLabel15.setText(listaA.size() + ".");
            jLabel16.setText(listaB.size() + ".");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jProgressBar1.setValue(0);
        jTextArea1.setText("Log process:\n");
        jMenuItem6.setEnabled(true);
        System.gc();

        if (!jrbCsv.isSelected()) {
            try {

                Process p = Runtime.getRuntime().exec("java -jar -Xincgc -Xmx1512M " + System.getProperty("user.dir") + "//RTTool.jar");
                //p = Runtime.getRuntime().exec("dir");
                //p.getInputStream().;
            } catch (Exception l) {
                //l.printStackTrace();
                System.err.println(l);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jButton17ActionPerformed
    private ArrayList<JFreeChart> graficos = new ArrayList();
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        jSlider3.setValue(70);

        jComboBox1ActionPerformed(evt);
        //saveGraphics(jTextField1.getText()+"//graphics");
    }//GEN-LAST:event_jButton19ActionPerformed

    private void geragraficosCRP() {
        graficos = new ArrayList();
        //jTextArea1.append("Chart generating...\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
        jProgressBar1.setValue(50);

        CGAllFInalCharts k = new CGAllFInalCharts();
        ArrayList<MetricaLinhas> mls = k.generateDataCRP(jTextField1.getText() + "//data-compliance", filesNames, jSlider3.getValue() / 5);
        int c = 0;
        for (MetricaLinhas ml : mls) {
            //testa se as métricas são iguais para que possa ser setado o k no gráfico
            for (FinalResult f : fr) {
                if (ml.getNomeMetrica().replaceAll("weigth_metric_", "").replaceAll(".csv", "").equalsIgnoreCase(f.getMetrica())) {
                    ml.setK(f.getK());
                }
            }
            boolean isCRP;
            if (jComboBox1.getSelectedIndex() == 0) {
                isCRP = true;
            } else {
                isCRP = false;
            }
            Chart demo = new Chart(ml.getK(), ml.getNomeMetrica().replaceAll("weigth_metric_", "").replaceAll(".csv", ""), 515, 225,
                    ml.getLinhas(), ml.getNomeMetrica().replaceAll("weigth_metric_", "").replaceAll(".csv", ""), "k", "Compliance Rate Penalty", isCRP, false);
            demo.pack();
            graficos.add(demo.getJFreechart());
            JPanel jp = new JPanel();
            jp.add(demo.getContentPane());
            jTabbedPane2.addTab(ml.getNomeMetrica().replaceAll("weigth_metric_", "").replaceAll(".csv", ""), jp);
        }
        jProgressBar1.setValue(100);
        //jTextArea1.append("Chart generated\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    private void geraGraficosCR() {
        graficos = new ArrayList();
        //jTextArea1.append("Chart generating...\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
        jTabbedPane1.setSelectedIndex(5);
        jProgressBar1.setValue(50);
        CGAllFInalCharts k = new CGAllFInalCharts();
        ArrayList<MetricaLinhas> mls = k.generateDataCR(jTextField1.getText() + "//data-compliance", filesNames, jSlider3.getValue() / 5);
        int c = 0;
        for (MetricaLinhas ml : mls) {
            //testa se as métricas são iguais para que possa ser setado o k no gráfico
            for (FinalResult f : fr) {
                if (ml.getNomeMetrica().replaceAll("crate_metric_", "").replaceAll(".csv", "").equalsIgnoreCase(f.getMetrica())) {
                    ml.setK(f.getK());
                }
            }
            boolean isCRP;
            if (jComboBox1.getSelectedIndex() == 0) {
                isCRP = true;
            } else {
                isCRP = false;
            }
            Chart demo = new Chart(ml.getK(), ml.getNomeMetrica().replaceAll("crate_metric_", "").replaceAll(".csv", ""), 515, 225,
                    ml.getLinhas(), ml.getNomeMetrica().replaceAll("crate_metric_", "").replaceAll(".csv", ""), "k", "Compliance Rate", isCRP, false);
            demo.pack();
            graficos.add(demo.getJFreechart());
            JPanel jp = new JPanel();
            jp.add(demo.getContentPane());
            jTabbedPane2.addTab(ml.getNomeMetrica().replaceAll("crate_metric_", "").replaceAll(".csv", ""), jp);
        }
        jProgressBar1.setValue(100);
        //jTextArea1.append("Chart generated\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    private void geraGraficosP() {
        graficos = new ArrayList();
        //jTextArea1.append("Chart generating...\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
        jTabbedPane1.setSelectedIndex(5);
        //jSlider3.setValue(70);
        jProgressBar1.setValue(50);
        CGAllFInalCharts k = new CGAllFInalCharts();
        ArrayList<String> nomesArquivosPercentiles = new ArrayList();
        //pega o nome dos aruqivos
        ManipulaDir.listar(new File(jTextField1.getText() + "//data-percentiles"), 0, nomesArquivosPercentiles);
        ArrayList<MetricaLinhas> mls = k.generateDataP(jTextField1.getText() + "//data-percentiles", nomesArquivosPercentiles);
        int c = 0;
        for (MetricaLinhas ml : mls) {
            //testa se as métricas são iguais para que possa ser setado o k no gráfico
            for (FinalResult f : fr) {
                if (ml.getNomeMetrica().replaceAll("metric_", "").replaceAll(".csv", "").equalsIgnoreCase(f.getMetrica())) {
                    ml.setK(f.getK());
                }
            }

            boolean isCRP;
            if (jComboBox1.getSelectedIndex() == 0) {
                isCRP = true;
            } else {
                isCRP = false;
            }
            Chart demo = new Chart(ml.getK(), ml.getNomeMetrica().replaceAll("metric_", "").replaceAll(".csv", ""), 515, 225,
                    ml.getLinhas(), ml.getNomeMetrica().replaceAll("metric_", "").replaceAll(".csv", ""), "p%", ml.getNomeMetrica().replaceAll("metric_", "").replaceAll(".csv", ""), isCRP, true);
            demo.pack();
            graficos.add(demo.getJFreechart());
            JPanel jp = new JPanel();
            jp.add(demo.getContentPane());
            jTabbedPane2.addTab(ml.getNomeMetrica().replaceAll("metric_", "").replaceAll(".csv", ""), jp);
        }
        jProgressBar1.setValue(100);
        //jTextArea1.append("Chart generated\n");
        //jTextArea1.requestFocus();
        //jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTabbedPane2.removeAll();
        jProgressBar1.setValue(0);
        jProgressBar1.setValue(0);
        jTextArea1.setText("Log process:\n");
        graficos.removeAll(graficos);
        jMenuItem6.setEnabled(true);
        if (!jrbCsv.isSelected()) {
            try {
                Process p = Runtime.getRuntime().exec("java -jar -Xincgc -Xmx1512M " + System.getProperty("user.dir") + "//RTTool.jar");
                System.exit(0);
            } catch (Exception l) {
                //l.printStackTrace();
                System.err.println(l);
            }
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        saveButtonFinalResults();

    }//GEN-LAST:event_jButton20ActionPerformed

    private void saveButtonFinalResults() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        StringBuffer saida = new StringBuffer();
        saida.append("metric,percentile,k,outliers\n");
        for (int i = 0; i < dtm.getRowCount(); i++) {
            saida.append(dtm.getValueAt(i, 0));
            saida.append(",");
            saida.append(dtm.getValueAt(i, 1));
            saida.append(",");
            saida.append(dtm.getValueAt(i, 2));
            saida.append(",");
            saida.append(dtm.getValueAt(i, 3));
            saida.append(",");
            saida.append(dtm.getValueAt(i, 4));
            saida.append("\n");
        }

        RWArquivoNew rw = new RWArquivoNew();
        JFileChooser arquivo = new JFileChooser();
        arquivo.setDialogType(JFileChooser.SAVE_DIALOG);
        arquivo.setMultiSelectionEnabled(false);
        arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retorno = arquivo.showSaveDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            rw.setUrl(arquivo.getSelectedFile().getAbsolutePath() + "//FinalResult.csv");

        } else {
            JOptionPane.showMessageDialog(null, "Inválid Folder");
        }
        try {
            rw.criaArq();
            rw.escreveLinha(saida);
            rw.fechaArq();
        } catch (IOException x) {
            System.err.println("Erro na gravação");
            JOptionPane.showMessageDialog(null, "Error of file.");
        } catch (Exception x) {
            JOptionPane.showMessageDialog(null, "Error of file.");
            //x.printStackTrace();

        }
    }


    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        String caminho = "";
        JFileChooser arquivo = new JFileChooser();
        arquivo.setDialogType(JFileChooser.SAVE_DIALOG);
        arquivo.setMultiSelectionEnabled(false);
        arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        arquivo.setCurrentDirectory(new File(jTextField1.getText()));
        int retorno = arquivo.showSaveDialog(null);

        if (retorno == JFileChooser.APPROVE_OPTION) {
            caminho = arquivo.getSelectedFile().getAbsolutePath();
            saveGraphics(caminho+"//graphics");
        } else {
           // JOptionPane.showMessageDialog(null, "Inválid Folder");
        }
        /*if (!caminho.isEmpty()) {
            for (JFreeChart g : graficos) {
                try {
                    ChartUtilities.saveChartAsJPEG(new File(caminho + "//" + jComboBox1.getSelectedItem().toString() + "-" + g.getTitle().getText() + ".jpg"), g, 800, 600);
                } catch (IOException x) {
                    System.err.println("Erro na gravação");
                    JOptionPane.showMessageDialog(null, "Error of file.");
                } catch (Exception x) {
                    JOptionPane.showMessageDialog(null, "Error of file.");
                    //x.printStackTrace();
                }
            }
        }*/
        
    }//GEN-LAST:event_jButton21ActionPerformed

    private void saveGraphics(String caminho){
        
        if (!caminho.isEmpty()) {
            ManipulaDir md = new ManipulaDir();
            if(!md.isDiretorio(caminho)){
                md.criaDiretorio(caminho);
            }else{
                deleteDir(new File(caminho));
                md.criaDiretorio(caminho);
            }
            for (JFreeChart g : graficos) {
                try {
                    ChartUtilities.saveChartAsJPEG(new File(caminho + "//" + jComboBox1.getSelectedItem().toString() + "-" + g.getTitle().getText() + ".jpg"), g, 800, 600);
                } catch (IOException x) {
                    System.err.println("Erro na gravação");
                    JOptionPane.showMessageDialog(null, "Error of file.");
                } catch (Exception x) {
                    JOptionPane.showMessageDialog(null, "Error of file.");
                    //x.printStackTrace();
                }
            }
        }
    }
    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        jTabbedPane1.setSelectedIndex(5);
        jComboBox1ActionPerformed(null);
    }//GEN-LAST:event_jSlider3StateChanged

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        /*JDialog jd = new JDialog();
         jd.setModal(true);
         MetricTool mt = new MetricTool();
         jd.setBounds(mt.getBounds());
         jd.setTitle("XML to CSV (Default)");

         jd.setContentPane(mt.getContentPane());
         jd.setVisible(true);*/
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        jTabbedPane2.removeAll();
        graficos.removeAll(graficos);

    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        jProgressBar1.setValue(0);
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JOptionPane.showMessageDialog(null,
                "RTTool is a tool to extract relative thresholds from a set of systems. The proposed thresholds are relative\n"
                + " because they assume that metric thresholds should be followed by most source code entities, but that it is \n"
                + "also natural to have a number of entities in the long-tai\" that do not follow the defined limits.\n"
                + "\n" + "The relative thresholds derived is based on a statistical analysis of a software corpus and attempting to \n"
                + "balance two forces. First, the derived relative thresholds should reflect real design rules , widely followed \n"
                + "by the systems in the considered corpus. Second, the derived relative thresholds should not be based on rather \n"
                + "lenient upper limits.");
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        jTabbedPane2.removeAll();
        switch (jComboBox1.getSelectedIndex()) {
            case 0:
                jSlider3.setEnabled(true);
                geragraficosCRP();
                break;
            case 1:
                geraGraficosCR();
                jSlider3.setEnabled(true);
                break;
            case 2:
                jSlider3.setEnabled(false);
                geraGraficosP();
                break;
        }

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        ChartsPercentiles cp = new ChartsPercentiles();
        JDialog jd = new JDialog();
        cp.geraGraficosP(jTextField1.getText());
        //jd.setAlwaysOnTop(true);
        jd.setAutoRequestFocus(true);
        jd.setBounds(cp.getBounds());
        jd.setModal(true);
        jd.setContentPane(cp.getContentPane());
        jd.setVisible(true);

    }//GEN-LAST:event_jButton24ActionPerformed

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
            java.util.logging.Logger.getLogger(First.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(First.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(First.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(First.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new First().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JRadioButton jrbCsv;
    private javax.swing.JRadioButton jrbXml;
    // End of variables declaration//GEN-END:variables
}
