package me.winterguardian.core.gameplay;

import me.winterguardian.core.DynamicComponent;
import me.winterguardian.core.gameplay.listener.GameplayListener;
import me.winterguardian.core.gameplay.listener.ProjectileEffectListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class GameplayManager extends DynamicComponent
{
	private Listener gameplay, projEffect;

	private int regenTask;

	public GameplayManager()
	{
		super();
		this.gameplay = new GameplayListener(this);
		this.projEffect = new ProjectileEffectListener(this);
		this.regenTask = -1;
	}

	@Override
	public void register(Plugin plugin)
	{

		Bukkit.getPluginManager().registerEvents(gameplay, plugin);
		Bukkit.getPluginManager().registerEvents(projEffect, plugin);

		this.regenTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
					if(player.getFoodLevel() > 18 && !player.isDead() && player.getHealth() > 0)
						if(player.getHealth() < player.getMaxHealth())
							player.setHealth(Math.min(player.getHealth() + 0.0125, player.getMaxHealth()));
			}
		}, 0, 1);
	}

	@Override
	public void unregister(Plugin plugin)
	{
		HandlerList.unregisterAll(gameplay);
		HandlerList.unregisterAll(projEffect);
		Bukkit.getScheduler().cancelTask(this.regenTask);
		this.regenTask = -1;
	}
}
