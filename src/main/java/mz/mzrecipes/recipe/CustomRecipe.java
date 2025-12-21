package mz.mzrecipes.recipe;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.Ingredient;
import mz.mzlib.minecraft.recipe.IngredientVanilla;
import mz.mzlib.minecraft.recipe.Recipe;
import mz.mzlib.util.Option;
import mz.mzrecipes.MzRecipes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomRecipe<R extends Recipe>
{
    Identifier id;
    List<ItemStack> ingredients;

    public CustomRecipe(Identifier id, List<ItemStack> ingredients)
    {
        this.setId(id);
        this.ingredients = ingredients;
    }
    public CustomRecipe(List<ItemStack> ingredients)
    {
        this(Identifier.newInstance(MzRecipes.instance.MOD_ID, UUID.randomUUID().toString()), ingredients);
    }

    public Identifier getId()
    {
        return this.id;
    }
    public void setId(Identifier value)
    {
        this.id = value;
    }

    public List<ItemStack> getIngredients()
    {
        return this.ingredients;
    }

    public List<Option<Ingredient>> compileIngredients()
    {
        return this.getIngredients().stream()
            .map(is -> Option.some(is).filter(it -> !it.isEmpty()).map(IngredientVanilla::of)
                .map(it -> it.withCount(is.getCount())))
            .collect(Collectors.toList()); // TODO
    }
}
