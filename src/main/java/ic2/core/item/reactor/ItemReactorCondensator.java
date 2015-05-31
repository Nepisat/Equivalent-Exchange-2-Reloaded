package ic2.core.item.reactor;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.init.InternalName;
import ic2.core.item.ItemGradualInt;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemReactorCondensator extends ItemGradualInt implements IReactorComponent
{
    public ItemReactorCondensator(Configuration config, InternalName internalName, int maxdmg)
    {
        super(config, internalName, maxdmg + 1);
    }

    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y) {}

    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY)
    {
        return false;
    }

    public boolean canStoreHeat(IReactor reactor, ItemStack yourStack, int x, int y)
    {
        return this.getDamageOfStack(yourStack) + 1 < super.maxDmg;
    }

    public int getMaxHeat(IReactor reactor, ItemStack yourStack, int x, int y)
    {
        return super.maxDmg;
    }

    public int getCurrentHeat(IReactor reactor, ItemStack yourStack, int x, int y)
    {
        return 0;
    }

    public int alterHeat(IReactor reactor, ItemStack yourStack, int x, int y, int heat)
    {
        int can = super.maxDmg - (this.getDamageOfStack(yourStack) + 1);

        if (can > heat)
        {
            can = heat;
        }

        heat -= can;
        this.setDamageForStack(yourStack, this.getDamageOfStack(yourStack) + can);
        return heat;
    }

    public float influenceExplosion(IReactor reactor, ItemStack yourStack)
    {
        return 0.0F;
    }
}
