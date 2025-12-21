package mz.mzrecipes.ui;

import mz.mzlib.minecraft.i18n.MinecraftI18n;
import mz.mzlib.minecraft.item.Item;
import mz.mzlib.minecraft.item.ItemStack;
import mz.mzlib.minecraft.recipe.Recipe;
import mz.mzlib.minecraft.recipe.RecipeRegistration;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.minecraft.ui.Ui;
import mz.mzlib.minecraft.ui.UiStack;
import mz.mzlib.minecraft.ui.window.UiWindowRect;
import mz.mzlib.minecraft.ui.window.control.UiWindowList;
import mz.mzlib.util.*;
import mz.mzrecipes.RecipeSource;
import mz.mzrecipes.kind.KindRecipeManager;
import mz.mzrecipes.kind.RegistrarKindRecipeManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FactoryUiRecipes
{
    public static Ui createEditorKinds()
    {
        UiWindowRect ui = new UiWindowRect(6);
        ui.region.addChild(UiWindowList.overlappedBuilder(
                new ArrayList<>(RegistrarKindRecipeManager.instance.registered.values()))
            .size(new Dimension(9, 6))
            .buttonPrevFirst(false).buttonNextLast(false)
            .iconGetter(entry -> entry.getElement().getKindIcon())
            .viewer(entry -> UiStack.get(entry.getPlayer()).go(createEditorRecipes(entry.getElement())))
            .listenerPageChanged(control ->
            {
                ui.setTitleGetter(player ->
                    Text.literal(String.format("kinds (%d / %d)", control.getPage() + 1, control.getPageCount())));
                ui.reopen();
            })
            .build());
        return ui;
    }
    public static <T extends Recipe, C> Ui createEditorRecipes(KindRecipeManager<T, C> manager)
    {
        UiWindowRect ui = new UiWindowRect(6);
        ui.region.addChild(UiWindowList.overlappedBuilder(manager.getRecipes().stream().sorted(Comparator.comparing(
                ThrowableFunction.of(RecipeRegistration<T>::getId).thenApply(manager::getCustomRecipe)
                    .thenApply(Option::isNone).thenApply(RuntimeUtil::castBooleanToByte))).collect(Collectors.toList()))
            .size(new Dimension(9, 10))
            .iconGetter(entry ->
            {
                RecipeRegistration<T> recipe = entry.getElement();
                ItemStack icon = Option.fromNullable(recipe.getRecipe())
                    .flatMap(r -> Option.fromOptional(r.getIcons().stream().findFirst()))
                    .map(ItemStack::clone0)
                    .unwrapOrGet(() -> ItemStack.builder().fromId("stone").build()); // TODO
                for(List<Text> lore : Item.LORE.revise(icon))
                {
                    lore.addAll(MinecraftI18n.resolveTexts(
                        entry.getPlayer(), "mzrecipes.recipe.lore",
                        MapBuilder.hashMap()
                            .put("id", recipe.getId())
                            .put(
                                "source", manager.getCustomRecipe(recipe.getId())
                                    .map(ThrowableSupplier.constant(RecipeSource.CUSTOM).ignore())
                                    .unwrapOr(RecipeSource.NATIVE)
                            )
                            .put("enabled", manager.isEnabled(recipe.getId()))
                            .get()
                    ));
                }
                return icon;
            })
            .viewer(entry -> UiStack.get(entry.getPlayer()).go(manager.view(entry.getElement().getRecipe())))
            .listenerPageChanged(control ->
            {
                ui.setTitleGetter(player ->
                    Text.literal(String.format(
                        "%s (%d / %d)", manager.getKindId(), control.getPage() + 1,
                        control.getPageCount()
                    )));
                ui.reopen();
            })
            .build());

        return ui;
    }
}
