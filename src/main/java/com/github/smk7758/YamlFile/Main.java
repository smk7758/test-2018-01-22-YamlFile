package com.github.smk7758.YamlFile;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static final String plugin_name = "YamlFile";
	public static boolean debug_mode = false;
	private CommandExecuter command_executer = new CommandExecuter(this);
	public GameFile game_file = null;

	@Override
	public void onEnable() {
		if (!Main.plugin_name.equals(getDescription().getName())) getPluginLoader().disablePlugin(this);
		// getServer().getPluginManager().registerEvents(command_listner, this);
		// getCommand(plugin_name).setExecutor(command_executer);
		game_file = new GameFile(this);
		game_file.saveDefaultYamlFile(game_file);
		getConfig();
	}

	@Override
	public void onDisable() {
	}

	public CommandExecuter getCommandExecuter() {
		return command_executer;
	}
}
