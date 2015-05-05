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

/** ��ά���񣬰���L*L����λ�������ɸ��� */
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

	/** ��¼���и���ı� */
	private ArrayList<Individual> IndividualList;
	/** ��ά���������п�λ����ά�����ʾ */
	private Seat[][] grid;
	/** ��ά����Ŀ�� */
	private int L;
//	/** ��ά�����и�����ܶ� */
//	private float d0;
	// /**
	// * �涨ÿ��������Χ�����Զ�ĸ�������ֱ���ھӣ����磺 ������Χ����Ϊ1����Ϊֱ���ھӣ�����ھ���Ϊ8
	// */
	// private int neighbourRange;

	private NeighbourCoverage neighbourCoverage;
	/** ��¼�������ܲ��õĲ��ԵĴ�����ֵ */
	private float[] strategySample;

	private Random random = new Random();

	/**
	 * ��ʼ��ģ��
	 * 
	 * @param length
	 *            ���񳤶�
	 * @param density
	 *            �����и�����ܶ�
	 * @param strategyPattern
	 *            ���и�����õĲ��Ĳ�������
	 * @param neighbourRange
	 *            ÿ��������Χֱ���ھӵ�������
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
			// ��ÿ��������������ʼ����
			initIndividuals(density, strategyPattern,w);

			// ��ÿ������������������е�λ��
			randomAllocateSeat();
		}

	}

	private void initIndividuals(float d0, StrategyPattern strategyPattern, float w) {

		int num = (int) (d0 * L * L); // ��������Ŀ
		// System.out.println("initIndividualStrategy"+num);
		int strategyNum = strategyPattern.getStrategyNum();

		strategySample = new float[strategyNum];

		int maxNeighbourNum = neighbourCoverage.getCoverageVector().size();
		// ��������������ӳ�ʼ����
		int count = 0;
		for (int i = 0; i < strategyNum; i++) {
			strategySample[i] = 0 + i / (float) (strategyNum - 1);
			for (int j = 0; j < num / strategyNum; j++) {
				this.IndividualList.add(new Individual(strategySample[i],
						maxNeighbourNum,w));
				count++;
			}
		}
		// ���ڲ�ȡ���ֲ��Եĸ�����Ŀ��ĳЩ����²�����ȫ����ȣ�ƽ����������µ����ɸ��尴�������ʣ�µ�����
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

	/** ��ÿ������������������е�λ�� */
	private void randomAllocateSeat() {
		int i = 0;
		// ��λ�ã�0,0����ʼ�����������������
		for (Iterator<Individual> e = IndividualList.iterator(); e.hasNext();) {
			getSeat(i).setOwner(e.next());
			i++;
		}
		// ʵ�ָ������������λ��������һ��ϴ���㷨������λ��ϴ�ơ�
		for (i = 0; i < L * L; i++) {
			swapSeat(getSeat(i), getSeat((int) (random.nextFloat() * L * L)));
		}
	}

	/** ��λ�ϵĸ���֮���໥����λ�� */
	private void swapSeat(Seat seat1, Seat seat2) {
		Individual tmp = seat1.getOwner();
		seat1.setOwner(seat2.getOwner());
		seat2.setOwner(tmp);
	}

	/** �õ�������,���ϵ��µ�loc��λ�õ���λ */
	private Seat getSeat(int loc) {
		int i = loc / L;
		int j = loc % L;
		return grid[i][j];
	}

	/** ���ض�ά����Ŀ�� */
	public int getLength() {
		return L;
	}

	/** �õ���i�У���j�еĸ��� */
	public Individual getIndividual(int i, int j) {
		return grid[i][j].getOwner();
	}

	/**
	 * һ��ʱ�䲽�����е����ڸ���֮�䶼Ҫ�����������ģ�ÿ�������ۼ��Լ�������
	 * 
	 * @param gr
	 *            ���Ĺ��򣬰�������Ӧ���������
	 */
	public void gambling(GamblingRule gr) {

		Individual neighbour;
		ArrayList<Seat> seatAround;

		for (Individual in : IndividualList) {
			in.resetAccumulatedPayoff();
			in.clearAllNeighbour();
		}

		for (Individual in : IndividualList) {

			seatAround = getOccupiedSeatAround(in.getSeat());// �õ�������Χ�����б�ռ����λ

			for (Seat s : seatAround) {
				neighbour = s.getOwner();
				if (!in.hasNeighbour(neighbour)) {
					// ��������Χ�������ھӣ�����еĻ�����ӵ������Լ��������ھӱ��С�
					// ��������ʹ���ھ�Ǩ�ƺ󣬸��������ҵ����ֳ�ʼ״̬�����ھ�
					in.addNeighbour(neighbour);
					neighbour.addNeighbour(in);
					//System.out.println( "interact :"+(in.getW() + neighbour.getW()) / 2);
					if (random.nextFloat() <= (in.getW() + neighbour.getW()) / 2) {
						// �������ھӵ��������ĺ��ۼ�����
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
	 * �ݻ�����pi�ĸ���ѧϰ�ھӵĲ��ԣ���qi�ĸ���Ǩ�㣬ͬʱ���¸���Ľ���ǿ��
	 * 
	 * @param pi
	 *            ѧϰ�ھӲ��Եĸ���
	 * @param qi
	 *            Ǩ��ĸ���
	 * @param learningPattern
	 *            ѧϰģʽ��������ѧϰ�����ھ�World.LEARNING_PATTERN_MAXPAYOFF,
	 *            Ҳ������fermiѧϰģʽWorld.LEARNING_PATTERN_FERMI
	 * @param imigrationPattern
	 *            Ǩ��ģʽ ����������Ǩ��World.IMIGRATE_PATTERN_NONE�����Ǩ��World.
	 *            IMIGRATE_PATTERN_RANDOM������Ǩ��World.IMIGRATE_PATTERN_OPTIMISTIC
	 * @return ��ȡ�²��Եĸ���ռ����ı���
	 */
	public float evolute(float pi, float qi, LearningPattern learningPattern,
			MigrationPattern imigrationPattern) {
		for (Individual in : IndividualList) {
			if (random.nextFloat() <= pi) {
				in.learnFromNeighbours(learningPattern, random);
			}
			if (random.nextFloat() <= qi) {
				// �����ڱ��ֽ���Ǩ�㣬ÿ�������ڲ��Ľ׶��Լ��������ھӱ�Ǩ����ھ������ҵ��ø���
				in.imigrate(this, imigrationPattern, random);
			}
			// getEmptySeatAround(in);
		}
		int change = 0;
		for (Individual in : IndividualList) {
			// ���������һ��Ҫ�õĲ���
			if (in.updateStrategy()) {
				change++;
			}
		}
		for (Individual in : IndividualList) {
			// ������½���ǿ��
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
	 * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ���λ
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

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ�������λ */
	public ArrayList<Seat> getAllSeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO �Զ����ɵķ������
				return true;
			}

		});
	}

	// ArrayList<Seat> emptySeatAround = new ArrayList<>();

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵġ��ա���λ */
	public ArrayList<Seat> getEmptySeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO �Զ����ɵķ������
				return object.isEmpty();
			}

		});
	}

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵı�ռ�ݵ���λ */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s) {
		return getSeatAround(s, new Condition<Seat>() {

			@Override
			public boolean test(Seat object) {
				// TODO �Զ����ɵķ������
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

	/** ���ظ������� */
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
