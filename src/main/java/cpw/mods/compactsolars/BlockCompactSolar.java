/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.compactsolars;

import ic2.core.CreativeTabIC2;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCompactSolar extends BlockContainer {
	private Random random;
	@SideOnly(Side.CLIENT)
	private Icon[][] textures;

	public BlockCompactSolar(int blockId) {
		super(blockId,Material.iron);
		setUnlocalizedName("CompactSolar");
		setHardness(3.0F);
		
		random=new Random();
		setCreativeTab(ic2.core.IC2.tabIC2);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return CompactSolarType.makeEntity(metadata);
	}

	@Override
	public Icon getIcon(int i, int j) {
	    if (j>=CompactSolarType.values().length)
	    {
	        return null;
	    }
	    else
	    {
	        return textures[j][i > 2 ? 2 : i];
	    }
	}



	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
	    TileEntityCompactSolar tileSolar = (TileEntityCompactSolar)world.getBlockTileEntity(i, j, k);
	    if (tileSolar != null)
	    {
	    	dropContent(0, tileSolar, world);
	    }
	    super.breakBlock(world, i, j, k, par5, par6);
	}

	public void dropContent(int newSize, TileEntityCompactSolar tileSolar, World world) {
        for (int l = newSize; l < tileSolar.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileSolar.getStackInSlot(l);
            if (itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0)
            {
                int i1 = random.nextInt(21) + 10;
                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)tileSolar.xCoord + f, (float)tileSolar.yCoord + (newSize>0 ? 1 : 0) + f1, (float)tileSolar.zCoord + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                if (itemstack.hasTagCompound())
                {
                	entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
	}
	 public boolean isOpaqueCube()
	    {
	        return false;
	    }

 public int getRenderBlockPass()
 {
     return 1;
 }
 public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
 {
	 boolean localFlag = false;
     int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
     return !localFlag && var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
 }
 public boolean renderAsNormalBlock()
 {
     return false;
 }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (CompactSolarType type : CompactSolarType.values()) {
			itemList.add(new ItemStack(this,1,type.ordinal()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    textures = new Icon[CompactSolarType.values().length][3];
	    for (CompactSolarType typ: CompactSolarType.values()) {
	        for (int i = 0; i < 3; i++) {
	            String side = i == 0 ? "Bottom" : i == 1 ? "Top" : "Side";
	            String texName = String.format("compactsolars:%s%s",typ.name().toLowerCase(),side);
	            textures[typ.ordinal()][i]=par1IconRegister.registerIcon(texName);
	        }
	    }
	}
}
