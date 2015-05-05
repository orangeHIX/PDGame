package rule;

import java.util.ArrayList;

import entity.Individual;
import entity.Seat;
import entity.WorldInfo;
import utils.RandomUtil;

public enum MigrationPattern {
	/** NONE,����ʲôҲ���� */
	NONE("no_migrate"),
	/** RANDOM���������Ǩ�㵽ֱ���ھӾ��뷶Χ�ڵ�һ����λ�� */
	RANDOM("random_migrate") {
		@Override
		public void migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			if (!emptySeatAround.isEmpty()) {
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));
				in.moveTo(emptySeat);
			}
		}

	},
	/**
	 * OPTIMISTIC���������i����λ��xΪxk��һ���ھ������ѡȡһ����λxl �����ո��ʦҵĿ�����Ǩ�Ƶ�λ���ϡ����У� <br>
	 * &nbsp;&nbsp; ��(xk��xl)= 1/(1+exp[(fcl-fck)/K]) K=0.1 ��5��<br>
	 * ���У�fck��ʾ����λ��kλ��ʱ�ھӵ�ƽ�������̶ȣ���ʽ5��ʾ����i������ھ���ѡȡһ����λl��û�����ƣ���
	 * ����ÿ�λl��Χ���ھӵ�ƽ�������̶�fcl����fck����agent i�и���Ŀ����Խ���Ǩ�ơ�
	 */
	OPTIMISTIC("optimistic_migrate") {
		@Override
		public void migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			if (!emptySeatAround.isEmpty()) {
				Seat tmpSeat = in.getSeat();
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));

				float scl = world.getSeatCooperationLevel(in.getSeat());
				// Ǩ�㵽��λ�á�֮��������Ǩ�㣬�ǿ��ǵ��������Ų���Ӱ�쵽ѡ�п�λ�ú���ˮƽ�ļ���
				in.moveTo(emptySeat);

				float escl = world.getSeatCooperationLevel(emptySeat);

				double imigratePosibility = 1 / (1 + Math.exp(scl - escl));

				if (RandomUtil.nextDouble() < imigratePosibility) {
					// ����Ǩ�㣬����֮ǰ��Ǩ�㶯�����
				} else {
					// �ص�ԭ����λ��
					in.moveTo(tmpSeat);
				}
			}
		}

	},
	/**����ͳ����Χ�ھӱ����ߵ�����nd����nd/8�ĸ���Ǩ�㵽��λ��*/
	ESCAPE("escape_migrate") {
		@Override
		public void migrate(Individual in, WorldInfo world) {
			// TODO Auto-generated method stub
			ArrayList<Seat> emptySeatAround = world.getEmptySeatAround(in
					.getSeat());
			if (!emptySeatAround.isEmpty()) {
				Seat emptySeat = emptySeatAround.get((int) (RandomUtil
						.nextFloat() * emptySeatAround.size()));
				float dl = world.getSeatDefectionLevel(in.getSeat());
				double imigratePosibility = dl / 8;
				if (RandomUtil.nextDouble() < imigratePosibility) {
					in.moveTo(emptySeat);
				}
			}
		}

	};

	public String name;

	private MigrationPattern(String s) {
		name = s;
	}

	public void migrate(Individual in, WorldInfo world) {
		// do nothing
	}

	@Override
	public String toString() {
		return name;
	}
}
