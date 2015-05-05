package entity;

import java.util.ArrayList;

public interface WorldInfo {


	/** 返回二维网格的宽度 */
	public int getLength();

	public float getSeatDefectionLevel(Seat s);

	public float getSeatCooperationLevel(Seat s);

	// ArrayList<Seat> seatAround = new ArrayList<>();

	/** 找到该座位周围所有在直接邻居距离范围内的所有座位 */
	public ArrayList<Seat> getAllSeatAround(Seat s);

	// ArrayList<Seat> emptySeatAround = new ArrayList<>();

	/** 找到该座位周围所有在直接邻居距离范围内的“空”座位 */
	public ArrayList<Seat> getEmptySeatAround(Seat s);

	/** 找到该座位周围所有在直接邻居距离范围内的被占据的座位 */
	public ArrayList<Seat> getOccupiedSeatAround(Seat s);

	public float getGlobalCooperationLevel();

	public WorldDetail getWorldDetail();

	public float getAverageNeighbourNum();

	/** 返回个体总数 */
	public int getPopulationNum();

	public String getIndividualStrategyPicture();

}
