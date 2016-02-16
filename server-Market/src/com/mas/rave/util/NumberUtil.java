package com.mas.rave.util;

import java.math.BigDecimal;

public class NumberUtil {
	public static double getDouble(double val) {
		BigDecimal bg = new BigDecimal(val);       
		double j = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return j;
	}

	public static void main(String[] args) {
		System.out.println(NumberUtil.getDouble(10.111111));
	}
}
