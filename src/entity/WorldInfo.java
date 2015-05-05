package entity;

import java.util.ArrayList;

public interface WorldInfo {


	/** ���ض�ά����Ŀ�� */
	public int getLength();

	public float getSeatDefectionLevel(Seat s);

	public float getSeatCooperationLevel(Seat s);

	// ArrayList<Seat> seatAround = new ArrayList<>();

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ�������λ */
	public ArrayList<Seat> getAllSeatAround(Seat s);

	// ArrayList<Seat> emptySeatAround = new ArrayList<>();

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵġ��ա���λ */
	public ArrayList<Seat> getEmptySeatAround(Seat s);

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵı�ռ�ݵ���λ */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s);

	public float getGlobalCooperationLevel();

	public WorldDetail getWorldDetail();

	public float getAverageNeighbourNum();

	/** ���ظ������� */
	public int getPopulationNum();

	public String getIndividualStrategyPicture();

}
