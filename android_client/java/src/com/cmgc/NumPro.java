package com.cmgc;


public class NumPro {

    public native String native_setPhoneNumber(String num);
    public String setPhoneNumber(String number){
        return  native_setPhoneNumber(number);
    }
    static{
        System.loadLibrary("numpro");
    }

}
