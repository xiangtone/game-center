package com.mas.rave.util.mp3;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mas.rave.util.CrawlerFileUtils;
/**
 *
 */
public class MusicInfo {
	private String path="";
	private boolean isAnalysis=false;
	
	private final int HEADER_SIZE=3;
	private byte[] header;
	private String HEAHER_START="ID3";
	
	private byte version;
	
	private byte reVersion;
	
	private byte flag;
	
	private int SIZE_SIZE=4;
	private byte[] size;

	private Map<String, FrameInfo> frameInfos;
	private int LABEL_SIZE=10;


	public MusicInfo() {
		// TODO Auto-generated constructor stub
		super();
		frameInfos=new HashMap<String, FrameInfo>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isAnalysis() {
		return isAnalysis;
	}

	public void setAnalysis(boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getReVersion() {
		return reVersion;
	}

	public void setReVersion(byte reVersion) {
		this.reVersion = reVersion;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte[] getSize() {
		return size;
	}

	public void setSize(byte[] size) {
		this.size = size;
	}

	public Map<String, FrameInfo> getFrameInfos() {
		return frameInfos;
	}

	public int parseMusic() throws Exception{
		return parseMusic("UTF-16");
	}
	
	public int parseMusic(String charset) throws Exception{
		File file=new File(path);
		if(!file.exists()){
			return 2;
		}
		if(!file.getName().endsWith(".mp3")){
			return 1;
		}
		try {
			RandomAccessFile raf=new RandomAccessFile(file, "r");
			/**
			 * ͷ����Ϣ
			 */
			header=new byte[HEADER_SIZE];
			raf.read(header, 0, HEADER_SIZE);
			if(new String(header).equals(HEAHER_START)){
				version=raf.readByte();
				reVersion=raf.readByte();
				flag=raf.readByte();
				size=new byte[SIZE_SIZE];
				raf.read(size);
				for(int i=0;i<size.length;i++){
				}
				byte []label=new byte[LABEL_SIZE];
				raf.read(label);
				FrameInfo frameInfo=null;
				while ((frameInfo=decodeFrame(label))!=null) {
					raf.skipBytes(1);
					int frameContentSize=frameInfo.getFrameContentSize();
					if(frameContentSize>raf.length()){
						break;
					}
					
					byte []content=new byte[frameContentSize];
					raf.read(content);
					frameInfo.setContent(content);
					frameInfos.put(frameInfo.getFrameId(), frameInfo);
					raf.read(label);
					
				}
				raf.close();
			}
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 3;
		}

	}
	public boolean hasImage(String path){
		setPath(path);
		int i = 0;
		try {
			i = parseMusic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		if(i==0){
			if(frameInfos==null){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	public Map<String, byte[]> getImage(){
		if(frameInfos==null)return null;
		FrameInfo apicInfo=frameInfos.get("APIC");
		if(apicInfo==null)return null;
		byte[]apic=apicInfo.getContent();

		boolean isMIMEComplte=false;
		int i=0;
		Map<String, byte[]>map=new HashMap<String, byte[]>();
		for(;i<apic.length;i++){
			if(!isMIMEComplte&apic[i]=='\0'){
				byte[] mime=new String(apic, 0, i).getBytes();
				map.put("mime", mime);
				isMIMEComplte=!isMIMEComplte;
			}
			if(apic[i]==((byte) 0xff)&&apic[i+1]==((byte) 0xd8)){
				byte[] data=new byte[apic.length-i];
				for(int j=0;j<data.length;j++){
					data[j]=apic[i+j];
				}
				map.put("data", data);
				System.out.println(data);
				return map;
			}

		}
		return null;
	}
	public static File writeImage(Map<String, byte[]>map,String path,String fileName) throws Exception{
		byte []apic=null;
		if(null!=map && (apic=map.get("data"))!=null){
				String mime=new String(map.get("mime"));
				String hz="jpg";
				if(mime!=null){
					hz=mime.replace("image/", "");
				}
				CrawlerFileUtils.createFile(path+File.separator+fileName+"."+hz);
				File file=new File(path+File.separator+fileName+"."+hz);
				System.out.println(file.getAbsolutePath());
				FileOutputStream fos=new FileOutputStream(file);
				fos.write(apic,0,apic.length);
				fos.flush();
				fos.close();
				return file;			
		}
		return null;
	}
	public String getTitle(String charset){
		if(frameInfos==null)return null;
		FrameInfo titleInfo=frameInfos.get("TIT2");
		if(titleInfo==null)return null;
		try {
			String title=new String(titleInfo.getContent(), charset);
			return title;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getTitle(){
		return getTitle("UTF-16");
	}
	public String getPerformer(String charset){
		if(frameInfos==null)return null;
		FrameInfo performerInfo=frameInfos.get("TPE1");
		if(performerInfo==null)return null;
		try {
			String performer=new String(performerInfo.getContent(), charset);
			return performer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getPerformer(){
		return getPerformer("UTF-16");
	}
	public String getAlbum(String charset){
		if(frameInfos==null)return null;
		FrameInfo albumInfo=frameInfos.get("TALB");
		if(albumInfo==null)return null;
		try {
			String album=new String(albumInfo.getContent(), charset);
			return album;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getAlbum(){
		return getAlbum("UTF-16");
	}
	private String parseDecimalToBinary(byte decimal){
		int temp=decimal;
		String tempStr="";
		for(int j=0;j<8;j++){
			int x=(temp>>j)&1;
			tempStr=x+tempStr;
		}
		return tempStr;
	}
	private FrameInfo decodeFrame(byte[]frameHead){

		if(frameHead.length!=LABEL_SIZE){
			return null;
		}
		try {
			String frameId=new String(frameHead,0,4);
			Pattern pattern=Pattern.compile("[A-Z]{3}[A-Z0-9]{1}");
			Matcher matcher=pattern.matcher(frameId);
			if(!matcher.matches()){
				return null;
			}
			int qw=frameHead[4];
			int bw=frameHead[5];
			int sw=frameHead[6];
			int gw=frameHead[7];
			if(qw<0){
				qw=Math.abs(qw)+128;
			}
			if(bw<0){
				bw=Math.abs(bw)+128;
			}
			if(sw<0){
				sw=Math.abs(sw)+128;
			}
			if(gw<0){
				gw=Math.abs(gw)+128;
			}
			//			int frameContentSize=new Integer(new String(frameHead,4,4));
			int frameContentSize= qw*0x1000000
					+bw*0x10000
					+sw*0x100
					+gw-1;
			byte []flag=new byte[2];
			flag[0]=frameHead[8];
			flag[1]=frameHead[9];
			FrameInfo frameInfo=new FrameInfo();
			frameInfo.setFrameId(frameId);
			frameInfo.setFrameContentSize(frameContentSize);
			frameInfo.setFlag(flag);
			return frameInfo;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String toString() {
		return toString("UTF-16");
	}
	public String toString(String charset) {
		return "title:"+getTitle(charset)+"\nperformer:"+getPerformer(charset)+"\nalbum:"+getAlbum(charset);
	}
}