package entity;

import rule.*;
import utils.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ��ά���񣬰���L*L����λ�������ɸ���
 */
public class World implements WorldInfo {

    /**
     * ��¼���и���ı�
     */
    private ArrayList<Individual> IndividualList;
    /**
     * ��ά���������п�λ����ά�����ʾ
     */
    private Seat[][] grid;
    /**
     * ��ά����Ŀ��
     */
    private int L;


    private NeighbourCoverage neighbourCoverage;
    private StrategyPattern strategyPattern;

//    private int[][] strategyGamblingMatrix;
//    private float[][] strategyPayoffMatrix;

    public World(){}


    /**
     * ��ʼ��ģ��
     *
     * @param length            ���񳤶�
     * @param w                 ����ȫ������ĳ�ʼ����ǿ��
     * @param density           �����и�����ܶ�
     * @param strategyPattern   ���и�����õĲ��Ĳ�������
     * @param neighbourCoverage ÿ��������Χֱ���ھӷֲ�ģʽ
     */
    public void init_world(int length, float density, float w,
                           StrategyPattern strategyPattern, NeighbourCoverage neighbourCoverage) {

        if (length > 0 && density >= 0
                && (density < 1.0 || density - 1.0f < 1.0e-5)) {
            L = length;
            // d0 = density;
            grid = new Seat[L][];
            for (int i = 0; i < L; i++) {
                grid[i] = new Seat[L];
                for (int j = 0; j < L; j++) {
                    grid[i][j] = new Seat(i, j);
                }
            }
            // this.neighbourRange = neighbourRange;
            this.neighbourCoverage = neighbourCoverage;
            this.strategyPattern = strategyPattern;
            IndividualList = new ArrayList<>();

            //initMatrix();

            // ��ÿ��������������ʼ����
            initIndividuals(density, w);

            // ��ÿ������������������е�λ��
            randomAllocateSeat();
        }

    }

//    private void initMatrix() {
//        int len = strategyPattern.getStrategyNum();
//        strategyGamblingMatrix = ArrayUtils.getIntMatrix(len);
//        strategyPayoffMatrix = ArrayUtils.getFloatMatrix(len);
//    }
//
//    private void clearMatrix() {
//        int len = strategyPattern.getStrategyNum();
//        ArrayUtils.clearIntMatrix(strategyGamblingMatrix, len, len);
//        ArrayUtils.clearFloatMatrix(strategyPayoffMatrix, len, len);
//    }

    /**
     * ��ʼ��ģ���еĸ���
     *
     * @param d0 �����и�����ܶ�
     * @param w  ����ȫ������ĳ�ʼ����ǿ��
     */
    private void initIndividuals(float d0,
                                 float w) {

        int inId = 1;

        int num = (int) (d0 * L * L); // ��������Ŀ
        // System.out.println("initIndividualStrategy"+num);
        int strategyNum = strategyPattern.getStrategyNum();

        //strategySample = new float[strategyNum];

        int maxNeighbourNum = neighbourCoverage.getCoverageVector().size();
        // ��������������ӳ�ʼ����
        int count = 0;
        for (int i = 0; i < strategyNum; i++) {
            //strategySample[i] = 0 + i / (float) (strategyNum - 1);
            for (int j = 0; j < num / strategyNum; j++) {
                this.IndividualList.add(new Individual(inId++, strategyPattern.getStrategySample(i),//strategySample[i],
                        maxNeighbourNum, w));
                count++;
            }
        }
        // ���ڲ�ȡ���ֲ��Եĸ�����Ŀ��ĳЩ����²�����ȫ����ȣ�ƽ����������µ����ɸ��尴�������ʣ�µ�����
        if (count < num) {
            for (int i = 0; i < strategyNum; i++) {
                this.IndividualList.add(new Individual(inId++, strategyPattern.getStrategySample(i),//strategySample[i],
                        maxNeighbourNum, w));
                count++;
                if (count >= num) {
                    break;
                }
            }
        }
    }

