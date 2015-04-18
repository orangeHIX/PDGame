package utils;

import java.text.DecimalFormat;

public class ArraytoString {
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
}
