package rule;

import java.util.Arrays;

public enum StrategyPattern {
    TWO(2)//("two_strategy", 2)
    ,
    THREE(3)//("three_strategy", 3)
    ,
    FIVE(5)//("five_strategy", 5)
    ,
    CONTINUOUS(11)//("continuous_strategy", 11)
    ;

    /**
     * ��¼�������ܲ��õĲ��ԵĴ�����ֵ������������ģʽ��TWO��������龭�͸ô洢0.0��1.0
     */
    final public float[] strategySample;
//    public String name;
    /**
     * ��ģʽ�²�����Ŀ�����ӣ���ɢ���ԣ�TWO�� strategyNum = 2 Ӧ�ð���0.0��1.0
     */
    public int strategyNum;

    StrategyPattern(int n){
        this.strategyNum = n;
        if (strategyNum > 1) {
            strategySample = new float[strategyNum];
            for (int i = 0; i < strategyNum; i++) {
                strategySample[i] = 0 + i / (float) (strategyNum - 1);
            }
        } else {
            strategySample = new float[1];
        }
    }

//    StrategyPattern(String s, int n) {
//        name = s;
//        this.strategyNum = n;
//        if (strategyNum > 1) {
//            strategySample = new float[strategyNum];
//            for (int i = 0; i < strategyNum; i++) {
//                strategySample[i] = 0 + i / (float) (strategyNum - 1);
//            }
//        } else {
//            strategySample = new float[1];
//        }
//    }

    public int getStrategyNum() {
        return strategyNum;
    }

    public float getStrategySample(int index) {
        if (index < 0 || index >= strategyNum) {
            return -1;
        } else {
            return strategySample[index];
        }
    }

    public int getStrategyIndex(float  strategy) {
        return Arrays.binarySearch(strategySample, strategy);
    }

//    @Override
//    public String toString() {
//        return name;
//    }
}
