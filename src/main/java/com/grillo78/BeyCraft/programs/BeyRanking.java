package com.grillo78.BeyCraft.programs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grillo78.BeyCraft.util.Person;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Text;

import net.minecraft.nbt.NBTTagCompound;

public class BeyRanking extends Application{

	private static File dir;
	private static List<Person> ranking = new ArrayList<Person>();
	private static List<String> rankingString = new ArrayList<String>();
	
	@Override
	public void init(NBTTagCompound intent) {
		this.setDefaultHeight(240);
		this.setDefaultWidth(362);
		ranking.clear();
		rankingString.clear();
		dir = new File("Ranking.json");
		if (!dir.exists()) {
			try {
				FileUtils.writeStringToFile(dir, "{\r\n" + 
						"	\"names\":[\r\n"+
						"	],\r\n" + 
						"	\r\n" + 
						"	\"points\":[\r\n" + 
						"	]\r\n" + 
						"}", "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Text title = new Text("Ranking",2, 5, 50);
		this.addComponent(title);
		ItemList<String> listComponent = new ItemList<>(0, 15, 362, 10);
		try {
	    	JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new FileReader(dir));
			JsonObject rootobj = root.getAsJsonObject();
			if (rootobj.get("names").getAsJsonArray().size() != 0) {
				for (int i = 0; i!=rootobj.get("names").getAsJsonArray().size(); i++) {
					ranking.add(new Person(rootobj.get("names").getAsJsonArray().get(i).getAsString(), rootobj.get("points").getAsJsonArray().get(i).getAsInt()));
//					ranking.add("Points: "+rootobj.get("points").getAsJsonArray().get(i).getAsString()+" Name: "+rootobj.get("names").getAsJsonArray().get(i).getAsString());
				}
			}
			 Collections.sort(ranking, Collections.reverseOrder());
//			Collections.sort(ranking);
//			Collections.reverse(ranking);
		} catch (IOException ex) {
		      ex.printStackTrace();
		}
		for (int i = 0; i!=ranking.size(); i++) {
			rankingString.add(ranking.get(i).toString());
		}
		listComponent.setItems(rankingString);
		this.addComponent(listComponent);
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
		
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		
	}

}
