package ums.services;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;

public class ShortMessageReceiveWebServiceTest extends TestBase {
		
	
	@Test
	public void send2Test() throws Exception{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		//factory.getInInterceptors().add(new LoggingInInterceptor());
		//factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(IShortMessageReceiveHandler.class);
		factory.setAddress("http://127.0.0.1:8080/ShortMessageServices/ums/pay");
		IShortMessageReceiveHandler shortMessageClinet = (IShortMessageReceiveHandler) factory.create();
		shortMessageClinet.send2("INFO","Test2");
	}
	
	
	
}
