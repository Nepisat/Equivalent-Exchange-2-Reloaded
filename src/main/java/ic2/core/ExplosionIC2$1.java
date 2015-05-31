package ic2.core;

import java.util.Comparator;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;

class ExplosionIC2$1 implements Comparator<Entry<Integer, Entity>>
{
    final ExplosionIC2 this$0;

    ExplosionIC2$1(ExplosionIC2 var1)
    {
        this.this$0 = var1;
    }

    public int compare(Entry<Integer, Entity> a, Entry<Integer, Entity> b)
    {
        return ((Integer)a.getKey()).intValue() - ((Integer)b.getKey()).intValue();
    }

  
}
