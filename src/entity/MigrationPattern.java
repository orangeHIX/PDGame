package entity;

public enum MigrationPattern {

	NONE("no_migrate"), RANDOM("random_migrate"), OPTIMISTIC(
			"optimistic_migrate"), ESCAPE("escape_migrate");

	public String name;

	private MigrationPattern(String s) {
		name = s;
	}

	@Override
	public String toString() {
		return name;
	}
}
