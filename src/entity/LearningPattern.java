package entity;

public enum LearningPattern {
	MAXPAYOFF("max_payoff_learning"), FERMI("fermi_learning");
	public String name;
	private LearningPattern(String s){
		name = s;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
