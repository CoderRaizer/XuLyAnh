package mypackage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageProcessing {

	private static final int BINS = 256;

	public ImageProcessing() {

	}

	public void convertColorImageToGrayScaleImage(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		float avg = 0;
		int pixel = 0;

		for (int y = 0; y < height; y++) { // Row
			for (int x = 0; x < width; x++) { // Column

				pixel = image.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;

				avg = (float) (red * 0.299 + green * 0.587 + blue * 0.114);
				pixel = (alpha << 24) | ((int) avg << 16) | ((int) avg << 8) | (int) avg;
				image.setRGB(x, y, pixel);
			}
		}
	}


	public ArrayList<int[]> imageHistogram(BufferedImage inputImage) {

		ArrayList<int[]> hist = new ArrayList<int[]>();

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		int color = 0;

		int[] grayVector = new int[256];
		for (int i = 0; i < 256; i++) {
			grayVector[i] = 0;
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				color = new Color(inputImage.getRGB(x, y)).getRed();
																		
				grayVector[color]++;
			}
		}
		hist.add(grayVector);
		return hist;
	}


	public ArrayList<int[]> histogramLookUpTable(BufferedImage inputImage) {

		ArrayList<int[]> imageHist = imageHistogram(inputImage);
		ArrayList<int[]> imageLUT = new ArrayList<int[]>();
		int[] grayHistogram = new int[256];
		for (int i = 0; i < 256; i++)
			grayHistogram[i] = 0;

		long sumHist = 0;

		// Calculate the scale factor
		int area = inputImage.getWidth() * inputImage.getHeight();
		float Dm = 255;
		float scale_factor = (float) (Dm / area);

		for (int i = 0; i < 256; i++) {
			sumHist += imageHist.get(0)[i];
			int val = (int) (scale_factor * sumHist);
			if (val > 255) {
				grayHistogram[i] = 255;
			} else
				grayHistogram[i] = val;
		}
		imageLUT.add(grayHistogram);
		return imageLUT;
	}


	// TODO : Convert R, G, B, Alpha to standard 8 bit
	public int colorToRGB(int alpha, int red, int green, int blue) {
		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel; // 32 bit
	}


	public BufferedImage histogramEqualization(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha, red, green, blue;
		int newPixel = 0;
		// Get the Lookup table for histogram equalization
		ArrayList<int[]> histLUT = histogramLookUpTable(image);

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// Get pixels by R, G, B
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				/*-------------------------------------------------*/

				// TODO : Set new pixel values using the histogram lookup table
				red = histLUT.get(0)[red];
				green = histLUT.get(0)[green];
				blue = histLUT.get(0)[blue];
				// Return back to original format
				newPixel = colorToRGB(alpha, red, green, blue);
				// Set newPixel into image
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}


	public BufferedImage Logarithmic(BufferedImage image, float constant) {

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha, red, green, blue;
		int newPixel = 0;

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				/*-------------------------------------------------*/
				ArrayList<Integer> list = photoCal.enhanceImageByLogarit(red, green, blue, constant);
				newPixel = colorToRGB(alpha, list.get(0), list.get(1), list.get(2));
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}


	public BufferedImage Power(BufferedImage image, float constant, float gramma) { 
																					
		int width = image.getWidth();
		int height = image.getHeight();
		int alpha, red, green, blue;
		int newPixel = 0;

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				ArrayList<Integer> list = photoCal.enhanceImageByPower(red, green, blue, constant, gramma);
				newPixel = colorToRGB(alpha, list.get(0), list.get(1), list.get(2));
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}


	public BufferedImage BitPlaneSlicing(BufferedImage image, int indexBit) {
																				
		int width = image.getWidth();
		int height = image.getHeight();
		int alpha, red, green, blue;
		int newPixel = 0;

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// Get pixels by R, G, B
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				ArrayList<Integer> list = photoCal.enhanceImageByBitPlaneSlicing(red, green, blue, indexBit);
				newPixel = colorToRGB(alpha, list.get(0), list.get(1), list.get(2));
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	public BufferedImage Max(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha;
		int newPixel = 0;
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				/*-------------------------------------------------*/
				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int B = new Color(image.getRGB(x, y - 1)).getRed();
				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();
				int D = new Color(image.getRGB(x + 1, y)).getRed();
				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();
				int F = new Color(image.getRGB(x, y + 1)).getRed();
				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int H = new Color(image.getRGB(x - 1, y)).getRed();
				int center = new Color(image.getRGB(x, y)).getRed();

				int[] array = { center, A, B, C, D, E, F, G, H };
				int max = photoCal.maxValueofNeighbourhood(array);
				newPixel = colorToRGB(alpha, max, max, max);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	public BufferedImage Min(BufferedImage image) { 

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha;
		int newPixel = 0;
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int B = new Color(image.getRGB(x, y - 1)).getRed();
				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();
				int D = new Color(image.getRGB(x + 1, y)).getRed();
				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();
				int F = new Color(image.getRGB(x, y + 1)).getRed();
				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int H = new Color(image.getRGB(x - 1, y)).getRed();
				int center = new Color(image.getRGB(x, y)).getRed();

				int[] array = { center, A, B, C, D, E, F, G, H };
				int min = photoCal.minValueofNeighbourhood(array);
				newPixel = colorToRGB(alpha, min, min, min);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	
	public BufferedImage Average(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha;
		int newPixel = 0;

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int B = new Color(image.getRGB(x, y - 1)).getRed();
				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();
				int D = new Color(image.getRGB(x + 1, y)).getRed();
				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();
				int F = new Color(image.getRGB(x, y + 1)).getRed();
				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int H = new Color(image.getRGB(x - 1, y)).getRed();
				int center = new Color(image.getRGB(x, y)).getRed();

				int avg = (center + A + B + C + D + E + F + G + H) / 9;
				newPixel = colorToRGB(alpha, avg, avg, avg);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}


	public BufferedImage AverageWeighted(BufferedImage image) {

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();
		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel = 0;
		int alpha;
		int value;

		int[][] tempMatrix;
		int widthTemp = width + 2;
		int heightTemp = height + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		photoCal.setupTempMatrixNew(image, tempMatrix, widthTemp, heightTemp);

		for (int y = 1; y < (heightTemp - 2); y++) {
			for (int x = 1; x < (widthTemp - 2); x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();

				int A = tempMatrix[y - 1][x - 1];
				int B = tempMatrix[y - 1][x];
				int C = tempMatrix[y - 1][x + 1];
				int D = tempMatrix[y][x + 1];
				int E = tempMatrix[y + 1][x + 1];
				int F = tempMatrix[y + 1][x];
				int G = tempMatrix[y + 1][x - 1];
				int H = tempMatrix[y][x - 1];
				int center = tempMatrix[y][x];

				value = (A) / 16 + (B * 2) / 16 + (C) / 16 + (H * 2) / 16 + (center * 4) / 16 + (D * 2) / 16 + (G) / 16
						+ (F * 2) / 16 + (E) / 16;

				newPixel = colorToRGB(alpha, value, value, value);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}


	public BufferedImage Median(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		int alpha;
		int newPixel = 0;
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Photo photoCal = new Photo();

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {
				alpha = new Color(image.getRGB(x, y)).getAlpha();
				/*-------------------------------------------------*/
				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int B = new Color(image.getRGB(x, y - 1)).getRed();
				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();
				int D = new Color(image.getRGB(x + 1, y)).getRed();
				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();
				int F = new Color(image.getRGB(x, y + 1)).getRed();
				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int H = new Color(image.getRGB(x - 1, y)).getRed();
				int center = new Color(image.getRGB(x, y)).getRed();

				int[] array = { center, A, B, C, D, E, F, G, H };
				photoCal.selectionSort(array);
				int median = array[4];
				newPixel = colorToRGB(alpha, median, median, median);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}
	public BufferedImage paintBorderForImage(BufferedImage image) {
		BufferedImage imageBordered = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel = 0;
		int alpha, red, green, blue;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				newPixel = colorToRGB(alpha, red, green, blue);
				imageBordered.setRGB(x, y, newPixel);

				if (x <= 1 || y <= 1 || x >= (width - 2) || y >= (height - 2)) {
					newPixel = colorToRGB(255, 255, 200, 0);
					imageBordered.setRGB(x, y, newPixel);
				}

			}
		}
		return imageBordered;
	}

	public BufferedImage contourDuplication(BufferedImage image) {
		BufferedImage imageContoured = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel;
		int alpha, red, green, blue;

		int point = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				red = new Color(image.getRGB(x, y)).getRed();
				green = new Color(image.getRGB(x, y)).getGreen();
				blue = new Color(image.getRGB(x, y)).getBlue();
				newPixel = colorToRGB(alpha, red, green, blue);
				imageContoured.setRGB(x, y, newPixel);

				if (x == 0) {
					point = new Color(image.getRGB(x + 1, y)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (y == 0) {
					point = new Color(image.getRGB(x, y + 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (x == (width - 1)) {
					point = new Color(image.getRGB(x - 1, y)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (y == (height - 1)) {
					point = new Color(image.getRGB(x, y - 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}

				if ((x == 0 && y == 0)) {
					point = new Color(image.getRGB(x + 1, y + 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (x == width - 1 && y == height - 1) {
					point = new Color(image.getRGB(x - 1, y - 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (x == 0 && y == height - 1) {
					point = new Color(image.getRGB(x + 1, y - 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
				if (y == 0 && x == width - 1) {
					point = new Color(image.getRGB(x - 1, y + 1)).getRed();
					newPixel = colorToRGB(255, point, point, point);
					imageContoured.setRGB(x, y, newPixel);
				}
			}
		}
		return imageContoured;
	}


	public BufferedImage filterLaplacian(BufferedImage image) {
		Photo photoCal = new Photo();
		BufferedImage imageLaplacianed = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel = 0;
		int value;

		int[][] tempMatrix;
		int widthTemp = width + 2;
		int heightTemp = height + 2;
		tempMatrix = new int[widthTemp][heightTemp];
		photoCal.setupTempMatrix(image, tempMatrix, widthTemp, heightTemp);

		for (int i = 1; i < widthTemp - 1; i++) {
			for (int j = 1; j < heightTemp - 1; j++) {
				value = photoCal.calculatorPointByFilterLaplacian(tempMatrix[i - 1][j - 1], tempMatrix[i][j - 1],
						tempMatrix[i + 1][j - 1], tempMatrix[i + 1][j], tempMatrix[i + 1][j + 1], tempMatrix[i][j + 1],
						tempMatrix[i - 1][j + 1], tempMatrix[i - 1][j], tempMatrix[i][j]);
				newPixel = colorToRGB(100, value, value, value);
				imageLaplacianed.setRGB(i - 1, j - 1, newPixel);
			}
		}
		return imageLaplacianed;
	}


	public BufferedImage filterSobel(BufferedImage image) {
		Photo photoCal = new Photo();
		BufferedImage imageSobel = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel = 0;
		int value1;
		int value2;
		int value;

		int[][] tempMatrix;
		int widthTemp = width + 2;
		int heightTemp = height + 2;
		tempMatrix = new int[widthTemp][heightTemp];
		photoCal.setupTempMatrix(image, tempMatrix, widthTemp, heightTemp);

		for (int i = 1; i < widthTemp - 1; i++) {
			for (int j = 1; j < heightTemp - 1; j++) {
				value1 = photoCal.calculatorPointByFilterSobelStep1(tempMatrix[i - 1][j - 1],
						tempMatrix[i][j - 1], tempMatrix[i + 1][j - 1], tempMatrix[i + 1][j], tempMatrix[i + 1][j + 1],
						tempMatrix[i][j + 1], tempMatrix[i - 1][j + 1], tempMatrix[i - 1][j], tempMatrix[i][j]);
				value2 = photoCal.calculatorPointByFilterSobelStep2(tempMatrix[i - 1][j - 1],
						tempMatrix[i][j - 1], tempMatrix[i + 1][j - 1], tempMatrix[i + 1][j], tempMatrix[i + 1][j + 1],
						tempMatrix[i][j + 1], tempMatrix[i - 1][j + 1], tempMatrix[i - 1][j], tempMatrix[i][j]);
				value = (int) Math.sqrt(Math.pow(value1, 2) + Math.pow(value2, 2));
				newPixel = colorToRGB(255, value, value, value);
				imageSobel.setRGB(i - 1, j - 1, newPixel);
			}
		}
		return imageSobel;
	}


	public int calAverageGrayLevelOfImage(BufferedImage image) {

		int height = image.getHeight();
		int width = image.getWidth();

		int average = 0;
		int size = height * width;
		int sum = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sum += new Color(image.getRGB(x, y)).getRed();
			}
		}
		average = sum / size;
		System.out.println("Average T = " + average);
		return average;
	}

	public ArrayList<Integer> generateGroup_G(BufferedImage image, int T1) {

		System.err.println("T1 = " + T1);

		ArrayList<Integer> G = new ArrayList<>();
		ArrayList<Integer> G1 = new ArrayList<>();
		ArrayList<Integer> G2 = new ArrayList<>();

		int height = image.getHeight();
		int width = image.getWidth();

		int value = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				value = new Color(image.getRGB(x, y)).getRed();
				if (value > T1) {
					G1.add(value);
				} else {
					G2.add(value);
				}
			}
		}

		int sumG1 = 0;
		int sumG2 = 0;
		for (int x : G1) {
			sumG1 += x;
		}
		for (int y : G2) {
			sumG1 += y;
		}
		G.add(sumG1 / G1.size());// u1
		G.add(sumG2 / G2.size());// u2

		return G;

	}

	public int calculatorThresholding(BufferedImage image) {

		int T2;
		int T1 = calAverageGrayLevelOfImage(image);
		boolean check = false;
		ArrayList<Integer> G;
		while (check == false) {

			G = new ArrayList<>(generateGroup_G(image, T1));
			int μ1 = (int) G.get(0);
			int μ2 = (int) G.get(1);
			double temp = (μ1 + μ2) / 2;
			T2 = (int) Math.round(temp * 10) / 10;
			System.out.println("T2 = " + T2);

			if (T1 == T2) {
				check = true;
			} else {
				T1 = T2;
				G.clear();
			}
		}
		return T1;
	}

	public BufferedImage handleApplyFilterPointDetection(BufferedImage image) {
		Photo photoCal = new Photo();
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int newPixel = 0;

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();

				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();

				int B = new Color(image.getRGB(x, y - 1)).getRed();

				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();

				int D = new Color(image.getRGB(x - 1, y)).getRed();

				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();

				int F = new Color(image.getRGB(x, y + 1)).getRed();

				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();

				int H = new Color(image.getRGB(x - 1, y)).getRed();

				int center = new Color(image.getRGB(x, y)).getRed();

				int ret = photoCal.calculatorByPointDetection(A, B, C, D, E, F, G, H, center);
				newPixel = colorToRGB(alpha, ret, ret, ret);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	public BufferedImage pointDetection(BufferedImage image) {

		BufferedImage resultImage = handleApplyFilterPointDetection(image);

		int T = calculatorThresholding(image);

		int width = resultImage.getWidth();
		int height = resultImage.getHeight();

		int alpha = 0;
		int newPixel = 0;
		int color = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(resultImage.getRGB(x, y)).getAlpha();
				color = new Color(resultImage.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;

	}


	public BufferedImage handleApplyFilterLineDetection(BufferedImage image, String type) {

		Photo photoCal = new Photo();
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int newPixel = 0;

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();

				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();

				int B = new Color(image.getRGB(x, y - 1)).getRed();

				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();

				int D = new Color(image.getRGB(x - 1, y)).getRed();

				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();

				int F = new Color(image.getRGB(x, y + 1)).getRed();

				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();

				int H = new Color(image.getRGB(x - 1, y)).getRed();

				int center = new Color(image.getRGB(x, y)).getRed();

				int ret = photoCal.calculatorByLineDetection(A, B, C, D, E, F, G, H, center, type);
				newPixel = colorToRGB(alpha, ret, ret, ret);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	public BufferedImage lineDetection(BufferedImage image, String type) {

		BufferedImage resultImage = handleApplyFilterLineDetection(image, type);

		int T = calculatorThresholding(image);

		int width = resultImage.getWidth();
		int height = resultImage.getHeight();

		int alpha = 0;
		int newPixel = 0;
		int color = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(resultImage.getRGB(x, y)).getAlpha();
				color = new Color(resultImage.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	public BufferedImage edgeDetectionPreWitt(BufferedImage image) {

		Photo photoCal = new Photo();
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int width = image.getWidth();
		int height = image.getHeight();
		int newPixel = 0;
		int value1;
		int value2;
		int value;
		int alpha;

		int[][] tempMatrix;
		int heightTemp = height + 2;
		int widthTemp = width + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		photoCal.setupTempMatrixNew(image, tempMatrix, heightTemp, widthTemp);

		for (int y = 1; y < (height - 1); y++) {
			for (int x = 1; x < (width - 1); x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();

				// TODO : Now get matrix around pixel
				int A = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int B = new Color(image.getRGB(x, y - 1)).getRed();
				int C = new Color(image.getRGB(x + 1, y - 1)).getRed();
				int D = new Color(image.getRGB(x - 1, y)).getRed();
				int E = new Color(image.getRGB(x + 1, y + 1)).getRed();
				int F = new Color(image.getRGB(x, y + 1)).getRed();
				int G = new Color(image.getRGB(x - 1, y - 1)).getRed();
				int H = new Color(image.getRGB(x - 1, y)).getRed();
				int center = new Color(image.getRGB(x, y)).getRed();

				value1 = photoCal.calculatorEdgeDetectionPrewittHorizontal(A, B, C, D, E, F, G, H, center);
				value2 = photoCal.calculatorEdgeDetectionPrewittVertival(A, B, C, D, E, F, G, H, center);

				value = (int) Math.sqrt(Math.pow(value1, 2) + Math.pow(value2, 2));
				newPixel = colorToRGB(alpha, value, value, value);
				resultImage.setRGB(x, y, newPixel);
			}
		}
		int T = calculatorThresholding(image);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				alpha = new Color(resultImage.getRGB(x, y)).getAlpha();
				newPixel = new Color(resultImage.getRGB(x, y)).getRed();
				if (newPixel > T) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
					resultImage.setRGB(x, y, newPixel);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
					resultImage.setRGB(x, y, newPixel);
				}
			}
		}
		return resultImage;
	}


	public BufferedImage thresholdingImage(BufferedImage image) {

		convertColorImageToGrayScaleImage(image);

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		int T = calculatorThresholding(image);

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int color = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				color = new Color(image.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}

	/*************************************************************************************/

	public BufferedImage erosionMorphologyImage(BufferedImage image) {

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		convertColorImageToGrayScaleImage(image);
		int T = 110;

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int color = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				color = new Color(image.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 0, 0, 0);//
				} else {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}

		int[][] tempMatrix;
		int heightTemp = height + 2;
		int widthTemp = width + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		Photo photoCal = new Photo();
		photoCal.setupTempMatrixNew(resultImage, tempMatrix, heightTemp, widthTemp);

		for (int y = 1; y < (heightTemp - 1); y++) {
			for (int x = 1; x < (widthTemp - 1); x++) {

				alpha = tempMatrix[y][x];
				// TODO : Now get value of Color (take RED) neighbourhood of (x,y)

				int A = tempMatrix[y - 1][x - 1];
				int B = tempMatrix[y - 1][x];
				int C = tempMatrix[y - 1][x + 1];
				int D = tempMatrix[y][x + 1];
				int E = tempMatrix[y + 1][x + 1];
				int F = tempMatrix[y + 1][x];
				int G = tempMatrix[y + 1][x - 1];
				int H = tempMatrix[y][x - 1];
				int center = tempMatrix[y][x];

				boolean check = photoCal.checkFit(A, B, C, D, E, F, G, H, center);
				if (check == true) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
					resultImage.setRGB(x - 1, y - 1, newPixel);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
					resultImage.setRGB(x - 1, y - 1, newPixel);
				}
			}
		}
		return resultImage;
	}


	public BufferedImage dilationMorphologyImage(BufferedImage image) {

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		convertColorImageToGrayScaleImage(image);
		int T = 110;

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int color = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				color = new Color(image.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 0, 0, 0);//
				} else {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}

		int[][] tempMatrix;
		int heightTemp = height + 2;
		int widthTemp = width + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		Photo photoCal = new Photo();
		photoCal.setupTempMatrixNew(resultImage, tempMatrix, heightTemp, widthTemp);

		for (int y = 1; y < (heightTemp - 1); y++) {
			for (int x = 1; x < (widthTemp - 1); x++) {

				alpha = tempMatrix[y][x];

				int A = tempMatrix[y - 1][x - 1];
				int B = tempMatrix[y - 1][x];
				int C = tempMatrix[y - 1][x + 1];
				int D = tempMatrix[y][x + 1];
				int E = tempMatrix[y + 1][x + 1];
				int F = tempMatrix[y + 1][x];
				int G = tempMatrix[y + 1][x - 1];
				int H = tempMatrix[y][x - 1];
				int center = tempMatrix[y][x];

				boolean check = photoCal.checkHit(A, B, C, D, E, F, G, H, center);
				if (check == true) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
					resultImage.setRGB(x - 1, y - 1, newPixel);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
					resultImage.setRGB(x - 1, y - 1, newPixel);
				}
			}
		}
		return resultImage;
	}


	public void handleErosion(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int newPixel = 0;

		int[][] tempMatrix;
		int heightTemp = height + 2;
		int widthTemp = width + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		Photo photoCal = new Photo();
		photoCal.setupTempMatrixNew(image, tempMatrix, heightTemp, widthTemp);

		for (int y = 1; y < (heightTemp - 1); y++) {
			for (int x = 1; x < (widthTemp - 1); x++) {

				alpha = tempMatrix[y][x];
				// TODO : Now get value of Color (take RED) neighbourhood of (x,y)

				int A = tempMatrix[y - 1][x - 1];
				int B = tempMatrix[y - 1][x];
				int C = tempMatrix[y - 1][x + 1];
				int D = tempMatrix[y][x + 1];
				int E = tempMatrix[y + 1][x + 1];
				int F = tempMatrix[y + 1][x];
				int G = tempMatrix[y + 1][x - 1];
				int H = tempMatrix[y][x - 1];
				int center = tempMatrix[y][x];

				boolean check = photoCal.checkFitSpeacial(A, B, C, D, E, F, G, H, center);
				if (check == true) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
					image.setRGB(x - 1, y - 1, newPixel);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
					image.setRGB(x - 1, y - 1, newPixel);
				}
			}
		}

	}

	public void handleDilation(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int newPixel = 0;

		int[][] tempMatrix;
		int heightTemp = height + 2;
		int widthTemp = width + 2;
		tempMatrix = new int[heightTemp][widthTemp];
		Photo photoCal = new Photo();
		photoCal.setupTempMatrixNew(image, tempMatrix, heightTemp, widthTemp);

		for (int y = 1; y < (heightTemp - 1); y++) {
			for (int x = 1; x < (widthTemp - 1); x++) {

				alpha = tempMatrix[y][x];
				// TODO : Now get value of Color (take RED) neighbourhood of (x,y)

				int A = tempMatrix[y - 1][x - 1];
				int B = tempMatrix[y - 1][x];
				int C = tempMatrix[y - 1][x + 1];
				int D = tempMatrix[y][x + 1];
				int E = tempMatrix[y + 1][x + 1];
				int F = tempMatrix[y + 1][x];
				int G = tempMatrix[y + 1][x - 1];
				int H = tempMatrix[y][x - 1];
				int center = tempMatrix[y][x];

				boolean check = photoCal.checkHitSpeacial(A, B, C, D, E, F, G, H, center);
				if (check == true) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
					image.setRGB(x - 1, y - 1, newPixel);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
					image.setRGB(x - 1, y - 1, newPixel);
				}
			}
		}

	}

	public BufferedImage OpenMorphologyImage(BufferedImage image) {

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		convertColorImageToGrayScaleImage(image);
		int T = calculatorThresholding(image);

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int color = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				color = new Color(image.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		// Done segmentation image
		for (int i = 0; i < 12; i++) {
			handleErosion(resultImage);
		}
		for (int i = 0; i < 12; i++) {
			handleDilation(resultImage);
		}

		return resultImage;
	}
	
	public BufferedImage CloseMorphologyImage(BufferedImage image) {

		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		// To Morphology need Segmentation by Thresholding before
		convertColorImageToGrayScaleImage(image);
		int T = calculatorThresholding(image);
//		int T = 110;

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int color = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				color = new Color(image.getRGB(x, y)).getRed();

				if (color > T && color <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		// Done segmentation image
		for (int i = 0; i < 8; i++) {
			handleDilation(resultImage);
		}
		for (int i = 0; i < 8; i++) {
			handleErosion(resultImage);
		}
		return resultImage;
	}
	
	public BufferedImage GetBoundaryOfTheObject(BufferedImage image) {
		
		BufferedImage originImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		BufferedImage erosionImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		
		int maunen = 0;
		int mauvat = 255;
		
		
		convertColorImageToGrayScaleImage(image);
		int T = calculatorThresholding(image);

		int width = image.getWidth();
		int height = image.getHeight();

		int alpha = 0;
		int colorOrigin = 0;
		int colorErosioned = 0;
		int newPixel = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				colorOrigin = new Color(image.getRGB(x, y)).getRed();

				if (colorOrigin > T && colorOrigin <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				originImage.setRGB(x, y, newPixel);
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				alpha = new Color(image.getRGB(x, y)).getAlpha();
				colorErosioned = new Color(image.getRGB(x, y)).getRed();

				if (colorErosioned > T && colorErosioned <= 255) {
					newPixel = colorToRGB(alpha, 255, 255, 255);
				} else {
					newPixel = colorToRGB(alpha, 0, 0, 0);
				}
				erosionImage.setRGB(x, y, newPixel);
			}
		}
		
		
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				
				alpha = new Color(originImage.getRGB(x, y)).getAlpha();
				
				colorOrigin = new Color(originImage.getRGB(x, y)).getRed();
				colorErosioned = new Color(erosionImage.getRGB(x, y)).getRed();
				
				if (colorOrigin == colorErosioned) {
					newPixel = colorToRGB(alpha, maunen, maunen, maunen);
				} else {
					newPixel = colorToRGB(alpha, mauvat, mauvat, mauvat);
				}
				resultImage.setRGB(x, y, newPixel);
			}
		}
		return resultImage;
	}
	
	
	
	

}
