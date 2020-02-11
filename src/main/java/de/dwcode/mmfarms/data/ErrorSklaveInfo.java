package de.dwcode.mmfarms.data;

public class ErrorSklaveInfo extends SklaveInfo {
	private String info;

	public ErrorSklaveInfo(SklaveType type, String info) {
		super(type);
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public String toString() {
		return getType() + "    " + getInfo();
	}

}
