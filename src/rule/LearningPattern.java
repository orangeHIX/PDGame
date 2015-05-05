package rule;

import java.util.ArrayList;

import entity.Individual;
import utils.RandomUtil;

public enum LearningPattern {
	/** MAXPAYOFF：从直接邻居学习策略，个体学习具有最大累积收益的邻居 */
	MAXPAYOFF("max_payoff_learning") {
		@Override
		public void learn(Individual in) {
			Individual maxPayoffNeighbour = in;
			final ArrayList<Individual> neighbours = in.getNeighbours();
			for (Individual nei : neighbours) {
				if (nei.getAccumulatedPayoff() > maxPayoffNeighbour
						.getAccumulatedPayoff())
					maxPayoffNeighbour = nei;
			}
			in.setNextStrategy(maxPayoffNeighbour.getStrategy());
		}
	},
	/**
	 * FERMI：从直接邻居学习策略，个体i随机选择一个邻居j后，按照下式决定是否学习该邻居，
	 * 其中Pi是i的本轮收益，Pj是邻居j的本轮收益，i学习后者j的概率为w=1/(1+exp[(pi-pj)/k]),
	 * k描述了环境的噪声因素，刻画了个体的非理性程度。当时k趋近于0时，意味着i具有完全理性——它只会学习高于自身收益的行为。
	 * 而随着k的增加，个体理性程度降低，学习低收益邻居行为的可能性增加
	 */
	FERMI("fermi_learning") {

		float noise = 0.1f; // 噪声暂时设为0.1

		@Override
		public void learn(Individual in) {
			// TODO Auto-generated method stub
			double imitatePosibility = 0;
			final ArrayList<Individual> neighbours = in.getNeighbours();
			if (!neighbours.isEmpty()) {
				Individual neighbour = neighbours.get((int) (RandomUtil
						.nextFloat() * neighbours.size()));
				imitatePosibility = 1 / (1 + Math.exp((in
						.getAccumulatedPayoff() - neighbour
						.getAccumulatedPayoff())
						/ noise));
				if (RandomUtil.nextDouble() <= imitatePosibility) {
					in.setNextStrategy(neighbour.getStrategy());
					// System.out.println(""+this+" learn from \n\t"+neighbour+" with "
					// +imitatePosibility);
				}
			}
		}
	};
	public String name;

	private LearningPattern(String s) {
		name = s;
	}

	public void learn(Individual in) {
		// do nothing
	}

	@Override
	public String toString() {
		return name;
	}
}
