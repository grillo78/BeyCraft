package ga.beycraft.util;

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
	
	public String getName(){
		switch(this) {
		default:
		case ATTACK:
			return "attack";
			case DEFENSE:
			return "defense";
			case STAMINA:
			return "stamina";
			case BALANCE:
			return "balance";
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
