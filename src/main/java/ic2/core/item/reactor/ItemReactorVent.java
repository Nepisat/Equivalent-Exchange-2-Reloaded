package ic2.core.item.reactor;

import ic2.api.reactor.IReactor;
import ic2.core.init.InternalName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemReactorVent extends ItemReactorHeatStorage
{
    public int selfVent;
    public int reactorVent;

    public ItemReactorVent(Configuration config, InternalName internalName, int heatStorage, int selfvent, int reactorvent)
    {
        super(config, internalName, heatStorage);
        this.selfVent = selfvent;
        this.reactorVent = reactorvent;
    }

    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y)
    {
        if (this.reactorVent > 0)
        {
            int rheat = reactor.getHeat();
            int reactorDrain = rheat;

            if (rheat > this.reactorVent)
            {
                reactorDrain = this.reactorVent;
            }

            rheat -= reactorDrain;

            if (this.alterHeat(reactor, yourStack, x, y, reactorDrain) > 0)
            {
                return;
            }

            reactor.setHeat(rheat);
        }

        this.alterHeat(reactor, yourStack, x, y, -this.selfVent);
    }
}
