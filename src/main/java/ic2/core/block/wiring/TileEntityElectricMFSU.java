package ic2.core.block.wiring;

public class TileEntityElectricMFSU extends TileEntityElectricBlock
{
    public TileEntityElectricMFSU()
    {
        super(3, 512, 10000000);
    }

    public String getInvName()
    {
        return "MFSU";
    }
}
