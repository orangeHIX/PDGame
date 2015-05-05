package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.NeighbourCoverage;
import rule.StrategyPattern;
import test.SpatialPDGame;
import utils.Vector;

/** 二维网格，包含L*L个空位，和若干个体 */
public class World {
	// public static final int LEARNING_PATTERN_MAXPAYOFF = 0;
	// public static final int LEARNING_PATTERN_FERMI = 1;
	//
	// public static final int IMIGRATION_PATTERN_NONE = 0;
	// public static final int IMIGRATION_PATTERN_RANDOM = 1;
	// public static final int IMIGRATION_PATTERN_OPTIMISTIC = 2;
	// public static final int IMIGRATION_PATTERN_ESCAPE = 3;
	//
	// public static final int STRATEGY_PATTERN_TWO = 0;
	// public static final int STRATEGY_PATTERN_THREE = 1;
	// public static final int STRATEGY_PATTERN_FIVE = 2;
	// public static final int STRATEGY_PATTERN_CONTINUOUS = 3;

	/** 记录所有个体的表 */
	private ArrayList<Individual> IndividualList;
	/** 二维网络中所有空位，二维数组表示 */
	private Seat[][] grid;
	/** 二维网格的宽度 */
	private int L;
//	/** 二维网格中个体的密度 */
//	private float d0;
	// /**
	// * 规定每个个体周围距离多远的个体算是直接邻居，例如： 个体周围距离为1的设为直接邻居，最大邻居数为8
	// */
	// private int neighbourRange;

	private NeighbourCoverage neighbourCoverage;
	/** 记录个体所能采用的策略的代表数值 */
	private float[] strategySample;

	private Random random = new Random();

	/**
	 * 初始化模型
	 * 
	 * @param length
	 *            网格长度
	 * @param density
	 *            网格中个体的密度
	 * @param strategyPattern
	 *            所有个体采用的博弈策略类型
	 * @param neighbourRange
	 *            每个个体周围直接邻居的最大距离
	 */
	public void init_world(int length, float density, float w,
			StrategyPattern strategyPattern, NeighbourCoverage neighbourCoverage) {
		if (length > 0 && density >= 0
				&& (density < 1.0 || density - 1.0f < 1.0e-5)) {
			L = length;
			//d0 = density;
			grid = new Seat[L][L];
			for (int i = 0; i < L; i++) {
				for (int j = 0; j < L; j++) {
					grid[i][j] = new Seat(i, j);
				}
			}
			// this.neighbourRange = neighbourRange;
			this.neighbourCoverage = neighbourCoverage;
			IndividualList = new ArrayList<>();
			// 给每个个体随机分配初始策略
			initIndividuals(density, strategyPattern,w);

			// 给每个个体随机分配网格中的位置
			randomAllocateSeat();
		}

	}

	private void initIndividuals(float d0, StrategyPattern strategyPattern, float w) {

		int num = (int) (d0 * L * L); // 个体总数目
		// System.out.println("initIndividualStrategy"+num);
		int strategyNum = strategyPattern.getStrategyNum();

		strategySample = new float[strategyNum];

		int maxNeighbourNum = neighbourCoverage.getCoverageVector().size();
		// 依次向个体表中添加初始个体
		int count = 0;
		for (int i = 0; i < strategyNum; i++) {
			strategySample[i] = 0 + i / (float) (strategyNum - 1);
			for (int j = 0; j < num / strategyNum; j++) {
				this.IndividualList.add(new Individual(strategySample[i],
						maxNeighbourNum,w));
				count++;
			}
		}
		// 由于采取各种策略的个体数目在某些情况下不可能全部相等，平均分配后余下的若干个体按次序分配剩下的名额
		if (count < num) {
			for (int i = 0; i < strategyNum; i++) {
				this.IndividualList.add(new Individual(strategySample[i],
						maxNeighbourNum,w));
				count++;
				if (count >= num) {
					break;
				}
			}
		}
		// int[] countS = new int[strategyNum];
		// for(int i = 0; i < strategyNum; i++){
		// countS[i] = 0;
		// }
		// for(Individual in : IndividualList){
		// countS[(int) (Math.floor((in.getStrategy()*(strategyNum-1))))]++;
		// }
		// int total = 0;
		// for(int i = 0; i < strategyNum; i++){
		// System.out.print("strategy " + i + ": "+ countS[i]+"\t");
		// total += countS[i];
		// }
		// System.out.println("total: "+ total);
	}

