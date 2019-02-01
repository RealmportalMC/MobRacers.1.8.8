package me.winterguardian.core.command;

import java.util.List;

import org.bukkit.permissions.Permission;

public interface CommandData
{
	String getName();
	Permission getPermission();
	String getPermissionMessage();
	List<String> getAliases();
	String getUsage();
	String getDescription();
}
