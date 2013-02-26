/**
 * Copyright (C) Momentek.  All rights reserved.
 *
 * This software is the confidential and proprietary
 * information of Momentek. ("Confidential Information"). 
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of
 * the license agreement you entered into with Momentek.
 */

package ums.services;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.momentek.smsapi.MessageClient;
import com.momentek.smsapi.MessageListener;
import com.momentek.smsapi.SmsDR;
import com.momentek.smsapi.SmsMO;
import com.momentek.smsapi.SmsMT;
import com.momentek.smsapi.SmsUtil;

/**
 * @author weitao
 *
 */
@WebService 
public class ShortMessageReceiveHandlerImp implements MessageListener,IShortMessageReceiveHandler{
	private static Logger logger = LoggerFactory.getLogger(ShortMessageReceiveHandlerImp.class);
	private MessageClient client = null;
	private int drCount = 0;
	private int mtCount = 0;
	private String username; 
	private String password;
	private String remoteAddr;
	private int remotePort;
	private String serviceCode;
	private long waitTime;
	private String extendCode;
	private String protocol;
	private Map<String,String> phoneNumber;
	
	private DaoDB daodb;
	
	public DaoDB getDaodb() {
		return daodb;
	}

	public void setDaodb(DaoDB daodb) {
		this.daodb = daodb;
	}

	public int getDrCount() {
		return drCount;
	}

	public void setDrCount(int drCount) {
		this.drCount = drCount;
	}

	public int getMtCount() {
		return mtCount;
	}

	public void setMtCount(int mtCount) {
		this.mtCount = mtCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public String getExtendCode() {
		return extendCode;
	}

	public void setExtendCode(String extendCode) {
		this.extendCode = extendCode;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * 初始化
	 * @throws Exception 
	 */
	public void initialization(){
		try {
			client = MessageClient.createInstance(protocol, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 设置客户端参数
		client.setUsername(this.username);
		client.setPassword(this.password);
		client.setRemoteAddr(this.remoteAddr);
		client.setRemotePort(this.remotePort);
		client.setVersion(0x20);
		client.setSocketTimeout(2000);
		if (!client.connect())
			throw new RuntimeException("bind fail");
		//从数据库中取数据
		phoneNumber =new HashMap<String,String>();
		List list =this.daodb.querySql("select adviceType,phoneNumber from advicephonenumber");
		for(Object one:list){
			Object[] objs=(Object[])one;
			logger.info(objs[0].toString()+":"+ objs[1].toString());
			phoneNumber.put(objs[0].toString(), objs[1].toString());
		}
	}

	/**
	 * 处理绑定操作的响应
	 */
	@Override
	public void onBindAck(int arg0) {
		logger.info("onBound: " + arg0); // 0 成功,其他错误
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.momentek.smsapi.MessageListener#onClosed(java.util.ArrayList)
	 */
	@Override
	public void onClosed(ArrayList arg0) {
		//arg0中为所有未提交到95516短信平台的短信
		logger.info("onClosed:" + arg0);
		if(arg0!=null&&arg0.size()!=0){
			Iterator iter = arg0.iterator();
			while(iter.hasNext()){
				SmsMT mt = (SmsMT) iter.next();
				logger.info("Result    : "+mt.getResult());
				logger.info("DestAddr  : "+mt.getDestAddr().toString());
			}
		}
	}

	/**
	 * process delivery-report,处理接收的状态报告
	 */
	@Override
	public void onSmsDR(SmsDR dr) {
		drCount++;
		logger.info("onSmsDR " + drCount);
		logger.info("Source-Addr: " + dr.getSourceAddr()); // 源地址,手机号
		logger.info("Message-ID : " + dr.getMessageID()); // 消息编号
		logger.info("ErrorCode  : " + dr.getErrorCode()); // 错误码信息
		logger.info("Receipt    : " + dr.getReceipt()); // 消息发送的详细状态结果,直接来自网关
		logger.info("Status     : " + dr.getStatus()); // 发送的状态,主要根据这个来确定是否成功
	}

	/**
	 * process mo message, 处理上行信息
	 */
	@Override
	public void onSmsMO(SmsMO mo) {
		logger.info("onSmsMO");
		logger.info("Data-Coding  : " + mo.getDataCoding()); // 内容编码格式
		logger.info("Dest-Addr    : " + mo.getDestAddr()); // 目标地址
		logger.info("Content      : " + SmsUtil.getLetter(mo)); // 内容
		logger.info("Service-Code : " + mo.getServiceCode()); // 业务代码
		logger.info("Source-Addr  : " + mo.getSourceAddr()); // 源手机号
	}

	/**
	 * process SmsMT response,处理发送MT消息的反馈
	 */
	@Override
	public void onSmsMTAck(SmsMT ack) {
		mtCount++;
		logger.info("onSmsMTAck " + mtCount);
		logger.info("Message-ID: " + ack.getMessageID()); // 消息编号
		logger.info("Result    : " + ack.getResult()); // 发送结果
		logger.info("content   "+ack.getContent());

	}

	/**
	 * 发送短信
	 * 
	 * @param msgId
	 * @param number
	 * @param content
	 */
	@Override
	public boolean send(String number, String content) {

		if (client == null || !client.isBound()) {
			try {
				initialization();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		// 同步发送时timeout填写等待应答的时间,单位ms
		// 异步发送填写0,发送结果在onSmsMTAck返回
		client.setMaxChineseChars(61); //设置中文最大长度
		client.setMaxEnglishChars(149); //设置英文最大长度
		//参数对应顺序: 扩展码，手机号;号分割，消息内容，优先级，是否报告，servicecode,timeout,tagObject,发送间隙
		ArrayList<SmsMT> list = client.send("", number, content, 0, 1,serviceCode, 0, null,3000);
		/*  MessageClient
			public ArrayList send(String sourceAddr, String destAddr, String message, int priority, 
			int registeredDelivery, String serviceCode, long timeout, Object tag, int pause);
		*/
		//发送结果检查,一条长短信会拆分成多条下发。
		for(SmsMT m:list){
			logger.info("Result:"+m.getResult()); //发送结果
			logger.info("DataCoding:"+m.getDataCoding());
			logger.info("msgLength:"+m.getContent().length);
			try { //内容查看
				if(m.getDataCoding()==8){
					String ssss = new String(m.getContent(),"UTF-16BE");
					logger.info(ssss.length()+" "+ssss);
				}else if(m.getDataCoding()==15){
					String ssss = new String(m.getContent(),"GB18030");
					logger.info(ssss.length()+" "+ssss);
				}else{
					logger.info(new String(m.getContent()));
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	/**
	 * 关闭client
	*/
	public void close() {
		client.close();
	}

	@Override
	public boolean send2(String adviceType, String content) {
		System.out.println("invoke send2");
		String number=this.phoneNumber.get(adviceType);
		if(number==null) throw new RuntimeException("获取通知phoneNumber为null");
		return this.send(number, content);
	}
	

}
