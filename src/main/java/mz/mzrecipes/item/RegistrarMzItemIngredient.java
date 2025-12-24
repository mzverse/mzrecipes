package mz.mzrecipes.item;

import mz.mzlib.minecraft.MinecraftPlatform;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.mzitem.RegistrarMzItem;
import mz.mzlib.minecraft.recipe.Ingredient;
import mz.mzlib.minecraft.recipe.IngredientVanilla;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.module.IRegistrar;
import mz.mzlib.module.MzModule;
import mz.mzlib.util.Instance;
import mz.mzlib.util.Option;
import mz.mzlib.util.RuntimeUtil;

import java.util.HashMap;
import java.util.Map;

public class RegistrarMzItemIngredient implements IRegistrar<MzItemIngredient>, Instance
{
    public static final RegistrarMzItemIngredient instance = RuntimeUtil.nul();

    @Override
    public Class<MzItemIngredient> getType()
    {
        return MzItemIngredient.class;
    }

    Map<Ingredient, MzItemIngredient> registry = new HashMap<>();

    @Override
    public void register(MzModule module, MzItemIngredient object)
    {
        this.registry.put(object.asIngredient(), object);
    }
    @Override
    public void unregister(MzModule module, MzItemIngredient object)
    {
        this.registry.remove(object.asIngredient(), object);
    }

    public Option<Ingredient> compile(ItemStack itemStack)
    {
        if(itemStack.isEmpty())
            return Option.none();
        return Option.some(RegistrarMzItem.instance.toMzItem(itemStack).filter(MzItemIngredient.class)
            .map(MzItemIngredient::asIngredient)
            .unwrapOrGet(() -> IngredientVanilla.of(itemStack))
            .withCount(itemStack.getCount())
        );
    }
    public ItemStack decompile(IngredientVanilla ingredient)
    {
        for(MzItemIngredient result : Option.fromNullable(registry.get(ingredient)))
            return result.copy();
        ItemStack result;
        if(MinecraftPlatform.instance.getVersion() < 1300)
        {
            result = ingredient.asItemStackV_1200(); // TODO V1200_1300
            if(result.getDamageV_1300() == Short.MAX_VALUE)
            {
                MzItemIngredientId display1 = RegistrarMzItem.instance.newMzItem(
                    MzItemIngredientId.class);
                display1.setIngredientId(result.getItem().getId());
                result = display1;
            }
        }
        else
            result = ingredient.getMatchingStacksV1300().stream()
                .findFirst().orElseGet(
                    () -> ItemStack.builder().fromId("minecraft:stone")
                        .customName(Text.literal("no matching items")).build());
        return result;
    }
}
