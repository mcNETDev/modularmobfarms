package de.dwcode.mmfarms;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

	private File config;
	private boolean needRF;
	private double m_killer;
	private double m_soul;
	private double m_u_sharpness;
	private double m_u_looting;
	private double m_u_beheading;
	private int max_looting;
	private int max_beheading;
	private int max_sharpness;
	private int speedEach;
	private int simpleController_baseTime;
	private int simpleController_baseCost;
	private boolean wither_controller;
	private int killer_baseCost;
	private int soul_baseCost;
	private int sharpness_baseCost;
	private int looting_baseCost;
	private int beheading_baseCost;
	private int simpleController_minTime;

	public ConfigManager(File modConfigurationDirectory) {
		this.config = modConfigurationDirectory;
	}

	public void init(File file) {
		Configuration config = new Configuration(file);
		simpleController_baseTime = config.getInt("simpleController_baseTime", "general", 40, 1, Integer.MAX_VALUE, "default ticks for one interval to generate loot");
		simpleController_minTime = config.getInt("simpleController_minTime", "general", 5, 1, Integer.MAX_VALUE, "min ticks for one interval to generate loot");
		simpleController_baseCost = config.getInt("simpleController_baseCost", "general", 1000, 1, Integer.MAX_VALUE, "RF/t base cost to run the controller");
		needRF = config.getBoolean("simpleController_consumed_rf", "general", true, "consumed rf");
		wither_controller = config.getBoolean("disable_wither_controller", "general", false, "disable the Wither Boss Controller [WIP]");

		max_looting = config.getInt("max_looting", "general", 10, 0, 1000, "max looting upgrades allowed");
		max_beheading = config.getInt("max_beheading", "general", 10, 0, 1000, "max beheading Upgrades allowed");
		max_sharpness = config.getInt("max_sharpness", "general", 10, 0, 1000, "max sharpness(speed) upgrades allowed");
		speedEach = config.getInt("speedEachSharpness", "general", 1, 1, Integer.MAX_VALUE, "how many ticks reduce one sharpness upgrade");

		killer_baseCost = config.getInt("killer_baseCost", "costs", 1000, 1, Integer.MAX_VALUE, "baseCost for the Killer");
		m_killer = config.get("costs", "multiplier_killer", 0.1, "multiplier for the killer", 0.1, Double.MAX_VALUE).getDouble();

		soul_baseCost = config.getInt("spawner_baseCost", "costs", 1000, 1, Integer.MAX_VALUE, "baseCost for the Spawner");
		m_soul = config.get("costs", "multiplier_spawner", 0.1, "multiplier for the spawner", 0.1, Double.MAX_VALUE).getDouble();

		sharpness_baseCost = config.getInt("sharpness_baseCost", "costs", 1200, 1, Integer.MAX_VALUE, "baseCost for one sharpness upgrade");
		m_u_sharpness = config.get("costs", "multiplier_sharpness", 0.3, "multiplier for each sharpness upgrade", 0.1, Double.MAX_VALUE).getDouble();

		looting_baseCost = config.getInt("looting_baseCost", "costs", 2000, 1, Integer.MAX_VALUE, "baseCost for one looting upgrade");
		m_u_looting = config.get("costs", "multiplier_looting", 0.2, "multiplier for each looting upgrade", 0.1, Double.MAX_VALUE).getDouble();

		beheading_baseCost = config.getInt("beheading_baseCost", "costs", 1500, 1, Integer.MAX_VALUE, "baseCost for one beheading upgrade");
		m_u_beheading = config.get("costs", "multiplier_beheading", 0.5, "multiplier for each beheading upgrade", 0.1, Double.MAX_VALUE).getDouble();

		config.save();
	}

	public int getSimpleController_minTime() {
		return simpleController_minTime;
	}

	public File getConfig() {
		return config;
	}

	public boolean isNeedRF() {
		return needRF;
	}

	public double getM_killer() {
		return m_killer;
	}

	public double getM_soul() {
		return m_soul;
	}

	public double getM_u_sharpness() {
		return m_u_sharpness;
	}

	public double getM_u_looting() {
		return m_u_looting;
	}

	public double getM_u_beheading() {
		return m_u_beheading;
	}

	public int getMax_looting() {
		return max_looting;
	}

	public int getMax_beheading() {
		return max_beheading;
	}

	public int getMax_sharpness() {
		return max_sharpness;
	}

	public int getSpeedEach() {
		return speedEach;
	}

	public int getSimpleController_baseTime() {
		return simpleController_baseTime;
	}

	public int getSimpleController_baseCost() {
		return simpleController_baseCost;
	}

	public boolean isWither_controller() {
		return wither_controller;
	}

	public int getKiller_baseCost() {
		return killer_baseCost;
	}

	public int getSoul_baseCost() {
		return soul_baseCost;
	}

	public int getSharpness_baseCost() {
		return sharpness_baseCost;
	}

	public int getLooting_baseCost() {
		return looting_baseCost;
	}

	public int getBeheading_baseCost() {
		return beheading_baseCost;
	}

}
