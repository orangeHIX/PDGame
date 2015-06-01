package entity;

import org.json.JSONArray;
import org.json.JSONObject;
import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;

import java.util.ArrayList;

/**
 * ��ά�������еĸ�����Ĳ�����
 */
public class Individual implements JsonEntity {
    /**
     * ����ǿ�ȱ������ֵ
     */
    public static final float MAX_W = 1.0f;
    /**
     * ����ǿ�ȱ�����Сֵ/
     */
    public static final float MIN_W = 0f;

    public int id;

    /**
     * �����ȡ�Ĳ��ԣ�ȡֵ[0.0,1.0]��0.0��ʾ����(D)��1.0��ʾ����(C)���м�ֵ��ʾ�м����
     */
    private float strategy;
    /**
     * ���ֲ����ݻ���ѧϰ�²���ʱ��ѧϰ����û����Ϊnull
     */
    private int teacherId;
    /**
     * ������һ�ֲ���Ҫ��ȡ�Ĳ���,���в��Ը���Ӧ���и���һ��ִ��
     */
    private float nextStrategy;
    /**
     * �ۼ����棬�ø�����ÿ��ֱ���ھӲ���������ܺ�
     */
    private float accumulatedPayoff;

    private ArrayList<Integer> gambleIndivIds;
    /**
     * ��һ�ֵ��ۼ�����
     */
    private float prePayoff;
    /**
     * ��������������λ��
     */
    private Seat seat;
    /**
     * ����ÿ�ֲ��ĵ�ֱ���ھӱ�
     */
    private ArrayList<Individual> neighbours;
    /**
     * ����ǿ�ȱ����أ�0 �� �� ��1�� ���������������ھӵĽ���ǿ�ȡ���Խ�󣬸�������������Ľ����ĸ���Խ��
     * �ر����˸�����벩�ĵĻ����̶ȡ��������ڸ��� ����i��j��ʾ����Ľ����ԣ���i+��j��/2�ĸ��ʽ�����
     */
    private float w;

    /**
     * �˹��췽���ھӱ�neighboursû����ȷ��ԭ!!!
     */
    public Individual(String jsonSouce) {
        initFromJSONSource(jsonSouce);
    }

    public Individual(int id, float strategy, int maxNeighbourNum, float w) {
        super();
        this.id = id;
        if (strategy >= 0.0f && strategy <= 1.0f) {
            this.strategy = strategy;
            this.nextStrategy = this.strategy;
        } else {
            this.strategy = GamblingRule.STRATEGY_D;
        }
        teacherId = -1;
        accumulatedPayoff = 0;
        prePayoff = 0;
        seat = null;
        neighbours = new ArrayList<>(maxNeighbourNum);
        gambleIndivIds = new ArrayList<>(maxNeighbourNum);
        this.w = w;

    }

