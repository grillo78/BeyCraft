package com.beycraft.utils;

import com.beycraft.client.screen.LoginScreen;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import xyz.heroesunited.heroesunited.util.HURichPresence;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientUtils {

    public static void setDiscordRPC() {
        HURichPresence.getPresence().setDiscordRichPresence("Beycraft Burst", "In the menus", HURichPresence.MiniLogos.EXTERNAL, "Beycraft Burst");
    }

    public static class RankingUtils {

        private static String TOKEN;

        public static void checkLogin(GuiOpenEvent event) throws IOException {
            if(!checkToken()) {
                event.setCanceled(true);
                Minecraft.getInstance()
                        .setScreen(new LoginScreen(new StringTextComponent("")));
            }
        }

        private static boolean checkToken() throws IOException {
            boolean validToken = false;
            File tokenFile = new File("token.properties");
            if(tokenFile.exists()) {
                Properties tokenProperties = new Properties();
                tokenProperties.load(new FileReader(tokenFile));
                if(tokenProperties.containsKey("token")){
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("https://beycraft.com/API/v3/check_login/?token=" + tokenProperties.getProperty("token"));

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inStream = entity.getContent();
                        String result = IOUtils.toString(inStream, StandardCharsets.UTF_8);
                        JsonElement root = new JsonParser().parse(result);
                        JsonObject elements = root.getAsJsonObject();
                        if (elements.get("status").getAsString().equals("success")) {
                            validToken = true;
                            TOKEN = tokenProperties.getProperty("token");
                        } else
                            System.out.println(result);
                    }
                }
            }
            return validToken;
        }

        public static boolean getToken(String username, String password) {
            boolean resultSuccess = false;
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("https://beycraft.com/API/v3/check_credentials/?username=" + username + "&password=" + password);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inStream = entity.getContent();
                    String result = IOUtils.toString(inStream, StandardCharsets.UTF_8);
                    JsonElement root = new JsonParser().parse(result);
                    JsonObject elements = root.getAsJsonObject();
                    if (elements.get("status").getAsString().equals("success")) {
                        String token = elements.get("token").getAsString();
                        Properties tokenProperties = new Properties();
                        tokenProperties.setProperty("token", token);
                        tokenProperties.store(new FileWriter("token.properties"), "");
                        TOKEN = token;
                        resultSuccess = true;
                    } else
                        System.out.println(result);
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultSuccess;
        }

        public static float getExperience(){
            float experience = 0;
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("https://beycraft.com/API/v3/get_xp/?token=" + TOKEN);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inStream = entity.getContent();
                    String result = IOUtils.toString(inStream, StandardCharsets.UTF_8);
                    JsonElement root = new JsonParser().parse(result);
                    JsonObject elements = root.getAsJsonObject();
                    if (elements.get("status").getAsString().equals("success")) {
                        experience = elements.get("xp").getAsFloat();
                    } else
                        System.out.println(result);
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

        public static void increaseExperience(float experience) {
            new Thread(() -> {
                try {
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("https://beycraft.com/API/v2/ranking/increase_experience/");

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("token", TOKEN));
                    params.add(new BasicNameValuePair("experience", String.valueOf(experience)));
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

        public static void winCombat() {
            new Thread(() -> {
                try {
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("https://beycraft.com/API/ranking/win_battle/");

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("token", TOKEN));
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
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("https://beycraft.com/API/ranking/lose_battle/");

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("token", TOKEN));
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
    }
}
