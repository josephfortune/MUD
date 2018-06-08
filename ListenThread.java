// ListenThread is the thread of execution responsible for listening for incoming connections and distributing them to player threads
// Written by Joseph Fortune
// 12/30/2014

package nettest;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ListenThread implements Runnable 
{

	ServerSocket listener;
	Thread[] availableThreads;
	byte threadID;
	
	public ListenThread()
	{
		// Open listen socket
		try
		{
			listener = new ServerSocket(4000);
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}
		
		// Keep track of available threads, rollover after 255
		availableThreads = new Thread[256];
		threadID = 0;
		
	}
	
	public void run() 
	{
		try
		{
			while (true)
			{
				// Accept incoming connection
				System.out.println("Listening...");
				Socket socket = listener.accept();
				
				// Distribute connections to their own threads
				availableThreads[threadID] = new Thread(new PlayerThread(socket), Byte.toString(threadID));
				availableThreads[threadID].start();
				threadID++;
			}
					
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}


	}

}
