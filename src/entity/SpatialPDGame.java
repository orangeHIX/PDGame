package entity;

import graphic.Painter;
import gui.DebugWindow;
import org.json.JSONObject;
import rule.*;
import utils.ArrayUtils;
import utils.Reporter;
import utils.Snapshot;
import utils.WorldDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.*;

public class SpatialPDGame implements Reporter, JsonEntity {

    /**
     * 是否要记录实验过程中的人口斑图
     */
    private boolean recordSnapShoot;


    public DataPrinter dataPrinter = new DataPrinter();

    private World world;
    private Map<String, Object> param = new HashMap<>();
    private GamblingRule gr;
    private LearningPattern learningPattern;
    private MigrationPattern migrationPattern;
    private StrategyPattern strategyPattern;
    private NeighbourCoverage neighbourCoverage;
    private EvolutionPattern evolutionPattern;
    private float d0;
    /**
     * 学习的概率
     */
    private float pi;
    /**
     * 迁徙的概率
     */
    private float qi;
    /**
     * 初始所有个体的交互强度
     */
    private float w;
    /**
     * 当前实验进行到的轮数
     */
    private int turn;
    /**
     * 当前实验中个体策略不再发生变化的累计轮数，可能会因为个体策略发生变化而被重置为0
     */
    private int noChangeTurn;
    /**
     * 当前实验是否结束
     */
    private boolean isFinished;
    /**
     * 用于输出调试信息的接口
     */
    private Reporter reporter;
    /**
     * 记录特定轮数模型快照的容器
     */
    private Map<Integer, WorldDetail> worldDetailHistory = new HashMap<>();
    /**
     * 记录实验过程中的人口斑图 的容器
     */
    private Map<Integer, Snapshot> snapshotMap = new HashMap<>();

    public SpatialPDGame() {
        world = new World();
        this.recordSnapShoot = true;
        this.reporter = this;
    }

    /**
     * @param recordSnapShoot 是否要记录实验过程中的人口斑图
     */
    public SpatialPDGame(boolean recordSnapShoot) {
        world = new World();
        this.recordSnapShoot = recordSnapShoot;
        this.reporter = this;
    }