    /**
     * ��ÿ������������������е�λ��
     */
    private void randomAllocateSeat() {
        int i = 0;
        // ��λ�ã�0,0����ʼ�����������������
        for (Iterator<Individual> e = IndividualList.iterator(); e.hasNext(); ) {
            getSeat(i).setOwner(e.next());
            i++;
        }
        // ʵ�ָ������������λ��������һ��ϴ���㷨������λ��ϴ�ơ�
        for (i = 0; i < L * L; i++) {
            swapSeat(getSeat(i),
                    getSeat((int) (RandomUtil.nextFloat() * L * L)));
        }
    }

    /**
     * ��λ�ϵĸ���֮���໥����λ��
     */
    private void swapSeat(Seat seat1, Seat seat2) {
        Individual tmp = seat1.getOwner();
        seat1.setOwner(seat2.getOwner());
        seat2.setOwner(tmp);
    }

    /**
     * �õ�������,���ϵ��µ�loc��λ�õ���λ
     */
    private Seat getSeat(int loc) {
        int i = loc / L;
        int j = loc % L;
        return grid[i][j];
    }

    @Override
    public int getLength() {
        return L;
    }

    /**
     * �õ���i�У���j�еĸ���
     */
    public Individual getIndividual(int i, int j) {
        return grid[i][j].getOwner();
    }

    /**
     * һ��ʱ�䲽�����е����ڸ���֮�䶼Ҫ�����������ģ�ÿ�������ۼ��Լ�������
     *
     * @param gr ���Ĺ��򣬰�������Ӧ���������
     */
    public void gambling(GamblingRule gr) {

        Individual neighbour;
        ArrayList<Seat> seatAround;
        // �������и�����ھ��б���գ����������и����ۼ����棨���㣩
        for (Individual in : IndividualList) {
            in.resetAccumulatedPayoff();
            in.clearLastTurnInfo();
        }


//        float inStra, neiStra;
//        int inStraIndex, neiStraIndex;
//        float inPayoff, neiPayoff;
        for (Individual in : IndividualList) {

            seatAround = getOccupiedSeatAround(in.getSeat());// �õ�������Χ�����б�ռ����λ

            for (Seat s : seatAround) {
                neighbour = s.getOwner();
                if (!in.hasNeighbour(neighbour)) {
                    // ��������Χ�������ھӣ�����еĻ�����ӵ������Լ��������ھӱ��С�
                    // ��������ʹ���ھ�Ǩ�ƺ󣬸��������ҵ����ֳ�ʼ״̬�����ھ�
                    in.addNeighbour(neighbour);
                    neighbour.addNeighbour(in);
                    // �����������彻��ǿ�ȵ�ƽ��ֵ�����Ƿ���в���
                    if (RandomUtil.nextFloat() <= (in.getW() + neighbour.getW()) / 2) {
                        // �������ھӵ��������ĺ��ۼ�����
                        // System.out.println("game : " +in+" and "+neighbour);
//                        inStra = in.getStrategy();
//                        neiStra = neighbour.getStrategy();
//                        inStraIndex = strategyPattern.getStrategyIndex(inStra);
//                        neiStraIndex = strategyPattern.getStrategyIndex(neiStra);
//
//                        strategyGamblingMatrix[inStraIndex][neiStraIndex]++;
//                        strategyGamblingMatrix[neiStraIndex][inStraIndex]++;
//
//                        inPayoff = gr.getPayOff(inStra, neiStra);
//                        neiPayoff = gr.getPayOff(neiStra, inStra);
//
//                        strategyPayoffMatrix[inStraIndex][neiStraIndex] += inPayoff;
//                        strategyPayoffMatrix[neiStraIndex][inStraIndex] += neiPayoff;
//
//                        in.accumulatePayoff(inPayoff);
//                        neighbour.accumulatePayoff(neiPayoff);
                        in.gamble(neighbour, gr);
                        neighbour.gamble(in, gr);
                    }
                }
            }
        }
    }

