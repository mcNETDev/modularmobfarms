package de.dwcode.mmfarms;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import de.dwcode.mmfarms.blocks.ItemOutputSklaveBlock;
import de.dwcode.mmfarms.blocks.ItemOutputSklaveTile;
import de.dwcode.mmfarms.blocks.KillerSklaveBlock;
import de.dwcode.mmfarms.blocks.SimpleControllerBlock;
import de.dwcode.mmfarms.blocks.SimpleControllerTile;
import de.dwcode.mmfarms.blocks.SoulSklaveBlock;
import de.dwcode.mmfarms.blocks.SoulSklaveTile;
import de.dwcode.mmfarms.blocks.UpgradeSklaveBlock;
import de.dwcode.mmfarms.data.SklaveType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

@Mod(modid = MMFarms.MODID, name = MMFarms.NAME, version = MMFarms.VERSION, dependencies = MMFarms.DEPENDENCIES)
public class MMFarms {
	public static final String MODID = "modularmobfarms";
	public static final String NAME = "Modular Mob Farms";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "after:draconicevolution;after:mob_grinding_utils;after:enderio";

	@Mod.Instance
	public static MMFarms INSTANCE;

	private static Logger logger;

	public static final SimpleControllerBlock controller1 = new SimpleControllerBlock();
	public static final SoulSklaveBlock soul_block = new SoulSklaveBlock();
	public static final ItemOutputSklaveBlock itemOutput_block = new ItemOutputSklaveBlock();
	public static final KillerSklaveBlock killer_block = new KillerSklaveBlock();

	public static final UpgradeSklaveBlock upgrade_sharpness_block = new UpgradeSklaveBlock(UpgradeSklaveBlock.name_sharpness, SklaveType.UPGRADE_SHARPNESS);
	public static final UpgradeSklaveBlock upgrade_looting_block = new UpgradeSklaveBlock(UpgradeSklaveBlock.name_looting, SklaveType.UPGRADE_LOOTING);
	public static final UpgradeSklaveBlock upgrade_beheading_block = new UpgradeSklaveBlock(UpgradeSklaveBlock.name_beheading, SklaveType.UPGRADE_BEHEADING);

	public static final FakeSword sword = new FakeSword();

