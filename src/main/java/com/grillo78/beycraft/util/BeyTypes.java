package com.grillo78.beycraft.util;

/**
 * @author a19guillermong
 *
 */
public enum BeyTypes {
	ATTACK, DEFENSE, STAMINA, BALANCE;
	
	public int getIndex() {
		switch(this) {
		default:
		case ATTACK:
			return 0;
		case DEFENSE:
			return 1;
		case STAMINA:
			return 2;
		case BALANCE:
			return 3;
		}
	}
	
	public static String getName(int index){
		switch(index) {
		default:
		case 0:
			return "Attack";
		case 1:
			return "Defense";
		case 2:
			return "Stamina";
		case 3:
			return "Balance";
		}
	}
	
	public static BeyTypes getByName(String name) {
		switch (name) {
		default:
			return null;
		case "attack":
			return ATTACK;
		case "defense":
			return DEFENSE;
		case "stamina":
			return STAMINA;
		case "balance":
			return BALANCE;
		}
	}
}
