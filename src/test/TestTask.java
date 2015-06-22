package test;

import entity.SpatialPDGame;
import rule.*;
import utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Created by hyx on 2015/6/4.
 */
public class TestTask implements Callable<Integer> {

    int length;

    float stepLength;

    SpatialPDGame spdg;
    float d0;
    float pi;
    float qi;
    float w;
    LearningPattern learningPattern;
    MigrationPattern migrationPattern;
    StrategyPattern strategyPattern;
    NeighbourCoverage neighbourCoverage;
    EvolutionPattern evolutionPattern;
    int maxTurn;
    String outputFilePath;

    private EarlyStageSampleCheck earlyStageSampleCheck;

    public TestTask(boolean recordSnapshoot, int length, float stepLength, float d0, float pi, float qi, float w, LearningPattern learningPattern,
                    MigrationPattern migrationPattern, StrategyPattern strategyPattern,
                    NeighbourCoverage neighbourCoverage, EvolutionPattern evolutionPattern, int maxTurn,
                    String outputFilePath) {

        this.spdg = new SpatialPDGame(recordSnapshoot);
        this.length = length;
        this.stepLength = stepLength;
        this.d0 = d0;
        this.pi = pi;
        this.qi = qi;
        this.w = w;
        this.learningPattern = learningPattern;
        this.migrationPattern = migrationPattern;
        this.strategyPattern = strategyPattern;
        this.neighbourCoverage = neighbourCoverage;
        this.evolutionPattern = evolutionPattern;
        this.maxTurn = maxTurn;
        this.outputFilePath = outputFilePath;

        earlyStageSampleCheck = new EarlyStageSampleCheck();
    }

    @Override
    public Integer call() throws Exception {
        float Dr = 0, Dg = 0;

        int L1, L2;
        L1 = Math.round(1 / stepLength) + 1;
        L2 = Math.round(1 / stepLength) + 1;
        double[][] cl = new double[L1][];
        for (int i = 0; i < L1; i++) {
            cl[i] = new double[L2];
        }
        int i, j;
        for (i = 0, Dr = 0; Dr <= 1.0001f; Dr += stepLength, i++) {
            for (j = 0, Dg = 0; Dg <= 1.0001f; Dg += stepLength, j++) {
                spdg.initSpatialPDGame(length, d0, Dr, Dg, pi, qi, w,
                        learningPattern, migrationPattern, strategyPattern,
                        neighbourCoverage, evolutionPattern);

                if (!checkSubTaskFinishedAndRecordCoorLevel(cl, i, j)) {

                    spdg.run(maxTurn);
                    spdg.done();

                    FileUtils.outputToFile(
                            spdg.dataPrinter.constructDetailReportFileName(outputFilePath),
                            spdg.dataPrinter.getDetailReport());
                    FileUtils.outputSnapshootToFile(
                            spdg.dataPrinter.constructImageFilePath(outputFilePath),
                            spdg.getJSONObject().toString(),
                            spdg.getSnapshootMap());

                    cl[i][j] = spdg.getCooperationLevel();
                }
                System.out.println("task " + spdg + " completed");
            }
        }
        FileUtils.outputToFile(
                spdg.dataPrinter.constructFilePath(outputFilePath)
                        + "\\CoopertationLevel.txt",
                Test.getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));
        return 1;
    }

    private boolean checkSubTaskFinishedAndRecordCoorLevel(double[][] cl, int i, int j) {
        File check1File = FileUtils.checkFileExists(spdg.dataPrinter.constructDetailReportFileName(outputFilePath));
        boolean check1 = check1File != null;
        int underwentTurn;
        //检查本工作是否已经做了
        if (check1) {
            String detailReport = FileUtils.readStringFromFile(check1File);
            underwentTurn = spdg.dataPrinter.getUnderwentTurnFromDetailReport(detailReport);
            boolean check2 = checkPicFileConsistency(spdg.dataPrinter.constructImageFilePath(outputFilePath), underwentTurn);
            if (check2) {
                cl[i][j] = spdg.dataPrinter.getGlobalCooperationLevelFromDetailReport(
                        FileUtils.readStringFromFile(check1File));
                return true;
            }
        }
        return false;
    }

    private boolean checkPicFileConsistency(String filePath, int underwentTurn) {
        File f = FileUtils.checkFileExists(filePath + "\\" + FileUtils.PDGameSnapshotTxtFormatDirectName);
        //System.out.println(f);
        if (f == null){
            return true;//  recordSnapshot should be false;
        } else {
            File[] allPicfs = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String filename = pathname.getName();
                    return pathname.isFile() && filename.endsWith(".txt")
                            && filename.startsWith(FileUtils.PDGameAllPicFilePrefix);
                }
            });
            File[] straPicfs = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String filename = pathname.getName();
                    return pathname.isFile() && filename.endsWith(".txt")
                            && filename.startsWith(FileUtils.PDGameStraPicFilePrefix);
                }
            });
            //System.out.println(Arrays.toString(allPicfs));
            //System.out.println(Arrays.toString(straPicfs));
            if (allPicfs != null
                    && allPicfs.length > 0
                    ) {
                int allMax = -1;
                for (File allf : allPicfs) {
                    int turn = getPicFileTurn(allf, FileUtils.PDGameAllPicFilePrefix, ".txt");
                    if (allMax < turn) {
                        allMax = turn;
                    }
                }
                if (straPicfs != null
                        && straPicfs.length > 0) {
                    int straMax = -1;
                    for (File straf : straPicfs) {
                        int turn = getPicFileTurn(straf, FileUtils.PDGameStraPicFilePrefix, ".txt");
                        if (straMax < turn) {
                            straMax = turn;
                        }
                    }
                    if (underwentTurn == straMax && underwentTurn == allMax) {
                        return true;
                    }
                } else {
                    if (underwentTurn == allMax) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private int getPicFileTurn(File f, String prefix, String suffix) {
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

    class EarlyStageSampleCheck implements SpatialPDGame.SampleCheck {
        final int idsLength = 200;
        final int[] ids;

        public EarlyStageSampleCheck() {
            ids = new int[idsLength];
            for (int i = 0; i < idsLength; i++) {
                ids[i] = i;
            }
        }

        @Override
        public boolean isWorldDetailHistorySampleTurn(int turn) {
            // TODO Auto-generated method stub
            if (Arrays.binarySearch(ids, turn) >= 0) {
                return true;
            } else if ((turn) % 100 == 0) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isSnapshootSampleTurn(int turn) {
            // TODO Auto-generated method stub
            if (Arrays.binarySearch(ids, turn) >= 0) {
                return true;
            } else {
                int i = 1;
                while (turn > Math.pow(10, i)) {
                    i++;
                }
                if (turn == Math.pow(10, i)) {
                    return true;
                }
            }
            return false;
        }
    }

}
