package nettest;

public class Character 
{
	protected String name;
	protected Room room;
	protected World world;
	protected WorldTimer worldTimer = new WorldTimer();
   protected CombatThread combatThread = null;  
   protected boolean inCombat = false; 
	
	public Character( String nameIn )
	{
		name = nameIn;
		world = new World(); //Need access to the world
	}
	
	// ********************** setName ********************** \\
	boolean setName(String nameIn)
	{
		// Name only allows letters
		if ( nameIn.matches("[a-zA-Z]+") )
		{
			name = nameIn;
			return true;
		}
		else
			return false;
	}
	
	// ********************** getName ********************** \\
	String getName()
	{
		return name;
	}
	
	// ********************** setRoom ********************** \\
	public void setRoom( Room newRoom )
	{
		room = newRoom;
	}
	
	// ********************** getRoom ********************** \\
	public Room getRoom()
	{
		return room;
	}
	
	// ********************** print ********************** \\
	public void print( String text )
	{
		// Really just a place holder to simplify triggers
	}
	
	// ********************** triggerArrive ********************** \\
	public void triggerArrive( Character character )
	{
		
	}
	
	// ********************** triggerLeave ********************** \\
	public void triggerLeave( Character character, int exitIndex )
	{
		
	}
   
   // ********************** triggerSay ************************ \\
   public void triggerSay( Character character, String speech )
   {
   
   }
   
   // ********************** enterCombat ******************* \\
   public void enterCombat( CombatThread cThread )
   {
      inCombat = true;
      combatThread = cThread;
   }
   
   // ********************** getCombatThread ******************* \\
   public CombatThread getCombatThread()
   {
      return combatThread;
   }
   
   // ********************** exitCombat ******************* \\
   public void exitCombat()
   {
      inCombat = false;
      combatThread = null;
   }
   
   // ********************** isInCombat ******************* \\
   public boolean isInCombat()
   {
      return inCombat;
   }
   
}
