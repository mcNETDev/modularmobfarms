package de.dwcode.mmfarms.blocks;

import de.dwcode.mmfarms.blocks.init.SklaveBlock;
import de.dwcode.mmfarms.data.SklaveInfo;
import de.dwcode.mmfarms.data.SklaveType;
import de.dwcode.mmfarms.data.UpgradeSklaveInfo;

public class UpgradeSklaveBlock extends SklaveBlock {
	public static final String name_sharpness = "up_sharpness";
	public static final String name_looting = "up_looting";
	public static final String name_beheading = "up_beheading";

	private UpgradeSklaveInfo info;

	public UpgradeSklaveBlock(String name, SklaveType type) {
		super(name);
		this.info = new UpgradeSklaveInfo(type);
	}

	@Override
	public SklaveInfo getSklaveInfo() {
		return this.info;
	}

}
