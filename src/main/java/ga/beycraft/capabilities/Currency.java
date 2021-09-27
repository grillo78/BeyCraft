package ga.beycraft.capabilities;

public class Currency implements ICurrency {

    private float currency = 0;

    @Override
    public float getCurrency() {
        return currency;
    }

    @Override
    public void increaseCurrency(float currency) {
        this.currency += currency;
    }

    @Override
    public void setCurrency(float currency) {
        this.currency = currency;
    }
}
