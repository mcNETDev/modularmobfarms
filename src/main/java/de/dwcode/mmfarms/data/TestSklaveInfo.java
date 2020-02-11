package de.dwcode.mmfarms.data;

public class TestSklaveInfo extends SklaveInfo {

	private String text;

	public TestSklaveInfo(SklaveType type, String text) {
		super(type);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getType() + "    " + getText();
	}

}
