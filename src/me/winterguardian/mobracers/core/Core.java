package me.winterguardian.core;

import me.winterguardian.core.bungee.BungeeMessager;
import me.winterguardian.core.entity.custom.CustomEntityManager;
import me.winterguardian.core.entity.custom.CustomEntityType;
import me.winterguardian.core.gameplay.GameplayManager;
import me.winterguardian.core.playerstats.UserDatasManager;
import me.winterguardian.core.shop.Shop;
import me.winterguardian.core.world.Wand;
import org.bukkit.plugin.Plugin;

public class Core
{
	//components
	private static UserDatasManager userDatasManager = new UserDatasManager();
	private static BungeeMessager bungeeMessager = new BungeeMessager();
	private static GameplayManager gameplayManager = new GameplayManager();
	private static CustomEntityManager customEntityManager = new CustomEntityManager();
	private static Shop shop = new Shop();
	private static Wand wand = new Wand();

	@Deprecated
	@SuppressWarnings("deprecation")
	public static void enableAsyncCommands(Plugin plugin)
	{

	}

	public static void disable(Plugin plugin)
	{
		getUserDatasManager().disable(plugin);
		getBungeeMessager().disable(plugin);
		getGameplayManager().disable(plugin);
		getCustomEntityManager().disable(plugin);
		getWand().disable(plugin);
	}


	@Deprecated
	@SuppressWarnings("deprecation")
	public static void enableCustomEntities()
	{
		CustomEntityType.registerEntities();
	}

	public static UserDatasManager getUserDatasManager()
	{
		return userDatasManager;
	}

	public static BungeeMessager getBungeeMessager()
	{
		return bungeeMessager;
	}

	public static GameplayManager getGameplayManager()
	{
		return gameplayManager;
	}

	public static CustomEntityManager getCustomEntityManager() { return customEntityManager; }

	public static Shop getShop()
	{
		return shop;
	}

	public static Wand getWand()
	{
		return wand;
	}
}
