package entity;

import org.json.JSONObject;

/**
 * 网格中每一个格子，可以容纳一个个体
 */
public class Seat implements JsonEntity{
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

    public Seat(int i, int j) {
        seat_i = i;
        seat_j = j;
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
    public JSONObject getJSONObject(){
        return new JSONObject().put("x", seat_i).put("y",seat_j);
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

    public static void main(String[] args){
        Seat s = new Seat(1,2);
        JSONObject jo = s.getJSONObject();
        System.out.println(jo);
        s.initFromJSONObject(jo);
        System.out.println(s);
        s.initFromJSONSource(jo.toString());
        System.out.println(s);
    }
}