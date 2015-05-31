package ic2.core;

import ic2.api.Direction;

class EnergyNet$EnergyBlockLink
{
    Direction direction;
    double loss;

    EnergyNet$EnergyBlockLink(Direction direction, double loss)
    {
        this.direction = direction;
        this.loss = loss;
    }
}
