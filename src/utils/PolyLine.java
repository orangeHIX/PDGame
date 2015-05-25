package utils;

/**
 * ’€œﬂ
 */
public class PolyLine {

    public float[] pointX;
    public float[] pointY;
    public int pointNum;
    public float xRangeS;
    public float xRangeE;
    public float yRangeS;
    public float yRangeE;

    public PolyLine(float[] pointX, float[] pointY, int pointNum,
                    float xRangeS, float xRangeE, float yRangeS, float yRangeE) {
        super();
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointNum = pointNum;
        this.xRangeS = xRangeS;
        this.xRangeE = xRangeE;
        this.yRangeS = yRangeS;
        this.yRangeE = yRangeE;
    }

}
