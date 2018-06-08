package nettest;

import java.util.Random;

public class Droid extends Character implements Timeable
{
   protected boolean following = false;
   Character master = null;

	public Droid( String name )
	{
		super(name);
		worldTimer.subscribe( this ); // Must subscribe for timable actions
	}
	
	public void triggerArrive( Character character )
	{
		world.printToRoomExceptPlayer( character.getRoom(), "The droid beeps at " + character.getName() + "'s arrival.", character );
		character.print( "The droid beeps at your arrival." );
	}
	
	public void triggerLeave( Character character, int exitIndex )
	{
		if ( following && character == master )
      {
         System.out.println("trying to follow" + character.getName() );
         world.moveCharacterThroughExit( this, exitIndex  );
      }
		
	}
   
   public void triggerSay( Character character, String speech )
   {
      if ( speech.contains("droid follow") )
      {
         following = true;
         master = character;
         
         // notify everyone in room
         world.printToRoomExceptPlayer( character.getRoom(), "The droid starts following " + character.getName() + '.', character );
         character.print( "The droid starts following you." );
      }
      else if ( speech.contains("droid stop following") )
      {
         following = false;
         master = null;
         
         // notify other players
         world.printToRoomExceptPlayer( character.getRoom(), "The droid stops following " + character.getName() + '.', character );
         character.print( "The droid stops following you." );
      }
   }


	public void action1Minute() 
	{

		
	}


	public void action6Minute() 
   {

		
	}


	public void action12Minute() 
   {

		
	}


	public void action24Minute() 
   {

		
	}
}
