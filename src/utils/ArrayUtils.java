package utils;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ArrayUtils {

    public static int[][] getIntMatrix(int length) {
        int a[][] = new int[length][];
        for (int i = 0; i < length; i++) {
            a[i] = new int[length];
        }
        return a;
    }

    public static float[][] getFloatMatrix(int length) {
        float a[][] = new float[length][];
        for (int i = 0; i < length; i++) {
            a[i] = new float[length];
        }
        return a;
    }

    public static void clearIntMatrix(int matrix[][], int L1, int L2) {
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public static void clearFloatMatrix(float matrix[][], int L1, int L2) {
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public static int[][] copyIntMatrix(int[][] fromMatrix, int L1, int L2) {
        int[][] toMatrix = new int[L1][];
        for (int i = 0; i < L1; i++) {
            toMatrix[i] = Arrays.copyOf(fromMatrix[i], L2);
        }
        return toMatrix;
    }

    public static float[][] copyFloatMatrix(float[][] fromMatrix, int L1, int L2) {
        float[][] toMatrix = new float[L1][];
        for (int i = 0; i < L1; i++) {
            toMatrix[i] = Arrays.copyOf(fromMatrix[i], L2);
        }
        return toMatrix;
    }

    public static String getTwoDeArrayStringinOneLine(int[][] a){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(Arrays.toString(a[i]));
            sb.append(", ");
        }
        sb.replace(sb.length()-", ".length(),sb.length(),"");
        sb.append("]");
        return sb.toString();
    }

    public static String getTwoDeArrayStringinOneLine(float[][] a){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        sb.append("[");
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for(int j = 0; j < a[i].length; j++){
                sb.append(df.format(a[i][j]));
                sb.append(", ");
            }
            sb.replace(sb.length()-", ".length(),sb.length(),"");
            sb.append("], ");
        }
        sb.replace(sb.length()-", ".length(),sb.length(),"");
        sb.append("]");
        return sb.toString();
    }

    public static String getTwoDeArrayString(double[][] a, int L1, int L2) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.000");
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                sb.append(df.format(a[i][j]));
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static String getTwoDeArrayString(float[][] a, int L1, int L2) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.000");
        for (int i = 0; i < L1; i++) {
            for (int j = 0; j < L2; j++) {
                sb.append(df.format(a[i][j]));
                sb.append('\t');
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static void main(String[] args){
        int len = 5;
        float[][] a = getFloatMatrix(len);
        for(int i =0; i < len; i++){
            for(int j = 0; j < len; j++){
                a[i][j] = RandomUtil.nextFloat();
            }
        }
        System.out.println(getTwoDeArrayStringinOneLine(a));
    }
}
