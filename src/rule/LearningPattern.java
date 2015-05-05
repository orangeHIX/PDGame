package rule;

import java.util.ArrayList;

import entity.Individual;
import utils.RandomUtil;

public enum LearningPattern {
	/** MAXPAYOFF����ֱ���ھ�ѧϰ���ԣ�����ѧϰ��������ۻ�������ھ� */
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
	 * FERMI����ֱ���ھ�ѧϰ���ԣ�����i���ѡ��һ���ھ�j�󣬰�����ʽ�����Ƿ�ѧϰ���ھӣ�
	 * ����Pi��i�ı������棬Pj���ھ�j�ı������棬iѧϰ����j�ĸ���Ϊw=1/(1+exp[(pi-pj)/k]),
	 * k�����˻������������أ��̻��˸���ķ����Գ̶ȡ���ʱk������0ʱ����ζ��i������ȫ���ԡ�����ֻ��ѧϰ���������������Ϊ��
	 * ������k�����ӣ��������Գ̶Ƚ��ͣ�ѧϰ�������ھ���Ϊ�Ŀ���������
	 */
	FERMI("fermi_learning") {

		float noise = 0.1f; // ������ʱ��Ϊ0.1

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
