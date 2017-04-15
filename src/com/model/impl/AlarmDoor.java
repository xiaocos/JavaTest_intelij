package com.model.impl;

import com.model.abstintf.Door;
import com.model.intf.Alarm;
import com.model.intf.CallThePlc;
import com.model.intf.Password;

public class AlarmDoor extends Door implements Alarm,Password,CallThePlc{
	
	@Override
	public void alarm() {
		System.out.println("just hit the gate..");
	}

	@Override
	public boolean checkPass(int pass) {
		if(pass == 65534){
			System.out.println("identified correct..");
			open();
			return true;
		}else{
			callThePolice();
			System.out.println("identified error..");
			return false;
		}
	}

	@Override
	public void callThePolice() {
		close();
		System.out.println("dial 110,ringing alert..");
	}
}
