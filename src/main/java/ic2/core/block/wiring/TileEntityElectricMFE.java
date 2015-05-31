package ic2.core.block.wiring;

public class TileEntityElectricMFE extends TileEntityElectricBlock
{
    public TileEntityElectricMFE()
    {
        super(2, 128, 600000);
    }

    public String getInvName()
    {
        return "MFE";
    }
}
