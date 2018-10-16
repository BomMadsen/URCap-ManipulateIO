package com.jbm.urcap.sample.manipulateIO.impl;

import java.util.Collection;
import java.util.Iterator;

import com.ur.urcap.api.domain.io.AnalogIO;
import com.ur.urcap.api.domain.io.DigitalIO;
import com.ur.urcap.api.domain.io.IOModel;

public class IOHandler {

	private final IOModel ioModel;
	
	public IOHandler(IOModel ioModel) {
		this.ioModel = ioModel;
	}
	
	
	/*Returns a DigitalIO object found by its default name
	 * Default names are: 
	 * 	digital_in[0]
	 *  digital_in[1]
	 *  ...
	 *  digital_in[7]
	 *  digital_out[0]
	 *  digital_out[1]
	 *  ...
	 *  digital_out[7]
	 *  tool_in[0]
	 *  tool_in[1]
	 *  tool_out[0]
	 *  tool_out[1]
	 *  config_in[0]
	 *  config_in[1]
	 *  ...
	 *  config_in[7]
	 *  config_out[0]
	 *  config_out[1]
	 *  ...
	 *  config_out[7]
	 * 
	 */
	public DigitalIO getDigitalIO(String defaultName){
		Collection<DigitalIO> IOcollection = ioModel.getIOs(DigitalIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0){
			Iterator<DigitalIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				DigitalIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
//				System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
	
	/*Returns an AnalogIO object found by its default name
	 * Default names are: 
	 *  analog_in[0]
	 *  analog_in[1]
	 *  analog_in[2] 	(Tool analog in 0)
	 *  analog_in[3]	(Tool analog in 1)
	 *  analog_out[0]
	 *  analog_out[1]
	 * 
	 */
	public AnalogIO getAnalogIO(String defaultName){
		Collection<AnalogIO> IOcollection = ioModel.getIOs(AnalogIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0){
			Iterator<AnalogIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				AnalogIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
//				System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
	
	
}
