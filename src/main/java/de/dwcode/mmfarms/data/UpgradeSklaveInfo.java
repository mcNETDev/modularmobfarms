package de.dwcode.mmfarms.data;

public class UpgradeSklaveInfo extends SklaveInfo {

	public UpgradeSklaveInfo(SklaveType type) {
		super(type);
	}

	@Override
	public String toString() {
		return getType() + "";
	}

}
