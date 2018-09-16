package tk.tobiplayer3.improveddispensers.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import tk.tobiplayer3.improveddispensers.ImprovedDispensers;

import java.util.Map;

public class Weapon {

	private Integer damage;
	private Material weapon;
	private ImprovedDispensers plugin = ImprovedDispensers.getPlugin();

	public Weapon(Material weapon, Integer damage) {
		this.damage = damage;
		this.weapon = weapon;
	}

	public void damage(Map<Enchantment, Integer> enchantments, Damageable entity) {

		double currentDamage = damage;
		
		for (Enchantment ench : enchantments.keySet()) {

			if (ench.equals(Enchantment.FIRE_ASPECT)) {

				entity.setFireTicks(70 * enchantments.get(ench));

			} else if (ench.equals(Enchantment.DAMAGE_ARTHROPODS)) {

				EntityType entityType = entity.getType();
				if (isArthropod(entityType)) {
					currentDamage += enchantments.get(ench) * 2.5;
				}

			} else if (ench.equals(Enchantment.DAMAGE_UNDEAD)) {

				EntityType entityType = entity.getType();
				if (isUndead(entityType)) {
					currentDamage += enchantments.get(ench) * 2.5;
				}

			} else if (ench.equals(Enchantment.DAMAGE_ALL)) {

				currentDamage += 1 + (enchantments.get(ench) * 0.5) - 0.5;

			}

		}
		
		entity.damage(currentDamage);
		
	}

	public Integer getDamage() {
		return damage;
	}

	public Material getWeapon() {
		return weapon;
	}

	private boolean isUndead(EntityType entityType) {
		for (EntityType undead : plugin.getUndead()) {
			if (entityType.equals(undead)) {
				return true;
			}
		}
		return false;
	}

	private boolean isArthropod(EntityType entityType) {
		for (EntityType arthropod : plugin.getArthropods()) {
			if (entityType.equals(arthropod)) {
				return true;
			}
		}
		return false;
	}
}
