package ic2.core.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.util.StackUtil;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemNanoSaber extends ItemElectricTool
{
    public static int ticker = 0;
    @SideOnly(Side.CLIENT)
    private Icon[] textures;
    private int soundTicker = 0;
    private final Random shinyrand = new Random();

    public ItemNanoSaber(Configuration config, InternalName internalName)
    {
        super(config, internalName, EnumToolMaterial.IRON, 10);
        super.maxCharge = 40000;
        super.transferLimit = 128;
        super.tier = 3;
        super.mineableBlocks.add(Block.web);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.textures = new Icon[3];
        this.textures[0] = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName() + "." + InternalName.off.name());
        this.textures[1] = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName() + "." + InternalName.active.name());
        this.textures[2] = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName() + "." + InternalName.activeAlternate.name());
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(ItemStack itemStack, int pass)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        return nbtData.getBoolean("active") ? (this.shinyrand.nextBoolean() ? this.textures[1] : this.textures[2]) : this.textures[0];
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemStack, Block block)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

        if (nbtData.getBoolean("active"))
        {
            ++this.soundTicker;

            if (this.soundTicker % 4 == 0)
            {
                IC2.platform.playSoundSp(this.getRandomSwingSound(), 1.0F, 1.0F);
            }

            return 4.0F;
        }
        else
        {
            return 1.0F;
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase source)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

        if (!nbtData.getBoolean("active"))
        {
            return true;
        }
        else
        {
            if (IC2.platform.isSimulating() && (!(source instanceof EntityPlayer) || !MinecraftServer.getServer().isPVPEnabled()))
            {
                for (int i = 0; i < 4; ++i)
                {
                    ItemStack armor = target.getCurrentItemOrArmor(i + 1);

                    if (armor != null)
                    {
                        short amount = 0;

                        if (armor.getItem() instanceof ItemArmorNanoSuit)
                        {
                            amount = 4800;
                        }
                        else if (armor.getItem() instanceof ItemArmorQuantumSuit)
                        {
                            amount = 30000;
                        }

                        if (amount > 0)
                        {
                            ElectricItem.manager.discharge(armor, amount, super.tier, true, false);

                            if (!ElectricItem.manager.canUse(armor, 1))
                            {
                                target.setCurrentItemOrArmor(i + 1, (ItemStack)null);
                            }

                            drainSaber(itemStack, 2, source);
                        }
                    }
                }

                drainSaber(itemStack, 5, source);
            }

            if (IC2.platform.isRendering())
            {
                IC2.platform.playSoundSp(this.getRandomSwingSound(), 1.0F, 1.0F);
            }

            return true;
        }
    }

    public String getRandomSwingSound()
    {
        switch (IC2.random.nextInt(3))
        {
            case 1:
                return "nanosabreSwingOne";

            case 2:
                return "nanosabreSwingTwo";

            default:
                return "nanosabreSwing";
        }
    }

    public boolean onBlockStartBreak(ItemStack itemStack, int i, int j, int k, EntityPlayer player)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

        if (nbtData.getBoolean("active"))
        {
            drainSaber(itemStack, 10, player);
        }

        return false;
    }

    public float getDamageVsEntity(Entity entity, ItemStack itemStack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        return nbtData.getBoolean("active") ? 20.0F : 4.0F;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return block.blockID == Block.web.blockID;
    }

    public static void drainSaber(ItemStack itemStack, int damage, EntityLivingBase entity)
    {
        if (!ElectricItem.manager.use(itemStack, damage * 8, entity))
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            nbtData.setBoolean("active", false);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityplayer)
    {
        if (!IC2.platform.isSimulating())
        {
            return itemStack;
        }
        else
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

            if (nbtData.getBoolean("active"))
            {
                nbtData.setBoolean("active", false);
                updateAttributes(nbtData);
            }
            else if (ElectricItem.manager.canUse(itemStack, 16))
            {
                nbtData.setBoolean("active", true);
                updateAttributes(nbtData);
                world.playSoundAtEntity(entityplayer, "nanosabrePower", 1.0F, 1.0F);
            }

            return super.onItemRightClick(itemStack, world, entityplayer);
        }
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean par5)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

        if (nbtData.getBoolean("active") && ticker % 16 == 0 && entity instanceof EntityPlayerMP)
        {
            if (slot < 9)
            {
                drainSaber(itemStack, 64, (EntityPlayer)entity);
            }
            else if (ticker % 64 == 0)
            {
                drainSaber(itemStack, 16, (EntityPlayer)entity);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    private static void updateAttributes(NBTTagCompound nbtData)
    {
        boolean active = nbtData.getBoolean("active");
        int damage = active ? 20 : 4;
        NBTTagCompound entry = new NBTTagCompound();
        entry.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        entry.setLong("UUIDMost", Item.field_111210_e.getMostSignificantBits());
        entry.setLong("UUIDLeast", Item.field_111210_e.getLeastSignificantBits());
        entry.setString("Name", "Tool modifier");
        entry.setDouble("Amount", (double)damage);
        entry.setInteger("Operation", 0);
        NBTTagList list = new NBTTagList();
        list.appendTag(entry);
        nbtData.setTag("AttributeModifiers", list);
    }
}
