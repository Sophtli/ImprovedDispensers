package tk.tobiplayer3.improveddispensers.items;

import org.bukkit.Material;

import java.util.List;

public class Plant {

	private Material plant;
	private Material plantItem;
	private List<Material> ground;

	public Plant(Material plant, Material plantItem, List<Material> ground) {
		this.plant = plant;
		this.ground = ground;
		this.plantItem = plantItem;
	}

	public Material getPlant() {
		return plant;
	}

	public List<Material> getGround() {
		return ground;
	}

	public Material getPlantItem() {
		return plantItem;
	}
}
