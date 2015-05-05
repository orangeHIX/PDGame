package rule;

import java.util.ArrayList;

import utils.Vector;

public enum NeighbourCoverage {
	

	/**
	 * ������Χ������ø���ĺ�������֮����������ض�ֵ�ĸ����Ϊ�ø����ֱ���ھӡ������ض�ֵΪ1����������Ϊ��3��3����ôλ�����꣨2��2����2��3����
	 * 2��4����3��2����3��4����4��2����4��3����4��4���ĸ����Ϊλ�ڣ�3��3��������ھ�
	 */
	Classic(new Coverage(){

		@Override
		public ArrayList<Vector> getCoverage(int range) {
			// TODO �Զ����ɵķ������
			ArrayList<Vector> vectors = new ArrayList<>();
			for(int i = -range; i < range+1; i++){
				for(int j = -range; j <range+1; j++){
					vectors.add(new Vector(i,j));
				}
			}
			vectors.remove(new Vector(0,0));
			return vectors;
		}
		
	}),
	/** Von(��ŵ�����ھ�),����ھӸ���n = 4������������4���ھ� */
	Von(new Coverage(){

		@SuppressWarnings("serial")
		@Override
		public ArrayList<Vector> getCoverage(int range) {
			// TODO �Զ����ɵķ������
			return new ArrayList<Vector>(){{
				add(new Vector(0,-1));
				add(new Vector(-1,0));
				add(new Vector(1,0));
				add(new Vector(0,1));
				}};
		}
		
	});
	
	
	private ArrayList<Vector> coverageVectors;
	
	private NeighbourCoverage(Coverage co){
		coverageVectors = co.getCoverage(1);
	}
	/**����ĳһ������Χ�������ھ�λ�õ�������꣬��ŵ�����ھ�Ϊ��:(0,1)(0,-1)(1,0)(-1,0)*/
	public ArrayList<Vector> getCoverageVector(){
		return  coverageVectors;
	}
	
	interface Coverage
	{
		ArrayList<Vector> getCoverage(int range);
	}
}
