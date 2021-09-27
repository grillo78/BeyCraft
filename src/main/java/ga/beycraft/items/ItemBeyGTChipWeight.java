package ga.beycraft.items;

public class ItemBeyGTChipWeight extends ItemBeyGTChip {

    private float weight;
    private float burst;

    public ItemBeyGTChipWeight(String name, float weight, float burst){
        super(name,weight,burst);
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }
}
