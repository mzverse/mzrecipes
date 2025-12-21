package mz.mzrecipes.kind;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.minecraft.inventory.Inventory;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.*;
import mz.mzlib.minecraft.recipe.crafting.RecipeCraftingShaped;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.minecraft.ui.window.UiWindow;
import mz.mzlib.minecraft.ui.window.UiWindowRect;
import mz.mzlib.minecraft.ui.window.UiWindowUtil;
import mz.mzlib.minecraft.ui.window.control.UiWindowControl;
import mz.mzlib.minecraft.ui.window.control.UiWindowControlReadOnly;
import mz.mzlib.util.Either;
import mz.mzlib.util.Instance;
import mz.mzlib.util.Option;
import mz.mzlib.util.ThrowableSupplier;
import mz.mzrecipes.recipe.CustomRecipeCraftingShaped;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class KindRecipeManagerShaped extends KindRecipeManager<RecipeCraftingShaped, CustomRecipeCraftingShaped> implements Instance
{
    public static KindRecipeManagerShaped instance = new KindRecipeManagerShaped();

    public Map<Identifier, CustomRecipeCraftingShaped> customRecipes = new HashMap<>();

    @Override
    public Identifier getKindId()
    {
        return Identifier.ofMinecraft("crafting_shaped");
    }
    @Override
    public ItemStack getKindIcon()
    {
        return ItemStack.builder().fromId("minecraft:crafting_table").customName(Text.literal("Crafting Shaped"))
            .build();
    }
    @Override
    public List<RecipeRegistration<RecipeCraftingShaped>> getRecipes()
    {
        List<RecipeRegistration<RecipeCraftingShaped>> result = RegistrarRecipeVanilla.instance.getEnabledRecipes()
            .get(RecipeType.CRAFTING).entrySet().stream()
            .filter(entry -> entry.getValue() instanceof RecipeCraftingShaped)
            .map(entry -> new RecipeRegistration<>(entry.getKey(), (RecipeCraftingShaped) entry.getValue()))
            .collect(Collectors.toList());
        RegistrarRecipeVanilla.instance.getDisablings().getOrDefault(RecipeType.CRAFTING, Collections.emptyMap())
            .values().stream()
            .filter(disabling -> disabling.getRecipe().filter(RecipeCraftingShaped.class).isSome())
            .map(disabling -> RecipeRegistration.of(
                disabling.getId(),
                disabling.getRecipe().filter(RecipeCraftingShaped.class).toNullable()
            ))
            .forEach(result::add);
        return result;
    }
    @Override
    public boolean isEnabled(Identifier recipe)
    {
        return !RegistrarRecipeVanilla.instance.getDisablings()
            .getOrDefault(RecipeType.CRAFTING, Collections.emptyMap()).containsKey(recipe);
    }

    @Override
    public Option<CustomRecipeCraftingShaped> getCustomRecipe(Identifier id)
    {
        return Option.fromNullable(this.customRecipes.get(id));
    }

    @Override
    public Identifier getId(CustomRecipeCraftingShaped recipe)
    {
        return recipe.getId();
    }

    @Override
    public UiWindowRect view(RecipeCraftingShaped recipe)
    {
        return this.view(Either.first(recipe), false);
    }

    @Override
    public UiWindowRect view(CustomRecipeCraftingShaped customRecipe, boolean modifiable)
    {
        return this.view(Either.second(customRecipe), modifiable);
    }

    public UiWindowRect view(Either<RecipeCraftingShaped, CustomRecipeCraftingShaped> recipe, boolean modifiable)
    {
        UiWindowRect result = new UiWindowRect(6);
        result.region.addChild(new View(recipe, modifiable));
        result.setTitleGetter(player -> Text.literal("Crafting Shaped"));
        return result;
    }

    public static class View extends UiWindowControlReadOnly
    {
        Either<RecipeCraftingShaped, CustomRecipeCraftingShaped> recipe;
        boolean modifiable;
        public View(Either<RecipeCraftingShaped, CustomRecipeCraftingShaped> recipe, boolean modifiable)
        {
            super(new Rectangle(9, 6));
            if(modifiable && recipe.isFirst())
                throw new IllegalArgumentException("Only custom recipes can be modified");
            this.recipe = recipe;
            this.modifiable = modifiable;

            this.setBackground(player -> ItemStack.builder().stainedGlassPane().lightBlue().emptyName().build());
            this.addChild(UiWindowUtil.buttonBack(new Point(0, 0)));
            this.addChild(this.new ControlIngredients(new Point(1, 2)));
        }

        class ControlIngredients extends UiWindowControl
        {
            public ControlIngredients(Point location)
            {
                super(new Rectangle(location, new Dimension(3, 3)));
            }

            public void init()
            {
                Inventory inventory = ((UiWindow) this.getUi()).getInventory();
                for(int x = 0; x < recipe.map(RecipeCraftingShaped::getWidth, ThrowableSupplier.constant(3).ignore()); x++)
                {
                    for(int y = 0; y < recipe.map(RecipeCraftingShaped::getHeight, ThrowableSupplier.constant(3).ignore()); y++)
                    {
                        for(RecipeCraftingShaped r : recipe.getFirst()) // native
                        {
                            for(IngredientVanilla ingredient : r.getIngredients().get(x + y * r.getWidth()))
                            {
                                inventory.setItemStack(this.getIndex(new Point(x, y)), ingredient.getMatchingStacks().stream().findFirst().orElseGet(
                                    () -> ItemStack.builder().fromId("minecraft:stone")
                                        .customName(Text.literal("no matching items")).build()));
                            }
                        }
                        for(CustomRecipeCraftingShaped r : recipe.getSecond()) // custom
                        {
                            inventory.setItemStack(this.getIndex(new Point(x, y)), r.getIngredients().get(x + y * 3));
                        }
                    }
                }
            }

            @Override
            public boolean canTake(Point point)
            {
                return View.this.modifiable;
            }

            @Override
            public boolean canPlace(Point point)
            {
                return View.this.modifiable;
            }
        }
    }
}
