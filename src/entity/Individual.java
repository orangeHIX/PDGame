package entity;

import org.json.JSONArray;
import org.json.JSONObject;
import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;

import java.util.ArrayList;

/**
 * 二维网格博弈中的个体或博弈参与者
 */
public class Individual implements JsonEntity {
    /**
     * 交互强度变量最大值
     */
    public static final float MAX_W = 1.0f;
    /**
     * 交互强度变量最小值/
     */
    public static final float MIN_W = 0f;

    public int id;

    /**
     * 个体采取的策略，取值[0.0,1.0]，0.0表示背叛(D)，1.0表示合作(C)，中间值表示中间策略
     */
    private float strategy;
    /**
     * 本轮策略演化，学习新策略时的学习对象。没有则为null
     */
    private int teacherId;
    /**
     * 个体下一轮博弈要采取的策略,进行策略更新应所有个体一起执行
     */
    private float nextStrategy;
    /**
     * 累计收益，该个体与每个直接邻居博弈收益的总和
     */
    private float accumulatedPayoff;

    private ArrayList<Integer> gambleIndivIds;
    /**
     * 上一轮的累计收益
     */
    private float prePayoff;
    /**
     * 个体所处的网格位置
     */
    private Seat seat;
    /**
     * 个体每轮博弈的直接邻居表
     */
    private ArrayList<Individual> neighbours;
    /**
     * 交互强度变量ω（0 ≤ ω ≤1） 来衡量个体与其邻居的交互强度。ω越大，个体与其他个体的交互的概率越大，
     * ω表现了个体参与博弈的积极程度。两个相邻个体 （用i或j表示）间的交互以（ωi+ωj）/2的概率交互。
     */
    private float w;

    /**
     * 此构造方法邻居表neighbours没有正确还原!!!
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
     * 获得个体交互强度
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
     * 累加收益，累计收益 = 累计收益 + payoff
     */
    public void accumulatePayoff(float payoff) {
        accumulatedPayoff += payoff;
    }

    /**
     * 保存累计受益历史值，重置累计收益，置零;
     */
    public void resetAccumulatedPayoff() {
        prePayoff = accumulatedPayoff;
        accumulatedPayoff = 0;
    }

    /**
     * 获取累计收益
     */
    public float getAccumulatedPayoff() {
        return accumulatedPayoff;
    }

    /**
     * 向个体直接邻居表中加入一个个体
     */
    public boolean addNeighbour(Individual neighbour) {
        return this.neighbours.add(neighbour);
    }

    /**
     * 检查直接邻居表中是否有这个邻居
     */
    public boolean hasNeighbour(Individual neighbour) {
        return this.neighbours.contains(neighbour);
    }

    /**
     * 清空上轮直接邻居表和与之博弈个体id列表
     */
    public void clearLastTurnInfo() {
        this.neighbours.clear();
        this.gambleIndivIds.clear();
    }

    /**
     * 尝试从邻居那里按照不同的方式学习策略<br>
     *
     * @param learningPattern 学习邻居策略的方式，LEARNING_PATTERN_MAXPAYOFF表示学习具有最大收益的邻居，
     *                        LEARNING_PATTERN_FERMI表示按照fermi策略学习邻居
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
     * 个体尝试迁徙到周围的空位，
     *
     * @param imigratePattern 迁徙模式 ，可以是无迁徙NONE、随机迁徙 RANDOM、机会迁徙OPTIMISTIC
     */
    public void imigrate(World world, MigrationPattern imigratePattern) {
        moveTo(imigratePattern.migrate(this, world));
    }

    /**
     * 更新个体采取的策略
     *
     * @return true个体改变了策略，false个体保持原来的策略
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
     * 获取邻居个体当前邻居列表
     */
    public final ArrayList<Individual> getNeighbours() {
        return neighbours;
    }

    /**
     * 交互强度更新：每个个体i能自适应地调整它的交互强度：初始态所有个体的ω = 1，如果个体的收益相比于上个时间步增加即pi(t) >
     * pj(t-1)，它的交互强度将从ωi增加到ωi +α，收益减少即pi(t) < pj(t-1)，交互强度将从ωi减少到ωi
     * -β；收益不变，个体会维持其交互强度ωi。ω→0，个体间的交互趋于“冻结”。为了避免出现冻结现象
     * ，我们设ω的下限值ωmin=0.1，上限值ωmax=1.0
     * ，即所有个体的ωi被约束在[0.1,1]。如果自适应地调整使得ω越界，ω的值将会被校订：ωi
     * =min（ωi+α，ωmax），ωi=max（ωi-β，ωmin
     * ）。这样，α和β的有效域将被约束在[0.1,0.9]。这种个体间的交互强度的演化规则被称为
     * “收益愈多→强度俞强，收益俞少→强度俞弱”，而每一组（α，β）表征了个体交互强度改变的程度。α（β）值越大，个体的交互强度变化幅度便越大。
     *
     * @param a 交互强度的增量
     * @param b 交互强度的减少量
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
     * 个体移动到新的位置。如果位置不为空，个体不移动
     *
     * @param emptySeat 要移动到的空位置
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
