package nepisat.ee2.gameObjs.items.tools;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class DamageSourceA extends DamageSource {
	
	public DamageSourceA(String name) {
		super(name);
		
	}
	public void callsetDamageBypassesArmor(){
		setDamageBypassesArmor();
	}
}