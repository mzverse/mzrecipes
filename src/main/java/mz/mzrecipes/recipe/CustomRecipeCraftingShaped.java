package mz.mzrecipes.recipe;

import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.crafting.RecipeCraftingShaped;

import java.util.Arrays;
import java.util.Collections;

public class CustomRecipeCraftingShaped extends CustomRecipe<RecipeCraftingShaped>
{
    public CustomRecipeCraftingShaped()
    {
        super(Arrays.asList(Collections.nCopies(9, ItemStack.empty()).toArray(new ItemStack[0])));
    }
}
