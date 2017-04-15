/**
 * copyright© rms-plc.com
 */
package util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * 工具类
 * 
 */
public class Util {

	/**
	 * 计算cs
	 * 
	 * @param data  需要计算的字节数组
	 * @param offset
	 * @param len
	 * @return
	 */
	public static int checksum(byte data[], int offset, int len) {
		int cs = 0;
		if (len + offset > data.length) {
			len = data.length - offset;
		}
		for (int i = offset; i < offset + len; i++) {
			cs += data[i];
		}
		return cs & 0xff;
	}

	/**
	 * 将二进制数组打印成字符串,带空格分割
	 * 
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 */
	public static String toHex(byte data[], int offset, int len) {
		StringBuilder sb = new StringBuilder();
		if (len + offset > data.length) {
			len = data.length - offset;
		}
		for (int i = offset; i < len; i++) {
			sb.append(String.format("%02x", data[i] & 0xff)).append(" ");
		}
		return sb.toString();
	}

	/**
	 * 将二进制数组打印成字符串,带空格分割
	 * 
	 * @param data
	 * @return
	 */
	public static String toHex(byte data[]) {
		return toHex(data, 0, data.length);
	}

	/**
	 * 将带空格分割的16进制表示的字符串转换为二进制数组
	 * 
	 * @param hexString
	 * @return
	 * @throws NumberFormatException
	 */
	public static byte[] fromHex(String hexString) throws NumberFormatException {
		String s[] = hexString.split(" ");
		byte ret[] = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			ret[i] = (byte) Integer.parseInt(s[i], 16);
		}
		return ret;
	}

	/**
	 * 将二进制数组打印成字符串,不带空格分割
	 * 
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 */
	public static String toHexNoSpace(byte data[], int offset, int len) {
		StringBuilder sb = new StringBuilder();
		if (len + offset > data.length) {
			len = data.length - offset;
		}
		for (int i = offset; i < len; i++) {
			sb.append(String.format("%02x", data[i] & 0xff));
		}
		return sb.toString();
	}

	/**
	 * 将二进制数组打印成字符串,补带空格分割
	 * 
	 * @param data
	 * @return
	 */
	public static String toHexNoSpace(byte data[]) {
		return toHexNoSpace(data, 0, data.length);
	}

	/**
	 * 将不带空格分割的16进制表示的字符串转换为二进制数组
	 * 
	 * @param hexString
	 * @return
	 * @throws NumberFormatException
	 */
	public static byte[] fromHexNoSpace(String hexString) throws NumberFormatException {
		byte ret[] = new byte[(hexString.length() + 1) / 2];
		for (int i = hexString.length(); i > 0; i -= 2) {
			ret[(i - 1) / 2] = (byte) Integer.parseInt(
					hexString.substring((i - 2) >= 0 ? (i - 2) : i - 1, i), 16);
		}
		return ret;
	}

	/**
	 * 反转数组
	 * 
	 * @param array
	 * @return
	 */
	public static byte[] reverse(byte[] array) {
		byte tmp[] = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			tmp[array.length - 1 - i] = array[i];
		}
		return tmp;
	}

	/**
	 * 为数组的每个元素自加0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static void add33(byte data[], int offset, int length) {
		for (int i = offset; i < offset + length; i++) {
			data[i] += 0x33;
		}
	}

	/**
	 * 为数组的每个元素自减0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static void dec33(byte data[], int offset, int length) {
		for (int i = offset; i < offset + length; i++) {
			data[i] -= 0x33;
		}
	}

	/**
	 * 根据DA1，DA2返回所有的Pn值，以整数方式返回
	 * 
	 * @param DA1
	 * @param DA2
	 * @return
	 */
	public static List<Integer> getPns(int DA1, int DA2) {
		List<Integer> result = new ArrayList<Integer>();
		if (DA1 == 0 && DA2 == 0) {
			result.add(0);
		} else {
			for (int i = 0; i < 8; i++) {
				if (((1 << i) & DA1) == (1 << i)) {// 该位被设置了
					if (DA2 == 0x00) {
						for (int j = 0; j < 255; j++) {
							result.add(j * 8 + i + 1);
						}
					} else {
						result.add((DA2 - 1) * 8 + i + 1);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 把pn转换为DA1，DA2，DA1存放在返回数组的0索引，DA2存放在返回数组的1索引
	 * 
	 * @return
	 */
	public static byte[] setPn(int pn) {
		byte[] result = new byte[2];
		if (pn == 0) {
			result[0] = 0;
			result[1] = 0;
		} else {
			result[0] = (byte) (1 << ((pn - 1) % 8));
			result[1] = (byte) ((pn - 1) / 8 + 1);
		}
		return result;
	}

	/**
	 * 把pn转换为DA1，DA2，DA1存放在返回数组的0索引，DA2存放在返回数组的1索引
	 * 
	 * @return
	 */
	public static void setPn(int pn, byte data[], int offset) {
		if (pn == 0) {
			data[offset] = 0;
			data[offset + 1] = 0;
		} else {
			data[offset] = (byte) (1 << ((pn - 1) % 8));
			data[offset + 1] = (byte) ((pn - 1) / 8 + 1);
		}
	}

	/**
	 * 根据DT1，DT2返回所有的Fn值，以整数方式返回
	 * 
	 * @param DT1
	 * @param DT2
	 * @return
	 */
	public static List<Integer> getFns(int DT1, int DT2) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < 8; i++) {
			if (((1 << i) & DT1) == (1 << i)) {// 该位被设置了
				if (DT2 == 0xff) {
					for (int j = 0; j < 31; j++) {
						result.add(j * 8 + i + 1);
					}
				} else {
					result.add(DT2 * 8 + i + 1);
				}
			}
		}
		return result;
	}

	/**
	 * 把pn转换为DT1，DT2，DT1存放在返回数组的0索引，DT2存放在返回数组的1索引
	 * 
	 * @return
	 */
	public static byte[] setFn(int fn) {
		byte[] result = new byte[2];
		if (fn == 0) {
			result[0] = 0;
			result[1] = 0;
		} else {
			result[0] = (byte) (1 << ((fn - 1) % 8));
			result[1] = (byte) ((fn - 1) / 8);
		}
		return result;
	}

	/**
	 * 把pn转换为DT1，DT2，DT1存放在返回数组的offset索引，DT2存放在返回数组的offset+1索引
	 * 
	 * @return
	 */
	public static void setFn(int fn, byte data[], int offset) {
		if (fn == 0) {
			data[offset] = 0;
			data[offset + 1] = 0;
		} else {
			data[offset] = (byte) (1 << ((fn - 1) % 8));
			data[offset + 1] = (byte) ((fn - 1) / 8);
		}
	}

	public static String A1(byte data[]) {
		return String.format("%02x-%02x-%02x %02x:%02x:%02x 周%d", data[5], data[4] & 0x1f, data[3], data[2],
				data[1], data[0], (data[4] & 0xe0) >> 5);
	}

	public static String A11(byte data[]) {
		String string = String.format("%02x年%02x月%02x日  %02x时%02x分%02x秒 星期%d", data[5], data[4] & 0x1f,
				data[3], data[2], data[1], data[0], (data[4] & 0xe0) >> 5);
		string = string.replace("期0", "期日");
		string = string.replace("期1", "期一");
		string = string.replace("期2", "期二");
		string = string.replace("期3", "期三");
		string = string.replace("期4", "期四");
		string = string.replace("期5", "期五");
		string = string.replace("期6", "期六");
		return string;
	}

	public static String A14(byte data[]) {
		return String.format("%02x%02x%02x.%02x%02xkWh", data[4], data[3], data[2], data[1], data[0]);
	}

	public static String A20(byte data[]) {
		return String.format("%02x年%02x月%02x日", data[2], data[1], data[0]);
	}

	/**
	 * 二进制转换成16进制
	 * 
	 * @param bString
	 * @return
	 */
	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	/**
	 * 十六进制转换成二进制
	 */
	public static String binaryString(byte data) {
		String s = "";
		if (Integer.toBinaryString(byte2ten(data)).length() < 8) {
			for (int i = 1; i <= 8 - Integer.toBinaryString(byte2ten(data)).length(); i++) {
				s += "0";
			}
		}
		String ss = s + Integer.toBinaryString(byte2ten(data));
		return ss;
	}

	/**
	 * 二进制转换成十进制
	 */
	public static String binaryString2ten(String data) {
		BigInteger src = new BigInteger(data, 2);// 转换为BigInteger类型
		return src.toString();
	}

	/**
	 * byte转换成10进制
	 */
	public static int byte2ten(byte data) {
		return (int) data & 0xff;
	}

	/**
	 * 字符串反转
	 */
	public static String strReverse(String str) {
		return new StringBuffer(str).reverse().toString();
	}

	/**
	 * 获取异常的描述字符串，即{@link Throwable#getMessage()}。如果为null，那么就从
	 * {@link Throwable#getCause()}中获取，以此类推，往上5层终止。
	 * 
	 * @param t
	 * @return
	 */
	public static String getExceptionMsg(Throwable t) {
		String msg = t.getClass().getName();
		for (int i = 0; i < 5; i++) {
			if (t.getMessage() == null) {
				if (t.getCause() != null) {
					t = t.getCause();
				} else {
					break;
				}
			} else {
				msg = t.getMessage();
			}
		}
		return msg;
	}

	/**
	 * 
	 * 
	 * @param str
	 * @param strLength
	 * @return str
	 */
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			// sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	/**
	 * 字符串后面加0
	 */
	public static String addZero(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			// sb.append("0").append(str);// 左(前)补0
			sb.append(str).append("0");// 右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}
	
	/**
	 * 去除list重复元素
	 * */
	public static List minusDuplicate(List list){
		for(int i=0;i<list.size();i++){
			for(int j=list.size()-1;j>i;j--){
				if(list.get(j)==list.get(i)){
					list.remove(j);
				}
				if(list.get(j)==null){
					list.remove(j);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 为数组的每个元素自加0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static byte[] add33Return(byte data[], int offset, int length) {
		byte[] ret = new byte[length];
		for (int i = offset; i < offset + length; i++) {
			ret[i-offset] = (byte)((byte)data[i] + (byte)0x33);
		}
		return ret;
	}
	
	/**
	 * 为数组的每个元素自加0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static byte[] add33ReverseReturn(byte data[], int offset, int length) {
		byte[] ret = add33Return(data, offset, length);
		ret = reverse(ret);
		return ret;
	}

	
	/**
	 * 为数组的每个元素自减0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static byte[] dec33Return(byte data[], int offset, int length) {
		byte[] ret = new byte[length];
		for (int i = offset; i < offset + length; i++) {
			ret[i-offset] = (byte)((byte)data[i] - (byte)0x33);
		}
		return ret;
	}

	/**
	 * 为数组的每个元素自加0x33
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 */
	public static byte[] dec33ReverseReturn(byte data[], int offset, int length) {
		byte[] ret = dec33Return(data, offset, length);
		ret = reverse(ret);
		return ret;
	}
	/**
	 * byte minus 0x33
	 * */
	public static byte dec33FromByte(byte b) {
		return (byte) (b - 0x33);
	}
	
	// 字符串按原文10进制转换成16进制字节
		public static byte[] stringToDecBytes(String s) {
			for (int w = 0; w < s.length(); w++) {
				if (s.charAt(0) == '0') {
					s = s.substring(1);
				}
				if (s.charAt(0) != '0') {
					break;
				}
			}
			String ret = s.replaceAll("[^0-9a-fA-F]", "");
			if (ret.length() % 2 != 0) {
				ret = "0".concat(ret);
			}
			// System.out.println("s:" + s + " ret:" + ret);
			byte[] b = new byte[ret.length() / 2];
			for (int k = 0; k < ret.length() / 2; k++) {
				// System.out.println("k:"+k);
				b[k] = (byte) (0xff & Integer.parseInt(
						ret.substring(k * 2, k * 2 + 2), 10));
			}
			return b;
		}
		
		// 字符串按原文16进制转换成16进制字节 已去除高位字符0
		public static byte[] stringToHexBytes(String s) {
			for (int w = 0; w < s.length(); w++) {
				if (s.charAt(0) == '0') {
					s = s.substring(1);
				}
				if (s.charAt(0) != '0') {
					break;
				}
			}
			String ret = s.replaceAll("[^0-9a-fA-F]", "");
			if (ret.length() % 2 != 0) {
				ret = "0".concat(ret);
			}
			// System.out.println("s:" + s + " ret:" + ret);
			byte[] b = new byte[ret.length() / 2];
			for (int k = 0; k < ret.length() / 2; k++) {
				// System.out.println("k:"+k);
				b[k] = (byte) (0xff & Integer.parseInt(
						ret.substring(k * 2, k * 2 + 2), 16));
			}
			return b;
		}
		
		// 逆置 减33
		public static byte[] reverseMinus33(byte[] data) {
			byte[] ret = new byte[data.length];
			ret = reverse(data);
			for (int i = 0; i < ret.length; i++) {
				ret[i] -= 0x33;
			}
			return ret;
		};
		
		// 逆置 加三三
		public static byte[] reverseAdd33(byte[] data) {
			byte[] ret = new byte[data.length];
			ret = reverse(data);
			for (int i = 0; i < ret.length; i++) {
				ret[i] += 0x33;
			}
			return ret;
		};
		//int to one byte bcd lowbit
		public static byte intToByteBCD(int i){
			byte ret = 0x00;
			int tmp = i % 10;
			ret |= tmp;
			tmp = (i / 10) % 10;
			ret = (byte) (ret | ((tmp & 0x00ff) << 4));
			return ret;
		}
		
		// int to 2bytes BCD reverse
		public static byte[] intToByte2BCD(int i) {
			byte[] ret = { 0x00, 0x00 };
			int tmp = i % 10;
			ret[0] |= tmp;
			tmp = (i / 10) % 10;
			ret[0] = (byte) (ret[0] | ((tmp & 0x00ff) << 4));
			tmp = (i / 100) % 10;
			ret[1] |= tmp;
			tmp = (i / 1000) % 10;
			ret[1] = (byte) (ret[1] | ((tmp & 0x00ff) << 4));
			return ret;
		}
		
		// int to 3bytes BCD
		public static byte[] intToByte3BCDReverse(int i) {
			byte[] ret = { 0x00, 0x00, 0x00 };
			int tmp;
			int left = i;
			for (int k = 0; k < 6; k++) {
				tmp = left % 10;
				left = left / 10;
				if (k % 2 == 0)
					ret[k / 2] = (byte) (ret[k / 2] | (tmp & 0x0f));
				else
					ret[k / 2] = (byte) (ret[k / 2] | (tmp & 0x0f) << 4);
			}
			return ret;
		}
		
		// BCD 2bytes addr reverse to int
		public static int byte2BCDReverseToInt(byte b_left, byte b_right) {
			int ret = 0;
			ret = ((b_right & 0x000f) * 100) + (((b_right & 0x00f0) >> 4) * 1000)
					+ (((b_left & 0x00f0) >> 4) * 10) + (b_left & 0x000f);
			return ret;
		}

		// BCD 3bytes addr reverse to int
		public static int byte3BCDReverseToInt(byte b_highbyte, byte b_midbyte,
				byte b_lowbyte) {
			int ret = 0;
			ret = ((b_lowbyte & 0x000f) * 10000)
					+ (((b_lowbyte & 0x00f0) >> 4) * 100000)
					+ ((b_midbyte & 0x000f) * 100)
					+ (((b_midbyte & 0x00f0) >> 4) * 1000)
					+ (((b_highbyte & 0x00f0) >> 4) * 10) + (b_highbyte & 0x000f);
			return ret;
		}
		
		// imei intercept last 12 number
		public static byte[] makeImeiToAddr(String str_imei) {
			byte[] ret = new byte[6];
			if (str_imei.length() < 12) {
				return ret;
			}
			str_imei = str_imei.substring(3);
			String tmp_str;
			for (int i = 0; i < 6; i++) {
				tmp_str = str_imei.substring(0 + i * 2, 2 + i * 2);
				// ret[5 - i] = Byte.decode("0x" + tmp_str);//超出126解析出错
				ret[5 - i] = (byte) (Integer.valueOf(tmp_str).byteValue());
			}
			return ret;
		}

		
		public static int byteToBCD(byte b) {
			int ret = 0;
			ret = (b & 0x0f) + (((b & 0xf0) >> 4) * 10);
			return ret;
		}
		
		/**
		 * @函数说明:去掉数组前面的零元素，以第一个非零元素开始，截取剩下的部分
		 * @param:byte[]数组
		 * @return:byte[]数组
		 * */
		public static byte[] cleanZero(byte[] data) {
			if (data == null) {
				return null;
			}
			byte[] tmp;
			int count = data.length;
			for (int i = 0; i < data.length; i++) {
				if (data[i] == 0) {
					count--;
					continue;
				}
				if (data[i] != 0) {
					break;
				}
			}
			tmp = new byte[count];
			for (int i = (data.length - count), k = 0; i < data.length; i++, k++) {
				tmp[k] = data[i];
			}
			for (int i = 0; i < tmp.length; i++) {
				System.out.println("cleanZero-tmp[]:" + tmp[i]);
			}
			return tmp;
		}
		
		public static String bcd2Str(byte[] bytes) {
			char temp[] = new char[bytes.length * 2], val;
			for (int i = 0; i < bytes.length; i++) {
				val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
				temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
				val = (char) (bytes[i] & 0x0f);
				temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			}
			return new String(temp);
		}
		
		/** */
		/**
		 * @函数功能: 10进制串转为BCD码
		 * @输入参数: 10进制串
		 * @输出结果: BCD码
		 */
		public static byte[] str2Bcd(String asc) {
			int len = asc.length();
			int mod = len % 2;
			if (mod != 0) {
				asc = "0" + asc;
				len = asc.length();
			}
			byte abt[] = new byte[len];
			if (len >= 2) {
				len = len / 2;
			}
			byte bbt[] = new byte[len];
			abt = asc.getBytes();
			int j, k;
			for (int p = 0; p < asc.length() / 2; p++) {
				if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
					j = abt[2 * p] - '0';
				} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
					j = abt[2 * p] - 'a' + 0x0a;
				} else {
					j = abt[2 * p] - 'A' + 0x0a;
				}
				if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
					k = abt[2 * p + 1] - '0';
				} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
					k = abt[2 * p + 1] - 'a' + 0x0a;
				} else {
					k = abt[2 * p + 1] - 'A' + 0x0a;
				}
				int a = (j << 4) + k;
				byte b = (byte) a;
				bbt[p] = b;
			}
			return bbt;
		}
		
		/**
		 * 将10进制转换为BCD字节数组（BCD字节数组倒序）
		 * 
		 * @param Dec
		 *            要转换的数
		 * @param length
		 *            转换后BCD字节数组的长度
		 * @return
		 */
		public static byte[] DectoBCD(Double Dec, int length) {
			byte[] Bcd = new byte[length];
			int i;
			int temp;
			for (i = 0; i < length; i++) {
				temp = (int) (Dec % 100);
				Bcd[i] = (byte) (((temp / 10) << 4) + ((temp % 10) & 0x0F));
				Dec /= 100;
			}
			return Bcd;
		}

		//trim ascII 0x0a
		public static String trim_7Fchar(String str){
			if(str == null){
				return "";
			}
			byte[] b = str.getBytes();
			int start = 0,end = b.length;
			for(int w = 0;w < b.length; w++){
				if(((int)(b[w+1]&0xff) == (int)0xa0)&& ((int)(b[w]&0xff) ==(int)0xc2) ){
					System.out.println("+2");
					start+=2;
					w++;
				}else
				if((int)(b[w]&0xff) <= (int)0x20){
					System.out.println("++");
					start++;
				}else{
					break;
				};
			}
			for(int r = b.length - 1; r >= 0 ; r--){
				if(((int)(b[r]&0xff) == (int)0xa0)&& ((int)(b[r-1]&0xff) ==(int)0xc2) ){
					System.out.println("-2");
					end-=2;
					r--;
				}else
				if((int)(b[r]&0xff) <= (int)0x20){
					System.out.println("--");
					end--;
				}else{
					break;
				}
			}
			return new String(b,start,end-start,Charset.forName("UTF-8"));
		}

		

		//byte[0]-byte[5]:YY MM DD hh mm ss
		public static final byte[] getTime(){
			byte[] ret = new byte[6];
			Calendar c = Calendar.getInstance();
			ret[0] = intToByteBCD(c.get(Calendar.YEAR));
			ret[1] = intToByteBCD(c.get(Calendar.MONTH) + 1);
			ret[2] = intToByteBCD(c.get(Calendar.DAY_OF_MONTH));
			ret[3] = intToByteBCD(c.get(Calendar.HOUR_OF_DAY));
			ret[4] = intToByteBCD(c.get(Calendar.MINUTE));
			ret[5] = intToByteBCD(c.get(Calendar.SECOND));
			return ret;
		}
		
		//获取当前日往前倒推7天日期
		public static final String get7DaysTerm(){
			StringBuffer sb = new StringBuffer();
			Calendar c = Calendar.getInstance();
			int Y = c.get(Calendar.YEAR);
			int M = c.get(Calendar.MONTH) + 1;
			int D = c.get(Calendar.DAY_OF_MONTH);
			int h = c.get(Calendar.HOUR_OF_DAY);
			int m = c.get(Calendar.MINUTE);
			int s = c.get(Calendar.SECOND);
			int M1 = M;
			int D1 = D - 7 + 1;
			c.set(Calendar.MONTH, M-2);
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			int maxDate = c.get(Calendar.DATE);
			if(D1 <= 0){
				D1 = maxDate + D1  ;
			}
			sb.append(String.valueOf(D1)+"日"+"-"+
					String.valueOf(D)+"日");
			return sb.toString();
		}
		
		//获取日期星期为4字节
		public static final byte[] getDateWeek(){
			byte[] ret = new byte[4];
			StringBuffer sb = new StringBuffer();
			Calendar c = Calendar.getInstance();
			ret[0] = intToByteBCD(c.get(Calendar.YEAR));
			ret[1] = intToByteBCD(c.get(Calendar.MONTH) + 1);
			ret[2] = intToByteBCD(c.get(Calendar.DAY_OF_MONTH));
			ret[3] = intToByteBCD(c.get(Calendar.DAY_OF_WEEK)-1);
			return ret;
		}
		//解析时间数据帧 hhmmss
		public static final String getTime(byte[] b){
			if(b.length<3){
				b = new byte[]{0x0,0x0,0x0};
			}
			byte[] ret = dec33ReverseReturn(b, 0, 3);
			StringBuilder sb = new StringBuilder();
			for(int w= 0; w<3;w++){
				int m = byteToBCD(ret[w]);
				sb.append(String.valueOf(m));
				if(w!=2){
					sb.append(":");
				}
			}
			return sb.toString();
		}
		//设置字符串解析
		public static final byte[] getSettingTime(String s){
			if(s.length()<5){
				return new byte[]{(byte)0xff,(byte)0xff,(byte)0xff};
			}
			String[] str = s.split(":");
			byte[] ret = new byte[3];
			if(str.length<3){
				return new byte[]{(byte)0xff,(byte)0xff,(byte)0xff};
			}else{
				try{
					ret[0] = intToByteBCD(Integer.valueOf(str[0]));
					ret[1] = intToByteBCD(Integer.valueOf(str[1]));
					ret[2] = intToByteBCD(Integer.valueOf(str[2]));
				}catch (NumberFormatException e) {
					return new byte[]{(byte)0xff,(byte)0xff,(byte)0xff};
				}
			}
			return ret;
		}
		/**
		 * 检查电表地址格式
		 * */
		public static final boolean checkMeterAddr(String str) {
			String format = "\\d{12}";
			if (str.matches(format)) {
				return true;
			}
			return false;
		}

		/**
		 * 5 bytes to 10bit-string,high bit set zero
		 * 
		 * */
		public static String bytes5To10BitsStr(byte[] bytes) {
			StringBuilder sb = new StringBuilder();
			if (bytes.length != 5) {
				return "0000000000";
			}
			for (byte b : bytes) {
				sb.append(String.valueOf((b & 0xff)));
			}
			while (sb.length() < 10) {
				sb.insert(0, "0");
			}
			return sb.toString();
		}

		/**
		 * 9 bytes to 17bit-string,high bit set zero
		 * 
		 * */
		public static String bytes9To17BitsStr(byte[] bytes) {
			StringBuilder sb = new StringBuilder();

			if (bytes.length != 9) {
				return "000000000000000000";
			}
			for (byte b : bytes) {
				sb.append(String.valueOf((b & 0xff)));
			}
			while (sb.length() < 17) {
				sb.insert(0, "0");
			}
			return sb.toString();
		}

		public static String getTaiQuNo(int no) {
			if (no == 1) {
				return "一号台区";
			} else if (no == 2) {
				return "二号台区";
			} else if (no == 3) {
				return "三号台区";
			} else if (no == 4) {
				return "四号台区";
			} else if (no == 5) {
				return "五号台区";
			} else if (no == 6) {
				return "六号台区";
			} else if (no == 7) {
				return "七号台区";
			} else if (no == 8) {
				return "八号台区";
			} else {
				return "一号台区";
			}
		}


		// string 转换成bcd码 逆置 转换成6字节地址
		public static byte[] stringTo6bytesBCDReverseAddr(String s) {
			byte[] ret = new byte[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
			if (s == null) {
				return ret;
			}
			while (s.length() < 12) {
				s = "0" + s;
			}
			if (s.length() > 12) {
				return ret;
			}
			String tmp_str;
			for (int i = 0; i < 6; i++) {
				tmp_str = s.substring(0 + i * 2, 2 + i * 2);
				ret[5 - i] = (byte)(Integer.valueOf(tmp_str).byteValue());
			}
			return ret;
		}

		// string 转换成int 逆置 转换成6字节地址
		public static byte[] stringTo6bytesReverseAddr(String s) {
			byte[] ret = new byte[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
			if (s == null) {
				return ret;
			}
			int _i = Integer.valueOf(s);
			ret[0] = (byte) (_i & 0x000000ff);
			ret[1] = (byte) ((_i & 0x0000ff00) >> 8);
			ret[2] = (byte) ((_i & 0x00ff0000) >> 16);
			ret[3] = (byte) ((_i & 0xff000000) >> 24);
			return ret;
		}

		// int to 2bytes reverse
		public static byte[] intTo2ByteReverse(int i) {
			byte[] ret = new byte[2];
			ret[0] = (byte) (i & 0x00ff);
			ret[1] = (byte) ((i & 0xff00) >> 8);
			return ret;
		}

		// make read status data frame
		public static byte[] makeReadStatusData(int signalVal, int thresholdVal,
				int pulseTime, int mode) {
			byte[] ret = new byte[10];
			ret[0] = (byte) ((signalVal & 0xff00) >> 8);
			ret[1] = (byte) (signalVal & 0x00ff);
			ret[2] = (byte) ((thresholdVal & 0xff00) >> 8);
			ret[3] = (byte) (thresholdVal & 0x00ff);
			ret[4] = (byte) ((signalVal & 0xff00) >> 8);
			ret[5] = (byte) (signalVal & 0x00ff);
			ret[6] = (byte) ((thresholdVal & 0xff00) >> 8);
			ret[7] = (byte) (thresholdVal & 0x00ff);
			ret[8] = (byte) (pulseTime & 0x00ff);
			ret[9] = (byte) (mode & 0x00ff);
			return ret;
		}

		// make setparam data frame
		public static byte[] makeSetParamData(int signalVal, int thresholdVal,
				int pulseTime) {
			byte[] ret = new byte[9];
			ret[0] = (byte) ((signalVal & 0xff00) >> 8);
			ret[1] = (byte) (signalVal & 0x00ff);
			ret[2] = (byte) ((thresholdVal & 0xff00) >> 8);
			ret[3] = (byte) (thresholdVal & 0x00ff);
			ret[4] = (byte) ((signalVal & 0xff00) >> 8);
			ret[5] = (byte) (signalVal & 0x00ff);
			ret[6] = (byte) ((thresholdVal & 0xff00) >> 8);
			ret[7] = (byte) (thresholdVal & 0x00ff);
			ret[8] = (byte) (pulseTime & 0x00ff);
			return ret;
		}

		// 2bytes reverse to int
		public static int byte2ReverseToInt(byte[] b) {
			if (b.length < 2) {
				return 0;
			} else {
				int ret = 0;
				ret = ((b[1] & 0x00ff) << 8) + (b[0] & 0x00ff);
				return ret;
			}
		}

		// 2bytes reverse to int
		public static int byte2ReverseToInt(byte b_left, byte b_right) {
			int ret = 0;
			ret = ((b_right & 0x00ff) << 8) + (b_left & 0x00ff);
			return ret;
		}
		
		public static String byte4ToAccMeterVersion(byte t1,byte t2,byte t3,byte t4){
			String ret_version = "";
			ret_version = "版本号:"+byteToAccMeterVersionStr(t1)+ " "
					+ byteToAccMeterVersionStr(t2)+ " "
					+ byteToAccMeterVersionStr(t3)+ " "
					+ byteToAccMeterVersionStr(t4);
			return ret_version;
		}
		
		public static String byteToAccMeterVersionStr(byte b){
			String ret = "";
//			ret = Byte.valueOf((byte)(b>>4)).toString() + Byte.valueOf((byte)(b&0x0f)).toString();
			ret = Byte.valueOf((byte)((b>>4)&0x0f)).toString()+"."
					+Byte.valueOf((byte)(b&0x0f)).toString();
			return ret;
		}
		
		//地址截取最后10位
		public static String getCheckedAddr(String addr){
			if(addr == null){
				return "";
			}else if(addr.length()>10){
				return addr.substring(addr.length() - 10, addr.length());
			}else {
				return addr;
			}
		}

		/**
		 * 计算cs
		 * 
		 * @param data
		 * @param offset
		 * @param len
		 * @return 4个字节
		 */
		public static int Checksum(byte data[], int offset, int len) {
			int cs = 0;
			if (len + offset > data.length) {
				len = data.length - offset;
			}
			for (int i = offset; i < offset + len; i++) {
				cs += data[i];
			}
			return cs;
		}

		/**
		 * int到byte[]
		 * 
		 * @param i
		 * @return
		 */
		public static byte[] intToByteArray(int i) {
			byte[] result = new byte[4];
			// 由高位到低位
			result[0] = (byte) ((i >> 24) & 0xFF);
			result[1] = (byte) ((i >> 16) & 0xFF);
			result[2] = (byte) ((i >> 8) & 0xFF);
			result[3] = (byte) (i & 0xFF);
			return result;
		}

		public static byte[] intToHEX2Bytes(int i) {
			byte[] ret = new byte[2];
			ret[0] = (byte) ((i >> 8) & 0xFF);
			ret[1] = (byte) ((i) & 0xFF);
			return ret;
		}

		/**
		 * byte[]转int
		 * 
		 * @param bytes
		 * @return
		 */
		public static int byteArrayToInt(byte[] bytes) {
			int value = 0;
			// 由高位到低位
			for (int i = 0; i < 4; i++) {
				int shift = (4 - 1 - i) * 8;
				value += (bytes[i] & 0x000000FF) << shift;// 往高位游
			}
			return value;
		}

		public static byte[] dec33toBytes(byte data[], int offset, int length) {
			byte[] ret = new byte[data.length];
			for (int i = offset; i < offset + length; i++) {
				ret[i] = (byte) (data[i] - (byte) 0x33);
			}
			return ret;
		}

		public static String byteToSignalData(byte b) {
			String ret = "";
			System.out.println("b>>7:" + (b >> 7));
			if ((b >> 7 & 0x01) == 0x01) {
				ret = "-";
			}
			ret = ret + String.valueOf((int) (b & 0x7F));
			return ret;
		}

		public static byte[] makeDevidToAddr(int devid) {
			byte[] ret = new byte[6];
			ret[0] = (byte) (devid & 0x00ff);
			ret[1] = (byte) ((devid & 0xff00) >> 8);
			for (int i = 0; i < 4; i++) {
				ret[i + 2] = 0x00;
			}
			return ret;
		}


		// 6bytes 时间 秒分时 日月年
		public static String readTimeOfFrame(byte[] b_time) {
			if (b_time == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			for (int w = 0; w < 6; w++) {
				sb.append(byteToBCD(b_time[5 - w]));
				if (w == 2) {
					sb.append(" ");
				} else if (w < 2) {
					sb.append("-");
				} else if (w < 5) {
					sb.append(":");
				}
			}
			return sb.toString();
		}

		// 4bytes 时间 秒分时日
		public static String readTimeof4bytes(byte[] b_time) {
			if (b_time == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			for (int w = 0; w < 4; w++) {
				sb.append(byteToBCD(b_time[3 - w]));
				if (w == 1) {
					sb.append(":");
				} else if (w < 1) {
					sb.append("-");
				} else if (w < 3) {
					sb.append(":");
				}
			}
			return sb.toString();
		}

		public static String getUartFromByte(byte b) {
			if (b == 0x00) {
				return "1200";
			} else if (b == 0x01) {
				return "2400";
			} else if (b == 0x02) {
				return "4800";
			} else if (b == 0x03) {
				return "9600";
			} else {
				return "9600";
			}
		}

		public static String getWirelessRate(byte v) {
			if (v == 0x01) {
				return "1.2";
			} else if (v == 0x02) {
				return "10";
			} else {
				return "10";
			}
		}

		public static byte[] getWirelessRate(String s){
			String s_tmp = "";
			if(s.equals("1.2")){
				s_tmp = "1";
			}else{
				s_tmp = "2";
			}
			return Util.stringToHexBytes(s_tmp);
		}
		
		public static byte[] getUartTimedOutTime(String s){
			if(s.length()>0)
			s = s.substring(0,s.length()-1);
			byte[] b = stringToDecBytes(s);
			byte[] ret = new byte[2];
			if(b.length == 1){
				ret[0] = b[0];
			}else if(b.length == 2){
				ret = b;
			}else{
				System.arraycopy(b, 0, ret, 0, ret.length);
			}
			return ret;
		}
		
		public static byte[] getWirelessChannelValueTh(String s){
			s.replaceAll("[^-0123456789]", "");
			int w = Integer.valueOf(s);
			byte b;
			if( w > 127 ){
				b = 127;
			}else if( w < -128 ){
				b = -128;
			}else{
				b = Byte.valueOf(s);
			}
			return new byte[]{b};
		}
		
		/**
		 * 将String类型数据转换成byte[]数组
		 * 
		 * */
		public static byte[] strToBytes(String str){
			if(str==null||str.length()==0){
			return null;
			}
			if(str.length()%2==0){
				byte[] b =  str.getBytes();
				return b;
			}
			return null;
		}
		/**
		 * 将双字节转换成int
		 * */
		public static int byte2ToInt(byte low,byte high){
			return ((low&0xff)+((high&0xff)<<8));
		}
		
		/**
		 * 将int转换成2字节
		 * 
		 * */
		public static byte[] intToByte2(int val){
			byte[] b = new byte[2];
			if(val>32767||val<-32768){
				b[0]=0;
				b[1]=0;
			}else{
				b[0] = (byte)(val>>8 & 0xff);
				b[1] = (byte)(val & 0xff);
			}
			return b;
		};
		
		/**
		 * 
		 * @note double 位置 转换成  分 秒
		 * 
		 * */
		public static String dblToLocation(double data){
			String ret_s = "";
			int tmp_i_du = (int)data;
			ret_s = String.valueOf(tmp_i_du)+"°";
			//度小数部分
			double tmp_d_du = data-tmp_i_du;
			int tmp_i_fen = (int)(tmp_d_du*60);
			ret_s = ret_s.concat(String.valueOf(tmp_i_fen)+"′");
			double tmp_d_fen = tmp_d_du*60 - tmp_i_fen;
			int tmp_i_miao = (int)(tmp_d_fen*60);
			ret_s = ret_s.concat(String.valueOf(tmp_i_miao)+"″");
			return ret_s;
		}
		
	/**
     * @note int到byte[] 2字节Bytes 超出范围则返回两字节0
     * @param i
     * @return
     */
	public static byte[] intTo2BytesArray(int i) {
		if(i>65535||i<0){
			return new byte[]{0,0};
		}
		byte[] result = new byte[2];  
		//由高位到低位
		result[0] = (byte)((i >> 8) & 0xFF);
		result[1] = (byte)(i & 0xFF);
		return result;
	}
	/**
     * int到byte[] dec. 2字节Bytes
     * @param s
     * @return
     */
	public static byte[] StringTo2BytesArray(String s) {
		Integer _i = Integer.valueOf(s);
		byte[] result = intTo2BytesArray(_i);
		return result;
	}
	/**
     * hexString到byte[]  2字节Bytes
     * @param hexString
     * @return
     */
	public static byte[] HexStringTo2BytesArr(String hexString){
		if(hexString==null){
			hexString="0";
		}
		byte[] ret = new byte[2];
		ret = Util.intTo2BytesArray(Integer.parseInt(hexString, 16));
		return ret;
	};
	/**
     * hexString到byte[]  2字节Bytes 反转
     * @param hexString
     * @return
     */
	public static byte[] HexStringTo2BytesReverse(String hexString){
		if(hexString==null){
			hexString="0";
		}
		byte[] ret = new byte[2];
		ret = Util.intTo2BytesArray(Integer.parseInt(hexString, 16));
		ret = Util.reverse(ret);
		return ret;
	};
	
	/**
	 * byte[]----转int
	 * @param bytes----计算谐波采样数据使用
	 * @return
	 */
	public static int byteArrayToInt_(byte[] bytes,int offset) {
		int value= 0;
		//由高位到低位
		for (int i = offset; i < offset+4; i++) {
			int shift= (4 - 1 - i) * 8;
			value +=(bytes[i] & 0xFF) << shift;//????位游
		}
		return value;
	}
	/**
	 * byte[]----2字节转int
	 * @param bytes
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes, int offset) {
		int value= 0;
		
		//由高位到低位
		for (int i = offset ; i <offset +2; i++) {
			int shift= (offset +2 - 1 - i) * 8;
			value +=(bytes[i] & 0x000000FF) << shift;//�?��位游
		}
		return value;
	}
	/**
	 * byte[]----4字节转int
	 * @param bytes
	 * @return
	 */
	public static long byte4ArrayToInt(byte[] bytes, int offset) {
		long value= 0;
		
		//由高位到低位
		for (int i = offset ; i <offset +4; i++) {
			int shift= (offset +4 - 1 - i) * 8;	
			value =value +(((long)bytes[i] & 0xFF) << shift);
		}
		return value;
	}
	/**
	 * byte[]----4字节转long,最高位为符号位
	 * @param bytes
	 * @return
	 */
	public static long ubyte4ArrayToInt(byte[] bytes, int offset) {
		long value= 0;		
		value=(int) (byte4ArrayToInt(bytes,offset)&0x7FFFFFFF);	
		if((byte4ArrayToInt(bytes,offset)>>31&1)==1){
			value=-(~(value-1)&0x7FFFFFFF);
		}
	
		return value;
	}

		/**
		 * CRC16校验
		 * 
		 * @return
		 */
		public static byte[] GetModBusCRC(String data) {
			byte[] bb = new byte[2];
			long functionReturnValue = 0;
			long i = 0;

			long J = 0;
			int[] v = null;
			byte[] d = null;
			// 之前之所以错误,是因为有的数字被认为是负数了.
			v = strToToHexByte(data);

			long CRC = 0;
			CRC = 0xffffL;
			for (i = 0; i <= (v).length - 1; i++) { // 2.把第一个8位二进制数据（既通讯信息帧的第一个字节）与16位的CRC寄存器的低8位相异或，把结果放于CRC寄存器；
				CRC = (CRC / 256) * 256L + (CRC % 256L) ^ v[(int) i];
				for (J = 0; J <= 7; J++) { // 3.把CRC寄存器的内容右移一位（朝低位）用0填补最高位，并检查最低位；
											// 4.如果最低位为0：重复第3步（再次右移一位）；
											// 如果最低位为1：CRC寄存器与多项式A001（1010 0000 0000
											// 0001）进行异或；
											// 5.重复步骤3和4，直到右移8次，这样整个8位数据全部进行了处理；
					long d0 = 0;
					d0 = CRC & 1L;
					CRC = CRC / 2;
					if (d0 == 1)
						CRC = CRC ^ 0xa001L;
				} // 6.重复步骤2到步骤5，进行通讯信息帧下一字节的处理；
			} // 7.最后得到的CRC寄存器内容即为：CRC码。
			CRC = CRC % 65536;
			int h1, l0;
			l0 = (int) CRC / 256;
			h1 = (int) CRC % 256;
			bb[0] = (byte) l0;
			bb[1] = (byte) h1;
			return bb;
		}

		private static int[] strToToHexByte(String hexString) {
			hexString = hexString.replace(" ", "");
			// 如果长度不是偶数，那么后面添加空格。

			if ((hexString.length() % 2) != 0) {
				hexString += " ";
			}

			// 定义数组，长度为待转换字符串长度的一半。
			int[] returnBytes = new int[hexString.length() / 2];

			for (int i = 0; i < returnBytes.length; i++)
				// 这里为什么会出现负数呢?
				returnBytes[i] = (0xff & Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
			return returnBytes;
		}
		
		private static byte[] intTo2ByteArray(int num) {
			byte[] result = new byte[2];
			result[1] = (byte) (num & 0xff);
			result[0] = (byte) (((num & 0xff00) >> 8) & 0xff);
			return result;
		}
		
		/**
		 * @detail 将1字节 BCD码转换成10进制int数值
		 * @param data byte BCDcode 8421code
		 * @result int
		 * 
		 * */
		public static int byteBCDtoInt(byte data){
			int ret = 0;
			ret = ((data & 0xf0) >>4) *10 + (data&0x0f); 
			return ret;
		}
		/**
		 * @note 将byte[]数组bcd码转成String类型
		 * */
		public static String byteBCDtoString(byte[] data){
			BigInteger ret = new BigInteger("0");
			long tmp = 0;
			long _i;
			for(int i = 0; i<data.length;i++){
				_i = byteBCDtoInt(data[i]);
				tmp = _i*((long)Math.pow(Double.valueOf("100"), data.length-i-1));
				ret = ret.add(BigInteger.valueOf(tmp));  
			}
			return ret.toString();
		}
		/**
		 * @note 将byte[]数组bcd码转成Integer类型
		 * */
		public static Integer byteBCDtoInt(byte[] data){
			Integer ret = 0;
			for(int i = 0; i<data.length;i++){
				ret += byteBCDtoInt(data[i])*((int)Math.pow(Double.valueOf("100"), data.length-i-1));
			}
			return ret;
		}
		
		/**
		 * 根据协议将byte转成字符串  存储在数据库里
		 * 
		 * */
		public static String byteToPort(byte b){
			String port = "";
			if(b==0x01){
				port = "485I";
			}else if(b==0x02){
				port = "485II";
			}
			return port;
		}
		/**
		 * @note 根据协议将byte转成字符串  存储在数据库里
		 * @note paramrun表
		 * */
		public static String intToPort(int i){
			String port = "";
			if(i==0x01){
				port = "1";
			}else if(i==0x02){
				port = "2";
			}else if(i==0x03){
				port = "3";
			}else if(i==0x04){
				port = "4";
			}
			return port;
		}
		/**
		 * 将地址域addr的addr[2]转换成仪表类型字符串
		 * 
		 * */
		public static String byteToMeterType(byte b){
			if(b == 0x00){
				return "通讯管理机";
			}else if(b == 0x01){
//				return "DLT645-1997";
				return "D645_97";
			}else if(b == 0x02){
				return "D645_07";
//				return "DLT645-2007";
			}else if(b == 0x03){
//				return "CK-S800";
				return "CKS800";
			}else if(b == 0x04){
				return "ST500L";
			}else if(b == 0x05){
				return "ST503L";
			}else if(b == 0x06){
				return "CK300";
			}else if(b == 0x07){
				return "P9S4H";
			}else if(b == 0x08){
				return "P9S4A";
			}else if(b == 0x09){
				return "PD264Z";
			}else if(b == 0x0A){
				return "IDIN200F";
			}else if(b == 0x0B){
				return "ACR";
			}else {
			return "";
			}
		}
		/**
		 * @note 将仪表类型字符串转换成byte类型
		 * 
		 * */
		public static byte meterTypeToByte(String s){
			if(s==null){
				return 0x00;
			}
			if(s.equals("通讯管理机")){
				return 0x00;
			}else if(s.equals("DLT645-1997")||s.equals("D645_97")){
				return 0x01;
			}else if(s.equals("DLT645-2007")||s.equals("D645_07")){
				return 0x02;
			}else if(s.equals("CK-S800")||s.equals("CKS800")){
				return 0x03;
			}else if(s.equals("ST500L")){
				return 0x04;
			}else if(s.equals("ST503L")){
				return 0x05;
			}else if(s.equals("CK300")){
				return 0x06;
			}else if(s.equals("P9S4H")){
				return 0x07;
			}else if(s.equals("P9S4A")){
				return 0x08;
			}else if(s.equals("PD264Z")){
				return 0x09;
			}else if(s.equals("IDIN200F")){
				return 0x0A;
			}else if(s.equals("ACR")){
				return 0x0B;
			}else {
			return 0x00;
			}
		}
		
		public static byte commuCtrlWordToByte(int commuCtrlWord){
			return (byte)commuCtrlWord;
		}
		
		public static byte commuCtrlWordToByte(String baud,int stopbit,int checkbit,int oddcheck,int bitnum){
			byte ret = (byte)0xff;
			if(baud==null){
				return 0x0;
			}
			//设置波特率
			if(baud.equals("300")){
				ret = (byte)(ret&(0x00<<5));
			}else if(baud.equals("600")){
				ret = (byte)(ret&(0x01<<5));
			}else if(baud.equals("1200")){
				ret = (byte)(ret&(0x02<<5));
			}else if(baud.equals("2400")){
				ret = (byte)(ret&(0x03<<5));
			}else if(baud.equals("4800")){
				ret = (byte)(ret&(0x04<<5));
			}else if(baud.equals("7200")){
				ret = (byte)(ret&(0x05<<5));
			}else if(baud.equals("9600")){
				ret = (byte)(ret&(0x06<<5));
			}else if(baud.equals("19200")){
				ret = (byte)(ret&(0x07<<5));
			}
			//停止位
			if(stopbit == 1){
				ret = (byte)(ret&0xe0);
			}else if(stopbit == 2){
				ret = (byte)(ret&0xf0);
			}
			//校验位
			if(checkbit==0){
				ret = (byte)(ret&0xf0);
			}else if(checkbit==1){
				ret = (byte)(ret&0xf8);
			}
			//奇偶校验
			if(oddcheck==0){
				ret = (byte)(ret&0xf8);
			}else if(oddcheck==1){
				ret = (byte)(ret&0xfc);
			}
			//数字位
			if(bitnum==5){
				ret = (byte)(ret&0xfc);
			}else if(bitnum==6){
				ret = (byte)(ret&0xfd);
			}else if(bitnum==7){
				ret = (byte)(ret&0xfe);
			}else if(bitnum==8){
				ret = (byte)(ret&0xff);
			}
			
			return ret;
		}
		
		/**
		 * @note 将仪表类型转换成下拉框位置
		 * */
		public static int metertypeToPosition(String s){
			if(s.equals("P9S4H")){
				return 0;
			}else if(s.equals("P9S4A")){
				return 1;
			}else if(s.equals("PD264Z")){
				return 2;
			}else if(s.equals("IDIN200F")){
				return 3;
			}else if(s.equals("ACR")){
				return 4;
			}else if(s.equals("D645_97")){
				return 5;
			}else if(s.equals("D645_07")){
				return 6;
			}else{
				return 0;
			}
		}
		
		/**
		 * @note 时间转换 String转换成long类型时间
		 * 格式为年-月-日,小时：分钟：秒,星期几(1-7代表周一到周日)
		 * @example 14-10-31,18:01:22,1 
		 **/
		public static long timeStringToLong(String date){
			String y = date.substring(0, 2);//截取两个字符串
			String year = "20".concat(y);
			String month = date.substring(3, 5);
			String day = date.substring(6, 8);
			String hour = date.substring(9, 11);
			String min = date.substring(12, 14);
			String sec = date.substring(15, 17);
			String week = date.substring(18, 19);
			Calendar c = Calendar.getInstance();
			c.set(Integer.valueOf(year), Integer.valueOf(month), 
					Integer.valueOf(day), Integer.valueOf(hour), 
					Integer.valueOf(min), Integer.valueOf(sec));
			return c.getTimeInMillis();
		}
		
		/**
		 * @return 0-6代表 星期天  星期一  星期二  星期三  星期四  星期五  星期六
		 * */
		public static int whatDayOfWeek(){
			Calendar c = Calendar.getInstance();
			int day = c.get(c.DAY_OF_WEEK) - 1;
			return day;
		}
		
		/**
		 * @note 16进制string转化int
		 * 
		 * */
		public static int hexStringToInt(String hexString){
			byte[] b = fromHexNoSpace(hexString);
			int ret_int = byteArrayToInt(b,0);
			return ret_int;
		}
		/*
		 * @note 16进制4字节转化成int
		 * */
		public static long hexString4byteToInt(String hexString){
			byte[] b = fromHexNoSpace(hexString);
			long ret_long = byte4ArrayToInt(b,0);
			return ret_long;
		}
		/**
		 * @note 转换IDIN200F电量数据  (-1)^s*2^(e-127)*(1+0.M)  奇葩的公式
		 * */
		public static double hexString4byteToIDIN200F(String hexString){
			double ret = hexsToIEEE754Double(hexString);
			return ret;
		}
		/**
		 * @note 转换IDIN200F电度数据  4BytesBCD转换成长整型
		 * */
		public static double hexs4BToINID200FEnergy(String hexString){
			byte[] b = fromHexNoSpace(hexString);
			double ret = byteBCDtoInt(b)*0.1;
			return ret;
		}
		
		/**
		 * @note 转换PD264Z电量数据  4Bytes
		 * */
		public static double hexs4BToPD264Z(String hexString){
			double ret = hexsToIEEE754Double(hexString);
			return ret;
		}
		/**
		 * @note 转换PD264Z电量数据  4Bytes
		 * */
		public static double hexs4BToPD264ZEnergy(String hexString){
			return 0;
		}
		
		/**
		 * @note 将四字节16进制转换成IEEE754浮点数
		 * */
		public static double hexsToIEEE754Double(String hexString){
			Long bits  = Long.valueOf(hexString,16);
			long s = ((bits >> 31) == 0) ? 1 : -1;
			long e = ((bits >> 23) & 0xff);
			long m = (e == 0) ?
			                 (bits & 0x7fffff) << 1 :
			                 (bits & 0x7fffff) | 0x800000;
			double ret = s*m*Math.pow(2, e-150);
			return ret;
		}
		
		public static Integer decStringToInt(String decString){
			if(decString.toLowerCase().contains("a")|decString.toUpperCase().contains("b")|decString.toUpperCase().contains("c")|decString.toUpperCase().contains("d")|decString.toUpperCase().contains("e")|decString.toUpperCase().contains("f")){
				return 999999999;
			}
			Integer ret = Integer.parseInt(decString);
			return ret;
		}
		/*
		 * BigInteger 转成bcd数组并且逆置 
		 * 通信地址转换
		 * */
		public static byte[] biToBCDArr(BigInteger bi){
			if(bi.compareTo(new BigInteger("999999999999"))>0){
				//返回999999999999
				return new byte[]{0x63,0x63,0x63,0x63,0x63,0x63};
			}
			byte[] ret = new byte[]{0,0,0,0,0,0};
			int tmp,i=0;
			BigInteger _bi_100 = new BigInteger("100");
			while(bi.divide(_bi_100).compareTo(BigInteger.valueOf(0l))>0){
				tmp = bi.mod(_bi_100).intValue();
				bi=bi.divide(_bi_100);
				ret[i] = DectoBCD((double)tmp,1)[0];
				i++;
			}
			ret[i] = DectoBCD(bi.mod(_bi_100).doubleValue(),1)[0];
			return ret;
		}
		
		/**
		 * 将byte[]数组转化成DLT645_07的标识,字母大写表示
		 * */
		public static String byteArrTo07ID(byte[] _b){
			if(_b.length!=4){
				return "00000000";
			}
			String ret ="";
			String tmp ="";
			for(int i= 0;i<_b.length;i++){
				tmp = Integer.toHexString(_b[i]&0xff).toUpperCase();
				if(tmp.length()<2){
					tmp="0".concat(tmp);
				}
				ret = ret.concat(tmp);
			}
			return ret;
		}
		
		/**
		 * 打印时间
		 * */
		public static final void printTime(String tag){
			if(System.getenv("OS").toLowerCase().contains("windows")){
				System.out.println(tag+":"+getStrTime());
			}else{
//			Log.e("Test", tag+":"+getStrTime());
			}
		}
		/**
		 * 获取格式化时间字符串
		 * */
		public static final String getStrTime(){
			Date nowTime = new Date(); 
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:ms"); 
			return time.format(nowTime); 
		}
}
