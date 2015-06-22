package rule;

import entity.Individual;
import utils.RandomUtil;

import java.util.ArrayList;

public enum LearningPattern {
    /**
     * MAXPAYOFF����ֱ���ھ�ѧϰ���ԣ�����ѧϰ��������ۻ�������ھ�
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
     * FERMI����ֱ���ھ�ѧϰ���ԣ�����i���ѡ��һ���ھ�j�󣬰�����ʽ�����Ƿ�ѧϰ���ھӣ�
     * ����Pi��i�ı������棬Pj���ھ�j�ı������棬iѧϰ����j�ĸ���Ϊw=1/(1+exp[(pi-pj)/k]),
     * k�����˻������������أ��̻��˸���ķ����Գ̶ȡ���ʱk������0ʱ����ζ��i������ȫ���ԡ�����ֻ��ѧϰ���������������Ϊ��
     * ������k�����ӣ��������Գ̶Ƚ��ͣ�ѧϰ�������ھ���Ϊ�Ŀ���������
     */
    FERMI{//"fermi_learning") {

        float noise = 0.1f; // ������ʱ��Ϊ0.1

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
        float noise = 0.1f; // ������ʱ��Ϊ0.1

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
     * ���尴�ո�ʵ����ָ��ѧϰģʽ����ѧϰ�²���
     *
     * @param in Ҫ����ѧϰ�ĸ���
     * @return ѧϰ�����²���
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
     * ���尴�ո�ʵ����ָ��ѧϰģʽ����ѧϰ�²���
     *
     * @param in Ҫ����ѧϰ�ĸ���
     * @return ѧϰ����
     */
    abstract public Individual getTeacher(Individual in);

//    @Override
//    public String toString() {
//        return name;
//    }

}
