package de.dwcode.mmfarms.data;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class ItemOutputSklaveInfo extends SklaveInfo {

	private BlockPos pos;

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public ItemOutputSklaveInfo() {
		super(SklaveType.ITEM_OUT);
	}

	@Override
	public String toString() {
		return getType() + "    " + getPos().toString();
	}

}
