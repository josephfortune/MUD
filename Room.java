package nettest;


public class Room 
{
	String name; // For ID purposes
	String title; // For display
	String description;
	boolean light = false;
	
	// Exits N S E W U D
	String[] exits = new String[6];
	
	public Room( String newName )
	{
		name = newName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setDescription( String text )
	{
		description = text;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setExit( int index, String room )
	{
		// If index is within range 0-5
		if ( index > -1 && index < 6)
		exits[ index ] = room;
	}
	
	public void setTitle( String newTitle )
	{
		title = newTitle;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String[] getExits()
	{
		return exits;
	}
	
	// ===================== Lighting functions =========================
	public boolean hasLight()
	{
		return light;
	}
	
	public void setLight( boolean on )
	{
		light = on;
	}
	
	public void flipLight()
	{
		if ( hasLight() )
			setLight( false );
		else
			setLight( true );
	}
	

}
