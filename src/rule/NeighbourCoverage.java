package rule;

import utils.Vector2;

import java.util.ArrayList;

public enum NeighbourCoverage {

    /**
     * ������Χ������ø���ĺ�������֮����������ض�ֵ�ĸ����Ϊ�ø����ֱ���ھӡ������ض�ֵΪ1����������Ϊ��3��3����ôλ�����꣨2��2����2��3����
     * 2��4����3��2����3��4����4��2����4��3����4��4���ĸ����Ϊλ�ڣ�3��3��������ھ�
     */
    Classic {
        @Override
        protected ArrayList<Vector2> getCoverage() {
            // TODO �Զ����ɵķ������
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
     * Von(��ŵ�����ھ�),����ھӸ���n = 4������������4���ھ�
     */
    Von {
        @SuppressWarnings("serial")
        @Override
        protected ArrayList<Vector2> getCoverage() {
            // TODO �Զ����ɵķ������
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
     * ����ĳһ������Χ�������ھ�λ�õ�������꣬��ŵ�����ھ�Ϊ��:(0,1)(0,-1)(1,0)(-1,0)
     */
    public ArrayList<Vector2> getCoverageVector() {
        return coverageVectors;
    }


    abstract protected ArrayList<Vector2> getCoverage();
}
