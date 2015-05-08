package rule;

import java.util.ArrayList;

import entity.Individual;
import entity.Seat;
import entity.WorldInfo;
import utils.RandomUtil;

public enum MigrationPattern {
	/** NONE,个体什么也不做 */
	NONE("no_migrate"),
	/** RANDOM，个体随机迁徙到直接邻居距离范围内的一个空位上 */
	RANDOM("random_migrate") {
		@Override
		public Seat migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			if (!emptySeatAround.isEmpty()) {
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));
				return emptySeat;
			}
			return in.getSeat();
		}

	},
	/**
	 * OPTIMISTIC，个体个体i所在位置x为xk在一阶邻居中随机选取一个空位xl ，按照概率σ的可能性迁移的位置上。其中： <br>
	 * &nbsp;&nbsp; σ(xk→xl)= 1/(1+exp[(fcl-fck)/K]) K=0.1 （5）<br>
	 * 其中，fck表示个体位于k位置时邻居的平均合作程度，公式5表示个体i在随机邻居中选取一个空位l（没有则不移），
	 * 如果该空位l周围的邻居的平均合作程度fcl大于fck，则agent i有更大的可能性进行迁移。
	 */
	OPTIMISTIC("optimistic_migrate") {
		@Override
		public Seat migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			Seat result = in.getSeat();
			if (!emptySeatAround.isEmpty()) {
				Seat tmpSeat = in.getSeat();
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));

				float scl = world.getSeatCooperationLevel(in.getSeat());
				// 迁徙到新位置。之所以事先迁徙，是考虑到这样做才不会影响到选中空位置合作水平的计算
				in.moveTo(emptySeat);

				float escl = world.getSeatCooperationLevel(emptySeat);

				double imigratePosibility = 1 / (1 + Math.exp(scl - escl));

				if (RandomUtil.nextDouble() < imigratePosibility) {
					result = emptySeat;
				} 
				// 回到原来的位置
				in.moveTo(tmpSeat);
			}
			return result;
		}

	},
	/** 个体统计周围邻居背叛者的数量nd，以nd/8的概率迁徙到空位上 */
	ESCAPE("escape_migrate") {
		@Override
		public Seat migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			if (!emptySeatAround.isEmpty()) {
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));
				float dl = world.getSeatDefectionLevel(in.getSeat());
				double imigratePosibility = dl / 8;
				if (RandomUtil.nextDouble() < imigratePosibility) {
					return emptySeat;
				}
			}
			return in.getSeat();
		}

	};

	public String name;

	private MigrationPattern(String s) {
		name = s;
	}
	/**个体按照该实例所指的迁徙模式尝试迁徙到某个位置上
	 * @param in 要尝试迁徙的个体
	 * @param world 能提供迁徙所需模型必要信息的接口
	* @return 应该迁徙到的位置，也可能没有迁徙，返回的位置是个体原来所在的的位置 */
	public Seat migrate(Individual in, WorldInfo world) {
		return in.getSeat();
	}

	@Override
	public String toString() {
		return name;
	}
}
