package nettest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main 
{

	public static void main(String[] args) throws IOException
	{
		// Initialize the world. Everything is created statically to be shared among all callers.
		World world = new World();
		world.init();
		
		// Thread to listen for incoming connections
		Thread listenerThread = new Thread( new ListenThread(), "ListenerThread" );
		listenerThread.start();
		
		// WORLD THREAD will go here, to update night/day AI movements blah blah blah
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new WorldTimer(), 0, 6 * 1000 );
		
	}

}
