package de.plugdev.cloud.packets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class Packet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2753522991464148398L;
	private long id;
	protected List<String> headerList = new ArrayList<String>();
	protected List<Object> objectList = new ArrayList<Object>();
	
}