	/** 给每个个体随机分配网格中的位置 */
	private void randomAllocateSeat() {
		int i = 0;
		// 从位置（0,0）开始，个体依次向后入座
		for (Iterator<Individual> e = IndividualList.iterator(); e.hasNext();) {
			getSeat(i).setOwner(e.next());
			i++;
		}
		// 实现个体随机分配座位，本质是一种洗牌算法，对座位“洗牌”
		for (i = 0; i < L * L; i++) {
			swapSeat(getSeat(i), getSeat((int) (random.nextFloat() * L * L)));
		}
	}

	/** 座位上的个体之间相互交换位置 */
	private void swapSeat(Seat seat1, Seat seat2) {
		Individual tmp = seat1.getOwner();
		seat1.setOwner(seat2.getOwner());
		seat2.setOwner(tmp);
	}

	/** 得到从左到右,从上到下第loc个位置的座位 */
	private Seat getSeat(int loc) {
		int i = loc / L;
		int j = loc % L;
		return grid[i][j];
	}

	/** 返回二维网格的宽度 */
	public int getLength() {
		return L;
	}

	/** 得到第i行，第j列的个体 */
	public Individual getIndividual(int i, int j) {
		return grid[i][j].getOwner();
	}

	/**
	 * 一个时间步内所有的相邻个体之间都要进行两两博弈，每个个体累计自己的收益
	 * 
	 * @param gr
	 *            博弈规则，包含了相应的收益矩阵
	 */
	public void gambling(GamblingRule gr) {

		Individual neighbour;
		ArrayList<Seat> seatAround;

		for (Individual in : IndividualList) {
			in.resetAccumulatedPayoff();
			in.clearAllNeighbour();
		}

		for (Individual in : IndividualList) {

			seatAround = getOccupiedSeatAround(in.getSeat());// 得到个体周围的所有被占据座位

			for (Seat s : seatAround) {
				neighbour = s.getOwner();
				if (!in.hasNeighbour(neighbour)) {
					// 将个体周围的所有邻居（如果有的话）添加到个体自己保留的邻居表中。
					// 这样，即使有邻居迁移后，个体仍能找到本轮初始状态所有邻居
					in.addNeighbour(neighbour);
					neighbour.addNeighbour(in);
					//System.out.println( "interact :"+(in.getW() + neighbour.getW()) / 2);
					if (random.nextFloat() <= (in.getW() + neighbour.getW()) / 2) {
						// 个体与邻居的两两博弈后，累计收益
						//System.out.println("game : " +in+" and "+neighbour);
						in.accumulatePayoff(gr.getPayOff(in.getStrategy(),
								neighbour.getStrategy()));
						neighbour.accumulatePayoff(gr.getPayOff(
								neighbour.getStrategy(), in.getStrategy()));
					}
				}
			}
		}
	}

	/**
	 * 演化：以pi的概率学习邻居的策略，以qi的概率迁徙，同时更新个体的交互强度
	 * 
	 * @param pi
	 *            学习邻居策略的概率
	 * @param qi
	 *            迁徙的概率
	 * @param learningPattern
	 *            学习模式，可以是学习最优邻居World.LEARNING_PATTERN_MAXPAYOFF,
	 *            也可以是fermi学习模式World.LEARNING_PATTERN_FERMI
	 * @param imigrationPattern
	 *            迁徙模式 ，可以是无迁徙World.IMIGRATE_PATTERN_NONE、随机迁徙World.
	 *            IMIGRATE_PATTERN_RANDOM、机会迁徙World.IMIGRATE_PATTERN_OPTIMISTIC
	 * @return 采取新策略的个体占总体的比率
	 */
	public float evolute(float pi, float qi, LearningPattern learningPattern,
			MigrationPattern imigrationPattern) {
		for (Individual in : IndividualList) {
			if (random.nextFloat() <= pi) {
				in.learnFromNeighbours(learningPattern, random);
			}
			if (random.nextFloat() <= qi) {
				// 个体在本轮进行迁徙，每个个体在博弈阶段自己保留有邻居表，迁徙后邻居仍能找到该个体
				in.imigrate(this, imigrationPattern, random);
			}
			// getEmptySeatAround(in);
		}
		int change = 0;
		for (Individual in : IndividualList) {
			// 个体更新下一轮要用的策略
			if (in.updateStrategy()) {
				change++;
			}
		}
		for (Individual in : IndividualList) {
			// 个体更新交互强度
			in.updateInteractionIntensity(0, 0);
		}
		return change / (float) IndividualList.size();
	}

	public float getSeatDefectionLevel(Seat s) {
		ArrayList<Seat> seatAround = getOccupiedSeatAround(s);
		return seatAround.size() - getSeatCooperationLevel(s);
	}

