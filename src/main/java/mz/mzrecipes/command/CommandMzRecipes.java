package mz.mzrecipes.command;

import mz.mzlib.minecraft.command.Command;
import mz.mzlib.minecraft.permission.Permission;
import mz.mzlib.module.MzModule;
import mz.mzrecipes.MzRecipes;

public class CommandMzRecipes extends MzModule
{
    public static CommandMzRecipes instance = new CommandMzRecipes();

    public Command command;

    public Permission permission = new Permission("mzrecipes.command.mzrecipes");

    @Override
    public void onLoad()
    {
        this.register(this.permission);
        this.register(this.command = new Command("mzrecipes", "mzr").setNamespace(MzRecipes.instance.MOD_ID)
            .setPermissionChecker(Command.permissionChecker(this.permission)));

        this.register(CommandMzRecipesEdit.instance);
    }
}
