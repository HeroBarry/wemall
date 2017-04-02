package com.wemall.pay.tenpay.util;

import java.security.MessageDigest;

public class MD5
{

	private static final String hexDigits[] = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
		"a", "b", "c", "d", "e", "f"
	};

	public MD5()
	{
	}

	public static String byteArrayToHexString(byte b[])
	{
		StringBuilder resultSb = new StringBuilder();
		byte abyte0[];
		int j = (abyte0 = b).length;
		for (int i = 0; i < j; i++)
		{
			byte aB = abyte0[i];
			resultSb.append(byteToHexString(aB));
		}

		return resultSb.toString();
	}

	private static String byteToHexString(byte b)
	{
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;

		return (new StringBuilder(String.valueOf(hexDigits[d1]))).append(hexDigits[d2]).toString();
	}

	public static String MD5Encode(String origin)
	{
		String resultString = null;
		try
		{
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resultString;
	}
}
