package utils;

public class Vector {

	public int x, y;
	public Vector(int i, int j){
		x= i;
		y = j;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
