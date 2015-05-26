package graphic;

import entity.World;
import utils.NamedImage;
import utils.PolyLine;
import utils.TurnAndCooLev;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 用于后期绘制各种图标的类
 */
public class Painter {

    public static final Color CColor = Color.YELLOW;
    public static final Color MColor = Color.RED;
    public static final Color DColor = Color.BLUE;
    /**
     * 图片格式：JPG
     */
    private static final String PICTRUE_FORMATE_JPG = "jpg";

    private static Color getGradient(Color cfrom, Color cto, float f) {
        if (f >= 0 && f <= 1.0f) {
            int r = (int) (cfrom.getRed() + (cto.getRed() - cfrom.getRed()) * f);
            int g = (int) (cfrom.getGreen() + (cto.getGreen() - cfrom
                    .getGreen()) * f);
            int b = (int) (cfrom.getBlue() + (cto.getBlue() - cfrom.getBlue())
                    * f);
            return new Color(r, g, b);
        } else {
            return null;
        }
    }

    private static Color getGradient(Color cfrom, Color cmid, Color cto, float f) {
        if (f >= 0 && f < .5f) {
            return getGradient(cfrom, cmid, f * 2);
        } else if (f <= 1.0f) {
            return getGradient(cmid, cto, (f - 0.5f) * 2);
        } else {
            return null;
        }
    }

    public static Image getPDGameImage(int width, int height, String individualStrategyPicture) {
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);

        int widPerCell, heiPerCell;
        String[] ss = individualStrategyPicture.split("\r\n");
        int L = ss.length;
        widPerCell = (width / L);
        heiPerCell =  (height / L);

        for (int i = 0; i < L; i++) {
            String[] straStrs = ss[i].split("\t");
            for (int j = 0; j < L; j++) {
                if (straStrs[j].compareTo("null") != 0) {
                    g2.setColor(getGradient(DColor, MColor, CColor,
                            Float.parseFloat(straStrs[j])));
                } else {
                    g2.setColor(new Color(0, 0, 0));
                }
                g2.fillRect(j * widPerCell, i * heiPerCell, widPerCell,
                        heiPerCell);
            }
        }
        g2.dispose();
        return bi;
    }

    public static Image getPDGameImage(int width, int height, World world) {

//        BufferedImage bi = new BufferedImage(width, height,
//                BufferedImage.TYPE_INT_RGB);
//
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//
//        g2.setBackground(Color.WHITE);
//        g2.clearRect(0, 0, width, height);
//
//        int widPerCell, heiPerCell;
//        int L = world.getLength();
//        widPerCell = (int) (width / L);
//        heiPerCell = (int) (height / L);
//        Individual in;
//
//        for (int i = 0; i < L; i++) {
//            for (int j = 0; j < L; j++) {
//                if ((in = world.getIndividual(i, j)) != null) {
//                    g2.setColor(getGradient(DColor, MColor, CColor,
//                            in.getStrategy()));
//                } else {
//                    g2.setColor(new Color(0, 0, 0));
//                }
//                g2.fillRect(j * widPerCell, i * heiPerCell, widPerCell,
//                        heiPerCell);
//            }
//        }
//        g2.dispose();
        return getPDGameImage(width, height, world.getIndividualStrategyPicture());
    }

    public static Image composingPopulationImage(ArrayList<NamedImage> images,
                                                 String caption, int column, int imageWidth, int imageHeight,
                                                 int marginLengthX, int marginLengthY, int spacingLengthX,
                                                 int spacingLengthY, int topCaptionSpace) {
        if (images == null) {
            return null;
        }

        int legendSpace = 0;// (int) (imageWidth / 3.5);
        int width = column * (imageWidth + spacingLengthX) + 2 * marginLengthX
                - spacingLengthX + legendSpace + spacingLengthX;
        int row = (int) Math.ceil(images.size() / (double) column);
        int height = row * (imageHeight + spacingLengthY) + 2 * marginLengthY
                - spacingLengthY + topCaptionSpace;

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = bi.createGraphics();
        // g2.setBackground(new Color(0,0,0,0));
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);

        // 画整理好的人口斑图
        Image popuImage = drawPopulationImage(images, column, imageWidth,
                imageHeight, spacingLengthX, spacingLengthY);
        g2.drawImage(popuImage, marginLengthX, marginLengthY - spacingLengthY
                + topCaptionSpace, null);

        // 画图例
