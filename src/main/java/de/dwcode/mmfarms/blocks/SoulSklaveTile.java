package de.dwcode.mmfarms.blocks;

import de.dwcode.mmfarms.blocks.init.SklaveTile;
import de.dwcode.mmfarms.data.SklaveInfo;
import de.dwcode.mmfarms.data.SoulSklaveInfo;
import net.minecraft.nbt.NBTTagCompound;

public class SoulSklaveTile extends SklaveTile {

	private SoulSklaveInfo info;

	public SoulSklaveTile() {
		info = new SoulSklaveInfo();
	}

	public void setSoul(String soulname) {
		info.setSoul(soulname);
	}

	@Override
	public SklaveInfo getSklaveInfo() {
		return info;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (info.getSoul() != null) {
			tag.setString("soul", info.getSoul());
		}
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("soul")) {
			info.setSoul(tag.getString("soul"));
		}
	}
}
