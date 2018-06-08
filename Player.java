package nettest;

import java.io.PrintWriter;

public class Player extends Character
{
	PrintWriter out;
	
	// ********************** Constructor ********************** \\
	public Player( String name, PrintWriter newOut)
	{
		super(name);
		out = newOut;
	}
	
	// ********************** setPrintWriter ********************** \\
	public void setPrintWriter( PrintWriter newOut )
	{
		out = newOut;
	}
		
	// ********************** print ********************** \\
	public void print( String text )
	{
		out.println( text );
	}
}
