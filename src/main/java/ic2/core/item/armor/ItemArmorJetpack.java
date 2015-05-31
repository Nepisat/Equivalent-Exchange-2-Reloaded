package ic2.core.item.armor;

import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemArmorJetpack extends ItemArmorUtility
{
    public static AudioSource audioSource;
    private static boolean lastJetpackUsed = false;

    public ItemArmorJetpack(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.jetpack, 1);
        this.setMaxDamage(18002);
    }

    public int getCharge(ItemStack itemStack)
    {
        int ret = this.getMaxCharge(itemStack) - itemStack.getItemDamage() - 1;
        return ret > 0 ? ret : 0;
    }

    public int getMaxCharge(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() - 2;
    }

    public void use(ItemStack itemStack, int amount)
    {
        int newCharge = this.getCharge(itemStack) - amount;

        if (newCharge < 0)
        {
            newCharge = 0;
        }

        itemStack.setItemDamage(1 + itemStack.getMaxDamage() - newCharge);
    }

    public boolean useJetpack(EntityPlayer player, boolean hoverMode)
    {
        ItemStack jetpack = player.inventory.armorInventory[2];

        if (this.getCharge(jetpack) == 0)
        {
            return false;
        }
        else
        {
            boolean electric = jetpack.itemID != Ic2Items.jetpack.itemID;
            float power = 1.0F;
            float dropPercentage = 0.2F;

            if (electric)
            {
                power = 0.7F;
                dropPercentage = 0.05F;
            }

            if ((float)this.getCharge(jetpack) / (float)this.getMaxCharge(jetpack) <= dropPercentage)
            {
                power *= (float)this.getCharge(jetpack) / ((float)this.getMaxCharge(jetpack) * dropPercentage);
            }

            if (IC2.keyboard.isForwardKeyDown(player))
            {
                float worldHeight1 = 0.15F;

                if (hoverMode)
                {
                    worldHeight1 = 0.5F;
                }

                if (electric)
                {
                    worldHeight1 += 0.15F;
                }

                float maxFlightHeight1 = power * worldHeight1 * 2.0F;

                if (maxFlightHeight1 > 0.0F)
                {
                    player.moveFlying(0.0F, 0.4F * maxFlightHeight1, 0.02F);
                }
            }

            int worldHeight11 = IC2.getWorldHeight(player.worldObj);
            int maxFlightHeight11 = electric ? (int)((float)worldHeight11 / 1.28F) : worldHeight11;
            double y = player.posY;

            if (y > (double)(maxFlightHeight11 - 25))
            {
                if (y > (double)maxFlightHeight11)
                {
                    y = (double)maxFlightHeight11;
                }

                power = (float)((double)power * (((double)maxFlightHeight11 - y) / 25.0D));
            }

            double prevmotion = player.motionY;
            player.motionY = Math.min(player.motionY + (double)(power * 0.2F), 0.6000000238418579D);

            if (hoverMode)
            {
                float consume1 = -0.1F;

                if (electric && IC2.keyboard.isJumpKeyDown(player))
                {
                    consume1 = 0.1F;
                }

                if (player.motionY > (double)consume1)
                {
                    player.motionY = (double)consume1;

                    if (prevmotion > player.motionY)
                    {
                        player.motionY = prevmotion;
                    }
                }
            }

            int consume11 = 9;

            if (hoverMode)
            {
                consume11 = 6;
            }

            if (electric)
            {
                consume11 -= 2;
            }

            this.use(jetpack, consume11);
            player.fallDistance = 0.0F;
            player.distanceWalkedModified = 0.0F;
            IC2.platform.resetPlayerInAirTime(player);
            return true;
        }
    }

    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        boolean hoverMode = nbtData.getBoolean("hoverMode");
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean jetpackUsed = false;

        if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0)
        {
            toggleTimer = 10;
            hoverMode = !hoverMode;

            if (IC2.platform.isSimulating())
            {
                nbtData.setBoolean("hoverMode", hoverMode);

                if (hoverMode)
                {
                    IC2.platform.messagePlayer(player, "Hover Mode enabled.", new Object[0]);
                }
                else
                {
                    IC2.platform.messagePlayer(player, "Hover Mode disabled.", new Object[0]);
                }
            }
        }

        if (IC2.keyboard.isJumpKeyDown(player) || hoverMode && player.motionY < -0.3499999940395355D)
        {
            jetpackUsed = this.useJetpack(player, hoverMode);
        }

        if (IC2.platform.isSimulating() && toggleTimer > 0)
        {
            --toggleTimer;
            nbtData.setByte("toggleTimer", toggleTimer);
        }

        if (IC2.platform.isRendering() && player == IC2.platform.getPlayerInstance())
        {
            if (lastJetpackUsed != jetpackUsed)
            {
                if (jetpackUsed)
                {
                    if (audioSource == null)
                    {
                        audioSource = IC2.audioManager.createSource(player, PositionSpec.Backpack, "Tools/Jetpack/JetpackLoop.ogg", true, false, IC2.audioManager.defaultVolume);
                    }

                    if (audioSource != null)
                    {
                        audioSource.play();
                    }
                }
                else if (audioSource != null)
                {
                    audioSource.remove();
                    audioSource = null;
                }

                lastJetpackUsed = jetpackUsed;
            }

            if (audioSource != null)
            {
                audioSource.updatePosition();
            }
        }

        if (jetpackUsed)
        {
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        itemList.add(new ItemStack(this, 1, 1));
    }
}
