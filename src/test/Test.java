package test;

import entity.SpatialPDGame;
import entity.SpatialPDGame.SampleCheck;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import utils.ArrayUtils;
import utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {

    /**
     * 无变化门限，若连续超过此值的博弈演化轮数所有个体的策略保持不变，则认为模型已经稳定，不再进行演化
     */
    public static final int noChangeThreshold = 100;
    /** 采样间隔，每隔间隔轮数进行一次采样，统计合作水平 */
    // public static final int sampleInterval = 100;
    /**
     * 网格长度
     */
    public static final int LENGTH = 100;

    public static final int MAX_TURN_NUM = 50000;

    public static final float STEP_LENGTH = 0.1f;

    public static void main(String args[]) {
//         SpatialPDGame spdg = new SpatialPDGame(true);
//         spdg.initSpatialPDGame(5, 1.0f, 0.1f, 0.1f, 1.0f, 0, 1.0f,
//                 LearningPattern.INTERACTIVE_FERMI, MigrationPattern.NONE,
//                 StrategyPattern.CONTINUOUS, NeighbourCoverage.Von);
//         spdg.run(MAX_TURN_NUM);
//         spdg.done();
//        FileUtils.outputToFile(
//                spdg.dataPrinter.constructDetailReportFileName("C:\\Users\\hyx\\Desktop\\kk"),
//                spdg.dataPrinter.getDetailReport());
//        //FileUtils.outputImageToFile();
//        FileUtils.outputSnapshootToFile(
//                spdg.dataPrinter.constructImageFilePath("C:\\Users\\hyx\\Desktop\\kk"),
//                spdg.dataPrinter.getJsonString(),
//                spdg.getSnapshootMap());
//        System.out.println(spdg.dataPrinter.getDetailReport());
        //spdg.printPicture();

        long start = System.currentTimeMillis();
        runOneTest5(LearningPattern.INTERACTIVE_FERMI, MigrationPattern.NONE,
                StrategyPattern.CONTINUOUS, NeighbourCoverage.Von,
                MAX_TURN_NUM, "F:\\interactive task\\data10");
        long end = System.currentTimeMillis();
        System.out.println("underwent: " + (end - start) + "ms");

    }

    /**
     * 用于本科论文实验的启动方法
     */
    public static void runOneTest2(LearningPattern learningPattern,
                                   MigrationPattern imigratePattern, StrategyPattern strategyPattern,
                                   int maxTurn, String outputFilePath) {
        int L = LENGTH;
        float qi;
        float pi = 0.5f;
        float d0 = 0.2f;
        float Dr, Dg;

        SpatialPDGame spdg = new SpatialPDGame(false);
        float stepLength = STEP_LENGTH;
        float stepLength2 = 0.1f;
        int L1, L2;
        L1 = (int) Math.round(1 / stepLength) + 1;
        L2 = (int) Math.round(1 / stepLength) + 1;
        double[][] cl = new double[L1][];
        for (int i = 0; i < L1; i++) {
            cl[i] = new double[L2];
        }

        int i, j;
        for (qi = 1.0f; qi <= 1.0001f; qi += stepLength2) {
            for (i = 0, Dr = 0; Dr <= 1.0001f; Dr += stepLength, i++) {
                for (j = 0, Dg = 0; Dg <= 1.0001f; Dg += stepLength, j++) {
                    spdg.initSpatialPDGame(L, d0, Dr, Dg, pi, qi, 1.0f,
                            learningPattern, imigratePattern, strategyPattern,
                            NeighbourCoverage.Classic);
                    spdg.run(maxTurn);
                    spdg.done();
                    // spdg.printPicture();
                    FileUtils.outputToFile(
                            spdg.dataPrinter.constructDetailReportFileName(outputFilePath),
                            spdg.dataPrinter.getDetailReport());

                    cl[i][j] = spdg.getCooperationLevel();
                    System.out.println("" + learningPattern + ","
                            + imigratePattern + "," + strategyPattern + ", Dr="
                            + Dr + ", Dg=" + Dg + ", d0= " + d0 + " completed");
                }
            }
            FileUtils.outputToFile(
                    spdg.dataPrinter.constructFilePath(outputFilePath)
                            + "\\CoopertationLevel.txt",
                    getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));
        }
    }

    /**
     * 用于本科论文实验的启动方法
     */
    public static void runOneTest3(LearningPattern learningPattern,
                                   MigrationPattern imigratePattern, StrategyPattern strategyPattern,
                                   int maxTurn, String outputFilePath) {
        int L = LENGTH;
        float pi = 0.5f;
        float d0 = 0.5f;
        float[] qis = {0, 0.1f, 0.9f};
        float[] Drs = {0.1f, 0.9f};
        float[] Dgs = {0.1f, 0.9f};
        SpatialPDGame spdg = new SpatialPDGame(true);
        float Dr = 0.1f;
        float Dg = 0.9f;
        for (float qi : qis) {

            spdg.initSpatialPDGame(L, d0, Dr, Dg, pi, qi, 1.0f,
                    learningPattern, imigratePattern, strategyPattern,
                    NeighbourCoverage.Classic);
            spdg.run(maxTurn, new SampleCheck() {
                int[] specialTurns = {1, 2, 3, 4, 5, 6, 20, 50};

                @Override
                public boolean isWorldDetailHistorySampleTurn(int turn) {
                    // TODO Auto-generated method stub
                    if ((turn) % 100 == 0) {
                        return true;
                    } else if (Arrays.binarySearch(specialTurns, turn) >= 0) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean isSnapshootSampleTurn(int turn) {
                    // TODO Auto-generated method stub
                    int keyturn = -1;
                    for (int i = 1; (keyturn = (int) Math.pow(10, i)) < turn; i++)
                        ;
                    if (keyturn == turn) {
                        return true;
                    } else if (Arrays.binarySearch(specialTurns, turn) >= 0) {
                        return true;
                    }
                    return false;
                }
            });
            spdg.done();
            // spdg.printPicture();
            FileUtils.outputToFile(
                    spdg.dataPrinter.constructDetailReportFileName(outputFilePath),
                    spdg.dataPrinter.getDetailReport());
            //FileUtils.outputImageToFile();
            FileUtils.outputSnapshootToFile(
                    spdg.dataPrinter.constructImageFilePath(outputFilePath),
                    spdg.getSnapshootMap());

        }
        // System.out.println("" + learningPattern + "," + imigratePattern + ","
        // + strategyPattern + " gr=" + r + ", d0= " + d0 + " completed");
    }

    /**
     * 这个方法是用于做交互强度试验的
     */
    public static void runOneTest4(LearningPattern learningPattern,
                                   MigrationPattern imigratePattern, StrategyPattern strategyPattern,
                                   NeighbourCoverage neighbourCoverage, int maxTurn,
                                   String outputFilePath) {
        int L = LENGTH;
        float qi = 0.0f;
        float pi = 1.0f;
        float w = 0.0f;
        float d0 = 1.0f;
        float Dr = 0, Dg = 0;

        SpatialPDGame spdg = new SpatialPDGame(true);
        float stepLength = STEP_LENGTH;
        // float stepLength2 = 0.1f;
        int L1, L2;
        L1 = (int) Math.round(1 / stepLength) + 1;
        L2 = (int) Math.round(1 / stepLength) + 1;
        double[][] cl = new double[L1][];
        for (int i = 0; i < L1; i++) {
            cl[i] = new double[L2];
        }

        int i, j;
        for (w = .5f; w <= .501f; w += stepLength) {
            for (i = 0, Dr = 0; Dr <= 1.0001f; Dr += stepLength, i++) {
                for (j = 0, Dg = 0; Dg <= 1.0001f; Dg += stepLength, j++) {
                    spdg.initSpatialPDGame(L, d0, Dr, Dg, pi, qi, w,
                            learningPattern, imigratePattern, strategyPattern,
                            neighbourCoverage);
                    spdg.run(maxTurn);
                    spdg.done();

                    FileUtils.outputToFile(
                            spdg.dataPrinter.constructDetailReportFileName(outputFilePath),
                            spdg.dataPrinter.getDetailReport());
                    FileUtils.outputSnapshootToFile(spdg.dataPrinter
                            .constructImageFilePath(outputFilePath), spdg
                            .getSnapshootMap());

                    cl[i][j] = spdg.getCooperationLevel();
                    System.out.println("" + learningPattern + ","
                            + imigratePattern + "," + strategyPattern + ", Dr="
                            + Dr + ", Dg=" + Dg + ", w= " + w + " completed");
                }
            }
            FileUtils.outputToFile(
                    spdg.dataPrinter.constructFilePath(outputFilePath)
                            + "\\CoopertationLevel.txt",
                    getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));

        }
    }

    public static void runOneTest5(LearningPattern learningPattern,
                                   MigrationPattern imigratePattern, StrategyPattern strategyPattern,
                                   NeighbourCoverage neighbourCoverage, int maxTurn,
                                   String outputFilePath) {
        int L = LENGTH;
        float d0 =1.f;
        float pi =1.f;
        float qi = 0;
        float w;
        ExecutorService pool = Executors.newFixedThreadPool(5);
        float stepLength = STEP_LENGTH;
        ArrayList<Future<Integer>> results = new ArrayList<>();
        for (w = .0f; w <= 1.01f; w += stepLength) {
            results.add(
                    pool.submit(new TestTask(
                            L,
                            stepLength,
                            d0,
                            pi,
                            qi,
                            w,
                            learningPattern,
                            imigratePattern,
                            strategyPattern,
                            neighbourCoverage,
                            maxTurn,
                            outputFilePath)));
        }

        try {
            for (Future<Integer> fu : results) {
                fu.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        File f = new File(outputFilePath);
        if(f.isDirectory()) {
            FileFixer.FixFile(f.getParentFile().getAbsolutePath(), f.getName(), "w=");
            FileReorganize.reorganize(f.getParentFile().getAbsolutePath(), f.getName());
        }
        System.out.println("**********************************");
    }

    /**
     * 生成本次实验产生的合作率数组文本文件
     */
    public static String getCoopertationLevelsReport(
            double[][] coopertationLevels, String horizontalName,
            String verticalName, int L1, int L2) {
        StringBuilder sb = new StringBuilder();
        float ALLPDCooLevel = 0.f;
        float CooLevel2 = 0.f;
        float CooLevel3 = 0.f;
        float CooLevel4 = 0.f;
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                ALLPDCooLevel += coopertationLevels[i][j];
                if (i == 0)
                    CooLevel2 += coopertationLevels[i][j];
                if (j == 0)
                    CooLevel3 += coopertationLevels[i][j];
                if (i == j)
                    CooLevel4 += coopertationLevels[i][j];
            }
        }
        ALLPDCooLevel = ALLPDCooLevel / (L1 * L2);
        CooLevel2 = CooLevel2 / L2;
        CooLevel3 = CooLevel3 / L1;
        CooLevel4 = CooLevel4 / (Math.min(L1, L2));
        sb.append("整体平均合作水平：" + ALLPDCooLevel + "\r\n");
        sb.append("第一行平均合作水平：" + CooLevel2 + "\r\n");
        sb.append("第一列平均合作水平：" + CooLevel3 + "\r\n");
        sb.append("左上到右下对角线平均合作水平：" + CooLevel4 + "\r\n");
        sb.append("每行" + horizontalName + ":从0增大到1.0," + STEP_LENGTH
                + "为间隔；\r\n每列" + verticalName + ":从0增大到1.0," + STEP_LENGTH
                + "为间隔\r\n");
        sb.append(ArrayUtils.getTwoDeArrayString(coopertationLevels));
        return sb.toString();
    }
}
