package com.github.smk7758.YamlFile;

import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.Plugin;

public class GameFile extends YamlFile {
	private final String file_name = "Game.yml";
	public String GameName;
	public int GameLength;
	@YamlFileManagerField(false)
	public List<HashMap<String, String>> Players;
	public Book Book = new Book();

	public class Book {
		public String Name, Title;
		public List<String> Lore, Pages;
	}

	public GameFile(Plugin plugin) {
		super(plugin);
	}

	@Override
	public String getFileName() {
		return file_name;
	}

	@Override
	public void loadField() {
		// Players = (List<HashMap<String, String>>) config.getList(Players.getClass().getName());
		// TODO
		// SendLog.info("LOADING...");
		// SendLog.info(file_name);
		// SendLog.info(GameName);
		// SendLog.info(String.valueOf(GameLength));
		// SendLog.info(Book.Name);
		// SendLog.info(Book.Title);
	}

	@Override
	public void saveField() {
	}
}
