package rule;

public class GamblingRule {

	/** ����-���� ȡֵ1.0f */
	public static float STRATEGY_C = 1.0f;
	/** ����-���� ȡֵ0.0f */
	public static float STRATEGY_D = 0.0f;

	private float R;
	private float S;
	private float T;
	private float P;
	boolean isContinuous;

	/**
	 * ��ʼ���������R S T P��Ϊ����
	 * 
	 * @param R
	 *            ˫��������ʱ�Ľ���
	 * @param S
	 *            ����ƭһ��������
	 * @param T
	 *            ���ѵ��ջ�
	 * @param P
	 *            ˫��������ʱ�ĳͷ�
	 */
	public GamblingRule(float R, float S, float T, float P) {
		this.R = R;
		this.S = S;
		this.T = T;
		this.P = P;
	}
	/**
	 * ��ʼ�������������R S T P�ֱ�Ϊ�� 1 -Dr (1.0+Dg) 0
	 */
	public GamblingRule(float Dr, float Dg){
		if (Dr < 0 || Dr > 1.0001f
				|| Dg < 0 || Dg > 1.0001f)
			throw new IllegalArgumentException("Dr����Dgȡֵ���䲻��[0,1.0]");
		this.R = 1.0f;
		this.S = -Dr;
		this.T = 1.0f + Dg;
		this.P = 0;
	}

	/**
	 * ��ʼ�������������R S T P�ֱ�Ϊ�� 1 -r (1.0+r) 0
	 * 
	 * @param r
	 *            ���ѵ��ջ�
	 */
	public GamblingRule(float r) {
		if (r < 0 || r > 1.0001f)
			throw new IllegalArgumentException("rȡֵ���䲻��[0,1.0]");
		this.R = 1.0f;
		this.S = -r;
		this.T = 1.0f + r;
		this.P = 0;
	}

	public float getTemptationOfDefection() {
		return T - R;
	}

	/** ���㲢���ز�ȡ����s1�ĸ���Ӧ�Բ�ȡ����s2�ĸ�����в��Ļ�õ����� */
	public float getPayOff(float s1, float s2) {
		// ���۲����Ƿ��������ģ������԰��������ʽ����
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
