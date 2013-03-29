package com.sbarnesisrael.exifgps;

public class TestSnippets {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "38.0° 37.0\' 28.800000000011323\"";
		
		System.out.println("Degrees: " + a.substring(0,a.indexOf("\u00b0")));
		System.out.println("Minutes: " + a.substring(a.indexOf("\u00b0") + 1,a.indexOf("'")));
		System.out.println("Seconds: " + a.substring(a.indexOf("'") + 1,a.indexOf("\"")));
	}

}
