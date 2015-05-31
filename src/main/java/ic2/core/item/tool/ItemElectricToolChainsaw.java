package ic2.core.item.tool;

import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.IHitSoundOverride;
import ic2.core.Ic2Items;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.init.InternalName;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemElectricToolChainsaw extends ItemElectricTool implements IHitSoundOverride
{
    public static boolean wasEquipped = false;
    public static AudioSource audioSource;

    public ItemElectricToolChainsaw(Configuration config, InternalName internalName)
    {
        super(config, internalName, EnumToolMaterial.IRON, 100);
        super.maxCharge = 10000;
        super.transferLimit = 100;
        super.tier = 1;
        super.efficiencyOnProperMaterial = 12.0F;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init()
    {
        super.mineableBlocks.add(Block.planks);
        super.mineableBlocks.add(Block.bookShelf);
        super.mineableBlocks.add(Block.wood);
        super.mineableBlocks.add(Block.chest);
        super.mineableBlocks.add(Block.leaves);
        super.mineableBlocks.add(Block.web);
        super.mineableBlocks.add(Block.blocksList[Ic2Items.crop.itemID]);

        if (Ic2Items.rubberLeaves != null)
        {
            super.mineableBlocks.add(Block.blocksList[Ic2Items.rubberLeaves.itemID]);
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker)
    {
        if (attacker instanceof EntityPlayer)
        {
            if (ElectricItem.manager.use(itemstack, super.operationEnergyCost, attacker))
            {
                entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), 9.0F);
            }

            if (entityliving instanceof EntityCreeper && entityliving.getHealth() <= 0.0F)
            {
                IC2.achievements.issueAchievement((EntityPlayer)attacker, "killCreeperChainsaw");
            }
        }

        return false;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return block.blockMaterial == Material.wood ? true : super.canHarvestBlock(block);
    }

    @ForgeSubscribe
    public void onEntityInteract(EntityInteractEvent event)
    {
        Entity entity = event.target;

        if (!entity.worldObj.isRemote)
        {
            EntityPlayer player = event.entityPlayer;
            ItemStack itemstack = player.inventory.mainInventory[player.inventory.currentItem];

            if (itemstack != null && itemstack.itemID == super.itemID && entity instanceof IShearable && ElectricItem.manager.use(itemstack, super.operationEnergyCost, player))
            {
                IShearable target = (IShearable)entity;

                if (target.isShearable(itemstack, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ))
                {
                    ArrayList drops = target.onSheared(itemstack, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                    EntityItem ent;

                    for (Iterator i$ = drops.iterator(); i$.hasNext(); ent.motionZ += (double)((Item.itemRand.nextFloat() - Item.itemRand.nextFloat()) * 0.1F))
                    {
                        ItemStack stack = (ItemStack)i$.next();
                        ent = entity.entityDropItem(stack, 1.0F);
                        ent.motionY += (double)(Item.itemRand.nextFloat() * 0.05F);
                        ent.motionX += (double)((Item.itemRand.nextFloat() - Item.itemRand.nextFloat()) * 0.1F);
                    }
                }
            }
        }
    }

    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            return false;
        }
        else
        {
            int id = player.worldObj.getBlockId(X, Y, Z);

            if (Block.blocksList[id] != null && Block.blocksList[id] instanceof IShearable)
            {
                IShearable target = (IShearable)Block.blocksList[id];

                if (target.isShearable(itemstack, player.worldObj, X, Y, Z) && ElectricItem.manager.use(itemstack, super.operationEnergyCost, player))
                {
                    ArrayList drops = target.onSheared(itemstack, player.worldObj, X, Y, Z, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                    Iterator i$ = drops.iterator();

                    while (i$.hasNext())
                    {
                        ItemStack stack = (ItemStack)i$.next();
                        float f = 0.7F;
                        double d = (double)(Item.itemRand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                        double d1 = (double)(Item.itemRand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                        double d2 = (double)(Item.itemRand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                        EntityItem entityitem = new EntityItem(player.worldObj, (double)X + d, (double)Y + d1, (double)Z + d2, stack);
                        entityitem.delayBeforeCanPickup = 10;
                        player.worldObj.spawnEntityInWorld(entityitem);
                    }

                    player.addStat(StatList.mineBlockStatArray[id], 1);
                }
            }

            return false;
        }
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        boolean isEquipped = flag && entity instanceof EntityLivingBase;

        if (IC2.platform.isRendering())
        {
            if (isEquipped && !wasEquipped)
            {
                if (audioSource == null)
                {
                    audioSource = IC2.audioManager.createSource(entity, PositionSpec.Hand, "Tools/Chainsaw/ChainsawIdle.ogg", true, false, IC2.audioManager.defaultVolume);
                }

                if (audioSource != null)
                {
                    audioSource.play();
                }
            }
            else if (!isEquipped && audioSource != null)
            {
                audioSource.stop();
                audioSource.remove();
                audioSource = null;

                if (entity instanceof EntityLivingBase)
                {
                    IC2.audioManager.playOnce(entity, PositionSpec.Hand, "Tools/Chainsaw/ChainsawStop.ogg", true, IC2.audioManager.defaultVolume);
                }
            }
            else if (audioSource != null)
            {
                audioSource.updatePosition();
            }

            wasEquipped = isEquipped;
        }
    }

    public String getHitSoundForBlock(int x, int y, int z)
    {
        String[] soundEffects = new String[] {"Tools/Chainsaw/ChainsawUseOne.ogg", "Tools/Chainsaw/ChainsawUseTwo.ogg"};
        return soundEffects[Item.itemRand.nextInt(soundEffects.length)];
    }
}
