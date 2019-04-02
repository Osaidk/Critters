package assignment4;

import assignment4.Critter.TestCritter;

/**
 * This is a Cloner Critter. It will clone itself only when it fights a Goblin and 
 * divides it's energy with it's cloner offspring and then it fights if has the minimum energy.
 * it walks in any direction during a time step with no other actions.
 * If it crosses a Clover it will fight without cloning.
 * If it doesn't have the required energy it elects to fight anyway without cloning.
 * Its toString method will implement a (#) symbol to resemble itself.
 */
public class Cloner extends TestCritter {
	private int direction;
	
    /**
     * Method doTimeStep
     * The method handles the individual world step of the cloner 
     * critter.
     * decides to walk the critter in a specific direction
     */
	@Override
	public void doTimeStep() {
		direction = getRandomInt(8);
		walk(direction);
	}
	
	
	
    /**
     * Method fight
     * The method handles the fight situation of the critter
     * @param string of the opponent.
     * @returns boolean true or false for fighting
     */
	@Override
	public boolean fight(String opponent) {
		if (opponent == "@") return true;
		else if(getEnergy() > 100){
			Cloner cloner = new Cloner();
			direction = getRandomInt(7);
			reproduce(cloner, direction);
			return true;
		}
		return true;
	}

	
	
	@Override
	public String toString() {
		return "#";
	}
}
