package utils;

/**
 * ��ά����
 */
public class Vector2 {

    public int x, y;

    public Vector2(int i, int j) {
        x = i;
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
        Vector2 other = (Vector2) obj;
        if (x != other.x)
            return false;
        return y == other.y;
    }
}