//		Image legend = getLegend(100, false);
//		g2.drawImage(legend, width - legendSpace - marginLengthX,
//				marginLengthY, legendSpace, (int) (legendSpace * 7.5), null);

        // 画题注
        int fontSize =  (topCaptionSpace);
        Font font = new Font("黑体", Font.PLAIN, fontSize);
        g2.setColor(Color.BLACK);
        g2.setFont(font);
        g2.drawString(caption, (int) (width / 2.0 - caption.length() * fontSize
                / 2.0 / 2.0), topCaptionSpace);
        g2.dispose();

        return bi;
    }

    public static Image drawPopulationImage(ArrayList<NamedImage> images,
                                            int column, int imageWidth, int imageHeight, // int marginLengthX,
                                            // int
                                            // marginLengthY,
                                            int spacingLengthX, int spacingLengthY) {

        int row = (int) Math.ceil(images.size() / (double) column);
        int width = column * (imageWidth + spacingLengthX) - spacingLengthX;
        int height = row * (imageHeight + spacingLengthY);
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = bi.createGraphics();
        // g2.setBackground(new Color(0,0,0,0));
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("宋体", Font.BOLD, spacingLengthY));

        // sortImageList(images);

        int k = 0;
        int drawX, drawY;
        for (int i = 0; i < row; i++) {
            drawY = spacingLengthY * i + imageHeight * i + spacingLengthY;
            // g2.drawLine(0, drawY,
            // width, drawY);
            for (int j = 0; j < column; j++) {
                drawX = spacingLengthX * j + imageWidth * j;
                // if (i == 0) {
                // g2.drawLine(drawX, 0, drawX, height);
                // }
                g2.drawImage(images.get(k).image, drawX, drawY, imageWidth,
                        imageHeight, null);
                // System.out.println("" + images.get(k).getID());
                g2.drawString("" + images.get(k).getID(), drawX, drawY);
                // g2.fillRect(spacingLength * (j + 1) + imageWidth * j,
                // spacingLength * (i + 1) + imageHeight * i, imageWidth,
                // imageHeight);
                k++;
                if (k >= images.size()) {
                    break;
                }
            }
        }
        // ImageComposer.printPicture(bi);
        g2.dispose();

        return bi;
    }

    private static Image getLegend(int width, boolean vertical) {
        int height = width * 15;
        int leftMargin = width;

        BufferedImage bi = new BufferedImage(width + leftMargin, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();

        g2.setBackground(Color.WHITE);
        // g2.setBackground(Color.GRAY);
        g2.clearRect(0, 0, width + leftMargin, height);

        for (int i = 0; i < height; i++) {
            g2.setColor(getGradient(CColor, MColor, DColor, i / (float) height));
            g2.fillRect(0, i, width, 1);
        }

        g2.setColor(Color.BLACK);
        int fontSize = (int) (leftMargin / 1.5);
        g2.setFont(new Font("新宋", Font.PLAIN, fontSize));
        // g2.drawRect(0, 0, width-1, height-1);
        float[] scale = {0, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
        float scaleDepth = 0.3f;
        int lineBold = height / 200;
        DecimalFormat df = new DecimalFormat("0.0");
        for (float sc : scale) {
            g2.fillRect(0, (int) (sc * height), (int) (width * scaleDepth),
                    lineBold);
            g2.fillRect((int) (width * (1 - scaleDepth)), (int) (sc * height),
                    (int) (width * scaleDepth), lineBold);
            g2.drawString("" + df.format(1 - sc), width,
                    (int) (sc * (height - fontSize)) + fontSize);
        }
        g2.dispose();
        return bi;
    }

    private static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                             final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
                .setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    public static Image drawCooperationLevelPolygon(
            ArrayList<TurnAndCooLev> list, ArrayList<Integer> snapshotTurn,
            int chartWidth, int chartHeight, int marginLengthX,
            int marginLengthY, int CoordinateAxisYSpace,
            int CoordinateAxisXHeight, int rightFixSpace) {
        int width = chartWidth + marginLengthX * 2 + CoordinateAxisYSpace
                + rightFixSpace;
        int height = chartHeight + marginLengthY * 2 + CoordinateAxisXHeight;
        int marginLeftLen = marginLengthX + CoordinateAxisYSpace;
        int marginTopLen = marginLengthY;
        int x0 = marginLeftLen;
        int y0 = height - marginLengthY - CoordinateAxisXHeight;
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setBackground(Color.WHITE);
        // g2.setBackground(Color.GRAY);
        g2.clearRect(0, 0, width, height);

        g2.setColor(Color.BLACK);
        PolyLine pl = convertListToPolyLine(list);

        int x[], y[];
        x = projectPointXToChartX(pl, x0, y0, chartWidth, chartHeight);
        y = projectPointYToChartY(pl, x0, y0, chartWidth, chartHeight);

        // System.out.println(snapshotTurn);
        drawCoordinateAxisX(g2, x0, y0, chartHeight, chartWidth,
                CoordinateAxisXHeight, pl, x, snapshotTurn);
        drawCoordinateAxisY(g2, x0, y0, chartHeight, chartWidth,
                CoordinateAxisYSpace);

        // g2.drawString(""+x[0]+","+y[0], marginLengthX, height
        // -marginLengthY);
        g2.setColor(Color.RED);
        drawPolyline(g2, x, y, x.length);
        g2.dispose();
        return bi;
    }

    private static void drawPolyline(Graphics2D g2, int[] x, int[] y, int num) {
        g2.setColor(Color.blue);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < num; j++) {
                y[j] += i - 1;
            }
            g2.drawPolyline(x, y, x.length);
        }

        for (int j = 0; j < num; j++) {
            y[j] -= 1;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < num; j++) {
                x[j] += i - 1;
            }
            g2.drawPolyline(x, y, x.length);
        }
        g2.setColor(Color.BLACK);
    }

    private static PolyLine convertListToPolyLine(ArrayList<TurnAndCooLev> list) {
        float[] x = new float[list.size()];
        float[] y = new float[list.size()];
        list.sort(new Comparator<TurnAndCooLev>() {

            @Override
            public int compare(TurnAndCooLev o1, TurnAndCooLev o2) {
                // TODO Auto-generated method stub
                return o1.turn - o2.turn;
            }
        });

        for (int i = 0; i < x.length; i++) {
            x[i] = list.get(i).turn;
            y[i] = list.get(i).cooLev;
        }
        return new PolyLine(x, y, x.length, x[0], x[x.length - 1], 0, 1.0f);
    }

    private static int[] projectPointXToChartX(PolyLine pl, int x0, int y0,
                                               int chartWidth, int chartHeight) {
        int[] px = new int[pl.pointX.length];
        // float step = chartWidth/(pl.xRangeE-pl.xRangeS);
        // for(int i = 0; i< px.length; i++){
        // //px[i] = (int) (pl.pointX[i]*step + marginLeftLen);
        // px[i] = (int)
        // (Math.log(pl.pointX[i])/Math.exp(pl.pointNum)*chartWidth +
        // marginLeftLen);
        // }
        double r = 1d / 1.0025;
        double a = (1 - r) * chartWidth / (1 - Math.pow(r, pl.xRangeE));
        double[] s = new double[(int) pl.xRangeE];
        // System.out.println(a1);
        for (int i = 0; i < px.length; i++) {
            px[i] = (int) (a * (1 - Math.pow(r, pl.pointX[i])) / (1 - r) + x0);
        }
        // System.out.println("size:" + px.length + "\n" + Arrays.toString(px));
        return px;
    }

    private static int[] projectPointYToChartY(PolyLine pl, int x0, int y0,
                                               int chartWidth, int chartHeight) {
        int[] py = new int[pl.pointY.length];
        float step = chartHeight / (pl.yRangeE - pl.yRangeS);
        for (int i = 0; i < py.length; i++) {
            // System.out.println(""+pl.pointY[i]+","+step);
            py[i] = (int) (y0 - pl.pointY[i] * step);
        }
        return py;
    }

    private static void drawCoordinateAxisY(Graphics2D g2, int x0, int y0,
                                            int chartHeight, int chartWidth, int leftSpace) {
        String nameY = "Cooperation Level";
        g2.drawLine(x0, y0, x0, y0 - chartHeight);
        // g2.drawLine(x0 + chartWidth, y0, x0 + chartWidth, y0 - chartHeight);
        float step = chartHeight / 10.0f;
        int fontSize = 20;
        g2.setFont(new Font("宋体", Font.PLAIN, fontSize));
        for (int i = 0; i <= 10; i++) {
            g2.drawLine(x0, (int) (y0 - step * i), x0 + 5,
                    (int) (y0 - step * i));
            g2.drawString("" + i / 10.f, x0 - fontSize * 2, (int) (y0 - step
                    * i + fontSize * 0.25));
            g2.drawLine(x0 + chartWidth, (int) (y0 - step * i), x0 + chartWidth
                    - 5, (int) (y0 - step * i));
        }
        g2.rotate(Math.toRadians(-90));
        g2.setFont(new Font("宋体", Font.BOLD, (int) (fontSize * 1.2)));
        g2.drawString(nameY, (int) (-y0 + chartHeight / 2 - nameY.length()
                * fontSize / 2.5), x0 - fontSize * 3);
        g2.rotate(Math.toRadians(90));
    }

    private static void drawCoordinateAxisX(Graphics2D g2, int x0, int y0,
                                            int chartHeight, int chartWidth, int BottomSpace, PolyLine pol,
                                            int x[], ArrayList<Integer> snapshotTurn) {
        String nameX = "Turn";
        snapshotTurn.sort(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                // TODO Auto-generated method stub
                return o1 - o2;
            }
        });
        g2.drawLine(x0, y0, x0 + chartWidth, y0);
        g2.drawLine(x0, y0 - chartHeight, x0 + chartWidth, y0 - chartHeight);
        int fontSize = 20;
        g2.setFont(new Font("宋体", Font.PLAIN, fontSize));
        // System.out.println(x.length);
        Iterator<Integer> it = snapshotTurn.iterator();
        // System.out.println(snapshotTurn);
        for (int i = 0; i < x.length; i++) {

            // System.out.println("i=" + i + "," + (int) (pol.pointX[i]));
            int key = (int) (pol.pointX[i]);
            if ((key < 20 && (key == 0 || key == 5))
                    || (key >= 20 && key < 700)
                    || (key >= 700 && (i == x.length - 1 || snapshotTurn
                    .contains(key)))) {
                g2.drawString("" + (int) (pol.pointX[i]),
                        (int) (x[i] - fontSize * 0.25), y0 + fontSize);
            }
            if (snapshotTurn.contains(key)) {
                g2.setColor(Color.RED);
                drawVertivalDottedLine(g2, x[i], y0, chartHeight);
                // g2.drawLine(x[i], y0 - chartHeight, x[i], y0);
                g2.setColor(Color.BLACK);
            }
            g2.drawLine(x[i], y0 - chartHeight, x[i], y0 - chartHeight + 5);
            g2.drawLine(x[i], y0, x[i], y0 - 5);
        }
        g2.setFont(new Font("宋体", Font.BOLD, (int) (fontSize * 1.2)));
        g2.drawString(nameX, x0 + chartWidth / 2 - nameX.length() * fontSize
                / 2, (int) (y0 + fontSize * 2.5));
    }

    public static void drawVertivalDottedLine(Graphics2D g2, int x0, int y0,
                                              int height) {
        float num = height / 5f;
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0)
                g2.drawLine(x0, y0 - 5 * i, x0, y0 - 5 * (i + 1));
        }
    }

    public static Image assembleImages(ArrayList<Image> images, int marginX,
                                       int marginY) {
        if (images.size() != 4)
            return null;
        int imageWidth = images.get(0).getWidth(null);
        int imageHeight = images.get(1).getHeight(null);
        int legendSpace = (int) (imageHeight / 3.5);
        int width = imageWidth + marginX * 2;
        int height = imageHeight * 4 + marginY * 5 + legendSpace;

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setBackground(Color.WHITE);
        // g2.setBackground(Color.GRAY);
        g2.clearRect(0, 0, width, height);
        int fontSize = imageHeight / 8;
        for (int i = 0; i < 4; i++) {
            int dx = marginX;
            int dy = marginY + (marginY + imageHeight) * i;
            g2.drawImage(images.get(i), dx, dy, null);
            g2.setFont(new Font("黑体", Font.PLAIN, fontSize));
            g2.setColor(Color.BLACK);
            g2.drawString("(" + (char) ('a' + i) + ")", dx + fontSize / 4, dy
                    + fontSize);
        }
        Image legend = getLegend(100, true);
        g2.rotate(Math.toRadians(90));
        int legendwidth = legendSpace;
        int legendHeight = (int) (legendSpace * 7.5);
        g2.drawImage(legend, height - legendwidth,
                -(width / 2 + legendHeight / 2), legendwidth, legendHeight, null);
        //ImageComposer.printPicture(bi);
        g2.dispose();
        return bi;
    }

    public static void main(String[] args) {
        // int width = 100;
        // int height = 100;
        // String s = "你好";
        //
        // File file = new File("c:/image.jpg");
        //
        // Font font = new Font("Serif", Font.BOLD, 10);
        // BufferedImage bi = new BufferedImage(width, height,
        // BufferedImage.TYPE_INT_RGB);
        //
        // Graphics2D g2 = (Graphics2D)bi.getGraphics();
        // g2.setColor(Color.WHITE);
        // g2.fillRect(0, 0,width, height);
        // //g2.setBackground(Color.WHITE);
        // //g2.clearRect(0, 0, width, height);
        // g2.setPaint(Color.RED);
        //
        // FontRenderContext context = g2.getFontRenderContext();
        // Rectangle2D bounds = font.getStringBounds(s, context);
        // double x = (width - bounds.getWidth()) / 2;
        // double y = (height - bounds.getHeight()) / 2;
        // double ascent = -bounds.getY();
        // double baseY = y + ascent;
        //
        // g2.drawString(s, (int)x, (int)baseY);
        //
        // try {
        // ImageIO.write(bi, "jpg", file);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

}
