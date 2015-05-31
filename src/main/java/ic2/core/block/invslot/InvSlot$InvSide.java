package ic2.core.block.invslot;

public enum InvSlot$InvSide
{
    ANY,
    TOP,
    BOTTOM,
    SIDE;

    public boolean matches(int side)
    {
        return this == ANY || side == 0 && this == BOTTOM || side == 1 && this == TOP || side >= 2 && side <= 5 && this == SIDE;
    }
}
