package me.winterguardian.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public abstract class PlayerProtectionListener implements Listener
{
	private EventPriority priority;
	private boolean cancelHunger;
	private boolean cancelBreak;
	private boolean cancelPlace;
	private boolean cancelDamage;
	private boolean cancelDamageOthers;
	private boolean cancelInteract;
	private boolean cancelInteractEntity;
	private boolean cancelSneak;
	private boolean cancelSprint;
	private boolean cancelPickupItem;
	private boolean cancelDropItem;
	private boolean cancelInventoryClick;
	private boolean cancelCombust;
	
	public PlayerProtectionListener()
	{
		this(null);
	}
	
	public PlayerProtectionListener(EventPriority priority)
	{
		this.setPriority(priority);
		
		this.cancelHunger = true;
		this.cancelBreak = true;
		this.cancelPlace = true;
		this.cancelDamage = true;
		this.cancelDamageOthers = true;
		this.cancelInteract = false;
		this.cancelInteractEntity = false;
		this.cancelSneak = false;
		this.cancelSprint = false;
		this.cancelPickupItem = false;
		this.cancelDropItem = false;
		this.cancelInventoryClick = false;
		this.cancelCombust = true;
	}
	
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(this.isAffected((Player) event.getEntity()))
				event.setCancelled(this.isCancelHunger());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFoodLevelChangeLOWEST(FoodLevelChangeEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onFoodLevelChange(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onFoodLevelChangeLOW(FoodLevelChangeEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onFoodLevelChange(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFoodLevelChangeNORMAL(FoodLevelChangeEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onFoodLevelChange(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onFoodLevelChangeHIGH(FoodLevelChangeEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onFoodLevelChange(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFoodLevelChangeHIGHEST(FoodLevelChangeEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onFoodLevelChange(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelBreak());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreakLOWEST(BlockBreakEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onBlockBreak(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreakLOW(BlockBreakEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onBlockBreak(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreakNORMAL(BlockBreakEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onBlockBreak(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakHIGH(BlockBreakEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onBlockBreak(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreakHIGHEST(BlockBreakEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onBlockBreak(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelPlace());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlaceLOWEST(BlockPlaceEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onBlockPlace(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlaceLOW(BlockPlaceEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onBlockPlace(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlaceNORMAL(BlockPlaceEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onBlockPlace(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlaceHIGH(BlockPlaceEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onBlockPlace(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlaceHIGHEST(BlockPlaceEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onBlockPlace(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(this.isAffected((Player) event.getEntity()))
				event.setCancelled(this.isCancelDamage());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamageLOWEST(EntityDamageEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamageLOW(EntityDamageEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamageNORMAL(EntityDamageEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamageHIGH(EntityDamageEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamageHIGHEST(EntityDamageEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerDamage(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerDamageOthers(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player)
			if(this.isAffected((Player) event.getDamager()))
				event.setCancelled(this.isCancelDamageOthers());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamageOthersLOWEST(EntityDamageByEntityEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamageOthersLOW(EntityDamageByEntityEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamageOthersNORMAL(EntityDamageByEntityEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamageOthersHIGH(EntityDamageByEntityEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerDamage(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamageOthersHIGHEST(EntityDamageByEntityEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerDamage(event);
	}
		
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerSneak(PlayerToggleSneakEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelSneak());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerSneakLOWEST(PlayerToggleSneakEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerSneak(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerSneakLOW(PlayerToggleSneakEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerSneak(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSneakNORMAL(PlayerToggleSneakEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerSneak(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerSneakHIGH(PlayerToggleSneakEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerSneak(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerSneakHIGHEST(PlayerToggleSneakEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerSneak(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerSprint(PlayerToggleSprintEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelSprint());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerSprintLOWEST(PlayerToggleSprintEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerSprint(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerSprintLOW(PlayerToggleSprintEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerSprint(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSprintNORMAL(PlayerToggleSprintEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerSprint(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerSprintHIGH(PlayerToggleSprintEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerSprint(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerSprintHIGHEST(PlayerToggleSprintEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerSprint(event);
	}
		
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelInteract());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractLOWEST(PlayerInteractEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerInteract(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractLOW(PlayerInteractEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerInteract(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractNORMAL(PlayerInteractEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerInteract(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractHIGH(PlayerInteractEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerInteract(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractHIGHEST(PlayerInteractEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerInteract(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
		
	
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelInteractEntity());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntityLOWEST(PlayerInteractEntityEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerInteractEntity(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractLOW(PlayerInteractEntityEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerInteractEntity(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractNORMAL(PlayerInteractEntityEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerInteractEntity(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractHIGH(PlayerInteractEntityEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerInteractEntity(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractHIGHEST(PlayerInteractEntityEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerInteractEntity(event);
	}

	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelPickupItem());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickupItemLOWEST(PlayerPickupItemEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerPickupItem(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPickupItemLOW(PlayerPickupItemEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerPickupItem(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItemNORMAL(PlayerPickupItemEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerPickupItem(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPickupItemHIGH(PlayerPickupItemEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerPickupItem(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItemHIGHEST(PlayerPickupItemEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerPickupItem(event);
	}
	

	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(this.isAffected(event.getPlayer()))
			event.setCancelled(this.isCancelDropItem());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItemLOWEST(PlayerDropItemEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerDropItem(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDropItemLOW(PlayerDropItemEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerDropItem(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItemNORMAL(PlayerDropItemEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerDropItem(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItemHIGH(PlayerDropItemEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerDropItem(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItemHIGHEST(PlayerDropItemEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerDropItem(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
		
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(event.getWhoClicked() instanceof Player)
			if(this.isAffected((Player) event.getWhoClicked()))
				event.setCancelled(this.isCancelInventoryClick());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickLOWEST(InventoryClickEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onInventoryClick(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClickLOW(InventoryClickEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onInventoryClick(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClickNORMAL(InventoryClickEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onInventoryClick(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClickHIGH(InventoryClickEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onInventoryClick(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickHIGHEST(InventoryClickEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onInventoryClick(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
			
	public void onPlayerCombust(EntityCombustEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(this.isAffected((Player) event.getEntity()))
				event.setCancelled(this.isCancelCombust());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCombustLOWEST(EntityCombustEvent event)
	{
		if(this.priority == EventPriority.LOWEST)
			onPlayerCombust(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCombustLOW(EntityCombustEvent event)
	{
		if(this.priority == EventPriority.LOW)
			onPlayerCombust(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCombustNORMAL(EntityCombustEvent event)
	{
		if(this.priority == EventPriority.NORMAL)
			onPlayerCombust(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCombustHIGH(EntityCombustEvent event)
	{
		if(this.priority == EventPriority.HIGH)
			onPlayerCombust(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCombustHIGHEST(EntityCombustEvent event)
	{
		if(this.priority == EventPriority.HIGHEST)
			onPlayerCombust(event);
	}
	
	//=============================================================================================
	//=============================================================================================
	//=============================================================================================
	
	public abstract boolean isAffected(Player player);

	public EventPriority getPriority() {
		return priority;
	}

	public void setPriority(EventPriority priority)
	{
		this.priority = priority;
		if(this.priority == EventPriority.MONITOR)
			this.priority = EventPriority.HIGHEST;
		
		if(this.priority == null)
			this.priority = EventPriority.NORMAL;
	}
	
	public boolean isCancelHunger()
	{
		return cancelHunger;
	}

	public void setCancelHunger(boolean cancelHunger)
	{
		this.cancelHunger = cancelHunger;
	}

	public boolean isCancelBreak() {
		return cancelBreak;
	}

	public void setCancelBreak(boolean cancelBreak) {
		this.cancelBreak = cancelBreak;
	}

	public boolean isCancelPlace() {
		return cancelPlace;
	}

	public void setCancelPlace(boolean cancelPlace) {
		this.cancelPlace = cancelPlace;
	}

	public boolean isCancelDamage() {
		return cancelDamage;
	}

	public void setCancelDamage(boolean cancelDamage) {
		this.cancelDamage = cancelDamage;
	}

	public boolean isCancelDamageOthers() {
		return cancelDamageOthers;
	}

	public void setCancelDamageOthers(boolean cancelDamageOthers) {
		this.cancelDamageOthers = cancelDamageOthers;
	}

	public boolean isCancelInteract() {
		return cancelInteract;
	}

	public void setCancelInteract(boolean cancelInteract) {
		this.cancelInteract = cancelInteract;
	}

	public boolean isCancelInteractEntity() {
		return cancelInteractEntity;
	}

	public void setCancelInteractEntity(boolean cancelInteractEntity) {
		this.cancelInteractEntity = cancelInteractEntity;
	}

	public boolean isCancelSneak() {
		return cancelSneak;
	}

	public void setCancelSneak(boolean cancelSneak) {
		this.cancelSneak = cancelSneak;
	}

	public boolean isCancelSprint() {
		return cancelSprint;
	}

	public void setCancelSprint(boolean cancelSprint) {
		this.cancelSprint = cancelSprint;
	}

	public boolean isCancelPickupItem() {
		return cancelPickupItem;
	}

	public void setCancelPickupItem(boolean cancelPickupItem) {
		this.cancelPickupItem = cancelPickupItem;
	}

	public boolean isCancelDropItem() {
		return cancelDropItem;
	}

	public void setCancelDropItem(boolean cancelDropItem) {
		this.cancelDropItem = cancelDropItem;
	}

	public boolean isCancelInventoryClick() {
		return cancelInventoryClick;
	}

	public void setCancelInventoryClick(boolean cancelInventoryClick) {
		this.cancelInventoryClick = cancelInventoryClick;
	}

	public boolean isCancelCombust() {
		return cancelCombust;
	}

	public void setCancelCombust(boolean cancelCombust) {
		this.cancelCombust = cancelCombust;
	}
}
