package ic2.core;

import ic2.api.Direction;
import ic2.core.EnergyNet$Tile;

class EnergyNet$EnergyTarget
{
    EnergyNet$Tile tile;
    Direction direction;

    EnergyNet$EnergyTarget(EnergyNet$Tile tile, Direction direction)
    {
        this.tile = tile;
        this.direction = direction;
    }
}
