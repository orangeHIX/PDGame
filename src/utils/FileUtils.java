package utils;

import graphic.Painter;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Map;

public class FileUtils {

    public static final String PDGameJSONFileSuffix = ".pdgame";
    public static final String PDGameAllPicFilePrefix = "all_";
    public static final String PDGameStraPicFilePrefix = "turn_";

    public static final String Encoding = System.getProperty("file.encoding");

    /**
     * 删除该文件夹即文件夹下所有文件
     */
    synchronized public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] ff = file.listFiles();
            for (int i = 0; i < ff.length; i++) {
                deleteFile(ff[i].getPath());
            }
        }
        file.delete();
    }

    synchronized public static void outputSnapshootToFile(String filePath,
                                             Map<Integer, Snapshot> Snapshot) {
        outputSnapshootToFile(filePath, null, Snapshot);
    }

    synchronized public static void outputSnapshootToFile(String filePath, String PDGameJSONString,
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

        }
    }

    synchronized public static void outputImageToFile(RenderedImage image,
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
    synchronized public static void outputToFile(String fileName, String str) {
        File f = getFile(fileName);
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName),Encoding) ) {
            out.write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取该文件对象，如果该文件对象不存在，则创建它
     */
    synchronized public static File getFile(String fileName) {
        File f = new File(fileName);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        return f;
    }

    synchronized public static String readStringFromFile(File f) {
        StringBuilder sb = new StringBuilder();
        if (f.exists() && f.isFile()) {
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(f),Encoding)) {
                char[] buff = new char[1024];
                int n;
                while ((n = isr.read(buff)) > 0) {
                    sb.append(buff, 0, n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static void main(String[] args){
        System.out.print(Encoding +"的萨芬两三年"+"你好啊");
    }

}
