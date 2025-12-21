package mz.mzrecipes.kind;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.Recipe;
import mz.mzlib.minecraft.recipe.RecipeRegistration;
import mz.mzlib.minecraft.ui.Ui;
import mz.mzlib.util.Option;

import java.util.List;

public abstract class KindRecipeManager<R extends Recipe, C>
{
    public abstract Identifier getKindId();

    public abstract ItemStack getKindIcon();

    public abstract List<RecipeRegistration<R>> getRecipes();

    public abstract Option<C> getCustomRecipe(Identifier id);

    public abstract Identifier getId(C recipe);

    public abstract boolean isEnabled(Identifier recipe);

    public abstract Ui view(R recipe);

    public abstract Ui view(C customRecipe, boolean modifiable);
}
