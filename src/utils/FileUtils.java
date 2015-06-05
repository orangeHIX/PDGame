package utils;

import graphic.Painter;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class FileUtils {

    public static final String PDGameSnapshotTxtFormatDirectName = "txt_format";
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
            outputToFile(filePath + "\\" + PDGameSnapshotTxtFormatDirectName + "\\"
                            + PDGameStraPicFilePrefix + i + ".txt",
                    Snapshot.get(i).individualStrategyPicture);
            outputToFile(filePath + "\\" + PDGameSnapshotTxtFormatDirectName + "\\"
                            + PDGameAllPicFilePrefix + i + ".txt",
                    Snapshot.get(i).allPicture);

            RenderedImage im = (RenderedImage) Painter.getPDGameImage(
                    400,
                    400,
                    Snapshot.get(i).individualStrategyPicture);
            File f = getFile(filePath + "\\" + PDGameStraPicFilePrefix + i + ".jpg");
            outputImageToFile(im, "jpg", f);


            if (PDGameJSONString != null) {
                outputToFile(filePath + "\\" + PDGameJSONFileSuffix,
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

    public static File checkFileExists(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            return f;
        }
        return null;
    }

    public static boolean checkPicFileConsistency(String filePath) {
        File f = checkFileExists(filePath + "\\" + PDGameSnapshotTxtFormatDirectName);
        //System.out.println(f);
        if (f != null) {
            File[] allPicfs = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String filename = pathname.getName();
                    if (pathname.isFile() && filename.endsWith(".txt")
                            && filename.startsWith(FileUtils.PDGameAllPicFilePrefix)) {
                        return true;
                    }
                    return false;
                }
            });
            File[] straPicfs = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String filename = pathname.getName();
                    if (pathname.isFile() && filename.endsWith(".txt")
                            && filename.startsWith(FileUtils.PDGameStraPicFilePrefix)) {
                        return true;
                    }
                    return false;
                }
            });
            //System.out.println(Arrays.toString(allPicfs));
            //System.out.println(Arrays.toString(straPicfs));
            if (allPicfs != null
                    && allPicfs.length > 0
                    && straPicfs != null
                    && straPicfs.length > 0) {
                int allMax = -1;
                for (File allf : allPicfs) {
                    int turn = getPicFileTurn(allf, FileUtils.PDGameAllPicFilePrefix, ".txt");
                    if (allMax < turn) {
                        allMax = turn;
                    }
                }
                int straMax = -1;
                for (File straf : straPicfs) {
                    int turn = getPicFileTurn(straf, FileUtils.PDGameStraPicFilePrefix, ".txt");
                    if (straMax < turn) {
                        straMax = turn;
                    }
                }
                //System.out.println(allMax + "," + straMax);
                if (allMax != -1 && straMax != -1 && allMax == straMax) {
                    return true;
                }

            }
        }
        return false;
    }

    private static int getPicFileTurn(File f, String prefix, String suffix) {
        String name = f.getName();
        int start = name.indexOf(prefix);
        int end = name.indexOf(suffix);
        if (start >= 0 && end >= 0) {
            start += prefix.length();
            if (start < end) {
                return Integer.parseInt(name.substring(start, end));
            }
        }
        return -1;
    }

    /**
     * @param fileName String The system-dependent filename
     * @param str      要输出到文件的内容
     */
    synchronized public static void outputToFile(String fileName, String str) {
        File f = getFile(fileName);
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName), Encoding)) {
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
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(f), Encoding)) {
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


    public static void main(String[] args) {
        String path = "F:\\interactive task\\data9\\INTERACTIVE_FERMI_$_NONE_$_TWO_$_Von_$_pi=1.00_$_qi=0.00_$_w=0.00";
        System.out.println(checkFileExists(path + "\\gr=(1.00,-0.00,1.00,0.00)_$_d0=1.00.txt"));
        System.out.println(checkPicFileConsistency(path+"\\gr=(1.00,-0.00,1.00,0.00)_$_d0=1.00"));
    }

}
