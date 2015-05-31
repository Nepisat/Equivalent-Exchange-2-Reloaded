package ic2.core;

class ExplosionIC2$ItemWithMeta
{
    int itemId;
    int metaData;

    ExplosionIC2$ItemWithMeta(int itemId, int metaData)
    {
        this.itemId = itemId;
        this.metaData = metaData;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof ExplosionIC2$ItemWithMeta))
        {
            return false;
        }
        else
        {
            ExplosionIC2$ItemWithMeta itemWithMeta = (ExplosionIC2$ItemWithMeta)obj;
            return itemWithMeta.itemId == this.itemId && itemWithMeta.metaData == this.metaData;
        }
    }

    public int hashCode()
    {
        return this.itemId * 31 ^ this.metaData;
    }
}
