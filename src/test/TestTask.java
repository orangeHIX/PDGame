package test;

import entity.SpatialPDGame;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import utils.FileUtils;

import java.io.File;
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
    int maxTurn;
    String outputFilePath;

    public TestTask(int length, float stepLength, float d0, float pi, float qi, float w, LearningPattern learningPattern,
                    MigrationPattern migrationPattern, StrategyPattern strategyPattern,
                    NeighbourCoverage neighbourCoverage, int maxTurn,
                    String outputFilePath) {

        this.spdg = new SpatialPDGame(true);
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
        this.maxTurn = maxTurn;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public Integer call() throws Exception {
        float Dr = 0, Dg = 0;

        int L1, L2;
        L1 = (int) Math.round(1 / stepLength) + 1;
        L2 = (int) Math.round(1 / stepLength) + 1;
        double[][] cl = new double[L1][];
        for (int i = 0; i < L1; i++) {
            cl[i] = new double[L2];
        }
        int i, j;
        for (i = 0, Dr = 0; Dr <= 1.0001f; Dr += stepLength, i++) {
            for (j = 0, Dg = 0; Dg <= 1.0001f; Dg += stepLength, j++) {
                spdg.initSpatialPDGame(length, d0, Dr, Dg, pi, qi, w,
                        learningPattern, migrationPattern, strategyPattern,
                        neighbourCoverage);
                File check1File = FileUtils.checkFileExists(spdg.dataPrinter.constructDetailReportFileName(outputFilePath));
                boolean check1 = check1File != null;
                boolean check2 = FileUtils.checkPicFileConsistency(spdg.dataPrinter.constructImageFilePath(outputFilePath));
                if (check1 && check2) {
                    System.out.println(spdg.dataPrinter.GlobalCooperationLevelString
                            + spdg.dataPrinter.getGlobalCooperationLevelFromDetailReport(
                            FileUtils.readStringFromFile(check1File)));
                }
                System.out.println("task " + spdg + "check completed: " + check1 + "," + check2+"\n");
            }
        }
        for (i = 0, Dr = 0; Dr <= 1.0001f; Dr += stepLength, i++) {
            for (j = 0, Dg = 0; Dg <= 1.0001f; Dg += stepLength, j++) {
                spdg.initSpatialPDGame(length, d0, Dr, Dg, pi, qi, w,
                        learningPattern, migrationPattern, strategyPattern,
                        neighbourCoverage);

                File check1File = FileUtils.checkFileExists(spdg.dataPrinter.constructDetailReportFileName(outputFilePath));
                boolean check1 = check1File != null;
                boolean check2 = FileUtils.checkPicFileConsistency(spdg.dataPrinter.constructImageFilePath(outputFilePath));
                //检查本工作是否已经做了
                if (check1 && check2) {
                    cl[i][j] = spdg.dataPrinter.getGlobalCooperationLevelFromDetailReport(
                            FileUtils.readStringFromFile(check1File));
                }else {
                    spdg.run(maxTurn, new EarlyStageSampleCheck());
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

    class EarlyStageSampleCheck implements SpatialPDGame.SampleCheck {
        final int idsLength = 100;
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

    ;
}
