package nepisat.ee2.events;


import cpw.mods.fml.common.FMLCommonHandler;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.container.AlchBagContainer;
import nepisat.ee2.handlers.PlayerChecks;
import nepisat.ee2.playerData.AlchemicalBags;
import nepisat.ee2.playerData.IOHandler;
import nepisat.ee2.playerData.Transmutation;
import nepisat.ee2.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerEvents
{
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			Transmutation.sync((EntityPlayer) event.entity);
			AlchemicalBags.sync((EntityPlayer) event.entity);
		//	PacketHandler.sendTo(new ClientSyncTableEMCPKT(Transmutation.getStoredEmc(event.entity.getCommandSenderName())), (EntityPlayerMP) event.entity);
		}
	}

	@ForgeSubscribe
	public void playerChangeDimension(PlayerEvent event)
	{
	//	System.out.println(FMLCommonHandler.instance().getEffectiveSide());

	//	PlayerChecks.onPlayerChangeDimension((EntityPlayerMP) event.entityPlayer);
	}

	@ForgeSubscribe
	public void pickupItem(EntityItemPickupEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;
		
		if (world.isRemote)
		{
			return;
		}
		
		if (player.openContainer instanceof AlchBagContainer)
		{
			IInventory inv = ((AlchBagContainer) player.openContainer).inventory;
			
			if (Utils.invContainsItem(inv, new ItemStack(ObjHandler.blackHole, 1, 1)) && Utils.hasSpace(inv, event.item.getEntityItem()))
			{
				ItemStack remain = Utils.pushStackInInv(inv, event.item.getEntityItem());
				
				if (remain == null)
				{
					event.item.delayBeforeCanPickup = 10;
					event.item.setDead();
					world.playSoundAtEntity(player, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
				else 
				{
					event.item.setEntityItemStack(remain);
				}
				
				event.setCanceled(true);
			}
		}
		else
		{
			ItemStack bag = getAlchemyBag(player, player.inventory.mainInventory);
			
			if (bag == null)
			{
				return;
			}
			
			ItemStack[] inv = AlchemicalBags.get(player.getCommandSenderName(), (byte) bag.getItemDamage());
			
			if (Utils.hasSpace(inv, event.item.getEntityItem()))
			{
				ItemStack remain = Utils.pushStackInInv(inv, event.item.getEntityItem());
				
				if (remain == null)
				{
					event.item.delayBeforeCanPickup = 10;
					event.item.setDead();
					world.playSoundAtEntity(player, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
				else 
				{
					event.item.setEntityItemStack(remain);
				}
				
				AlchemicalBags.set(player.getCommandSenderName(), (byte) bag.getItemDamage(), inv);
				AlchemicalBags.sync(player);
				
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void playerSaveData(PlayerEvent event)
	{
		if (IOHandler.markedDirty)
		{
			IOHandler.saveData();
			//PELogger.logInfo("Saved transmutation and alchemical bag data.");
			
			IOHandler.markedDirty = false;
		}
	}
	
	private ItemStack getAlchemyBag(EntityPlayer player, ItemStack[] inventory)
	{
		for (ItemStack stack : inventory)
		{
			if (stack == null) 
			{
				continue;
			}
			
			if (stack.getItem() == ObjHandler.alchBag && Utils.invContainsItem(AlchemicalBags.get(player.getCommandSenderName(), (byte) stack.getItemDamage()), new ItemStack(ObjHandler.blackHole, 1, 1)))
			{
				return stack;
			}
		}
		
		return null;
	}
}
