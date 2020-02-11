package de.dwcode.mmfarms.blocks.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class ControllerBlock extends Block {

	public ControllerBlock(String name) {
		super(Material.IRON);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

}
