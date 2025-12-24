package mz.mzrecipes.item;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.MinecraftPlatform;
import mz.mzlib.minecraft.event.player.async.EventAsyncPlayerDisplayItem;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.mzitem.MzItemClass;
import mz.mzlib.minecraft.nbt.NbtCompound;
import mz.mzlib.minecraft.recipe.Ingredient;
import mz.mzlib.minecraft.recipe.IngredientVanilla;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.minecraft.text.TextColor;
import mz.mzrecipes.MzRecipes;

@MzItemClass
public interface MzItemIngredientId extends MzItemIngredient
{
    @Override
    default Identifier static$getMzId()
    {
        return Identifier.of(MzRecipes.instance.MOD_ID, "ingredient_id");
    }

    @Override
    default ItemStack static$vanilla()
    {
        return ItemStack.builder().fromId("stick").build(); // useless
    }

    @Override
    default Ingredient asIngredient()
    {
        Builder builder = ItemStack.builder().fromId(this.getIngredientId());
        if(MinecraftPlatform.instance.getVersion() < 1300)
            builder.damageV_1300(Short.MAX_VALUE); // match any damage
        return IngredientVanilla.of(builder.build());
    }

    default Identifier getIngredientId()
    {
        for(NbtCompound nbt : MZ_DATA.get(this))
        {
            for(String result : nbt.getString("id"))
            {
                return Identifier.of(result);
            }
        }
        throw new IllegalStateException();
    }
    default void setIngredientId(Identifier value)
    {
        for(NbtCompound data : MZ_DATA.revise(this))
        {
            data.put("id", value.toString());
        }
    }

    @Override
    default void onDisplay(EventAsyncPlayerDisplayItem event)
    {
        Builder builder = ItemStack.builder(event.getItemStack());
        try
        {
            builder.fromId(this.getIngredientId());
        }
        catch(Throwable e)
        {
            builder.customName(Text.literal(e.getMessage()).setColor(TextColor.RED));
        }
        event.setItemStack(builder.build());
    }
    @Override
    default Object getDisplayArgs(String lang)
    {
        return new DisplayArgs(this);
    }

    class DisplayArgs
    {
        MzItemIngredientId item;
        public DisplayArgs(MzItemIngredientId item)
        {
            this.item = item;
        }
        Identifier id;
        public Identifier getId()
        {
            if(this.id != null)
                return this.id;
            return this.id = this.item.getIngredientId();
        }
    }
}
