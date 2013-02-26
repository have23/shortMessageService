package ums.services;

import javax.annotation.Resource;

import org.junit.Test;

public class ShortMessageReceiveHandlerTest extends TestBase {
		
	@Resource(name="myShortMessageReceiveHandlerImp")
	private IShortMessageReceiveHandler shortMessageReceiveHandler;

	public IShortMessageReceiveHandler getShortMessageReceiveHandler() {
		return shortMessageReceiveHandler;
	} 

	
	public void setShortMessageReceiveHandler(
			IShortMessageReceiveHandler shortMessageReceiveHandler) {
		this.shortMessageReceiveHandler = shortMessageReceiveHandler;
	}
	@Test
	public void send2Test() throws Exception{
		shortMessageReceiveHandler.send2("INFO", "测试");
		
		//System.in.read();
	}
	
	
	
}
