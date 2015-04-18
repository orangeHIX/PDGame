package entity;

public enum StrategyPattern {
	TWO("two_strategy"), THREE("three_strategy"), FIVE("five_strategy"), CONTINUOUS(
			"continuous_strategy");

	public String name;

	private StrategyPattern(String s) {
		name = s;
	}

	@Override
	public String toString() {
		return name;
	}
}
