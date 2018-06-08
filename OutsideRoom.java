package nettest;

public class OutsideRoom extends Room
{
	WorldTimer worldTimer = new WorldTimer();
		
	public OutsideRoom( String name )
	{
		super( name );
		worldTimer.addOutside( this );
	}
	
	public String getSunriseMessage()
	{
		return "The sun rises over the horizon.";
	}
	
	public String getSundownMessage()
	{
		return "The world grows dim as the sun descends.";
	}
	
	
}
