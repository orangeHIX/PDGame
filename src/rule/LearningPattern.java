package rule;

import entity.Individual;
import utils.RandomUtil;

import java.util.ArrayList;

public enum LearningPattern {
    /**
     * MAXPAYOFF：从直接邻居学习策略，个体学习具有最大累积收益的邻居
     */
    MAXPAYOFF{//"max_payoff_learning") {
        @Override
        public Individual getTeacher(Individual in) {
            Individual maxPayoffNeighbour = null;
            final ArrayList<Individual> neighbours = in.getNeighbours();
            if (!neighbours.isEmpty()) {
                maxPayoffNeighbour = neighbours.get(0);
                for (Individual nei : neighbours) {
                    if (nei.getAccumulatedPayoff() > maxPayoffNeighbour
                            .getAccumulatedPayoff())
                        maxPayoffNeighbour = nei;
                }
            }
            if (maxPayoffNeighbour != null
                    && maxPayoffNeighbour.getAccumulatedPayoff() > in.getAccumulatedPayoff())
                return maxPayoffNeighbour;
            else {
                return null;
            }
        }
    },
    /**
     * FERMI：从直接邻居学习策略，个体i随机选择一个邻居j后，按照下式决定是否学习该邻居，
     * 其中Pi是i的本轮收益，Pj是邻居j的本轮收益，i学习后者j的概率为w=1/(1+exp[(pi-pj)/k]),
     * k描述了环境的噪声因素，刻画了个体的非理性程度。当时k趋近于0时，意味着i具有完全理性——它只会学习高于自身收益的行为。
     * 而随着k的增加，个体理性程度降低，学习低收益邻居行为的可能性增加
     */
    FERMI{//"fermi_learning") {

        float noise = 0.1f; // 噪声暂时设为0.1

        @Override
        public Individual getTeacher(Individual in) {
            // TODO Auto-generated method stub
            double imitatePosibility = 0;
            final ArrayList<Individual> neighbours = in.getNeighbours();
            if (!neighbours.isEmpty()) {
                Individual neighbour = neighbours.get((int) (RandomUtil
                        .nextFloat() * neighbours.size()));
                imitatePosibility = 1 / (1 + Math.exp((in
                        .getAccumulatedPayoff() - neighbour
                        .getAccumulatedPayoff())
                        / noise));
                if (RandomUtil.nextDouble() <= imitatePosibility) {
                    return neighbour;
                    // System.out.println(""+this+" learn from \n\t"+neighbour+" with "
                    // +imitatePosibility);
                }
            }
            return null;
        }
    },
    INTERACTIVE_FERMI{//"interactive_fermi") {
        float noise = 0.1f; // 噪声暂时设为0.1

        @Override
        public Individual getTeacher(Individual in) {
            // TODO Auto-generated method stub
            double imitatePosibility = 0;
            final ArrayList<Individual> neighbours = in.getNeighbours();
            if (!neighbours.isEmpty()) {
                Individual neighbour = null; // neighbour to learn from

                float neighboursPayoff = 0.0f;
                for (Individual nei : neighbours) {
                    neighboursPayoff += nei.getAccumulatedPayoff();
                }
                // there are instances where neighboursPayoff is equal to zero.
                // Where this occurs, player x will
                // randomly select one player y from its adjacent neighbors.
                if (neighboursPayoff <= 1.e-6) {
                    neighbour = neighbours
                            .get((int) (RandomUtil.nextFloat() * neighbours
                                    .size()));
                } else {
                    float chooseRan = neighboursPayoff * RandomUtil.nextFloat();
                    float preA = 0;
                    float A = 0;
                    for (Individual nei : neighbours) {
                        A += nei.getAccumulatedPayoff();
                        if (preA <= chooseRan && chooseRan <= A) {
                            neighbour = nei;
                            break;
                        }
                        preA = A;
                    }
                }

                imitatePosibility = 1 / (1 + Math.exp((in
                        .getAccumulatedPayoff() - neighbour
                        .getAccumulatedPayoff())
                        / noise));
                if (RandomUtil.nextDouble() <= imitatePosibility) {
                    return neighbour;
                    // System.out.println(""+this+" learn from \n\t"+neighbour+" with "
                    // +imitatePosibility);
                }
            }
            return null;
        }
    };

//    public String name;
//
//    private LearningPattern(String s) {
//        name = s;
//    }

    /**
     * 个体按照该实例所指的学习模式尝试学习新策略
     *
     * @param in 要进行学习的个体
     * @return 学习到的新策略
     */
    public float learn(Individual in) {
        // do nothing
        Individual teacher = getTeacher(in);
        if (teacher == null)
            return in.getStrategy();
        else
            return teacher.getStrategy();
    }


    /**
     * 个体按照该实例所指的学习模式尝试学习新策略
     *
     * @param in 要进行学习的个体
     * @return 学习对象
     */
    abstract public Individual getTeacher(Individual in);

//    @Override
//    public String toString() {
//        return name;
//    }

}
