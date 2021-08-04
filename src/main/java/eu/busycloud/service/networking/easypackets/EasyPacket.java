package eu.busycloud.service.networking.easypackets;

import java.util.LinkedList;
import java.util.List;

public final class EasyPacket {
	
	private EasyPacketHeader header = null;
	private final List<Object> arguments = new LinkedList<>();
	
	public EasyPacket(EasyPacketHeader header, Object... objects) {
		this.header = header;
		for(Object object : objects)
			arguments.add(object);
	}
	
	public EasyPacketHeader getHeader() {
		return header;
	}
	
	public List<Object> getArguments() {
		return arguments;
	}
	
	public void addArgument(Object argument) {
		arguments.add(argument);
	}

}