package com.mas.rave.common.web;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.log4j.Logger;

public class FileUploadListener implements ProgressListener {
	Logger log = Logger.getLogger(FileUploadListener.class);
	private long num100Ks = 0;
	private long theBytesRead = 0;
	private long theContentLength = -1;
	private int whichItem = 0;
	private int percentDone = 0;
	private boolean contentLengthKnown = false;

	public void update(long bytesRead, long contentLength, int items) {

		if (contentLength > -1) {
			contentLengthKnown = true;
		}
		theBytesRead = bytesRead;
		theContentLength = contentLength;
		whichItem = items;
		//log.info("FileUploadListener:  目前已读字节数:"+ bytesRead +"   总字节数:" +contentLength);

		long nowNum100Ks = bytesRead / 1000;
		if (nowNum100Ks > num100Ks) {
			num100Ks = nowNum100Ks;
			if (contentLengthKnown) {
				percentDone = (int) Math.round(100.00 * bytesRead
						/ contentLength);
			}

		}
	}

	public int getPercentDone() {
		return percentDone;
	}
	
}
