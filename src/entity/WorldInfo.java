package entity;

import utils.WorldDetail;

import java.util.ArrayList;

public interface WorldInfo {

    /**
     * ���ض�ά����Ŀ��
     */
    int getLength();

    /**
     * ���ظ�λ����Χ����ˮƽ
     */
    float getSeatDefectionLevel(Seat s);

    /**
     * ���ظ�λ����Χ����ˮƽ
     */
    float getSeatCooperationLevel(Seat s);

    /**
     * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ�������λ
     */
    ArrayList<Seat> getAllSeatAround(Seat s);

    /**
     * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵġ��ա���λ
     */
    ArrayList<Seat> getEmptySeatAround(Seat s);

    /**
     * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵı�ռ�ݵ���λ
     */
    ArrayList<Seat> getOccupiedSeatAround(Seat s);

    /**
     * ����ģ����ƽ��������
     */
    float getGlobalCooperationLevel();

    /**
     * ����ģ����ƽ�������ʺͲ�ȡ���ֲ��Ը�����ռ�ı���
     */
    WorldDetail getWorldDetail();

    /**
     * ���ظ���ƽ���ھ���Ŀ
     */
    float getAverageNeighbourNum();

    /**
     * ���ظ�������
     */
    int getPopulationNum();

    /**
     * ���ַ�������ʽ���ص�ǰģ���˿ڷֲ�ͼ
     */
    String getIndividualStrategyPicture();

}
