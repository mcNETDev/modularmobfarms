package de.dwcode.mmfarms.blocks;

import java.util.ArrayList;

import de.dwcode.mmfarms.blocks.init.ControllerTile;
import de.dwcode.mmfarms.blocks.init.SklaveTile;
import de.dwcode.mmfarms.data.ItemOutputSklaveInfo;
import de.dwcode.mmfarms.data.SklaveInfo;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemOutputSklaveTile extends SklaveTile {
	private ItemOutputSklaveInfo info;
	private ControllerTile controller;
	private BlockPos pos_controller;

	public ItemOutputSklaveTile() {
		info = new ItemOutputSklaveInfo();
	}

	public boolean isControllerActive() {
		if (world.getTileEntity(pos_controller) != null && world.getTileEntity(pos_controller) instanceof ControllerTile) {
			return true;
		} else {
			return false;
		}
	}

	public ControllerTile getController() {
		return controller;
	}

	public void setController(ControllerTile controller) {
		this.controller = controller;
		setPos_controller(controller.getPos());
	}

	public BlockPos getPos_controller() {
		return pos_controller;
	}

	public void setPos_controller(BlockPos pos_controller) {
		this.pos_controller = pos_controller;
	}

	@Override
	public SklaveInfo getSklaveInfo() {
		return info;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	public void output(ArrayList<ItemStack> items) {
		for (EnumFacing f : EnumFacing.VALUES) {
			if (items.isEmpty()) {
				continue;
			}
			if (world.getTileEntity(getPos().add(f.getDirectionVec())) != null) {
				TileEntity t = world.getTileEntity(getPos().add(f.getDirectionVec()));
				IItemHandler handler = t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				if (handler == null) {
					continue;
				}
				ArrayList<ItemStack> next = new ArrayList<ItemStack>();

				for (ItemStack s : items) {
					for (int i = 0; i < handler.getSlots(); i++) {
						if (s.isEmpty())
							continue;
						if (handler.isItemValid(i, s)) {
							ItemStack over = handler.insertItem(i, s, false);
							s = over;
						}
					}
					if (!s.isEmpty()) {
						next.add(s);
					}
				}
				items = next;
				t.markDirty();
			}
		}
	}

}
