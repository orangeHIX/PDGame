package utils;

public class WorldDetail {

    public float globalCooperationLevel;
    public float[] strategyProportion;
    //public Matrix strategyGamblingMatrix = new Matrix(1,1);
    public int[][] strategyGamblingMatrix;
    public float[][] strategyPayoffMatrix;

    public WorldDetail(float globalCooperationLevel, float[] strategyProportion,
                       int[][] gambingMatrix, float[][] payoffMatrix) {
        super();
        this.globalCooperationLevel = globalCooperationLevel;
        this.strategyProportion = strategyProportion;
        this.strategyGamblingMatrix = gambingMatrix;
        this.strategyPayoffMatrix = payoffMatrix;
    }

}