    /**
     * �ݻ�����pi�ĸ���ѧϰ�ھӵĲ��ԣ���qi�ĸ���Ǩ�㣬ͬʱ���¸���Ľ���ǿ��
     *
     * @param pi                ѧϰ�ھӲ��Եĸ���
     * @param qi                Ǩ��ĸ���
     * @param learningPattern   ѧϰģʽ��������ѧϰ�����ھ�MAXPAYOFF,
     *                          Ҳ������fermiѧϰģʽERMI
     * @param imigrationPattern Ǩ��ģʽ ����������Ǩ��NONE�����Ǩ��RANDOM������Ǩ��OPTIMISTIC
     * @return ��ȡ�²��Եĸ���ռ����ı���
     */
    public float evolute(float pi, float qi, LearningPattern learningPattern,
                         MigrationPattern imigrationPattern) {
        for (Individual in : IndividualList) {
            if (RandomUtil.nextFloat() <= pi) {
                in.learnFromNeighbours(learningPattern);
            }
            if (RandomUtil.nextFloat() <= qi) {
                // �����ڱ��ֽ���Ǩ�㣬ÿ�������ڲ��Ľ׶��Լ��������ھӱ�Ǩ����ھ������ҵ��ø���
                in.imigrate(this, imigrationPattern);
            }
            // getEmptySeatAround(in);
        }
        int change = 0;
        // ���������һ��Ҫ�õĲ���
        for (Individual in : IndividualList) {
            if (in.updateStrategy()) {
                change++;
            }
        }
        // ������½���ǿ��
        for (Individual in : IndividualList) {
            in.updateInteractionIntensity(0, 0);
        }
        return change / (float) IndividualList.size();
    }


    @Override
    public float getSeatDefectionLevel(Seat s) {
        ArrayList<Seat> seatAround = getOccupiedSeatAround(s);
        return seatAround.size() - getSeatCooperationLevel(s);
    }

    @Override
    public float getSeatCooperationLevel(Seat s) {
        ArrayList<Seat> seatAround = getOccupiedSeatAround(s);
        // int n = 0;
        float seatCooperateLevel = 0.f;
        // Individual in;
        for (Seat sa : seatAround) {
            seatCooperateLevel += sa.owner.getStrategy();
            // n++;
        }
        return seatCooperateLevel;
    }

    // ArrayList<Seat> seatAround = new ArrayList<>();

    /**
     * �ҵ�����λ��Χ������ֱ���ھӾ��뷶Χ�ڵ���λ
     */
    public ArrayList<Seat> getSeatAround(Seat s, Condition<Seat> test) {
        // seatAround.clear();
        ArrayList<Seat> seatAround = new ArrayList<Seat>();
        int i, j;
        i = s.seat_i;
        j = s.seat_j;
        int x, y;
        for (Vector2 v : neighbourCoverage.getCoverageVector()) {
            x = i + v.x;
            y = j + v.y;
            if (x >= 0 && x < L && y >= 0 && y < L) {
                // System.out.println("seat("+i+","+j+"): "+x+","+y);
                if (test.test(grid[x][y])) {
                    seatAround.add(grid[x][y]);
                }
            }
        }
        return seatAround;
    }

    @Override
    public ArrayList<Seat> getAllSeatAround(Seat s) {
        return getSeatAround(s, new Condition<Seat>() {

            @Override
            public boolean test(Seat object) {

                return true;
            }

        });
    }

    @Override
    public ArrayList<Seat> getEmptySeatAround(Seat s) {
        return getSeatAround(s, new Condition<Seat>() {

            @Override
            public boolean test(Seat object) {
                return object.isEmpty();
            }

        });
    }

    // ArrayList<Seat> emptySeatAround = new ArrayList<>();

    @Override
    public ArrayList<Seat> getOccupiedSeatAround(Seat s) {
        return getSeatAround(s, new Condition<Seat>() {

            @Override
            public boolean test(Seat object) {
                return !object.isEmpty();
            }

        });
    }

    @Override
    public float getGlobalCooperationLevel() {
        float cp = 0;
        for (Individual e : IndividualList) {
            cp += e.getStrategy();
        }
        return cp / IndividualList.size();
    }

