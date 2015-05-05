package entity;

/** ������ÿһ�����ӣ���������һ������ */
public class Seat {
	/** ��λ�ϵĸ��壬û�и���ʱΪnull */
	Individual owner;
	/** ��λ��������������λ�� �к�i */
	public final int seat_i;
	/** ��λ��������������λ�� �к�j */
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

	/** ���ظ���λ�Ƿ��ǿյ� */
	public boolean isEmpty() {
		return owner == null;
	}

	@Override
	public String toString() {
		return "(" + seat_i + "," + seat_j + ")\t";
	}
}