// World is in charge of containing all of the entities and the functions that act upon them
// Written by Joseph Fortune
// 12/30/2014

package nettest;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class World 
{
	static ArrayList<Player> playerList = new ArrayList<Player>();
	static ArrayList<Room> roomList = new ArrayList<Room>();
	static ArrayList<Character> characterList = new ArrayList<Character>();
   Timer timer = new Timer();
	
	// The NULL room!
	Room NULLroom = new Room("null");
	
	// Temporary rooms
	static Room room1 = new Room("Room1");
	static OutsideRoom room2 = new OutsideRoom("Room2");
	static OutsideRoom room3 = new OutsideRoom("Room3");
	static OutsideRoom room4 = new OutsideRoom("Room4");
	static OutsideRoom room5 = new OutsideRoom("Room5");
	
	public void init()
	{
		// The NULL room!
		NULLroom.setDescription( "You are in the NULL room!");
		NULLroom.setLight( true );
		// Temporary rooms
		room1.setDescription( "This recreational tent has the typical pentagon shape. \r\nThe walls are slightly damp with dew. The tarp-like floor \r\ncrackles with every movement. A dangling flashlight emits a soft glow" );
		room1.setTitle( "Inside The Tent" );
		room1.setExit( 1, "Room2" );
		room1.setLight( true );
		
		room2.setTitle( "In Front of The Tent" );
		room2.setDescription( "The background is dominated by green forest. The smell \r\nof smoldering fire permeates the air. There is a current of warm, \r\nstale air from the tent. " );
		room2.setExit(0, "Room1");
		room2.setExit( 1, "Room3" );
		
		room3.setTitle("Along the path");
		room3.setDescription( "Both sides of the path are dense with walls of trees \r\nexcept for the clearing to the North.");
		room3.setExit( 0, "Room2" );
		room3.setExit( 2, "Room5" );
		room3.setExit( 3, "Room4" );
		
		room4.setTitle("Along the path");
		room4.setDescription( "This section of the path is damp and muddy. Ruts formed \r\n from passing game have pooled with water. There is a hint of \r\nsmoke in the air.");
		room4.setExit( 2, "Room3" );
		
		room5.setTitle( "Along the path" );
		room5.setDescription( "The path is beginning to veer to the south. This section \r\n is fairly dry, packed with harder dirt.");
		room5.setExit( 3, "Room3" );
		
		
		roomList.add(NULLroom);
		roomList.add(room1);
		roomList.add(room2);
		roomList.add(room3);
		roomList.add(room4);
		roomList.add(room5);
		
		Dog otis = new Dog("A scruffy dog");
		otis.setRoom(room1);
		characterList.add(otis);
      
      Droid droid = new Droid("A droid");
      droid.setRoom(room1);
      characterList.add(droid);

	}
	
	// ======================== Thread Stuff ===========================
	public void lock( boolean locked )
	{
		while ( locked );
		locked = true;
	}
	
	public void unlock ( boolean locked )
	{
		locked = false;
	}
	
	// ======================== World Stuff ============================
	public void addPlayer( Player newPlayer )
	{
		playerList.add( newPlayer );
	}
	
	public void removePlayer( Player player2Remove )
	{
		playerList.remove( player2Remove );
	}
	
	public Room getNullRoom()
	{
		return NULLroom;
	}
	
	public void addRoom( Room newRoom )
	{
		roomList.add( newRoom );
	}
	
	public Room getRoomByName( String name )
	{
		for ( int i = 0; i < roomList.size(); i++ )
		{
			if ( roomList.get(i).getName().equals(name) )
				return roomList.get(i);
		}
		return null;
	}
	
	public Room getRoomByPlayer( Player player )
	{
		return player.getRoom();
	}
	
	public String getTitleByRoom( Room room )
	{
		return room.getTitle();
	}
	
	public ArrayList<Player> getPlayersByRoom( Room room )
	{
		ArrayList<Player> players = new ArrayList<Player>();
		
		// Search through all the players and their rooms and add the matching ones to the arraylist
		for ( int i = 0; i < playerList.size(); i++ )
		{
			if ( playerList.get( i ).getRoom() == room )
				players.add( playerList.get( i ) );
		}
		
		return players;
	}
	
	public ArrayList<Character> getCharactersByRoom( Room room )
	{
		ArrayList<Character> characters = new ArrayList<Character>();
		
		// Search through all the characters and their rooms and add the matching ones to the arraylist
				for ( int i = 0; i < characterList.size(); i++ )
				{
					if ( characterList.get( i ).getRoom().equals( room ) )
						characters.add( characterList.get( i ) );
				}
				
				return characters;
		
	}
	
	boolean movePlayerMUTEX = false;
	public void movePlayer ( Player player, String newRoomStr )
	{
      Room newRoom = getRoomByName( newRoomStr );
      
		lock( movePlayerMUTEX );
		if ( newRoom != null )
		{
			// Need old room for alerts
			Room oldRoom = player.getRoom();
			
			// Alert AI in current room
			//alertTriggerLeave( player ); maybe dont need this is vanishing
			
			// Change room
			player.setRoom( newRoom );
			
			// Update player screen
			printRoomDesc( player );
			
			// Update people of players departure
			printToRoomExceptPlayer( oldRoom, player.getName() + " has vanished into thin air.", player );
			
			// Update people of player's arrival
			printToRoomExceptPlayer( newRoom, player.getName() + " has appeared.", player);
			
			// Alert AI in new room
			alertTriggerArrival( player );
		}
		else
		{
			// Error message
			player.print( "No such place" );
						
		}
		unlock ( movePlayerMUTEX );
	}
	
	boolean movePlayerThroughExitMUTEX = false;
	public void movePlayerThroughExit( Player player, int exitIndex )
	{
		lock ( movePlayerThroughExitMUTEX );
		
		String[] exitDirections = { "North", "South", "East", "West", "Up", "Down" };
		Room oldRoom = player.getRoom();
		Room newRoom = getRoomByName(oldRoom.getExits()[ exitIndex ]);
		
		if ( newRoom != null )
		{
			// Update room
			player.setRoom( newRoom );
			
			// Update player screen
			printRoomDesc( player );
			
			// Notify players of departure
			// If theres light in that direction
			if ( oldRoom.hasLight() )
				printToRoomExceptPlayer( oldRoom, player.getName() + " departed " + exitDirections[ exitIndex ], player );
			// No light
			else
				printToRoomExceptPlayer( oldRoom, "Someone has departed " + exitDirections[ exitIndex ], player );
			
			// Notify players of arrival if theres light
			if ( newRoom.hasLight() )	
				printToRoomExceptPlayer( newRoom, player.getName() + " arrived", player );
			else
				printToRoomExceptPlayer( newRoom, "Someone has arrived", player );
         
			// Alert AI in new room
			alertTriggerArrivalDelayed( player );
         
         // Alert AI of departure in old room
         alertTriggerLeave( player, oldRoom, exitIndex );

		}
		else
			player.print( "You can't go that way." );
		
		unlock( movePlayerThroughExitMUTEX );
	}
	
	boolean moveCharacterThroughExitMUTEX;
	public boolean moveCharacterThroughExit( Character character, int exitIndex )
	{
		boolean result = false;
		
		lock ( moveCharacterThroughExitMUTEX );
		
		String[] exitDirections = { "North", "South", "East", "West", "Up", "Down" };
		Room oldRoom = character.getRoom();
		Room newRoom = getRoomByName( oldRoom.getExits()[ exitIndex ] );
		
		if ( newRoom != null)
		{
			// Update room
			character.setRoom( newRoom );
			
			// Notify players of departure
			// If theres light in that direction
			if ( oldRoom.hasLight() )
				printToRoom( oldRoom, character.getName() + " departed " + exitDirections[ exitIndex ] );
			// No light
			else
				printToRoomExceptPlayer( oldRoom, "Someone has departed " + exitDirections[ exitIndex ], character );
			
			// Notify players of arrival if theres light
			if ( newRoom.hasLight() )	
				printToRoom( newRoom, character.getName() + " arrived" );
			else
				printToRoomExceptPlayer( newRoom, "Someone has arrived", character );
			
         
			// Alert AI in new room
			alertTriggerArrivalDelayed( character );
         
         // Alert AI of departure in old room
         alertTriggerLeave( character, oldRoom, exitIndex );

			result = true;
		}
		
		unlock( movePlayerThroughExitMUTEX );
		return result;
	}
	
	public synchronized void alertTriggerArrivalDelayed( final Character character )
   {
      // We have to have an initial list of the room, otherwise characters following a player
      // will be alerted as if they had been in the arriving room all along
      final ArrayList<Character> firstCharacterList = getCharactersByRoom( character.getRoom() );
   
      
      // All this sorcery is to create a delay before alerting AI of arrival.
      timer.cancel();
      timer = new Timer();
      timer.schedule( 
         new java.util.TimerTask() 
         {
            public void run() 
            {
               // Store the room the character is currently in. If he/she is speeding through the rooms,
               // don't bother to alert AI
               Room roomBeforeDelay = character.getRoom();
               
               // Retrieve and alert characters in room
         		ArrayList<Character> secondCharacterList = getCharactersByRoom( roomBeforeDelay );
               
               // Alert the characters
         		for ( int i = 0; i < secondCharacterList.size(); i++ )
               {
                  Character characterToAlert = secondCharacterList.get( i );
                  
                  // The character doesn't need to alert theirself of their own arrival
                  boolean charToAlertIsNotMe = characterToAlert != character;
                  
                  // The character didn't show up after the delay (ex. a character following the player)
                  boolean charWasHereBeforeDelay = firstCharacterList.contains( characterToAlert );
                  
                  // Don't bother alerting AI if the character isn't even in the room anymore (flying through)
                  boolean stillInSameRoom = character.getRoom() == roomBeforeDelay;
                  
                  if ( stillInSameRoom && charToAlertIsNotMe && charWasHereBeforeDelay )
                     characterToAlert.triggerArrive( character ); // Alert the character
               }
            }
         }, 2000 );

   }
   
   
	public void alertTriggerArrival( Character character )
	{
		// Retrieve and alert characters in room
		ArrayList<Character> characters = getCharactersByRoom( character.getRoom() );
		
      // Alert the characters
		for ( int i = 0; i < characters.size(); i++ )
      {
         Character characterToAlert = characters.get( i );
         
         // The character doesn't need to alert theirself of their own arrival
         if ( characterToAlert != character )
			   characterToAlert.triggerArrive( character );
      }
	}
	
	public void alertTriggerLeave( Character character, Room roomToLeave, int exitIndex )
	{
		// Retrieve and alert characters in room
		ArrayList<Character> characters = getCharactersByRoom( roomToLeave );
		
		for ( int i = 0; i < characters.size(); i++ )
			// Alert the characters
			characters.get( i ).triggerLeave( character, exitIndex );
	}
	
	public void printRoomDesc( Player player )
	{
		Room room = getRoomByPlayer( player );
		
		// Room description
		player.print("\n______________________________________\n"); // Newline
		if ( room.hasLight() ) // OR PLAYER HAS LIGHT, ADD LATER --------------------------------------------------------------
		{
			player.print( room.getTitle() );
			player.print( room.getDescription() );
		}
		else
			player.print("It is too dark to see...");
		
		
		
		// Exits
		player.print("Exits: ");
		String[] exits = room.getExits();
		String[] exitDirections = { "North", "South", "East", "West", "Up", "Down" };
		
		int totalExits = 0;
		for ( int i = 0; i < 6; i++ )
		{
			// If exit is not null
			if ( exits[i] != null )
			{
				// If adjacent rooms have light
				if ( getRoomByName( exits[i] ).hasLight() )
					player.print( exitDirections[i] + " - " + getTitleByRoom( getRoomByName( exits[i] ) ) );
				else
					player.print( exitDirections[i] + " - " + "too dark to tell." );
				totalExits++;
			}
		}
		// If no exits
		if ( totalExits == 0 )
			player.print( "none" );

		
		
		// Retrieve and print players in room
		ArrayList<Player> players = getPlayersByRoom( player.getRoom() );
		
		player.print(""); // Newline
		for ( int i = 0; i < players.size(); i++ )
		{
			// Print the local players, so long as it aint you
			if ( !player.getName().equals( players.get( i ).getName() ) )
			{
				// If theres light
				if ( player.getRoom().hasLight() )
					player.print( players.get( i ).getName() + " is here.");
				else
					player.print( "Someone is here.");
					
			}
			
		}
		
		// Retrieve and print characters in room
		ArrayList<Character> characters = getCharactersByRoom( player.getRoom() );
		
		for ( int i = 0; i < characters.size(); i++ )
			// Print the local characters
			// If theres light
			if ( player.getRoom().hasLight() )
				player.print( characters.get( i ).getName() + " is here");
			else
				player.print( "Someone is here");

      // Add space after player/character list, if there is a list
      if ( characters.size() > 0 )
         player.print(""); // Will generate a new line
	}
	
	public void printToRoom( Room room, String text )
	{
		ArrayList<Player> players = getPlayersByRoom( room );
		
		for ( int i = 0; i < players.size(); i++ )
		{
			players.get( i ).print("\n" + text );
		}
	}
	
	public void printToRoomExceptPlayer( Room room, String text, Character character )
	{
		ArrayList<Player> players = getPlayersByRoom( room );
		
		for ( int i = 0; i < players.size(); i++ )
		{
			if ( !players.get( i ).equals( character ) )
				players.get( i ).print("\n" + text );
		}
	}
   

	public void playerSay( Player player, String text )
	{
		if ( player.getRoom().hasLight() )
		{
			String speech = player.getName() + " says: " + text;
			printToRoomExceptPlayer( player.getRoom(), speech, player );
			player.print( "You said: " + text );
         
         // Retrieve list of characters in room
		   ArrayList<Character> characters = getCharactersByRoom( player.getRoom() ); // THE GETROOM FUNCTION DIRECTLY FROM PLAYER
         
         // Alert the characters
         for ( int i = 0; i < characters.size(); i++ )
			   characters.get( i ).triggerSay( player, speech );
            
		}
		else
		{
			String speech = "Someone says: " + text;
			printToRoomExceptPlayer( player.getRoom(), speech, player );
			player.print( "You said: " + text );
		}
	}
   
   public void attack( Character attacker, String victimStr )
   {
      Thread thread; // for combat
      
      // Make sure player isn't already in combat
      if ( attacker.isInCombat() )
         return;
      
      // Is such a victim even in the room?
      Room room = attacker.getRoom();
      ArrayList<Character> characters = getCharactersByRoom( room );
      ArrayList<Player> players = getPlayersByRoom( room );
      
      // Check against all characters in room
      for ( int i = 0; i < characters.size(); i++ )
      {
         if ( characters.get(i).getName().equals(victimStr) ) // Found the victim, start combat
         {
            Character victim = characters.get(i);
            
            // Is the victim already in combat? Join the thread
            if ( victim.isInCombat() )
            {
               // Join the fight
               attacker.enterCombat( victim.getCombatThread() ); // Set player for combat
               victim.getCombatThread().addAttacker( attacker, victim ); // Add player to combat Thread
               return;     
            }
            else // Start a new thread
            {
               CombatThread combatThread = new CombatThread( attacker, victim );
               thread = new Thread( combatThread, "CombatThread" );
               
               attacker.enterCombat( combatThread );
               victim.enterCombat( combatThread );
               thread.start();
               return;
            }
         }
         
      }
      
      // Check against all players in room
      for ( int i = 0; i < players.size(); i++ )
      {
         if ( players.get(i).getName().equals(victimStr) ) // Found the victim, start combat
         {
            Player victim = players.get(i);
            
            // Is the victim already in combat?
            if ( victim.isInCombat() )
            {
               // Join the fight
               attacker.enterCombat( victim.getCombatThread() ); // Set player for combat
               victim.getCombatThread().addAttacker( attacker, victim ); // Add player to combat Thread
               return;     
            }
            else // start a new thread
            {
               CombatThread combatThread = new CombatThread( attacker, victim );
               thread = new Thread( new CombatThread( attacker, victim ), "CombatThread" );
               
               attacker.enterCombat( combatThread );
               victim.enterCombat( combatThread );
               thread.start();
               return;
            }
         }
         
      }
      
      // Couldn't find a character/player by that name
      attacker.print("No one here by that name.");
   }
	

}
