package ic2.core;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

class IC2$1 extends Properties
{
    final IC2 this$0;

    IC2$1(IC2 var1)
    {
        this.this$0 = var1;
    }

    public Set<Object> keySet()
    {
        return Collections.unmodifiableSet(new TreeSet(super.keySet()));
    }

    public synchronized Enumeration<Object> keys()
    {
        return Collections.enumeration(new TreeSet(super.keySet()));
    }
}
