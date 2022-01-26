package ga.beycraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ga.beycraft.gui.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nullable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RankingUtil {

    @Nullable
    public static JsonArray getRanking() {

        URL url;
        InputStream is = null;
        BufferedReader br;
        String json = "";
        String line;
        JsonArray ranking = null;

        try {
            url = new URL("https://beycraft.ga/API/ranking/get_ranking/");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                json += line;
            }
            ranking = new JsonParser().parse(json).getAsJsonArray();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return ranking;
    }

    public static void winCombat() {
        new Thread(() -> {
            try {
                HttpClient httpclient = HttpClients.custom().setSslcontext(ConnectionUtils.getDisabledSSLCheckContext()).build();
                HttpPost httppost = new HttpPost("https://beycraft.ga/API/ranking/win_battle/");

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("token", getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println(new BufferedReader(new InputStreamReader(entity.getContent())).readLine());
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void loseCombat() {

        new Thread(() -> {
            try {
                HttpClient httpclient = HttpClients.custom().setSslcontext(ConnectionUtils.getDisabledSSLCheckContext()).build();
                HttpPost httppost = new HttpPost("https://beycraft.ga/API/ranking/lose_battle/");

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("token", getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println(new BufferedReader(new InputStreamReader(entity.getContent())).readLine());
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void updateExperience(float experience) {
        new Thread(() -> {
            try {
                HttpClient httpclient = HttpClients.custom().setSslcontext(ConnectionUtils.getDisabledSSLCheckContext()).build();
                HttpPost httppost = new HttpPost("https://beycraft.ga/API/ranking/update_experience/");

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("token", getToken()));
                params.add(new BasicNameValuePair("experience", String.valueOf(experience)));
                params.add(new BasicNameValuePair("xp_updated", String.valueOf(true)));
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println(new BufferedReader(new InputStreamReader(entity.getContent())).readLine());
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getToken(String username, String password) {
        String token = null;
        JsonObject elements = null;

        try {
            HttpClient httpclient = HttpClients.custom().setSslcontext(ConnectionUtils.getDisabledSSLCheckContext()).build();
            HttpPost httppost = new HttpPost("https://beycraft.ga/API/login/");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                System.out.println(result);
                JsonElement root = new JsonParser().parse(result);
                elements = root.getAsJsonObject();
                if (elements.get("result").getAsString().equals("valid")) {
                    token = elements.get("token").getAsString();
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static void checkLogIn(GuiOpenEvent event) {
        if (getToken() == null) {
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(new LoginScreen(new StringTextComponent(""), ConfigManager.getConfig(), ConfigManager.getConfigFile()));
        }
    }

    private static String getToken() {
        String tokenString = null;
        try {
            Properties token = new Properties();
            File tokenFile = new File("token.properties");
            if (!tokenFile.exists()) {
                tokenFile.createNewFile();
            }
            token.load(new FileReader(tokenFile));
                tokenString = token.getProperty("token");
        } catch (IOException e) {

        }

        return tokenString;
    }

    public static float getExperience() {
        float experience = 0;
        JsonObject elements = null;

        try {
            HttpClient httpclient = HttpClients.custom().setSslcontext(ConnectionUtils.getDisabledSSLCheckContext()).build();
            HttpPost httppost = new HttpPost("https://beycraft.ga/API/ranking/get_experience/");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("token", getToken()));
            params.add(new BasicNameValuePair("xp_updated", String.valueOf(true)));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                JsonElement root = new JsonParser().parse(result);
                elements = root.getAsJsonObject();
                if (elements.get("result").getAsString().equals("valid")) {
                    experience = elements.get("experience").getAsFloat();
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experience;
    }

    public static void storeToken(String tokenString) {
        try{
            Properties token = new Properties();
            File tokenFile = new File("token.properties");
            token.load(new FileReader(tokenFile));
            token.setProperty("token", tokenString);
            token.store(new FileWriter(tokenFile), "");
        } catch (IOException e){
        }
    }
}
