package me.winterguardian.mobracers.item;

public enum ItemResult
{
	DISCARD(true, false), USEANDDISCARD(true, true), USEANDKEEP(false, true), KEEP(false, false);
	
	private boolean delete;
	private boolean use;
	
	ItemResult(boolean delete, boolean use)
	{
		this.delete = delete;
		this.use = use;
	}
	
	public boolean deleteItem()
	{
		return delete;
	}
	
	public boolean useItem()
	{
		return use;
	}
}
