package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class FurnaceRecipe implements Recipe {
	private ItemStack output;
	private ItemStack ingredient;

	public FurnaceRecipe(ItemStack result, Material source) {
		this(result, source, 0);
	}

	public FurnaceRecipe(ItemStack result, MaterialData source) {
		this(result, source.getItemType(), source.getData());
	}

	@Deprecated
	public FurnaceRecipe(ItemStack result, Material source, int data) {
		this.output = new ItemStack(result);
		this.ingredient = new ItemStack(source, 1, (short) data);
	}

	public FurnaceRecipe setInput(MaterialData input) {
		return setInput(input.getItemType(), input.getData());
	}

	public FurnaceRecipe setInput(Material input) {
		return setInput(input, 0);
	}

	@Deprecated
	public FurnaceRecipe setInput(Material input, int data) {
		this.ingredient = new ItemStack(input, 1, (short) data);
		return this;
	}

	public ItemStack getInput() {
		return this.ingredient.clone();
	}

	public ItemStack getResult() {
		return output.clone();
	}
}
