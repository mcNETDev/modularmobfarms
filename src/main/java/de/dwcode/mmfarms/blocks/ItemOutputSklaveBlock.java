package de.dwcode.mmfarms.blocks;

import java.util.List;

import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.blocks.init.SklaveBlock;
import de.dwcode.mmfarms.data.SklaveInfo;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemOutputSklaveBlock extends SklaveBlock implements ITileEntityProvider {
	public static final String name = "itemoutputsklave";

	public ItemOutputSklaveBlock() {
		super(name);
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Automatically eject items in inventorys");
	}

	@Override
	public SklaveInfo getSklaveInfo() {
		return null;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ItemOutputSklaveTile();
	}
}
