package mediaTracker;

import java.awt.Point;
import java.io.File;

public class Service {
	
	public static int getInt(String str) {
		if (str.indexOf("-1")!=-1)
			return -1;
		if (str.isEmpty())
			return 0;
		if (Character.isDigit(str.charAt(str.length()-1)))
			return str.charAt(str.length()-1)-'0'+getInt(str.substring(0,str.length()-1))*10;
		return getInt(str.substring(0,str.length()-1));
	}
	
	public static int getNext(File f,int num) {
		if (!f.isDirectory())
			return -1;
		for (int i=++num;i<f.listFiles().length;i++)
			if (getSize(getFile(f,f.listFiles()[i].getName()))>60*Math.pow(10,6))
				return i;
		return -1;
	}
	public static int getPre(File f,int num) {
		if (!f.isDirectory())
			return -1;
		for (int i=--num;i>=0;i--)
			if (getSize(getFile(f,f.listFiles()[i].getName()))>50*Math.pow(10,6))
				return i;
		return -1;
	}
	
	public static long getSize(File f) {
		long res = f.length();
		if (!f.isDirectory())
			return res;
		for (File tmp:f.listFiles())
			if (tmp.isDirectory())
				res+=getSize(tmp);
			else
				res+=tmp.length();
		return res;
	}
	
	public static int[] getLoc(String str) {
		int[] res = { 0, 0 };
		boolean flag=false;
		for (char c:str.toCharArray())
			if (c==32)
				flag=true;
			else
				if (!Character.isDigit(c)){
					int[] tmp = {50,50};
					return tmp;
				}
				else
					if (!flag) {
						res[0]*=10;
						res[0]+=c-48;
					}
					else{
						res[1]*=10;
						res[1]+=c-48;
					}
		return res;
	}
	
//	public static int nameCheck(String str,boolean flag) {
//		if (str.length()<1)
//			return 0;
//		if (!flag) {
//			if (Character.isAlphabetic(str.charAt(0)))
//				return 1+nameCheck(str.substring(1),true);
//		}
//		else
//			if (Character.isDigit(str.charAt(0)))
//				return 1;
//		return nameCheck(str.substring(1),flag);
//	}
	
	public static int getSum(String str) {
		if (str.isEmpty())
			return 0;
		return str.charAt(0)+getSum(str.substring(1));
	}
	
//	public static String[] sortArr(String[] arr) {
//		String str;
//		for (int i=1;i<=arr.length;i++)
//			for (int j=0;j<arr.length-i;j++)
//				if ((getSum(arr[j])>getSum(arr[j+1]))) {
//					str=arr[j];
//					arr[j]=arr[j+1];
//					arr[j+1]=str;
//				}
//		return arr;
//	}

	public static File getFile(File f,String str) {
		if (str.isEmpty() || !f.exists())
			return null;
		for (File res:f.listFiles())
			if (res.getName().toLowerCase().indexOf(str.toLowerCase())!=-1)
				return res;
		return null;
	}
	
	public static int getFileInt(String[] arr,String str) {
		if (str.isEmpty())
			return -1;
		for (int i=0;i<arr.length;i++)
			if (arr[i].toLowerCase().indexOf(str.toLowerCase())!=-1)
				return  i;
		return -1;
	}
	
}
