package it.enricogamba.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;

public final class Utils {
	
	
	/*
	 * given a file path, the file is opened and translated into a matrix (char[][])
	 */
	public static char[][] getMatrixFromFilePath(String filePath) {
		return null;
	}
	
	
	
	
	/*
	 * given 2 matrixes, return percentage of match (0..100)
	 */
	public static BigDecimal getMatchPerc(char[][] a, char[][] b) {
		try {
			//if matrixes sizes are different the match is 0
			if (a.length!=b.length || a[0].length!=b[0].length) return new BigDecimal(0);
			
			int tot_matches = 0;
			int tot = 0;
			
			BigDecimal matchPerc = new BigDecimal(0);
			
			for (int y=0; y<a.length; y++) {
				for (int x=0; x<a[y].length; x++) {
					tot++;
					if (a[y][x]==b[y][x]) tot_matches++;
				}
			}
			
			try {
			matchPerc = (new BigDecimal(tot_matches).divide(new BigDecimal(tot),4,RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
			matchPerc = matchPerc.setScale(0, RoundingMode.HALF_UP);
			return matchPerc;
			} catch (Exception e) {
				e.printStackTrace();
				return new BigDecimal(0);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}
	
	
	/*
	 * given a file, returns the matrix representing the images
	 * params:
	 * - radarImageFile
	 * - radarImageMatrixExtensions (to extend the matrix in order to consider edge cases
	 * - ext: char used to fill extensions
	 */
	public static char[][] getMatrixFromFile(File radarImageFile, int radarImageMatrixExtensions, char ext) {
		System.out.println("Processing " + radarImageFile.getName());
		HashMap<Integer,String> lines = new HashMap<Integer,String>();
		Integer line_length = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(radarImageFile))) {
		    String line;
		    Integer line_number = 0;
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	if (line_length<line.length()) line_length = line.length();
		       lines.put(line_number, line);
		       line_number++;
		    }
		} catch (FileNotFoundException e) {
			System.out.println("OPS, something went wrong in reading the file ...");
		} catch (IOException e) {
			System.out.println("OPS, something went wrong in reading the file ...");
		}
		int radarImageMatrixLength = 0;
		int radarImageMatrixHeight = 0;
		
		radarImageMatrixHeight = lines.size();
		radarImageMatrixLength = line_length; //though the lines are supposed to be the same size, I take the longest for setting the matrix size
		
		if (radarImageMatrixHeight<1 || radarImageMatrixLength<1) {
			System.out.println("May you plese verify if the file is empty and try again?");
		}
		
		char[][] radarImageMatrix = new char[radarImageMatrixHeight+2*radarImageMatrixExtensions][radarImageMatrixLength+2*radarImageMatrixExtensions];
		
		//fill in the matrix
		//begin with the extensions
		//extension will be marked with the extension char ext
		for (int y=0; y<radarImageMatrix.length; y++) {
			for (int x=0; x<radarImageMatrix[y].length; x++) {
				radarImageMatrix[y][x] = ext;
			}
		}
		
		
		//now let's feel the matrix with the image values
	    @SuppressWarnings("rawtypes")
		Iterator it = lines.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			HashMap.Entry pair = (HashMap.Entry)it.next();
	        Integer y = (Integer) pair.getKey();
	        String line = (String) pair.getValue();
	        char[] lineCharArray = line.toCharArray();
	        for (int x=0; x<lineCharArray.length; x++) {
	        	radarImageMatrix[radarImageMatrixExtensions+y][radarImageMatrixExtensions+x] = lineCharArray[x];
	        }
	    }
		
		
		return radarImageMatrix;
	}
	
	
	
	
	
	
	
	
	
	/*
	 * given a matrix, it outputs it
	 */
	public static void printImageMatrix(char[][] imageMatrix) {
	    for (int y=0; y<imageMatrix.length; y++) {
			for (int x=0; x<imageMatrix[y].length; x++) {
				System.out.print(imageMatrix[y][x]);
			}
			System.out.print(System.getProperty("line.separator"));
		}
	}
	
	/*
	 * given a matrix and an extension, it outputs the matrix without extension
	 */
	public static void printImageMatrixNoExtension(char[][] imageMatrix, int extension) {
	    for (int y=0+extension; y<imageMatrix.length-extension; y++) {
			for (int x=0+extension; x<imageMatrix[y].length-extension; x++) {
				System.out.print(imageMatrix[y][x]);
			}
			System.out.print(System.getProperty("line.separator"));
		}
	}
}
