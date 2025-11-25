package mz.mzrecipes.entrypoint;

import mz.mzlib.minecraft.vanilla.MzLibMinecraftInitializer;
import mz.mzlib.minecraft.vanilla.ServerModule;
import mz.mzlib.module.MzModule;
import mz.mzrecipes.MzRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class MzRecipesFabric extends MzModule implements ModInitializer
{
    public static MzRecipesFabric instance;

    {
        instance = this;
    }

    @Override
    public void onInitialize()
    {
        MzRecipes.instance.jar = FabricLoader.getInstance().getModContainer(MzRecipes.instance.MOD_ID)
            .orElseThrow(AssertionError::new).getOrigin().getPaths().get(0).toFile();
        MzLibMinecraftInitializer.instance.future.thenRun(this::load);
    }

    @Override
    public void onLoad()
    {
        this.register(new ServerModule(MzRecipes.instance));
    }
}
