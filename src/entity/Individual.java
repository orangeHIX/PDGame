package entity;

import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;

import java.util.ArrayList;

/**
 * ��ά�������еĸ�����Ĳ�����
 */
public class Individual {
    /**
     * ����ǿ�ȱ������ֵ
     */
    public static final float MAX_W = 1.0f;
    /**
     * ����ǿ�ȱ�����Сֵ/
     */
    public static final float MIN_W = 0f;

    /**
     * �����ȡ�Ĳ��ԣ�ȡֵ[0.0,1.0]��0.0��ʾ����(D)��1.0��ʾ����(C)���м�ֵ��ʾ�м����
     */
    private float strategy;
    /**
     * ������һ�ֲ���Ҫ��ȡ�Ĳ���,���в��Ը���Ӧ���и���һ��ִ��
     */
    private float nextStrategy;
    /**
     * �ۼ����棬�ø�����ÿ��ֱ���ھӲ���������ܺ�
     */
    private float accumulatedPayoff;
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
     * ���ֱ���ھӱ�
     */
    public void clearAllNeighbour() {
        this.neighbours.clear();
    }

    /**
     * ���Դ��ھ����ﰴ�ղ�ͬ�ķ�ʽѧϰ����<br>
     *
     * @param learningPattern ѧϰ�ھӲ��Եķ�ʽ��LEARNING_PATTERN_MAXPAYOFF��ʾѧϰ�������������ھӣ�
     *                        LEARNING_PATTERN_FERMI��ʾ����fermi����ѧϰ�ھ�
     */
    public void learnFromNeighbours(LearningPattern learningPattern) {
        nextStrategy = learningPattern.learn(this);
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

    @Override
    public String toString() {
        return "Individual [s=" + strategy + ", ns=" + nextStrategy
                + ", payoff=" + accumulatedPayoff + ", w=" + w + ",("
                + seat.seat_i + "," + seat.seat_j + ") ] ";
    }

}
