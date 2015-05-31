package bcplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import buildcraft.core.Version;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class blockonBuilder extends Block{

	public blockonBuilder(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setCreativeTab(CreativeTabs.tabFood);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float disX, float disY, float disZ)
	{
		
			File dir = new File("BC_BuilderTXT");
			dir.mkdir();
	        File file = new File(dir, "newFile2.txt");
	        int px = x;
	        int py = y;
	        int pz = z;
			try {
				
				FileOutputStream output = new FileOutputStream(file);

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "8859_1"));
				//writer.write("VER:1.0");
				//writer.newLine();
				py--;
				for (int i=0;i<=16*16*256; i++){
					if(px==x+16){
						px=x;
						pz++;
					}else{
						writer.write(String.valueOf(w.getBlockId(px, py, pz)));
						writer.newLine();
						if(String.valueOf(w.getBlockMetadata(px, py, pz))==null){
							writer.write("0");
						}else{
							writer.write(String.valueOf(w.getBlockMetadata(px, py, pz)));
						}
						writer.newLine();
						px++;
					}
					if(pz==z+16){
						pz=z;
						py--;
					}
					if(py<=0){
						
						break;
					}
						
						
					
				}
				writer.close();
			} catch (IOException e1) {

				System.out.println(e1);
			}
		
		
		//ブロックが右クリックされた時
		return false;
	}
 
	
	
	
}
