package simu.framework;
import eduni.distributions.*;

public class ArrivalProcess {
	
	private ContinuousGenerator generator;
	private EventList eventList;
	private IEventType type;

	public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType type){
		this.generator = g;
		this.eventList = tl;
		this.type = type;
	}

	public void generateNext(){
		Event t = new Event(type, Clock.getInstance().getTime()+ generator.sample());
		eventList.add(t);
	}

}
