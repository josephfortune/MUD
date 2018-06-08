package nettest;

import java.util.TimerTask;
import java.util.ArrayList;

public class WorldTimer extends TimerTask
{
	World world = new World();
	
	// Counters
	static int seconds6 = 0;
	static int minute = 0;
	static int minute6 = 0;
	static int minute12 = 0;
	
	// ============================= Date/Time (game time) =============================
	static int time = 8;
	
	public void updateHour()
	{
		if ( time == 24 )
			time = 0;
		time++;
	}
	
	public String getTime()
	{
		if ( time < 13)
			return time + "am";
		else
			return time + "pm";
	}
	

	// ============================= Subscribed Outside Rooms =============================
	static ArrayList<OutsideRoom> outsideRooms = new ArrayList<OutsideRoom>();
	
	public void addOutside( OutsideRoom newRoom )
	{
		outsideRooms.add( newRoom );
	}
	
	public void sunRise()
	{
		for ( int i = 0; i < outsideRooms.size(); i++ )
		{
			world.printToRoom( outsideRooms.get( i ), outsideRooms.get( i ).getSunriseMessage() );
			outsideRooms.get( i ).setLight( true );
		}
	}
	
	public void sunDown()
	{
		for ( int i = 0; i < outsideRooms.size(); i++ )
		{
			world.printToRoom( outsideRooms.get( i ), outsideRooms.get( i ).getSundownMessage() );
			outsideRooms.get( i ).setLight( false );
		}
	}
	
	public void updateWorldEvents()
	{
		if ( time == 7 )
		{
			sunRise();
			
		}
		if ( time == 19 )
			sunDown();
			
	}
	
	
	// ============================= Subscribed Objects =============================
	static ArrayList<Timeable> subscribedObjects = new ArrayList<Timeable>();
	
	public void subscribe( Timeable subscriber )
	{
		subscribedObjects.add( subscriber );
		System.out.println("Object subscribed, Total: " + subscribedObjects.size() );
	}
	
	public void unsubscribe( Timeable subscriber )
	{
		subscribedObjects.remove( subscriber );
	}

	// ============================= Time Interval Functions =============================
	
	void run1Minute()
	{
		for ( int i = 0; i < subscribedObjects.size(); i++ )
			subscribedObjects.get( i ).action1Minute();
		updateHour();
		updateWorldEvents();
	}
	
	void run6Minute()
	{
		for ( int i = 0; i < subscribedObjects.size(); i++ )
			subscribedObjects.get( i ).action6Minute();
	}
	
	void run12Minute()
	{
		for ( int i = 0; i < subscribedObjects.size(); i++ )
			subscribedObjects.get( i ).action12Minute();
	}
	
	void run24Minute()
	{
		for ( int i = 0; i < subscribedObjects.size(); i++ )
			subscribedObjects.get( i ).action24Minute();
	}
	
	// ============================= Execute every 6 seconds =============================
	
	public void run()
	{

		seconds6++;
		
		if ( seconds6 == 10 )
		{
			// Run 1 minute task
			run1Minute();
			System.out.println("One minute");
			seconds6 = 0;
			minute++;
			
			if ( minute == 6 )
			{	// RUN 6 minute TASK --------------------------
				run6Minute();
				System.out.println("5 minutes");
				
				minute = 0;
				minute6++;
				
				if ( minute6 == 2 )
				{
					// Run 12 minute TASK -----------------------
					run12Minute();
					System.out.println("15 minutes");
					
					minute6 = 0;
					minute12++;
					
					if ( minute12 == 2 )
					{
						// Run 24 minute TASK ------------------------
						run24Minute();
						System.out.println("30 minutes");
						
						minute12 = 0;
	
					}
				}
			}
		}
	}
}
