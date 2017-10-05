/*
 * SPACE INVADERS IDENTIFICATION
 * 
 * 
 * PROCEDURE:
 * 
 * 1. ask the user for a tolerance value in percentage (in order to recognize space invaders and tolerate false positives / negatives)
 * 2. read radar image file list
 * cycle the list:
 * 3. create a matrix from it
 * 	3a. the matrix should be larger than the original file in order to consider edge cases
 * 4. read space invaders images files names
 * 5. cycle the space invaders images files names list (?or ask the user what image they want to analyze?)
 * 	5a. create an empty matrix that will be filled with the recognised space invaders
 * 	5b. read the current space invader file
 * 	5c. create a matrix from the file
 * 	5d. cycle the radar image starting from the top left.
 * 		5d1. For any point extract a submatrix of the same dimensions as the space invader matrix.
 * 		5d2. compare the submatrix with the space invader's one
 * 		5d3. if the matrixes are equals (considering the tolerance) the submatrix is accepted and painted to the output matrix
 * 		5d4. for the moment we don't consider overlapping, so, if the tolerance is satisfied, we jump to the next non-overlapping position (OR NOT???)
 * 	5e. output:
 * 		5e1. output image (with false positives / negatives highlighted)
 * 		5e2. some statistics (number of invaders found, ...)
 * 
 * 
 * COMPROMISES:
 * - Didn't use interface for utilities class
 */
 
 
 FIle structure:
 src/it.enricogamba.main -> main class
 src/it.enricogamba.utils -> utility class(es)
 src/it.enricogamba.test -> tet main class
 KnownSpaceInvadersImages -> default space invaders folder
 RadarImages -> default radar images folder
 Test/KnownSpaceInvadersImages -> test space invaders folder
 Test/RadarImages -> test radar images folder
 