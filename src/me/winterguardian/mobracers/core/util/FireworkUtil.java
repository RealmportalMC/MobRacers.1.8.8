package me.winterguardian.core.util;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtil
{
	private FireworkUtil(){}
	
	public static void generate(Location loc, FireworkEffect.Type type, Color color1, Color color2, boolean flicker, boolean trail, int power)
	{
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		FireworkEffect effect = FireworkEffect.builder().flicker(flicker).withColor(color1).withFade(color2).with(type).trail(trail).build();
		fwm.addEffect(effect);
		fwm.setPower(power);

		fw.setFireworkMeta(fwm);
	}
	
	public static void generateRandom(Location loc)
	{
		Random r = new Random();
		generate(loc, 
				Type.values()[r.nextInt(Type.values().length)], 
				Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)), 
				Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)),  
				r.nextBoolean(), 
				r.nextBoolean(),  
				r.nextInt(3));
	}
}
