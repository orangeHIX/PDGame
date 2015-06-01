package gui;

import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import entity.SpatialPDGame;
import utils.CompleteListener;
import utils.FileUtils;
import utils.Reporter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author hyx
 */
public class MainJFrame extends javax.swing.JFrame implements Reporter,
        CompleteListener {

    private boolean isRunning = false;
    private WorkThread thread = null;
    private float R, S, T, P;
    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroupLearningPattern;
    private javax.swing.ButtonGroup buttonGroupMigrationPattern;
    private javax.swing.ButtonGroup buttonGroupPayoffMatrix;
    private javax.swing.ButtonGroup buttonGroupStrategyPattern;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxNeedOutputImage;
    private javax.swing.JFileChooser jFileChooserReportOutputPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelGridLengthCheck;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButtonBCHPayoffMatrix;
    private javax.swing.JRadioButton jRadioButtonBSHPayoffMatrix;
    private javax.swing.JRadioButton jRadioButtonClassicPayoffMatrix;
    private javax.swing.JRadioButton jRadioButtonContinuousStrategy;
    private javax.swing.JRadioButton jRadioButtonDRGPayoffMatrix;
    private javax.swing.JRadioButton jRadioButtonFermiLearning;
    private javax.swing.JRadioButton jRadioButtonFiveStrategy;
    private javax.swing.JRadioButton jRadioButtonMaxPayoffLearning;
    private javax.swing.JRadioButton jRadioButtonMigrationEscape;
    private javax.swing.JRadioButton jRadioButtonMigrationNone;
    private javax.swing.JRadioButton jRadioButtonMigrationOptimistic;
    private javax.swing.JRadioButton jRadioButtonMigrationRandom;
    private javax.swing.JRadioButton jRadioButtonThreeStrategy;
    private javax.swing.JRadioButton jRadioButtonTwoStrategy;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerBCH;
    private javax.swing.JSpinner jSpinnerBSH;
    private javax.swing.JSpinner jSpinnerDRG;
    private javax.swing.JSpinner jSpinnerMaxTurn;
    private javax.swing.JSpinner jSpinnerPayoffP;
    private javax.swing.JSpinner jSpinnerPayoffR;
    private javax.swing.JSpinner jSpinnerPayoffS;
    private javax.swing.JSpinner jSpinnerPayoffT;
    private javax.swing.JSpinner jSpinnerPi;
    private javax.swing.JSpinner jSpinnerPopulationDensity;
    private javax.swing.JSpinner jSpinnerQi;
    private javax.swing.JTextArea jTextAreaReport;
    private javax.swing.JTextArea jTextAreaTaskDescription;
    private javax.swing.JTextField jTextFieldGridLength;
    private javax.swing.JTextField jTextFieldLearningPattern;
    private javax.swing.JTextField jTextFieldMigrationPattern;
    private javax.swing.JTextField jTextFieldOutputFilePath;
    private javax.swing.JTextField jTextFieldPayoffMatrix;
    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        this.setLocationByPlatform(true);
        this.setTitle("空间受限网络博弈模型模拟演化软件");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed"
        // desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                    .getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

		/* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        buttonGroupPayoffMatrix = new javax.swing.ButtonGroup();
        buttonGroupMigrationPattern = new javax.swing.ButtonGroup();
        buttonGroupLearningPattern = new javax.swing.ButtonGroup();
        buttonGroupStrategyPattern = new javax.swing.ButtonGroup();
        jFileChooserReportOutputPath = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldGridLength = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerPopulationDensity = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldOutputFilePath = new javax.swing.JTextField();
        jButtonBrowse = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jRadioButtonBCHPayoffMatrix = new javax.swing.JRadioButton();
        jRadioButtonClassicPayoffMatrix = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSpinnerPayoffR = new javax.swing.JSpinner();
        jSpinnerPayoffP = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerPayoffS = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerPayoffT = new javax.swing.JSpinner();
        jSpinnerBCH = new javax.swing.JSpinner();
        jRadioButtonBSHPayoffMatrix = new javax.swing.JRadioButton();
        jSpinnerBSH = new javax.swing.JSpinner();
        jRadioButtonDRGPayoffMatrix = new javax.swing.JRadioButton();
        jSpinnerDRG = new javax.swing.JSpinner();
        jTextFieldPayoffMatrix = new javax.swing.JTextField();
        jCheckBoxNeedOutputImage = new javax.swing.JCheckBox();
        jLabelGridLengthCheck = new javax.swing.JLabel();
        jSpinnerMaxTurn = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jRadioButtonTwoStrategy = new javax.swing.JRadioButton();
        jRadioButtonThreeStrategy = new javax.swing.JRadioButton();
        jRadioButtonFiveStrategy = new javax.swing.JRadioButton();
        jRadioButtonContinuousStrategy = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerPi = new javax.swing.JSpinner();
        jSpinnerQi = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonMigrationNone = new javax.swing.JRadioButton();
        jRadioButtonMigrationRandom = new javax.swing.JRadioButton();
        jRadioButtonMigrationOptimistic = new javax.swing.JRadioButton();
        jRadioButtonMigrationEscape = new javax.swing.JRadioButton();
        jTextFieldMigrationPattern = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jRadioButtonMaxPayoffLearning = new javax.swing.JRadioButton();
        jRadioButtonFermiLearning = new javax.swing.JRadioButton();
        jTextFieldLearningPattern = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButtonStart = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaTaskDescription = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaReport = new javax.swing.JTextArea();

        jFileChooserReportOutputPath
                .setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(3, 1));

        jLabel1.setText("网格边长");
        jLabel1.setToolTipText("");

        jTextFieldGridLength.setColumns(10);
        jTextFieldGridLength
                .setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldGridLength.setText("100");

        jLabel2.setText("人口密度");

        jSpinnerPopulationDensity.setModel(new javax.swing.SpinnerNumberModel(
                Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f),
                Float.valueOf(0.1f)));

        jLabel3.setText("最大步数");

        jLabel5.setText("输出文件目录");

        jTextFieldOutputFilePath.setText("jTextField5");

        jButtonBrowse.setText("浏览");

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("博弈矩阵"));

        buttonGroupPayoffMatrix.add(jRadioButtonBCHPayoffMatrix);
        jRadioButtonBCHPayoffMatrix.setText("BCH");

        buttonGroupPayoffMatrix.add(jRadioButtonClassicPayoffMatrix);
        jRadioButtonClassicPayoffMatrix.setText("经典收益矩阵");

        jLabel6.setText("R =");

        jLabel7.setText("P =");

        jSpinnerPayoffR.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        jSpinnerPayoffP.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        jLabel8.setText("S =");

        jSpinnerPayoffS.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        jLabel9.setText("T =");

        jSpinnerPayoffT.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        jSpinnerBCH.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        buttonGroupPayoffMatrix.add(jRadioButtonBSHPayoffMatrix);
        jRadioButtonBSHPayoffMatrix.setText("BSH");

        jSpinnerBSH.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        buttonGroupPayoffMatrix.add(jRadioButtonDRGPayoffMatrix);
        jRadioButtonDRGPayoffMatrix.setText("DRG");
        jRadioButtonDRGPayoffMatrix.setToolTipText("");

        jSpinnerDRG.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), null, null, Float.valueOf(0.1f)));

        jTextFieldPayoffMatrix.setEditable(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(
                jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout
                .setHorizontalGroup(jPanel10Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel10Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel10Layout
                                                                        .createSequentialGroup()
                                                                        .addGap(21,
                                                                                21,
                                                                                21)
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jLabel9)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jSpinnerPayoffT))
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jLabel6)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jSpinnerPayoffR,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                54,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jLabel7)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jSpinnerPayoffP))
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jLabel8)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jSpinnerPayoffS,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                54,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(
                                                                jPanel10Layout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addComponent(
                                                                                jRadioButtonClassicPayoffMatrix)))
                                        .addGap(25, 25, 25)
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel10Layout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jRadioButtonBCHPayoffMatrix)
                                                                                                        .addGap(24,
                                                                                                                24,
                                                                                                                24)
                                                                                                        .addComponent(
                                                                                                                jRadioButtonBSHPayoffMatrix))
                                                                                        .addGroup(
                                                                                                jPanel10Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jSpinnerBCH,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                58,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                        .addComponent(
                                                                                                                jSpinnerBSH,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                58,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addGap(6,
                                                                                6,
                                                                                6)
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(
                                                                                                jRadioButtonDRGPayoffMatrix)
                                                                                        .addComponent(
                                                                                                jSpinnerDRG,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                58,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGap(0,
                                                                                22,
                                                                                Short.MAX_VALUE))
                                                        .addComponent(
                                                                jTextFieldPayoffMatrix))
                                        .addContainerGap()));
        jPanel10Layout
                .setVerticalGroup(jPanel10Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel10Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel10Layout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(
                                                                                                jRadioButtonBCHPayoffMatrix)
                                                                                        .addComponent(
                                                                                                jRadioButtonClassicPayoffMatrix)
                                                                                        .addComponent(
                                                                                                jRadioButtonBSHPayoffMatrix)
                                                                                        .addComponent(
                                                                                                jRadioButtonDRGPayoffMatrix))
                                                                        .addGap(0,
                                                                                0,
                                                                                Short.MAX_VALUE))
                                                        .addGroup(
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel10Layout
                                                                        .createSequentialGroup()
                                                                        .addGap(0,
                                                                                0,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(
                                                                                jPanel10Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(
                                                                                                jLabel6)
                                                                                        .addComponent(
                                                                                                jSpinnerPayoffR,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel9)
                                                        .addComponent(
                                                                jSpinnerPayoffT,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jTextFieldPayoffMatrix,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel10Layout
                                        .createSequentialGroup()
                                        .addContainerGap(41, Short.MAX_VALUE)
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(
                                                                jSpinnerPayoffS,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jSpinnerBCH,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jSpinnerBSH,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jSpinnerDRG,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                jPanel10Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(
                                                                jSpinnerPayoffP,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))));

        jCheckBoxNeedOutputImage.setText("输出人口斑图");

        jLabelGridLengthCheck.setForeground(new java.awt.Color(255, 0, 0));
        jLabelGridLengthCheck.setText("*");

        jSpinnerMaxTurn.setModel(new javax.swing.SpinnerNumberModel(30000, 100,
                1000000, 100));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("策略环境"));

        buttonGroupStrategyPattern.add(jRadioButtonTwoStrategy);
        jRadioButtonTwoStrategy.setText("双策略");

        buttonGroupStrategyPattern.add(jRadioButtonThreeStrategy);
        jRadioButtonThreeStrategy.setText("三策略");

        buttonGroupStrategyPattern.add(jRadioButtonFiveStrategy);
        jRadioButtonFiveStrategy.setText("五策略");

        buttonGroupStrategyPattern.add(jRadioButtonContinuousStrategy);
        jRadioButtonContinuousStrategy.setText("连续策略");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(
                jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout
                .setHorizontalGroup(jPanel7Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel7Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jRadioButtonTwoStrategy)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jRadioButtonThreeStrategy)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jRadioButtonFiveStrategy)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                jRadioButtonContinuousStrategy)
                                        .addContainerGap(
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                jPanel7Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonTwoStrategy)
                        .addComponent(jRadioButtonThreeStrategy)
                        .addComponent(jRadioButtonFiveStrategy)
                        .addComponent(jRadioButtonContinuousStrategy)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
                jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
                .setHorizontalGroup(jPanel1Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel1Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel1Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                jLabel5)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jTextFieldOutputFilePath)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(
                                                                                jButtonBrowse))
                                                        .addGroup(
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(
                                                                                                jPanel1Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                jLabel1)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jTextFieldGridLength,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                jLabelGridLengthCheck))
                                                                                        .addGroup(
                                                                                                jPanel1Layout
                                                                                                        .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                false)
                                                                                                        .addGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                jPanel1Layout
                                                                                                                        .createSequentialGroup()
                                                                                                                        .addComponent(
                                                                                                                                jLabel2)
                                                                                                                        .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                        .addComponent(
                                                                                                                                jSpinnerPopulationDensity))
                                                                                                        .addGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                jPanel1Layout
                                                                                                                        .createSequentialGroup()
                                                                                                                        .addComponent(
                                                                                                                                jLabel3)
                                                                                                                        .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                        .addComponent(
                                                                                                                                jSpinnerMaxTurn,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                66,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                        .addComponent(
                                                                                                jCheckBoxNeedOutputImage))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                101,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(
                                                                                jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                        .addComponent(
                                                                                                jPanel10,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                        .addComponent(
                                                                                                jPanel7,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))))
                                        .addContainerGap()));
        jPanel1Layout
                .setVerticalGroup(jPanel1Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel1Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel1Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(
                                                                                                jTextFieldGridLength,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(
                                                                                                jLabel1)
                                                                                        .addComponent(
                                                                                                jLabelGridLengthCheck))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addGroup(
                                                                                jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(
                                                                                                jLabel2)
                                                                                        .addComponent(
                                                                                                jSpinnerPopulationDensity,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addGroup(
                                                                                jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(
                                                                                                jLabel3)
                                                                                        .addComponent(
                                                                                                jSpinnerMaxTurn,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jCheckBoxNeedOutputImage))
                                                        .addComponent(
                                                                jPanel10,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                jPanel7,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                jPanel1Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(
                                                                jTextFieldOutputFilePath,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jButtonBrowse))
                                        .addContainerGap()));

        getContentPane().add(jPanel1);

        jPanel2.setLayout(new java.awt.GridLayout(3, 0));

        jPanel6.setBorder(javax.swing.BorderFactory
                .createTitledBorder("协同演化方式"));

        jLabel10.setText("pi =");

        jLabel11.setText("qi =");

        jLabel4.setText("每一步演化，每个个体以pi的概率学习，以qi的概率迁徙");

        jSpinnerPi.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float
                .valueOf(0.01f)));

        jSpinnerQi.setModel(new javax.swing.SpinnerNumberModel(Float
                .valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float
                .valueOf(0.01f)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(
                jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout
                .setHorizontalGroup(jPanel6Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel6Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel6Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addGroup(
                                                                jPanel6Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                jLabel10)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jSpinnerPi,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                64,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18,
                                                                                18,
                                                                                18)
                                                                        .addComponent(
                                                                                jLabel11)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jSpinnerQi,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                64,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap(363, Short.MAX_VALUE)));
        jPanel6Layout
                .setVerticalGroup(jPanel6Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel6Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                jPanel6Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel10)
                                                        .addComponent(jLabel11)
                                                        .addComponent(
                                                                jSpinnerPi,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                jSpinnerQi,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))));

        jPanel2.add(jPanel6);

        jPanel4.setBorder(javax.swing.BorderFactory
                .createTitledBorder("个体迁徙方式"));

        buttonGroupMigrationPattern.add(jRadioButtonMigrationNone);
        jRadioButtonMigrationNone.setText("不迁徙");

        buttonGroupMigrationPattern.add(jRadioButtonMigrationRandom);
        jRadioButtonMigrationRandom.setText("随机迁徙");

        buttonGroupMigrationPattern.add(jRadioButtonMigrationOptimistic);
        jRadioButtonMigrationOptimistic.setText("机会迁徙");

        buttonGroupMigrationPattern.add(jRadioButtonMigrationEscape);
        jRadioButtonMigrationEscape.setText("逃逸迁徙");

        jTextFieldMigrationPattern.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(
                jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout
                .setHorizontalGroup(jPanel4Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel4Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel4Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                jTextFieldMigrationPattern)
                                                        .addGroup(
                                                                jPanel4Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                jRadioButtonMigrationNone)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jRadioButtonMigrationRandom)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jRadioButtonMigrationOptimistic)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jRadioButtonMigrationEscape)
                                                                        .addGap(0,
                                                                                367,
                                                                                Short.MAX_VALUE)))
                                        .addContainerGap()));
        jPanel4Layout
                .setVerticalGroup(jPanel4Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel4Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel4Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(
                                                                jRadioButtonMigrationNone)
                                                        .addComponent(
                                                                jRadioButtonMigrationRandom)
                                                        .addComponent(
                                                                jRadioButtonMigrationOptimistic)
                                                        .addComponent(
                                                                jRadioButtonMigrationEscape))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                7, Short.MAX_VALUE)
                                        .addComponent(
                                                jTextFieldMigrationPattern,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

        jPanel2.add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory
                .createTitledBorder("个体学习方式"));

        buttonGroupLearningPattern.add(jRadioButtonMaxPayoffLearning);
        jRadioButtonMaxPayoffLearning.setText("最大收益学习");

        buttonGroupLearningPattern.add(jRadioButtonFermiLearning);
        jRadioButtonFermiLearning.setText("fermi学习");

        jTextFieldLearningPattern.setEditable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(
                jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout
                .setHorizontalGroup(jPanel5Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel5Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel5Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel5Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                jRadioButtonMaxPayoffLearning)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(
                                                                                jRadioButtonFermiLearning)
                                                                        .addGap(0,
                                                                                475,
                                                                                Short.MAX_VALUE))
                                                        .addComponent(
                                                                jTextFieldLearningPattern,
                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addContainerGap()));
        jPanel5Layout
                .setVerticalGroup(jPanel5Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel5Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel5Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(
                                                                jRadioButtonMaxPayoffLearning)
                                                        .addComponent(
                                                                jRadioButtonFermiLearning))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                7, Short.MAX_VALUE)
                                        .addComponent(
                                                jTextFieldLearningPattern,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

        jPanel2.add(jPanel5);

        getContentPane().add(jPanel2);

        jButtonStart.setText("开始运行");

        jTextAreaTaskDescription.setEditable(false);
        jTextAreaTaskDescription.setColumns(20);
        jTextAreaTaskDescription.setRows(5);
        jScrollPane2.setViewportView(jTextAreaTaskDescription);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(
                jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout
                .setHorizontalGroup(jPanel9Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel9Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jButtonStart)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                jScrollPane2,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                574, Short.MAX_VALUE)
                                        .addContainerGap()));
        jPanel9Layout
                .setVerticalGroup(jPanel9Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel9Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel9Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                jButtonStart,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                        .addGroup(
                                                                jPanel9Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                jScrollPane2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                81,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(0,
                                                                                0,
                                                                                Short.MAX_VALUE)))));

        jTextAreaReport.setColumns(20);
        jTextAreaReport.setRows(5);
        jScrollPane1.setViewportView(jTextAreaReport);
        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(
                jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                jPanel8Layout.createSequentialGroup().addContainerGap()
                        .addComponent(jScrollPane1).addContainerGap()));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                jPanel8Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 128,
                                Short.MAX_VALUE).addContainerGap()));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
                jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jPanel3Layout
                .setVerticalGroup(jPanel3Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel3Layout
                                        .createSequentialGroup()
                                        .addComponent(
                                                jPanel9,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                jPanel8,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

        getContentPane().add(jPanel3);

        // add listener
        addComponentsListener();

        initComponentsState();
        pack();
    }// </editor-fold>

    private void initComponentsState() {
        jLabelGridLengthCheck.setVisible(false);

        jRadioButtonClassicPayoffMatrix.setSelected(true);
        setClassicPayoffMatrixSpinnerGroupEnable(true);
        jSpinnerBCH.setEnabled(false);
        jSpinnerBSH.setEnabled(false);
        jSpinnerDRG.setEnabled(false);
        updateJTextFieldPayoffMatrix((Number) jSpinnerPayoffR.getValue(),
                (Number) jSpinnerPayoffS.getValue(),
                (Number) jSpinnerPayoffT.getValue(),
                (Number) jSpinnerPayoffP.getValue());

        jTextFieldOutputFilePath.setText(jFileChooserReportOutputPath
                .getCurrentDirectory().getAbsolutePath());

        jRadioButtonTwoStrategy.setSelected(true);

        jRadioButtonMaxPayoffLearning.setSelected(true);
        setLearningPatternText(jRadioButtonMaxPayoffLearning);

        jRadioButtonMigrationNone.setSelected(true);
        setMigratePatternText(jRadioButtonMigrationNone);
    }

    private void addComponentsListener() {
        jTextFieldGridLength.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                
                // JOptionPane.showMessageDialog(MainJFrame.this, new
                // String(""));
                int i = 0;
                try {
                    i = Integer.parseUnsignedInt(((JTextField) e.getSource())
                            .getText().trim());
                } catch (NumberFormatException ne) {

                }
                if (i <= 0) {
                    JOptionPane.showMessageDialog(MainJFrame.this,
                            "网格边长必须是一个有效的正整数");
                    jTextFieldGridLength.requestFocusInWindow();
                    jTextFieldGridLength.selectAll();
                    jLabelGridLengthCheck.setVisible(true);
                } else {
                    jLabelGridLengthCheck.setVisible(false);
                }
            }
        });
        jRadioButtonClassicPayoffMatrix.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setClassicPayoffMatrixSpinnerGroupEnable(true);
                jSpinnerBCH.setEnabled(false);
                jSpinnerBSH.setEnabled(false);
                jSpinnerDRG.setEnabled(false);
                updateJTextFieldPayoffMatrix(
                        (Number) jSpinnerPayoffR.getValue(),
                        (Number) jSpinnerPayoffS.getValue(),
                        (Number) jSpinnerPayoffT.getValue(),
                        (Number) jSpinnerPayoffP.getValue());
            }
        });
        jRadioButtonBCHPayoffMatrix.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                setClassicPayoffMatrixSpinnerGroupEnable(false);
                jSpinnerBCH.setEnabled(true);
                jSpinnerBSH.setEnabled(false);
                jSpinnerDRG.setEnabled(false);
                Number Dg = (Number) jSpinnerBCH.getValue();
                updateJTextFieldPayoffMatrixBCH(Dg);
            }
        });
        jRadioButtonBSHPayoffMatrix.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setClassicPayoffMatrixSpinnerGroupEnable(false);
                jSpinnerBCH.setEnabled(false);
                jSpinnerBSH.setEnabled(true);
                jSpinnerDRG.setEnabled(false);
                Number Dr = (Number) jSpinnerBSH.getValue();
                updateJTextFieldPayoffMatrixBSH(Dr);
            }
        });
        jRadioButtonDRGPayoffMatrix.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setClassicPayoffMatrixSpinnerGroupEnable(false);
                jSpinnerBCH.setEnabled(false);
                jSpinnerBSH.setEnabled(false);
                jSpinnerDRG.setEnabled(true);
                Number DrOrDg = (Number) jSpinnerDRG.getValue();
                updateJTextFieldPayoffMatrixDRG(DrOrDg);
            }
        });
        ChangeListener cl = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                updateJTextFieldPayoffMatrix(
                        (Number) jSpinnerPayoffR.getValue(),
                        (Number) jSpinnerPayoffS.getValue(),
                        (Number) jSpinnerPayoffP.getValue(),
                        (Number) jSpinnerPayoffT.getValue());
            }
        };
        jSpinnerPayoffR.addChangeListener(cl);
        jSpinnerPayoffS.addChangeListener(cl);
        jSpinnerPayoffT.addChangeListener(cl);
        jSpinnerPayoffP.addChangeListener(cl);

        jSpinnerBCH.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Number Dg = (Number) jSpinnerBCH.getValue();
                updateJTextFieldPayoffMatrixBCH(Dg);
            }
        });
        jSpinnerBSH.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Number Dr = (Number) jSpinnerBSH.getValue();
                updateJTextFieldPayoffMatrixBSH(Dr);
            }
        });
        jSpinnerDRG.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Number DrOrDg = (Number) jSpinnerDRG.getValue();
                updateJTextFieldPayoffMatrixDRG(DrOrDg);
            }
        });
        jTextFieldOutputFilePath.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                File f = new File(jTextFieldOutputFilePath.getText());
                if ((!f.exists()) || (!f.isDirectory())) {
                    JOptionPane.showMessageDialog(MainJFrame.this, "该文件目录不存在");
                    jTextFieldOutputFilePath.requestFocusInWindow();
                    jTextFieldOutputFilePath.selectAll();
                }
            }
        });
        jButtonBrowse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jFileChooserReportOutputPath.showOpenDialog(MainJFrame.this);
                File selectedFilePath = jFileChooserReportOutputPath
                        .getSelectedFile();
                if (selectedFilePath != null)
                    jTextFieldOutputFilePath.setText(selectedFilePath
                            .getAbsolutePath());
            }
        });
        ActionListener migrationlistener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setMigratePatternText((JRadioButton) e.getSource());
            }
        };
        for (Enumeration<AbstractButton> e = buttonGroupMigrationPattern
                .getElements(); e.hasMoreElements(); ) {
            e.nextElement().addActionListener(migrationlistener);
        }
        ActionListener learninglistener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setLearningPatternText((JRadioButton) e.getSource());
            }
        };
        for (Enumeration<AbstractButton> e = buttonGroupLearningPattern
                .getElements(); e.hasMoreElements(); ) {
            e.nextElement().addActionListener(learninglistener);
        }

        jButtonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!isRunning) {
                    thread = new WorkThread(Integer
                            .parseInt(jTextFieldGridLength.getText()),
                            (Float) (Number) jSpinnerPopulationDensity
                                    .getValue(), R, S, T, P,
                            (Float) (Number) jSpinnerPi.getValue(),
                            (Float) (Number) jSpinnerQi.getValue(),
                            getLearningPattern(), getMigratePattern(),
                            getStrategyPattern(), jTextFieldOutputFilePath
                            .getText(), jCheckBoxNeedOutputImage
                            .isSelected(),
                            (Integer) (Number) jSpinnerMaxTurn.getValue(),
                            MainJFrame.this, MainJFrame.this);
                    thread.start();
                    if (thread.getState().equals(Thread.State.RUNNABLE)) {
                        isRunning = true;
                        taskStateChanged();
                        jTextAreaTaskDescription.setText(thread.getTaskDes());
                    }
                } else if (thread != null) {
                    thread.stopTask();
                    while (!thread.getState().equals(Thread.State.TERMINATED)) {
                        // System.out.println(thread.getState());
                    }
                    isRunning = false;
                    taskStateChanged();
                }
            }
        });
    }

    private void setClassicPayoffMatrixSpinnerGroupEnable(boolean aflag) {
        jSpinnerPayoffR.setEnabled(aflag);
        jSpinnerPayoffS.setEnabled(aflag);
        jSpinnerPayoffP.setEnabled(aflag);
        jSpinnerPayoffT.setEnabled(aflag);
    }

    private void updateJTextFieldPayoffMatrix(Number r, Number s, Number t,
                                              Number p) {
        this.R = r.floatValue();
        this.S = s.floatValue();
        this.T = t.floatValue();
        this.P = p.floatValue();
        jTextFieldPayoffMatrix.setText("R=" + r + ", S=" + s + ", T=" + t
                + ", P=" + p);
    }

    private void updateJTextFieldPayoffMatrixBCH(Number Dg) {
        updateJTextFieldPayoffMatrix(1, 0, 1 + Dg.floatValue(), 0);
    }

    private void updateJTextFieldPayoffMatrixBSH(Number Dr) {
        updateJTextFieldPayoffMatrix(1, -Dr.floatValue(), 1, 0);
    }

    private void updateJTextFieldPayoffMatrixDRG(Number DrOrDg) {
        updateJTextFieldPayoffMatrix(1, -DrOrDg.floatValue(),
                1 + DrOrDg.floatValue(), 0);
    }

    private MigrationPattern getMigratePattern() {
        if (jRadioButtonMigrationNone.isSelected()) {
            return MigrationPattern.NONE;
        } else if (jRadioButtonMigrationRandom.isSelected()) {
            return MigrationPattern.RANDOM;
        } else if (jRadioButtonMigrationOptimistic.isSelected()) {
            return MigrationPattern.OPTIMISTIC;
        } else if (jRadioButtonMigrationEscape.isSelected()) {
            return MigrationPattern.ESCAPE;
        }
        return null;
    }

    private LearningPattern getLearningPattern() {
        if (jRadioButtonMaxPayoffLearning.isSelected()) {
            return LearningPattern.MAXPAYOFF;
        } else if (jRadioButtonFermiLearning.isSelected()) {
            return LearningPattern.FERMI;
        }
        return null;
    }

    private StrategyPattern getStrategyPattern() {
        if (jRadioButtonTwoStrategy.isSelected()) {
            return StrategyPattern.TWO;
        } else if (jRadioButtonThreeStrategy.isSelected()) {
            return StrategyPattern.THREE;
        } else if (jRadioButtonFiveStrategy.isSelected()) {
            return StrategyPattern.FIVE;
        } else if (jRadioButtonContinuousStrategy.isSelected()) {
            return StrategyPattern.CONTINUOUS;
        }
        return null;
    }

    private void setLearningPatternText(JRadioButton radioButton) {
        if (radioButton.equals(jRadioButtonMaxPayoffLearning)) {
            jTextFieldLearningPattern.setText("个体从直接邻居学习策略，学习具有最大累积收益的邻居");
        } else if (radioButton.equals(jRadioButtonFermiLearning)) {
            jTextFieldLearningPattern
                    .setText("从直接邻居学习策略，个体i随机选择一个邻居j后，按照下式决定是否学习该邻居，"
                            + "其中Pi是i的本轮收益，Pj是邻居j的本轮收益，i学习后者j的概率为w=1/(1+exp[(pi-pj)/k])");
        }
    }

    private void setMigratePatternText(JRadioButton radioButton) {
        if (radioButton.equals(jRadioButtonMigrationNone)) {
            jTextFieldMigrationPattern.setText("个体不移动");
        } else if (radioButton.equals(jRadioButtonMigrationRandom)) {
            jTextFieldMigrationPattern.setText("个体随机迁徙到直接相邻的一个空位上");
        } else if (radioButton.equals(jRadioButtonMigrationOptimistic)) {
            jTextFieldMigrationPattern
                    .setText("个体个体i所在位置x为xk在一阶邻居中随机选取一个空位xl，"
                            + "按照概率σ的可能性迁移的位置上。其中： σ(xk→xl)= 1/(1+exp[(fcl-fck)/K]) K=0.1 （5），"
                            + "fck表示个体位于k位置时邻居的平均合作程度，公式5表示个体i在随机邻居中选取一个空位l（没有则不移）");
        } else if (radioButton.equals(jRadioButtonMigrationEscape)) {
            jTextFieldMigrationPattern
                    .setText("个体统计周围邻居背叛者的数量nd，以nd/8的概率迁徙到空位上");
        }
    }

    private void taskStateChanged() {
        if (isRunning) {
            jButtonStart.setText("停止运行");
            jTextAreaReport.setText("");
        } else {
            jButtonStart.setText("开始运行");
            jTextAreaTaskDescription.append("\r\n"
                    + getDateString(System.currentTimeMillis()) + "停止任务");
            jTextAreaTaskDescription.setCaretPosition(jTextAreaTaskDescription
                    .getText().length());
        }
    }

    private String getDateString(long timeInMillis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
                timeInMillis));
    }

    @Override
    public void report(String s) {
        // TODO Auto-generated method stub
        jTextAreaReport.append(s + "\r\n");
        jTextAreaReport.setCaretPosition(jTextAreaReport.getText().length());
    }

    @Override
    public void notifyCompleted(String message) {
        // TODO Auto-generated method stub
        isRunning = false;
        taskStateChanged();
        jTextAreaReport.append(message + "\r\n任务完成\r\n");
    }

    class WorkThread extends Thread {
        SpatialPDGame spdg;
        String outputFilePath;
        CompleteListener completeListener;
        int maxTurn;
        long startTime;

        boolean mStop = false;

        public WorkThread(int L, float d0, float R, float S, float T, float P,
                          float pi, float qi, LearningPattern learningPattern,
                          MigrationPattern imigratePattern,
                          StrategyPattern strategyPattern, String filepath,
                          boolean recordSnapShoot, int maxTurn, Reporter reporter,
                          CompleteListener completeListener) {
            spdg = new SpatialPDGame(recordSnapShoot, reporter);
            spdg.initSpatialPDGame(L, d0, R, S, T, P, pi, qi, 1.0f,
                    learningPattern, imigratePattern, strategyPattern,
                    NeighbourCoverage.Classic);
            outputFilePath = filepath;
            this.maxTurn = maxTurn;
            this.completeListener = completeListener;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int count = 0;
            startTime = System.currentTimeMillis();
            while (count < maxTurn && !mStop) {
                if (count + 1000 < maxTurn) {
                    spdg.run(1000);
                    count += 1000;
                } else {
                    spdg.run(maxTurn - count);
                    count = maxTurn;
                }
                // System.out.println(spdg.getTurn());
            }
            spdg.done();
            FileUtils.outputToFile(
                    jTextFieldOutputFilePath.getText().concat(
                            "\\" + spdg.toString() + ".txt"),
                    spdg.dataPrinter.getDetailReport());
            FileUtils.outputSnapshootToFile(jTextFieldOutputFilePath.getText()
                    .concat("\\" + spdg.toString() + "\\"), spdg
                    .getSnapshootMap());
            completeListener.notifyCompleted("underwent " + spdg.getTurn()
                    + " turns");
            // MainJFrame.this.setBounds(r);
            // try {
            // sleep(100000);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }

        public void stopTask() {
            mStop = true;
        }

        public String getTaskDes() {
            return "当前运行任务简述：" + spdg.toString() + "\r\n结果输出文件目录："
                    + outputFilePath + "\r\n开始时间：" + getDateString(startTime);
        }
    }
    // End of variables declaration

}