    /**
     * ��ø��彻��ǿ��
     */
    public float getW() {
        return w;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public float getStrategy() {
        return strategy;
    }

    /**
     * �ۼ����棬�ۼ����� = �ۼ����� + payoff
     */
    public void accumulatePayoff(float payoff) {
        accumulatedPayoff += payoff;
    }

    /**
     * �����ۼ�������ʷֵ�������ۼ����棬����;
     */
    public void resetAccumulatedPayoff() {
        prePayoff = accumulatedPayoff;
        accumulatedPayoff = 0;
    }

    /**
     * ��ȡ�ۼ�����
     */
    public float getAccumulatedPayoff() {
        return accumulatedPayoff;
    }

    /**
     * �����ֱ���ھӱ��м���һ������
     */
    public boolean addNeighbour(Individual neighbour) {
        return this.neighbours.add(neighbour);
    }

    /**
     * ���ֱ���ھӱ����Ƿ�������ھ�
     */
    public boolean hasNeighbour(Individual neighbour) {
        return this.neighbours.contains(neighbour);
    }

    /**
     * �������ֱ���ھӱ����֮���ĸ���id�б�
     */
    public void clearLastTurnInfo() {
        this.neighbours.clear();
        this.gambleIndivIds.clear();
    }

    /**
     * ���Դ��ھ����ﰴ�ղ�ͬ�ķ�ʽѧϰ����<br>
     *
     * @param learningPattern ѧϰ�ھӲ��Եķ�ʽ��LEARNING_PATTERN_MAXPAYOFF��ʾѧϰ�������������ھӣ�
     *                        LEARNING_PATTERN_FERMI��ʾ����fermi����ѧϰ�ھ�
     */
    public void learnFromNeighbours(LearningPattern learningPattern) {
        Individual teacher = learningPattern.getTeacher(this);
        if (teacher == null) {
            nextStrategy = strategy;
            teacherId = -1;
        } else {
            nextStrategy = teacher.getStrategy();
            teacherId = teacher.id;
        }
    }

    /**
     * ���峢��Ǩ�㵽��Χ�Ŀ�λ��
     *
     * @param imigratePattern Ǩ��ģʽ ����������Ǩ��NONE�����Ǩ�� RANDOM������Ǩ��OPTIMISTIC
     */
    public void imigrate(World world, MigrationPattern imigratePattern) {
        moveTo(imigratePattern.migrate(this, world));
    }

    /**
     * ���¸����ȡ�Ĳ���
     *
     * @return true����ı��˲��ԣ�false���屣��ԭ���Ĳ���
     */
    public boolean updateStrategy() {
        if (strategy != nextStrategy) {
            strategy = nextStrategy;
            return true;
        } else {
            return false;
        }
    }

    /**
     * ��ȡ�ھӸ��嵱ǰ�ھ��б�
     */
    public final ArrayList<Individual> getNeighbours() {
        return neighbours;
    }

    /**
     * ����ǿ�ȸ��£�ÿ������i������Ӧ�ص������Ľ���ǿ�ȣ���ʼ̬���и���Ħ� = 1��������������������ϸ�ʱ�䲽���Ӽ�pi(t) >
     * pj(t-1)�����Ľ���ǿ�Ƚ��Ӧ�i���ӵ���i +����������ټ�pi(t) < pj(t-1)������ǿ�Ƚ��Ӧ�i���ٵ���i
     * -�£����治�䣬�����ά���佻��ǿ�Ȧ�i���ء�0�������Ľ������ڡ����ᡱ��Ϊ�˱�����ֶ�������
     * ��������ص�����ֵ��min=0.1������ֵ��max=1.0
     * �������и���Ħ�i��Լ����[0.1,1]���������Ӧ�ص���ʹ�æ�Խ�磬�ص�ֵ���ᱻУ������i
     * =min����i+������max������i=max����i-�£���min
     * �������������ͦµ���Ч�򽫱�Լ����[0.1,0.9]�����ָ����Ľ���ǿ�ȵ��ݻ����򱻳�Ϊ
     * �����������ǿ����ǿ���������١�ǿ������������ÿһ�飨�����£������˸��彻��ǿ�ȸı�ĳ̶ȡ������£�ֵԽ�󣬸���Ľ���ǿ�ȱ仯���ȱ�Խ��
     *
     * @param a ����ǿ�ȵ�����
     * @param b ����ǿ�ȵļ�����
     */
    public void updateInteractionIntensity(float a, float b) {
        if (accumulatedPayoff > prePayoff) {
            w += a;
        } else if (accumulatedPayoff < prePayoff) {
            w -= b;
        }
        if (w > MAX_W) {
            w = MAX_W;
        } else if (w < MIN_W) {
            w = MIN_W;
        }
    }

    /**
     * �����ƶ����µ�λ�á����λ�ò�Ϊ�գ����岻�ƶ�
     *
     * @param emptySeat Ҫ�ƶ����Ŀ�λ��
     */
    public boolean moveTo(Seat emptySeat) {
        if (emptySeat.isEmpty()) {
            seat.setOwner(null);
            emptySeat.setOwner(this);
            return true;
        }
        return false;
    }

    public void gamble(Individual other, GamblingRule gr) {
        accumulatePayoff(gr.getPayOff(this.getStrategy(), other.getStrategy()));
        gambleIndivIds.add(other.id);
    }


    @Override
    public String toString() {
        return "Individual [s=" + strategy + ", ns=" + nextStrategy
                + ", payoff=" + accumulatedPayoff + ", w=" + w + ",("
                + seat.seat_i + "," + seat.seat_j + ") ] ";// + gambleIndivIds;
    }


    @Override
    public JSONObject getJSONObject() {
        //gambleIndivIds.add(99);
        return new JSONObject().put("id", id).put("stra", strategy)
                .put("teaId", teacherId)
                //.put("nextStra", nextStrategy)
                .put("gamIds", new JSONArray(gambleIndivIds.toArray()))
                .put("payoff", accumulatedPayoff)
                .put("seat", seat.getJSONObject())
                .put("w", w);
    }

    @Override
    public void initFromJSONSource(String source) {
        initFromJSONObject(new JSONObject(source));
    }

    @Override
    public void initFromJSONObject(JSONObject jsonObject) {

        neighbours = new ArrayList<>();
        gambleIndivIds = new ArrayList<>();

        id = jsonObject.getInt("id");
        strategy = new Float(jsonObject.getDouble("stra"));
        teacherId = jsonObject.getInt("teaId");
        //nextStrategy = new Float(jsonObject.getDouble("nextStra"));
        gambleIndivIds.clear();
        JSONArray ja = jsonObject.getJSONArray("gamIds");
        int len = ja.length();
        for (int i = 0; i < len; i++) {
            gambleIndivIds.add(ja.getInt(i));
        }
        accumulatedPayoff = new Float(jsonObject.getDouble("payoff"));
        seat = new Seat(0, 0);
        seat.initFromJSONObject(jsonObject.getJSONObject("seat"));
        seat.setOwner(this);
        w = new Float(jsonObject.getDouble("w"));
    }

    public static void main(String[] args) {
        Individual in = new Individual(1, 0.5f, 8, 1);
        in.setSeat(new Seat(8, 9));
        JSONObject jo = in.getJSONObject();
        System.out.println(jo);

        Individual in2 = new Individual(3, 0.2f, 4, 0.5f);
        in2.setSeat(new Seat(10, 10));
        System.out.println(in2);
        in2.initFromJSONObject(jo);
        System.out.println(in2);
    }
}
