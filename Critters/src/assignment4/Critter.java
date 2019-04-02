package assignment4;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
import java.util.*;
/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();



    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    private static String myPackage;
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name) throws InvalidCritterException {
    	try {
        	critter_class_name = myPackage + "." + critter_class_name;
			Class<?> critter = Class.forName(critter_class_name);
			Constructor <?> constructor = critter.getConstructor();
			Object object = constructor.newInstance();
			Critter critter2 = (Critter)object;
			setCoordsAndEnergy(critter2);
			population.add(critter2);					
			
			
		} catch (NoClassDefFoundError | ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    
    
    /**
     * method setCoordsAndEnergy
     * The method sets the coordinates and energy of a critter.
     * @param critter
     */
    public static void setCoordsAndEnergy (Critter critter) {
		critter.x_coord = getRandomInt(Params.WORLD_WIDTH);
		critter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
		critter.energy = Params.START_ENERGY;
    }

    
    
    
    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
    	List<Critter> instances = new ArrayList<Critter>();
    	try {
    		critter_class_name = myPackage + "." + critter_class_name;
        	Class<?> instance = Class.forName(critter_class_name);
			Constructor<?> forInstance = instance.getConstructor();
			Object object = forInstance.newInstance();
			Critter critter = (Critter)object;
			for (Critter critter2 : population) {
				if (critter.getClass().isInstance(critter2))
					instances.add(critter2);
			}
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoClassDefFoundError | ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return instances;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
    	babies.clear();
    	population.clear();
    }
    
   
    /**
     * method worldTimeStep
     * The method iterates the world step.
     * The method also makes sure to remove all dead critters.
     * The method generates Clovers by depending on the number of refresh count.
     * The method adds all the babies to the grid from previous world step.
     */
    public static void worldTimeStep() {
    	
        for(int i=0; i<population.size(); i++) {
        	population.get(i).doTimeStep();
        	population.get(i).energy -= Params.REST_ENERGY_COST;
        	if (population.get(i).energy <= 0) {
        		population.remove(i);
        		i--;
        	}
        }
        
        
        for(int i=0; i < population.size(); i++) {
        	for(int j=i; j < population.size(); j++) {
        		if (population.get(i) == population.get(j))
        			continue;
        		if (population.get(i).checkOccupancy(population.get(j))) {
        			population.get(i).doEnconters(population.get(j));
        			i--;
        			break;
        		}
        	}
        }

    	genClover();
        
        if(babies.size() > 0) {
        	population.addAll(babies);
        	babies.clear();
        }   
    }
    
    
    
    
    
    /**
     * Method checkOccupany
     * The method checks a specific coordinate for an encounter
     * between two or more critters.
     * @param Critter
     * @returns boolean
     */
    public boolean checkOccupancy(Critter critter) {
    	if(this.x_coord == critter.x_coord && this.y_coord == critter.y_coord)
    		return true;
    	else return false;
    	
    }

    
    /**
     * Method doEncounters
     * The method checks solves encounters between critters.
     * The solution of an encounter could either be fight, no fight, or run.
     * @param Critter
     */
    public void doEnconters(Critter critter) {
    	boolean thisFight = this.fight(critter.toString());
    	boolean critterFight = critter.fight(this.toString());
    	int winChance1 = 0, winChance2 = 0;
    	
    	if(this.checkOccupancy(critter)) {
    		if (this.energy <=0 || critter.energy <=0) {
    			if(this.energy <= 0) {
    				population.remove(this);
    				}
    			if(critter.energy <= 0) {
    				population.remove(critter);
    			}
    			return;
    		}
    		
    		if(thisFight) {
    			winChance1 = getRandomInt(this.energy);
    		}
    		if(critterFight) {
    			winChance2 = getRandomInt(critter.energy);
    		}
 
			if(winChance1 > winChance2) {
				this.energy += critter.energy / 2;
				population.remove(critter);
				return;
			} else {
				critter.energy += this.energy / 2;
				population.remove(this);
				return;
			}
			
    	}
    	else return;
    }
    
    
    
    
    
    
    /**
     * Method genClover
     * The method generates a specified number of clovers
     * and add them to the population every time step.
     */ 
    public static void genClover() {
    	int count = 0;
    	while(count < Params.REFRESH_CLOVER_COUNT) {
    	Clover clover = new Clover();
    	clover.setEnergy(Params.START_ENERGY);
    	clover.setX_coord(getRandomInt(Params.WORLD_WIDTH-1));
    	clover.setY_coord(getRandomInt(Params.WORLD_HEIGHT-1));
    	population.add(clover);
    	count++;
    	}
    }
    
    
        
    
    
    
    /**
     * Method displayWorld
     * The method displays the world into the console through System.out
     */
    public static void displayWorld() {
    	System.out.print("+");
    	for (int i=0; i<Params.WORLD_WIDTH; i++) {
    		System.out.print("-");
    	}
    	System.out.print("+");
    	System.out.println();

    	for(int i=0; i<Params.WORLD_HEIGHT; i++) {
        	System.out.print("|");
        	if (!population.isEmpty()) {
        		CritterOnMap(i);
        		continue;
        	}
        	for (int j=0; j<Params.WORLD_WIDTH; j++) {
            	System.out.print(" ");
        	}
        	System.out.print("|");	
        	System.out.println();

    	}

    	System.out.print("+");
    	for (int i=0; i<Params.WORLD_WIDTH; i++) {
    		System.out.print("-");
    	}
       	System.out.print("+");
    	System.out.println();
    	
    }

    
    
    
    
    
    /**
     * Method CritterOnMap
     * The method checks if there's a critter on a specific coordinates
     * and prints it to the console.
     * The method is a helper for display world. 
     * @param int coordinate x.
     */
    public static void CritterOnMap(int x) {
    	for (int j=0; j<Params.WORLD_WIDTH; j++) {
    		if (CritterAt(x, j) != null) {
    			System.out.print(CritterAt(x, j).toString());
    		}
    		else System.out.print(" ");		
    	}
    	System.out.print("|");	
    	System.out.println();
    }
    
    
    
    
    
    /**
     * Method CritterAt
     * The method checks if a critters is available at the 
     * passed coordinates and returns it. Otherwise it returns null.
     * @param Coordinates x and y. 
     * @returns Critter at the specified coordinates.
     */
    public static Critter CritterAt(int y, int x) {
    	for (Critter critter : population) {
    		if (critter.x_coord == x && critter.y_coord == y)
    			return critter;
    	}
		return null;
    }
    
    
    
    
    
    /**
     * Prints out how many Critters of each type there are on the board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map <String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string, critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }
    
    
    
    
    
    
    /**
     * Method walk
     * The method handles the walk operation of a critter in all directions.
     * The method also serves as a helper method for the run method. 
     * @param the direction in which the critter is supposed to walk.
     */
    protected final void walk(int direction) {
        if(direction == 0) {
        	this.directioRight();
    		this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 1) {
        	this.directioRight();
        	this.directioUp();
    		this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 2) {
        	this.directioUp();
    		this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 3) {
        	this.directioLeft();
        	this.directioUp();
        	this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 4) {
        	this.directioLeft();
        	this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 5) {
        	this.directioLeft();
        	this.directioDown();
        	this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 6) {
        	this.directioDown();
        	this.energy-=Params.WALK_ENERGY_COST;
        }
        else if(direction == 7) {
        	this.directioRight();
        	this.directioDown();
        	this.energy-=Params.WALK_ENERGY_COST;
        }
    }


    
    /**
     * Method directionRight
     * The method handles the coordinates modification if a critter
     * decides to move right on the world.
     */
    private void directioRight() {
		if (this.x_coord == Params.WORLD_WIDTH-1)
			this.x_coord = 0;
		else this.x_coord+=1;		
	}
    
    
    /**
     * Method directionUp
     * The method handles the coordinates modification if a critter
     * decides to move up on the world.
     */
    private void directioUp() {
		if (this.y_coord == 0)
			this.y_coord = Params.WORLD_HEIGHT-1;
		else {
			this.y_coord-=1;
		}
	}
    
    /**
     * Method directionDown
     * The method handles the coordinates modification if a critter
     * decides to move down on the world.
     */
    private void directioDown() {
		if (this.y_coord == Params.WORLD_HEIGHT-1)
			this.y_coord = 0;
		else {
			this.y_coord+=1;
		}	
	}
    
    /**
     * Method directionLeft
     * The method handles the coordinates modification if a critter
     * decides to move left on the world.
     */
    private void directioLeft() {
		if (this.x_coord == 0)
			this.x_coord = Params.WORLD_WIDTH-1;
		else this.x_coord-=1;		
	}
    
    
 
    /**
     * Method run
     * The method handles the run operation of a critter.
     * The method takes advantage of the walk run to implement
     * its operation.
     * @param the direction which the critter decides to move in. 
     */
    protected final void run(int direction) {
        walk(direction);
        walk(direction);
        this.energy+=2*(Params.WALK_ENERGY_COST);
        this.energy-=Params.RUN_ENERGY_COST;

    }

    
    
    
    /**
     * Method reproduce
     * The method handles the reproduction of critters and divides
     * the energy of the parent between the parent and offspring.
     * @param critter which represents the offspring of a critter
     * and the direction in which to place the offspring next to the parent.
     */
    protected final void reproduce(Critter offspring, int direction) {
        if (this.energy > Params.MIN_REPRODUCE_ENERGY) {
        	offspring.energy = (int) Math.floor(((double)this.energy/2));;
        	this.energy = (int) Math.ceil(((double)this.energy/2));
        	offspring.x_coord = this.x_coord;
        	offspring.y_coord = this.y_coord;
        	offspring.walk(direction);
        	offspring.energy += Params.WALK_ENERGY_COST;
        	babies.add(offspring);
        }
        else return; 	
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you want to
     * create tests of your Critter model, you can create subclasses of this class
     * and then use the setter functions contained here.
     *
     * NOTE: you must make sure that the setter functions work with your implementation
     * of Critter. That means, if you're recording the positions of your critters
     * using some sort of external grid or some other data structure in addition
     * to the x_coord and y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you are not using the population
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population
         * at either the beginning OR the end of every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
        
    }

}
