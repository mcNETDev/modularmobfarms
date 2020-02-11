package de.dwcode.mmfarms.blocks;

import de.dwcode.mmfarms.ConfigManager;
import de.dwcode.mmfarms.MMFarms;
import de.dwcode.mmfarms.blocks.init.ControllerTile;
import de.dwcode.mmfarms.data.ErrorSklaveInfo;
import de.dwcode.mmfarms.data.SklaveInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;

import static de.dwcode.mmfarms.S.s;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

public class SimpleControllerTile extends ControllerTile implements ITickable {

	private ArrayList<ArrayList<ItemStack>> demo_loot = new ArrayList<ArrayList<ItemStack>>();
	private Random rnd = new Random();
	private int ticks = 40;

	private boolean run = false;

	public void sendInfoToPlayer(EntityPlayer player) {
		ConfigManager c = MMFarms.INSTANCE.getConfig();
		updateSklaveInfo();
		player.sendMessage(s("§8---------- §bController Info §8----------"));
		player.sendMessage(s("§7Assembled: " + (canRun() ? "§atrue" : "§cfalse")));
		player.sendMessage(s("§7RF/t: " + calculateRF()));
		player.sendMessage(s("§7Loot Time(t): " + ticks));
		player.sendMessage(s("§7Soul: §6" + (getSoul() == null ? "" : getSoul())));
		player.sendMessage(s("§7Upgrades:"));
		player.sendMessage(s("§7 Sharpness (Speed): " + getSharpness() + "/" + c.getMax_sharpness()));
		player.sendMessage(s("§7 Looting: " + getLooting() + "/" + c.getMax_looting()));
		player.sendMessage(s("§7 Beheading[WIP]: " + getBeheading() + "/" + c.getMax_beheading()));

		if (!canRun()) {
			player.sendMessage(s("§8----------§b     Errors      §8----------"));
			for (ErrorSklaveInfo i : getErrors()) {
				player.sendMessage(s("§c" + i.getInfo()));
			}
		}

	}

	@Override
	public void updateSklaveInfo() {
		ConfigManager c = MMFarms.INSTANCE.getConfig();
		super.updateSklaveInfo();
		if (canRun()) {
			generateDemoLoot();
			if (getSharpness() > c.getSimpleController_baseTime() - c.getSimpleController_minTime())
				setSharpness(c.getSimpleController_baseTime() - c.getSimpleController_minTime());
			if (getSharpness() == 0) {
				ticks = c.getSimpleController_baseTime();
			} else {
				ticks = c.getSimpleController_baseTime() - (c.getSpeedEach() * getSharpness());
				if (ticks < c.getSimpleController_minTime()) {
					ticks = c.getSimpleController_minTime();
				}
			}
		}
		if (canRun()) {
			rfpt = calculateRF();
			run = true;
		} else {
			run = false;
		}
	}

	public ArrayList<ArrayList<ItemStack>> getDemo_loot() {
		return demo_loot;
	}

	public void setDemo_loot(ArrayList<ArrayList<ItemStack>> demo_loot) {
		this.demo_loot = demo_loot;
	}

	/**
	 * 
	 * IF COFH is installed Change Config common.cfg # If TRUE, death messages are
	 * displayed for any named entity. [default: true]
	 * B:EnableGenericDeathMessage=false
	 * 
	 */
	private void generateDemoLoot() {
		demo_loot.clear();
		if (!canRun()) {
			return;
		}
		EntityPlayerMP fakePlayer = FakePlayerFactory.get((WorldServer) world, new GameProfile(UUID.nameUUIDFromBytes(new TextComponentTranslation("fakeplayer.modularmobfarms").getFormattedText().getBytes()), new TextComponentTranslation("fakeplayer.modularmobfarms").getFormattedText()));
		fakePlayer.setPosition(this.pos.getX(), -100D, this.pos.getZ());

		for (int i = 0; i < 100; i++) {
			BlockPos up = getPos().add(0, 200, 0);
			Entity e = EntityList.createEntityByIDFromName(getSoul(), world);
			if (e == null) {
				System.out.println("Entity creation failed");
				return;
			}
			e.setPositionAndRotation(up.getX(), up.getY(), up.getZ(), 0, 0);

			EntityLiving entityliving = e instanceof EntityLiving ? (EntityLiving) e : null;
			e.setLocationAndAngles(up.getX(), up.getY(), up.getZ(), 0, 0);
			entityliving.onInitialSpawn(world.getDifficultyForLocation(up), null);
			e.setSilent(true);
			e.setNoGravity(true);
			e.setInvisible(true);
			AnvilChunkLoader.spawnEntity(e, world);
			e.setCustomNameTag("DEMO;" + getPos().getX() + ";" + getPos().getY() + ";" + getPos().getZ());

			ItemStack fs = new ItemStack(MMFarms.sword, 1, 0);
			if (!fs.hasTagCompound()) {
				fs.setTagCompound(new NBTTagCompound());
			}
			fs.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), 100);
			if (getLooting() > 0) {
				fs.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), getLooting());
			}
			fs.getTagCompound().setInteger("beheadingValue", getBeheading());
			fakePlayer.setHeldItem(EnumHand.MAIN_HAND, fs);
			fakePlayer.resetCooldown();
			fakePlayer.attackTargetEntityWithCurrentItem(e);
			fakePlayer.resetCooldown();
		}
	}

	public ArrayList<ItemStack> getRandomLoot() {
		return demo_loot.get(rnd.nextInt(demo_loot.size()));
	}

	private int tick = 0;
	private int rfpt = 1000;
	private int runtimer = 0;

	@Override
	public void update() {
		if (!run) {
			runtimer++;
			if (runtimer >= 100) {
				runtimer = 0;
				updateSklaveInfo();
			}

			return;
		}
		if (getEnergy().getEnergyStored() < rfpt) {
			return;
		}
		tick++;
		getEnergy().extractEnergy(rfpt, false);
		if (tick == ticks) {
			tick = 0;
			useItems(getRandomLoot());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		run = false;
	}
}
