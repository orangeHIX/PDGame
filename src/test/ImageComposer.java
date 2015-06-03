package test;

import graphic.Painter;
import utils.FileUtils;
import utils.NamedImage;
import utils.TurnAndCooLev;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * 用于重排图表的类，不必理会
 */
public class ImageComposer {
    public static void printPicture(final Image image) {
        // BufferedImage bi = null;
        // final ArrayList<graphic.NamedImage> imageList = //
        // readImages("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju");
        // readImages("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju\\"
        // +
        // "max_payoff_learning_$_optimistic_migrate_$_continuous_strategy_$_pi=0.50_$_qi=0.00\\"
        // + "gr=(1.00,-0.10,1.90,0.00)_$_d0=0.50");

        // 创建frame

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        final float proportion = imageWidth / (float) imageHeight;
        JFrame frame = new JFrame() {
            public static final int MarginWith = 40;

            @Override
            public void paint(Graphics g) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());
                g.drawImage(image, MarginWith, MarginWith, (int) (1000),
                        (int) (1000 / proportion), null);
            }
        };
        // 调整frame的大小和初始位置
        frame.setSize(1000, 880);
        frame.setLocation(100, 100);
        // 增加窗口监听事件，使用内部类方法，并用监听器的默认适配器
        frame.addWindowListener(new WindowAdapter() {

            // 重写窗口关闭事件
            @Override
            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }

        });
        frame.setTitle("test");
        // 显示窗体
        frame.setVisible(true);
    }

    private static ArrayList<NamedImage> readImages(File f) {

        if (!f.isDirectory()) {
            return null;
        }

        BufferedImage bi = null;
        ArrayList<NamedImage> imageList = new ArrayList<>();

        if (f != null && f.isDirectory()) {
            File[] fs = f.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    // TODO Auto-generated method stub
                    if (name.startsWith("turn_") && name.endsWith(".jpg")) {
                        return true;
                    }
                    return false;
                }
            });

            try {
                for (File imagef : fs) {
                    bi = ImageIO.read(imagef);
                    if (bi != null) {
                        imageList.add(new NamedImage(imagef.getName(), bi));
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        imageList.sort(new Comparator<NamedImage>() {

            @Override
            public int compare(NamedImage o1, NamedImage o2) {
                // TODO Auto-generated method stub
                return o1.getID() - o2.getID();
            }
        });
        return imageList;
    }

    private static ArrayList<TurnAndCooLev> readCooLev(File f) {
        ArrayList<TurnAndCooLev> list = new ArrayList<>();
        try (Scanner sc = new Scanner(f)) {
            String s = "";
            for (s = sc.nextLine(); sc.hasNext(); s = sc.nextLine()) {
                if (s.contains("turn") && s.contains("coopLev")) {
                    break;
                }
            }
            String[] ss;
            ss = sc.nextLine().split("\t");
            for (; ss.length > 1; ss = sc.nextLine().split("\t")) {
                list.add(new TurnAndCooLev(Integer.parseInt(ss[0]), Float
                        .parseFloat(ss[1])));
            }

        }
        // turn coopLev
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(list);
        return list;
    }

    public static void composeImageFromFile(String dataFilePath) {

        File[] fs1 = new File(dataFilePath).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // TODO Auto-generated method stub
                return name.contains("_$_");
            }
        });
        for (File f1 : fs1) {
            // System.out.println(f1.getName());
            if (f1.isDirectory()) {
                File[] fs2 = f1.listFiles();
                composePopulationImageFromFile(fs2, getQIString(f1.getName())
                        + ", " + getStrategyPatternString(f1.getName()), "");

                // for (File f2 : fs2) {
                // if (!f2.isDirectory() && f2.getName().endsWith(".txt")) {
                // // File[] fs3 = f2.listFiles();
                // // System.out.println(f2.getName());
                // composeChartImageFromFile(
                // f2,
                // f1.getName() + "_$_" + f2.getName(),
                // getTurnFromImageList(new File(f2
                // .getAbsolutePath().replace(".txt", ""))));
                // // System.out.println();
                // }
                // }
            }
        }
    }

    private static String getStrategyPatternString(String s) {
        int index;
        if ((index = s.indexOf("strategy")) != -1
                && (s.charAt(index - 1) == '_' || s.charAt(index - 1) == ' ')) {
            int i = index - 2;
            for (; i >= 0; i--) {
                if (s.charAt(i) == '_' || s.charAt(i) == ' ') {
                    break;
                }
            }
            String sub = s.substring(i + 1, index - 1);
            if (sub.compareTo("two") == 0) {
                sub = "discrete";
            }
            return sub + " strategy";
        }
        return "";
    }

    private static String getQIString(String s) {
        Float qi = getQi(s);
        if (qi.equals(Float.NaN))
            return "";
        else {
            DecimalFormat df = new DecimalFormat("0.00");
            return "m=" + df.format(qi);
        }
    }

    public static Float getQi(String s) {
        int index;
        if ((index = s.indexOf("qi=")) != -1) {
            int end = s.indexOf("_", index);

            String qis;
            if (end != -1)
                qis = s.substring(index + "qi=".length(), end);
            else
                qis = s.substring(index + "qi=".length(), s.length());

            return Float.parseFloat(qis);
        }
        return Float.NaN;
    }

    public static void composePopulationImageFromFile(File[] fs,
                                                      String captionString, String outputFileNamePre) {
        ArrayList<utils.NamedImage> imageList;
        int column = 7;
        int zoom = 2;
        Image image = null;
        for (File f2 : fs) {
            if (f2.isDirectory()) {
                imageList = readImages(f2);
                // System.out.println(f2.getAbsolutePath());
                removeUnnecessaryImage(imageList);
                // new File(f2.getAbsolutePath() + "\\"
                // + captionString
                // + ".jpg").delete();
                String expand = captionString + ", "
                        + getDrAndDgFromString(f2.getName());
                image = Painter.composingEvolutionImage(imageList, expand,
                        column, (int) (120 * zoom), (int) (120 * zoom),
                        (int) (5 * zoom), (int) (15 * zoom), (int) (3 * zoom),
                        (int) (15 * zoom), (int) (25 * zoom));
                //printPicture(image);
                if (image != null) {
                    // new File(f2.getAbsolutePath() + "\\"
                    // + outputFileNamePre + "_$_" + f2.getName()+"_new"
                    // + ".jpg").delete();
//					 FileUtils.outputImageToFile((RenderedImage) image, "jpg",
//					 new File(f2.getParentFile().getParentFile()
//					 .getAbsolutePath()
//					 + "\\" + expand + ".jpg"));
                    // printPicture(image);
                }
            }
        }
    }

    private static String getDrAndDgFromString(String s) {
        Float Dr = getDr(s);
        Float Dg = getDg(s);
        if (Dr.equals(Float.NaN) || Dg.equals(Float.NaN)) {
            return "";
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            return "Dr=" + df.format(Dr) + ", Dg=" + df.format(Dg - 1.0f);
        }

    }

    public static Float getDr(String s) {
        int start;
        int end;
        if ((start = s.indexOf("gr=(")) != -1
                && (end = s.indexOf(")", start)) != -1) {
            String sub = s.substring(start + "gr=(".length(), end);
            String[] ss = sub.split(",");

            return Float.parseFloat(ss[1]);
        }
        return Float.NaN;
    }

    public static Float getDg(String s) {
        int start;
        int end;
        if ((start = s.indexOf("gr=(")) != -1
                && (end = s.indexOf(")", start)) != -1) {
            String sub = s.substring(start + "gr=(".length(), end);
            String[] ss = sub.split(",");

            return Float.parseFloat(ss[2]);
        }
        return Float.NaN;
    }

    public static void composeChartImageFromFile(File f, String outputFileName,
                                                 ArrayList<Integer> snapshotTurn) {
        ArrayList<TurnAndCooLev> list = readCooLev(f);
        Image image = Painter.drawCooperationLevelPolygon(list, snapshotTurn,
                800, 500, 20, 20, 80, 40, 30);
        // printPicture(image);
        if (image != null) {
            FileUtils.outputImageToFile((RenderedImage) image, "jpg", new File(
                    f.getParentFile().getAbsolutePath() + "\\" + outputFileName
                            + "_$_poly.jpg"));
        }

    }

    private static ArrayList<Integer> getTurnFromImageList(File imageFile) {
        // System.out.println(imageFile.getAbsolutePath() + " "
        // + imageFile.isDirectory());
        ArrayList<NamedImage> imageList = readImages(imageFile);
        removeUnnecessaryImage(imageList);
        ArrayList<Integer> list = new ArrayList<>();
        for (NamedImage nim : imageList) {
            list.add(nim.getID());
        }
        return list;
    }

    private static void removeUnnecessaryImage(ArrayList<NamedImage> imageList) {
        // 省略多余图片
        if (imageList.size() > 7) {
            final int[] ids = {0, 1, 3, 5, 10};
            int listmiddle = ids.length + (imageList.size() - ids.length) / 2;
            final int[] excep = {imageList.get(listmiddle).getID(),
                    imageList.get(imageList.size() - 1).getID()};
            imageList.removeIf(new Predicate<NamedImage>() {

                @Override
                public boolean test(NamedImage t) {
                    // TODO Auto-generated method stub
                    int id = t.getID();
                    if (Arrays.binarySearch(ids, id) < 0
                            && Arrays.binarySearch(excep, id) < 0) {
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public static void removeJPGExceptTurnImage(File f) {
        if (f.exists() && f.isDirectory()) {
            File[] fs = f.listFiles();
            for (File f2 : fs) {
                if (f2.getName().endsWith(".jpg")
                        && !f2.getName().contains("turn")) {
                    f2.delete();
                } else {
                    removeJPGExceptTurnImage(f2);
                }
            }
        }
    }

    public static void assembleFourImagesWithSameSize(String path,
                                                      String outputPath) {
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
            File[] fs = f.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    // TODO Auto-generated method stub
                    if (name.endsWith(".jpg") && name.contains("m")
                            && name.contains("strategy") && name.contains("Dr")
                            && name.contains("Dg"))
                        return true;
                    return false;
                }
            });

            ArrayList<Image> list = new ArrayList<>();
            ArrayList<Image> list2 = new ArrayList<>();
            Image bi;
            try {
                for (File fi : fs) {
                    System.out.println(fi.getName());
                    bi = ImageIO.read(fi);
                    if (bi != null)
                        list.add(bi);
                    if (list.size() == 4) {
                        list2.add(Painter.assembleEvolutionImages(list, 20, 20));
                        list.clear();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int k = 1;
            for (Image image : list2) {
                FileUtils.outputImageToFile((RenderedImage) image, "jpg",
                        new File(f.getAbsolutePath() + "\\" + k + ".jpg"));
                k++;
            }
        }
    }

    public static void main(String[] args) {

//		composeImageFromFile("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju");

        assembleFourImagesWithSameSize(
                "C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju", "");
        // removeJPG(new File("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju"));
        // printPicture(Painter.drawCooperationLevelPolygon(800, 800, 20, 20));
    }
}
