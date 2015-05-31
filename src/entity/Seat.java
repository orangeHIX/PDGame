package entity;

import org.json.JSONObject;

/**
 * ������ÿһ�����ӣ���������һ������
 */
public class Seat implements JsonEntity{
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

    public Seat(int i, int j) {
        seat_i = i;
        seat_j = j;
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