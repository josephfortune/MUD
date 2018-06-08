package nettest;

import java.util.Random;

public class Dog extends Character implements Timeable
{

	public Dog( String name )
	{
		super(name);
		worldTimer.subscribe( this ); // Must subscribe for timable actions
	}
	
	public void triggerArrive( Character character )
	{
		world.printToRoomExceptPlayer( character.getRoom(), "The dog barks loudly at " + character.getName() + "'s arrival.", character );
		character.print( "The dog barks loudly at your arrival." );
	}
	
	public void triggerLeave( Character character )
	{
		
		
	}


	public void action1Minute() 
	{
		Random rand = new Random();
		int randomNum = rand.nextInt(3);
				
		if ( randomNum == 0 )
			world.printToRoom( this.getRoom(), name + " starts licking himself." );
		
		if ( randomNum == 1 )
			world.printToRoom( this.getRoom(), name + " rolls on the ground." );
		
		if ( randomNum == 2 )
		{
			// Try to leave through random exit, if exists
			randomNum = rand.nextInt( 6 );

			if ( world.moveCharacterThroughExit( this, randomNum ) == false )
				world.printToRoom( this.getRoom(), name + " sniffs around the floor." );
				
		}

		
	}


	public void action6Minute() {

		
	}


	public void action12Minute() {

		
	}


	public void action24Minute() {

		
	}
}
