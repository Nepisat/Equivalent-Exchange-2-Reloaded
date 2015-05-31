package ic2.core.item;

class ItemScrapbox$Drop
{
    float originalChance;
    float upperChanceBound;
    static float topChance;

    ItemScrapbox$Drop(float chance)
    {
        this.originalChance = chance;
        this.upperChanceBound = topChance += chance;
    }
}
