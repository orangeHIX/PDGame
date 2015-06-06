package entity;

import org.json.JSONObject;
import rule.NeighbourCoverage;
import utils.Predicate;
import utils.Vector2;

import java.util.ArrayList;

/**
 * 网格中每一个格子，可以容纳一个个体
 */
public class Seat implements JsonEntity {
    /**
     * 座位在网格中所处的位置 行号i
     */
    public int seat_i;
    /**
     * 座位在网格中所处的位置 列号j
     */
    public int seat_j;
    /**
     * 座位上的个体，没有个体时为null
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
     * 找到该座位周围所有在直接邻居距离范围内的座位
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
     * 获取处于该位置的个体，没有返回null
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
     * 返回该座位是否是空的
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