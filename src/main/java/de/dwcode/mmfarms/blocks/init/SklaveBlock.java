package de.dwcode.mmfarms.blocks.init;

import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.data.SklaveInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class SklaveBlock extends Block {

	public SklaveBlock(String name) {
		super(Material.IRON);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

	/**
	 * 
	 * @return can be null, when null, tileentity must exists
	 */
	public abstract SklaveInfo getSklaveInfo();

}
