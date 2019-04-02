package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Spring 2019
 */

import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
   // private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**y
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }
        commandInterpreter(kb);

        System.out.flush();

    }
    /* Do not alter the code above for your submission. */
    /**y
     * commandInterpreter method.
     * @param scanner.  
     * The method interprets the commands entered on the console by user.
     * The method interprets commands create, seed, stats, quit, show, and clear.
     */
    private static void commandInterpreter (Scanner kb) {
    	int userNumber;
    	String class_name;
    	String[] promptsArray;
    	String userPrompts;
        System.out.print("critters> ");
        userPrompts = kb.nextLine();
    	promptsArray = userPrompts.split(" ");
    	boolean modified = false;

    	if(promptsArray.length > 1 && promptsArray[0].equals("quit")) {
			promptsArray[0] = "InvalidQuit";
			modified = true;
    	}
    	while (!(promptsArray[0].equals("quit"))) {
        	switch (promptsArray[0]) {
			case "create":
				if (promptsArray.length == 3) {
					try {
						class_name = promptsArray[1];
						userNumber = Integer.parseInt(promptsArray[2]);
					}
						catch(IllegalArgumentException e1) {
							System.out.println("error processing: " + userPrompts);
							break;
						}
						while (userNumber > 0) {
							try {
							Critter.createCritter(class_name);
							}
							
							catch (InvalidCritterException e2) {
								System.out.println("error processing: " + userPrompts);
								break;
							}
							userNumber--;
						}
							
					} 

					else if (promptsArray.length < 3 || promptsArray.length > 3) {
						System.out.println("error processing: " + userPrompts);
	       				break;
					}
				break;
				
				case "seed":
					if(promptsArray.length > 2 || promptsArray.length < 2) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					try {
						userNumber = Integer.parseInt(promptsArray[1]);
						Critter.setSeed(userNumber);
					}
					catch (IllegalArgumentException e3) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					break;
				
					
				case "show":
					if (promptsArray.length != 1) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					Critter.displayWorld();
					break;
				
					
				case "step": 
					if (promptsArray.length == 2) {
						try {
							userNumber = Integer.parseInt(promptsArray[1]);
							while (userNumber > 0) {
								Critter.worldTimeStep();
								userNumber--;
							}
						}
						catch (IllegalArgumentException e4) {
							System.out.println("error processing: " + userPrompts);
							break;
						}
						break;
					}
					else { 
						System.out.println("error processing: " + userPrompts);
					}
					break;
					
				case "stats":
					if (promptsArray.length !=2) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					class_name = promptsArray[1];
					try {
						List<Critter> critters = Critter.getInstances(class_name);
						try {
							Class <?> calling_class = Class.forName(myPackage + "." + class_name);
							Method runStats = calling_class.getMethod("runStats", List.class);
							runStats.invoke(calling_class, critters);
						}
						catch (ClassNotFoundException e5) {
							e5.printStackTrace();
							break;
						}
						catch (IllegalArgumentException e6) {
							e6.printStackTrace();
							break;
						} 
						catch (NoSuchMethodException e7) {
							e7.printStackTrace();
							break;
						} 
						catch (SecurityException e8) {
							e8.printStackTrace();
							break;
						} 
						catch (IllegalAccessException e) {
							e.printStackTrace();
							break;
						} 
						catch (InvocationTargetException e) {
							e.printStackTrace();
							break;
						}
					
					}
					catch (InvalidCritterException e7) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					break;
		
				case "clear":
					if(promptsArray.length > 1) {
						System.out.println("error processing: " + userPrompts);
						break;
					}
					else Critter.clearWorld();
					break;
					
			default:
				if(!modified)
					System.out.println("invalid command: " + userPrompts);
				else {
					System.out.println("error processing: " + userPrompts);
					modified = false;
				}

				break;
        	}
            System.out.print("critters> ");
            userPrompts = kb.nextLine();
        	promptsArray = userPrompts.split(" ");
        	if(promptsArray.length > 1 && promptsArray[0].equals("quit")) {
    			promptsArray[0] = "InvalidQuit";
    			modified = true;
        	}
    	}

    }
}
