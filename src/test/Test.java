package test;

import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.Writer;
import java.lang.Thread.State;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat.Encoding;

import com.compute.thread.Thread_dataService;
import com.model.impl.AlarmDoor;

import pattern.mediator.Mediator;
import pattern.mediator.MyMediator;
import util.Util;

public class Test {
	static ReentrantLock lock = new ReentrantLock();
	static Integer count = 0;
	static void doSth(String who){
		if(count == 50000){
			Thread.currentThread().interrupt();
//			return;
		}
		Util.printTime(who+" doSth start");
//		for(int w = 0;w < 1000000;w++){
//			
//		}
		count++;
		Util.printTime(who+" doSth end 次数："+count);
	}
	static class Thread_1 extends Thread{
		Boolean b;
		static Thread_1 instance = new Thread_1();
		static Thread_1 getInstance(){
			return instance;
		}
		@Override
		public void run() {
			super.run();
			while(true){
				while( !(b =lock.tryLock())){
					continue;
				}
			System.out.println("++=1=++"+b);
			doSth("++1++");
			lock.unlock();
			Util.printTime("++=1=++lock.unlock()");
			synchronized (lock) {
				try {
					lock.notifyAll();
					System.out.println("++=1=++wait");
					if(!Thread.currentThread().isInterrupted())
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}
		}
	}
	static class Thread_2 extends Thread{
		Boolean b;
		static Thread_2 instance = new Thread_2();
		static Thread_2 getInstance(){
			return instance;
		}
		@Override
		public void run() {
			super.run();
			while(true){
				while( !(b =lock.tryLock())){
					continue;
				}
				System.out.println("++=2=++"+b);
				doSth("++2++");
				lock.unlock();
				Util.printTime("++=2=++lock.unlock()");
				synchronized (lock) {
				try {
					lock.notifyAll();
					System.out.println("++=2=++wait");
					if(!Thread.currentThread().isInterrupted())
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}
		}
	}
	static class Thread_3 extends Thread{
		Boolean b;
		static Thread_3 instance = new Thread_3();
		static Thread_3 getInstance(){
			return instance;
		}
		@Override
		public void run() {
			super.run();
			while(true){
				while( !(b =lock.tryLock())){
					continue;
				}
				System.out.println("++=3=++"+b);
				doSth("++3++");
				lock.unlock();
				Util.printTime("++=3=++lock.unlock()");
				synchronized (lock) {
				try {
					lock.notifyAll();
					System.out.println("++=3=++wait");
					if(!Thread.currentThread().isInterrupted())
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}
		}
	}
	public static void main(String args[]) throws InterruptedException {
		Thread_1 t1 = new Thread_1();
		Thread_2 t2 = new Thread_2();
		Thread_3 t3 = new Thread_3();
		t1.start();
		t2.start();
		t3.start();
	}
	
	/**
	 * 打印系统环境变量
	 * */
	public static final void printSystemEnv(){
		Map<String,String> m = System.getenv();
		Iterator it = m.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			String value = (String) m.get(key);
			System.out.println(key+":"+value);
		}
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

	private static final String _DBNAME = "lvlvollineanalyzer.db";
	private static final int _DBVERSION = 1;
	private static final String TABLE_NAME_METER = "meter";
	private static final String ID_COL = "id";
	private static final String METERCODE_COL = "metercode";
	private static final String FROMANALYZERL2_COL = "fromanalyerL2";
	private static final String FROMANALYZERL1_COL = "fromanalyerL1";
	private static final String FCHECKMETER_COL = "fcheckmeter";
	private static final String FOACTPE_COL = "foactpe";// 当前正向有功电能
	private static final String REACTPE_COL = "reactpe";// 当前反向有功电能
	private static final String DAY1FOACTPE_COL = "day1foactpe";
	private static final String DAY2FOACTPE_COL = "day2foactpe";
	private static final String DAY3FOACTPE_COL = "day3foactpe";
	private static final String DAY4FOACTPE_COL = "day4foactpe";
	private static final String DAY5FOACTPE_COL = "day5foactpe";
	private static final String DAY6FOACTPE_COL = "day6foactpe";
	private static final String DAY7FOACTPE_COL = "day7foactpe";
	private static final String HOUR1FOACTPE_COL = "hour1foactpe";
	private static final String HOUR2FOACTPE_COL = "hour2foactpe";
	private static final String HOUR3FOACTPE_COL = "hour3foactpe";
	private static final String HOUR4FOACTPE_COL = "hour4foactpe";
	private static final String HOUR5FOACTPE_COL = "hour5foactpe";
	private static final String HOUR6FOACTPE_COL = "hour6foactpe";
	private static final String HOUR7FOACTPE_COL = "hour7foactpe";
	private static final String HOUR8FOACTPE_COL = "hour8foactpe";
	private static final String HOUR9FOACTPE_COL = "hour9foactpe";
	private static final String HOUR10FOACTPE_COL = "hour10foactpe";
	private static final String HOUR11FOACTPE_COL = "hour11foactpe";
	private static final String HOUR12FOACTPE_COL = "hour12foactpe";
	private static final String HOUR13FOACTPE_COL = "hour13foactpe";
	private static final String HOUR14FOACTPE_COL = "hour14foactpe";
	private static final String HOUR15FOACTPE_COL = "hour15foactpe";
	private static final String HOUR16FOACTPE_COL = "hour16foactpe";
	private static final String HOUR17FOACTPE_COL = "hour17foactpe";
	private static final String HOUR18FOACTPE_COL = "hour18foactpe";
	private static final String HOUR19FOACTPE_COL = "hour19foactpe";
	private static final String HOUR20FOACTPE_COL = "hour20foactpe";
	private static final String HOUR21FOACTPE_COL = "hour21foactpe";
	private static final String HOUR22FOACTPE_COL = "hour22foactpe";
	private static final String HOUR23FOACTPE_COL = "hour23foactpe";
	private static final String HOUR24FOACTPE_COL = "hour24foactpe";

	public static final byte[] getSettingTime(String s) {
		if (s.length() < 8) {
			return new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
		}
		String[] str = s.split("-");
		byte[] ret = new byte[3];
		if (str.length < 3) {
			return new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
		} else {
			try {
				ret[0] = intToByteBCD(Integer.valueOf(str[0]));
				ret[1] = intToByteBCD(Integer.valueOf(str[1]));
				ret[2] = intToByteBCD(Integer.valueOf(str[2]));
			} catch (NumberFormatException e) {
				return new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
			}
		}
		return ret;
	}

	// 解析时间数据帧 hhmmss
	public static final String getTime(byte[] b) {
		if (b.length < 3) {
			b = new byte[] { 0x0, 0x0, 0x0 };
		}
		byte[] ret = dec33ReverseReturn(b, 0, 3);
		StringBuilder sb = new StringBuilder();
		for (int w = 0; w < 3; w++) {
			int m = byteToBCD(ret[w]);
			sb.append(String.valueOf(m));
			if (w != 2) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public static final byte[] getDateWeek() {
		byte[] ret = new byte[4];
		StringBuffer sb = new StringBuffer();
		Calendar c = Calendar.getInstance();
		ret[0] = intToByteBCD(c.get(Calendar.YEAR));
		ret[1] = intToByteBCD(c.get(Calendar.MONTH) + 1);
		ret[2] = intToByteBCD(c.get(Calendar.DAY_OF_MONTH));
		ret[3] = intToByteBCD(c.get(Calendar.DAY_OF_WEEK) - 1);
		return ret;
	}



	public static class Thread_getLog extends Thread {
		@Override
		public void run() {
			String SDCARD_PATH = "E:\\";
			// String exec_getlog =
			// "adb logcat > "+SDCARD_PATH+"\\LOWVOLLog.txt -v time -s *:e";
			String exec_getlog = "adb logcat > e:/zzzz_lvlapp.txt -v time -s *:e";
			try {
				Process process = Runtime.getRuntime().exec(exec_getlog);
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {

			}
			super.run();
		}
	}

	public static final String get7DaysTerm() {
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
		c.set(Calendar.MONTH, M - 2);
		c.set(Calendar.DATE, 1);
		c.roll(Calendar.DATE, -1);
		int maxDate = c.get(Calendar.DATE);
		if (D1 <= 0) {
			D1 = maxDate + D1;
		}
		sb.append(String.valueOf(D1) + "日" + "-" + String.valueOf(D) + "日");
		return sb.toString();
	}

	public static final byte[] getTime() {
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

	public static byte intToByteBCD(int i) {
		byte ret = 0x00;
		int tmp = i % 10;
		ret |= tmp;
		tmp = (i / 10) % 10;
		ret = (byte) (ret | ((tmp & 0x00ff) << 4));
		return ret;
	}

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


	public static byte[] dec33ReverseReturn(byte data[], int offset, int length) {
		byte[] ret = dec33Return(data, offset, length);
		ret = reverse(ret);
		return ret;
	}

	public static byte[] dec33Return(byte data[], int offset, int length) {
		byte[] ret = new byte[length];
		for (int i = offset; i < offset + length; i++) {
			ret[i - offset] = (byte) ((byte) data[i] - (byte) 0x33);
		}
		return ret;
	}

	// 删除字符串首尾空白 包括ascII 0x0a空白
	public static String trim_7Fchar(String str) {
		if (str == null) {
			return "";
		}
		byte[] b = str.getBytes();
		int start = 0, end = b.length;
		for (int w = 0; w < b.length; w++) {
			if (((int) (b[w + 1] & 0xff) == (int) 0xa0)
					&& ((int) (b[w] & 0xff) == (int) 0xc2)) {
				System.out.println("+2");
				start += 2;
				w++;
			} else if ((int) (b[w] & 0xff) <= (int) 0x20) {
				System.out.println("++");
				start++;
			} else {
				break;
			}
			;
		}
		for (int r = b.length - 1; r >= 0; r--) {
			if (((int) (b[r] & 0xff) == (int) 0xa0)
					&& ((int) (b[r - 1] & 0xff) == (int) 0xc2)) {
				System.out.println("-2");
				end -= 2;
				r--;
			} else if ((int) (b[r] & 0xff) <= (int) 0x20) {
				System.out.println("--");
				end--;
			} else {
				break;
			}
		}
		return new String(b, start, end - start, Charset.forName("UTF-8"));
	}

	public final static byte[][] _biaozhi_DayReverseActivePowerEnergy = new byte[][] {
			{ 0x05, 0x06, 0x02, 0x01 }, { 0x05, 0x06, 0x02, 0x02 },
			{ 0x05, 0x06, 0x02, 0x03 }, { 0x05, 0x06, 0x02, 0x04 },
			{ 0x05, 0x06, 0x02, 0x05 }, { 0x05, 0x06, 0x02, 0x06 },
			{ 0x05, 0x06, 0x02, 0x07 }, { 0x05, 0x06, 0x02, 0x08 },
			{ 0x05, 0x06, 0x02, 0x09 }, { 0x05, 0x06, 0x02, 0x0a },
			{ 0x05, 0x06, 0x02, 0x0b }, { 0x05, 0x06, 0x02, 0x0c },
			{ 0x05, 0x06, 0x02, 0x0d }, { 0x05, 0x06, 0x02, 0x0e },
			{ 0x05, 0x06, 0x02, 0x0f }, { 0x05, 0x06, 0x02, 0x10 },
			{ 0x05, 0x06, 0x02, 0x11 }, { 0x05, 0x06, 0x02, 0x12 },
			{ 0x05, 0x06, 0x02, 0x13 }, { 0x05, 0x06, 0x02, 0x14 },
			{ 0x05, 0x06, 0x02, 0x15 }, { 0x05, 0x06, 0x02, 0x16 },
			{ 0x05, 0x06, 0x02, 0x17 }, { 0x05, 0x06, 0x02, 0x18 },
			{ 0x05, 0x06, 0x02, 0x19 }, { 0x05, 0x06, 0x02, 0x1a },
			{ 0x05, 0x06, 0x02, 0x1b }, { 0x05, 0x06, 0x02, 0x1c },
			{ 0x05, 0x06, 0x02, 0x1d }, { 0x05, 0x06, 0x02, 0x1e },
			{ 0x05, 0x06, 0x02, 0x1f }, };

	static class MyClass {
		public final double i = Math.random();
		public static double j = Math.random();

		void changeValue(StringBuffer buffer) {
			StringBuffer sbw = new StringBuffer("wk-");
			buffer = sbw;
			buffer.append("world");
			System.out.println("sbw:" + sbw);
		}
	}

	// 地址截取最后10位
	public static String getCheckedAddr(String addr) {
		if (addr == null) {
			return "";
		} else if (addr.length() > 10) {
			return addr.substring(addr.length() - 10, addr.length());
		} else {
			return addr;
		}
	}

	public static byte[] intToHEX2Bytes(int i) {
		byte[] ret = new byte[2];
		ret[0] = (byte) ((i >> 8) & 0xFF);
		ret[1] = (byte) ((i) & 0xFF);
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

	public static byte[] getUartTimedOutTime(String s) {
		if (s.length() > 0)
			s = s.substring(0, s.length() - 1);
		byte[] b = stringToDecBytes(s);
		byte[] ret = new byte[2];
		if (b.length == 1) {
			ret[0] = b[0];
		} else if (b.length == 2) {
			ret = b;
		} else {
			System.arraycopy(b, 0, ret, 0, ret.length);
		}
		return ret;
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

	// 字符串按原文16进制转换成16进制字节
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

	public static String toHexNoSpace(byte data[], int offset, int len) {
		StringBuilder sb = new StringBuilder();
		if (len + offset > data.length) {
			len = data.length - offset;
		}
		for (int i = offset; i < offset + len; i++) {
			sb.append(String.format("%02x", data[i] & 0xff));
		}
		return sb.toString();
	}

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

	// 逆置 减33
	public static byte[] reverseMinus33(byte[] data) {
		byte[] ret = new byte[data.length];
		ret = reverse(data);
		for (int i = 0; i < ret.length; i++) {
			ret[i] -= 0x33;
		}
		return ret;
	};

	public static String readTimeOfFrame(byte[] b_time) {
		StringBuilder sb = new StringBuilder();
		for (int w = 0; w < 6; w++) {
			sb.append(byteToBCD(b_time[6 - w]));
			if (w == 2) {
				sb.append(" ");
			} else if (w < 2) {
				sb.append(".");
			} else if (w < 5) {
				sb.append(":");
			}
		}
		return sb.toString();
	}

	public static byte[] make3BytesDevidTo6BytesAddr(int devid) {
		byte[] ret = new byte[6];
		ret[0] = (byte) (devid & 0x00ff);
		ret[1] = (byte) ((devid & 0xff00) >> 8);
		ret[2] = (byte) ((devid & 0xff0000) >> 8);
		for (int i = 0; i < 3; i++) {
			ret[i + 2] = 0x00;
		}
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

	public static byte[] intToByte3BCD(int i) {
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

	public static byte[] reverseAdd33(byte[] data) {
		byte[] ret = new byte[data.length];
		ret = reverse(data);
		for (int i = 0; i < ret.length; i++) {
			ret[i] += 0x33;
		}
		return ret;
	};

	public static byte[] reverse(byte[] array) {
		byte tmp[] = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			tmp[array.length - 1 - i] = array[i];
		}
		return tmp;
	}

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

	void testAlarmDoor() {
		AlarmDoor ad = new AlarmDoor();
		boolean ifPassed = ad.checkPass(input());
	};

	static int input() {
		String str_input = "";
		Scanner s = new Scanner(System.in);
		str_input = s.nextLine();
		byte[] tmp_b = str_input.getBytes();
		for (int i = 0; i < tmp_b.length; i++) {
			if (tmp_b[i] < 0x30 || tmp_b[i] > 0x39) {
				tmp_b = deleteAt(tmp_b, i);
			}
		}
		return Integer.valueOf(new String(tmp_b));
	}

	public static byte[] deleteAt(byte[] b, int index) {
		byte[] tmp_byte = new byte[b.length - 1];
		if (index != b.length) {
			System.arraycopy(b, 0, tmp_byte, 0, index);
			System.arraycopy(b, 0, tmp_byte, index, b.length - 1 - index);
			return tmp_byte;
		}
		if (index == b.length) {
			System.arraycopy(b, 0, tmp_byte, 0, index - 1);
			return tmp_byte;
		}
		return b;
	}

	// BCD 2bytes addr reverse to int
	public static int byte2BCDReverseToInt(byte b_left, byte b_right) {
		int ret = 0;
		ret = ((b_right & 0x000f) * 100) + (((b_right & 0x00f0) >> 4) * 1000)
				+ (((b_left & 0x00f0) >> 4) * 10) + (b_left & 0x000f);
		return ret;
	}

	public static String bcdReverse2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;
		bytes = bytesReverse(bytes);
		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	public static byte[] bytesReverse(byte[] b) {
		byte ret[] = new byte[b.length];
		if (b.length % 2 == 0) {
			for (int t = 0; t < b.length; t++) {
				if (t % 2 == 1) {
					continue;
				}
				ret[t] = b[1 + t];
				ret[t + 1] = b[t];
			}
		} else if (b.length % 2 == 1) {
			for (int t1 = 0; t1 < b.length - 1; t1++) {
				if (t1 % 2 == 1) {
					continue;
				}
				ret[t1] = b[1 + t1];
				ret[t1 + 1] = b[t1];
			}
			ret[b.length - 1] = b[b.length - 1];
		}
		return ret;
	}

	public static String byte4ToAccMeterVersion(byte t1, byte t2, byte t3,
			byte t4) {
		String ret_version = "";
		ret_version = "版本号:" + byteToAccMeterVersionStr(t1) + " "
				+ byteToAccMeterVersionStr(t2) + " "
				+ byteToAccMeterVersionStr(t3) + " "
				+ byteToAccMeterVersionStr(t4);
		return ret_version;
	}

	public static String byteToAccMeterVersionStr(byte b) {
		String ret = "";
		// ret = Byte.valueOf((byte)(b>>4)).toString() +
		// Byte.valueOf((byte)(b&0x0f)).toString();
		ret = Byte.valueOf((byte) ((b >> 4) & 0x0f)).toString() + "."
				+ Byte.valueOf((byte) (b & 0x0f)).toString();
		return ret;
	}

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

	public static int byte2ReverseToInt(byte b_left, byte b_right) {
		int ret = 0;
		ret = ((b_right & 0x00ff) << 8) + (b_left & 0x00ff);
		return ret;
	}

	public static int byte2ReverseToInt(byte[] b) {
		if (b.length < 2) {
			return 0;
		} else {
			int ret = 0;
			ret = ((b[1] & 0x00ff) << 8) + (b[0] & 0x00ff);
			return ret;
		}
	}

	public static String byteToSignalData(byte b) {
		String ret = "";
		int i = (int) ((b >> 7) & 0x01);
		System.out.println("b>>7:" + Integer.toHexString(i));
		if ((b & 0x00ff >> 7) == 0x01) {
			ret = "-";
		}
		ret += String.valueOf((int) (b & 0x7F));
		return ret;
	}

	public static void printBytes(String comment, byte[] b) {
		StringBuilder sb = new StringBuilder();
		String tmp_s = "";
		for (int i = 0; i < b.length; i++) {
			tmp_s = String.valueOf(Integer.toHexString(b[i] & 0xff));
			if (tmp_s.length() < 2) {
				tmp_s = "0" + tmp_s;
			}
			sb.append(tmp_s + " ");
		}
		System.out.print(("Test:" + comment + sb.toString()));
	}

	public static int byteToBCD(byte b) {
		int ret = 0, ret1, ret2;
		ret1 = b & 0x0f;
		ret2 = ((b & 0xf0) >> 4) * 10;
		ret = (b & 0x0f) + (((b & 0xf0) >> 4) * 10);
		// System.out.println("+++++ret1:" + ret1);
		// System.out.println("+++++ret2:" + ret2);
		// System.out.println("+++++ret:" + ret);
		return ret;
	}

	private enum STATUS {
		WAIT4FIRST68, WAIT4SECOND68, WAIT4LEN
	}

	public final static void filter(ByteBuffer in) {
		STATUS status = STATUS.WAIT4FIRST68;
		// 循环处理，直至数据长度不够
		while (true) {
			switch (status) {
			case WAIT4FIRST68:
				for (int i = in.position(); i < in.limit(); i++) {
					if ((in.get(i) & 0xff) == 0x68) {
						in.position(i);
						status = STATUS.WAIT4SECOND68;
						break;
					}
				}
			case WAIT4SECOND68:
				if (in.remaining() < 8) {
					System.out.println(status.WAIT4SECOND68
							+ "in.remaining() < 8");
				} else {
					if ((in.get(in.position() + 7) & 0xff) == 0x68) {
						status = STATUS.WAIT4LEN;
					} else {
						in.position(in.position() + 1);
						status = STATUS.WAIT4FIRST68;
						continue;
					}
				}
			case WAIT4LEN:
				if (in.remaining() < 10) {
					System.out.println(status.WAIT4LEN + "in.remaining() < 10");
				} else {
					int len = (in.get(in.position() + 9) & 0xff);
					if (in.remaining() < len + 12) {
						System.out.println(status.WAIT4LEN
								+ "in.remaining() < len + 12");
					} else {

						if ((in.get(in.position() + len + 11) & 0xff) != 0x16) {
							in.position(in.position() + 1);
							status = STATUS.WAIT4FIRST68;
							continue;
						}
						if (Util.checksum(in.array(), in.position(), 10 + len) == (0xff & in
								.get(in.position() + len + 10))) {
							System.out.println("checksum correct...");
							try {
								// PdaBranchFrame frame = new
								// PdaBranchFrame(Arrays.copyOfRange(in.array(),
								// in.position(), in.position() + 12 + len));
								// return frame;
								byte[] b = Arrays
										.copyOfRange(in.array(), in.position(),
												in.position() + 12 + len);
								for (byte t : b) {
									System.out.println("t:"
											+ Integer.toHexString(t & 0xff));
								}
							} catch (Exception e) {
								// 报文格式有误，理论上这里的异常应当永远不会出现，因为在这个swtich-case中已经保证了报文完整性。可以打印到trace日志
							} finally {
								in.position(in.position() + 12 + len);
								in.compact();
								in.flip();
							}
						} else {
							in.position(in.position() + 12 + len);
							status = STATUS.WAIT4FIRST68;
						}
					}
				}
			}
		}
	}

	public static int function(int num1, int num2, int num3) {
		return num1 * num2 + num3;
	}

	public static int byte2ToInt(byte low, byte high) {
		int i = low;
		i = (high & 0xff) << 8 + i;
		System.out.println("i:" + i);
		return ((low & 0xff) + ((high & 0xff) << 8));
	}

	public static int checksum(byte data[], int offset, int len) {
		int cs = 0;
		if (len + offset > data.length) {
			len = data.length - offset;
		}
		for (int i = offset; i < offset + len; i++) {
			cs += (int) (data[i] & 0x000000ff);
		}
		System.out.println("cs:" + Integer.toHexString(cs));
		return cs & 0xffff;
	}

	public static Boolean writeToFile(String path, long offset, byte[] data) {
		try {
			RandomAccessFile f = new RandomAccessFile(path, "rw");
			byte buff[] = new byte[1024];
			String temp = new String();
			int len = 0;
			// while((temp = f.readLine())!=null){
			// len += temp.length();
			// }
			// f.seek(len);
			f.seek(offset);
			f.write(data);
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static long byte4ArrToLong(byte[] b) {
		if (b.length != 4) {
			return 0L;
		}
		long ret_l = 0L;
		ret_l += (b[3] & 0xff);
		ret_l += (b[2] & 0xff) << 8;
		ret_l += (b[1] & 0xff) << 16;
		ret_l += (b[0] & 0xff) << 24;

		return ret_l;
	}

	public static long byte4ArrReverseToLong(byte[] b) {
		if (b.length != 4) {
			return 0L;
		}
		long ret_l = 0L;
		ret_l += (b[0] & 0xff);
		ret_l += (b[1] & 0xff) << 8;
		ret_l += (b[2] & 0xff) << 16;
		ret_l += (b[3] & 0xff) << 24;

		return ret_l;
	}

	public static Long decStringToLong(String decString) {
		if (decString.toLowerCase().contains("a")
				| decString.toLowerCase().contains("b")
				| decString.toLowerCase().contains("c")
				| decString.toLowerCase().contains("d")
				| decString.toLowerCase().contains("e")
				| decString.toLowerCase().contains("f")) {
			return 999999999l;
		}
		Long ret = Long.parseLong(decString);
		return ret;
	}

	/**
	 * @note flagΪ1��ʾ���ֽ� flagΪ2��ʾ���ֽ�
	 * */
	public static String p9s4a8bytesEnergy(byte[] b, int flag) {
		if (b.length != 16) {
			return "00000000";
		}
		byte[] tmp1 = new byte[4];
		byte[] tmp2 = new byte[4];
		byte[] tmp3 = new byte[4];
		byte[] tmp4 = new byte[4];
		System.arraycopy(b, 0, tmp1, 0, 4);
		System.arraycopy(b, 4, tmp2, 0, 4);
		System.arraycopy(b, 8, tmp3, 0, 4);
		System.arraycopy(b, 12, tmp4, 0, 4);
		long i1 = byte4ArrayToInt(tmp1, 0);
		long i2 = byte4ArrayToInt(tmp2, 0);
		long i3 = byte4ArrayToInt(tmp3, 0);
		long i4 = byte4ArrayToInt(tmp4, 0);
		Long i = i1 + i2 + i3 + i4;
		String str = Long.toHexString(i).toLowerCase();
		int str_len = str.length();
		StringBuilder sb = new StringBuilder("");
		if (str_len < 8 && str_len > 0) {
			for (int t1 = 0; t1 < 8 - str.length(); t1++) {
				sb.append("0");
			}
			str = sb.append(str).toString();
		}
		System.out.println("p9s4a8bytesEnergy:" + str);
		if (str_len == 0) {
			return "00000000";
		}
		if (flag == 1) {
			return str.substring(0, 4);
		} else {
			return str.substring(4, 8);
		}
	}

	/**
	 * byte[]----4�ֽ�תint
	 * 
	 * @param bytes
	 * @return
	 */
	public static long byte4ArrayToInt(byte[] bytes, int offset) {
		long value = 0;

		// �ɸ�λ����λ
		for (int i = offset; i < offset + 4; i++) {
			int shift = (offset + 4 - 1 - i) * 8;
			value = value + (((long) bytes[i] & 0xFF) << shift);
		}
		return value;
	}

	public class TestBean {
		private String addr;

		public String getAddr() {
			return addr;
		}

		public void setAddr(String addr) {
			this.addr = addr;
		}
	}

	// public static void sysout(Object s){
	// System.out.println("+++++ret:"+s.toString());
	// }

	public static Integer decStringToInt(String decString) {
		// if(decString.toLowerCase().contains("a")|decString.toUpperCase().contains("b")|decString.toUpperCase().contains("c")|decString.toUpperCase().contains("d")|decString.toUpperCase().contains("e")|decString.toUpperCase().contains("f")){
		// return 999999999;
		// }
		if ("4c9e3a38".matches("//w+")) {
			return 999999999;
		}
		Integer ret = Integer.parseInt(decString);
		return ret;
	}

	public static String byteArrTo07ID(byte b) {
		String ret;
		ret = Integer.toHexString(b & 0xff);
		if (ret.length() < 2) {
			StringBuilder sb = new StringBuilder();
			sb.append("0");
			ret = sb.toString().concat(ret);
		}
		return ret;
	}

	public static byte[] fromHexNoSpace(String hexString)
			throws NumberFormatException {
		byte ret[] = new byte[(hexString.length() + 1) / 2];
		for (int i = hexString.length(); i > 0; i -= 2) {
			ret[(i - 1) / 2] = (byte) Integer.parseInt(
					hexString.substring((i - 2) >= 0 ? (i - 2) : i - 1, i), 16);
		}
		return ret;
	}

	/**
	 * @����˵��:ȥ������ǰ�����Ԫ�أ��Ե�һ������Ԫ�ؿ�ʼ����ȡʣ�µĲ���
	 * @param:byte[]����
	 * @return:byte[]����
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

	/**
	 * ��ȡ�ļ���
	 * 
	 * */
	public static int getFileNum(File f) {
		int count = 0;
		if (!f.exists()) {
			System.out.println("file is not exists.");
			return 0;
		}
		if (f.isFile()) {
			return 1;
		}
		File[] f_list = f.listFiles();
		if (f.isDirectory()) {
			for (int i = 0; i < f_list.length; i++) {
				if (f_list[i].isDirectory()) {
					count = count + getFileNum(f_list[i]);
				}
				if (f_list[i].isFile()) {
					count++;
				}
			}
			return count;
		}
		return 0;
	}

	/**
	 * java �Ӽ��̻�ȡ����
	 * */
	public Scanner getFromKeyboard() {
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		int age = sc.nextInt();
		float salary = sc.nextFloat();
		return sc;
	}

	private static void testSitchAndIf() {
		int testCount = 10000000;
		int flag = 0;

		Random random = new Random(testCount);
		int[] flags = new int[testCount];
		for (int i = 0; i < testCount; i++) {
			flags[i] = random.nextInt() + 1;
		}
		// ��ͳһȡһ��ֵ����֤���Զ��û���ֵ
		for (int i = 0; i < testCount; i++) {
			flag = flags[i];
		}
		long time1 = System.currentTimeMillis();
		for (int i = 0; i < testCount; i++) {
			// flag = random.nextInt() + 1;
			flag = flags[i];
			if (flag == 1) {
			} else if (flag == 2) {
			} else if (flag == 3) {
			} else if (flag == 4) {
			} else if (flag == 5) {
			} else if (flag == 6) {
			} else if (flag == 7) {
			} else if (flag == 8) {
			} else if (flag == 9) {
			} else if (flag == 10) {
			} else if (flag == 11) {
			} else if (flag == 12) {
			} else if (flag == 13) {
			} else if (flag == 14) {
			} else if (flag == 15) {
			} else if (flag == 16) {
			} else if (flag == 17) {
			} else if (flag == 18) {
			} else if (flag == 19) {
			} else if (flag == 20) {
			} else if (flag == 21) {
			} else if (flag == 22) {
			} else if (flag == 23) {
			} else if (flag == 24) {
			} else if (flag == 25) {
			} else if (flag == 26) {
			} else if (flag == 27) {
			} else if (flag == 28) {
			} else if (flag == 29) {
			} else if (flag == 30) {
			} else if (flag == 31) {
			} else if (flag == 32) {
			} else if (flag == 33) {
			} else if (flag == 34) {
			} else if (flag == 35) {
			} else if (flag == 36) {
			} else if (flag == 37) {
			} else if (flag == 38) {
			} else if (flag == 39) {
			} else if (flag == 40) {
			} else if (flag == 41) {
			} else if (flag == 42) {
			} else if (flag == 43) {
			} else if (flag == 44) {
			} else if (flag == 45) {
			} else if (flag == 46) {
			} else if (flag == 47) {
			} else if (flag == 48) {
			} else if (flag == 49) {
			} else if (flag == 50) {
			} else if (flag == 51) {
			} else if (flag == 52) {
			} else if (flag == 53) {
			} else if (flag == 54) {
			} else if (flag == 55) {
			} else if (flag == 56) {
			} else if (flag == 57) {
			} else if (flag == 58) {
			} else if (flag == 59) {
			} else if (flag == 60) {
			} else if (flag == 61) {
			} else if (flag == 62) {
			} else if (flag == 63) {
			} else if (flag == 64) {
			} else if (flag == 65) {
			} else if (flag == 66) {
			} else if (flag == 67) {
			} else if (flag == 68) {
			} else if (flag == 69) {
			} else if (flag == 70) {
			} else if (flag == 71) {
			} else if (flag == 72) {
			} else if (flag == 73) {
			} else if (flag == 74) {
			} else if (flag == 75) {
			} else if (flag == 76) {
			} else if (flag == 77) {
			} else if (flag == 78) {
			} else if (flag == 79) {
			} else if (flag == 80) {
			} else if (flag == 81) {
			} else if (flag == 82) {
			} else if (flag == 83) {
			} else if (flag == 84) {
			} else if (flag == 85) {
			} else if (flag == 86) {
			} else if (flag == 87) {
			} else if (flag == 88) {
			} else if (flag == 89) {
			} else if (flag == 90) {
			} else if (flag == 91) {
			} else if (flag == 92) {
			} else if (flag == 93) {
			} else if (flag == 94) {
			} else if (flag == 95) {
			} else if (flag == 96) {
			} else if (flag == 97) {
			} else if (flag == 98) {
			} else if (flag == 99) {
			} else if (flag == 100) {
			} else if (flag == 101) {
			} else if (flag == 102) {
			} else if (flag == 103) {
			} else if (flag == 104) {
			} else if (flag == 105) {
			} else if (flag == 106) {
			} else if (flag == 107) {
			} else if (flag == 108) {
			} else if (flag == 109) {
			} else if (flag == 110) {
			} else if (flag == 111) {
			} else if (flag == 112) {
			} else if (flag == 113) {
			} else if (flag == 114) {
			} else if (flag == 115) {
			} else if (flag == 116) {
			} else if (flag == 117) {
			} else if (flag == 118) {
			} else if (flag == 119) {
			} else if (flag == 120) {
			} else if (flag == 121) {
			} else if (flag == 122) {
			} else if (flag == 123) {
			} else if (flag == 124) {
			} else if (flag == 125) {
			} else if (flag == 126) {
			} else if (flag == 127) {
			} else if (flag == 128) {
			} else if (flag == 129) {
			} else if (flag == 130) {
			} else if (flag == 131) {
			} else if (flag == 132) {
			} else if (flag == 133) {
			} else if (flag == 134) {
			} else if (flag == 135) {
			} else if (flag == 136) {
			} else if (flag == 137) {
			} else if (flag == 138) {
			} else if (flag == 139) {
			} else if (flag == 140) {
			} else if (flag == 141) {
			} else if (flag == 142) {
			} else if (flag == 143) {
			} else if (flag == 144) {
			} else if (flag == 145) {
			} else if (flag == 146) {
			} else if (flag == 147) {
			} else if (flag == 148) {
			} else if (flag == 149) {
			} else if (flag == 150) {
			} else if (flag == 151) {
			} else if (flag == 152) {
			} else if (flag == 153) {
			} else if (flag == 154) {
			} else if (flag == 155) {
			} else if (flag == 156) {
			} else if (flag == 157) {
			} else if (flag == 158) {
			} else if (flag == 159) {
			} else if (flag == 160) {
			} else if (flag == 161) {
			} else if (flag == 162) {
			} else if (flag == 163) {
			} else if (flag == 164) {
			} else if (flag == 165) {
			} else if (flag == 166) {
			} else if (flag == 167) {
			} else if (flag == 168) {
			} else if (flag == 169) {
			} else if (flag == 170) {
			} else if (flag == 171) {
			} else if (flag == 172) {
			} else if (flag == 173) {
			} else if (flag == 174) {
			} else if (flag == 175) {
			} else if (flag == 176) {
			} else if (flag == 177) {
			} else if (flag == 178) {
			} else if (flag == 179) {
			} else if (flag == 180) {
			} else if (flag == 181) {
			} else if (flag == 182) {
			} else if (flag == 183) {
			} else if (flag == 184) {
			} else if (flag == 185) {
			} else if (flag == 186) {
			} else if (flag == 187) {
			} else if (flag == 188) {
			} else if (flag == 189) {
			} else if (flag == 190) {
			} else if (flag == 191) {
			} else if (flag == 192) {
			} else if (flag == 193) {
			} else if (flag == 194) {
			} else if (flag == 195) {
			} else if (flag == 196) {
			} else if (flag == 197) {
			} else if (flag == 198) {
			} else if (flag == 199) {
			}
		}

		long time2 = System.currentTimeMillis();
		for (int i = 0; i < testCount; i++) {
			flag = flags[i];
			switch (flag) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				break;
			case 13:
				break;
			case 14:
				break;
			case 15:
				break;
			case 16:
				break;
			case 17:
				break;
			case 18:
				break;
			case 19:
				break;
			case 20:
				break;
			case 21:
				break;
			case 22:
				break;
			case 23:
				break;
			case 24:
				break;
			case 25:
				break;
			case 26:
				break;
			case 27:
				break;
			case 28:
				break;
			case 29:
				break;
			case 30:
				break;
			case 31:
				break;
			case 32:
				break;
			case 33:
				break;
			case 34:
				break;
			case 35:
				break;
			case 36:
				break;
			case 37:
				break;
			case 38:
				break;
			case 39:
				break;
			case 40:
				break;
			case 41:
				break;
			case 42:
				break;
			case 43:
				break;
			case 44:
				break;
			case 45:
				break;
			case 46:
				break;
			case 47:
				break;
			case 48:
				break;
			case 49:
				break;
			case 50:
				break;
			case 51:
				break;
			case 52:
				break;
			case 53:
				break;
			case 54:
				break;
			case 55:
				break;
			case 56:
				break;
			case 57:
				break;
			case 58:
				break;
			case 59:
				break;
			case 60:
				break;
			case 61:
				break;
			case 62:
				break;
			case 63:
				break;
			case 64:
				break;
			case 65:
				break;
			case 66:
				break;
			case 67:
				break;
			case 68:
				break;
			case 69:
				break;
			case 70:
				break;
			case 71:
				break;
			case 72:
				break;
			case 73:
				break;
			case 74:
				break;
			case 75:
				break;
			case 76:
				break;
			case 77:
				break;
			case 78:
				break;
			case 79:
				break;
			case 80:
				break;
			case 81:
				break;
			case 82:
				break;
			case 83:
				break;
			case 84:
				break;
			case 85:
				break;
			case 86:
				break;
			case 87:
				break;
			case 88:
				break;
			case 89:
				break;
			case 90:
				break;
			case 91:
				break;
			case 92:
				break;
			case 93:
				break;
			case 94:
				break;
			case 95:
				break;
			case 96:
				break;
			case 97:
				break;
			case 98:
				break;
			case 99:
				break;
			}
		}

		long time3 = System.currentTimeMillis();
		System.out.println("loop count:" + testCount);
		System.out.println("if consume time:" + (time2 - time1) + ",avg:"
				+ testCount / (time2 - time1) * 1000);
		System.out.println("switch consume time:" + (time3 - time2) + ",avg:"
				+ testCount / (time3 - time2) * 1000);
	}

	// private static String reduceDuplicate(String s){
	// String ret_str = "";
	// if(s!=null){
	// for(int i = 0 ; i < s.length();i++){
	// s.charAt(i);
	// }
	// }
	// }
}
