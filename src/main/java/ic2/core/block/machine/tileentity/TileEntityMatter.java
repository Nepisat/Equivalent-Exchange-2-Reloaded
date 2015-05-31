package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.recipe.Recipes;
import ic2.core.ContainerBase;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.ContainerMatter;
import ic2.core.block.machine.gui.GuiMatter;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMatter extends TileEntityElectricMachine implements IHasGui
{
    public int soundTicker;
    public int scrap = 0;
    private final int StateIdle = 0;
    private final int StateRunning = 1;
    private final int StateRunningScrap = 2;
    private int state = 0;
    private int prevState = 0;
    private AudioSource audioSource;
    private AudioSource audioSourceScrap;
    public final InvSlotProcessableGeneric amplifierSlot;
    public final InvSlotOutput outputSlot;

    public TileEntityMatter()
    {
        super(1000000, 3, -1);
        this.soundTicker = IC2.random.nextInt(32);
        this.amplifierSlot = new InvSlotProcessableGeneric(this, "scrap", 0, 1, Recipes.matterAmplifier);
        this.outputSlot = new InvSlotOutput(this, "output", 1, 1);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        try
        {
            this.scrap = nbttagcompound.getInteger("scrap");
        }
        catch (Throwable var3)
        {
            this.scrap = nbttagcompound.getShort("scrap");
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("scrap", this.scrap);
    }

    public String getInvName()
    {
        return "Mass Fabricator";
    }

    public void updateEntity()
    {
        super.updateEntity();

        if (!this.isRedstonePowered() && super.energy > 0)
        {
            this.setState(this.scrap > 0 ? 2 : 1);
            this.setActive(true);
            boolean needsInvUpdate = false;

            if (this.scrap < 1000)
            {
                Integer amplifier = (Integer)this.amplifierSlot.processRaw(false);

                if (amplifier != null && amplifier.intValue() > 0)
                {
                    this.scrap += amplifier.intValue();
                }
            }

            if (super.energy >= 1000000)
            {
                needsInvUpdate = this.attemptGeneration();
            }

            if (needsInvUpdate)
            {
                this.onInventoryChanged();
            }
        }
        else
        {
            this.setState(0);
            this.setActive(false);
        }
    }

    public void onUnloaded()
    {
        if (IC2.platform.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
            this.audioSourceScrap = null;
        }

        super.onUnloaded();
    }

    public boolean attemptGeneration()
    {
        if (this.outputSlot.add(Ic2Items.matter.copy()) == 0)
        {
            super.energy -= 1000000;
            return true;
        }
        else
        {
            return false;
        }
    }

    public int demandsEnergy()
    {
        return this.isRedstonePowered() ? 0 : super.maxEnergy - super.energy;
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (amount > this.getMaxSafeInput())
        {
            super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, 0, 0, 7);
            ExplosionIC2 bonus1 = new ExplosionIC2(super.worldObj, (Entity)null, (double)((float)super.xCoord + 0.5F), (double)((float)super.yCoord + 0.5F), (double)((float)super.zCoord + 0.5F), 15.0F, 0.01F, 1.5F);
            bonus1.doExplosion();
            return 0;
        }
        else if (super.energy < super.maxEnergy && !this.isRedstonePowered())
        {
            int bonus = Math.min(amount, this.scrap);
            this.scrap -= bonus;
            super.energy += amount + 5 * bonus;
            return 0;
        }
        else
        {
            return amount;
        }
    }

    public int getMaxSafeInput()
    {
        return 1000000;
    }

    public String getProgressAsString()
    {
        int p = super.energy / 10000;

        if (p > 100)
        {
            p = 100;
        }

        return "" + p + "%";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerMatter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiMatter(new ContainerMatter(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    private void setState(int state)
    {
        this.state = state;

        if (this.prevState != state)
        {
            IC2.network.updateTileEntityField(this, "state");
        }

        this.prevState = state;
    }

    public List<String> getNetworkedFields()
    {
        Vector ret = new Vector(1);
        ret.add("state");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field)
    {
        if (field.equals("state") && this.prevState != this.state)
        {
            switch (this.state)
            {
                case 0:
                    if (this.audioSource != null)
                    {
                        this.audioSource.stop();
                    }

                    if (this.audioSourceScrap != null)
                    {
                        this.audioSourceScrap.stop();
                    }

                    break;

                case 1:
                    if (this.audioSource == null)
                    {
                        this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabLoop.ogg", true, false, IC2.audioManager.defaultVolume);
                    }

                    if (this.audioSource != null)
                    {
                        this.audioSource.play();
                    }

                    if (this.audioSourceScrap != null)
                    {
                        this.audioSourceScrap.stop();
                    }

                    break;

                case 2:
                    if (this.audioSource == null)
                    {
                        this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabLoop.ogg", true, false, IC2.audioManager.defaultVolume);
                    }

                    if (this.audioSourceScrap == null)
                    {
                        this.audioSourceScrap = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabScrapSolo.ogg", true, false, IC2.audioManager.defaultVolume);
                    }

                    if (this.audioSource != null)
                    {
                        this.audioSource.play();
                    }

                    if (this.audioSourceScrap != null)
                    {
                        this.audioSourceScrap.play();
                    }
            }

            this.prevState = this.state;
        }

        super.onNetworkUpdate(field);
    }

    public float getWrenchDropRate()
    {
        return 0.7F;
    }

    public boolean amplificationIsAvailable()
    {
        if (this.scrap > 0)
        {
            return true;
        }
        else
        {
            Integer amplifier = (Integer)this.amplifierSlot.processRaw(false);
            return amplifier != null && amplifier.intValue() > 0;
        }
    }
}
