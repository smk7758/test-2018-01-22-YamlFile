package com.github.smk7758.YamlFile;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class YamlFile {
	protected FileConfiguration config = null;
	protected File config_file = null;

	public YamlFile(Plugin plugin) {
		config_file = new File(plugin.getDataFolder(), getFileName());
	}

	/**
	 * @return with file extension: .yml
	 */
	public abstract String getFileName();

	public abstract void loadField();

	public abstract void saveField();

	public FileConfiguration getFileConfiguration() {
		return config != null ? config : reloadFileConfiguration();
	}

	public FileConfiguration reloadFileConfiguration() {
		return (config = YamlConfiguration.loadConfiguration(config_file));
	}
}
