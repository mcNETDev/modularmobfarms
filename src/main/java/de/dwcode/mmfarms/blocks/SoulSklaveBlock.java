package de.dwcode.mmfarms.blocks;

import java.util.List;

import de.dwcode.mmfarms.blocks.init.SklaveBlock;
import de.dwcode.mmfarms.data.SklaveInfo;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class SoulSklaveBlock extends SklaveBlock implements ITileEntityProvider {
	public static final String name = "soulsklaveblock";

	public SoulSklaveBlock() {
		super(name);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (player.getHeldItemMainhand() != null && !player.getHeldItemMainhand().hasTagCompound()) {
			return true;
		}
		NBTTagCompound tag = player.getHeldItemMainhand().getTagCompound();
		if (!tag.hasKey("Animal", Constants.NBT.TAG_COMPOUND)) {
			return true;
		}
		NBTTagCompound an = tag.getCompoundTag("Animal");
		if (an.hasKey("id")) {
			String id = an.getString("id");
			if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof SoulSklaveTile) {
				((SoulSklaveTile) world.getTileEntity(pos)).setSoul(id);
				player.sendMessage(new TextComponentString("§7Mob set to: §6" + id));
				player.sendMessage(new TextComponentString("§7Right click the Controller to activate!"));
			}
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Right Click with a Golden Lasso or Cursed Lasso to set the Mob Type");

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
		return new SoulSklaveTile();
	}
}
