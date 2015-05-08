package entity;

import java.util.ArrayList;

public interface WorldInfo {

	/** 返回二维网格的宽度 */
	public int getLength();

	/** 返回该位置周围背叛水平 */
	public float getSeatDefectionLevel(Seat s);

	/** 返回该位置周围合作水平 */
	public float getSeatCooperationLevel(Seat s);

	/** 找到该座位周围所有在直接邻居距离范围内的所有座位 */
	public ArrayList<Seat> getAllSeatAround(Seat s);

	/** 找到该座位周围所有在直接邻居距离范围内的“空”座位 */
	public ArrayList<Seat> getEmptySeatAround(Seat s);

	/** 找到该座位周围所有在直接邻居距离范围内的被占据的座位 */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s);

	/** 返回模型中平均合作率 */
	public float getGlobalCooperationLevel();

	/** 返回模型中平均合作率和采取各种策略个体所占的比例 */
	public WorldDetail getWorldDetail();

	/** 返回个体平均邻居数目 */
	public float getAverageNeighbourNum();

	/** 返回个体总数 */
	public int getPopulationNum();

	/** 以字符串的形式返回当前模型人口分布图 */
	public String getIndividualStrategyPicture();

}
