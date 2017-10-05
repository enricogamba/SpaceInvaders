package it.enricogamba.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Scanner;

import it.enricogamba.utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		
		/*
		 * paramethers and variables setup
		 */
		int tolerance_perc = 0;
		boolean tolerance_OK = false;
		
		final String radarImagesFolderPath = "./Test/RadarImages";
		final String knownSpaceInvadersImagesPath = "./Test/KnownSpaceInvadersImages";
		final int radarImageMatrixExtensions = 10; //radar image matrix wil be extended in order to analyze the edge cases
		final char ext = '*'; //char used to mark extensions
		final char empty = '.'; //char used to mark empty matrix
		
		final boolean DEBUG = false;
		
		Scanner reader = new Scanner(System.in);
		/*
		 * 1. Read input paramethers
		 */
		
		System.out.println("**************************************************************");
		System.out.println("Welcome to SPACE INVADERS!");
		System.out.println("**************************************************************");
		
		System.out.println("Test space invaders images -> " + knownSpaceInvadersImagesPath);
		System.out.println("Test radar image -> " + radarImagesFolderPath);
		System.out.println("");
		System.out.println("Ready to detect space invaders. What percentage of tolerance do you want me to use? [0-100]");
		System.out.println("(0 -> perfect match, 100 -> really???");
		
		while (!tolerance_OK) {
			try {
				tolerance_perc = reader.nextInt();
				tolerance_OK = true;
			} catch (Exception e) {
				System.out.println("Are you sure you entered a correct integer value? Would you like to try again? (Y/N)");
				if (!reader.next().trim().equalsIgnoreCase("Y")) {
					System.out.println("Sad about that. Bye, see you next time!");
					System.exit(0);
				}
			}
		}
		
		
		/*
		 * 2. read radar image file(s)
		 */
		File radarImagesFolder = new File(radarImagesFolderPath);
		File[] radarImageFileList = radarImagesFolder.listFiles();
		for(File radarImageFile : radarImageFileList) {
			String radarImageFileName = radarImageFile.getName();
			System.out.println("This radar image has been found: '"+radarImageFileName + "', proceed? [Y/N]");
			/*
			 * 3. create radar image matrix
			 */
			char[][] radarImageMatrix = Utils.getMatrixFromFile(radarImageFile, radarImageMatrixExtensions, ext);
			
			


		    
		    //print the extended radar image (just for check & fun)
		    System.out.println("Would you like to see the extended radar image (extended image is used to find edge cases)? (Y/N)");
		    	Utils.printImageMatrix(radarImageMatrix);

		    
		    
		    /*
		     * 4. read space invaders image files names
		     */
			File knownSpaceInvadersImagesFolder = new File(knownSpaceInvadersImagesPath);
			File[] knownSpaceInvadersImageFileList = knownSpaceInvadersImagesFolder.listFiles();
			/*
			 * 5. cycle space invaders
			 */
			for(File knownSpaceInvadersImageFile : knownSpaceInvadersImageFileList) {
				String knownSpaceInvadersImageFileName = knownSpaceInvadersImageFile.getName();
				System.out.println("==================================================================================");
				System.out.println("==================================================================================");
				System.out.println("==================================================================================");
				System.out.println("This space invaders image has been found: '"+knownSpaceInvadersImageFileName + "', proceed or skip? [Y/N]");
				
				//read image file and create a matrix
				
				/*
				 * 5a. empty matrix thath will be filled with recognized space invaders
				 */
				char[][] recognizedSpaceInvadersImageMatrix = new char[radarImageMatrix.length][radarImageMatrix[0].length];
				
				//fill in the empty matrix
				//first extensions ...
				for (int y=0; y<recognizedSpaceInvadersImageMatrix.length; y++) {
					for (int x=0; x<recognizedSpaceInvadersImageMatrix[y].length; x++) {
						recognizedSpaceInvadersImageMatrix[y][x] = ext;
					}
				}
				///...then empties ...
				for (int y=radarImageMatrixExtensions; y<recognizedSpaceInvadersImageMatrix.length-radarImageMatrixExtensions; y++) {
					for (int x=radarImageMatrixExtensions; x<recognizedSpaceInvadersImageMatrix[y].length-radarImageMatrixExtensions; x++) {
						recognizedSpaceInvadersImageMatrix[y][x] = empty;
					}
				}
				
				
				/*
				 * 	5b. read the current space invader file
				 * 	5c. create a matrix from the file
				 */
				char[][] knownSpaceInvaderMatrix = Utils.getMatrixFromFile(knownSpaceInvadersImageFile, 0, ext);
				
				if (knownSpaceInvaderMatrix==null || knownSpaceInvaderMatrix.length < 1) {
					System.out.println("Matrix is empty. I will step to the next file.");
					continue;
				}
				
				System.out.println("==================================================================================");
				Utils.printImageMatrix(knownSpaceInvaderMatrix);
				System.out.println("==================================================================================");

				
				/*
				 * 	5d. cycle the radar image starting from the top left.
				 */
				int matches_n = 0;
				for (int y=0; y< radarImageMatrix.length-knownSpaceInvaderMatrix.length; y++) {
					for (int x=0; x<radarImageMatrix[y].length-knownSpaceInvaderMatrix[0].length; x++) {
						/* 
						 * 		5d1. For any point extract a submatrix of the same dimensions as the space invader matrix from the radar image matrix
						 */
						char[][] radarImageSubMatrix = new char[knownSpaceInvaderMatrix.length][knownSpaceInvaderMatrix[0].length];
						int y_s = 0;//submatrix y
						for (int y_=y; y_< y+knownSpaceInvaderMatrix.length; y_++) {
							int x_s = 0;//submatrix x
							for (int x_=x; x_<x+knownSpaceInvaderMatrix[0].length; x_++) {
								
								
								radarImageSubMatrix[y_s][x_s] = radarImageMatrix[y_][x_];
								
								x_s++;
							}//for (int x_=x; x_<x+knownSpaceInvaderMatrix[0].length; x_++) END
							y_s++;
						}//for (int y_=y; y_< y+knownSpaceInvaderMatrix.length; y_++) END
						
						//System.out.println("TEST SUBMATRIX");
						//Utils.printImageMatrix(radarImageSubMatrix);
						
				/* 
				* 		5d2. compare the submatrix with the space invader's one
				*/
						BigDecimal matchPerc = new BigDecimal(0);
						
						matchPerc = Utils.getMatchPerc(knownSpaceInvaderMatrix, radarImageSubMatrix);
						
						if (matchPerc.compareTo(new BigDecimal(100-tolerance_perc))>0) {
							matches_n++;
							
							//DEBUG
							if (DEBUG) {
								System.out.println("==================================================================================");
								Utils.printImageMatrix(knownSpaceInvaderMatrix);
								System.out.println("----------------------------------------------------------------------------------");
								Utils.printImageMatrix(radarImageSubMatrix);
								System.out.println("----------------------------------------------------------------------------------");
								System.out.println(matchPerc);
								System.out.println("==================================================================================");
								System.out.println("Proceed?");
								while (!reader.next().trim().equalsIgnoreCase("Y")) {
									System.out.println("OK, and now?");	
								}
							}
						
							/*
							 * 		5d3. if the matrixes are equals (considering the tolerance) the submatrix is accepted and painted to the output matrix
							 */
							for (int y_=y; y_< y+knownSpaceInvaderMatrix.length; y_++) {
								for (int x_=x; x_<x+knownSpaceInvaderMatrix[0].length; x_++) {
									recognizedSpaceInvadersImageMatrix[y_][x_] = radarImageMatrix[y_][x_];
								}//for (int x_=x; x_<x+knownSpaceInvaderMatrix[0].length; x_++) END
							}//for (int y_=y; y_< y+knownSpaceInvaderMatrix.length; y_++) END
							
						
						
						}//if (matchPerc.compareTo(new BigDecimal(100-tolerance_perc))>0) END
						
						
						
				 
					}//for (int x=0; x<radarImageMatrix[y].length-knownSpaceInvaderMatrix[0].length; x++) END
				}//for (int y=0; y< radarImageMatrix.length-knownSpaceInvaderMatrix.length; y++) END
				
				/*
				 *  	5e. output:
				 *
				 * 		5e1. output image (with false positives / negatives highlighted)
				 * 		5e2. some statistics (number of invaders found, ...)
				 */
				System.out.println("==================================================================================");
				System.out.println("==================================================================================");
				Utils.printImageMatrixNoExtension(recognizedSpaceInvadersImageMatrix, radarImageMatrixExtensions);
				System.out.println("==================================================================================");
				System.out.println("Using " + tolerance_perc + "% tolerance, " + matches_n + " matches have been found for " + knownSpaceInvadersImageFileName + " in " + radarImageFile.getName());
				if (tolerance_perc>20) {
					System.out.println("You have selected a high tolerance value. This can cause overlapped images!!!");
				}
				System.out.println("==================================================================================");
				System.out.println("No more Space Invaders to find for " + radarImageFileName);
			} //for(File knownSpaceInvadersImageFile : knownSpaceInvadersImageFileList) END
		    
			System.out.println("I will step to the next radar image file.");
		}//for(File radarImageFile : radarImageFileList) END
		System.out.println("No more files to examine, quit.");
		reader.close();
		System.exit(0);
	} //main END
}






