package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description: 扩展JTabbedPane, 增加关闭、右键菜单、缩略图提示功能
 */
public class YiTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 2985098138273905480L;

    private TabChangeListener tabChangeListener;


    public YiTabbedPane() {
        super();
        addMouseListener(YiTabbedPane.mouseAdapter);
        addTab(" ", new Panel());
        //setTabComponentAt(getTabCount() - 1, new AddTabComponent());
    }

    public void setTabChangeListener(TabChangeListener listener) {
        this.tabChangeListener = listener;
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        if(getTabCount() > 1){
            index--;
        }
        super.insertTab(title, icon, component, tip, index);

        setTabComponentAt(index, new CloseButtonTabComponent(this));

        if(tabChangeListener != null) {
            tabChangeListener.notifyInsertTabAt(index);
        }

    }



    @Override
    public void removeTabAt(int index) {
        Component component = getComponentAt(index);

        if( getTabCount() > 2) {
            if(tabChangeListener != null) {
                tabChangeListener.notifyRemoveTabAt(index);
            }
            super.removeTabAt(index);
        }else{
            JComponent parantComponent = YiTabbedPane.this.getRootPane();
            if(parantComponent == null){
                parantComponent = YiTabbedPane.this;
            }
            JOptionPane.showMessageDialog(parantComponent,
                    "this is the last tab");
            //YiTabbedPane.this.requestFocus();
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        if(index != getTabCount() -1) {
            super.setSelectedIndex(index);
        } else {
            insertTab("new tab", null, null, null, index);
        }
    }

    private static final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (SwingUtilities.isRightMouseButton(e)) {
                YiTabbedPane tabbedPane = (YiTabbedPane)e.getComponent();
                tabbedPane.showPopupMenu(e);
            }
        }
    };


    private void showPopupMenu(final MouseEvent event) {

        // 如果当前事件与右键菜单有关（单击右键），则弹出菜单
        if (event.isPopupTrigger()) {
            final int index = ((YiTabbedPane) event.getComponent()).getUI().tabForCoordinate(this, event.getX(), event.getY());
            final int count = ((YiTabbedPane) event.getComponent()).getTabCount();
            JPopupMenu pop = new JPopupMenu();
            JMenuItem closeCurrent = new JMenuItem("Close");
            closeCurrent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((YiTabbedPane) event.getComponent()).removeTabAt(index);
                }
            });
            pop.add(closeCurrent);

            JMenuItem closeLeft = new JMenuItem("Close Left Tabs");
            closeLeft.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int j = (index - 1); j >= 0; j--) {
                        ((YiTabbedPane) event.getComponent()).removeTabAt(j);
                    }
                }
            });
            pop.add(closeLeft);

            JMenuItem closeRight = new JMenuItem("Close Right Tabs");
            closeRight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int j = (count - 1); j > index; j--) {
                        ((YiTabbedPane) event.getComponent()).removeTabAt(j);
                    }
                }
            });
            pop.add(closeRight);

            pop.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    /**
     * Component to be used as tabComponent;
     * Contains a JLabel to show the text and
     * a JButton to close the tab it belongs to
     */
    public static class CloseButtonTabComponent extends JPanel {
        private final JTabbedPane pane;

        public CloseButtonTabComponent(final JTabbedPane pane) {
            //unset default FlowLayout' gaps
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            if (pane == null) {
                throw new NullPointerException("TabbedPane is null");
            }
            this.pane = pane;
            setOpaque(false);

            //make JLabel read titles from JTabbedPane
            JLabel label = new JLabel() {
                public String getText() {
                    int i = pane.indexOfTabComponent(CloseButtonTabComponent.this);
                    if (i != -1) {
                        return pane.getTitleAt(i);
                    }
                    return null;
                }
            };

            add(label);
            //add more space between the label and the button
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            //tab button
            JButton button = new CloseTabButton();
            add(button);
            //add more space to the top of the component
            setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        }

        private class CloseTabButton extends JButton implements ActionListener {
            public CloseTabButton() {
                int size = 17;
                setPreferredSize(new Dimension(size, size));
                setToolTipText("close this tab");
                //Make the button looks the same for all Laf's
                setUI(new BasicButtonUI());
                //Make it transparent
                setContentAreaFilled(false);
                //No need to be focusable
                setFocusable(false);
                setBorder(BorderFactory.createEtchedBorder());
                setBorderPainted(false);
                //Making nice rollover effect
                //we use the same listener for all buttons
                addMouseListener(buttonMouseListener);
                setRolloverEnabled(true);
                //Close the proper tab by clicking the button
                addActionListener(this);
            }

            public void actionPerformed(ActionEvent e) {
                int i = pane.indexOfTabComponent(CloseButtonTabComponent.this);
                if (i != -1) {
                    pane.remove(i);
                }
            }

            //we don't want to update UI for this button
            public void updateUI() {
            }

            //paint the cross
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                //shift the image for pressed buttons
                if (getModel().isPressed()) {
                    g2.translate(1, 1);
                }
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.BLACK);
                if (getModel().isRollover()) {
                    g2.setColor(Color.MAGENTA);
                }
                int delta = 6;
                g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
                g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
                g2.dispose();
            }
        }

        private final static MouseListener buttonMouseListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Component component = e.getComponent();
                if (component instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                Component component = e.getComponent();
                if (component instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(false);
                }
            }
        };
    }

}
//
//class CloseTabIcon implements Icon {
//    private int x_pos;
//    private int y_pos;
//    private int width;
//    private int height;
//    private Icon fileIcon;
//
//    public CloseTabIcon(Icon fileIcon) {
//        this.fileIcon = fileIcon;
//        width = 16;
//        height = 16;
//    }
//
//    public void paintIcon(Component c, Graphics g, int x, int y) {
//        this.x_pos = x;
//        this.y_pos = y;
//        int y_p = y + 2;
//        Color col = g.getColor();
//        g.setColor(Color.black);
//        g.drawLine(x + 1, y_p, x + 12, y_p);
//        g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
//        g.drawLine(x, y_p + 1, x, y_p + 12);
//        g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
//        g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
//        g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
//        g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
//        g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
//        g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
//        g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
//        g.setColor(col);
//        if (fileIcon != null) {
//            fileIcon.paintIcon(c, g, x + width, y_p);
//        }
//    }
//
//    public int getIconWidth() {
//        return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
//    }
//
//    public int getIconHeight() {
//        return height;
//    }
//
//    public Rectangle getBounds() {
//        return new Rectangle(x_pos, y_pos, width, height);
//    }
//}
