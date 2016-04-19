package edu.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
	
	private static final Logger myLogger = Logger.getLogger("edu.test");
	
	public static String test(String name) {
		myLogger.entering("Test", "test", name);
//		Logger.getGlobal().info("File->Open menu item selected.");
		myLogger.info("Some Message");
		myLogger.exiting("Test", "test", name);
		return name;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Logger.getGlobal().setLevel(Level.OFF);
		myLogger.setLevel(Level.FINER);
		myLogger.setUseParentHandlers(false);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.FINER);
		myLogger.addHandler(handler);
		System.out.println(test("jadestrong"));
	}	

}
