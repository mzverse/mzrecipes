package mz.mzrecipes.entrypoint;

import mz.mzlib.minecraft.vanilla.MzLibMinecraftInitializer;
import mz.mzlib.minecraft.vanilla.ServerModule;
import mz.mzlib.module.MzModule;
import mz.mzrecipes.MzRecipes;
import net.fabricmc.api.ModInitializer;

public class MzRecipesFabric extends MzModule implements ModInitializer
{
    public static MzRecipesFabric instance;

    {
        instance = this;
    }

    @Override
    public void onInitialize()
    {
        MzLibMinecraftInitializer.instance.future.thenRun(this::load);
    }

    @Override
    public void onLoad()
    {
        this.register(new ServerModule(MzRecipes.instance));
    }
}
