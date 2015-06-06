package test;

import entity.SpatialPDGame;
import utils.ArrayUtils;
import utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 用于数据后期处理的类，不必理会
 */
public class FileFixer {

    synchronized public static void FixFile(String filepath, String fileKeyStr,
                                            String secFileKeyStr) {
        File f = new File(filepath);
        float[][] data = null;

        File[] filelist = f.listFiles();
        for (File file1 : filelist) {
            if (
                // file1.getName().endsWith(")")
                // && !
                    file1.getName().contains(fileKeyStr)
                            && !file1.getName().endsWith("$")) {
                System.out.println(file1.getName());
                for (File file2 : file1.listFiles()) {
                    if (file2.isDirectory()
                            && file2.getName().contains(secFileKeyStr)) {
                        System.out.println(file2.getName());

                        //补写PDGame的JSON描述文件
                        rewritePDGameJSONFile(file2);

                        for (File file3 : file2.listFiles()) {
                            if (file3.getName().equals("CoopertationLevel.txt")) {
                                System.out.println(file3.getName());
                                data = readDataCoopertationLevel(file3);
                                // rewriteFile(file3,data);
                                //break;
                            }
                        }
                        if (data != null) {
                            String outFileName = filepath + "\\"
                                    + file1.getName() + "$\\" + file2.getName()
                                    + "_CooperationLevelSummary.txt";
                            writeFile(outFileName, data);

                        }
                    }
                }
            }
        }
    }

    public static void rewriteFile(File f, float[][] data) {

        int L1, L2;
        L1 = L2 = 11;
        float ALLPDCooLevel = 0.f;
        float CooLevel2 = 0.f;
        float CooLevel3 = 0.f;
        float CooLevel4 = 0.f;
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                ALLPDCooLevel += data[i][j];
                if (i == 0)
                    CooLevel2 += data[i][j];
                if (j == 0)
                    CooLevel3 += data[i][j];
                if (i == j)
                    CooLevel4 += data[i][j];
            }
        }
        ALLPDCooLevel = ALLPDCooLevel / (L1 * L2);
        CooLevel2 = CooLevel2 / L2;
        CooLevel3 = CooLevel3 / L1;
        CooLevel4 = CooLevel4 / (Math.min(L1, L2));
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.print("ALLPD:");
            pw.print(ALLPDCooLevel);
            pw.print("\r\nBCH:");
            pw.print(CooLevel2);
            pw.print("\r\nBSH:");
            pw.print(CooLevel3);
            pw.print("\r\nDRG:");
            pw.print(CooLevel4);
            pw.print("\r\n");
            pw.print("\r\n每行Dg:从0增大到1.0,0.1为间隔；\r\n每列Dr:从0增大到1.0,0.1为间隔\r\n");
            pw.print(ArrayUtils.getTwoDeArrayString(data, L1, L2));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeFile(String filename, float[][] data) {
        File f = FileUtils.getFile(filename);
        int L1, L2;
        L1 = L2 = 11;
        float ALLPDCooLevel = 0.f;
        float CooLevel2 = 0.f;
        float CooLevel3 = 0.f;
        float CooLevel4 = 0.f;
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                ALLPDCooLevel += data[i][j];
                if (i == 0)
                    CooLevel2 += data[i][j];
                if (j == 0)
                    CooLevel3 += data[i][j];
                if (i == j)
                    CooLevel4 += data[i][j];
            }
        }
        ALLPDCooLevel = ALLPDCooLevel / (L1 * L2);
        CooLevel2 = CooLevel2 / L2;
        CooLevel3 = CooLevel3 / L1;
        CooLevel4 = CooLevel4 / (Math.min(L1, L2));

        try (PrintWriter pw = new PrintWriter(f)) {
            pw.print("ALLPD:");
            pw.print(ALLPDCooLevel);
            pw.print("\r\nBCH:");
            pw.print(CooLevel2);
            pw.print("\r\nBSH:");
            pw.print(CooLevel3);
            pw.print("\r\nDRG:");
            pw.print(CooLevel4);
            pw.print("\r\n");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void rewritePDGameJSONFile(File path) {
        if (path != null && path.isDirectory()) {
            File[] directs = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    return false;
                }
            });
            File[] txtFiles = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isFile() && pathname.getName().endsWith(".txt")) {
                        return true;
                    }
                    return false;
                }
            });
            //System.out.println(Arrays.toString(directs));
            //System.out.println(Arrays.toString(txtFiles));
            for (File direct : directs) {
                File txtFile = findFile(txtFiles, direct.getName()+ ".txt");
                //System.out.println("txtFile: "+ txtFile);
                if (txtFile != null) {
                    File jsonFile = FileUtils.findPDGameJSONFile(direct);
                    if (jsonFile == null) {
                        String JSONString = convertSptialPDgameToStringFromFiletoJSONString(txtFile);
                        //System.out.println("JSONString: "+ JSONString);
                        FileUtils.outputPDGameJSONFile(direct.getAbsolutePath(), JSONString);
                    }
                }
            }
        }
    }

    private static File findFile(File[] fs, String filename) {
        for (File f : fs) {
            if (f.getName().compareTo(filename) == 0) {
                return f;
            }
        }
        return null;
    }


    private static String convertSptialPDgameToStringFromFiletoJSONString(File f) {
        if (f != null && f.isFile()) {
            SpatialPDGame pdGmae = new SpatialPDGame();
            try (Scanner sc = new Scanner(f)) {

                pdGmae.initFromToString(sc.nextLine());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return pdGmae.getJSONObject().toString();
        }
        return null;
    }

    public static float[][] readDataCoopertationLevel(File f) {

        int L = 11;

        if (f.isFile() && f.exists()) {
            float[][] data = new float[L][];
            for (int i = 0; i < L; i++) {
                data[i] = new float[L];
            }

            try (Scanner sc = new Scanner(f)) {
                for (int i = 0; i < 6; i++) {
                    System.out.println(sc.nextLine());
                }
                for (int i = 0; i < L; i++) {
                    for (int j = 0; j < L; j++) {
                        data[i][j] = sc.nextFloat();
                        // System.out.println(data[i][j]);
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(ArrayUtils.getTwoDeArrayString(data, L, L));

            return data;
        }
        return null;
    }

    public static void main(String[] args) {
        //FixFile("F:\\交互强度任务", "data5", "w=");
        rewritePDGameJSONFile(new File("C:\\Users\\hyx\\Desktop\\kk2" +
                "\\INTERACTIVE_FERMI_$_NONE_$_CONTINUOUS_$_Von_$_pi=1.00_$_qi=0.00_$_w=1.00"));
    }
}
