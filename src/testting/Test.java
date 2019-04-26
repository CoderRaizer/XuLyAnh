package testting;

import java.util.Scanner;

public class Test {
	
    public static void setupTempMatrix(int [][] matrix, int width, int height, /* ------ */ int [][] tempMatrix, int tempWidth , int tempHeight){
    	// Correctly

        for (int y = 0 ; y < tempHeight ; y++){
            for (int x = 0 ; x < tempWidth ; x++){
                tempMatrix[x][y] = -1;
            }
        }
        
        int tempVal = 0;

        // TODO : Setup for corner
        for (int y = 0 ; y < tempHeight ; y++){
            for (int x = 0 ; x < tempWidth ; x++){

                if (x == 0 && y == 0){
                	tempVal = matrix[0][0];
                    tempMatrix[y][x] = tempVal;
                }
                if (y == 0 && x == tempWidth-1){
                	tempVal = matrix[0][width-1];
                    tempMatrix[y][x] = tempVal;
                }
                if( y == tempHeight-1 && x == tempWidth-1 ){
                	tempVal = matrix[height-1][width-1];
                    tempMatrix[y][x] = tempVal;
                }
                if( y == tempHeight-1 && x == 0 ){
                	tempVal = matrix[height-1][0];
                    tempMatrix[y][x] = tempVal;
                }

            }
        }
        // TODO : Setup for border
        for (int y = 0 ; y < tempHeight ; y++){
            for (int x = 0 ; x < tempWidth ; x++){

                if (y == 0){
                    if (x > 0 && x < tempWidth-1){
                    	tempVal = matrix[0][x-1];
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (x == 0){
                    if (y > 0 && y <tempHeight-1){
                    	tempVal = matrix[y-1][x];
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (y == (tempHeight-1)){
                    if(x > 0 && x < tempWidth-1){
                    	tempVal = matrix[height-1][x-1];
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (x == (tempWidth-1)){
                    if (y > 0 && y < tempHeight-1){
                    	tempVal = matrix[y-1][width-1];
                        tempMatrix[y][x] = tempVal;
                    }
                }

            }
        }

        for (int y = 1 ; y < tempHeight-1 ; y++) {
        	for (int x = 1 ; x < tempWidth-1 ; x++) {
        		tempMatrix[y][x] = matrix[y-1][x-1];
        	}
        }

    }
	
	
	
	public static void main(String []arg) {
		
		Scanner o = new Scanner(System.in);
		
		int width = 5;
		int height = 5;
		int num = 0;
		int [][] matrix = new int[height][width];
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				System.out.println("Nhap : " );num = o.nextInt();
				matrix[y][x] = num;
			}
		}
		
		
		int[][] tempMatrix;
		int widthTemp = width + 2;
		int heightTemp = height + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		setupTempMatrix(matrix,width,height, tempMatrix, widthTemp, heightTemp);
		
		for (int y = 0 ; y < heightTemp ; y++) {
			for (int x = 0 ; x < widthTemp ; x++) {
				System.out.print(tempMatrix[y][x]);
			}System.out.print("\n");
		}
	}

}
