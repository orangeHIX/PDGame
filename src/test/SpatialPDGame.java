package test;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import utils.ArraytoString;
import utils.FileUtils;
import utils.Reporter;
import entity.World;
import entity.WorldDetail;
import graphic.Painter;
import gui.DebugWindow;

public class SpatialPDGame implements Reporter {

	/** 无变化门限，若连续超过此值的博弈演化轮数所有个体的策略保持不变，则认为模型已经稳定，不再进行演化 */
	public static final int noChangeThreshold = 100;
	/** 采样间隔，每隔间隔轮数进行一次采样，统计合作水平 */
	// public static final int sampleInterval = 100;
	/** 网格长度 */
	public static final int LENGTH = 100;

	public static final int MAX_TURN_NUM = 30000;

	public static final float STEP_LENGTH = 0.1f;

	public static void main(String args[]) {
		// SpatialPDGame spdg = new SpatialPDGame(false);
		// spdg.initSpatialPDGame(10, 1.0f, 0.1f, 0.1f, 1.0f, 0, 1.0f,
		// LearningPattern.FERMI, MigrationPattern.NONE, StrategyPattern.TWO,
		// NeighbourCoverage.Von);
		// spdg.run(1);
		// spdg.finalize();
		// System.out.println(spdg.getDetailReport());
		// spdg.printPicture();

		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 4; j++) {
		// long start = System.currentTimeMillis();
		// runOneTest(World.LEARNING_PATTERN_MAXPAYOFF,
		// World.IMIGRATION_PATTERN_ESCAPE, j, MAX_TURN_NUM,
		// "F:\\毕设任务\\data");
		// long end = System.currentTimeMillis();
		// System.out.println("underwent: " + (end - start) + "ms");
		// }
		// float qi = 0;
		long start = System.currentTimeMillis();
		runOneTest4(LearningPattern.INTERACTIVE_FERMI, MigrationPattern.NONE,
				StrategyPattern.TWO, NeighbourCoverage.Von, MAX_TURN_NUM,
				"F:\\交互强度任务\\data4");
		long end = System.currentTimeMillis();
		System.out.println("underwent: " + (end - start) + "ms");
		// }
	}

