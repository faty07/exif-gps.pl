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
		new ExifGPS("/home/sbarnesisrael/Downloads/exif_java/res/f6db4c02e353bc82cfb2d5fd277d76cf.jpg");
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
            System.out.println("LatLong Pair: " + convertCoords(latitude) + "," + convertCoords(longitude));
            
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
	
	public String convertCoords(String coords){
		// TODO: Refactor code to run regardless of Lat or Long (one method, run twice - once per coord required)
		
		// Grab just what we need
		// NOTE: \u00b0 is the degrees symbol in Unicode
		String degCoords = coords.substring(0,coords.indexOf("\u00b0"));
		String minCoords = coords.substring(coords.indexOf("\u00b0") + 1,coords.indexOf("'"));
		String secCoords = coords.substring(coords.indexOf("'") + 1,coords.indexOf("\""));
				
		// Do the conversion math to get a LatLong Pair
		// If the degrees are negative we need to be sure we are not adding to a negative number
		double cnvrtdCoords = 0;
		if (Double.parseDouble(degCoords) <= 0) {
			// West or South
			cnvrtdCoords = -(Math.abs(Double.parseDouble(degCoords)) + (Double.parseDouble(minCoords)/60) + (Double.parseDouble(secCoords)/3600));
		} else {
			// East or North
			cnvrtdCoords = Double.parseDouble(degCoords) + (Double.parseDouble(minCoords)/60) + (Double.parseDouble(secCoords)/3600);
		}
				
		//Round to 4 decimal places
		cnvrtdCoords = roundFourDecimals(cnvrtdCoords);
		
		// Return what we found
		return Double.toString(cnvrtdCoords);
	}

}
