package ic2.core.item.reactor;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.init.InternalName;
import ic2.core.item.reactor.ItemReactorHeatSwitch$ItemStackCoord;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemReactorHeatSwitch extends ItemReactorHeatStorage
{
    public int switchSide;
    public int switchReactor;

    public ItemReactorHeatSwitch(Configuration config, InternalName internalName, int heatStorage, int switchside, int switchreactor)
    {
        super(config, internalName, heatStorage);
        this.switchSide = switchside;
        this.switchReactor = switchreactor;
    }

    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y)
    {
        int myHeat = 0;
        ArrayList heatAcceptors = new ArrayList();
        double med = (double)this.getCurrentHeat(reactor, yourStack, x, y) / (double)this.getMaxHeat(reactor, yourStack, x, y);
        int c = 1;

        if (this.switchReactor > 0)
        {
            ++c;
            med += (double)reactor.getHeat() / (double)reactor.getMaxHeat();
        }

        if (this.switchSide > 0)
        {
            med += this.checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
            med += this.checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
            med += this.checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
            med += this.checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);
        }

        med /= (double)(c + heatAcceptors.size());
        int add1;

        if (this.switchSide > 0)
        {
            for (Iterator var14 = heatAcceptors.iterator(); var14.hasNext(); myHeat += add1)
            {
                ItemReactorHeatSwitch$ItemStackCoord stackcoord = (ItemReactorHeatSwitch$ItemStackCoord)var14.next();
                IReactorComponent heatable = (IReactorComponent)stackcoord.stack.getItem();
                add1 = (int)(med * (double)heatable.getMaxHeat(reactor, stackcoord.stack, stackcoord.x, stackcoord.y)) - heatable.getCurrentHeat(reactor, stackcoord.stack, stackcoord.x, stackcoord.y);

                if (add1 > this.switchSide)
                {
                    add1 = this.switchSide;
                }

                if (add1 < -this.switchSide)
                {
                    add1 = -this.switchSide;
                }

                myHeat -= add1;
                add1 = heatable.alterHeat(reactor, stackcoord.stack, stackcoord.x, stackcoord.y, add1);
            }
        }

        if (this.switchReactor > 0)
        {
            int var141 = (int)(med * (double)reactor.getMaxHeat()) - reactor.getHeat();

            if (var141 > this.switchReactor)
            {
                var141 = this.switchReactor;
            }

            if (var141 < -this.switchReactor)
            {
                var141 = -this.switchReactor;
            }

            myHeat -= var141;
            reactor.setHeat(reactor.getHeat() + var141);
        }

        this.alterHeat(reactor, yourStack, x, y, myHeat);
    }

    private double checkHeatAcceptor(IReactor reactor, int x, int y, ArrayList<ItemReactorHeatSwitch$ItemStackCoord> heatAcceptors)
    {
        ItemStack thing = reactor.getItemAt(x, y);

        if (thing != null && thing.getItem() instanceof IReactorComponent)
        {
            IReactorComponent comp = (IReactorComponent)thing.getItem();

            if (comp.canStoreHeat(reactor, thing, x, y))
            {
                heatAcceptors.add(new ItemReactorHeatSwitch$ItemStackCoord(this, thing, x, y));
                double max = (double)comp.getMaxHeat(reactor, thing, x, y);

                if (max <= 0.0D)
                {
                    return 0.0D;
                }

                double cur = (double)comp.getCurrentHeat(reactor, thing, x, y);
                return cur / max;
            }
        }

        return 0.0D;
    }
}
