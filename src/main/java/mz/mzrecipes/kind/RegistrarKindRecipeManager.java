package mz.mzrecipes.kind;

import mz.mzlib.minecraft.Identifier;
import mz.mzlib.module.IRegistrar;
import mz.mzlib.module.MzModule;
import mz.mzlib.util.RuntimeUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegistrarKindRecipeManager implements IRegistrar<KindRecipeManager<?, ?>>
{
    public static RegistrarKindRecipeManager instance = new RegistrarKindRecipeManager();

    public Map<Identifier, KindRecipeManager<?, ?>> registered = new LinkedHashMap<>();

    @Override
    public Class<KindRecipeManager<?, ?>> getType()
    {
        return RuntimeUtil.castClass(KindRecipeManager.class);
    }
    @Override
    public void register(MzModule module, KindRecipeManager object)
    {
        this.registered.put(object.getKindId(), object);
    }
    @Override
    public void unregister(MzModule module, KindRecipeManager object)
    {
        this.registered.remove(object.getKindId());
    }
}
