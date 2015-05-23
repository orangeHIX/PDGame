package test;

import java.util.Arrays;

import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import test.SpatialPDGame.SampleCheck;
import utils.ArraytoString;
import utils.FileUtils;

public class Test {

	/** �ޱ仯���ޣ�������������ֵ�Ĳ����ݻ��������и���Ĳ��Ա��ֲ��䣬����Ϊģ���Ѿ��ȶ������ٽ����ݻ� */
	public static final int noChangeThreshold = 100;
	/** ���������ÿ�������������һ�β�����ͳ�ƺ���ˮƽ */
	// public static final int sampleInterval = 100;
	/** ���񳤶� */
	public static final int LENGTH = 100;

	public static final int MAX_TURN_NUM = 50000;

	public static final float STEP_LENGTH = 0.1f;

	public static void main(String args[]) {
		// SpatialPDGame spdg = new SpatialPDGame(false);
		// spdg.initSpatialPDGame(10, 1.0f, 0.1f, 0.1f, 1.0f, 0, 1.0f,
		// LearningPattern.FERMI, MigrationPattern.NONE,
		// StrategyPattern.TWO, NeighbourCoverage.Von);
		// spdg.run(1);
		// spdg.done();
		// System.out.println(spdg.dataPrinter.getDetailReport());
		// spdg.printPicture();

		long start = System.currentTimeMillis();
		runOneTest4(LearningPattern.INTERACTIVE_FERMI, MigrationPattern.NONE,
				StrategyPattern.CONTINUOUS, NeighbourCoverage.Von,
				MAX_TURN_NUM, "F:\\����ǿ������\\data5");
		long end = System.currentTimeMillis();
		System.out.println("underwent: " + (end - start) + "ms");

	}

	/** ���ڱ�������ʵ����������� */
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
					FileUtils.outputTofile(
							spdg.dataPrinter.constructFileName(outputFilePath),
							spdg.dataPrinter.getDetailReport());

					cl[i][j] = spdg.getCooperationLevel();
					System.out.println("" + learningPattern + ","
							+ imigratePattern + "," + strategyPattern + ", Dr="
							+ Dr + ", Dg=" + Dg + ", d0= " + d0 + " completed");
				}
			}
			FileUtils.outputTofile(
					spdg.dataPrinter.constructFilePath(outputFilePath)
							+ "\\CoopertationLevel.txt",
					getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));
		}
	}

	/** ���ڱ�������ʵ����������� */
	public static void runOneTest3(LearningPattern learningPattern,
			MigrationPattern imigratePattern, StrategyPattern strategyPattern,
			int maxTurn, String outputFilePath) {
		int L = LENGTH;
		float pi = 0.5f;
		float d0 = 0.5f;
		float[] qis = { 0, 0.1f, 0.9f };
		float[] Drs = { 0.1f, 0.9f };
		float[] Dgs = { 0.1f, 0.9f };
		SpatialPDGame spdg = new SpatialPDGame(true);
		float Dr = 0.1f;
		float Dg = 0.9f;
		for (float qi : qis) {

			spdg.initSpatialPDGame(L, d0, Dr, Dg, pi, qi, 1.0f,
					learningPattern, imigratePattern, strategyPattern,
					NeighbourCoverage.Classic);
			spdg.run(maxTurn, new SampleCheck() {
				int[] specialTurns = { 1, 2, 3, 4, 5, 6, 20, 50 };

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
			FileUtils.outputTofile(
					spdg.dataPrinter.constructFileName(outputFilePath),
					spdg.dataPrinter.getDetailReport());
			FileUtils.outputSnapshootToFile(
					spdg.dataPrinter.constructImageFilePath(outputFilePath),
					spdg.getSnapshootMap());

		}
		// System.out.println("" + learningPattern + "," + imigratePattern + ","
		// + strategyPattern + " gr=" + r + ", d0= " + d0 + " completed");
	}

	/** �������������������ǿ������� */
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

					FileUtils.outputTofile(
							spdg.dataPrinter.constructFileName(outputFilePath),
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
			FileUtils.outputTofile(
					spdg.dataPrinter.constructFilePath(outputFilePath)
							+ "\\CoopertationLevel.txt",
					getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));

		}
	}

	/** ���ɱ���ʵ������ĺ����������ı��ļ� */
	private static String getCoopertationLevelsReport(
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
		sb.append("����ƽ������ˮƽ��" + ALLPDCooLevel + "\r\n");
		sb.append("��һ��ƽ������ˮƽ��" + CooLevel2 + "\r\n");
		sb.append("��һ��ƽ������ˮƽ��" + CooLevel3 + "\r\n");
		sb.append("���ϵ����¶Խ���ƽ������ˮƽ��" + CooLevel4 + "\r\n");
		sb.append("ÿ��" + horizontalName + ":��0����1.0," + STEP_LENGTH
				+ "Ϊ�����\r\nÿ��" + verticalName + ":��0����1.0," + STEP_LENGTH
				+ "Ϊ���\r\n");
		sb.append(ArraytoString.getTwoDeArrayString(coopertationLevels, L1, L2));
		return sb.toString();
	}
}