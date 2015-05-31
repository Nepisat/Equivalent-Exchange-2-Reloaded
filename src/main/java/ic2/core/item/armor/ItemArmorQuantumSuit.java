package ic2.core.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.ItemTinCan;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class ItemArmorQuantumSuit extends ItemArmorElectric
{
    private static final Map<Integer, Integer> potionRemovalCost = new HashMap();
    public static Map<EntityPlayer, Integer> speedTickerMap = new HashMap();
    public static Map<EntityPlayer, Boolean> onGroundMap = new HashMap();
    public static Map<EntityPlayer, Boolean> enableQuantumSpeedOnSprintMap = new HashMap();
    private static final int extraFallDistanceProtection = 7;
    private float jumpCharge;

    public ItemArmorQuantumSuit(Configuration config, InternalName internalName, int armorType)
    {
        super(config, internalName, InternalName.quantum, armorType, 1000000, 1000, 3);

        if (armorType == 3)
        {
            MinecraftForge.EVENT_BUS.register(this);
        }

        potionRemovalCost.put(Integer.valueOf(Potion.poison.id), Integer.valueOf(10000));
        potionRemovalCost.put(Integer.valueOf(IC2Potion.radiation.id), Integer.valueOf(10000));
        potionRemovalCost.put(Integer.valueOf(Potion.wither.id), Integer.valueOf(25000));
    }

    public ArmorProperties getProperties(EntityLivingBase entity, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (source == DamageSource.fall && super.armorType == 3)
        {
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = energyPerDamage > 0 ? 25 * ElectricItem.manager.getCharge(armor) / energyPerDamage : 0;
            return new ArmorProperties(10, 1.0D, damageLimit);
        }
        else
        {
            return super.getProperties(entity, armor, source, damage, slot);
        }
    }

    @ForgeSubscribe
    public void onEntityLivingFallEvent(LivingFallEvent event)
    {
        if (IC2.platform.isSimulating() && event.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entity = (EntityLivingBase)event.entity;
            ItemStack armor = entity.getCurrentItemOrArmor(1);

            if (armor != null && armor.itemID == super.itemID)
            {
                int fallDamage = Math.max((int)event.distance - 3 - 7, 0);
                int energyCost = this.getEnergyPerDamage() * fallDamage;

                if (energyCost <= ElectricItem.manager.getCharge(armor))
                {
                    ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false);
                    event.setCanceled(true);
                }
            }
        }
    }

    public double getDamageAbsorptionRatio()
    {
        return super.armorType == 1 ? 1.1D : 1.0D;
    }

    public int getEnergyPerDamage()
    {
        return 900;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.rare;
    }

    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        boolean ret = false;
        int var11;

        switch (super.armorType)
        {
            case 0:
                IC2.platform.profilerStartSection("QuantumHelmet");
                int air = player.getAir();

                if (ElectricItem.manager.canUse(itemStack, 1000) && air < 100)
                {
                    player.setAir(air + 200);
                    ElectricItem.manager.use(itemStack, 1000, (EntityLivingBase)null);
                    ret = true;
                }
                else if (air <= 0)
                {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                if (ElectricItem.manager.canUse(itemStack, 1000) && player.getFoodStats().needFood())
                {
                    int var12 = -1;

                    for (var11 = 0; var11 < player.inventory.mainInventory.length; ++var11)
                    {
                        if (player.inventory.mainInventory[var11] != null && player.inventory.mainInventory[var11].itemID == Ic2Items.filledTinCan.itemID)
                        {
                            var12 = var11;
                            break;
                        }
                    }

                    if (var12 > -1)
                    {
                        ItemTinCan var13 = (ItemTinCan)player.inventory.mainInventory[var12].getItem();
                        player.getFoodStats().addStats(var13.getHealAmount(), var13.getSaturationModifier());
                        var13.onFoodEaten(player.inventory.mainInventory[var12], player.worldObj, player);
                        var13.onEaten(player);

                        if (--player.inventory.mainInventory[var12].stackSize <= 0)
                        {
                            player.inventory.mainInventory[var12] = null;
                        }

                        ElectricItem.manager.use(itemStack, 1000, (EntityLivingBase)null);
                        ret = true;
                    }
                }
                else if (player.getFoodStats().getFoodLevel() <= 0)
                {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                Iterator var111 = (new LinkedList(player.getActivePotionEffects())).iterator();

                while (var111.hasNext())
                {
                    PotionEffect var14 = (PotionEffect)var111.next();
                    int var15 = var14.getPotionID();
                    Integer cost = (Integer)potionRemovalCost.get(Integer.valueOf(var15));

                    if (cost != null)
                    {
                        cost = Integer.valueOf(cost.intValue() * (var14.getAmplifier() + 1));

                        if (ElectricItem.manager.canUse(itemStack, cost.intValue()))
                        {
                            ElectricItem.manager.use(itemStack, cost.intValue(), (EntityLivingBase)null);
                            IC2.platform.removePotion(player, var15);
                        }
                    }
                }

                IC2.platform.profilerEndSection();
                break;

            case 1:
                IC2.platform.profilerStartSection("QuantumBodyarmor");
                player.extinguish();
                IC2.platform.profilerEndSection();
                break;

            case 2:
                IC2.platform.profilerStartSection("QuantumLeggings");
                boolean enableQuantumSpeedOnSprint = true;

                if (IC2.platform.isRendering())
                {
                    enableQuantumSpeedOnSprint = IC2.enableQuantumSpeedOnSprint;
                }
                else if (enableQuantumSpeedOnSprintMap.containsKey(player))
                {
                    enableQuantumSpeedOnSprint = ((Boolean)enableQuantumSpeedOnSprintMap.get(player)).booleanValue();
                }

                if (ElectricItem.manager.canUse(itemStack, 1000) && (player.onGround || player.isInWater()) && IC2.keyboard.isForwardKeyDown(player) && (enableQuantumSpeedOnSprint && player.isSprinting() || !enableQuantumSpeedOnSprint && IC2.keyboard.isBoostKeyDown(player)))
                {
                    var11 = speedTickerMap.containsKey(player) ? ((Integer)speedTickerMap.get(player)).intValue() : 0;
                    ++var11;

                    if (var11 >= 10)
                    {
                        var11 = 0;
                        ElectricItem.manager.use(itemStack, 1000, (EntityLivingBase)null);
                        ret = true;
                    }

                    speedTickerMap.put(player, Integer.valueOf(var11));
                    float var121 = 0.22F;

                    if (player.isInWater())
                    {
                        var121 = 0.1F;

                        if (IC2.keyboard.isJumpKeyDown(player))
                        {
                            player.motionY += 0.10000000149011612D;
                        }
                    }

                    if (var121 > 0.0F)
                    {
                        player.moveFlying(0.0F, 1.0F, var121);
                    }
                }

                IC2.platform.profilerEndSection();
                break;

            case 3:
                IC2.platform.profilerStartSection("QuantumBoots");

                if (IC2.platform.isSimulating())
                {
                    boolean wasOnGround = onGroundMap.containsKey(player) ? ((Boolean)onGroundMap.get(player)).booleanValue() : true;

                    if (wasOnGround && !player.onGround && IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player))
                    {
                        ElectricItem.manager.use(itemStack, 4000, (EntityLivingBase)null);
                        ret = true;
                    }

                    onGroundMap.put(player, Boolean.valueOf(player.onGround));
                }
                else
                {
                    if (ElectricItem.manager.canUse(itemStack, 4000) && player.onGround)
                    {
                        this.jumpCharge = 1.0F;
                    }

                    if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater())
                    {
                        if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player))
                        {
                            if (this.jumpCharge == 1.0F)
                            {
                                player.motionX *= 3.5D;
                                player.motionZ *= 3.5D;
                            }

                            player.motionY += (double)(this.jumpCharge * 0.3F);
                            this.jumpCharge = (float)((double)this.jumpCharge * 0.75D);
                        }
                        else if (this.jumpCharge < 1.0F)
                        {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }

                IC2.platform.profilerEndSection();
        }

        if (ret)
        {
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    public static void removePlayerReferences(EntityPlayer player)
    {
        speedTickerMap.remove(player);
        onGroundMap.remove(player);
        enableQuantumSpeedOnSprintMap.remove(player);
    }
}
