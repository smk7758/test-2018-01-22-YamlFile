package com.github.smk7758.YamlFile;

import java.awt.List;
import java.lang.reflect.Field;

import org.bukkit.plugin.Plugin;

public abstract class YamlFileManager {
	private final Plugin plugin;

	public YamlFileManager(Plugin plugin) {
		this.plugin = plugin;
	}

	public YamlFile reloadYamlFile(YamlFile file) {
		loadFields(file, file.getClass().getFields());
		return file;
	}

	private void loadFields(YamlFile file_object, Field[] fields, Field... fields_parent) {
		try {
			for (Field field : fields) {
				if (field.isAnnotationPresent(YamlFileManagerField.class)) break;
				String yaml_path = "";
				if (fields_parent != null) {
					for (Field field_parent : fields_parent) {
						yaml_path += field_parent.getName() + ".";
					}
				}
				yaml_path += field.getName();
				if (field.getType().equals(String.class)) {
					field.set(file_object, getString(yaml_path));
				} else if (field.getType().equals(List.class) && field.getGenericType().equals(String.class)) {
					field.set(file_object, getStringList(yaml_path));
				} else if (field.getType().equals(int.class)) {
					field.set(file_object, getInt(yaml_path));
				} else {
					loadFields(file_object, field.getClass().getFields(), field);
				}
			}
			loadField();
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Please override this and use for your own type field.
	 */
	protected abstract void loadField();

	public void saveYamlFile(YamlFile file) {
		saveFields(file);
	}

	private void saveFields(YamlFile file_object) {
		// TODO
		for (Field field : file_object.getClass().getFields()) {
			if (field.isAnnotationPresent(YamlFileManagerField.class)) break;
			if (field.getType().equals(String.class)
					|| field.getType().equals(List.class) && field.getGenericType().equals(String.class)
					|| field.getType().equals(int.class)) {
				config.set(field.getName(), field);
			} else {
				// TODO
				// saveFields();
			}
		}
		saveField();
	}

	/**
	 * Please override this and use for your own type field.
	 */
	protected abstract void saveField();

	public void saveDefaultYamlFile(YamlFile file) {
		plugin.saveResource(file.getFileName() + ".yml", false);
	}
}
