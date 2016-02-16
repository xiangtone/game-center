/**
 * @author haikuan.huang
 *
 */
package com.Indomog.pay.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

import com.Indomog.pay.utils.Util;

public class IndomogConfig{
	private static Properties p = new Properties();
    static{
       InputStream in = Util.class.getResourceAsStream("/indomogConfig.Properties");
       try {
			p.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public static Integer exchange = 100;
	
	protected static String url = getIndomogConfig("url");
	
	protected static String secretkey = getIndomogConfig("secretkey");
	
	protected static String RMID=getIndomogConfig("RMID");
	
	protected static String Alg=getIndomogConfig("Alg");
	
	protected static String AlgID=getIndomogConfig("AlgID");
	
	protected static String Name="uplay";
	
	protected static String EmailHP="christine.tan@zymko.com";
	
	protected static String IPD = getIPD();
	
    public static String getIndomogConfig(String code){
		return p.get(code).toString().trim();
    }
	private static String getIPD() {
		try {
			String os = System.getProperty("os.name");
			if (os != null && os.toUpperCase().indexOf("LINUX") > -1){
				Enumeration<NetworkInterface> netInterfaces=NetworkInterface.getNetworkInterfaces();
				while(netInterfaces.hasMoreElements()){
					NetworkInterface   ni=(NetworkInterface)netInterfaces.nextElement();
					System.out.println(ni.getName());
					InetAddress ip = (InetAddress)ni.getInetAddresses().nextElement();
					if(!ip.isSiteLocalAddress() & !ip.isLoopbackAddress() && ip.getHostAddress().indexOf( ": ")==-1){
						IPD = ip.getHostAddress();
					}else{
						IPD = "127.0.0.1";
					}
				}
			}else{
				InetAddress localHost = InetAddress.getLocalHost();
				IPD = localHost.getHostAddress();
		    }
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(IPD);
		return IPD;
	}
	
	
}