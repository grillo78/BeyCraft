package com.grillo78.beycraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RankingUtil {

    @Nullable
    public static JsonArray getRanking(){

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
            ranking =new JsonParser().parse(json).getAsJsonArray();
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
}