	public float getSeatCooperationLevel(Seat s) {
		ArrayList<Seat> seatAround = getOccupiedSeatAround(s);
		// int n = 0;
		float seatCooperateLevel = 0.f;
		// Individual in;
		for (Seat sa : seatAround) {
			seatCooperateLevel += sa.owner.getStrategy();
			// n++;
		}
		return seatCooperateLevel;
	}

	// ArrayList<Seat> seatAround = new ArrayList<>();

	interface Condition<E> {
		public boolean test(E object);
	}

	/**
	 * 找到该座位周围所有在直接邻居距离范围内的座位
	 */
	public ArrayList<Seat> getSeatAround(Seat s, Condition<Seat> test) {
		// seatAround.clear();
		ArrayList<Seat> seatAround = new ArrayList<Seat>();
		int i, j;
		i = s.seat_i;
		j = s.seat_j;

		// for (int m = (i - neighbourRange > 0) ? i - neighbourRange : 0; (m <
		// i
		// + neighbourRange + 1)
		// && m < L; m++) {
		// for (int n = (j - neighbourRange > 0) ? j - neighbourRange : 0; (n <
		// j
		// + neighbourRange + 1)
		// && n < L; n++) {
		// if (m != i || n != j) {
		// if (test.test(grid[m][n])) {
		// seatAround.add(grid[m][n]);
		// }
		// }
		// }
		// }
		int x, y;
		for (Vector v : neighbourCoverage.getCoverageVector()) {
			x = i + v.x;
			y = j + v.y;
			if (x >= 0 && x < L && y >= 0 && y < L) {
				// System.out.println("seat("+i+","+j+"): "+x+","+y);
				if (test.test(grid[x][y])) {
					seatAround.add(grid[x][y]);
				}
			}
		}
		return seatAround;
	}

	/** 找到该座位周围所有在直接邻居距离范围内的所有座位 */
	public ArrayList<Seat> getAllSeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO 自动生成的方法存根
				return true;
			}

		});
	}

	// ArrayList<Seat> emptySeatAround = new ArrayList<>();

	/** 找到该座位周围所有在直接邻居距离范围内的“空”座位 */
	public ArrayList<Seat> getEmptySeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO 自动生成的方法存根
				return object.isEmpty();
			}

		});
	}

	/** 找到该座位周围所有在直接邻居距离范围内的被占据的座位 */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO 自动生成的方法存根
				return !object.isEmpty();
			}

		});
	}

	public float getGlobalCooperationLevel() {
		float cp = 0;
		for (Individual e : IndividualList) {
			cp += e.getStrategy();
		}
		return cp / IndividualList.size();
	}

	public WorldDetail getWorldDetail() {
		int i;
		float globalCooperationLevel = 0;
		float[] strategyProportion = new float[strategySample.length];
		for (i = 0; i < strategyProportion.length; i++) {
			strategyProportion[i] = 0;
		}
		for (Individual e : IndividualList) {
			globalCooperationLevel += e.getStrategy();
			for (i = 0; i < strategySample.length; i++) {
				if (Math.abs(e.getStrategy() - strategySample[i]) < 1.0e-4) {
					strategyProportion[i] += 1.0f;
					break;
				}
			}
		}
		globalCooperationLevel = globalCooperationLevel / IndividualList.size();
		for (i = 0; i < strategyProportion.length; i++) {
			strategyProportion[i] = strategyProportion[i]
					/ IndividualList.size();
		}
		return new WorldDetail(globalCooperationLevel, strategyProportion);
	}

	public float getAverageNeighbourNum() {
		float ann = 0;
		for (Individual e : IndividualList) {
			ann += (getAllSeatAround(e.getSeat()).size() - getEmptySeatAround(
					e.getSeat()).size());
//			System.out.println("getAverageNeighbourNum "+getAllSeatAround(e.getSeat()).size() +","+ getEmptySeatAround(
//					e.getSeat()).size());
		}
		return ann / IndividualList.size();
	}

	/** 返回个体总数 */
	public int getPopulationNum() {
		return IndividualList.size();
	}

	public String getIndividualStrategyPicture() {
		StringBuilder sb = new StringBuilder();
		Individual in;
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				in = grid[i][j].getOwner();
				sb.append(in == null ? null : in.getStrategy());
				sb.append('\t');
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public void printIndividualPayoff() {
		StringBuilder sb = new StringBuilder();
		Individual in;
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				in = grid[i][j].getOwner();
				sb.append(in == null ? null : in.getAccumulatedPayoff());
				sb.append('\t');
			}
			sb.append("\r\n");
		}
		System.out.println(sb.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				sb.append(grid[i][j]);
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

}
