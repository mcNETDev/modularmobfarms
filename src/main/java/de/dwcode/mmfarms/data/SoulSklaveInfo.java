package de.dwcode.mmfarms.data;

public class SoulSklaveInfo extends SklaveInfo {

	private String soul;

	public SoulSklaveInfo() {
		super(SklaveType.SOUL);
	}

	public String getSoul() {
		return soul;
	}

	public void setSoul(String soul) {
		this.soul = soul;
	}

	@Override
	public String toString() {
		return getType() + "    " + getSoul();
	}

}
