package ic2.core;

class ExplosionIC2$DropData
{
    int n;
    int maxY;

    ExplosionIC2$DropData(int n, int y)
    {
        this.n = n;
        this.maxY = y;
    }

    public ExplosionIC2$DropData add(int n, int y)
    {
        this.n += n;

        if (y > this.maxY)
        {
            this.maxY = y;
        }

        return this;
    }
}
