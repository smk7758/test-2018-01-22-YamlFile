package com.github.smk7758.YamlFile;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class GameFile extends YamlFileManager implements YamlFile {
	private final String file_name = "Game.yml";
	private FileConfiguration config = null;
	private File config_file = null;
	public String GameName;
	public int GameLength;
	@YamlFileManagerField(false)
	public List<HashMap<String, String>> Players;
	public Book Book; // TODO

	public class Book {
		public String Name, Title;
		public List<String> Lore, Pages;
	}

	public GameFile(Plugin plugin) {
		super(plugin);
		config_file = new File(file_name);
		config = YamlConfiguration.loadConfiguration(config_file);
		final InputStream defConfigStream = plugin.getResource(file_name);
		if (defConfigStream == null) {
			return;
		}
		config.setDefaults(
				YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
	}

	@Override
	public String getFileName() {
		return file_name;
	}

	@Override
	public void loadField() {
		// Players = (List<HashMap<String, String>>) config.getList(Players.getClass().getName());
		// TODO
		SendLog.info("LOADING...");
		SendLog.info(file_name);
		SendLog.info(GameName);
		SendLog.info(String.valueOf(GameLength));
		SendLog.info(Book.Name);
		SendLog.info(Book.Title);
	}

	@Override
	protected void saveField() {
		// TODO 自動生成されたメソッド・スタブ
	}
}
