package ga.beycraft.network.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.util.ConfigManager;
import ga.beycraft.util.RankingUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MessageGetExperience implements IMessage<MessageGetExperience>{

    @Override
    public void encode(MessageGetExperience message, PacketBuffer buffer) {}

    @Override
    public MessageGetExperience decode(PacketBuffer buffer) {
        return new MessageGetExperience();
    }

    @Override
    public void handle(MessageGetExperience message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PacketHandler.instance.sendToServer(new MessageSetBladerExperience(RankingUtil.getExperience()));
        });
        supplier.get().setPacketHandled(true);
    }
}
