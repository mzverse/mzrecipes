package mz.mzrecipes;

import mz.mzlib.module.MzModule;
import mz.mzrecipes.command.CommandMzRecipes;

public class MzRecipes extends MzModule
{
    public static MzRecipes instance = new MzRecipes();

    public final String MOD_ID = "mzrecipes";

    @Override
    public void onLoad()
    {
        this.register(CommandMzRecipes.instance);
    }
}
