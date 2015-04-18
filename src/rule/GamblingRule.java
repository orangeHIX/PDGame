package rule;

public class GamblingRule {

	/** 策略-合作 取值1.0f */
	public static float STRATEGY_C = 1.0f;
	/** 策略-背叛 取值0.0f */
	public static float STRATEGY_D = 0.0f;

	private float R;
	private float S;
	private float T;
	private float P;
	boolean isContinuous;

	/**
	 * 初始化收益矩阵，R S T P均为收益
	 * 
	 * @param R
	 *            双方都合作时的奖励
	 * @param S
	 *            被欺骗一方的收益
	 * @param T
	 *            背叛的诱惑
	 * @param P
	 *            双方都背叛时的惩罚
	 */
	public GamblingRule(float R, float S, float T, float P) {
		this.R = R;
		this.S = S;
		this.T = T;
		this.P = P;
	}
	/**
	 * 初始化收益矩阵，收益R S T P分别为： 1 -Dr (1.0+Dg) 0
	 */
	public GamblingRule(float Dr, float Dg){
		if (Dr < 0 || Dr > 1.0001f
				|| Dg < 0 || Dg > 1.0001f)
			throw new IllegalArgumentException("Dr或者Dg取值区间不在[0,1.0]");
		this.R = 1.0f;
		this.S = -Dr;
		this.T = 1.0f + Dg;
		this.P = 0;
	}

	/**
	 * 初始化收益矩阵，收益R S T P分别为： 1 -r (1.0+r) 0
	 * 
	 * @param r
	 *            背叛的诱惑
	 */
	public GamblingRule(float r) {
		if (r < 0 || r > 1.0001f)
			throw new IllegalArgumentException("r取值区间不在[0,1.0]");
		this.R = 1.0f;
		this.S = -r;
		this.T = 1.0f + r;
		this.P = 0;
	}

	public float getTemptationOfDefection() {
		return T - R;
	}

	/** 计算并返回采取策略s1的个体应对采取策略s2的个体进行博弈获得的收益 */
	public float getPayOff(float s1, float s2) {
		// 无论策略是否是连续的，都可以按照这个公式计算
		return (R - S - T + P) * s1 * s2 + (S - P) * s1 + (T - P) * s2 + P;
	}
	@Override
	public String toString() {
		return "[R=" + R + ", S=" + S + ", T=" + T + ", P=" + P
				+ "]";
	}
	public float getR() {
		return R;
	}
	public float getS() {
		return S;
	}
	public float getT() {
		return T;
	}
	public float getP() {
		return P;
	}
}
