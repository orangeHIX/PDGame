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
        deleteFile(path, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
    }

    public static void deleteFile(String path, FileFilter fileFilter) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] ff = file.listFiles();
            for (int i = 0; i < ff.length; i++) {
                deleteFile(ff[i].getPath(), fileFilter);
            }
        }
        if (fileFilter.accept(file)) {
            file.delete();
        }
    }

    synchronized public static void outputSnapshootToFile(String filePath,
                                                          Map<Integer, Snapshot> Snapshot) {
        outputSnapshootToFile(filePath, null, Snapshot);
    }

    synchronized public static void outputSnapshootToFile(String filePath, String PDGameJSONString,
                                                          Map<Integer, Snapshot> snapshot) {
        FileUtils.deleteFile(filePath);
        for (Integer i : snapshot.keySet()) {
            //File f = getFile();
            outputPicTxtFile(snapshot.get(i).allPicture, i, PDGameAllPicFilePrefix, filePath);
            outputPicTxtFile(snapshot.get(i).individualStrategyPicture, i, PDGameStraPicFilePrefix, filePath);
            outputStraPicImageFile(snapshot.get(i).individualStrategyPicture, i, filePath);
            outputPDGameJSONFile(filePath, PDGameJSONString);

        }
    }

    private static void outputPicTxtFile(String pic, int turn, String fileNamePrefix, String filePath) {
        if (pic != null &&
                !pic.isEmpty()) {
            outputToFile(filePath + "\\" +
                            PDGameSnapshotTxtFormatDirectName + "\\" +
                            fileNamePrefix + turn + ".txt",
                    pic);
        }
    }

    private static void outputStraPicImageFile(String individualStrategyPicture, int turn, String filePath) {
        if (individualStrategyPicture != null &&
                !individualStrategyPicture.isEmpty()) {
            RenderedImage im = (RenderedImage) Painter.getPDGameImage(
                    400,
                    400,
                    individualStrategyPicture);
            File f = getFile(filePath + "\\" + PDGameStraPicFilePrefix + turn + ".jpg");
            outputImageToFile(im, "jpg", f);
        }
    }

    synchronized public static void outputPDGameJSONFile(String path, String PDGameJSONString) {
        if (PDGameJSONString != null) {
            outputToFile(path + "\\" + PDGameJSONFileSuffix,
                    PDGameJSONString);
        }
    }

    synchronized public static void outputImageToFile(RenderedImage image,
                                                      String formatName, File output) {
        try {
            ImageIO.write(image, "jpg", output);
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

    /**
     * @return null if not find
     */
    public static File findPDGameJSONFile(File direct) {
        if (direct != null && direct.isDirectory()) {
            File[] fs = direct.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(FileUtils.PDGameJSONFileSuffix);
                }
            });
            if (fs.length > 0) {
                return fs[0];
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        String path = "F:\\interactive task\\data9\\INTERACTIVE_FERMI_$_NONE_$_TWO_$_Von_$_pi=1.00_$_qi=0.00_$_w=0.00";
//        System.out.println(checkFileExists(path + "\\gr=(1.00,-0.00,1.00,0.00)_$_d0=1.00.txt"));
//        System.out.println(checkPicFileConsistency(path+"\\gr=(1.00,-0.00,1.00,0.00)_$_d0=1.00"));

        String path = "F:\\interactive task\\data9\\INTERACTIVE_FERMI_$_NONE_$_TWO_$_Von_$_pi=1.00_$_qi=0.00_$_w=0.10";
        deleteFile(path, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                int start = name.indexOf(FileUtils.PDGameAllPicFilePrefix);
                if( start < 0 ){
                    return false;
                }
                start += FileUtils.PDGameAllPicFilePrefix.length();
                int end = name.indexOf(".txt");
                if( end < 0 ){
                    return false;
                }
                int turn  = Integer.parseInt(name.substring(start, end));
                if( turn >100 && turn < 200){
                    return true;
                }
                return false;
            }
        });
    }

}
