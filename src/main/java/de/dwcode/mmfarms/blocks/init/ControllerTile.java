package de.dwcode.mmfarms.blocks.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.dwcode.mmfarms.ConfigManager;
import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.blocks.ItemOutputSklaveTile;
import de.dwcode.mmfarms.data.ErrorSklaveInfo;
import de.dwcode.mmfarms.data.ItemOutputSklaveInfo;
import de.dwcode.mmfarms.data.KillerSklaveInfo;
import de.dwcode.mmfarms.data.SklaveInfo;
import de.dwcode.mmfarms.data.SklaveType;
import de.dwcode.mmfarms.data.SoulSklaveInfo;
import de.dwcode.mmfarms.data.UpgradeSklaveInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class ControllerTile extends TileEntity implements ICapabilityProvider {

	private ItemOutputSklaveTile item_output;

	private List<SklaveInfo> latestInfo = new ArrayList();
	private List<ErrorSklaveInfo> errors = new ArrayList();

	private boolean canRUN = false;

	private int looting;

	private int sharpness;
	private int beheading;

	private ResourceLocation soul;
	private boolean hasSpawner;
	private boolean haskiller;

	private boolean running;

	private IEnergyStorage energy;

	public ControllerTile() {
		energy = new EnergyStorage(1000000);
	}

	public void updateSklaveInfo() {
		errors.clear();
		ArrayList<SklaveInfo> info = new ArrayList<SklaveInfo>();
		Stack<BlockPos> poss = new Stack<BlockPos>();
		ArrayList<Long> dawarichschon = new ArrayList<Long>();
		for (EnumFacing fac : EnumFacing.VALUES) {
			poss.add(getPos().add(fac.getDirectionVec()));
		}
		dawarichschon.add(getPos().toLong());
		while (!poss.isEmpty()) {
			boolean skip = false;
			BlockPos pos = poss.pop();
			if (dawarichschon.contains(pos.toLong())) {
				skip = true;
				// Dawarichschon
			} else {
				dawarichschon.add(pos.toLong());
			}
			Block b = world.getBlockState(pos).getBlock();
			if (b instanceof ControllerBlock && !pos.equals(getPos())) {
				if (!skip)
					info.add(new ErrorSklaveInfo(SklaveType.TOMANYCONTROLLERS, "Found another Controller!!"));
			}
			if (!(b instanceof SklaveBlock)) {
				continue;
			}
			SklaveBlock sb = (SklaveBlock) b;
			if (sb.getSklaveInfo() != null) {
				if (!skip)
					info.add(sb.getSklaveInfo());
			} else {
				if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof SklaveTile) {
					SklaveTile tile = (SklaveTile) world.getTileEntity(pos);
					if (!skip) {
						if (tile.getSklaveInfo() != null) {
							if (tile.getSklaveInfo() instanceof ItemOutputSklaveInfo) {
								ItemOutputSklaveInfo ii = (ItemOutputSklaveInfo) tile.getSklaveInfo();
								ii.setPos(tile.getPos());
								info.add(ii);
							} else {
								info.add(tile.getSklaveInfo());

							}
						}
					}
				}
			}

			for (EnumFacing fac : EnumFacing.VALUES) {
				if (dawarichschon.contains(pos.add(fac.getDirectionVec()).toLong())) {
					// Dawarichschon
				} else {
					poss.add(pos.add(fac.getDirectionVec()));
				}
			}

		}
		latestInfo = info;
		initController();
	}

	public void initController() {
		ConfigManager c = MMFarms.INSTANCE.getConfig();
		canRUN = false;
		errors.clear();
		looting = 0;
		boolean tomanylooting = false;
		sharpness = 0;
		boolean tomannysharpness = false;
		beheading = 0;
		boolean tomannybeheading = false;
		haskiller = false;
		soul = null;
		hasSpawner = false;
		item_output = null;
		/*
		 * An Sachen die gedacht werden müssen
		 * 
		 * 1. Maximal 1 Item Output 2. Spawner cooldown setzen
		 */
		boolean foundItemOutput = false;
		boolean foundSoul = false;
		for (SklaveInfo i : getLatestInfo()) {
			if (i instanceof SoulSklaveInfo) {
				if (foundSoul) {
					errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "Found more than one Spawner"));
					continue;
				} else {
					hasSpawner = true;
					foundSoul = true;
					if (((SoulSklaveInfo) i).getSoul() == null) {
						errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "No Soul in Spawner"));
						continue;
					}
					String soul = ((SoulSklaveInfo) i).getSoul();
					if (!soul.contains(":")) {
						errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "Wrong Soul (report this to GitHub)"));
						continue;
					}
					String[] mob = soul.split(":");
					ResourceLocation loc = new ResourceLocation(mob[0], mob[1]);
					this.soul = loc;
				}
			} else if (i instanceof ItemOutputSklaveInfo) {
				if (foundItemOutput) {
					errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "Found more than one Item Output"));
					continue;
				}
				ItemOutputSklaveInfo info = (ItemOutputSklaveInfo) i;
				if (world.getTileEntity(info.getPos()) != null && world.getTileEntity(info.getPos()) instanceof ItemOutputSklaveTile) {
					item_output = (ItemOutputSklaveTile) world.getTileEntity(info.getPos());
					item_output.setController(this);
					foundItemOutput = true;
				}
			} else if (i instanceof ErrorSklaveInfo) {
				errors.add((ErrorSklaveInfo) i);
			} else if (i instanceof UpgradeSklaveInfo) {
				switch (i.getType()) {
				case UPGRADE_SHARPNESS:
					if (sharpness >= c.getMax_sharpness()) {
						tomannysharpness = true;
					}
					sharpness++;
					break;
				case UPGRADE_LOOTING:
					if (looting >= c.getMax_looting()) {
						tomanylooting = true;
					}
					looting++;
					break;
				case UPGRADE_BEHEADING:
					if (beheading >= c.getMax_beheading()) {
						tomannybeheading = true;
					}
					beheading++;
					break;
				default:
					break;
				}
			} else if (i instanceof KillerSklaveInfo) {
				haskiller = true;
			}

		}
		if (!haskiller) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "No Killer found!"));
		}
		if (!hasSpawner) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "No Spawner found!"));
		}
		if (item_output == null) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "No Item Output found!"));
		}
		if (tomannybeheading) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "To many beheading Upgrades " + beheading + "/" + c.getMax_beheading()));
		}
		if (tomannysharpness) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "To many sharpness Upgrades " + sharpness + "/" + c.getMax_sharpness()));
		}
		if (tomanylooting) {
			errors.add(new ErrorSklaveInfo(SklaveType.ERROR, "To many looting Upgrades " + looting + "/" + c.getMax_looting()));
		}
		if (!errors.isEmpty()) {
			canRUN = false;
			return;
		}
		canRUN = true;
	}

	public int calculateRF() {
		ConfigManager c = MMFarms.INSTANCE.getConfig();
		if (!c.isNeedRF()) {
			return 0;
		}
		int base = c.getSimpleController_baseCost();
		if (haskiller)
			base += c.getKiller_baseCost() * c.getM_killer();
		if (soul != null)
			base += c.getSoul_baseCost() * c.getM_soul();
		base += c.getSharpness_baseCost() * c.getM_u_sharpness() * sharpness;
		base += c.getLooting_baseCost() * c.getM_u_looting() * looting;
		base += c.getBeheading_baseCost() * c.getM_u_beheading() * beheading;
		return base;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energy);
		}
		return super.getCapability(capability, facing);

	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}

		return super.hasCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("energy", energy.getEnergyStored());

		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("energy"))
			energy.receiveEnergy(tag.getInteger("energy"), false);

	}

	public void useItems(ArrayList<ItemStack> items) {
		if (item_output != null) {
			item_output.output(items);
		}
	}

	private void stop() {
		running = false;
	}

	public ItemOutputSklaveTile getItemOutput() {
		return item_output;
	}

	public int getLooting() {
		return looting;
	}

	public int getSharpness() {
		return sharpness;
	}

	public void setSharpness(int sharpness) {
		this.sharpness = sharpness;
	}

	public int getBeheading() {
		return beheading;
	}

	public IEnergyStorage getEnergy() {
		return energy;
	}

	public boolean hasKiller() {
		return haskiller;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean canRun() {
		return canRUN;
	}

	public ResourceLocation getSoul() {
		return soul;
	}

	public List<ErrorSklaveInfo> getErrors() {
		return errors;
	}

	public List<SklaveInfo> getLatestInfo() {
		return latestInfo;
	}
}
