package entity;

import org.json.JSONObject;
import rule.NeighbourCoverage;
import utils.Predicate;
import utils.Vector2;

import java.util.ArrayList;

/**
 * ������ÿһ�����ӣ���������һ������
 */
public class Seat implements JsonEntity {
    /**
     * ��λ��������������λ�� �к�i
     */
    public int seat_i;
    /**
     * ��λ��������������λ�� �к�j
     */
    public int seat_j;
    /**
     * ��λ�ϵĸ��壬û�и���ʱΪnull
     */
    Individual owner;

    ArrayList<Seat> seatAroundList;

    public Seat(int i, int j) {
        seat_i = i;
        seat_j = j;
    }

    public void setSeatAroundList(Seat[][] grid, NeighbourCoverage neighbourCoverage) {
        int L = grid.length;
        ArrayList<Vector2> vector2s = neighbourCoverage.getCoverageVector();
        seatAroundList = new ArrayList<>(vector2s.size());
        int x, y;
        for (Vector2 v : vector2s) {
            x = seat_i + v.x;
            y = seat_j + v.y;
            if (x >= 0 && x < L && y >= 0 && y < L) {
                // System.out.println("seat("+i+","+j+"): "+x+","+y);
                seatAroundList.add(grid[x][y]);
            }
        }
    }

    /**
     * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ���λ
     */
    public ArrayList<Seat> getSeatAround(Predicate<Seat> test) {
        // seatAround.clear();
        ArrayList<Seat> seatAround = new ArrayList<>(seatAroundList.size());
        int i, j;
        i = seat_i;
        j = seat_j;
        int x, y;
        for (Seat s : seatAroundList) {
            if (test.test(s)) {
                seatAround.add(s);
            }
        }
        return seatAround;
    }

    /**
     * ��ȡ���ڸ�λ�õĸ��壬û�з���null
     */
    public Individual getOwner() {
        return owner;
    }

    public void setOwner(Individual in) {
        this.owner = in;
        if (in != null)
            in.setSeat(this);
    }

    /**
     * ���ظ���λ�Ƿ��ǿյ�
     */
    public boolean isEmpty() {
        return owner == null;
    }

    @Override
    public String toString() {
        return "(" + seat_i + "," + seat_j + ")\t";
    }

    @Override
    public JSONObject getJSONObject() {
        return new JSONObject().put("x", seat_i).put("y", seat_j);
    }

    @Override
    public void initFromJSONSource(String source) {
        JSONObject jo = new JSONObject(source);
        initFromJSONObject(jo);
    }

    @Override
    public void initFromJSONObject(JSONObject jsonObject) {
        seat_i = jsonObject.getInt("x");
        seat_j = jsonObject.getInt("y");
    }

    public static void main(String[] args) {
        int L = 5;
        Seat[][] grid;
        NeighbourCoverage neighbourCoverage = NeighbourCoverage.Von;
        grid = new Seat[L][];
        for (int i = 0; i < L; i++) {
            grid[i] = new Seat[L];
            for (int j = 0; j < L; j++) {
                grid[i][j] = new Seat(i, j);
            }
        }

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                grid[i][j].setSeatAroundList(grid, neighbourCoverage);
                System.out.println("seat " + grid[i][j].seat_i + "," + grid[i][j].seat_j + ": " + grid[i][j].getSeatAround(new Predicate<Seat>() {
                    @Override
                    public boolean test(Seat object) {
                        return true;
                    }
                }));
            }
        }
        System.out.println();
    }
}