package me.winterguardian.mobracers;

import java.io.File;

import me.winterguardian.core.Core;
import me.winterguardian.core.world.Wand;
import me.winterguardian.mobracers.command.VersionSubCommand;
import me.winterguardian.mobracers.item.types.CloudItem;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.pluginsupport.MobRacersHook;
import me.winterguardian.mobracers.pluginsupport.VaultSupport;
import me.winterguardian.mobracers.stats.ArenaStats;

import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.stats.CourseStats;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MobRacersPlugin extends JavaPlugin
{
	public static final Permission PLAY = new Permission("MobRacers.play", "MobRacers regular access", PermissionDefault.TRUE);
	public static final Permission VOTE = new Permission("MobRacers.vote", "MobRacers permission to vote for an arena", PermissionDefault.TRUE);
	public static final Permission VIP = new Permission("MobRacers.vip", "MobRacers complete Vip access", PermissionDefault.OP);
	public static final Permission STAFF = new Permission("MobRacers.staff", "MobRacers Mod/Staff access", PermissionDefault.OP);
	public static final Permission CMD_PROTECT_BYPASS = new Permission("MobRacers.command-protect-bypass", "MobRacers permission to type any command in-game", PermissionDefault.OP);
	public static final Permission AUTOJOIN_BYPASS = new Permission("MobRacers.autojoin-bypass", "MobRacers permission to bypass autojoin/teleportjoin feature.", PermissionDefault.FALSE);
	public static final Permission ADMIN = new Permission("MobRacers.admin", "MobRacers Admin/Owner access", PermissionDefault.OP);
	public static final Permission ALL_UNLOCKED = new Permission("MobRacers.unlock-all", "Access to all vehicles/discs without unlocking them", PermissionDefault.FALSE);
	
	private static YamlConfiguration lang;
	private static MobRacersGame game;
	
	private static MobRacersHook hook;
	private static VaultSupport vault;
	
	public void onEnable()
	{
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.getLogger().info("[MobRacers] " + lookForUpdates());
			}
		});


		try
		{
			this.saveResource("langEN.yml", true);
			this.saveResource("langFR.yml", true);
			this.saveResource("langRU.yml", true);
			this.saveResource("langDE.yml", true);
			this.saveResource("langES.yml", true);

			if(!new File(getDataFolder(), "purchases.yml").exists())
				this.saveResource("purchases.yml", false);

			this.saveDefaultConfig();
			this.saveResource("configFR.yml", true);
		}
		catch(Exception e)
		{
			new Exception("MobRacers.jar is CORRUPTED please redownload from spigot.", e).printStackTrace();
		}

		MobRacersConfig baseConfig = new MobRacersConfig(new File(getDataFolder(), "config.yml"));
		baseConfig.load();

		try
		{
			File lang = new File(getDataFolder(), "lang.yml");
			File mainLang = new File(getDataFolder(), baseConfig.getInjectFrom());

			if(!lang.exists())
			{
				mainLang.renameTo(lang);
				this.saveResource(baseConfig.getInjectFrom(), true);
			}
			else if(baseConfig.injectLangFile())
			{
				boolean save = false;
				YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
				YamlConfiguration mainLangConfig = YamlConfiguration.loadConfiguration(mainLang);
				for(String key : mainLangConfig.getKeys(false))
					if(!langConfig.isString(key))
					{
						save = true;
						langConfig.set(key, mainLangConfig.get(key));
					}

				if(save)
					langConfig.save(lang);

			}
		}
		catch(Exception e)
		{
			new Exception("An exception occured when trying to inject your MobRacers lang file. (Bad config ?)", e).printStackTrace();
		}

		loadLang(this);

		Core.getCustomEntityManager().enable(this);
		Core.getBungeeMessager().enable(this);
        Core.getWand().enable(this, STAFF, CourseMessage.WAND);

		game = new MobRacersGame(this, baseConfig);
		game.onEnable();
		game.setOpen(true);

		CoursePurchase.init();
		CloudItem.init(this);
		ShieldItem.init(this);
		
		STAFF.addParent(ADMIN, true);
		VIP.addParent(STAFF, true);
		PLAY.addParent(VIP, true);
		VOTE.addParent(PLAY, true);
		CMD_PROTECT_BYPASS.addParent(ADMIN, true);

		if(((MobRacersConfig)game.getConfig()).enableStats())
		{
			MobRacersConfig config = (MobRacersConfig)game.getConfig();
			if(config.isSqlEnabled())
				Core.getUserDatasManager().enableDB(this, config.getSqlDriver(), config.getSqlURL(), config.getSqlUser(), config.getSqlPassword(), CourseStats.getTables());
			else
				Core.getUserDatasManager().enableYaml(this);

			ArenaStats.init();

			if(((MobRacersConfig) game.getConfig()).enableVault() && getServer().getPluginManager().getPlugin("Vault") != null)
			{
				vault = new VaultSupport();
				if(vault.isEnabled())
					getLogger().info("§aMobRacers has detected Vault and is going to use it as economy plugin");
				else
					vault = null;
			}
			else
				vault = null;
		}
		
		try
		{
			getClass().getClassLoader().loadClass("me.clip.placeholderapi.PlaceholderHook");
			getClass().getClassLoader().loadClass("me.clip.placeholderapi.PlaceholderAPI");
	      
			hook = new MobRacersHook(game);
			if(hook.register(this))
				getLogger().info("§aMobRacers Hook have been properly registred to PlaceHoldersAPI");
			else
				hook = null;
	    }
	    catch (Throwable t)
	    {
	    	hook = null;
	    }
	}
	
	public void onDisable()
	{
		if (hook != null)
			try
			{
				if (hook.unregister(this))
					getLogger().info("§aMobRacers Hook have been properly unregistred to PlaceHoldersAPI");
				hook = null;
			}
			catch (Throwable localThrowable)
			{
				hook = null;
			}
		
		game.onDisable();
		game = null;
		Core.disable(this);

		lang = null;
		vault = null;

		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public static MobRacersGame getGame()
	{
		return game;
	}

	public static YamlConfiguration getLang()
	{
		return lang;
	}
	
	public static void loadLang(Plugin plugin)
	{
		lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "lang.yml"));
	}
	
	public static Plugin getPlugin()
	{
		return game.getPlugin();
	}
	
	public static VaultSupport getVault()
	{
		return vault;
	}
	
	public String lookForUpdates()
	{	String version = VersionSubCommand.getLatestVersion();
		if(version != null)
		{	if(VersionSubCommand.isLatest(version, this))
				return "You are on the latest version";
			return "Version " + version + " is available on Spigot !";	}
		return "Failed to find latest version";	}
}
