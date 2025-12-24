package mz.mzrecipes.item;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.MinecraftPlatform;
import mz.mzlib.minecraft.event.player.async.EventAsyncPlayerDisplayItem;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.mzitem.MzItemClass;
import mz.mzlib.minecraft.mzitem.RegistrarMzItem;
import mz.mzlib.minecraft.nbt.NbtCompound;
import mz.mzlib.minecraft.recipe.IngredientVanilla;
import mz.mzlib.util.Option;
import mz.mzrecipes.MzRecipes;

@MzItemClass
public interface MzItemIngredientCategory extends MzItemIngredient
{
    static MzItemIngredientCategory of(String category)
    {
        return of(Identifier.of(category));
    }
    static MzItemIngredientCategory of(Identifier category)
    {
        return of(category, category);
    }
    static MzItemIngredientCategory of(Identifier idV_1300, Identifier tagV1300)
    {
        MzItemIngredientCategory result = RegistrarMzItem.instance.newMzItem(MzItemIngredientCategory.class);
        result.setData(new Data(idV_1300, tagV1300));
        return result;
    }

    @Override
    default Identifier static$getMzId()
    {
        return Identifier.of(MzRecipes.instance.MOD_ID, "ingredient_category");
    }
    @Override
    default ItemStack static$vanilla()
    {
        return ItemStack.builder().fromId("stick").build(); // usually useless
    }

    @Override
    default IngredientVanilla asIngredient()
    {
        Data data = this.getData();
        return IngredientVanilla.ofCategory(data.getIdV_1300(), data.getTagV1300());
    }

    @Override
    default void onDisplay(EventAsyncPlayerDisplayItem event)
    {
        Builder builder = ItemStack.builder(event.getItemStack());
        if(MinecraftPlatform.instance.getVersion() < 1300)
            builder.fromId(this.getData().getIdV_1300());
        else
        {
            for(ItemStack any : Option.fromOptional(this.asIngredient().getMatchingStacksV1300().stream().findAny()))
                builder.item(any.getItem());
        }
        event.setItemStack(builder.build());
    }
    @Override
    default Object getDisplayArgs(String lang)
    {
        return this.getData();
    }

    default Data getData()
    {
        for(NbtCompound nbt : MZ_DATA.get(this))
        {
            for(String idV_1300 : nbt.getString("id"))
            {
                for(String tagV1300 : nbt.getString("tag"))
                {
                    return new Data(Identifier.of(idV_1300), Identifier.of(tagV1300));
                }
            }
        }
        return new Data(Identifier.ofMinecraft("air"), Identifier.of(MzRecipes.instance.MOD_ID, "error"));
    }
    default void setData(Data value)
    {
        for(NbtCompound data : MZ_DATA.revise(this))
        {
            data.put("id", value.getIdV_1300().toString());
            data.put("tag", value.getTagV1300().toString());
        }
    }
    class Data
    {
        Identifier idV_1300;
        Identifier tagV1300;
        public Data(Identifier idV_1300, Identifier tagV1300)
        {
            this.idV_1300 = idV_1300;
            this.tagV1300 = tagV1300;
        }
        public Identifier getIdV_1300()
        {
            return this.idV_1300;
        }
        public Identifier getTagV1300()
        {
            return this.tagV1300;
        }
    }
}
