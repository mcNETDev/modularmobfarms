package de.dwcode.mmfarms.blocks;

import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.blocks.init.SklaveBlock;
import de.dwcode.mmfarms.data.KillerSklaveInfo;
import de.dwcode.mmfarms.data.SklaveInfo;

public class KillerSklaveBlock extends SklaveBlock {
	public static final String name = "killer";
	private KillerSklaveInfo info;

	public KillerSklaveBlock() {
		super(name);
		this.info = new KillerSklaveInfo();
	}

	@Override
	public SklaveInfo getSklaveInfo() {
		return info;
	}

}
