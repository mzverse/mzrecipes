package mz.mzrecipes.entrypoint;

import mz.mzlib.MzLib;
import mz.mzrecipes.MzRecipes;
import org.bukkit.plugin.java.JavaPlugin;

public class MzRecipesBukkit extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        MzLib.instance.register(MzRecipes.instance);
    }

    @Override
    public void onDisable()
    {
        MzLib.instance.unregister(MzRecipes.instance);
    }
}
