// PlayerThread takes over the flow of execution from the individual socket connections. Each player is given a thread.
// Written by Joseph Fortune
// 12/30/2014

package nettest;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PlayerThread implements Runnable 
{
	// ********************** Private Variables ********************** \\
	Socket socket;
	Player player;
	PrintWriter out;
	BufferedReader in;
	World world;
	WorldTimer worldTimer;
	boolean active;
	
	// ********************** Constructor ********************** \\
	public PlayerThread( Socket socketIn )
	{
		// System inits
		socket = socketIn;
		active = true;
				
		// Setup I/O
		try
		{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}
		
		// World/Player inits
		world = new World(); // A whole new world!
		worldTimer = new WorldTimer();
		player = new Player( "UNNAMED", out);
		player.setName( "UNNAMED" );
		player.setRoom( world.getRoomByName("Room1") );
	}
	
	// ********************** Print Line ********************** \\
	public void println( String text )
	{
		out.print( text + "\r\n" );
	}
	
	// ********************** Login ********************** \\
	void login()
	{
		try
		{
			// **** Begin dialogue **** 
			out.println("Connected...");
			out.println("Enter name");
			
			// Create player
			boolean invalidName = true;
			while ( invalidName )
			{
				if ( player.setName( in.readLine() ) ) 
					invalidName = false;
				else
					out.println("Name can only contain letters. Try again.");
			}
	
			System.out.println( player.getName() + " has connected" );
			
			world.addPlayer( player );
			
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}
	}
	
	
	// ********************** shutdown ********************** \\
	void shutdown()
	{
		// Shutdown and clean-up
		world.removePlayer( player );
		try
		{
			socket.close();
			System.out.println( player.getName() + " disconnected");
		}
		catch ( IOException e )
		{
			System.err.println( "IOException: " + e.getMessage() );
			System.out.println( "Error disconnecting player: " + player.getName() );
		}
	}
	
	
	// ********************** Thread's execution ********************** \\
	public void run()
	{
		login();
		world.printRoomDesc( player );
		while ( active )
		{
			// Telnet loop
			processCommand();
		}
		shutdown();
		
	}

	// ********************** processCommand ********************** \\
	void processCommand()
	{
 
		String command = null;
		
		// Read in command, and set Thread to inactive if NULL
		try
		{
			command = in.readLine();
			if ( command == null )
         {
				active = false;
            return;
         }
		}
		catch ( IOException e )
		{
			System.err.println("IOException: " + e.getMessage());
		}

		// ======================== COMMANDS ============================= \\
		
		if ( command.equals( "n" ) || command.equals( "north" ) )
			world.movePlayerThroughExit( player, 0 );
		
		else if ( command.equals( "s" ) || command.equals( "south" ) )
			world.movePlayerThroughExit( player, 1 );
		
		else if ( command.equals( "e" ) || command.equals( "east" ) )
			world.movePlayerThroughExit( player, 2 );
		
		else if ( command.equals( "w" ) || command.equals( "west" ) )
			world.movePlayerThroughExit( player, 3 );
		
		else if ( command.equals( "u" ) || command.equals( "up" ) )
			world.movePlayerThroughExit( player, 4 );
		
		else if ( command.equals( "d" ) || command.equals( "down" ) )
			world.movePlayerThroughExit( player, 5 );
		
		else if ( command.equals( "look" ) )
			world.printRoomDesc( player );
		
		else if ( command.equals( "time" ) )
			player.print( "The time is " + worldTimer.getTime() );
		
		else if ( command.equals( "quit" ) || command.equals( "exit" ))
			// Setting active to false terminates the loop and initiates the shutdown/cleanup procedure
			active = false;
		
		else if ( command.startsWith( "goto") )
		{
			String newRoom = command.substring( 5 );
			world.movePlayer( player, newRoom );
		}
		
		else if ( command.startsWith( "say" ) )
			world.playerSay( player, command.substring( 4 ) );
         
      else if ( command.startsWith("kill ") )
      {
         world.attack( player, command.substring( 5 ) );
      }

		else
			out.println( "Unknown command" );
	}

}
