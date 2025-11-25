package mz.mzrecipes.command;

import mz.mzlib.minecraft.command.ChildCommandRegistration;
import mz.mzlib.minecraft.command.Command;
import mz.mzlib.minecraft.permission.Permission;
import mz.mzlib.minecraft.ui.UiStack;
import mz.mzlib.module.MzModule;
import mz.mzrecipes.ui.FactoryUiRecipes;

public class CommandMzRecipesEdit extends MzModule
{
    public static CommandMzRecipesEdit instance = new CommandMzRecipesEdit();

    public Permission permission = new Permission("mzrecipes.command.mzrecipes");

    @Override
    public void onLoad()
    {
        this.register(new ChildCommandRegistration(
            CommandMzRecipes.instance.command, new Command("edit")
            .setPermissionCheckers(Command::checkPermissionSenderPlayer, Command.permissionChecker(this.permission))
            .setHandler(context ->
            {
                if(!context.successful || !context.doExecute)
                    return;
                UiStack.get(context.getSource().getPlayer().unwrap()).go(FactoryUiRecipes.createTypes());
            })
        ));
    }
}
