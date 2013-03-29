package com.sbarnesisrael.exifgps;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDescriptor;
import com.drew.metadata.exif.GpsDirectory;
import java.text.*;

import java.io.File;
import java.io.IOException;

public class ExifGPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ExifGPS("/home/sbarnesisrael/Downloads/exif_java/res/41137_2_279_123_256.jpg");
	}
	
	public ExifGPS(String fileName) {
		File file = new File(fileName); 
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            
            GpsDirectory GPSdir = metadata.getDirectory(GpsDirectory.class);
            GpsDescriptor GPSdesc = new GpsDescriptor(GPSdir);
            ExifIFD0Directory EXIFdir = metadata.getDirectory(ExifIFD0Directory.class);
            ExifIFD0Descriptor Exifdesc = new ExifIFD0Descriptor(EXIFdir);
            
            
            String make = Exifdesc.getDescription(ExifIFD0Directory.TAG_MAKE);
            String model = Exifdesc.getDescription(ExifIFD0Directory.TAG_MODEL);
            String latitudeRef = GPSdesc.getDescription(GpsDirectory.TAG_GPS_LATITUDE_REF);
            String latitude = GPSdesc.getGpsLatitudeDescription();
            String longitudeRef = GPSdesc.getDescription(GpsDirectory.TAG_GPS_LONGITUDE_REF);
            String longitude = GPSdesc.getGpsLongitudeDescription();
            
            System.out.println("Make: " + make);
            System.out.println("Model: " + model);
            System.out.println("Latitude: " + latitude + " " + latitudeRef);
            System.out.println("Longitude: " + longitude + " " + longitudeRef);
            System.out.println("LatLong Pair: " + getLongLatPair(latitude, longitude));
            
        } catch (ImageProcessingException e) {
            System.err.println("error 1a: " + e);
        } catch (IOException e) {
            System.err.println("error 1b: " + e);
        }
	}
		
	public void printImageTags(int approachCount, Metadata metadata)
    {
        System.out.println();
        System.out.println("*** APPROACH " + approachCount + " ***");
        System.out.println();
        // iterate over the exif data and print to System.out
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags())
                System.out.println(tag);
            for (String error : directory.getErrors())
                System.err.println("ERROR: " + error);
        }
    }
	
	private double roundFourDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("###.####");
		return Double.valueOf(twoDForm.format(d));
	}
	
	public String getLongLatPair(String latitude, String longitude){
		// TODO: Refactor code to run regardless of Lat or Long (one method, run twice - once per coord required)
		
		// Grab just what we need
		// NOTE: \u00b0 is the degrees symbol in Unicode
		String latDegrees = latitude.substring(0,latitude.indexOf("\u00b0"));
		String latMinutes = latitude.substring(latitude.indexOf("\u00b0") + 1,latitude.indexOf("'"));
		String latSeconds = latitude.substring(latitude.indexOf("'") + 1,latitude.indexOf("\""));
		String longDegrees = longitude.substring(0,longitude.indexOf("\u00b0"));
		String longMinutes = longitude.substring(longitude.indexOf("\u00b0") + 1,longitude.indexOf("'"));
		String longSeconds = longitude.substring(longitude.indexOf("'") + 1,longitude.indexOf("\""));
		
		// Do the conversion math to get a LatLong Pair
		// If the degrees are negative we need to be sure we are not adding to a negative number
		double cnvrtdLat = 0;
		if (Double.parseDouble(latDegrees) <= 0) {
			// West or South
			cnvrtdLat = -(Math.abs(Double.parseDouble(latDegrees)) + (Double.parseDouble(latMinutes)/60) + (Double.parseDouble(latSeconds)/3600));
		} else {
			// East or North
			cnvrtdLat = Double.parseDouble(latDegrees) + (Double.parseDouble(latMinutes)/60) + (Double.parseDouble(latSeconds)/3600);
		}
		
		double cnvrtdLong = Math.abs(Double.parseDouble(longDegrees)) + (Double.parseDouble(longMinutes)/60) + (Double.parseDouble(longSeconds)/3600);
		
		//Round to 4 decimal places
		cnvrtdLat = roundFourDecimals(cnvrtdLat);
		cnvrtdLong = roundFourDecimals(cnvrtdLong);
		
		// Convert everything to a string and return
		String longLatPair = Double.toString(cnvrtdLat) + "," + Double.toString(cnvrtdLong);	
		return longLatPair;
	}

}