    @Override
    public WorldDetail getWorldDetail() {
        int i;
        float globalCooperationLevel = 0;
        int strategyNum = strategyPattern.getStrategyNum();
        float[] strategyProportion = new float[strategyNum];
        for (i = 0; i < strategyNum; i++) {
            strategyProportion[i] = 0;
        }
        for (Individual e : IndividualList) {
            globalCooperationLevel += e.getStrategy();
            for (i = 0; i < strategyNum; i++) {
                if (Math.abs(e.getStrategy() - strategyPattern.getStrategySample(i)) < 1.0e-4) {
                    strategyProportion[i] += 1.0f;
                    break;
                }
            }
        }
        globalCooperationLevel = globalCooperationLevel / IndividualList.size();
        for (i = 0; i < strategyProportion.length; i++) {
            strategyProportion[i] = strategyProportion[i]
                    / IndividualList.size();
        }
//        float[][] PM = ArrayUtils.copyFloatMatrix(
//                strategyPayoffMatrix, strategyNum, strategyNum);
//        int[][] GM = ArrayUtils.copyIntMatrix(
//                strategyGamblingMatrix, strategyNum, strategyNum);
//        clearMatrix();
        return new WorldDetail(globalCooperationLevel,
                strategyProportion, null, null);// GM, PM);
    }

    @Override
    public float getAverageNeighbourNum() {
        float ann = 0;
        for (Individual e : IndividualList) {
            ann += (getAllSeatAround(e.getSeat()).size() - getEmptySeatAround(
                    e.getSeat()).size());
            // System.out.println("getAverageNeighbourNum "+getAllSeatAround(e.getSeat()).size()
            // +","+ getEmptySeatAround(
            // e.getSeat()).size());
        }
        return ann / IndividualList.size();
    }

    @Override
    public int getPopulationNum() {
        return IndividualList.size();
    }

    @Override
    public String getIndividualStrategyPicture() {
        StringBuilder sb = new StringBuilder();
        Individual in;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                in = grid[i][j].getOwner();
                sb.append(in == null ? null : in.getStrategy());
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public String getIndividualPayoffPicture() {
        StringBuilder sb = new StringBuilder();
        Individual in;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                in = grid[i][j].getOwner();
                sb.append(in == null ? null : in.getAccumulatedPayoff());
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public String getIndividualAllPicture() {
        StringBuilder sb = new StringBuilder();
        Individual in;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                in = grid[i][j].getOwner();
                sb.append(in == null ? null : in.getJSONObject());
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public Snapshot getSnapshot() {
        //return new Snapshot(getIndividualStrategyPicture(), getIndividualPayoffPicture());
        return new Snapshot(getIndividualStrategyPicture(), getIndividualAllPicture());
    }

    public void initFromIndividualAllPicture(String individualAllPicture) {

        String[] ss = individualAllPicture.split("\r\n");

        IndividualList = new ArrayList<>();
        L = ss.length;
        grid = new Seat[ss.length][];

        for (int i = 0; i < ss.length; i++) {
            String[] ss2 = ss[i].split("\t");
            grid[i] = new Seat[ss2.length];
            for (int j = 0; j < ss2.length; j++) {
                Individual in = new Individual(ss2[j]);
                grid[i][j] = in.getSeat();
                IndividualList.add(in);
            }
        }
    }

    public void printIndividualPayoff() {
        StringBuilder sb = new StringBuilder();
        Individual in;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                in = grid[i][j].getOwner();
                sb.append(in == null ? null : in.getAccumulatedPayoff());
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        System.out.println(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                sb.append(grid[i][j]);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    interface Condition<E> {
        public boolean test(E object);
    }

    public static void main(String[] args){
        World world= new World();
        world.initFromIndividualAllPicture(
                FileUtils.readStringFromFile(
                        new File("C:\\Users\\hyx\\Desktop\\kk" +
                                "\\interactive_fermi_$_no_migrate_$_continuous_strategy_$_pi=1.00_$_qi=0.00_$_w=1.00" +
                                "\\gr=(1.00,-0.10,1.10,0.00)_$_d0=1.00" +
                                "\\txt_format\\all_1.txt")));
        System.out.println(world.getIndividualAllPicture());

    }
}
