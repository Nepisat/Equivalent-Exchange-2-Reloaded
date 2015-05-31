package ic2.core.block;

import ic2.api.crops.BaseSeed;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.crop.IC2Crops;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.item.ItemCropSeed;
import ic2.core.util.StackUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityCrop extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, ICropTile
{
    public short id = -1;
    public byte size = 0;
    public byte statGrowth = 0;
    public byte statGain = 0;
    public byte statResistance = 0;
    public byte scanLevel = 0;
    public NBTTagCompound customData = new NBTTagCompound();
    public int nutrientStorage = 0;
    public int waterStorage = 0;
    public int exStorage = 0;
    public int growthPoints = 0;
    public boolean upgraded = false;
    public char ticker;
    public boolean dirty;
    public static char tickRate = 256;
    public byte humidity;
    public byte nutrients;
    public byte airQuality;

    public TileEntityCrop()
    {
        this.ticker = (char)IC2.random.nextInt(tickRate);
        this.dirty = true;
        this.humidity = -1;
        this.nutrients = -1;
        this.airQuality = -1;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.id = nbttagcompound.getShort("cropid");
        this.size = nbttagcompound.getByte("size");
        this.statGrowth = nbttagcompound.getByte("statGrowth");
        this.statGain = nbttagcompound.getByte("statGain");
        this.statResistance = nbttagcompound.getByte("statResistance");

        if (nbttagcompound.hasKey("data0"))
        {
            for (int var3 = 0; var3 < 16; ++var3)
            {
                this.customData.setShort("legacy" + var3, nbttagcompound.getShort("data" + var3));
            }
        }
        else if (nbttagcompound.hasKey("customData"))
        {
            this.customData = nbttagcompound.getCompoundTag("customData");
        }

        this.growthPoints = nbttagcompound.getInteger("growthPoints");

        try
        {
            this.nutrientStorage = nbttagcompound.getInteger("nutrientStorage");
            this.waterStorage = nbttagcompound.getInteger("waterStorage");
        }
        catch (Throwable var31)
        {
            this.nutrientStorage = nbttagcompound.getByte("nutrientStorage");
            this.waterStorage = nbttagcompound.getByte("waterStorage");
        }

        this.upgraded = nbttagcompound.getBoolean("upgraded");
        this.scanLevel = nbttagcompound.getByte("scanLevel");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("cropid", this.id);
        nbttagcompound.setByte("size", this.size);
        nbttagcompound.setByte("statGrowth", this.statGrowth);
        nbttagcompound.setByte("statGain", this.statGain);
        nbttagcompound.setByte("statResistance", this.statResistance);
        nbttagcompound.setCompoundTag("customData", this.customData);
        nbttagcompound.setInteger("growthPoints", this.growthPoints);
        nbttagcompound.setInteger("nutrientStorage", this.nutrientStorage);
        nbttagcompound.setInteger("waterStorage", this.waterStorage);
        nbttagcompound.setBoolean("upgraded", this.upgraded);
        nbttagcompound.setByte("scanLevel", this.scanLevel);
    }

    public void updateEntity()
    {
        super.updateEntity();
        ++this.ticker;

        if (this.ticker % tickRate == 0)
        {
            this.tick();
        }

        if (this.dirty)
        {
            this.dirty = false;
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            super.worldObj.updateLightByType(EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord);

            if (IC2.platform.isSimulating())
            {
                IC2.network.announceBlockUpdate(super.worldObj, super.xCoord, super.yCoord, super.zCoord);

                if (!IC2.platform.isRendering())
                {
                    Iterator i$ = this.getNetworkedFields().iterator();

                    while (i$.hasNext())
                    {
                        String field = (String)i$.next();
                        IC2.network.updateTileEntityField(this, field);
                    }
                }
            }
        }
    }

    public List<String> getNetworkedFields()
    {
        Vector ret = new Vector(4);
        ret.add("id");
        ret.add("size");
        ret.add("upgraded");
        ret.add("customData");
        return ret;
    }

    public void tick()
    {
        if (IC2.platform.isSimulating())
        {
            if (this.ticker % (tickRate << 2) == 0)
            {
                this.humidity = this.updateHumidity();
            }

            if ((this.ticker + tickRate) % (tickRate << 2) == 0)
            {
                this.nutrients = this.updateNutrients();
            }

            if ((this.ticker + tickRate * 2) % (tickRate << 2) == 0)
            {
                this.airQuality = this.updateAirQuality();
            }

            if (this.id < 0 && (!this.upgraded || !this.attemptCrossing()))
            {
                if (IC2.random.nextInt(100) != 0 || this.hasEx())
                {
                    if (this.exStorage > 0 && IC2.random.nextInt(10) == 0)
                    {
                        --this.exStorage;
                    }

                    return;
                }

                this.reset();
                this.id = (short)IC2Crops.weed.getId();
                this.size = 1;
            }

            this.crop().tick(this);

            if (this.crop().canGrow(this))
            {
                this.growthPoints += this.calcGrowthRate();

                if (this.id > -1 && this.growthPoints >= this.crop().growthDuration(this))
                {
                    this.growthPoints = 0;
                    ++this.size;
                    this.dirty = true;
                }
            }

            if (this.nutrientStorage > 0)
            {
                --this.nutrientStorage;
            }

            if (this.waterStorage > 0)
            {
                --this.waterStorage;
            }

            if (this.crop().isWeed(this) && IC2.random.nextInt(50) - this.statGrowth <= 2)
            {
                this.generateWeed();
            }
        }
    }

    public void generateWeed()
    {
        int x = super.xCoord;
        int y = super.yCoord;
        int z = super.zCoord;

        switch (IC2.random.nextInt(4))
        {
            case 0:
                ++x;

            case 1:
                --x;

            case 2:
                ++z;

            case 3:
                --z;
        }

        if (super.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityCrop)
        {
            TileEntityCrop var6 = (TileEntityCrop)super.worldObj.getBlockTileEntity(x, y, z);

            if (var6.id == -1 || !var6.crop().isWeed(var6) && IC2.random.nextInt(32) >= var6.statResistance && !var6.hasEx())
            {
                byte newGrowth = this.statGrowth;

                if (var6.statGrowth > newGrowth)
                {
                    newGrowth = var6.statGrowth;
                }

                if (newGrowth < 31 && IC2.random.nextBoolean())
                {
                    ++newGrowth;
                }

                var6.reset();
                var6.id = 0;
                var6.size = 1;
                var6.statGrowth = newGrowth;
            }
        }
        else if (super.worldObj.getBlockId(x, y, z) == 0)
        {
            int var61 = super.worldObj.getBlockId(x, y - 1, z);

            if (var61 == Block.dirt.blockID || var61 == Block.grass.blockID || var61 == Block.tilledField.blockID)
            {
                super.worldObj.setBlock(x, y - 1, z, Block.grass.blockID, 0, 7);
                super.worldObj.setBlock(x, y, z, Block.tallGrass.blockID, 1, 7);
            }
        }
    }

    public boolean hasEx()
    {
        if (this.exStorage > 0)
        {
            this.exStorage -= 5;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean attemptCrossing()
    {
        if (IC2.random.nextInt(3) != 0)
        {
            return false;
        }
        else
        {
            LinkedList crops = new LinkedList();
            this.askCropJoinCross(super.xCoord - 1, super.yCoord, super.zCoord, crops);
            this.askCropJoinCross(super.xCoord + 1, super.yCoord, super.zCoord, crops);
            this.askCropJoinCross(super.xCoord, super.yCoord, super.zCoord - 1, crops);
            this.askCropJoinCross(super.xCoord, super.yCoord, super.zCoord + 1, crops);

            if (crops.size() < 2)
            {
                return false;
            }
            else
            {
                int[] ratios = new int[256];
                int total;

                for (total = 1; total < ratios.length; ++total)
                {
                    CropCard var6 = Crops.instance.getCropList()[total];

                    if (var6 != null && var6.canGrow(this))
                    {
                        for (int j = 0; j < crops.size(); ++j)
                        {
                            ratios[total] += this.calculateRatioFor(var6, ((TileEntityCrop)crops.get(j)).crop());
                        }
                    }
                }

                total = 0;
                int var61;

                for (var61 = 0; var61 < ratios.length; ++var61)
                {
                    total += ratios[var61];
                }

                total = IC2.random.nextInt(total);

                for (var61 = 0; var61 < ratios.length; ++var61)
                {
                    if (ratios[var61] > 0 && ratios[var61] > total)
                    {
                        total = var61;
                        break;
                    }

                    total -= ratios[var61];
                }

                this.upgraded = false;
                this.id = (short)total;
                this.dirty = true;
                this.size = 1;
                this.statGrowth = 0;
                this.statResistance = 0;
                this.statGain = 0;

                for (var61 = 0; var61 < crops.size(); ++var61)
                {
                    this.statGrowth += ((TileEntityCrop)crops.get(var61)).statGrowth;
                    this.statResistance += ((TileEntityCrop)crops.get(var61)).statResistance;
                    this.statGain += ((TileEntityCrop)crops.get(var61)).statGain;
                }

                var61 = crops.size();
                this.statGrowth = (byte)(this.statGrowth / var61);
                this.statResistance = (byte)(this.statResistance / var61);
                this.statGain = (byte)(this.statGain / var61);
                this.statGrowth = (byte)(this.statGrowth + (IC2.random.nextInt(1 + 2 * var61) - var61));

                if (this.statGrowth < 0)
                {
                    this.statGrowth = 0;
                }

                if (this.statGrowth > 31)
                {
                    this.statGrowth = 31;
                }

                this.statGain = (byte)(this.statGain + (IC2.random.nextInt(1 + 2 * var61) - var61));

                if (this.statGain < 0)
                {
                    this.statGain = 0;
                }

                if (this.statGain > 31)
                {
                    this.statGain = 31;
                }

                this.statResistance = (byte)(this.statResistance + (IC2.random.nextInt(1 + 2 * var61) - var61));

                if (this.statResistance < 0)
                {
                    this.statResistance = 0;
                }

                if (this.statResistance > 31)
                {
                    this.statResistance = 31;
                }

                return true;
            }
        }
    }

    public int calculateRatioFor(CropCard a, CropCard b)
    {
        if (a == b)
        {
            return 500;
        }
        else
        {
            int value = 0;
            int i = 0;
            int j;

            while (i < 5)
            {
                j = a.stat(i) - b.stat(i);

                if (j < 0)
                {
                    j *= -1;
                }

                switch (j)
                {
                    default:
                        --value;

                    case 0:
                        value += 2;

                    case 1:
                        ++value;

                    case 2:
                        ++i;
                }
            }

            for (i = 0; i < a.attributes().length; ++i)
            {
                for (j = 0; j < b.attributes().length; ++j)
                {
                    if (a.attributes()[i].equalsIgnoreCase(b.attributes()[j]))
                    {
                        value += 5;
                    }
                }
            }

            if (b.tier() < a.tier() - 1)
            {
                value -= 2 * (a.tier() - b.tier());
            }

            if (b.tier() - 3 > a.tier())
            {
                value -= b.tier() - a.tier();
            }

            if (value < 0)
            {
                value = 0;
            }

            return value;
        }
    }

    public void askCropJoinCross(int x, int y, int z, LinkedList<TileEntityCrop> crops)
    {
        if (super.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityCrop)
        {
            TileEntityCrop sidecrop = (TileEntityCrop)super.worldObj.getBlockTileEntity(x, y, z);

            if (sidecrop.id > 0 && sidecrop.crop().canGrow(this) && sidecrop.crop().canCross(sidecrop))
            {
                int base = 4;

                if (sidecrop.statGrowth >= 16)
                {
                    ++base;
                }

                if (sidecrop.statGrowth >= 30)
                {
                    ++base;
                }

                if (sidecrop.statResistance >= 28)
                {
                    base += 27 - sidecrop.statResistance;
                }

                if (base >= IC2.random.nextInt(20))
                {
                    crops.add(sidecrop);
                }
            }
        }
    }

    public boolean leftclick(EntityPlayer player)
    {
        if (this.id < 0)
        {
            if (this.upgraded)
            {
                this.upgraded = false;
                this.dirty = true;

                if (IC2.platform.isSimulating())
                {
                    StackUtil.dropAsEntity(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(Ic2Items.crop.getItem()));
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return this.crop().leftclick(this, player);
        }
    }

    public boolean pick(boolean manual)
    {
        if (this.id < 0)
        {
            return false;
        }
        else
        {
            boolean bonus = this.harvest(false);
            float firstchance = this.crop().dropSeedChance(this);
            int drop;

            for (drop = 0; drop < this.statResistance; ++drop)
            {
                firstchance *= 1.1F;
            }

            drop = 0;
            int x;

            if (bonus)
            {
                if (IC2.random.nextFloat() <= (firstchance + 1.0F) * 0.8F)
                {
                    ++drop;
                }

                float var7 = this.crop().dropSeedChance(this) + (float)this.statGrowth / 100.0F;

                if (!manual)
                {
                    var7 *= 0.8F;
                }

                for (x = 23; x < this.statGain; ++x)
                {
                    var7 *= 0.95F;
                }

                if (IC2.random.nextFloat() <= var7)
                {
                    ++drop;
                }
            }
            else if (IC2.random.nextFloat() <= firstchance * 1.5F)
            {
                ++drop;
            }

            ItemStack[] var71 = new ItemStack[drop];

            for (x = 0; x < drop; ++x)
            {
                var71[x] = this.crop().getSeeds(this);
            }

            this.reset();

            if (IC2.platform.isSimulating() && var71 != null && var71.length > 0)
            {
                for (x = 0; x < var71.length; ++x)
                {
                    if (var71[x].itemID != Ic2Items.cropSeed.itemID)
                    {
                        var71[x].stackTagCompound = null;
                    }

                    StackUtil.dropAsEntity(super.worldObj, super.xCoord, super.yCoord, super.zCoord, var71[x]);
                }
            }

            return true;
        }
    }

    public boolean rightclick(EntityPlayer player)
    {
        ItemStack current = player.getCurrentEquippedItem();

        if (current != null)
        {
            if (this.id < 0)
            {
                if (current.itemID == Ic2Items.crop.itemID && !this.upgraded)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        --current.stackSize;

                        if (current.stackSize <= 0)
                        {
                            player.inventory.mainInventory[player.inventory.currentItem] = null;
                        }
                    }

                    this.upgraded = true;
                    this.dirty = true;
                    return true;
                }

                if (this.applyBaseSeed(current, player))
                {
                    return true;
                }
            }
            else if (current.itemID == Ic2Items.cropnalyzer.itemID)
            {
                if (IC2.platform.isSimulating())
                {
                    String desc = this.getScanned();

                    if (desc == null)
                    {
                        desc = "Unknown Crop";
                    }

                    IC2.platform.messagePlayer(player, desc, new Object[0]);
                }

                return true;
            }

            if (current.itemID == Item.bucketWater.itemID || current.itemID == Ic2Items.waterCell.getItem().itemID)
            {
                if (this.waterStorage < 10)
                {
                    this.waterStorage = 10;
                    return true;
                }

                return current.itemID == Item.bucketWater.itemID;
            }

            if (current.itemID == Item.seeds.itemID)
            {
                if (this.nutrientStorage <= 50)
                {
                    this.nutrientStorage += 25;
                    --current.stackSize;

                    if (current.stackSize <= 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }

                    return true;
                }

                return false;
            }

            if (current.itemID == Item.dyePowder.itemID && current.getItemDamage() == 15 || current.itemID == Ic2Items.fertilizer.itemID)
            {
                if (this.applyFertilizer(true))
                {
                    --current.stackSize;

                    if (current.stackSize <= 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }

                    return true;
                }

                return false;
            }

            if (current.itemID == Ic2Items.hydratingCell.itemID)
            {
                if (this.applyHydration(true, current))
                {
                    if (current.stackSize <= 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }

                    return true;
                }

                return false;
            }

            if (current.itemID == Ic2Items.weedEx.itemID && this.applyWeedEx(true))
            {
                current.damageItem(1, player);

                if (current.stackSize <= 0)
                {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                }

                return true;
            }
        }

        return this.id < 0 ? false : this.crop().rightclick(this, player);
    }

    public boolean applyBaseSeed(ItemStack current, EntityPlayer player)
    {
        BaseSeed seed = Crops.instance.getBaseSeed(current);

        if (seed != null)
        {
            if (current.stackSize < seed.stackSize)
            {
                return false;
            }

            if (this.tryPlantIn(seed.id, seed.size, seed.statGrowth, seed.statGain, seed.statResistance, 1))
            {
                if (current.getItem().hasContainerItem())
                {
                    current = current.getItem().getContainerItemStack(current);
                }
                else
                {
                    current.stackSize -= seed.stackSize;

                    if (current.stackSize <= 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean tryPlantIn(int i, int si, int statGr, int statGa, int statRe, int scan)
    {
        if (this.id <= -1 && i > 0 && !this.upgraded)
        {
            if (!Crops.instance.getCropList()[i].canGrow(this))
            {
                return false;
            }
            else
            {
                this.reset();
                this.id = (short)i;
                this.size = (byte)si;
                this.statGrowth = (byte)statGr;
                this.statGain = (byte)statGa;
                this.statResistance = (byte)statRe;
                this.scanLevel = (byte)scan;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean applyFertilizer(boolean manual)
    {
        if (this.nutrientStorage >= 100)
        {
            return false;
        }
        else
        {
            this.nutrientStorage += manual ? 100 : 90;
            return true;
        }
    }

    public boolean applyHydration(boolean manual, InvSlotConsumable hydrationSlot)
    {
        if ((manual || this.waterStorage < 180) && this.waterStorage < 200)
        {
            int apply = manual ? 200 - this.waterStorage : 180 - this.waterStorage;
            ItemStack affected = hydrationSlot.damage(apply, (EntityLivingBase)null);
            apply = affected.stackSize * affected.getMaxDamage() + affected.getItemDamage();
            this.waterStorage += apply;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean applyHydration(boolean manual, ItemStack itemStack)
    {
        if ((manual || this.waterStorage < 180) && this.waterStorage < 200)
        {
            int apply = manual ? 200 - this.waterStorage : 180 - this.waterStorage;
            apply = Math.min(apply, itemStack.getMaxDamage() - itemStack.getItemDamage());

            if (itemStack.attemptDamageItem(apply, new Random()))
            {
                --itemStack.stackSize;
                itemStack.setItemDamage(0);
            }

            this.waterStorage += apply;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean applyWeedEx(boolean manual)
    {
        if ((this.exStorage < 100 || !manual) && this.exStorage < 150)
        {
            this.exStorage += 50;
            boolean trigger = super.worldObj.rand.nextInt(3) == 0;

            if (manual)
            {
                trigger = super.worldObj.rand.nextInt(5) == 0;
            }

            if (this.id > 0 && this.exStorage >= 75 && trigger)
            {
                switch (super.worldObj.rand.nextInt(5))
                {
                    case 0:
                        if (this.statGrowth > 0)
                        {
                            --this.statGrowth;
                        }

                    case 1:
                        if (this.statGain > 0)
                        {
                            --this.statGain;
                        }

                    default:
                        if (this.statResistance > 0)
                        {
                            --this.statResistance;
                        }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean harvest(boolean manual)
    {
        if (this.id >= 0 && this.crop().canBeHarvested(this))
        {
            float chance = this.crop().dropGainChance();
            int drop;

            for (drop = 0; drop < this.statGain; ++drop)
            {
                chance *= 1.03F;
            }

            chance -= IC2.random.nextFloat();

            for (drop = 0; chance > 0.0F; chance -= IC2.random.nextFloat())
            {
                ++drop;
            }

            ItemStack[] re = new ItemStack[drop];
            int x;

            for (x = 0; x < drop; ++x)
            {
                re[x] = this.crop().getGain(this);

                if (re[x] != null && IC2.random.nextInt(100) <= this.statGain)
                {
                    ++re[x].stackSize;
                }
            }

            this.size = this.crop().getSizeAfterHarvest(this);
            this.dirty = true;

            if (IC2.platform.isSimulating() && re != null && re.length > 0)
            {
                for (x = 0; x < re.length; ++x)
                {
                    StackUtil.dropAsEntity(super.worldObj, super.xCoord, super.yCoord, super.zCoord, re[x]);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void onNeighbourChange()
    {
        if (this.id >= 0)
        {
            this.crop().onNeighbourChange(this);
        }
    }

    public int emitRedstone()
    {
        return this.id < 0 ? 0 : this.crop().emitRedstone(this);
    }

    public void onBlockDestroyed()
    {
        if (this.id >= 0)
        {
            this.crop().onBlockDestroyed(this);
        }
    }

    public int getEmittedLight()
    {
        return this.id < 0 ? 0 : this.crop().getEmittedLight(this);
    }

    public byte getHumidity()
    {
        if (this.humidity == -1)
        {
            this.humidity = this.updateHumidity();
        }

        return this.humidity;
    }

    public byte getNutrients()
    {
        if (this.nutrients == -1)
        {
            this.nutrients = this.updateNutrients();
        }

        return this.nutrients;
    }

    public byte getAirQuality()
    {
        if (this.airQuality == -1)
        {
            this.airQuality = this.updateAirQuality();
        }

        return this.airQuality;
    }

    public byte updateHumidity()
    {
        int value = Crops.instance.getHumidityBiomeBonus(super.worldObj.getBiomeGenForCoords(super.xCoord, super.zCoord));

        if (super.worldObj.getBlockMetadata(super.xCoord, super.yCoord - 1, super.zCoord) >= 7)
        {
            value += 2;
        }

        if (this.waterStorage >= 5)
        {
            value += 2;
        }

        value += (this.waterStorage + 24) / 25;
        return (byte)value;
    }

    public byte updateNutrients()
    {
        int value = Crops.instance.getNutrientBiomeBonus(super.worldObj.getBiomeGenForCoords(super.xCoord, super.zCoord));

        for (int i = 2; i < 5 && super.worldObj.getBlockId(super.xCoord, super.yCoord - i, super.zCoord) == Block.dirt.blockID; ++i)
        {
            ++value;
        }

        value += (this.nutrientStorage + 19) / 20;
        return (byte)value;
    }

    public byte updateAirQuality()
    {
        byte value = 0;
        int height = (super.yCoord - 64) / 15;

        if (height > 4)
        {
            height = 4;
        }

        if (height < 0)
        {
            height = 0;
        }

        int var6 = value + height;
        int fresh = 9;

        for (int x = super.xCoord - 1; x < super.xCoord + 1 && fresh > 0; ++x)
        {
            for (int z = super.zCoord - 1; z < super.zCoord + 1 && fresh > 0; ++z)
            {
                if (super.worldObj.isBlockOpaqueCube(x, super.yCoord, z) || super.worldObj.getBlockTileEntity(x, super.yCoord, z) instanceof TileEntityCrop)
                {
                    --fresh;
                }
            }
        }

        var6 += fresh / 2;

        if (super.worldObj.canBlockSeeTheSky(super.xCoord, super.yCoord + 1, super.zCoord))
        {
            var6 += 2;
        }

        return (byte)var6;
    }

    public byte updateMultiCulture()
    {
        LinkedList crops = new LinkedList();

        for (int x = -1; x < 1; ++x)
        {
            for (int z = -1; z < 1; ++z)
            {
                if (super.worldObj.getBlockTileEntity(x + super.xCoord, super.yCoord, z + super.zCoord) instanceof TileEntityCrop)
                {
                    this.addIfNotPresent(((TileEntityCrop)super.worldObj.getBlockTileEntity(x + super.xCoord, super.yCoord, z + super.zCoord)).crop(), crops);
                }
            }
        }

        return (byte)(crops.size() - 1);
    }

    public void addIfNotPresent(CropCard crop, LinkedList<CropCard> crops)
    {
        for (int i = 0; i < crops.size(); ++i)
        {
            if (crop == crops.get(i))
            {
                return;
            }
        }

        crops.add(crop);
    }

    public int calcGrowthRate()
    {
        int base = 3 + IC2.random.nextInt(7) + this.statGrowth;
        int need = (this.crop().tier() - 1) * 4 + this.statGrowth + this.statGain + this.statResistance;

        if (need < 0)
        {
            need = 0;
        }

        int have = this.crop().weightInfluences(this, (float)this.getHumidity(), (float)this.getNutrients(), (float)this.getAirQuality()) * 5;

        if (have >= need)
        {
            base = base * (100 + (have - need)) / 100;
        }
        else
        {
            int neg = (need - have) * 4;

            if (neg > 100 && IC2.random.nextInt(32) > this.statResistance)
            {
                this.reset();
                base = 0;
            }
            else
            {
                base = base * (100 - neg) / 100;

                if (base < 0)
                {
                    base = 0;
                }
            }
        }

        return base;
    }

    public void calcTrampling()
    {
        if (IC2.platform.isSimulating() && IC2.random.nextInt(100) == 0 && IC2.random.nextInt(40) > this.statResistance)
        {
            this.reset();
            super.worldObj.setBlock(super.xCoord, super.yCoord - 1, super.zCoord, Block.dirt.blockID, 0, 7);
        }
    }

    public CropCard crop()
    {
        return Crops.instance.getCropList()[this.id];
    }

    public void onEntityCollision(Entity entity)
    {
        if (this.id >= 0 && this.crop().onEntityCollision(this, entity))
        {
            this.calcTrampling();
        }
    }

    public void reset()
    {
        this.id = -1;
        this.size = 0;
        this.customData = new NBTTagCompound();
        this.dirty = true;
        this.statGain = 0;
        this.statResistance = 0;
        this.statGrowth = 0;
        this.nutrients = -1;
        this.airQuality = -1;
        this.humidity = -1;
        this.growthPoints = 0;
        this.upgraded = false;
        this.scanLevel = 0;
    }

    public void updateState()
    {
        this.dirty = true;
    }

    public String getScanned()
    {
        return this.scanLevel > 0 && this.id >= 0 ? (this.scanLevel >= 4 ? this.crop().name() + " - Gr: " + this.statGrowth + " Ga: " + this.statGain + " Re: " + this.statResistance : this.crop().name()) : null;
    }

    public boolean isBlockBelow(Block block)
    {
        for (int i = 1; i < 4; ++i)
        {
            int id = super.worldObj.getBlockId(super.xCoord, super.yCoord - i, super.zCoord);

            if (id == 0)
            {
                return false;
            }

            if (Block.blocksList[id] == block)
            {
                return true;
            }
        }

        return false;
    }

    public ItemStack generateSeeds(short plant, byte growth, byte gain, byte resis, byte scan)
    {
        return ItemCropSeed.generateItemStackFromValues(plant, growth, gain, resis, scan);
    }

    public void onNetworkUpdate(String field)
    {
        this.dirty = true;
    }

    public short getID()
    {
        return this.id;
    }

    public byte getSize()
    {
        return this.size;
    }

    public byte getGrowth()
    {
        return this.statGrowth;
    }

    public byte getGain()
    {
        return this.statGain;
    }

    public byte getResistance()
    {
        return this.statResistance;
    }

    public byte getScanLevel()
    {
        return this.scanLevel;
    }

    public NBTTagCompound getCustomData()
    {
        return this.customData;
    }

    public int getNutrientStorage()
    {
        return this.nutrientStorage;
    }

    public int getHydrationStorage()
    {
        return this.waterStorage;
    }

    public int getWeedExStorage()
    {
        return this.exStorage;
    }

    public int getLightLevel()
    {
        return super.worldObj.getBlockLightValue(super.xCoord, super.yCoord, super.zCoord);
    }

    public void setID(short id)
    {
        this.id = id;
        this.dirty = true;
    }

    public void setSize(byte size)
    {
        this.size = size;
        this.dirty = true;
    }

    public void setGrowth(byte growth)
    {
        this.statGrowth = growth;
    }

    public void setGain(byte gain)
    {
        this.statGain = gain;
    }

    public void setResistance(byte resistance)
    {
        this.statResistance = resistance;
    }

    public void setScanLevel(byte scanLevel)
    {
        this.scanLevel = scanLevel;
    }

    public void setNutrientStorage(int nutrientStorage)
    {
        this.nutrientStorage = nutrientStorage;
    }

    public void setHydrationStorage(int hydrationStorage)
    {
        this.waterStorage = hydrationStorage;
    }

    public void setWeedExStorage(int weedExStorage)
    {
        this.exStorage = weedExStorage;
    }

    public World getWorld()
    {
        return super.worldObj;
    }

    public ChunkCoordinates getLocation()
    {
        return new ChunkCoordinates(super.xCoord, super.yCoord, super.zCoord);
    }
}
