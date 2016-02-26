package com.x.business.zerodata.helper;


import java.util.Comparator;

import com.x.db.zerodata.TransferHistory;

/**
* @Description: 收发记录中的拼音比较
*/

public class PinyinComparator implements Comparator<TransferHistory> {

	@Override
	public int compare(TransferHistory o1, TransferHistory o2) {
		if (o1.getFileName().equals("@") || o2.getFileName().equals("#")) {
			return -1;
		} else if (o1.getFileName().equals("#") || o2.getFileName().equals("@")) {
			return 1;
		} else {
			return o1.getFileName().compareTo(o2.getFileName());
		}
	}

}
