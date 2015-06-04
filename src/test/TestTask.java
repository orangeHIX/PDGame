package test;

import entity.SpatialPDGame;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import utils.FileUtils;

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

    public TestTask(int length, float stepLength,float d0, float pi, float qi, float w, LearningPattern learningPattern,
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
                spdg.run(maxTurn);
                spdg.done();

                FileUtils.outputToFile(
                        spdg.dataPrinter.constructDetailReportFileName(outputFilePath),
                        spdg.dataPrinter.getDetailReport());
                FileUtils.outputSnapshootToFile(spdg.dataPrinter
                        .constructImageFilePath(outputFilePath), spdg
                        .getSnapshootMap());

                cl[i][j] = spdg.getCooperationLevel();
                System.out.println("task " +spdg+" completed");
            }
        }
        FileUtils.outputToFile(
                spdg.dataPrinter.constructFilePath(outputFilePath)
                        + "\\CoopertationLevel.txt",
                Test.getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));
        return 1;
    }
}