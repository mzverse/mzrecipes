package mz.mzrecipes.ui;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.i18n.MinecraftI18n;
import mz.mzlib.minecraft.item.Item;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.Recipe;
import mz.mzlib.minecraft.recipe.RecipeType;
import mz.mzlib.minecraft.recipe.RegistrarRecipe;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.minecraft.ui.Ui;
import mz.mzlib.minecraft.ui.window.UiWindowList;
import mz.mzlib.util.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactoryUiRecipes
{
    public static Ui createTypes()
    {
        UiWindowList.Builder<Map.Entry<Identifier, Recipe>> builder = UiWindowList.builder(
            new ArrayList<>(RegistrarRecipe.instance.getOriginalRecipes().get(RecipeType.CRAFTING).entrySet()));
        builder.iconGetter((entry, player) ->
        {
            ItemStack icon = entry.getValue().getIcons().stream().findFirst().map(ItemStack::clone0)
                .orElse(ItemStack.builder().fromId("stone").build());
            for(List<Text> lore : Item.LORE.revise(icon))
            {
                lore.addAll(MinecraftI18n.resolveTexts(
                    player, "mzrecipes.recipe.lore",
                    MapBuilder.hashMap()
                        .put("id", entry.getKey())
                        .put("source", "native")
                        .put("loaded", true)
                        .put("enabled", true)
                        .get()
                ));
            }
            return icon;
        });
        return builder.build();
    }
}
