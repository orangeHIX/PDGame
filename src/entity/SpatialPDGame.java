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

public class SpatialPDGame implements Reporter, JsonEntity{

    /**
     * �Ƿ�Ҫ��¼ʵ������е��˿ڰ�ͼ
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
    private float d0;
    /**
     * ѧϰ�ĸ���
     */
    private float pi;
    /**
     * Ǩ��ĸ���
     */
    private float qi;
    /**
     * ��ʼ���и���Ľ���ǿ��
     */
    private float w;
    /**
     * ��ǰʵ����е�������
     */
    private int turn;
    /**
     * ��ǰʵ���и�����Բ��ٷ����仯���ۼ����������ܻ���Ϊ������Է����仯��������Ϊ0
     */
    private int noChangeTurn;
    /**
     * ��ǰʵ���Ƿ����
     */
    private boolean isFinished;
    /**
     * �������������Ϣ�Ľӿ�
     */
    private Reporter reporter;
    /**
     * ��¼�ض�����ģ�Ϳ��յ�����
     */
    private Map<Integer, WorldDetail> worldDetailHistory = new HashMap<>();
    /**
     * ��¼ʵ������е��˿ڰ�ͼ ������
     */
    private Map<Integer, Snapshot> snapshotMap = new HashMap<>();

    public SpatialPDGame(){};

    /**
     * @param recordSnapShoot �Ƿ�Ҫ��¼ʵ������е��˿ڰ�ͼ
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
     * ����ʵ�飬ģ�ͽ��ᱻ���ᣬ�޷��ٽ����ݻ���run��
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
     * ��ʵ��������ͽ����((R��S),(T,P))
     *
     * @param pi              ѧϰ�ھӲ��Եĸ���
     * @param qi              Ǩ��ĸ���
     * @param w               ��ʼ���и���Ľ���ǿ��
     * @param learningPattern ѧϰģʽ��������ѧϰ�����ھ�MAXPAYOFF, Ҳ������fermiѧϰģʽFERMI
     * @param migratePattern  Ǩ��ģʽ ����������Ǩ��NONE�����Ǩ��RANDOM������Ǩ��OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float R, float S, float T,
                                  float P, float pi, float qi, float w,
                                  LearningPattern learningPattern, MigrationPattern migratePattern,
                                  StrategyPattern strategyPattern, NeighbourCoverage neighbourCoverage) {

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
        param.put("learningPattern",
                this.learningPattern);
        param.put("migrationPattern",
                this.migrationPattern);
        param.put("strategyPattern",
                this.strategyPattern);
        param.put("neighbourCoverage",
                this.neighbourCoverage);
        System.out.println(this);
    }

    /**
     * ��ʵ��������ͽ����((1��-Dr),(1+Dg,0))
     *
     * @param pi              ѧϰ�ھӲ��Եĸ���
     * @param qi              Ǩ��ĸ���
     * @param w               ��ʼ���и���Ľ���ǿ��
     * @param learningPattern ѧϰģʽ��������ѧϰ�����ھ�MAXPAYOFF, Ҳ������fermiѧϰģʽFERMI
     * @param migratePattern  Ǩ��ģʽ ����������Ǩ��NONE�����Ǩ��RANDOM������Ǩ��OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float Dr, float Dg,
                                  float pi, float qi, float w, LearningPattern learningPattern,
                                  MigrationPattern migratePattern, StrategyPattern strategyPattern,
                                  NeighbourCoverage neighbourCoverage) {

        initSpatialPDGame(L, d0, 1, -Dr, 1 + Dg, 0, pi, qi, w, learningPattern,
                migratePattern, strategyPattern, neighbourCoverage);

    }

    /**
     * ��ʵ��������ͽ����((1��-r),(1+r,0))
     *
     * @param pi              ѧϰ�ھӲ��Եĸ���
     * @param qi              Ǩ��ĸ���
     * @param w               ��ʼ���и���Ľ���ǿ��
     * @param learningPattern ѧϰģʽ��������ѧϰ�����ھ�MAXPAYOFF, Ҳ������fermiѧϰģʽFERMI
     * @param migratePattern  Ǩ��ģʽ ����������Ǩ��NONE�����Ǩ��RANDOM������Ǩ��OPTIMISTIC
     */
    public void initSpatialPDGame(int L, float d0, float r, float pi, float qi,
                                  float w, LearningPattern learningPattern,
                                  MigrationPattern migratePattern, StrategyPattern strategyPattern,
                                  NeighbourCoverage neighbourCoverage) {

        initSpatialPDGame(L, d0, r, r, pi, qi, w, learningPattern,
                migratePattern, strategyPattern, neighbourCoverage);

    }

    private void recordSnapshoot() {
        if (recordSnapShoot) {
            snapshotMap.put(turn, world.getSnapshot());//getCurrentPicture());
            reporter.report("snapshot at turn " + turn);
        }
    }