    public SpatialPDGame(boolean recordSnapShoot, Reporter reporter) {
        world = new World();
        this.recordSnapShoot = recordSnapShoot;
        this.reporter = reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    /**
     * 结束实验，模型将会被冻结，无法再进行演化（run）
     */
    public void done() {
        if (!isFinished) {
            isFinished = true;
            // globalCooperationLevelHistory.put(turn,
            // world.getGlobalCooperationLevel());
            worldDetailHistory.put(turn, world.getWorldDetail());
            recordSnapshoot();
        }
    }

    public float getCooperationLevel() {
        return world.getGlobalCooperationLevel();
    }

    // float averageNeighbourNum = 0;
    public Image getCurrentPicture() {
        return Painter.getPDGameImage(400, 400, world);
    }

    public Map<Integer, Snapshot> getSnapshootMap() {
        return snapshotMap;
    }

    /**
     * 该实验所用囚徒矩阵((R，S),(T,P))
     *
     * @param pi              学习邻居策略的概率
     * @param qi              迁徙的概率
     * @param w               初始所有个体的交互强度
     * @param learningPattern 学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
     * @param migratePattern  迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float R, float S, float T,
                                  float P, float pi, float qi, float w,
                                  LearningPattern learningPattern, MigrationPattern migratePattern,
                                  StrategyPattern strategyPattern, NeighbourCoverage neighbourCoverage,
                                  EvolutionPattern evolutionPattern) {

        world.init_world(L, d0, w, strategyPattern, neighbourCoverage);
        gr = new GamblingRule(R, S, T, P);
        this.d0 = d0;
        this.pi = pi;
        this.qi = qi;
        this.w = w;
        this.learningPattern = learningPattern;
        this.migrationPattern = migratePattern;
        this.strategyPattern = strategyPattern;
        this.neighbourCoverage = neighbourCoverage;
        this.evolutionPattern = evolutionPattern;
        turn = 0;
        noChangeTurn = 0;
        isFinished = false;
        snapshotMap.clear();
        worldDetailHistory.clear();
        param.clear();

        param.put("L", world.getLength());
        param.put("d0", this.d0);
        param.put("pi", this.pi);
        param.put("qi", this.qi);
        param.put("w", this.w);
        param.put("gr", this.gr);
        param.put("learn",
                this.learningPattern);
        param.put("migrate",
                this.migrationPattern);
        param.put("strategy",
                this.strategyPattern);
        param.put("neighbour",
                this.neighbourCoverage);
        param.put("evolute", this.evolutionPattern);
        System.out.println(this);
    }

    /**
     * 该实验所用囚徒矩阵((1，-Dr),(1+Dg,0))
     *
     * @param pi              学习邻居策略的概率
     * @param qi              迁徙的概率
     * @param w               初始所有个体的交互强度
     * @param learningPattern 学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
     * @param migratePattern  迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float Dr, float Dg,
                                  float pi, float qi, float w, LearningPattern learningPattern,
                                  MigrationPattern migratePattern, StrategyPattern strategyPattern,
                                  NeighbourCoverage neighbourCoverage, EvolutionPattern evolutionPattern) {

        initSpatialPDGame(L, d0, 1, -Dr, 1 + Dg, 0, pi, qi, w, learningPattern,
                migratePattern, strategyPattern, neighbourCoverage, evolutionPattern);

    }

    /**
     * 该实验所用囚徒矩阵((1，-r),(1+r,0))
     *
     * @param pi              学习邻居策略的概率
     * @param qi              迁徙的概率
     * @param w               初始所有个体的交互强度
     * @param learningPattern 学习模式，可以是学习最优邻居MAXPAYOFF, 也可以是fermi学习模式FERMI
     * @param migratePattern  迁徙模式 ，可以是无迁徙NONE、随机迁徙RANDOM、机会迁徙OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float r, float pi, float qi,
                                  float w, LearningPattern learningPattern,
                                  MigrationPattern migratePattern, StrategyPattern strategyPattern,
                                  NeighbourCoverage neighbourCoverage, EvolutionPattern evolutionPattern) {

        initSpatialPDGame(L, d0, r, r, pi, qi, w, learningPattern,
                migratePattern, strategyPattern, neighbourCoverage, evolutionPattern);

    }

    private void recordSnapshoot() {
        if (recordSnapShoot) {
            snapshotMap.put(turn, world.getSnapshot());//getCurrentPicture());
            //reporter.report("snapshot at turn " + turn);
        }
    }

    /**
     * 实验进行若干轮
     *
     * @param turnNum 实验要进行的轮数
     */
    public void run(int turnNum) {
        run(turnNum, new SampleCheck() {
            final int[] ids = {0, 1, 2, 3, 4, 5, 6, 20, 50};

            @Override
            public boolean isWorldDetailHistorySampleTurn(int turn) {
                // TODO Auto-generated method stub
                if (Arrays.binarySearch(ids, turn) >= 0) {
                    return true;
                } else if ((turn) % 100 == 0) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean isSnapshootSampleTurn(int turn) {
                // TODO Auto-generated method stub
                if (Arrays.binarySearch(ids, turn) >= 0) {
                    return true;
                } else {
                    int i = 1;
                    while (turn > Math.pow(10, i)) {
                        i++;
                    }
                    if (turn == Math.pow(10, i)) {
                        return true;
                    }
                }
                return false;
            }

        });
    }

    /**
     * 实验进行若干轮
     *
     * @param turnNum     实验要进行的轮数
     * @param sampleCheck 是否是特定采样轮数的检查
     */
    public void run(int turnNum, SampleCheck sampleCheck) {
        if (!isFinished) {
            int cumulativeTurnNum = 0;
            // 如果是初次调用该方法，先进行一次采样记录模型初始状态
            if (turn == 0) {
                // globalCoopertationLevelHistory.clear();
                worldDetailHistory.clear();
                snapshotMap.clear();
                noChangeTurn = 0;

                worldDetailHistory.put(turn, world.getWorldDetail());

                recordSnapshoot();

            }

            int population = world.getPopulationNum();

            while (cumulativeTurnNum < turnNum) {
                turn++;
                cumulativeTurnNum++;

                world.gambling(gr);
                if (sampleCheck.isWorldDetailHistorySampleTurn(turn)) {
                    // globalCoopertationLevelHistory.put(turn,
                    // world.getGlobalCooperationLevel());
                    worldDetailHistory.put(turn, world.getWorldDetail());
                }

                if (world.evolute(evolutionPattern, pi, qi, learningPattern, migrationPattern)
                        < 1 / (float) population) {
                    noChangeTurn++;
                    if (noChangeTurn >= 100) {
                        break;
                    }
                } else {
                    noChangeTurn = 0;
                }
                if (sampleCheck.isSnapshootSampleTurn(turn)) {
                    recordSnapshoot();
                }

                if ((turn) % (2000) == 0) {
                    reporter.report("turn " + turn + " ......");
                }

                // System.out.println("合作率："+world.getCooperationRate());
            }
        }

    }

    /**
     * 获取当前实验进行到的轮数
     */
    public int getTurn() {
        return turn;
    }

    public World getWorld() {
        return world;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer("");
        sb.append('[');
        for (Map.Entry<String, Object> p : param.entrySet()) {
            sb.append(p.getKey());
            sb.append('=');
            if (p.getValue().getClass() == Float.class
                    || p.getValue().getClass() == Double.class) {
                sb.append(df.format(p.getValue()));
            } else {
                sb.append(p.getValue());
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');
        return sb.toString();
    }

    /**
     * 注意：只恢复了参数列表param
     */
    public void initFromToString(String toString) {
        String s = toString.substring(1, toString.length() - 1);
        param.clear();
        param.put("L", Integer.parseInt(getStringValue(s, "L")));
        param.put("d0", Float.parseFloat(getStringValue(s, "d0")));
        param.put("pi", Float.parseFloat(getStringValue(s, "pi")));
        param.put("qi", Float.parseFloat(getStringValue(s, "qi")));
        param.put("w", Float.parseFloat(getStringValue(s, "w")));
        GamblingRule gr = new GamblingRule(0);
        gr.initFromToString(getStringValue(s, "gr"));
        param.put("gr", gr);
        param.put("learn",
                LearningPattern.valueOf(getStringValue(s, "learn")));
        param.put("migrate",
                MigrationPattern.valueOf(getStringValue(s, "migrate")));
        param.put("strategy",
                StrategyPattern.valueOf(getStringValue(s, "strategy")));
        param.put("neighbour",
                NeighbourCoverage.valueOf(getStringValue(s, "neighbour")));
        param.put("evolute", EvolutionPattern.valueOf(getStringValue(s, "evolute")));
    }

    private String getStringValue(String s, String name) {
        s += ",";
        int nameIndex = s.indexOf(name);
        if (nameIndex < 0) {
            return null;
        }
        int start = s.indexOf("=", nameIndex) + 1;
        int end = s.indexOf(",", start);
        String value = s.substring(start, end);
        if (value.contains("[")) {
            end = s.indexOf(",", s.indexOf("]", start));
            value = s.substring(start, end);
        }
        return value;
    }

    @Override
    public void report(String s) {
        // TODO Auto-generated method stub
        System.out.println(s);
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jo = new JSONObject();
        for (Map.Entry<String, Object> p : param.entrySet()) {
//            Class<?> type = p.getValue().getClass();
//            if( type.isEnum()){
//                jo.put(p.getKey(), p.getValue().toString());
//            }else{
//                jo.put(p.getKey(),p.getValue());
//            }
            if (p.getValue() instanceof JsonEntity) {
                jo.put(p.getKey(), ((JsonEntity) p.getValue()).getJSONObject());
            } else {
                jo.put(p.getKey(), p.getValue());
            }
        }
        return jo;
    }

    /**
     * 注意：只恢复了参数列表param
     */
    @Override
    public SpatialPDGame initFromJSONObject(JSONObject jsonObject) {
        param.clear();
        param.put("L", jsonObject.get("L"));
        param.put("d0", jsonObject.get("d0"));
        param.put("pi", jsonObject.get("pi"));
        param.put("qi", jsonObject.get("qi"));
        param.put("w", jsonObject.get("w"));
        GamblingRule gr = new GamblingRule(0);
        gr.initFromJSONObject(jsonObject.getJSONObject("gr"));
        param.put("gr", gr);
        param.put("learn",
                LearningPattern.valueOf((String) jsonObject.get("learn")));
        param.put("migrate",
                MigrationPattern.valueOf((String) jsonObject.get("migrate")));
        param.put("strategy",
                StrategyPattern.valueOf((String) jsonObject.get("strategy")));
        param.put("neighbour",
                NeighbourCoverage.valueOf((String) jsonObject.get("neighbour")));
        param.put("evolute", EvolutionPattern.valueOf((String) jsonObject.get("evolute")));
        return this;
    }

    /**
     * 注意：只恢复了参数列表param
     */
    @Override
    public SpatialPDGame initFromJSONSource(String source) {
        return initFromJSONObject(new JSONObject(source));
    }

    public interface SampleCheck {
        /**
         * 判断是否需要采样，每隔间隔轮数进行一次采样，统计合作水平
         *
         * @param turn 当前演化轮数
         */
        boolean isWorldDetailHistorySampleTurn(int turn);

        /**
         * 判断是否需要记录演化斑图
         *
         * @param turn 当前演化轮数
         */
        boolean isSnapshootSampleTurn(int turn);
    }

    public class DataPrinter {


        public static final String GlobalCooperationLevelString = "Global cooperate level = ";
        public static final String UnderwentTurnPrefix = "underwent ";
        public static final String UnderwentTurnSuffix = " turns";

        public String getJsonString() {
            return getJSONObject().toString();
        }

        /**
         * 获取本次实验最终结果报告
         */
        public String getDetailReport() {
            StringBuilder sb = new StringBuilder();
            ArrayList<Integer> keyList = new ArrayList<>();
            // keyList.addAll(globalCoopertationLevelHistory.keySet());
            // Collections.sort(keyList);
            keyList.addAll(worldDetailHistory.keySet());
            Collections.sort(keyList);

            sb.append(SpatialPDGame.this.toString());
            sb.append("\r\n");
            sb.append(GlobalCooperationLevelString +
                    +world.getGlobalCooperationLevel());
            sb.append("\t");
            sb.append(UnderwentTurnPrefix).append(turn).append(UnderwentTurnSuffix).append("\r\n");
            sb.append("average neigbour num: ").append(world.getAverageNeighbourNum()).append("\r\n");
            sb.append("turn\tcoopLev\t");

            WorldDetail wd = worldDetailHistory.get(new Integer(0));
            int strategyNum = 0;
            if (wd != null) {
                strategyNum = wd.strategyProportion.length;
            }
            for (int i = 0; i < strategyNum; i++) {
                sb.append("stra" + i + "\t");
            }
            sb.append("\r\n");

            DecimalFormat df = new DecimalFormat("0.000");

            for (Integer i : keyList) {
                sb.append(i);
                sb.append('\t');
                wd = worldDetailHistory.get(i);
                sb.append(df.format(wd.globalCooperationLevel));
                sb.append('\t');
                for (int j = 0; j < strategyNum; j++) {
                    sb.append(df.format(wd.strategyProportion[j]));
                    sb.append('\t');
                }
                //sb.append("GM = ");
                //sb.append(ArrayUtils.getTwoDeArrayStringinOneLine(wd.strategyGamblingMatrix));
                //sb.append("\tPM = ");
                //sb.append(ArrayUtils.getTwoDeArrayStringinOneLine(wd.strategyPayoffMatrix));
                sb.append("\r\n");
            }
            sb.append("\r\nfinal stragety picture:\r\n");
            sb.append(world.getIndividualStrategyPicture());
            return sb.toString();
        }

        public float getGlobalCooperationLevelFromDetailReport(String detailReport) {
            int start = detailReport.indexOf(GlobalCooperationLevelString) + GlobalCooperationLevelString.length();
            int end = detailReport.indexOf("\t", start);
            return Float.parseFloat(detailReport.substring(start, end));
        }


        public Integer getUnderwentTurnFromDetailReport(String detailReport) {
            int start = detailReport.indexOf(UnderwentTurnPrefix) + UnderwentTurnPrefix.length();
            int end = detailReport.indexOf(UnderwentTurnSuffix, start);
            return Integer.parseInt(detailReport.substring(start, end));
        }

        /**
         * 显示模型散点图
         */
        public void printPicture() {

            // 创建frame
            JFrame frame = new DebugWindow(world);
            // 调整frame的大小和初始位置
            frame.setSize(880, 880);
            frame.setLocation(100, 100);
            // 增加窗口监听事件，使用内部类方法，并用监听器的默认适配器
            frame.addWindowListener(new WindowAdapter() {

                // 重写窗口关闭事件
                @Override
                public void windowClosing(WindowEvent arg0) {
                    System.exit(0);
                }

            });
            frame.setTitle(this.toString());
            // 显示窗体
            frame.setVisible(true);
        }

        public String constructFilePath(String base) {
            DecimalFormat df = new DecimalFormat("0.00");
            return base + "\\" + learningPattern + "_$_" + migrationPattern
                    + "_$_" + strategyPattern + "_$_" + neighbourCoverage
                    + "_$_" + evolutionPattern + "_$_pi=" + df.format(pi)
                    + "_$_qi=" + df.format(qi) + "_$_w=" + df.format(w);
        }

        public String constructDetailReportFileName(String base) {
            DecimalFormat df = new DecimalFormat("0.00");
            return constructFilePath(base) + "\\" + "gr=("
                    + df.format(gr.getR()) + "," + df.format(gr.getS()) + ","
                    + df.format(gr.getT()) + "," + df.format(gr.getP()) + ")"
                    + "_$_d0=" + df.format(d0) + ".txt";
        }

        public String constructImageFilePath(String base) {
            return constructDetailReportFileName(base).replace(".txt", "");
        }
    }

    public static void main(String[] args) {
        SpatialPDGame spdg = new SpatialPDGame(true);
        spdg.initSpatialPDGame(5, 1.0f, 0.1f, 0.1f, 1.0f, 0, 1.0f,
                LearningPattern.INTERACTIVE_FERMI, MigrationPattern.NONE,
                StrategyPattern.CONTINUOUS, NeighbourCoverage.Von, EvolutionPattern.CDO);
        System.out.println(spdg.getJSONObject());
        spdg.initFromToString(spdg.toString());
        System.out.println(spdg.getJSONObject());
    }
}
