package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import entity.World.Seat;
import rule.GamblingRule;
import test.SpatialPDGame;

/** 二维网格博弈中的个体或博弈参与者 */
public class Individual {

	/** 个体采取的策略，取值[0.0,1.0]，0.0表示背叛(D)，1.0表示合作(C)，中间值表示中间策略 */
	float strategy;
	/** 个体下一轮博弈要采取的策略,进行策略更新应所有个体一起执行 */
	float nextStrategy;
	/** 累计收益，该个体与每个直接邻居博弈收益的总和 */
	float accumulatedPayoff;
	/** 个体所处的网格位置 */
	Seat seat;
	/** 个体每轮博弈的直接邻居表 */
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

	/** 累加收益，累计收益 = 累计收益 + payoff */
	public void accumulatePayoff(float payoff) {
		accumulatedPayoff += payoff;
	}

	/** 重置累计收益，置零 */
	public void resetAccumulatedPayoff() {
		accumulatedPayoff = 0;
	}

	public float getAccumulatedPayoff() {
		return accumulatedPayoff;
	}

	/** 向个体直接邻居表中加入一个个体 */
	public boolean addNeighbour(Individual neighbour) {
		return this.neighbours.add(neighbour);
	}

	/** 清空直接邻居表 */
	public void clearAllNeighbour() {
		this.neighbours.clear();
	}

	/**
	 * 尝试从邻居那里按照不同的方式学习策略<br>
	 * LEARNING_PATTERN_MAXPAYOFF：从直接邻居学习策略，个体学习具有最大累积收益的邻居<br>
	 * LEARNING_PATTERN_FERMI：从直接邻居学习策略，个体i随机选择一个邻居j后，按照下式决定是否学习该邻居，
	 * 其中Pi是i的本轮收益，Pj是邻居j的本轮收益，i学习后者j的概率为w=1/(1+exp[(pi-pj)/k]),
	 * k描述了环境的噪声因素，刻画了个体的非理性程度。当时k趋近于0时，意味着i具有完全理性——它只会学习高于自身收益的行为。
	 * 而随着k的增加，个体理性程度降低，学习低收益邻居行为的可能性增加
	 * 
	 * @param learningPattern
	 *            学习邻居策略的方式，LEARNING_PATTERN_MAXPAYOFF表示学习具有最大收益的邻居，
	 *            LEARNING_PATTERN_FERMI表示按照fermi策略学习邻居
	 * @param random
	 *            用于生成随机数
	 */
	public void learnFromNeighbours(int learningPattern, Random random) {
		nextStrategy = strategy;

		float noise = 0.1f; // 噪声暂时设为0.1

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
				throw new IllegalArgumentException("不合法的learningPattern");
			}

		}
	}

	/**
	 * 个体尝试迁徙到周围的空位，当imigratePattern是 World.IMIGRATE_PATTERN_NONE,个体什么也不做； <br>
	 * 当imigratePattern是IMIGRATE_PATTERN_RANDOM，个体随机迁徙到直接邻居距离范围内的一个空位上； <br>
	 * 当imigratePattern是World
	 * .IMIGRATE_PATTERN_OPTIMISTIC，个体个体i所在位置x为xk在一阶邻居中随机选取一个空位xl
	 * ，按照概率σ的可能性迁移的位置上。其中： <br>
	 * &nbsp;&nbsp; σ(xk→xl)= 1/(1+exp[(fcl-fck)/K]) K=0.1 （5）<br>
	 * 其中，fck表示个体位于k位置时邻居的平均合作程度，公式5表示个体i在随机邻居中选取一个空位l（没有则不移），
	 * 如果该空位l周围的邻居的平均合作程度fcl大于fck，则agent i有更大的可能性进行迁移。
	 * 
	 * 
	 * @param emptySeatAround
	 *            个体所在位置周围在直接邻居范围内的所有空位
	 * @param imigratePattern
	 *            迁徙模式 ，可以是无迁徙World.IMIGRATE_PATTERN_NONE、随机迁徙World.
	 *            IMIGRATE_PATTERN_RANDOM、机会迁徙World.IMIGRATE_PATTERN_OPTIMISTIC
	 * @param random
	 *            用于生成随机数
	 */
	public void imigrate(World world, int imigratePattern, Random random) {
		ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(this.seat);
		double imigratePosibility = 0;

		switch (imigratePattern) {
		case World.IMIGRATION_PATTERN_NONE:
			// 什么也不做
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
				// 迁徙到新位置。之所以事先迁徙，是考虑到这样做才不会影响到选中空位置合作水平的计算
				this.seat.setOwner(null);
				emptySeat.setOwner(this);

				float escl = world.getSeatCooperationLevel(emptySeat);

				imigratePosibility = 1 / (1 + Math.exp(scl - escl));

				if (random.nextDouble() < imigratePosibility) {
					// 个体迁徙，保持之前的迁徙动作结果
				} else {
					// 回到原来的位置
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
	 * 更新个体采取的策略
	 * 
	 * @return true个体改变了策略，false个体保持原来的策略
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
