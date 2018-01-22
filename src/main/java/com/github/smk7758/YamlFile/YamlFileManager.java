package com.github.smk7758.YamlFile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class YamlFileManager {
	private final Plugin plugin;

	public YamlFileManager(Plugin plugin) {
		this.plugin = plugin;
	}

	public YamlFile reloadYamlFile(YamlFile file) {
		loadFields(file, file.getClass().getFields(), file, "");
		return file;
	}

	private void loadFields(YamlFile file_object, Field[] fields, Object parent, String parent_yaml_path) {
		try {
			boolean is_root_class = false;
			String yaml_path = "", yaml_path_access;
			if (parent_yaml_path == null || parent_yaml_path.isEmpty()) is_root_class = true;
			else yaml_path += parent_yaml_path + ".";
			for (Field field : fields) {
				SendLog.debug(field.getName() + " | " + field.getType(), Bukkit.getConsoleSender());
				if (field.isAnnotationPresent(YamlFileManagerField.class) || Modifier.isFinal(field.getModifiers())) continue;
				// TODO: 動作が怪しい。

				yaml_path_access = yaml_path + field.getName();

				if (field.getType().equals(String.class)) {
					SendLog.debug("inside!: " + yaml_path_access, Bukkit.getConsoleSender());
					field.set(parent, file_object.getFileConfiguration().getString(yaml_path_access));
					SendLog.debug(file_object.getFileConfiguration().getString(yaml_path_access), Bukkit.getConsoleSender());
				} else if (field.getType().equals(List.class) && field.getGenericType().equals(String.class)) {
					field.set(parent, file_object.getFileConfiguration().getStringList(yaml_path_access));
				} else if (field.getType().equals(int.class)) {
					field.set(parent, file_object.getFileConfiguration().getInt(yaml_path_access));
				} else {
					// 再帰的呼び出し
					SendLog.debug(field.getName() + " is not value.", Bukkit.getConsoleSender());

					// TODO: クラスオブジェクトとして読み込まれないから、そのクラスのフィールドを取得出来ないことの対処。
					Object field_object = field.getType().getConstructor(file_object.getClass()).newInstance(file_object);
					loadFields(file_object, field_object.getClass().getFields(), field_object, yaml_path_access);
				}
			}
			if (is_root_class) file_object.loadField();
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
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
