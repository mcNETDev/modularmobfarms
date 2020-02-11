package de.dwcode.mmfarms.blocks;

import java.util.List;

import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.blocks.init.ControllerBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimpleControllerBlock extends ControllerBlock implements ITileEntityProvider {
	public static final String name = "simple_controller";

	public SimpleControllerBlock() {
		super(name);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof SimpleControllerTile)) {
			return true;
		}
		SimpleControllerTile tile = (SimpleControllerTile) world.getTileEntity(pos);
		tile.sendInfoToPlayer(player);

		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("The Brain of a Modular Mobfarm");
		tooltip.add("Requires 1 Killer, Spawner and Item Output");
		tooltip.add("Maximum " + MMFarms.config.getMax_sharpness() + " Sharpness (Speed) Upgrades");
		tooltip.add("Maximum " + MMFarms.config.getMax_looting() + " Looting Upgrades");
		tooltip.add("Maximum " + MMFarms.config.getMax_beheading() + " Beheading Upgrades");
		tooltip.add("Activate with a right click.");
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SimpleControllerTile();
	}

}
