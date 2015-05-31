package ic2.core;

import ic2.api.Direction;
import ic2.core.EnergyNet$Tile;
import java.util.HashSet;
import java.util.Set;

class EnergyNet$EnergyPath
{
    EnergyNet$Tile target = null;
    Direction targetDirection;
    Set<EnergyNet$Tile> conductors = new HashSet();
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int minZ = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    int maxZ = Integer.MIN_VALUE;
    double loss = 0.0D;
    int minInsulationEnergyAbsorption = Integer.MAX_VALUE;
    int minInsulationBreakdownEnergy = Integer.MAX_VALUE;
    int minConductorBreakdownEnergy = Integer.MAX_VALUE;
    long totalEnergyConducted = 0L;
}