    /**
     * ʵ�����������
     *
     * @param turnNum ʵ��Ҫ���е�����
     */
    public void run(int turnNum) {
        run(turnNum, new SampleCheck() {
            final int[] ids = {0, 1, 2, 3, 4, 5, 6,20,50 };

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
                    while(turn > Math.pow(10, i)){
                        i++;
                    }
                    if( turn == Math.pow(10,i)){
                        return true;
                    }
                }
                return false;
            }

        });
    }

    /**
     * ʵ�����������
     *
     * @param turnNum     ʵ��Ҫ���е�����
     * @param sampleCheck �Ƿ����ض����������ļ��
     */
    public void run(int turnNum, SampleCheck sampleCheck) {
        if (!isFinished) {
            int cumulativeTurnNum = 0;
            // ����ǳ��ε��ø÷������Ƚ���һ�β�����¼ģ�ͳ�ʼ״̬
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

                if (world.evolute(pi, qi, learningPattern, migrationPattern) < 1 / (float) population) {
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

                // System.out.println("�����ʣ�"+world.getCooperationRate());
            }
        }

    }

    /**
     * ��ȡ��ǰʵ����е�������
     */
    public int getTurn() {
        return turn;
    }

    public Map<String,Object> getParam(){
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
        // return "[L=" + world.getLength() + ", d0=" + df.format(d0) + ", " +
        // gr
        // + ", learningPattern=" + learningPattern + ", imigratePattern="
        // + migrationPattern + ", strategyPattern=" + strategyPattern
        // + ", pi=" + pi + ", qi=" + qi + "]";
    }

    @Override
    public void report(String s) {
        // TODO Auto-generated method stub
        System.out.println(s);
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jo = new JSONObject();
        for(Map.Entry<String, Object> p : param.entrySet()){
//            Class<?> type = p.getValue().getClass();
//            if( type.isEnum()){
//                jo.put(p.getKey(), p.getValue().toString());
//            }else{
//                jo.put(p.getKey(),p.getValue());
//            }
            if(p.getValue() instanceof  JsonEntity){
                jo.put(p.getKey(), ((JsonEntity) p.getValue()).getJSONObject());
            }else {
                jo.put(p.getKey(), p.getValue());
            }
        }
        return jo;
    }

    /**ע�⣺ֻ�ָ��˲����б�param*/
    @Override
    public void initFromJSONObject(JSONObject jsonObject) {
        param = new HashMap<>();
        param.put("L", jsonObject.get("L"));
        param.put("d0",jsonObject.get("d0"));
        param.put("pi", jsonObject.get("pi"));
        param.put("qi", jsonObject.get("qi"));
        param.put("w", jsonObject.get("w"));
        GamblingRule gr = new GamblingRule(0);
        gr.initFromJSONObject(jsonObject.getJSONObject("gr"));
        param.put("gr", gr);
        param.put("learningPattern",
                LearningPattern.valueOf((String)jsonObject.get("learningPattern")));
        param.put("migrationPattern",
                MigrationPattern.valueOf((String)jsonObject.get("migrationPattern")));
        param.put("strategyPattern",
                StrategyPattern.valueOf((String) jsonObject.get("strategyPattern")));
        param.put("neighbourCoverage",
                NeighbourCoverage.valueOf((String) jsonObject.get("neighbourCoverage")));

    }

    /**ע�⣺ֻ�ָ��˲����б�param*/
    @Override
    public void initFromJSONSource(String source) {
        initFromJSONObject(new JSONObject(source));
    }

    public interface SampleCheck {
        /**
         * �ж��Ƿ���Ҫ������ÿ�������������һ�β�����ͳ�ƺ���ˮƽ
         *
         * @param turn ��ǰ�ݻ�����
         */
        boolean isWorldDetailHistorySampleTurn(int turn);

        /**
         * �ж��Ƿ���Ҫ��¼�ݻ���ͼ
         *
         * @param turn ��ǰ�ݻ�����
         */
        boolean isSnapshootSampleTurn(int turn);
    }

    public class DataPrinter {

        public String getJsonString(){
            return getJSONObject().toString();
        }
        /**
         * ��ȡ����ʵ�����ս������
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
            sb.append("Global cooperate level = "
                    + world.getGlobalCooperationLevel());
            sb.append("\tunderwent " + turn + " turns\r\n");
            sb.append("average neigbour num: " + world.getAverageNeighbourNum()
                    + "\r\n");
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

        /**
         * ��ʾģ��ɢ��ͼ
         */
        public void printPicture() {

            // ����frame
            JFrame frame = new DebugWindow(world);
            // ����frame�Ĵ�С�ͳ�ʼλ��
            frame.setSize(880, 880);
            frame.setLocation(100, 100);
            // ���Ӵ��ڼ����¼���ʹ���ڲ��෽�������ü�������Ĭ��������
            frame.addWindowListener(new WindowAdapter() {

                // ��д���ڹر��¼�
                @Override
                public void windowClosing(WindowEvent arg0) {
                    System.exit(0);
                }

            });
            frame.setTitle(this.toString());
            // ��ʾ����
            frame.setVisible(true);
        }

        public String constructFilePath(String base) {
            DecimalFormat df = new DecimalFormat("0.00");
            return base + "\\" + learningPattern + "_$_" + migrationPattern
                    + "_$_" + strategyPattern + "_$_pi=" + df.format(pi)
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
}