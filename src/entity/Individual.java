package entity;

import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;

import java.util.ArrayList;

/**
 * 二维网格博弈中的个体或博弈参与者
 */
public class Individual {
    /**
     * 交互强度变量最大值
     */
    public static final float MAX_W = 1.0f;
    /**
     * 交互强度变量最小值/
     */
    public static final float MIN_W = 0f;

    /**
     * 个体采取的策略，取值[0.0,1.0]，0.0表示背叛(D)，1.0表示合作(C)，中间值表示中间策略
     */
    private float strategy;
    /**
     * 个体下一轮博弈要采取的策略,进行策略更新应所有个体一起执行
     */
    private float nextStrategy;
    /**
     * 累计收益，该个体与每个直接邻居博弈收益的总和
     */
    private float accumulatedPayoff;
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

    public Individual(float strategy, int maxNeighbourNum, float w) {
        super();
        if (strategy >= 0.0f && strategy <= 1.0f) {
            this.strategy = strategy;
            this.nextStrategy = this.strategy;
        } else {
            this.strategy = GamblingRule.STRATEGY_D;
        }
        accumulatedPayoff = 0;
        prePayoff = 0;
        seat = null;
        neighbours = new ArrayList<>(maxNeighbourNum);
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
     * 清空直接邻居表
     */
    public void clearAllNeighbour() {
        this.neighbours.clear();
    }

    /**
     * 尝试从邻居那里按照不同的方式学习策略<br>
     *
     * @param learningPattern 学习邻居策略的方式，LEARNING_PATTERN_MAXPAYOFF表示学习具有最大收益的邻居，
     *                        LEARNING_PATTERN_FERMI表示按照fermi策略学习邻居
     */
    public void learnFromNeighbours(LearningPattern learningPattern) {
        nextStrategy = learningPattern.learn(this);
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

    @Override
    public String toString() {
        return "Individual [s=" + strategy + ", ns=" + nextStrategy
                + ", payoff=" + accumulatedPayoff + ", w=" + w + ",("
                + seat.seat_i + "," + seat.seat_j + ") ] ";
    }

}
