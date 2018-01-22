package com.github.smk7758.YamlFile;

import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class YamlFileManager {
	private final Plugin plugin;

	public YamlFileManager(Plugin plugin) {
		this.plugin = plugin;
	}

	public YamlFile reloadYamlFile(YamlFile file) {
		loadFields(file, file.getClass().getFields(), "");
		return file;
	}

	private void loadFields(YamlFile file_object, Field[] fields, String parent_yaml_path) {
		try {
			boolean is_root_class = false;
			String yaml_path = "";
			if (parent_yaml_path == null || parent_yaml_path.isEmpty()) is_root_class = true;
			else yaml_path += parent_yaml_path + ".";
			for (Field field : fields) {
				SendLog.debug(field.getName() + " | " + field.getType(), Bukkit.getConsoleSender());
				if (field.isAnnotationPresent(YamlFileManagerField.class) || Modifier.isFinal(field.getModifiers())) continue;
				// TODO: 動作が怪しい。

				yaml_path += field.getName();

				if (field.getType().equals(String.class)) {
					SendLog.debug("inside!: " + yaml_path, Bukkit.getConsoleSender());
					field.set(file_object, file_object.getFileConfiguration().getString(yaml_path));
					SendLog.debug(file_object.getFileConfiguration().getString(yaml_path), Bukkit.getConsoleSender());
				} else if (field.getType().equals(List.class) && field.getGenericType().equals(String.class)) {
					field.set(file_object, file_object.getFileConfiguration().getStringList(yaml_path));
				} else if (field.getType().equals(int.class)) {
					field.set(file_object, file_object.getFileConfiguration().getInt(yaml_path));
				} else {
					// 再帰的呼び出し
					SendLog.debug(field.getName() + " is not value.", Bukkit.getConsoleSender());

					// TODO: クラスオブジェクトとして読み込まれないから、そのクラスのフィールドを取得出来ないことの対処。
					// loadFields(file_object, c.getFields(), yaml_path);
				}
			}
			if (is_root_class) file_object.loadField();
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	public void saveYamlFile(YamlFile file) {
		saveFields(file, file.getClass().getFields(), "");
	}

	private void saveFields(YamlFile file_object, Field[] fields, String parent_yaml_path) {
		boolean is_root_class = false;
		String yaml_path = "";
		if (parent_yaml_path == null || parent_yaml_path.isEmpty()) is_root_class = true;
		else yaml_path += parent_yaml_path + ".";
		for (Field field : file_object.getClass().getFields()) {
			if (field.isAnnotationPresent(YamlFileManagerField.class)) continue;
			yaml_path += field.getName();
			if (field.getType().equals(String.class)
					|| field.getType().equals(List.class) && field.getGenericType().equals(String.class)
					|| field.getType().equals(int.class)) {
				file_object.getFileConfiguration().set(yaml_path, field);
			} else {
				// 再帰的呼び出し
				// saveFields(file_object, field.getClass().getFields(), yaml_path);
				// TODO
			}
		}
		if (is_root_class) file_object.saveField();
	}

	public void saveDefaultYamlFile(YamlFile file) {
		plugin.saveResource(file.getFileName(), false);
	}
}
