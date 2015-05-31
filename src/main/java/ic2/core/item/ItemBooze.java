package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemBooze extends ItemIC2
{
    public String[] solidRatio = new String[] {"Watery ", "Clear ", "Lite ", "", "Strong ", "Thick ", "Stodge ", "X"};
    public String[] hopsRatio = new String[] {"Soup ", "Alcfree ", "White ", "", "Dark ", "Full ", "Black ", "X"};
    public String[] timeRatioNames = new String[] {"Brew", "Youngster", "Beer", "Ale", "Dragonblood", "Black Stuff"};
    public int[] baseDuration = new int[] {300, 600, 900, 1200, 1600, 2000, 2400};
    public float[] baseIntensity = new float[] {0.4F, 0.75F, 1.0F, 1.5F, 2.0F};
    public static float rumStackability = 2.0F;
    public static int rumDuration = 600;

    public ItemBooze(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxStackSize(1);
        this.setCreativeTab((CreativeTabs)null);
    }

    public String getTextureName(int index)
    {
        return index < this.timeRatioNames.length ? this.getUnlocalizedName() + "." + InternalName.beer.name() + "." + this.timeRatioNames[index] : (index == this.timeRatioNames.length ? this.getUnlocalizedName() + "." + InternalName.rum.name() : null);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        int type = getTypeOfValue(meta);

        if (type == 1)
        {
            int timeRatio = Math.min(getTimeRatioOfBeerValue(meta), this.timeRatioNames.length - 1);
            return super.textures[timeRatio];
        }
        else
        {
            return type == 2 ? super.textures[this.timeRatioNames.length] : null;
        }
    }

    public String getItemDisplayName(ItemStack itemstack)
    {
        int meta = itemstack.getItemDamage();
        int type = getTypeOfValue(meta);

        if (type == 1)
        {
            int timeRatio = Math.min(getTimeRatioOfBeerValue(meta), this.timeRatioNames.length - 1);
            return timeRatio == this.timeRatioNames.length - 1 ? this.timeRatioNames[timeRatio] : this.solidRatio[getSolidRatioOfBeerValue(meta)] + this.hopsRatio[getHopsRatioOfBeerValue(meta)] + this.timeRatioNames[timeRatio];
        }
        else
        {
            return type == 2 ? "Rum" : "Zero";
        }
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player)
    {
        int meta = itemstack.getItemDamage();
        int type = getTypeOfValue(meta);

        if (type == 0)
        {
            return new ItemStack(Ic2Items.mugEmpty.getItem());
        }
        else
        {
            int level;

            if (type == 1)
            {
                if (getTimeRatioOfBeerValue(meta) == 5)
                {
                    return this.drinkBlackStuff(player);
                }

                int def1 = getSolidRatioOfBeerValue(meta);
                level = getHopsRatioOfBeerValue(meta);
                int duration = this.baseDuration[def1];
                float intensity = this.baseIntensity[getTimeRatioOfBeerValue(meta)];
                player.getFoodStats().addStats(6 - level, (float)def1 * 0.15F);
                int max = (int)(intensity * (float)level * 0.5F);
                PotionEffect slow = player.getActivePotionEffect(Potion.digSlowdown);
                int level1 = -1;

                if (slow != null)
                {
                    level1 = slow.getAmplifier();
                }

                this.amplifyEffect(player, Potion.digSlowdown, max, intensity, duration);

                if (level1 > -1)
                {
                    this.amplifyEffect(player, Potion.damageBoost, max, intensity, duration);

                    if (level1 > 0)
                    {
                        this.amplifyEffect(player, Potion.moveSlowdown, max / 2, intensity, duration);

                        if (level1 > 1)
                        {
                            this.amplifyEffect(player, Potion.resistance, max - 1, intensity, duration);

                            if (level1 > 2)
                            {
                                this.amplifyEffect(player, Potion.confusion, 0, intensity, duration);

                                if (level1 > 3)
                                {
                                    player.addPotionEffect(new PotionEffect(Potion.harm.id, 1, player.worldObj.rand.nextInt(3)));
                                }
                            }
                        }
                    }
                }
            }

            if (type == 2)
            {
                if (getProgressOfRumValue(meta) < 100)
                {
                    this.drinkBlackStuff(player);
                }
                else
                {
                    this.amplifyEffect(player, Potion.fireResistance, 0, rumStackability, rumDuration);
                    PotionEffect def11 = player.getActivePotionEffect(Potion.resistance);
                    level = -1;

                    if (def11 != null)
                    {
                        level = def11.getAmplifier();
                    }

                    this.amplifyEffect(player, Potion.resistance, 2, rumStackability, rumDuration);

                    if (level >= 0)
                    {
                        this.amplifyEffect(player, Potion.blindness, 0, rumStackability, rumDuration);
                    }

                    if (level >= 1)
                    {
                        this.amplifyEffect(player, Potion.confusion, 0, rumStackability, rumDuration);
                    }
                }
            }

            return new ItemStack(Ic2Items.mugEmpty.getItem());
        }
    }

    public void amplifyEffect(EntityPlayer player, Potion potion, int max, float intensity, int duration)
    {
        PotionEffect eff = player.getActivePotionEffect(potion);

        if (eff == null)
        {
            player.addPotionEffect(new PotionEffect(potion.id, duration, 0));
        }
        else
        {
            int newdur = eff.getDuration();
            int maxnewdur = (int)((float)duration * (1.0F + intensity * 2.0F) - (float)newdur) / 2;

            if (maxnewdur < 0)
            {
                maxnewdur = 0;
            }

            if (maxnewdur < duration)
            {
                duration = maxnewdur;
            }

            newdur += duration;
            int newamp = eff.getAmplifier();

            if (newamp < max)
            {
                ++newamp;
            }

            player.addPotionEffect(new PotionEffect(potion.id, newdur, newamp));
        }
    }

    public ItemStack drinkBlackStuff(EntityPlayer player)
    {
        switch (player.worldObj.rand.nextInt(6))
        {
            case 1:
                player.addPotionEffect(new PotionEffect(Potion.confusion.id, 1200, 0));
                break;

            case 2:
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 2400, 0));
                break;

            case 3:
                player.addPotionEffect(new PotionEffect(Potion.poison.id, 2400, 0));
                break;

            case 4:
                player.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 2));
                break;

            case 5:
                player.addPotionEffect(new PotionEffect(Potion.harm.id, 1, player.worldObj.rand.nextInt(4)));
        }

        return new ItemStack(Ic2Items.mugEmpty.getItem());
    }

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.drink;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public static int getTypeOfValue(int value)
    {
        return skipGetOfValue(value, 0, 2);
    }

    public static int getAmountOfValue(int value)
    {
        return getTypeOfValue(value) == 0 ? 0 : skipGetOfValue(value, 2, 5) + 1;
    }

    public static int getSolidRatioOfBeerValue(int value)
    {
        return skipGetOfValue(value, 7, 3);
    }

    public static int getHopsRatioOfBeerValue(int value)
    {
        return skipGetOfValue(value, 10, 3);
    }

    public static int getTimeRatioOfBeerValue(int value)
    {
        return skipGetOfValue(value, 13, 3);
    }

    public static int getProgressOfRumValue(int value)
    {
        return skipGetOfValue(value, 7, 7);
    }

    private static int skipGetOfValue(int value, int bitshift, int take)
    {
        value >>= bitshift;
        take = (int)Math.pow(2.0D, (double)take) - 1;
        return value & take;
    }
}
