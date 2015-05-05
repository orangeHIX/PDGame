package rule;

public enum StrategyPattern {
	TWO("two_strategy",2), THREE("three_strategy",3), FIVE("five_strategy",5), CONTINUOUS(
			"continuous_strategy",11);

	public String name;
	public int strategyNum;
	private StrategyPattern(String s, int i) {
		name = s;
		strategyNum = i;
	}
	
	public int getStrategyNum(){
		return strategyNum;
	}

	@Override
	public String toString() {
		return name;
	}
}
