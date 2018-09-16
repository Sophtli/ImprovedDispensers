package tk.tobiplayer3.improveddispensers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import tk.tobiplayer3.improveddispensers.items.Plant;
import tk.tobiplayer3.improveddispensers.items.Weapon;
import tk.tobiplayer3.improveddispensers.listener.DispenserListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImprovedDispensers extends JavaPlugin {

    private static ImprovedDispensers improvedDispensers;
    private List<Weapon> weapons = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private List<EntityType> undead = new ArrayList<>();
    private List<EntityType> arthropods = new ArrayList<>();
    private boolean hoeRequired = true;
    private boolean durabilityEnabled = true;
    
    private Logger logger;

	@Override
	public void onEnable() {
		logger = getLogger();
	    improvedDispensers = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DispenserListener(), this);

        saveDefaultConfig();
        loadConfig();
    }

    public static ImprovedDispensers getPlugin(){
	    return improvedDispensers;
    }

    private void loadConfig(){
        ConfigurationSection weaponsSection = getConfig().getConfigurationSection("weapons");
        for(String s : weaponsSection.getKeys(false)){
            try {
                Material weapon = Material.valueOf(s.toUpperCase());
                weapons.add(new Weapon(weapon, weaponsSection.getInt(s)));
			} catch (Exception e) {
				logger.log(Level.WARNING, "The item "+s+" couldn't be loaded as weapon.");
			}
        }

        ConfigurationSection plantsSection = getConfig().getConfigurationSection("plants");
        for(String s : plantsSection.getKeys(false)){
        	try {
        		String[] items = s.split("/");
                Material plant = Material.valueOf(items[0].toUpperCase());
                Material plantItem = Material.valueOf(items[1].toUpperCase());
                List<String> groundStringList = plantsSection.getStringList(s);
                List<Material> groundList = new ArrayList<>();
                
                for(String groundString : groundStringList) {
                	groundList.add(Material.valueOf(groundString.toUpperCase()));
                }
                
                plants.add(new Plant(plant, plantItem, groundList));
			} catch (Exception e) {
				logger.log(Level.WARNING, "The item "+s+" couldn't be loaded as plant.");
			}

        }
        
        List<String> undeadStringList = getConfig().getStringList("undead");
        List<EntityType> undeadList = new ArrayList<>();
        
        for(String undead : undeadStringList) {
        	try {
				undeadList.add(EntityType.valueOf(undead.toUpperCase()));
			} catch (Exception e) {
				logger.log(Level.WARNING, "The entity "+undead+" couldn't be loaded as undead.");
			}
        }
        
        List<String> arthropodStringList = getConfig().getStringList("arthropods");
        List<EntityType> arthropodList = new ArrayList<>();
        
        for(String arthropod : arthropodStringList) {
        	try {
        		arthropodList.add(EntityType.valueOf(arthropod.toUpperCase()));
			} catch (Exception e) {
				logger.log(Level.WARNING, "The entity "+undead+" couldn't be loaded as arthropod.");
			}
        }
    }

    public List<Weapon> getWeapons(){
	    return weapons;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public List<EntityType> getUndead() {
        return undead;
    }

    public List<EntityType> getArthropods() {
        return arthropods;
    }

    public Weapon getWeapon(Material material){
	    for(Weapon weapon : getWeapons()){
	        if(material.equals(weapon.getWeapon())){
	            return weapon;
            }
        }
        return null;
    }
    
    public Plant getPlant(Material material) {
    	for(Plant plant : getPlants()){
	        if(material.equals(plant.getPlantItem())){
	            return plant;
            }
        }
        return null;
    }
    
    public boolean isHoeRequired() {
		return hoeRequired;
	}
    
    public boolean isDurabilityEnabled() {
		return durabilityEnabled;
	}
}
