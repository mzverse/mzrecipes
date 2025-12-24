package mz.mzrecipes.item;

import mz.mzlib.minecraft.event.player.async.EventAsyncPlayerDisplayItem;
import mz.mzlib.minecraft.item.Item;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.mzitem.MzItem;
import mz.mzlib.minecraft.recipe.Ingredient;
import mz.mzlib.util.wrapper.WrapSameClass;

@WrapSameClass(MzItem.class)
public interface MzItemIngredient extends MzItem
{
    Ingredient asIngredient() throws RuntimeException;

    @Override
    default void onDisplay(EventAsyncPlayerDisplayItem event)
    {
        for(ItemStack itemStack : event.reviseItemStack())
        {
            Item.makeEnchantmentGlint(itemStack);
        }
    }
}
