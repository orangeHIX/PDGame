package rule;

import utils.Vector2;

import java.util.ArrayList;

public enum NeighbourCoverage {

    /**
     * 个体周围其他与该个体的横纵坐标之差均不超过特定值的个体均为该个体的直接邻居。例如特定值为1，个体坐标为（3，3）那么位于坐标（2，2）（2，3）（
     * 2，4）（3，2）（3，4）（4，2）（4，3）（4，4）的个体均为位于（3，3）个体的邻居
     */
    Classic {
        @Override
        protected ArrayList<Vector2> getCoverage() {
            // TODO 自动生成的方法存根
            int range = 1;
            ArrayList<Vector2> vectors = new ArrayList<>();
            for (int i = -range; i < range + 1; i++) {
                for (int j = -range; j < range + 1; j++) {
                    vectors.add(new Vector2(i, j));
                }
            }
            vectors.remove(new Vector2(0, 0));
            return vectors;
        }

    },
    /**
     * Von(冯诺依曼邻居),最大邻居个数n = 4，既上下左右4个邻居
     */
    Von {
        @SuppressWarnings("serial")
        @Override
        protected ArrayList<Vector2> getCoverage() {
            // TODO 自动生成的方法存根
            return new ArrayList<Vector2>() {
                {
                    add(new Vector2(0, -1));
                    add(new Vector2(-1, 0));
                    add(new Vector2(1, 0));
                    add(new Vector2(0, 1));
                }
            };
        }

    };

    private ArrayList<Vector2> coverageVectors;

    NeighbourCoverage() {
        coverageVectors = getCoverage();
    }

    /**
     * 返回某一个体周围可能是邻居位置的相对坐标，冯诺依曼邻居为例:(0,1)(0,-1)(1,0)(-1,0)
     */
    public ArrayList<Vector2> getCoverageVector() {
        return coverageVectors;
    }


    abstract protected ArrayList<Vector2> getCoverage();
}
