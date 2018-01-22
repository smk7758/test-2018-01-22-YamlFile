package com.github.smk7758.YamlFile;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExecuter implements CommandExecutor {
	public Main main = null;

	public CommandExecuter(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("YamlFile")) {
			main.game_file.reloadYamlFile(main.game_file);
			return true;
		}
		return false;
	}
}
