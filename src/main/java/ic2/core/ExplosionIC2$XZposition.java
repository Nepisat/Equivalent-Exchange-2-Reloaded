package ic2.core;

class ExplosionIC2$XZposition
{
    int x;
    int z;

    ExplosionIC2$XZposition(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof ExplosionIC2$XZposition))
        {
            return false;
        }
        else
        {
            ExplosionIC2$XZposition xZposition = (ExplosionIC2$XZposition)obj;
            return xZposition.x == this.x && xZposition.z == this.z;
        }
    }

    public int hashCode()
    {
        return this.x * 31 ^ this.z;
    }
}
