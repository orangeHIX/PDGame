package entity;

/** 网格中每一个格子，可以容纳一个个体 */
public class Seat {
	/** 座位上的个体，没有个体时为null */
	Individual owner;
	/** 座位在网格中所处的位置 行号i */
	public final int seat_i;
	/** 座位在网格中所处的位置 列号j */
	public final int seat_j;

	public Seat(int i, int j) {
		seat_i = i;
		seat_j = j;
	}

	public Individual getOwner() {
		return owner;
	}

	public void setOwner(Individual in) {
		this.owner = in;
		if (in != null)
			in.setSeat(this);
	}

	/** 返回该座位是否是空的 */
	public boolean isEmpty() {
		return owner == null;
	}

	@Override
	public String toString() {
		return "(" + seat_i + "," + seat_j + ")\t";
	}
}