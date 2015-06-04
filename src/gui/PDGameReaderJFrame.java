/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import entity.Individual;
import entity.SpatialPDGame;
import entity.World;
import org.json.JSONObject;
import utils.FileUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hyx
 */
public class PDGameReaderJFrame extends JFrame implements TabChangeListener, MouseClickLocationChangeListener {

    LinkedList<ReaderModel> tabModelList;
    ReaderModel currReaderModel;

    /**
     * Creates new form PDGameReaderJFrame
     */
    public PDGameReaderJFrame() {
        tabModelList = new LinkedList<>();
        setResizable(false);
        initComponents();
        //tabModelList.set(1, null);

        jTabbedPanePopuPic.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

       // add("sdsa",new YiTabbedPane.CloseButtonTabComponent(jTabbedPanePopuPic));

        tabModelList.add(new ReaderModel());
        jTabbedPanePopuPic.setSelectedIndex(0);
        updateAll();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel3 = new JLabel();
        jButtonUp = new JButton();
        jLabel4 = new JLabel();
        jButtonLeft = new JButton();
        jLabel5 = new JLabel();
        jButtonRight = new JButton();
        jLabel6 = new JLabel();
        jButtonDown = new JButton();
        jLabel7 = new JLabel();
        jTabbedPanePopuPic = new YiTabbedPane();
        jPanel2 = new JPanel();
        jSliderTurn = new JSlider(0, 50000, 0);
        jLabelMinTurn = new JLabel();
        jLabelMaxTurn = new JLabel();
        jTextFieldTurn = new JTextField();
        jButtonBackward = new JButton();
        jButtonForward = new JButton();
        jTextFieldPath = new JTextField();
        jLabel10 = new JLabel();
        jLabelPreviousTurn = new JLabel();
        jLabelNextTurn = new JLabel();
        jButtonPath = new JButton();
        jLabel15 = new JLabel();
        jPanel3 = new JPanel();
        jLabel8 = new JLabel();
        jTextFieldX = new JTextField();
        jLabel9 = new JLabel();
        jTextFieldY = new JTextField();
        jPanel6 = new JPanel();
        jPanel4 = new JPanel();
        jScrollPane2 = new JScrollPane();
        jListIndivInfoModel = new DefaultListModel<>();
        jListIndivInfo = new JList<>(jListIndivInfoModel);
        jLabel14 = new JLabel();
        jPanel5 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jListPDGameInfoModel = new DefaultListModel<>();
        jListPDGameInfo = new JList<>(jListPDGameInfoModel);
        jLabel13 = new JLabel();

        jFileChooserFilePath = new JFileChooser();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(3, 3));
        jPanel1.add(jLabel3);

        jButtonUp.setText("Up");
        jPanel1.add(jButtonUp);
        jPanel1.add(jLabel4);

        jButtonLeft.setText("Left");
        jPanel1.add(jButtonLeft);
        jPanel1.add(jLabel5);

        jButtonRight.setText("Right");
        jPanel1.add(jButtonRight);
        jPanel1.add(jLabel6);

        jButtonDown.setText("Down");
        jPanel1.add(jButtonDown);
        jPanel1.add(jLabel7);

        jLabelMinTurn.setText("min turn");

        jLabelMaxTurn.setText("max turn");

        jTextFieldTurn.setText("100");

        jButtonBackward.setText("Backward");

        jButtonForward.setText("Forward");

        jTextFieldPath.setText("jTextField4");

        jLabel10.setText("Path: ");

        jLabelPreviousTurn.setText("valid");

        jLabelNextTurn.setText("valid");

        jButtonPath.setText("Change Path");

