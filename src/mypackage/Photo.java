package mypackage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Photo {

    public Photo(){

    }
    
    
    /*************************************************************************************/
    public ArrayList<Integer> enhanceImageByLogarit(int red , int green , int blue, float constant){
    	
        ArrayList <Integer> list = new ArrayList<Integer>();
        final double c = constant;
        float rRed = (float)(red / 255.0);
        float rGreen = (float)(green / 255.0);
        float rBlue = (float)(blue / 255.0);

        double sRed = (float) Math.log(1+rRed) * c * 255;
        double sGreen = (float) Math.log(1+rGreen) * c * 255;
        double sBlue = (float) Math.log(1+rBlue) * c * 255;

        if (sRed < 0){ sRed = 0; } if (sRed > 255){ sRed = 255; }
        if (sGreen < 0){ sGreen = 0; }if (sGreen > 255){ sGreen = 255; }
        if (sBlue < 0){ sBlue = 0; }if (sBlue > 255){ sBlue = 255; }

        list.add((int)sRed);
        list.add((int)sGreen);
        list.add((int)sBlue);
        return list;
    }
    /*************************************************************************************/

    /*************************************************************************************/
    public ArrayList<Integer> enhanceImageByPower(int red , int green , int blue, float constant, float gramma){
        ArrayList <Integer> list = new ArrayList<Integer>();
        final double c = constant;
        final double y = gramma;
        float rRed = (float)(red / 255.0);
        float rGreen = (float)(green / 255.0);
        float rBlue = (float)(blue / 255.0);

        double sRed = (float) c * Math.pow(rRed,y) * 255;
        double sGreen = (float) c * Math.pow(rGreen,y) * 255;
        double sBlue = (float) c * Math.pow(rBlue,y) * 255;

        if (sRed < 0){ sRed = 0; } if (sRed > 255){ sRed = 255; }
        if (sGreen < 0){ sGreen = 0; }if (sGreen > 255){ sGreen = 255; }
        if (sBlue < 0){ sBlue = 0; }if (sBlue > 255){ sBlue = 255; }

        list.add((int)sRed);
        list.add((int)sGreen);
        list.add((int)sBlue);
        return list;
    }
    /*************************************************************************************/

    /*************************************************************************************/
    public int [] convertToBinaryArray(int number){
        int [] arrayBit = new int[8];
        for (int i = 0 ; i < 8 ; i++){
            arrayBit[i] = 0;
        }
        int i = 7;
        while (number > 0){
            arrayBit[i] = number%2;
            i--;
            number = number/2;
        }
        return arrayBit;
    }
    /*************************************************************************************/
    
    public int convertBinToDeg( int [] arrayBit){
        int newVal = 0;
        for (int i = 0 ; i < arrayBit.length ; i++){
            newVal += arrayBit[i]*Math.pow(2,(7-i));
        }
        return newVal;
    }
    /*************************************************************************************/
    
    
    public ArrayList<Integer> enhanceImageByBitPlaneSlicing(int red , int green , int blue , int indexBit) {

        ArrayList <Integer> list = new ArrayList<Integer>();

        // get array bit for each color
        int[] arrayBitRed = convertToBinaryArray(red);
        int[] arrayBitGreen = convertToBinaryArray(green);
        int[] arrayBitBlue = convertToBinaryArray(blue);

        if (arrayBitRed[7-indexBit] == 0){
            arrayBitRed[7-indexBit] = 1;
        }
        if (arrayBitGreen[7-indexBit] == 0){
            arrayBitGreen[7-indexBit] = 1;
        }
        if (arrayBitBlue[7-indexBit] == 0){
            arrayBitBlue[7-indexBit] = 1;
        }

        int newRed , newGreen , newBlue;
        newRed = convertBinToDeg(arrayBitRed);
        newGreen = convertBinToDeg(arrayBitGreen);
        newBlue = convertBinToDeg(arrayBitBlue);

        list.add(newRed);
        list.add(newGreen);
        list.add(newBlue);
        return list;

    }
    /*************************************************************************************/

    public int minValueofNeighbourhood(int [] array){
        int min = array[0];
        for (int i = 1 ; i < 8 ; i++){
            if(array[i] < min){
                min = array[i];
            }
        }
        return min;
    }
    public int maxValueofNeighbourhood(int [] array){
        int max = array[0];
        for (int i = 1 ; i < 8 ; i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }

    public void selectionSort(int [] array){
        int min;
        for (int i = 0 ; i < (array.length - 1) ; i++){
            min = i;
            for (int j = i+1 ; j < array.length ; j++){
                if (array[j] < array[min]){
                    min = j;
                }
            }
            int temp = array[i];
            array[i] = array[min];
            array[min] = temp;
        }
    }
    /*************************************************************************************/
    
    public void setupTempMatrixNew(BufferedImage image,int [][] tempMatrix, int heightTemp , int widthTemp){ // Correctly

    	int width = image.getWidth();
    	int height = image.getHeight();
    	int tempVal = 0;
    	
        for (int y = 0 ; y < heightTemp ; y++){
            for (int x = 0 ; x < widthTemp ; x++){
                tempMatrix[y][x] = 0;
            }
        }

        // TODO : Setup for corner < Correctly >
        for (int y = 0 ; y < heightTemp ; y++){
            for (int x = 0 ; x < widthTemp ; x++){

                if (x == 0 && y == 0){
                	tempVal = new Color(image.getRGB(0,0)).getRed();
                    tempMatrix[y][x] = tempVal;
                }
                if (y == 0 && x == widthTemp-1){
                	tempVal = new Color(image.getRGB(width-1,0)).getRed();
                    tempMatrix[y][x] = tempVal;
                }
                if( y == heightTemp-1 && x == widthTemp-1 ){
                	tempVal = new Color(image.getRGB(width-1,height-1)).getRed();
                    tempMatrix[y][x] = tempVal;
                }
                if( y == heightTemp-1 && x == 0 ){
                	tempVal = new Color(image.getRGB(0,height-1)).getRed();
                    tempMatrix[y][x] = tempVal;
                }

            }
        }
        // TODO : Setup for border
        for (int y = 0 ; y < heightTemp ; y++){
            for (int x = 0 ; x < widthTemp ; x++){

                if (y == 0){
                    if (x > 0 && x < widthTemp-1){
                    	tempVal = new Color(image.getRGB(x-1,0)).getRed();
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (x == 0){
                    if (y > 0 && y < heightTemp-1){
                    	tempVal = new Color(image.getRGB(0,y-1)).getRed();
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (y == (heightTemp-1)){
                    if(x > 0 && x < widthTemp-1){
                    	tempVal = new Color(image.getRGB(x-1,height-1)).getRed();
                        tempMatrix[y][x] = tempVal;
                    }
                }
                if (x == (widthTemp-1)){
                    if (y > 0 && y < heightTemp-1){
                    	tempVal = new Color(image.getRGB(width-1,y-1)).getRed();
                        tempMatrix[y][x] = tempVal;
                    }
                }

            }
        }

        // FOR INSIDE MATRIX NOT IS BORDER
        for (int y = 1 ; y < heightTemp-1 ; y++) {
        	for (int x = 1 ; x < widthTemp-1 ; x++) {
        		tempMatrix[y][x] = new Color(image.getRGB(x-1,y-1)).getRed();
        	}
        }
        
        // Show Test TempMatrix <Correctly>
//        for (int y = 0 ; y < heightTemp ; y++) {
//        	for (int x = 0 ; x < widthTemp ; x++) {
//        		System.out.print(tempMatrix[y][x] + " ");
//        	}System.out.print("\n");
//        }

    }


    public void setupTempMatrix(BufferedImage image, int [][] tempMatrix, int tempWidth , int tempHight){ // OLD Code

        for (int i = 0 ; i < tempWidth ; i++){
            for (int j = 0 ; j < tempHight ; j++){
                tempMatrix[i][j] = -1;
            }
        }
        int tempVal = 0;
        int width = image.getWidth();
        int height = image.getHeight();

        // TODO : Setup for corner
        for (int i = 0 ; i < tempWidth ; i++){
            for (int j = 0 ; j < tempHight ; j++){

                if (i == 0 && j == 0){
                    tempVal = new Color(image.getRGB(0,0)).getRed();
                    tempMatrix[i][j] = tempVal;
                }
                if (i == tempWidth-1 && j == 0){
                    tempVal = new Color(image.getRGB(width-1,0)).getRed();
                    tempMatrix[i][j] = tempVal;
                }
                if(i == tempWidth-1 && j == tempHight-1){
                    tempVal = new Color(image.getRGB(width-1,height-1)).getRed();
                    tempMatrix[i][j] = tempVal;
                }
                if(i == 0 && j == tempHight-1){
                    tempVal = new Color(image.getRGB(0,height-1)).getRed();
                    tempMatrix[i][j] = tempVal;
                }

            }
        }

        // TODO : Setup for border
        for (int i = 0 ; i < tempWidth ; i++){
            for (int j = 0 ; j < tempHight ; j++){

                if (i == 0){
                    if (j > 0 && j < tempHight-1){
                        tempVal = new Color(image.getRGB(0,j-1)).getRed();
                        tempMatrix[i][j] = tempVal;
                    }
                }
                if (j == 0){
                    if (i > 0 && i <tempWidth-1){
                        tempVal = new Color(image.getRGB(i-1,0)).getRed();
                        tempMatrix[i][j] = tempVal;
                    }
                }
                if (i == (tempWidth-1)){
                    if(j > 0 && j < tempHight-1){
                        tempVal = new Color(image.getRGB(width-1,j-1)).getRed();
                        tempMatrix[i][j] = tempVal;
                    }
                }
                if (j == (tempHight-1)){
                    if (i > 0 && i <tempWidth-1){
                        tempVal = new Color(image.getRGB(i-1,height-1)).getRed();
                        tempMatrix[i][j] = tempVal;
                    }
                }

            }
        }

//        if (i == 0 || j == 0 || i == (tempWidth-1) || j == (tempHight-1)){
        for (int i = 1 ; i < tempWidth-1 ; i++){
            for (int j = 1 ; j < tempHight-1 ; j++){
                tempMatrix[i][j] = new Color(image.getRGB(i-1,j-1)).getRed();
            }
        }

    }
    /*************************************************************************************/
    

    public int calculatorPointByFilterLaplacian(int a,int b,int c,int d,int e,int f,int g,int h,int i){
        int ret = 0;
        ret = (-1)*b + (-1)*d + (-1)*f + (-1)*h + (5*i);
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;

    }
    /*************************************************************************************/

    public int calculatorPointByFilterSobelStep1(int a,int b,int c,int d,int e,int f,int g,int h,int i){
        int ret = 0;
        ret = (-1)*a + (-2)*b + (-1)*c + 1*e + 2*f + 1*g;
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;
    }
    /*************************************************************************************/

    public int calculatorPointByFilterSobelStep2(int a,int b,int c,int d,int e,int f,int g,int h,int i){
        int ret = 0;
        ret = (-1)*a + 1*c + 2*d + 1*e + (-1)*g + (-2)*h;
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;
    }
    /*************************************************************************************/
    
    public int calculatorByPointDetection(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	int ret = 0;
    	ret = (-1)*a + (-1)*b + (-1)*c + (-1)*d + (-1)*e + (-1)*f + (-1)*g + (-1)*h + 8*i;
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;
    }
    /*************************************************************************************/
    
    public int calculatorByLineDetection(int a,int b,int c,int d,int e,int f,int g,int h,int i, String type) {
    	int ret = 0;
    	switch(type) {
	    	case "Horizontal":{
	    		ret = (-1)*a + (-1)*b + (-1)*c + (2*d) + (-1)*e + (-1)*f + (-1)*g + (2*h) + (2*i);
	            if (ret < 0){
	                ret = 0;
	            }
	            if (ret > 255){
	                ret = 255;
	            }
	    		break;
	    	}
	    	case "Vertival":{
	    		ret = (-1)*a + (2*b) + (-1)*c + (-1)*d + (-1)*e + (2*f) + (-1)*g + (-1)*h + (2*i);
	            if (ret < 0){
	                ret = 0;
	            }
	            if (ret > 255){
	                ret = 255;
	            }
	    		break;
	    	}
	    	case "+45":{
	    		ret = (-1)*a + (-1)*b + (2*c) + (-1)*d + (-1)*e + (-1)*f + (2*g) + (-1)*h + (2*i);
	            if (ret < 0){
	                ret = 0;
	            }
	            if (ret > 255){
	                ret = 255;
	            }
	    		break;
	    	}
	    	case "-45":{
	    		ret = (2*a) + (-1)*b + (-1)*c + (-1)*d + (2*e) + (-1)*f + (-1)*g + (-1)*h + (2*i);
	            if (ret < 0){
	                ret = 0;
	            }
	            if (ret > 255){
	                ret = 255;
	            }
	    		break;
	    	}
    	}
    	return ret;
    }

    /*************************************************************************************/
    public int calculatorEdgeDetectionPrewittHorizontal(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	int ret = 0;
    	ret = (-1)*a + (-1)*b + (-1)*c + 0*d + 1*e + 1*f + 1*g + 0*h + 0*i;
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;
    }
    public int calculatorEdgeDetectionPrewittVertival(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	int ret = 0;
    	ret = (-1)*a + 0*b + 1*c + 1*d + 1*e + 0*f + (-1)*g + (-1)*h + 0*i;
        if (ret < 0){
            ret = 0;
        }
        if (ret > 255){
            ret = 255;
        }
        return ret;
    }
    /*************************************************************************************/
    public boolean checkFit(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	
    	boolean check = false;
    	if (b == 255 && d == 255 && f == 255 && h == 255 && i == 255) {
    		check = true;
    	}
    	
    	if (a == 0 && b == 0 && c == 0 && d == 0 && e == 0 && f == 0 && g==0 && h == 0 && i == 0) {
    		check = false;
    	}
    	
    	if (b == 0 || d == 0 || f == 0 || h == 0 || i == 0) {
    		check = false;
    	}
    	return check;
    }
    /*************************************************************************************/
    public boolean checkHit(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	
    	boolean check = false;
    	if (b == 255 || d == 255 || f == 255 || h == 255 || i == 255) {
    		check = true;
    	}
    	
    	if (a == 0 && b == 0 && c == 0 && d == 0 && e == 0 && f == 0 && g==0 && h == 0 && i == 0) {
    		check = false;
    	}
    	return check;
    }
    /*************************************************************************************/
    public boolean checkFitSpeacial(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	
    	boolean check = false;
    	if (a == 255 && b == 255 && c == 255 && d == 255 && e == 255 && f == 255 && g == 255 && h == 255 && i == 255) {
    		check = true;
    	} else {
    		check = false;
    	}
    	return check;
    }
    /*************************************************************************************/
    public boolean checkHitSpeacial(int a,int b,int c,int d,int e,int f,int g,int h,int i) {
    	
    	boolean check = false;
    	if (a == 255 || b == 255 || c == 255 || d == 255 || e == 255 || f == 255 || g == 255 || h == 255 || i == 255) {
    		check = true;
    	} else {
    		check = false;
    	}
    	return check;
    }
    
    

}
