package entity;

import java.util.ArrayList;

public interface WorldInfo {

	/** ���ض�ά����Ŀ�� */
	public int getLength();

	/** ���ظ�λ����Χ����ˮƽ */
	public float getSeatDefectionLevel(Seat s);

	/** ���ظ�λ����Χ����ˮƽ */
	public float getSeatCooperationLevel(Seat s);

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ�������λ */
	public ArrayList<Seat> getAllSeatAround(Seat s);

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵġ��ա���λ */
	public ArrayList<Seat> getEmptySeatAround(Seat s);

	/** �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵı�ռ�ݵ���λ */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s);

	/** ����ģ����ƽ�������� */
	public float getGlobalCooperationLevel();

	/** ����ģ����ƽ�������ʺͲ�ȡ���ֲ��Ը�����ռ�ı��� */
	public WorldDetail getWorldDetail();

	/** ���ظ���ƽ���ھ���Ŀ */
	public float getAverageNeighbourNum();

	/** ���ظ������� */
	public int getPopulationNum();

	/** ���ַ�������ʽ���ص�ǰģ���˿ڷֲ�ͼ */
	public String getIndividualStrategyPicture();

}