	public static ConfigManager config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		config = new ConfigManager(event.getModConfigurationDirectory());
		config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> registry) {
		// Controllers
		registry.getRegistry().register(controller1);
		// Blocks
		registry.getRegistry().register(soul_block);
		registry.getRegistry().register(itemOutput_block);
		registry.getRegistry().register(killer_block);

		// Upgrades
		registry.getRegistry().register(upgrade_sharpness_block);
		registry.getRegistry().register(upgrade_looting_block);
		registry.getRegistry().register(upgrade_beheading_block);

		GameRegistry.registerTileEntity(SimpleControllerTile.class, MODID + ":" + SimpleControllerBlock.name);

		GameRegistry.registerTileEntity(SoulSklaveTile.class, MODID + ":" + SoulSklaveBlock.name);
		GameRegistry.registerTileEntity(ItemOutputSklaveTile.class, MODID + ":" + ItemOutputSklaveBlock.name);

	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> registry) {
		// Controllers
		registry.getRegistry().register(new ItemBlock(controller1).setRegistryName(controller1.getRegistryName()));
		// Blocks
		registry.getRegistry().register(new ItemBlock(soul_block).setRegistryName(soul_block.getRegistryName()));
		registry.getRegistry().register(new ItemBlock(itemOutput_block).setRegistryName(itemOutput_block.getRegistryName()));
		registry.getRegistry().register(new ItemBlock(killer_block).setRegistryName(killer_block.getRegistryName()));

		// Upgrades
		registry.getRegistry().register(new ItemBlock(upgrade_sharpness_block).setRegistryName(upgrade_sharpness_block.getRegistryName()));
		registry.getRegistry().register(new ItemBlock(upgrade_looting_block).setRegistryName(upgrade_looting_block.getRegistryName()));
		registry.getRegistry().register(new ItemBlock(upgrade_beheading_block).setRegistryName(upgrade_beheading_block.getRegistryName()));
		registry.getRegistry().register(sword.setRegistryName("fakesword"));

	}

	public ConfigManager getConfig() {
		return config;
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if (!event.getEntity().hasCustomName()) {
			return;
		}
		if (!event.getEntity().getCustomNameTag().startsWith("DEMO")) {
			return;
		}
		String name = event.getEntity().getCustomNameTag();
		if (!name.contains(";")) {
			return;
		}
		String[] nn = name.split(";");
		if (nn.length != 4) {
			return;
		}
		int x = Integer.valueOf(nn[1]);
		int y = Integer.valueOf(nn[2]);
		int z = Integer.valueOf(nn[3]);
		BlockPos pos = new BlockPos(x, y, z);
		if (event.getEntity().world.getTileEntity(pos) == null || !(event.getEntity().world.getTileEntity(pos) instanceof SimpleControllerTile)) {
			return;
		}
		SimpleControllerTile tile = (SimpleControllerTile) event.getEntity().world.getTileEntity(pos);
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (EntityItem i : event.getDrops()) {
			items.add(i.getItem());
		}
		tile.getDemo_loot().add(items);
		event.setCanceled(true);

	}

	@SubscribeEvent
	public void onEntityDrop(LivingExperienceDropEvent event) {
		if (!event.getEntity().hasCustomName()) {
			return;
		}
		if (!event.getEntity().getCustomNameTag().startsWith("DEMO")) {
			return;
		}
		String name = event.getEntity().getCustomNameTag();
		if (!name.contains(";")) {
			return;
		}
		String[] nn = name.split(";");
		if (nn.length != 4) {
			return;
		}
		event.setDroppedExperience(0);
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(itemOutput_block), 0, new ModelResourceLocation(MODID + ":" + itemOutput_block.name, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(controller1), 0, new ModelResourceLocation(MODID + ":" + controller1.name, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(soul_block), 0, new ModelResourceLocation(MODID + ":" + soul_block.name, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(killer_block), 0, new ModelResourceLocation(MODID + ":" + killer_block.name, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(upgrade_beheading_block), 0, new ModelResourceLocation(MODID + ":" + upgrade_beheading_block.name_beheading, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(upgrade_looting_block), 0, new ModelResourceLocation(MODID + ":" + upgrade_looting_block.name_looting, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(upgrade_sharpness_block), 0, new ModelResourceLocation(MODID + ":" + upgrade_sharpness_block.name_sharpness, "inventory"));
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		addShapedRecipe(new ItemStack(controller1), "BRB", "SKG", "BOB", 'B', new ItemStack(Blocks.IRON_BLOCK), 'R', new ItemStack(Items.ROTTEN_FLESH), 'S', new ItemStack(Items.STRING), 'K', new ItemStack(Items.SKULL, 1, 1), 'G', new ItemStack(Items.GUNPOWDER), 'O', new ItemStack(Items.BONE));
		addShapedRecipe(new ItemStack(upgrade_looting_block), "LGL", "GBG", "LGL", 'L', new ItemStack(Blocks.LAPIS_BLOCK), 'G', new ItemStack(Blocks.WOOL, 1, 13), 'B', new ItemStack(Blocks.GOLD_BLOCK));
		addShapedRecipe(new ItemStack(upgrade_sharpness_block), "LGL", "GBG", "LGL", 'L', new ItemStack(Items.DIAMOND_SWORD), 'G', new ItemStack(Blocks.QUARTZ_BLOCK, 1, 0), 'B', new ItemStack(Items.GOLDEN_SWORD));
		if (Loader.isModLoaded("draconicevolution")) {
			addShapedRecipe(new ItemStack(itemOutput_block), "LGL", "GBG", "LGL", 'L', new ItemStack(Blocks.OBSIDIAN), 'G', new ItemStack(Item.getByNameOrId("minecraft:hopper")), 'B', new ItemStack(Block.getBlockFromName("draconicevolution:draconium_chest")));
			addShapedRecipe(new ItemStack(soul_block), "BBB", "BSB", "BBB", 'B', new ItemStack(Blocks.IRON_BARS), 'S', new ItemStack(Item.getByNameOrId("draconicevolution:mob_soul")));
			addShapedRecipe(new ItemStack(killer_block), "BBB", "BSB", "BBB", 'B', new ItemStack(Items.DIAMOND_SWORD), 'S', new ItemStack(Block.getBlockFromName("draconicevolution:grinder")));
		} else {
			if (Loader.isModLoaded("enderio")) {
				addShapedRecipe(new ItemStack(soul_block), "BBB", "BSB", "BBB", 'B', new ItemStack(Blocks.IRON_BARS), 'S', new ItemStack(Item.getByNameOrId("enderio:item_broken_spawner")));
			} else {
				addShapedRecipe(new ItemStack(soul_block), "BBB", "BSB", "BBB", 'B', new ItemStack(Blocks.IRON_BARS), 'S', new ItemStack(Items.SKULL, 1, 1));
			}
			if (Loader.isModLoaded("mob_grinding_utils")) {
				addShapedRecipe(new ItemStack(killer_block), "BBB", "BSB", "BBB", 'B', new ItemStack(Items.DIAMOND_SWORD), 'S', new ItemStack(Block.getBlockFromName("mob_grinding_utils:saw")));
			} else {
				addShapedRecipe(new ItemStack(killer_block), "BBB", "BBB", "BBB", 'B', new ItemStack(Items.DIAMOND_SWORD));
			}
			addShapedRecipe(new ItemStack(itemOutput_block), "LGL", "GBG", "LGL", 'L', new ItemStack(Blocks.OBSIDIAN), 'G', new ItemStack(Item.getByNameOrId("minecraft:hopper")), 'B', new ItemStack(Blocks.CHEST));
		}

	}

	private void addShapedRecipe(ItemStack output, Object... input) {
		ResourceLocation base = new ResourceLocation(MODID, output.getItem().getRegistryName().getResourcePath());
		ResourceLocation loc = base;
		int index = 0;
		while (CraftingManager.REGISTRY.containsKey(loc)) {
			index++;
			loc = new ResourceLocation(MODID, base.getResourcePath() + "_" + index);
		}
		CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(input);
		ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
		recipe.setRegistryName(loc);
		GameData.register_impl(recipe);
	}
}
