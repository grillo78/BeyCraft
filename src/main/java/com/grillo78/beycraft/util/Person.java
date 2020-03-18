package com.grillo78.beycraft.util;

public class Person implements Comparable<Person>{
	private final String name;
    private final int points;
    
	public Person(String name, int points) {
		this.name = name;
		this.points = points;
	}
	
	public boolean equals(Object o) {
        if (!(o instanceof Person))
            return false;
        Person person = (Person) o;
        return person.name.equals(name) && person.points == points;
    }
	
	public int points() {
        return points;
    }
	
	public String name() {
		return name;
	}
	
	public String toString(){
        return "Name: " + name +". Points: " + points ;
    }
	
	@Override
	public int compareTo(Person person) {
		 return points > person.points ? 1 : points < person.points ? -1 : 0;
	}

}
