package nepisat.ee2.utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.HashMap;

public final class WorldTransmutations
{
	private static final HashMap<MetaBlock, MetaBlock[]> MAP = new HashMap<MetaBlock, MetaBlock[]>();

	public static void init()
	{
		register(Block.stone, Block.cobblestone, Block.grass);
		register(Block.cobblestone, Block.stone, Block.grass);
		register(Block.grass, Block.sand, Block.cobblestone);
		register(Block.dirt, Block.sand, Block.cobblestone);
		register(Block.sand, Block.grass, Block.cobblestone);
		register(Block.gravel, Block.sandStone);
		register(Block.waterStill, Block.ice);
		register(Block.lavaStill, Block.obsidian);
		register(Block.melon, Block.pumpkin);


		register(new MetaBlock(Block.wood, 0), new MetaBlock[] {new MetaBlock(Block.wood, 1), new MetaBlock(Block.wood, 1)});
		register(new MetaBlock(Block.leaves, 0), new MetaBlock[] {new MetaBlock(Block.leaves, 1), new MetaBlock(Block.leaves, 1)});

		for (int i = 1; i < 3; i++)
		{
			register(new MetaBlock(Block.wood, i), new MetaBlock[]{new MetaBlock(Block.wood, i + 1), new MetaBlock(Block.wood, i - 1)});
			register(new MetaBlock(Block.leaves, i), new MetaBlock[] {new MetaBlock(Block.leaves, i + 1), new MetaBlock(Block.leaves, i - 1)});
		}

		register(new MetaBlock(Block.wood, 3), new MetaBlock[]{new MetaBlock(Block.wood, 0), new MetaBlock(Block.wood, 2)});
		register(new MetaBlock(Block.leaves, 3), new MetaBlock[]{new MetaBlock(Block.leaves, 0), new MetaBlock(Block.leaves, 2)});
		register(new MetaBlock(Block.leaves, 0), new MetaBlock[] {new MetaBlock(Block.leaves, 1), new MetaBlock(Block.leaves, 3)});

		register(new MetaBlock(Block.sapling, 0), new MetaBlock[] {new MetaBlock(Block.sapling, 1), new MetaBlock(Block.sapling, 5)});

		for (int i = 1; i < 5; i++)
		{
			register(new MetaBlock(Block.sapling, i), new MetaBlock[] {new MetaBlock(Block.sapling, i + 1), new MetaBlock(Block.sapling, i - 1)});
		}

		register(new MetaBlock(Block.sapling, 5), new MetaBlock[] {new MetaBlock(Block.sapling, 0), new MetaBlock(Block.sapling, 4)});
/*
		register(new MetaBlock(Block., 0), new MetaBlock[] {new MetaBlock(Block.wool, 1), new MetaBlock(Block.wool, 15)});

		for (int i = 1; i < 15; i++)
		{
			register(new MetaBlock(Block.wool, i), new MetaBlock[] {new MetaBlock(Block.wool, i + 1), new MetaBlock(Block.wool, i - 1)});
		}

		register(new MetaBlock(Block.wool, 15), new MetaBlock[] {new MetaBlock(Block.wool, 0), new MetaBlock(Block.wool, 14)});
	*/}

	public static MetaBlock getWorldTransmutation(World world, int x, int y, int z, boolean isSneaking)
	{
		MetaBlock block = new MetaBlock(world, x, y, z);

		if (MAP.containsKey(block))
		{
			return MAP.get(block)[isSneaking ? 1 : 0];
		}

		return null;
	}

	public static MetaBlock getWorldTransmutation(MetaBlock block, boolean isSneaking)
	{
		if (MAP.containsKey(block))
		{
			return MAP.get(block)[isSneaking ? 1 : 0];
		}

		return null;
	}

	private static void register(Block block, Block result)
	{
		MAP.put(new MetaBlock(block), new MetaBlock[] {new MetaBlock(result), new MetaBlock(result)});
		MAP.put(new MetaBlock(result), new MetaBlock[] {new MetaBlock(block), new MetaBlock(block)});
	}

	private static void register(MetaBlock block, MetaBlock result)
	{
		MAP.put(block, new MetaBlock[] {result, result});
		MAP.put(result, new MetaBlock[] {block, block});
	}

	private static void register(Block block, Block b1, Block b2)
	{
		MAP.put(new MetaBlock(block), new MetaBlock[] {new MetaBlock(b1), new MetaBlock(b2)});
	}

	private static void register(MetaBlock block, MetaBlock[] result)
	{
		MAP.put(block, result);
	}
}
