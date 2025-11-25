package mz.mzrecipes;

import mz.mzlib.i18n.I18n;
import mz.mzlib.module.MzModule;
import mz.mzlib.util.RuntimeUtil;
import mz.mzrecipes.command.CommandMzRecipes;

import java.io.File;
import java.io.IOException;

public class MzRecipes extends MzModule
{
    public static MzRecipes instance = new MzRecipes();

    public final String MOD_ID = "mzrecipes";

    public File jar;

    @Override
    public void onLoad()
    {
        try
        {
            this.register(I18n.load(this.jar, "lang", 0));
        }
        catch(IOException e)
        {
            throw RuntimeUtil.sneakilyThrow(e);
        }

        this.register(CommandMzRecipes.instance);
    }
}