        jLabel15.setText("Turn: ");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabelMinTurn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jSliderTurn, GroupLayout.PREFERRED_SIZE, 462, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabelMaxTurn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonPath))
                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabelPreviousTurn)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButtonBackward)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldTurn, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonForward)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabelNextTurn)
                                                .addGap(117, 117, 117))))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextFieldPath)
                                                .addComponent(jButtonPath))
                                        .addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextFieldTurn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonBackward)
                                        .addComponent(jButtonForward)
                                        .addComponent(jLabelNextTurn)
                                        .addComponent(jLabelPreviousTurn)
                                        .addComponent(jLabel15))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabelMaxTurn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelMinTurn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSliderTurn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setText("location x: ");

        jTextFieldX.setText("jTextField2");

        jLabel9.setText("y: ");

        jTextFieldY.setText("jTextField3");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldX, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldY, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 50, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(jTextFieldX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9)
                                        .addComponent(jTextFieldY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setLayout(new java.awt.GridLayout(2, 1));


        jScrollPane2.setViewportView(jListIndivInfo);

        jLabel14.setText("Individual Info");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel4);


        jScrollPane1.setViewportView(jListPDGameInfo);

        jLabel13.setText("PDGame Info");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel5);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTabbedPanePopuPic))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jTabbedPanePopuPic, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                                        .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        jFileChooserFilePath.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.getName().endsWith(FileUtils.PDGameJSONFileSuffix)) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return FileUtils.PDGameJSONFileSuffix + " file";
            }
        });

        jTextFieldPath.setText(jFileChooserFilePath
                .getCurrentDirectory().getAbsolutePath());
        jTextFieldPath.setEditable(false);

        addComponentsListener();

        pack();
    }// </editor-fold>

    private void addComponentsListener() {
        addTextFieldListener(jTextFieldTurn, new DealTextField() {
            @Override
            public void deal() {
                int i = getPositiveNumberFromTextField(jTextFieldTurn);
                if (currReaderModel != null) {
                    if (i < 0) {
                        updateTurnPanel();
                        jTextFieldTurn.requestFocusInWindow();
                        jTextFieldTurn.selectAll();
                    } else {
                        currReaderModel.changeTurn(i);
                        updateAll();
                    }
                }
            }
        });

        jButtonPath.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jFileChooserFilePath.showOpenDialog(PDGameReaderJFrame.this);
                File selectedFile = jFileChooserFilePath
                        .getSelectedFile();
                if (selectedFile != null) {
                    int index = jTabbedPanePopuPic.getSelectedIndex();
                    ReaderModel rm = tabModelList.get(index);
                    jTextFieldPath.setText(selectedFile.getParentFile()
                            .getAbsolutePath());
                    if (rm.initFromDirect(selectedFile.getParentFile())) {
                        //tabModelList.add(rm);
                        jTabbedPanePopuPic.setToolTipTextAt(index, rm.pdGame.toString());
                        changeTab(index);
                    } else {
                        JOptionPane.showMessageDialog(PDGameReaderJFrame.this,
                                "can not find \""
                                        + FileUtils.PDGameJSONFileSuffix
                                        + "\" file in the directory");
                        jTextFieldPath.requestFocusInWindow();
                        jTextFieldPath.selectAll();
                    }
                }
            }

        });
        jButtonForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currReaderModel != null) {
                    currReaderModel.changeTurn(currReaderModel.nextTurn);
                    updateAll();
                }
            }
        });
        jButtonBackward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currReaderModel != null) {
                    currReaderModel.changeTurn(currReaderModel.previousTurn);
                    updateAll();
                }
            }
        });
        jButtonUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealDirectionButtonEvent(-1, 0);
            }
        });
        jButtonDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealDirectionButtonEvent(1, 0);
            }
        });
        jButtonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealDirectionButtonEvent(0, -1);
            }
        });
        jButtonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealDirectionButtonEvent(0, 1);
            }
        });
        addTextFieldListener(jTextFieldX, new DealTextField() {
            @Override
            public void deal() {
                int i = getPositiveNumberFromTextField(jTextFieldX);
                if (currReaderModel != null) {
                    if (currReaderModel != null) {
                        if (currReaderModel.changeLocation(i, currReaderModel.y)) {
                            updateAll();
                        } else {
                            updateLocationPanel();
                            jTextFieldY.requestFocusInWindow();
                            jTextFieldY.selectAll();
                        }
                    }
                }
            }
        });
        addTextFieldListener(jTextFieldY, new DealTextField() {
            @Override
            public void deal() {
                int i = getPositiveNumberFromTextField(jTextFieldY);
                if (currReaderModel != null) {
                    if (currReaderModel.changeLocation(currReaderModel.x, i)) {
                        updateAll();
                    } else {
                        updateLocationPanel();
                        jTextFieldY.requestFocusInWindow();
                        jTextFieldY.selectAll();
                    }
                }
            }
        });
        jTabbedPanePopuPic.setTabChangeListener(this);

    }

    interface DealTextField {
        void deal();
    }

    private void addTextFieldListener(JTextField textField, final DealTextField dealTextField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    dealTextField.deal();
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                dealTextField.deal();
            }
        });
    }


    private int getPositiveNumberFromTextField(JTextField textField) {
        int i = -1;
        try {
            i = Integer.parseUnsignedInt(textField
                    .getText().trim());
        } catch (NumberFormatException ne) {

        }
        return i;
    }

    private void dealDirectionButtonEvent(int v_i, int v_j) {
        if (currReaderModel != null) {
            changeLocationTo(currReaderModel.x + v_i, currReaderModel.y + v_j);
        }
    }

    private File findPDGameDescFile(File[] fs) {
        for (File f : fs) {
            if (f.isFile()
                    && f.getName().endsWith(
                    FileUtils.PDGameJSONFileSuffix)) {
                return f;
            }
        }
        return null;
    }

    public void changeTab(int index) {
        if (index > -1 && index < jTabbedPanePopuPic.getTabCount()) {
            jTabbedPanePopuPic.setSelectedIndex(index);
            if( !(jTabbedPanePopuPic.getComponentAt(index) instanceof PicturePanel)){
                jTabbedPanePopuPic.setComponentAt(index, new PicturePanel(this));
            }
            currReaderModel = tabModelList.get(jTabbedPanePopuPic.getSelectedIndex());
            updateAll();
        } else if (index == -1) {
            currReaderModel = null;
            updateAll();
        }

    }

    private void updateAll() {
        updateStraPic();
        updateIndividualInfo();
        updatePDGameInfo();
        updateLocationPanel();
        updateTurnPanel();
    }

    private void updateStraPic() {
        if(currReaderModel != null) {
            int index = jTabbedPanePopuPic.getSelectedIndex();
            if (index < 0)
                return;
            PicturePanel pp = (PicturePanel) jTabbedPanePopuPic.getComponentAt(index);
            if (pp != null) {
                pp.setWorld(currReaderModel.world, currReaderModel.x, currReaderModel.y);
            }
        }
    }

    private void updatePDGameInfo() {
        jListPDGameInfoModel.clear();
        if (currReaderModel != null) {
            Map<String, Object> param = currReaderModel.pdGame.getParam();

            for (String key : param.keySet()) {
                jListPDGameInfoModel.addElement(key + "=" + param.get(key));
            }
        }
    }

    private void updateIndividualInfo() {
        jListIndivInfoModel.clear();
        if (currReaderModel != null) {
            Individual in = currReaderModel.in;
            JSONObject joIn = in.getJSONObject();

            for (String key : joIn.keySet()) {
                jListIndivInfoModel.addElement(key + "=" + joIn.get(key));
            }
        }
    }

    private void updateLocationPanel() {
        if (currReaderModel != null) {
            jTextFieldX.setText("" + currReaderModel.x);
            jTextFieldY.setText("" + currReaderModel.y);
        } else {
            jTextFieldX.setText("");
            jTextFieldY.setText("");
        }
    }

    private void updateTurnPanel() {
        if (currReaderModel != null) {
            jTextFieldTurn.setText("" + currReaderModel.turn);
            jLabelPreviousTurn.setText("prev " + currReaderModel.previousTurn);
            jLabelNextTurn.setText("next " + currReaderModel.nextTurn);
            jLabelMaxTurn.setText("" + currReaderModel.maxTurn);
            jLabelMinTurn.setText("" + currReaderModel.minTurn);
        } else {
            jTextFieldTurn.setText("0");
            jLabelPreviousTurn.setText("valid");
            jLabelNextTurn.setText("valid");
            jLabelMaxTurn.setText("max turn");
            jLabelMinTurn.setText("min turn");
        }
        updatejSliderTurnValue();
    }

    private void updatejSliderTurnValue() {
        if (currReaderModel != null) {
            jSliderTurn.setValue(projectSliderValue(currReaderModel.turn));
        } else {
            jSliderTurn.setValue(0);
        }
    }

    private int projectSliderValue(int value) {
        int minTurn = currReaderModel.minTurn;
        int maxTurn = currReaderModel.maxTurn;
        int turnRange = maxTurn - minTurn;
        int min = jSliderTurn.getMinimum();
        int max = jSliderTurn.getMaximum();
        int range = max - min;
        return (int) (min + ((float) (value - minTurn)) / turnRange * range);
    }

    @Override
    public void notifyInsertTabAt(int index) {
        tabModelList.add(index, new ReaderModel());
        System.out.println("add tab model at " + index);
    }

    @Override
    public void notifyRemoveTabAt(int index) {
        tabModelList.remove(index);
        System.out.println("remove tab model at " + index);
    }

    @Override
    public void changeLocationTo(int i, int j) {
        if (currReaderModel != null) {
            currReaderModel.changeLocation(i, j);
        }
        updateAll();
    }

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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PDGameReaderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PDGameReaderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PDGameReaderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PDGameReaderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame jf = new PDGameReaderJFrame();
                jf.setTitle("PDGameReader");
                jf.setLocationByPlatform(true);

                jf.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JButton jButtonBackward;
    private JButton jButtonDown;
    private JButton jButtonForward;
    private JButton jButtonLeft;
    private JButton jButtonPath;
    private JButton jButtonRight;
    private JButton jButtonUp;
    private JLabel jLabelMinTurn;
    private JLabel jLabel10;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabelMaxTurn;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel jLabelNextTurn;
    private JLabel jLabelPreviousTurn;
    private JList<String> jListIndivInfo;
    private JList<String> jListPDGameInfo;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSlider jSliderTurn;
    private YiTabbedPane jTabbedPanePopuPic;
    private JTextField jTextFieldTurn;
    private JTextField jTextFieldPath;
    private JTextField jTextFieldX;
    private JTextField jTextFieldY;

    private JFileChooser jFileChooserFilePath;
    private DefaultListModel<String> jListIndivInfoModel;
    private DefaultListModel<String> jListPDGameInfoModel;
    // End of variables declaration


    class ReaderModel {
        //File direct;
        TreeMap<Integer, File> allPicFileMap;
        //ArrayList<File> allPicFileList;
        int minTurn;
        int maxTurn;
        int previousTurn;
        int nextTurn;
        int turn;
        World world;
        SpatialPDGame pdGame;
        int x, y;
        Individual in;

        public ReaderModel() {
            allPicFileMap = new TreeMap<>();
            //allPicFileList = new ArrayList<>();
            pdGame = new SpatialPDGame();
            world = pdGame.getWorld();
            x = 0;
            y = 0;
            minTurn = maxTurn = turn = 0;
            previousTurn = nextTurn = 0;
        }

        public boolean initFromDirect(File f) {
            if (f.exists() && f.isDirectory()) {
                File[] fs = f.listFiles(
                        new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                if (pathname.isFile()) {
                                    if (pathname.getName().endsWith(FileUtils.PDGameJSONFileSuffix)
                                            || pathname.getName().startsWith(FileUtils.PDGameAllPicFilePrefix))
                                        return true;
                                }
                                return false;
                            }
                        }
                );
                File desc = findPDGameDescFile(fs);
                if (desc == null) {
                    return false;
                } else {
                    pdGame.initFromJSONSource(FileUtils.readStringFromFile(desc));
                    for (File pic : fs) {
                        if (pic != desc) {
                            allPicFileMap.put(getTurnFromALlPicFile(pic.getName()),
                                    pic);
                        }
                    }
                    minTurn = allPicFileMap.firstKey();
                    maxTurn = allPicFileMap.lastKey();
                    changeTurn(minTurn);
                    //System.out.println(allPicFileMap);
                    changeLocation(0, 0);
                    return true;
                }
            }
            return false;
        }

        private int getPreviousTurn() {
            Integer i = allPicFileMap.lowerKey(turn);
            return i == null ? turn : i;
        }

        private int getNextTurn() {
            Integer i = allPicFileMap.higherKey(turn);
            return i == null ? turn : i;
        }

        private int getTurnFromALlPicFile(String filename) {
            int index1 = FileUtils.PDGameAllPicFilePrefix.length();
            int index2 = filename.indexOf('.');
            return new Integer(filename.substring(index1, index2));
        }

        public boolean changeLocation(int i, int j) {
            if (i >= 0 && i < world.getLength() && j >= 0 && j < world.getLength()) {
                x = i;
                y = j;
                in = world.getIndividual(x, y);
                return true;
            } else {
                return false;
            }
        }

        public void changeTurn(int newTurn) {
            if (newTurn < minTurn) {
                this.turn = minTurn;
            } else if (newTurn > maxTurn) {
                this.turn = maxTurn;
            } else {
                int floor = allPicFileMap.floorKey(newTurn);
                int ceiling = allPicFileMap.ceilingKey(newTurn);
                if (Math.abs(floor - newTurn) < Math.abs(ceiling - newTurn)) {
                    this.turn = floor;
                } else {
                    this.turn = ceiling;
                }
                previousTurn = getPreviousTurn();
                nextTurn = getNextTurn();
                String allPicStr = FileUtils.readStringFromFile(allPicFileMap.get(this.turn));
                world.initFromIndividualAllPicture(allPicStr);
                in = world.getIndividual(x, y);
            }
        }

//        private void sortAllPicFiles() {
//            allPicFileList.sort(new Comparator<File>() {
//                @Override
//                public int compare(File o1, File o2) {
//                    return getTurn(o1.getName()) - getTurn(o2.getName());
//                }
//
//            });
//        }


    }

}
