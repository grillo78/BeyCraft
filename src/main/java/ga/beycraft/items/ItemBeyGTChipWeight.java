package ga.beycraft.items;

public class ItemBeyGTChipWeight extends ItemBeyGTChip {

    private float weight;
    private float burst;

    public ItemBeyGTChipWeight(String name, String displayName, float weight, float burst, ItemBeyLayer.Color resonanceColor, ItemBeyLayer.Color secondResonanceColor, ItemBeyLayer.Color thirdResonanceColor) {
        super(name, displayName, weight, burst, resonanceColor, secondResonanceColor, thirdResonanceColor);
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }
}
