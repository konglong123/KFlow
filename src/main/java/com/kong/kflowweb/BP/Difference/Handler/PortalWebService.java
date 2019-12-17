package com.kong.kflowweb.BP.Difference.Handler;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.kong.kflowweb.BP.Sys.SystemConfig;

public class PortalWebService {

	/**
	 * 发送消息
	 *
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @return
	 * @throws Exception
	 */
	public boolean SendToWebServices(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl) throws Exception {
		return HttpRequest(msgPK, sender, sendToEmpNo, tel, msgInfo,title,openUrl,"SendToWebServices");
	}
	/**
	 * 发送站内消息
	 *
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @return
	 * @throws Exception
	 */
	public boolean SendToCCMSG(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl) throws Exception {
		return HttpRequest(msgPK, sender, sendToEmpNo, tel, msgInfo,title,openUrl,"SendToCCMSG");
	}

	/**
	 * 发送到钉钉
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @return
	 * @throws Exception
	 */
	public boolean SendToDingDing(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl) throws Exception {
		return HttpRequest(msgPK, sender, sendToEmpNo, tel, msgInfo,title,openUrl,"SendToDingDing");
	}
	
	/**
	 * 发送到微信
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @return
	 * @throws Exception
	 */
	public boolean SendToWeiXin(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl) throws Exception {
		return HttpRequest(msgPK, sender, sendToEmpNo, tel, msgInfo,title,openUrl,"SendToWeiXin");
	}
	
	/**
	 * 发送到即时通
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @return
	 * @throws Exception
	 */
	public boolean SendToCCIM(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl) throws Exception {
		return HttpRequest(msgPK, sender, sendToEmpNo, tel, msgInfo,title,openUrl,"SendToCCIM");
	}
	
	/**
	 * 调用Jflow-web中WebService接口
	 * @param msgPK
	 * @param sender
	 * @param sendToEmpNo
	 * @param tel
	 * @param msgInfo
	 * @param title
	 * @param openUrl
	 * @param method
	 * @return
	 */
	private boolean HttpRequest(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title,String openUrl, String method) throws Exception {
		String webPath = SystemConfig.getAppSettings().get("HostURL")+"/services/PortalInterfaceWS";
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(webPath);
		call.setOperationName(new QName("http://WebServiceImp", method));// WSDL里面描述的接口名称
		call.addParameter("msgPK", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("sender", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("sendToEmpNo", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("tel", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("msgInfo", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("title", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("OpenUrl", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数

		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_BOOLEAN);// 设置返回类型

		boolean result = (boolean) call.invoke(new Object[] {msgPK, sender, sendToEmpNo,tel,msgInfo,title, openUrl});
		// 给方法传递参数，并且调用方法
		System.out.println("result is " + result);
		return result;
	}

}
