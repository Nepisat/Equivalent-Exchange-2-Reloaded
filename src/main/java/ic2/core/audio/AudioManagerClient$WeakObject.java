package ic2.core.audio;

import java.lang.ref.WeakReference;

public class AudioManagerClient$WeakObject extends WeakReference<Object>
{
    public AudioManagerClient$WeakObject(Object referent)
    {
        super(referent);
    }

    public boolean equals(Object object)
    {
        return object instanceof AudioManagerClient$WeakObject ? ((AudioManagerClient$WeakObject)object).get() == this.get() : this.get() == object;
    }

    public int hashCode()
    {
        Object object = this.get();
        return object == null ? 0 : object.hashCode();
    }
}
