package de.dwcode.mmfarms.data;

public class KillerSklaveInfo extends SklaveInfo {

	public KillerSklaveInfo() {
		super(SklaveType.KILLER);
	}

	@Override
	public String toString() {
		return getType() + "";
	}

}
