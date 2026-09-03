package org.bukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class ShapelessRecipe implements Recipe {
	private ItemStack output;
	private List<ItemStack> ingredients = new ArrayList<ItemStack>();

	public ShapelessRecipe(ItemStack result) {
		this.output = new ItemStack(result);
	}

	public ShapelessRecipe addIngredient(MaterialData ingredient) {
		return addIngredient(1, ingredient);
	}

	public ShapelessRecipe addIngredient(Material ingredient) {
		return addIngredient(1, ingredient, 0);
	}

	@Deprecated
	public ShapelessRecipe addIngredient(Material ingredient, int rawdata) {
		return addIngredient(1, ingredient, rawdata);
	}

	public ShapelessRecipe addIngredient(int count, MaterialData ingredient) {
		return addIngredient(count, ingredient.getItemType(), ingredient.getData());
	}

	public ShapelessRecipe addIngredient(int count, Material ingredient) {
		return addIngredient(count, ingredient, 0);
	}

	@Deprecated
	public ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata) {
		Validate.isTrue(ingredients.size() + count <= 9, "Shapeless recipes cannot have more than 9 ingredients");

		if (rawdata == -1) {
			rawdata = Short.MAX_VALUE;
		}

		while (count-- > 0) {
			ingredients.add(new ItemStack(ingredient, 1, (short) rawdata));
		}
		return this;
	}

	public ShapelessRecipe removeIngredient(Material ingredient) {
		return removeIngredient(ingredient, 0);
	}

	public ShapelessRecipe removeIngredient(MaterialData ingredient) {
		return removeIngredient(ingredient.getItemType(), ingredient.getData());
	}

	public ShapelessRecipe removeIngredient(int count, Material ingredient) {
		return removeIngredient(count, ingredient, 0);
	}

	public ShapelessRecipe removeIngredient(int count, MaterialData ingredient) {
		return removeIngredient(count, ingredient.getItemType(), ingredient.getData());
	}

	@Deprecated
	public ShapelessRecipe removeIngredient(Material ingredient, int rawdata) {
		return removeIngredient(1, ingredient, rawdata);
	}

	@Deprecated
	public ShapelessRecipe removeIngredient(int count, Material ingredient, int rawdata) {
		Iterator<ItemStack> iterator = ingredients.iterator();
		while (count > 0 && iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if (stack.getType() == ingredient && stack.getDurability() == rawdata) {
				iterator.remove();
				count--;
			}
		}
		return this;
	}

	public ItemStack getResult() {
		return output.clone();
	}

	public List<ItemStack> getIngredientList() {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>(ingredients.size());
		for (ItemStack ingredient : ingredients) {
			result.add(ingredient.clone());
		}
		return result;
	}
}
