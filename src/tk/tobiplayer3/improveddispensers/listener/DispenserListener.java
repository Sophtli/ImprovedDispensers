package tk.tobiplayer3.improveddispensers.listener;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import tk.tobiplayer3.improveddispensers.ImprovedDispensers;
import tk.tobiplayer3.improveddispensers.items.Plant;

public class DispenserListener implements Listener {

	private Inventory inv;
	private ItemStack item;
	private Block block;

	private ImprovedDispensers plugin = ImprovedDispensers.getPlugin();

	@EventHandler
	public void onDispenseItem(BlockDispenseEvent e) {

		if (e.getBlock().getState() instanceof Dispenser) {

			// get facing
			block = e.getBlock();
			MaterialData materialData = block.getState().getData();
			Dispenser dispenser = (Dispenser) block.getState();
			inv = dispenser.getInventory();
			org.bukkit.material.Dispenser dispenserData = (org.bukkit.material.Dispenser) materialData;
			BlockFace blockFace = dispenserData.getFacing();

			Location location = block.getLocation();
			item = e.getItem();

			// set coordinates for block in front of dispenser
			location.setX(location.getBlockX() + blockFace.getModX());
			location.setY(location.getBlockY() + blockFace.getModY());
			location.setZ(location.getBlockZ() + blockFace.getModZ());

			// get target block
			Block targetBlock = location.getBlock();

			// get block below target block
			Location belowLoc = location;
			belowLoc.setY(location.getY() - 1);
			Block belowBlock = location.getBlock();

			// check if block in front of dispenser is occupied
			if (!targetBlock.getType().equals(Material.AIR)) {
				// break block
				if(plugin.isToolRequired() && !plugin.isTool(item.getType())) {
					return;
				}
				targetBlock.breakNaturally(item);
				e.setCancelled(true);
				if (plugin.isTool(item.getType())) {
					reduceDurability();
				}
				return;

			} else {
				// hit entities
				if (plugin.getWeapon(item.getType()) != null) {

					location.setX(location.getBlockX() + blockFace.getModX());
					location.setY(location.getBlockY() + blockFace.getModY());
					location.setZ(location.getBlockZ() + blockFace.getModZ());

					Collection<Entity> entityList = location.getWorld().getNearbyEntities(location, 1.5, 3, 1.5);

					for (Entity entity : entityList) {
						if (entity != null) {
							Damageable entityDamageable = (Damageable) entity;
							plugin.getWeapon(item.getType()).damage(item.getEnchantments(), entityDamageable);
						}
					}
					e.setCancelled(true);
					reduceDurability();
					return;
				}

				if (plugin.getPlant(item.getType()) != null) {
					Plant plant = plugin.getPlant(item.getType());
					List<Material> ground = plant.getGround();

					if (targetBlock.getType().equals(Material.AIR)) {

						if ((belowBlock.getType().equals(Material.DIRT) || belowBlock.getType().equals(Material.GRASS))
								&& ground.contains(Material.SOIL)) {
							if (plugin.isHoeRequired()) {
								for (ItemStack itemStack : inv.getContents()) {
									if (itemStack != null) {
										if (plugin.isHoe(itemStack.getType())) {
											belowBlock.setType(Material.SOIL);
											reduceDurability(itemStack);
											break;
										}
									}
								}
							}
						}

						if (ground.contains(belowBlock.getType())) {
							targetBlock.setType(plant.getPlant());
							removeItem();
							e.setCancelled(true);
						}
					}
					return;
				}
				
				// place block
				if (item.getType().isBlock()) {
					targetBlock.setType(item.getType());
					e.setCancelled(true);
					removeItem();
					return;
				}
			}
		}
	}

	// ---------- uility functions ----------//
	private void reduceDurability() {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (ItemStack stack : inv.getContents()) {
					if (stack != null) {
						if (stack.equals(item)) {
							int newDurability = item.getDurability() + 1;
							if (stack.getType().getMaxDurability() < newDurability) {
								stack.setAmount(-1);
							} else {
								stack.setDurability((short) newDurability);
							}
						}
					}
				}
			}
		});
	}

	private void reduceDurability(ItemStack itemStack) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (ItemStack stack : inv.getContents()) {
					if (stack != null) {
						if (stack.equals(itemStack)) {
							int newDurability = itemStack.getDurability() + 1;
							if (stack.getType().getMaxDurability() < newDurability) {
								stack.setAmount(-1);
							} else {
								stack.setDurability((short) newDurability);
							}
						}
					}
				}
			}
		});
	}

	private void removeItem() {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (ItemStack stack : inv.getContents()) {
					if (stack != null) {
						if (stack.isSimilar(item)) {
							stack.setAmount(stack.getAmount()-1);
						}
					}
				}
			}
		});
	}
}
