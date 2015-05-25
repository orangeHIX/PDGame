package entity;

import utils.WorldDetail;

import java.util.ArrayList;

public interface WorldInfo {

    /**
     * 返回二维网格的宽度
     */
    int getLength();

    /**
     * 返回该位置周围背叛水平
     */
    float getSeatDefectionLevel(Seat s);

    /**
     * 返回该位置周围合作水平
     */
    float getSeatCooperationLevel(Seat s);

    /**
     * 找到该座位周围所有在直接邻居距离范围内的所有座位
     */
    ArrayList<Seat> getAllSeatAround(Seat s);

    /**
     * 找到该座位周围所有在直接邻居距离范围内的“空”座位
     */
    ArrayList<Seat> getEmptySeatAround(Seat s);

    /**
     * 找到该座位周围所有在直接邻居距离范围内的被占据的座位
     */
    ArrayList<Seat> getOccupiedSeatAround(Seat s);

    /**
     * 返回模型中平均合作率
     */
    float getGlobalCooperationLevel();

    /**
     * 返回模型中平均合作率和采取各种策略个体所占的比例
     */
    WorldDetail getWorldDetail();

    /**
     * 返回个体平均邻居数目
     */
    float getAverageNeighbourNum();

    /**
     * 返回个体总数
     */
    int getPopulationNum();

    /**
     * 以字符串的形式返回当前模型人口分布图
     */
    String getIndividualStrategyPicture();

}
