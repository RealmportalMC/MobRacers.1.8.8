package me.winterguardian.mobracers.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.winterguardian.core.util.RecordUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CoursePurchase;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CourseMusic
{
	public static final CourseMusic CAT = new CourseMusic(CourseRecord.CAT); 
	public static final CourseMusic BLOCKS = new CourseMusic(CourseRecord.BLOCKS); 
	
	public static final CourseMusic CHIRP = new PurchasableMusic(CourseRecord.CHIRP, CoursePurchase.CHIRP); 
	public static final CourseMusic FAR = new VipMusic("far", CourseRecord.FAR); 
	public static final CourseMusic MALL = new PurchasableMusic(CourseRecord.MALL, CoursePurchase.MALL); 
	public static final CourseMusic MELLOHI = new WinnableMusic(CourseRecord.MELLOHI, CourseAchievement.UNDEADHORSE_WIN); 
	public static final CourseMusic STAL = new WinnableMusic(CourseRecord.STAL, CourseAchievement.PIG_PASSINGS); 
	public static final CourseMusic STRAD = new PurchasableMusic(CourseRecord.STRAD, CoursePurchase.STRAD); 
	public static final CourseMusic WARD = new VipMusic("ward", CourseRecord.WARD); 
	public static final CourseMusic WAIT = new WinnableMusic(CourseRecord.WAIT, CourseAchievement.WOLF_PLAY); 
	
	public static final CourseMusic NOMUSIC = new CourseMusic(CourseMessage.RECORD_NOMUSIC.toString(), Material.BARRIER)
	{
		public void play(Player p, Arena arena) {}
		public void stop(Player p, Arena arena) {}
	}; 
	
	private String name;
	private Material item;
	
	
	public CourseMusic(String name, Material item)
	{
		this.name = name;
		this.item = item;
	}
	
	public CourseMusic(CourseRecord record)
	{
		this.name = record.getName();
		this.item = record.getItem();
	}

	public String getName()
	{
		return name;
	}

	public Material getMaterial()
	{
		return this.item;
	}
	
	public boolean isAvailable(Player p)
	{
		return true;
	}
	
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(this.item, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(Collections.singletonList(CourseMessage.RECORD_LORE.toString()));
		item.setItemMeta(meta);
		return item;
	}
	
	public void play(Player p, Arena arena)
	{
		for(SerializableLocation location : arena.getJukeboxes())
			if(location.getLocation() != null)
				RecordUtil.playRecord(p, location.getLocation(), this.item);
	}
	
	public void stop(Player p, Arena arena)
	{
		for(SerializableLocation location : arena.getJukeboxes())
			if(location.getLocation() != null)
				RecordUtil.stopRecord(p, location.getLocation());
	}
	
	public static CourseMusic[] values()
	{
		CourseMusic[] musics = new CourseMusic[CourseMusic.class.getFields().length];
		
		for(int i = 0; i < CourseMusic.class.getFields().length; i++)
			try
			{
				musics[i] = (CourseMusic) CourseMusic.class.getFields()[i].get(null);
			}
			catch (Exception e)
			{
				musics[i] = null;
			}
		return musics;
	}
	
	public static List<CourseMusic> getAvailable(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return Arrays.asList(values());

		List<CourseMusic> available = new ArrayList<CourseMusic>();
		
		for(CourseMusic music : values())
			if(music.isAvailable(p))
				available.add(music);
		return available;
	}
}
