package com.grillo78.BeyCraft.programs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.util.Person;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Text;

import net.minecraft.nbt.NBTTagCompound;

public class BeyRanking extends Application{

	
	private static List<Person> ranking = new ArrayList<Person>();
	private static List<String> rankingString = new ArrayList<String>();
	
	@Override
	public void init(NBTTagCompound intent) {

		ranking.clear();
		ResultSet resultSet = null;
			try {
				String selectSql = "SELECT * FROM `players` ";
            
				resultSet = BeyCraft.dbConn.getStatement().executeQuery(selectSql);
			

            // Print results from select statement
            while (resultSet.next()) {
            	int i = 1;
                System.out.println(resultSet.getString(i));
            	ranking.add(new Person(resultSet.getString(i++), resultSet.getInt(i)));
            }
            } catch (SQLException e) {
    			BeyCraft.logger.error("Database exception: "+e.getMessage());
    		}
		Collections.sort(ranking, Collections.reverseOrder());
		this.setDefaultHeight(240);
		this.setDefaultWidth(362);
		rankingString.clear();
		
		Text title = new Text("Ranking",2, 5, 50);
		this.addComponent(title);
		ItemList<String> listComponent = new ItemList<>(0, 15, 362, 10);
		for (int i = 0; i!=ranking.size(); i++) {
			rankingString.add(i+1+"º. "+ranking.get(i).toString());
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
