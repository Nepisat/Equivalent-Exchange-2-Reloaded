package ic2.core.block.generator.container;

import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerSolarGenerator extends ContainerBaseGenerator
{
    public final TileEntitySolarGenerator tileEntity;
    public boolean sunIsVisible = false;
    public boolean initialized = false;

    public ContainerSolarGenerator(EntityPlayer entityPlayer, TileEntitySolarGenerator tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 80, 26);
        this.tileEntity = tileEntity;
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.sunIsVisible != this.tileEntity.sunIsVisible || !this.initialized)
            {
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.sunIsVisible ? 1 : 0);
                this.initialized = true;
            }
        }

        this.sunIsVisible = this.tileEntity.sunIsVisible;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 3:
                this.tileEntity.sunIsVisible = value != 0;

            default:
        }
    }
}
