package me.winterguardian.core.gameplay.listener;

import me.winterguardian.core.gameplay.EntityEffectByProjectileEvent;
import me.winterguardian.core.gameplay.GameplayManager;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectileEffectListener implements Listener
{
	private GameplayManager manager;
	private HashMap<Projectile, List<PotionEffect>> projectilesEffects;
	
	public ProjectileEffectListener(GameplayManager manager)
	{
		this.projectilesEffects = new HashMap<>();
		this.manager = manager;
	}
	
	@EventHandler
	public void onEntityShootEffectBow(EntityShootBowEvent e)
	{
		if(!e.getBow().hasItemMeta() || !e.getBow().getItemMeta().hasLore())
			return;

		for(String effectDataLine : e.getBow().getItemMeta().getLore())
		{
			String[] data = effectDataLine.split(" ");
			if(data.length < 3)
				continue;

			String effectName = data[0].replaceAll("§[0-9a-f]", "").toUpperCase();

			int i = 1;
			if(data.length > 3)
				for(;i < data.length - 2; i++)
					effectName += "_" + data[i].toUpperCase();

			int amplifier = TextUtil.romanToArabicNumbers(data[i]) - 1;

			String duration = data[i + 1].replaceAll("\\(|\\)", "");
			int ticks = (Integer.parseInt(duration.split(":")[0]) * 60 + Integer.parseInt(duration.split(":")[1])) * 20;

			PotionEffectType type = PotionEffectType.getByName(effectName);

			if(type != null)
			{
				if(!this.projectilesEffects.containsKey(e.getProjectile()))
					this.projectilesEffects.put((Projectile) e.getProjectile(), new ArrayList<PotionEffect>());

				this.projectilesEffects.get(e.getProjectile()).add(new PotionEffect(type, ticks, amplifier, false, false));
			}

		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Projectile)
		{
			if(e.getEntity() instanceof LivingEntity)
			{
				if(((Projectile)e.getDamager()).getShooter() instanceof LivingEntity)
				{
					if(this.projectilesEffects.containsKey(e.getDamager()))
					{
						for(PotionEffect effect : this.projectilesEffects.get(e.getDamager()))
						{
							EntityEffectByProjectileEvent event = new EntityEffectByProjectileEvent((LivingEntity)e.getEntity(), (Projectile)e.getDamager(), effect);
							Bukkit.getPluginManager().callEvent(event);
							
							if(!event.isCancelled())
								((LivingEntity)e.getEntity()).addPotionEffect(effect, true);
						}
						
						this.projectilesEffects.remove(e.getDamager());
					}
				}
			}
		}
	}
}
