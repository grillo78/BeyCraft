package grillo78.beycraft.common.capability.item.beylogger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class Beylogger implements IBeylogger{

    private String url = "";

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void readNBT(CompoundNBT nbt) {
        url = nbt.getString("url");
    }

    @Override
    public INBT writeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("url", url);
        return compound;
    }
}
