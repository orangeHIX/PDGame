package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import entity.World.Seat;
import rule.GamblingRule;
import test.SpatialPDGame;

/** ��ά�������еĸ�����Ĳ����� */
public class Individual {

	/** �����ȡ�Ĳ��ԣ�ȡֵ[0.0,1.0]��0.0��ʾ����(D)��1.0��ʾ����(C)���м�ֵ��ʾ�м���� */
	float strategy;
	/** ������һ�ֲ���Ҫ��ȡ�Ĳ���,���в��Ը���Ӧ���и���һ��ִ�� */
	float nextStrategy;
	/** �ۼ����棬�ø�����ÿ��ֱ���ھӲ���������ܺ� */
	float accumulatedPayoff;
	/** ��������������λ�� */
	Seat seat;
	/** ����ÿ�ֲ��ĵ�ֱ���ھӱ� */
	ArrayList<Individual> neighbours;

	public Individual(float strategy, int maxNeighbourNum) {
		super();
		if (strategy >= 0.0f && strategy <= 1.0f) {
			this.strategy = strategy;
			this.nextStrategy = this.strategy;
		} else {
			this.strategy = GamblingRule.STRATEGY_D;
		}
		accumulatedPayoff = 0;
		seat = null;
		neighbours = new ArrayList<>(maxNeighbourNum);
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public float getStrategy() {
		return strategy;
	}

	/** �ۼ����棬�ۼ����� = �ۼ����� + payoff */
	public void accumulatePayoff(float payoff) {
		accumulatedPayoff += payoff;
	}

	/** �����ۼ����棬���� */
	public void resetAccumulatedPayoff() {
		accumulatedPayoff = 0;
	}

	public float getAccumulatedPayoff() {
		return accumulatedPayoff;
	}

	/** �����ֱ���ھӱ��м���һ������ */
	public boolean addNeighbour(Individual neighbour) {
		return this.neighbours.add(neighbour);
	}

	/** ���ֱ���ھӱ� */
	public void clearAllNeighbour() {
		this.neighbours.clear();
	}

	/**
	 * ���Դ��ھ����ﰴ�ղ�ͬ�ķ�ʽѧϰ����<br>
	 * LEARNING_PATTERN_MAXPAYOFF����ֱ���ھ�ѧϰ���ԣ�����ѧϰ��������ۻ�������ھ�<br>
	 * LEARNING_PATTERN_FERMI����ֱ���ھ�ѧϰ���ԣ�����i���ѡ��һ���ھ�j�󣬰�����ʽ�����Ƿ�ѧϰ���ھӣ�
	 * ����Pi��i�ı������棬Pj���ھ�j�ı������棬iѧϰ����j�ĸ���Ϊw=1/(1+exp[(pi-pj)/k]),
	 * k�����˻������������أ��̻��˸���ķ����Գ̶ȡ���ʱk������0ʱ����ζ��i������ȫ���ԡ�����ֻ��ѧϰ���������������Ϊ��
	 * ������k�����ӣ��������Գ̶Ƚ��ͣ�ѧϰ�������ھ���Ϊ�Ŀ���������
	 * 
	 * @param learningPattern
	 *            ѧϰ�ھӲ��Եķ�ʽ��LEARNING_PATTERN_MAXPAYOFF��ʾѧϰ�������������ھӣ�
	 *            LEARNING_PATTERN_FERMI��ʾ����fermi����ѧϰ�ھ�
	 * @param random
	 *            �������������
	 */
	public void learnFromNeighbours(int learningPattern, Random random) {
		nextStrategy = strategy;

		float noise = 0.1f; // ������ʱ��Ϊ0.1

		if (!neighbours.isEmpty()) {
			switch (learningPattern) {
			case World.LEARNING_PATTERN_MAXPAYOFF:
				Individual maxPayoffNeighbour = this;
				for (Individual in : neighbours) {
					if (in.getAccumulatedPayoff() > maxPayoffNeighbour
							.getAccumulatedPayoff())
						maxPayoffNeighbour = in;

				}
				nextStrategy = maxPayoffNeighbour.getStrategy();
				// System.out.println(""+this+" learn from \n\t"+
				// maxPayoffNeighbour);
				break;
			case World.LEARNING_PATTERN_FERMI:
				double imitatePosibility = 0;
				Individual neighbour = neighbours
						.get((int) (random.nextFloat() * neighbours.size()));
				imitatePosibility = 1 / (1 + Math
						.exp((accumulatedPayoff - neighbour
								.getAccumulatedPayoff()) / noise));
				if (random.nextDouble() <= imitatePosibility) {
					nextStrategy = neighbour.getStrategy();
					// System.out.println(""+this+" learn from \n\t"+neighbour+" with "
					// +imitatePosibility);
				}
				break;
			default:
				throw new IllegalArgumentException("���Ϸ���learningPattern");
			}

		}
	}

	/**
	 * ���峢��Ǩ�㵽��Χ�Ŀ�λ����imigratePattern�� World.IMIGRATE_PATTERN_NONE,����ʲôҲ������ <br>
	 * ��imigratePattern��IMIGRATE_PATTERN_RANDOM���������Ǩ�㵽ֱ���ھӾ��뷶Χ�ڵ�һ����λ�ϣ� <br>
	 * ��imigratePattern��World
	 * .IMIGRATE_PATTERN_OPTIMISTIC���������i����λ��xΪxk��һ���ھ������ѡȡһ����λxl
	 * �����ո��ʦҵĿ�����Ǩ�Ƶ�λ���ϡ����У� <br>
	 * &nbsp;&nbsp; ��(xk��xl)= 1/(1+exp[(fcl-fck)/K]) K=0.1 ��5��<br>
	 * ���У�fck��ʾ����λ��kλ��ʱ�ھӵ�ƽ�������̶ȣ���ʽ5��ʾ����i������ھ���ѡȡһ����λl��û�����ƣ���
	 * ����ÿ�λl��Χ���ھӵ�ƽ�������̶�fcl����fck����agent i�и���Ŀ����Խ���Ǩ�ơ�
	 * 
	 * 
	 * @param emptySeatAround
	 *            ��������λ����Χ��ֱ���ھӷ�Χ�ڵ����п�λ
	 * @param imigratePattern
	 *            Ǩ��ģʽ ����������Ǩ��World.IMIGRATE_PATTERN_NONE�����Ǩ��World.
	 *            IMIGRATE_PATTERN_RANDOM������Ǩ��World.IMIGRATE_PATTERN_OPTIMISTIC
	 * @param random
	 *            �������������
	 */
	public void imigrate(World world, int imigratePattern, Random random) {
		ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(this.seat);
		double imigratePosibility = 0;

		switch (imigratePattern) {
		case World.IMIGRATION_PATTERN_NONE:
			// ʲôҲ����
			break;
		case World.IMIGRATION_PATTERN_RANDOM:
			if (!emptySeatAround.isEmpty()) {
//				float dl = world.getSeatDefectionLevel(this.seat);
//				imigratePosibility = dl / 8;
//				if (random.nextDouble() < imigratePosibility) {
//					this.seat.setOwner(null);
//					emptySeatAround
//							.get((int) (random.nextFloat() * emptySeatAround
//									.size())).setOwner(this);
//				}
				Seat emptySeat = emptySeatAround
						.get((int) (random.nextFloat() * emptySeatAround.size()));
				this.seat.setOwner(null);
				emptySeat.setOwner(this);
			}
			break;
		case World.IMIGRATION_PATTERN_OPTIMISTIC:
			if (!emptySeatAround.isEmpty()) {
				Seat tmpSeat = this.seat;
				Seat emptySeat = emptySeatAround
						.get((int) (random.nextFloat() * emptySeatAround.size()));

				float scl = world.getSeatCooperationLevel(this.getSeat());
				// Ǩ�㵽��λ�á�֮��������Ǩ�㣬�ǿ��ǵ��������Ų���Ӱ�쵽ѡ�п�λ�ú���ˮƽ�ļ���
				this.seat.setOwner(null);
				emptySeat.setOwner(this);

				float escl = world.getSeatCooperationLevel(emptySeat);

				imigratePosibility = 1 / (1 + Math.exp(scl - escl));

				if (random.nextDouble() < imigratePosibility) {
					// ����Ǩ�㣬����֮ǰ��Ǩ�㶯�����
				} else {
					// �ص�ԭ����λ��
					this.seat.setOwner(null);
					tmpSeat.setOwner(this);
				}
			}
			break;
		case World.IMIGRATION_PATTERN_ESCAPE:
			if (!emptySeatAround.isEmpty()) {
				float dl = world.getSeatDefectionLevel(this.seat);
				imigratePosibility = dl / 8;
				if (random.nextDouble() < imigratePosibility) {
					this.seat.setOwner(null);
					emptySeatAround
							.get((int) (random.nextFloat() * emptySeatAround
									.size())).setOwner(this);
				}
			}
			break;
		default:
			throw new IllegalArgumentException("illegal imigratePattern");
		}

	}

	/**
	 * ���¸����ȡ�Ĳ���
	 * 
	 * @return true����ı��˲��ԣ�false���屣��ԭ���Ĳ���
	 */
	public boolean updateStrategy() {
		if (strategy != nextStrategy) {
			strategy = nextStrategy;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Individual [s=" + strategy + ", ns=" + nextStrategy
				+ ", payoff=" + accumulatedPayoff + ",(" + seat.seat_i + ","
				+ seat.seat_j + ") ] ";
	}

}
