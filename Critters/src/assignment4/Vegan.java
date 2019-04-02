package assignment4;


import assignment4.Critter.TestCritter;
/**
 * This critter is Vegan, it only fights Clovers to eat them. 
 * if it crosses a Goblin or a Cloner, it refuses to fight.
 * The Vegan Critter is represented with a V in its toString method.
 * During a time step the Critter walks in any given direction or it stays put
 * depending on the last action it took.
 */
public class Vegan extends TestCritter {
	private static boolean Move = false;

    /**
     * Method doTimeStep
     * The method handles the individual world step of the vegan 
     * critter.
     * decides to walk the critter in a specific direction
     * and /or reproduce the critter
     */
	@Override
	public void doTimeStep() {
		if(getEnergy() > 100) {
			Vegan child = new Vegan();
			reproduce(child, getRandomInt(8));
		}
		
		if(Move == false) {
			run(getRandomInt(8));
		}
		else Move = true;
	}


    /**
     * Method fight
     * The method handles the individual fight method of the Vegan 
     * critter.
     * Decides to either fight or not. 
     * @param String opponent to fight.
     * @return boolean of either true of false to fight. 
     */
	@Override
	public boolean fight(String opponent) {
		if(opponent == "@")
			return true;
		else {
			return false;
		}
	}


	@Override
	public String toString () {
		return "V";
	}
}
