package gui;

import entity.World;
import graphic.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by hyx on 2015/6/3.
 */
public class PicturePanel extends JPanel implements Runnable {

    public static final int MARGIN_SIZE = 5;
//    private int squareX = 50;
//    private int squareY = 50;
//    private int squareW = 20;
//    private int squareH = 20;

    private int imageX = 0;
    private int imageY = 0;
    private int imageSize = 0;

    World world;
    int spotX, spotY;
    int spotSize;
    boolean showSpot;
    Color spotColor;
    MouseClickLocationChangeListener locationChangeListener;


    public PicturePanel(MouseClickLocationChangeListener listener) {

        setBackground(Color.WHITE);

        showSpot = false;
        spotColor = Color.GREEN;

        locationChangeListener = listener;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                //moveSquare(x, y);
                if (world != null) {
                    int j = (x - imageX) * world.getLength() / imageSize;
                    int i = (y - imageY) * world.getLength() / imageSize;
                    locationChangeListener.changeLocationTo(i, j);
                }
            }
        });

//        addMouseMotionListener(new MouseAdapter() {
//            public void mouseDragged(MouseEvent e) {
//                moveSquare(e.getX(), e.getY());
//            }
//        });
        new Thread(this).start();
    }

//    private void moveSquare(int x, int y) {
//        int OFFSET = 1;
//        if ((squareX != x) || (squareY != y)) {
//            repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
//            squareX = x;
//            squareY = y;
//            repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
//        }
//    }


    public void setWorld(World world, int i, int j) {
        this.world = world;
        if (this.world != null) {
            spotSize = imageSize / world.getLength();
            setSpotLocation(i, j);
            repaint(0, 0, getWidth(), getHeight());
        }
    }

    public void setSpotLocation(int i, int j) {
        if (checkSpotLocation(i, j)) {
            repaint(spotX, spotY, spotSize, spotSize);
            spotX = imageX + j * spotSize;
            spotY = imageY + i * spotSize;
            repaint(spotX, spotY, spotSize, spotSize);
        }
    }

    private boolean checkSpotLocation(int i, int j) {
        if (world != null) {
            if (i >= 0 && i < world.getLength()
                    && j >= 0 && j < world.getLength()) {
                return true;
            }
        }
        return false;
    }

    private void updateImageParam() {
        int h = getHeight();
        int w = getWidth();
        if (h > w) {
            imageSize = w - 2 * MARGIN_SIZE;
            imageX = MARGIN_SIZE;
            imageY = (h - imageSize) / 2;
        } else {
            imageSize = h - 2 * MARGIN_SIZE;
            imageY = MARGIN_SIZE;
            imageX = (w - imageSize) / 2;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateImageParam();
        if (world != null) {
            Painter.drawPDGameImage((Graphics2D) g, imageX, imageY, imageSize, imageSize, world);
            if (showSpot) {
                g.setColor(spotColor);
                g.fillRect(spotX, spotY, spotSize, spotSize);
                //System.out.println("show spot");
            }
        }
//
//        g.setColor(Color.black);
//        g.drawString("This is my custom Panel!", 10, 20);
//        g.setColor(Color.RED);
//        g.fillRect(squareX, squareY, squareW, squareH);
//        g.setColor(Color.BLACK);
//        g.drawRect(squareX, squareY, squareW, squareH);
    }

    @Override
    public void run() {
        //long start = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(800);

                showSpot = !showSpot;
                repaint(spotX, spotY, spotSize, spotSize);


            } catch (InterruptedException e) {

            }
        }
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new PicturePanel(null));
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