	/** 用于本科论文实验的启动方法 */
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
			FileUtils.outputTofile(spdg.dataPrinter.constructFilePath(outputFilePath)
					+ "\\CoopertationLevel.txt",
					getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));
		}
	}

	/** 用于本科论文实验的启动方法 */
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
			FileUtils.outputTofile(spdg.dataPrinter.constructFileName(outputFilePath),
					spdg.dataPrinter.getDetailReport());
			FileUtils.outputSnapshootToFile(
					spdg.dataPrinter.constructImageFilePath(outputFilePath),
					spdg.getSnapshootMap());

		}
		// System.out.println("" + learningPattern + "," + imigratePattern + ","
		// + strategyPattern + " gr=" + r + ", d0= " + d0 + " completed");
	}

	/** 这个方法是用于做交互强度试验的 */
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
		for (w = .0f; w <= 1.0001f; w += stepLength) {
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
					FileUtils.outputSnapshootToFile(
							spdg.dataPrinter.constructImageFilePath(outputFilePath),
							spdg.getSnapshootMap());

					cl[i][j] = spdg.getCooperationLevel();
					System.out.println("" + learningPattern + ","
							+ imigratePattern + "," + strategyPattern + ", Dr="
							+ Dr + ", Dg=" + Dg + ", w= " + w + " completed");
				}
			}
			FileUtils.outputTofile(spdg.dataPrinter.constructFilePath(outputFilePath)
					+ "\\CoopertationLevel.txt",
					getCoopertationLevelsReport(cl, "Dg", "Dr", L1, L2));

		}
	}

	/** 生成本次实验产生的合作率数组文本文件 */
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
		sb.append("整体平均合作水平：" + ALLPDCooLevel + "\r\n");
		sb.append("第一行平均合作水平：" + CooLevel2 + "\r\n");
		sb.append("第一列平均合作水平：" + CooLevel3 + "\r\n");
		sb.append("左上到右下对角线平均合作水平：" + CooLevel4 + "\r\n");
		sb.append("每行" + horizontalName + ":从0增大到1.0," + STEP_LENGTH
				+ "为间隔；\r\n每列" + verticalName + ":从0增大到1.0," + STEP_LENGTH
				+ "为间隔\r\n");
		sb.append(ArraytoString.getTwoDeArrayString(coopertationLevels, L1, L2));
		return sb.toString();
	}

	// =============================从这里开始，下面的是空间受限网络模拟器================================================//

	private World world;

	private GamblingRule gr;

	private LearningPattern learningPattern;

	private MigrationPattern migrationPattern;
	private StrategyPattern strategyPattern;

	private float d0;
	/** 学习的概率 */
	private float pi;
	/** 迁徙的概率 */
	private float qi;
	/** 初始所有个体的交互强度 */
	private float w;
	/** 当前实验进行到的轮数 */
	private int turn;
	/** 当前实验中个体策略不再发生变化的累计轮数，可能会因为个体策略发生变化而被重置为0 */
	private int noChangeTurn;
	/** 当前实验是否结束 */
	private boolean isFinished;
	/** 是否要记录实验过程中的人口斑图 */
	private final boolean recordSnapShoot;
	/** 用于输出调试信息的接口 */
	private Reporter reporter;
	/** 记录特定轮数模型快照的容器 */
	private Map<Integer, WorldDetail> worldDetailHistory = new HashMap<>();

	/** 记录实验过程中的人口斑图 的容器 */
	private Map<Integer, Image> Snapshoot = new HashMap<>();
	
	public DataPrinter dataPrinter = new DataPrinter();

	/**
	 * @param recordSnapShoot
	 *            是否要记录实验过程中的人口斑图
	 */
	public SpatialPDGame(boolean recordSnapShoot) {
		world = new World();
		this.recordSnapShoot = recordSnapShoot;
		this.reporter = this;
	}

	public SpatialPDGame(boolean recordSnapShoot, Reporter reporter) {
		world = new World();
		this.recordSnapShoot = recordSnapShoot;
		this.reporter = reporter;
	}

	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}

	/** 结束实验，模型将会被冻结，无法再进行演化（run） */
	public void done() {
		if (!isFinished) {
			isFinished = true;
			// globalCoopertationLevelHistory.put(turn,
			// world.getGlobalCooperationLevel());
			worldDetailHistory.put(turn, world.getWorldDetail());
			recordSnapshoot();
		}
	}

	public float getCooperationLevel() {
		return world.getGlobalCooperationLevel();
	}

	// float averageNeighbourNum = 0;
	public Image getCurrentPicture() {
		return Painter.getPDGameImage(400, 400, world);
	}

	

	public Map<Integer, Image> getSnapshootMap() {
		return Snapshoot;
	}

	/**该实验所用囚徒矩阵((R，S),(T,P))
	 * @param pi
	 *            学习邻居策略的概率
	 * @param qi
	 *            迁徙的概率
	 * @param w
	 *            初始所有个体的交互强度
	 * @param learningPattern
	 *            学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
	 * @param migrationPattern
	 *            迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
	 * 
	 */
	public void initSpatialPDGame(int L, float d0, float R, float S, float T,
			float P, float pi, float qi, float w,
			LearningPattern learningPattern, MigrationPattern imigratePattern,
			StrategyPattern strategyPattern, NeighbourCoverage neighbourCoverage) {

		world.init_world(L, d0, w, strategyPattern, neighbourCoverage);
		gr = new GamblingRule(R, S, T, P);
		this.d0 = d0;
		this.pi = pi;
		this.qi = qi;
		this.w = w;
		this.learningPattern = learningPattern;
		this.migrationPattern = imigratePattern;
		this.strategyPattern = strategyPattern;
		turn = 0;
		noChangeTurn = 0;
		isFinished = false;
		Snapshoot.clear();
		worldDetailHistory.clear();
	}
	/**该实验所用囚徒矩阵((1，-Dr),(1+Dg,0))
	 * @param pi
	 *            学习邻居策略的概率
	 * @param qi
	 *            迁徙的概率
	 * @param w
	 *            初始所有个体的交互强度
	 * @param learningPattern
	 *            学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
	 * @param migrationPattern
	 *            迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
	 * 
	 */
	public void initSpatialPDGame(int L, float d0, float Dr, float Dg,
			float pi, float qi, float w, LearningPattern learningPattern,
			MigrationPattern imigratePattern, StrategyPattern strategyPattern,
			NeighbourCoverage neighbourCoverage) {

		initSpatialPDGame(L, d0, 1, -Dr, 1 + Dg, 0, pi, qi, w, learningPattern,
				imigratePattern, strategyPattern, neighbourCoverage);

	}
	/**该实验所用囚徒矩阵((1，-r),(1+r,0))
	 * @param pi
	 *            学习邻居策略的概率
	 * @param qi
	 *            迁徙的概率
	 * @param w
	 *            初始所有个体的交互强度
	 * @param learningPattern
	 *            学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
	 * @param migrationPattern
	 *            迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
	 * 
	 */
	public void initSpatialPDGame(int L, float d0, float r, float pi, float qi,
			float w, LearningPattern learningPattern,
			MigrationPattern imigratePattern, StrategyPattern strategyPattern,
			NeighbourCoverage neighbourCoverage) {

		initSpatialPDGame(L, d0, r, r, pi, qi, w, learningPattern,
				imigratePattern, strategyPattern, neighbourCoverage);

	}



	private void recordSnapshoot() {
		if (recordSnapShoot) {
			Snapshoot.put(turn, getCurrentPicture());
			reporter.report("snapshoot at turn " + turn);
		}
	}
	/**实验进行若干轮
	 * @param turnNum 实验要进行的轮数*/
	public void run(int turnNum) {
		run(turnNum, new SampleCheck() {

			@Override
			public boolean isWorldDetailHistorySampleTurn(int turn) {
				// TODO Auto-generated method stub
				if ((turn) % 100 == 0) {
					return true;
				}
				return false;
			}

			@Override
			public boolean isSnapshootSampleTurn(int turn) {
				// TODO Auto-generated method stub
				if (turn == Math.pow(10, Snapshoot.size())) {
					return true;
				}
				return false;
			}

		});
	}
	/**实验进行若干轮
	 * @param turnNum 实验要进行的轮数
	 * @param sampleCheck 是否是特定采样轮数的检查*/
	public void run(int turnNum, SampleCheck sampleCheck) {
		if (!isFinished) {
			int cumulativeTurnNum = 0;
			//如果是初次调用该方法，先进行一次采样记录模型初始状态
			if (turn == 0) {
				// globalCoopertationLevelHistory.clear();
				worldDetailHistory.clear();
				Snapshoot.clear();
				noChangeTurn = 0;

				worldDetailHistory.put(turn, world.getWorldDetail());

				recordSnapshoot();

			}

			int population = world.getPopulationNum();
			
			while (cumulativeTurnNum < turnNum) {
				turn++;
				cumulativeTurnNum++;
				world.gambling(gr);
				if (world.evolute(pi, qi, learningPattern, migrationPattern) < 1 / (float) population) {
					noChangeTurn++;
					if (noChangeTurn >= 100) {
						break;
					}
				} else {
					noChangeTurn = 0;
				}
				if (sampleCheck.isWorldDetailHistorySampleTurn(turn)) {
					// globalCoopertationLevelHistory.put(turn,
					// world.getGlobalCooperationLevel());
					worldDetailHistory.put(turn, world.getWorldDetail());
				}
				if ((turn) % (2000) == 0) {
					reporter.report("turn " + turn + " ......");
				}
				if (sampleCheck.isSnapshootSampleTurn(turn)) {
					recordSnapshoot();
				}
				// System.out.println("合作率："+world.getCooperationRate());
			}
		}

	}
	/**获取当前实验进行到的轮数*/
	public int getTurn() {
		return turn;
	}

	

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "[L=" + world.getLength() + ", d0=" + df.format(d0) + ", " + gr
				+ ", learningPattern=" + learningPattern + ", imigratePattern="
				+ migrationPattern + ", strategyPattern=" + strategyPattern
				+ ", pi=" + pi + ", qi=" + qi + "]";
	}

	@Override
	public void report(String s) {
		// TODO Auto-generated method stub
		System.out.println(s);
	}
	
	public class DataPrinter
	{
		/** 获取本次实验最终结果报告 */
		public String getDetailReport() {
			StringBuilder sb = new StringBuilder();
			ArrayList<Integer> keyList = new ArrayList<>();
			// keyList.addAll(globalCoopertationLevelHistory.keySet());
			// Collections.sort(keyList);
			keyList.addAll(worldDetailHistory.keySet());
			Collections.sort(keyList);

			sb.append(this.toString());
			sb.append("\r\n");
			sb.append("Global cooperate level = "
					+ world.getGlobalCooperationLevel());
			sb.append("\tunderwent " + turn + " turns\r\n");
			sb.append("average neigbour num: " + world.getAverageNeighbourNum()
					+ "\r\n");
			sb.append("turn\tcoopLev\t");

			WorldDetail wd = worldDetailHistory.get(new Integer(0));
			int strategyNum = 0;
			if (wd != null) {
				strategyNum = wd.strategyProportion.length;
			}
			for (int i = 0; i < strategyNum; i++) {
				sb.append("s" + i + "\t");
			}
			sb.append("\r\n");

			DecimalFormat df = new DecimalFormat("0.000");

			for (Integer i : keyList) {
				sb.append(i);
				sb.append('\t');
				wd = worldDetailHistory.get(i);
				sb.append(df.format(wd.globalCooperationLevel));
				sb.append('\t');
				for (int j = 0; j < strategyNum; j++) {
					sb.append(df.format(wd.strategyProportion[j]));
					sb.append('\t');
				}
				sb.append("\r\n");
			}
			sb.append("\r\nfinal stragety picture:\r\n");
			sb.append(world.getIndividualStrategyPicture());
			return sb.toString();
		}
		/** 显示模型散点图 */
		public void printPicture() {

			// 创建frame
			JFrame frame = new DebugWindow(world);
			// 调整frame的大小和初始位置
			frame.setSize(880, 880);
			frame.setLocation(100, 100);
			// 增加窗口监听事件，使用内部类方法，并用监听器的默认适配器
			frame.addWindowListener(new WindowAdapter() {

				// 重写窗口关闭事件
				@Override
				public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}

			});
			frame.setTitle(this.toString());
			// 显示窗体
			frame.setVisible(true);
		}
		public String constructFilePath(String base) {
			DecimalFormat df = new DecimalFormat("0.00");
			return base + "\\" + learningPattern + "_$_" + migrationPattern + "_$_"
					+ strategyPattern + "_$_pi=" + df.format(pi) + "_$_qi="
					+ df.format(qi) + "_$_w=" + df.format(w);
		}

		public String constructFileName(String base) {
			DecimalFormat df = new DecimalFormat("0.00");
			return constructFilePath(base) + "\\" + "gr=(" + df.format(gr.getR())
					+ "," + df.format(gr.getS()) + "," + df.format(gr.getT()) + ","
					+ df.format(gr.getP()) + ")" + "_$_d0=" + df.format(d0)
					+ ".txt";
		}

		public String constructImageFilePath(String base) {
			return constructFileName(base).replace(".txt", "");
		}
	}

	public static interface SampleCheck {
		/**
		 * 判断是否需要采样，每隔间隔轮数进行一次采样，统计合作水平
		 * 
		 * @param 当前演化轮数
		 */
		public boolean isWorldDetailHistorySampleTurn(int turn);

		/**
		 * 判断是否需要记录演化斑图
		 * 
		 * @param 当前演化轮数
		 */
		public boolean isSnapshootSampleTurn(int turn);
	}
}
