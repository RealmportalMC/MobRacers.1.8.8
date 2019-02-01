package me.winterguardian.mobracers.music;

import me.winterguardian.mobracers.CourseMessage;

import org.bukkit.Material;

public enum CourseRecord
{
	DISC13("---", Material.GOLD_RECORD),
	CAT(CourseMessage.RECORD_CAT.toString(), Material.GREEN_RECORD),
	BLOCKS(CourseMessage.RECORD_BLOCKS.toString(), Material.RECORD_3),
	CHIRP(CourseMessage.RECORD_CHIRP.toString(), Material.RECORD_4),
	FAR(CourseMessage.RECORD_FAR.toString(), Material.RECORD_5),
	MALL(CourseMessage.RECORD_MALL.toString(), Material.RECORD_6),
	MELLOHI(CourseMessage.RECORD_MELLOHI.toString(), Material.RECORD_7),
	STAL(CourseMessage.RECORD_STAL.toString(), Material.RECORD_8),
	STRAD(CourseMessage.RECORD_STRAD.toString(), Material.RECORD_9),
	WARD(CourseMessage.RECORD_WARD.toString(), Material.RECORD_10),
	DISC11("---", Material.RECORD_11),
	WAIT(CourseMessage.RECORD_WAIT.toString(), Material.RECORD_12);
	
	private String name;
	private Material item;
	
	CourseRecord(String name, Material item)
	{
		this.name = name;
		this.item = item;
	}

	public String getName()
	{
		return name;
	}

	public Material getItem()
	{
		return item;
	}
}
