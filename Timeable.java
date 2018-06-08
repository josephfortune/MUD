// This interface is for any game object that needs to have a recurring action at certain intervals
// Before the action can take place, the object must subscribe to the worldtimer

package nettest;

public interface Timeable 
{
	public void action1Minute();
	public void action6Minute();
	public void action12Minute();
	public void action24Minute();
	
}
