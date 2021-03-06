/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.out;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eimantas
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public boolean machineRunning = true;
    
    public GUI() {
        initComponents();
        pack();
        setTitle("MIF_OS_4");
        setVisible(true);
        setResizable(false);
    }
    public void initTables(){
        DefaultTableModel plModel = (DefaultTableModel) ProcessList.getModel();
        for( int i = 0; i < OS.processDesc.size();i++){
            plModel.addRow( new Object[]{ OS.processDesc.get(i).getName(), OS.processDesc.get(i).getPriority(),OS.processDesc.get(i).getState()});
        }
        DefaultTableModel omMemory = (DefaultTableModel) operatingMemory.getModel();
        for( int i = 0 ; i < OS.RM_MEMORY_SIZE ; i++ ){
            omMemory.addRow( new Object[]{ Integer.toString(i), OS.rmMemory[i].getCell() });
        }
        DefaultTableModel extMemory = (DefaultTableModel) exMemory.getModel();
        for( int i = 0 ; i < OS.EXTERNAL_MEMORY_SIZE ; i++ ){
            extMemory.setValueAt(new Object[]{ OS.externalMemory[i].getCell() }, i, 2);
        }
    }
    public void refreshProcessList(){
        try{
            DefaultTableModel plModel = (DefaultTableModel) ProcessList.getModel();
            plModel.setRowCount(0);
            for( int i = 0; i < OS.processDesc.size();i++){
                plModel.addRow( new Object[]{ OS.processDesc.get(i).getName(), OS.processDesc.get(i).getPriority(),OS.processDesc.get(i).getState()});
            }
        } catch( ArrayIndexOutOfBoundsException exception ){
            out.println("error");
        }
    }
    
    public void refreshOperatingMemory(){
        try{
            DefaultTableModel omMemory = (DefaultTableModel) operatingMemory.getModel();
            omMemory.setRowCount(0);
            for( int i = 0 ; i < OS.RM_MEMORY_SIZE ; i++ ){
                omMemory.addRow( new Object[]{ Integer.toString(i), OS.rmMemory[i].getCell() });
            }
        } catch( ArrayIndexOutOfBoundsException exception ){
            out.println("error");
        }
    }
    
    public void refreshExternalMemory(){
        try{
            DefaultTableModel extMemory = (DefaultTableModel) exMemory.getModel();
            extMemory.setRowCount(0);
            for( int i = 0 ; i < OS.EXTERNAL_MEMORY_SIZE ; i++ ){
                extMemory.addRow( new Object[]{ Integer.toString(i), OS.externalMemory[i].getCell() });
            }
        } catch( ArrayIndexOutOfBoundsException exception ){
            out.println("error");
        }
    }
    public void refreshGUI(){
        if(this.machineRunning){
            refreshExternalMemory();
            refreshOperatingMemory();
            refreshProcessList();
        }
    }
    public void shutdown(){
        DefaultTableModel plModel = (DefaultTableModel) ProcessList.getModel();
        plModel.setRowCount(0);
        DefaultTableModel omMemory = (DefaultTableModel) operatingMemory.getModel();
        omMemory.setRowCount(0);
        DefaultTableModel extMemory = (DefaultTableModel) exMemory.getModel();
        extMemory.setRowCount(0);
        jTextArea1.setText(null);
        jTextArea2.setText(null);
        jTextArea1.setEditable(false);
        jTextArea2.setEditable(false);
        this.machineRunning = false;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel tableModel = new DefaultTableModel();
        ProcessList = new javax.swing.JTable(tableModel);
        jScrollPane2 = new javax.swing.JScrollPane();
        DefaultTableModel memoryModel = new DefaultTableModel();
        operatingMemory = new javax.swing.JTable(memoryModel);
        jScrollPane3 = new javax.swing.JScrollPane();
        DefaultTableModel exmemoryModel = new DefaultTableModel();
        exMemory = new javax.swing.JTable(exmemoryModel);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ProcessList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Process", "Prior","State"
            }
        ));
        ProcessList.getColumnModel().getColumn(0).setPreferredWidth(190);
        jScrollPane1.setViewportView(ProcessList);
        ProcessList.setVerifyInputWhenFocusTarget(false);
        ProcessList.setName("");
        ProcessList.getAccessibleContext().setAccessibleName("");

        operatingMemory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cell"
            }
        ));
        jScrollPane2.setViewportView(operatingMemory);
        operatingMemory.setVerifyInputWhenFocusTarget(false);
        operatingMemory.setName("");
        operatingMemory.getAccessibleContext().setAccessibleName("");

        exMemory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Cell"
            }
        ));
        jScrollPane3.setViewportView(exMemory);
        exMemory.setVerifyInputWhenFocusTarget(false);
        exMemory.setName("");
        exMemory.getAccessibleContext().setAccessibleName("");

        jLabel1.setText("Process list");

        jLabel2.setText("Operating Memory");

        jLabel3.setText("External Memory");

        jButton1.setText("Įrašyti programą");

        jButton2.setText("Išjungti sistemą");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel4.setText("Output console");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane5.setViewportView(jTextArea2);

        jLabel5.setText("Input console");

        jButton3.setText("Input");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(103, 103, 103)
                                        .addComponent(jLabel1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(100, 100, 100)
                                        .addComponent(jLabel2)
                                        .addGap(155, 155, 155)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(385, 385, 385)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(393, 393, 393)
                                .addComponent(jLabel5)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //registras AI 0
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OS.realMachine.setRegisterAI(1);
            }
        }
    );
    // AI = 1
    jButton2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            OS.realMachine.setRegisterAI(2);
        }
    }
    );
    //jButton3

    pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    /*
    
    /**
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    /*    java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }*/
    public JButton getInputButton(){
        return this.jButton3;
    }
    
    public JTextArea getOutputArea(){
        return this.jTextArea1;
    }
    public JTextArea getInputArea(){
        return this.jTextArea2;
    }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ProcessList;
    private javax.swing.JTable exMemory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTable operatingMemory;
    // End of variables declaration//GEN-END:variables
}
