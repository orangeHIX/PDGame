package utils;

import graphic.Painter;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileUtils {

    public static final String PDGameJSONFileSuffix = ".pdgame";
    public static final String PDGameAllPicFilePrefix = "all_";
    public static final String PDGameStraPicFilePrefix = "turn_";

    /**
     * 删除该文件夹即文件夹下所有文件
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] ff = file.listFiles();
            for (int i = 0; i < ff.length; i++) {
                deleteFile(ff[i].getPath());
            }
        }
        file.delete();
    }

    public static void outputSnapshootToFile(String filePath,
                                             Map<Integer, Snapshot> Snapshot) {
        outputSnapshootToFile(filePath, null, Snapshot);
    }

    public static void outputSnapshootToFile(String filePath, String PDGameJSONString,
                                             Map<Integer, Snapshot> Snapshot) {
        FileUtils.deleteFile(filePath);
        for (Integer i : Snapshot.keySet()) {
            //File f = getFile();
            outputToFile(filePath + "\\txt_format\\" + PDGameStraPicFilePrefix + i + ".txt",
                    Snapshot.get(i).individualStrategyPicture);
            RenderedImage im = (RenderedImage) Painter.getPDGameImage(
                    400,
                    400,
                    Snapshot.get(i).individualStrategyPicture);
            File f = getFile(filePath + "\\" + PDGameStraPicFilePrefix + i + ".jpg");
            outputImageToFile(im, "jpg", f);

            outputToFile(filePath + "\\txt_format\\"
                            + PDGameAllPicFilePrefix + i + ".txt",
                    Snapshot.get(i).allPicture);
            if (PDGameJSONString != null) {
                outputToFile(filePath + "\\txt_format\\" + PDGameJSONFileSuffix,
                        PDGameJSONString);
            }
//            RenderedImage im2 = (RenderedImage)Painter.getPDGameImage(
//                    400,
//                    400,
//                    Snapshot.get(i).individualPayoffPicture);
//            File f2 = getFile(filePath + "\\payoff_" + i + ".jpg");
            //outputImageToFile(im2, "jpg", f2);
        }
    }
//    public static void outputSnapshotToFile(String filePath,
//                                             Map<Integer, Image> Snapshot) {
//
//        FileUtils.deleteFile(filePath);
//        for (Integer i : Snapshot.keySet()) {
//            File f = getFile(filePath + "\\turn_" + i + ".jpg");
//            outputImageToFile((RenderedImage) Snapshot.get(i), "jpg", f);
//        }
//
//    }

    public static void outputImageToFile(RenderedImage image,
                                         String formatName, File output) {
        try {
            ImageIO.write((RenderedImage) image, "jpg", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param fileName String The system-dependent filename
     * @param str      要输出到文件的内容
     */
    public static void outputToFile(String fileName, String str) {
        File f = getFile(fileName);
        try (FileWriter w = new FileWriter(f.getAbsolutePath())) {
            w.write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取该文件对象，如果该文件对象不存在，则创建它
     */
    public static File getFile(String fileName) {
        File f = new File(fileName);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        return f;
    }

    public static String readStringFromFile(File f) {
        StringBuilder sb = new StringBuilder();
        if (f.exists() && f.isFile()) {
            try (FileReader fr = new FileReader(f)) {
                char[] buff = new char[1024];
                int n;
                while ((n = fr.read(buff)) > 0) {
                    sb.append(buff, 0, n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
