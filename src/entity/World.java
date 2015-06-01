package entity;

import rule.*;
import utils.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 二维网格，包含L*L个空位，和若干个体
 */
public class World implements WorldInfo {

    /**
     * 记录所有个体的表
     */
    private ArrayList<Individual> IndividualList;
    /**
     * 二维网络中所有空位，二维数组表示
     */
    private Seat[][] grid;
    /**
     * 二维网格的宽度
     */
    private int L;


    private NeighbourCoverage neighbourCoverage;
    private StrategyPattern strategyPattern;

//    private int[][] strategyGamblingMatrix;
//    private float[][] strategyPayoffMatrix;

    public World(){}


    /**
     * 初始化模型
     *
     * @param length            网格长度
     * @param w                 设置全部个体的初始交互强度
     * @param density           网格中个体的密度
     * @param strategyPattern   所有个体采用的博弈策略类型
     * @param neighbourCoverage 每个个体周围直接邻居分布模式
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

            // 给每个个体随机分配初始策略
            initIndividuals(density, w);

            // 给每个个体随机分配网格中的位置
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
     * 初始化模型中的个体
     *
     * @param d0 网格中个体的密度
     * @param w  设置全部个体的初始交互强度
     */
    private void initIndividuals(float d0,
                                 float w) {

        int inId = 1;

        int num = (int) (d0 * L * L); // 个体总数目
        // System.out.println("initIndividualStrategy"+num);
        int strategyNum = strategyPattern.getStrategyNum();

        //strategySample = new float[strategyNum];

        int maxNeighbourNum = neighbourCoverage.getCoverageVector().size();
        // 依次向个体表中添加初始个体
        int count = 0;
        for (int i = 0; i < strategyNum; i++) {
            //strategySample[i] = 0 + i / (float) (strategyNum - 1);
            for (int j = 0; j < num / strategyNum; j++) {
                this.IndividualList.add(new Individual(inId++, strategyPattern.getStrategySample(i),//strategySample[i],
                        maxNeighbourNum, w));
                count++;
            }
        }
        // 由于采取各种策略的个体数目在某些情况下不可能全部相等，平均分配后余下的若干个体按次序分配剩下的名额
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
     * 给每个个体随机分配网格中的位置
     */
    private void randomAllocateSeat() {
        int i = 0;
        // 从位置（0,0）开始，个体依次向后入座
        for (Iterator<Individual> e = IndividualList.iterator(); e.hasNext(); ) {
            getSeat(i).setOwner(e.next());
            i++;
        }
        // 实现个体随机分配座位，本质是一种洗牌算法，对座位“洗牌”
        for (i = 0; i < L * L; i++) {
            swapSeat(getSeat(i),
                    getSeat((int) (RandomUtil.nextFloat() * L * L)));
        }
    }

    /**
     * 座位上的个体之间相互交换位置
     */
    private void swapSeat(Seat seat1, Seat seat2) {
        Individual tmp = seat1.getOwner();
        seat1.setOwner(seat2.getOwner());
        seat2.setOwner(tmp);
    }

    /**
     * 得到从左到右,从上到下第loc个位置的座位
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
     * 得到第i行，第j列的个体
     */
    public Individual getIndividual(int i, int j) {
        return grid[i][j].getOwner();
    }

    /**
     * 一个时间步内所有的相邻个体之间都要进行两两博弈，每个个体累计自己的收益
     *
     * @param gr 博弈规则，包含了相应的收益矩阵
     */
    public void gambling(GamblingRule gr) {

        Individual neighbour;
        ArrayList<Seat> seatAround;
        // 重置所有个体的邻居列表（清空），重置所有个体累计收益（置零）
        for (Individual in : IndividualList) {
            in.resetAccumulatedPayoff();
            in.clearLastTurnInfo();
        }


//        float inStra, neiStra;
//        int inStraIndex, neiStraIndex;
//        float inPayoff, neiPayoff;
        for (Individual in : IndividualList) {

            seatAround = getOccupiedSeatAround(in.getSeat());// 得到个体周围的所有被占据座位

            for (Seat s : seatAround) {
                neighbour = s.getOwner();
                if (!in.hasNeighbour(neighbour)) {
                    // 将个体周围的所有邻居（如果有的话）添加到个体自己保留的邻居表中。
                    // 这样，即使有邻居迁移后，个体仍能找到本轮初始状态所有邻居
                    in.addNeighbour(neighbour);
                    neighbour.addNeighbour(in);
                    // 按照两个个体交互强度的平均值决定是否进行博弈
                    if (RandomUtil.nextFloat() <= (in.getW() + neighbour.getW()) / 2) {
                        // 个体与邻居的两两博弈后，累计收益
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
     * 演化：以pi的概率学习邻居的策略，以qi的概率迁徙，同时更新个体的交互强度
     *
     * @param pi                学习邻居策略的概率
     * @param qi                迁徙的概率
     * @param learningPattern   学习模式，可以是学习最优邻居MAXPAYOFF,
     *                          也可以是fermi学习模式ERMI
     * @param imigrationPattern 迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
     * @return 采取新策略的个体占总体的比率
     */
    public float evolute(float pi, float qi, LearningPattern learningPattern,
                         MigrationPattern imigrationPattern) {
        for (Individual in : IndividualList) {
            if (RandomUtil.nextFloat() <= pi) {
                in.learnFromNeighbours(learningPattern);
            }
            if (RandomUtil.nextFloat() <= qi) {
                // 个体在本轮进行迁徙，每个个体在博弈阶段自己保留有邻居表，迁徙后邻居仍能找到该个体
                in.imigrate(this, imigrationPattern);
            }
            // getEmptySeatAround(in);
        }
        int change = 0;
        // 个体更新下一轮要用的策略
        for (Individual in : IndividualList) {
            if (in.updateStrategy()) {
                change++;
            }
        }
        // 个体更新交互强度
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
     * 找到该座位周围所有在直接邻居距离范围内的座位
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
