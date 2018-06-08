package nettest;

import java.util.ArrayList;

public class CombatThread implements Runnable 
{
   protected boolean active = true;
   protected Room room;
	protected ArrayList<Character> teamA = new ArrayList<Character>();
   protected ArrayList<Character> teamB = new ArrayList<Character>();
   World world = new World();
   
   // Our first two opponents, additional attackers will be added to the arrayList
   public CombatThread( Character attacker, Character victim )
   {
      teamA.add( attacker );
      teamB.add( victim );
      room = attacker.getRoom(); // Store the room so we dont have to keep looking it up to broadcast
   }
   
   public void addAttacker( Character attacker, Character victim )
   {
      if ( teamA.contains( victim ) )
         teamB.add( attacker );
      else if( teamB.contains( victim ) )
         teamA.add( attacker );
   }
   
	public void run() 
	{
		while ( active )
      {
         try
         {
         
            for ( int i = 0; i < teamA.size(); i++ )
            {
               Character attacker = teamA.get(i);
               world.printToRoomExceptPlayer( room, attacker.getName() + " attacks", attacker );
               attacker.print("You attack");
            }
            
            Thread.sleep(1000);
            
            for ( int i = 0; i < teamB.size(); i++ )
            {
               Character attacker = teamB.get(i);
               world.printToRoomExceptPlayer( room, attacker.getName() + " attacks", attacker );
               attacker.print("You attack");   
            }
         }  
         catch( InterruptedException ex )
         {
            ex.printStackTrace();
         }
      }
   }

}
