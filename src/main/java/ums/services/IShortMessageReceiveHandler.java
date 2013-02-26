package ums.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style=Style.RPC, use=Use.LITERAL)
public interface IShortMessageReceiveHandler {
	
	@WebMethod(operationName="send")  
	public boolean send(@WebParam(name="number")String number, @WebParam(name="content")String content);
	
	@WebMethod(operationName="send2")
	public boolean send2(@WebParam(name="number")String adviceType, @WebParam(name="content")String content);
	
}
