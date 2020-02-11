package de.dwcode.mmfarms.data;

public abstract class SklaveInfo {
	private SklaveType type;

	public SklaveInfo(SklaveType type) {
		this.type = type;
	}

	public SklaveType getType() {
		return type;
	}

	public abstract String toString();
}
