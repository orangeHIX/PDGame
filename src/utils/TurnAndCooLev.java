package utils;

public class TurnAndCooLev {

	public int turn;
	public float cooLev;

	public TurnAndCooLev(int turn, float cooLev) {
		super();
		this.turn = turn;
		this.cooLev = cooLev;
	}

	@Override
	public String toString() {
		return "TurnAndCooLev [turn=" + turn + ", cooLev=" + cooLev + "]";
	}

}
