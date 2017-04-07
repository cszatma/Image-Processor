import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class feedforward {
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			showMessage();
			return;
		}
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("numbers/" + args[0])); //UNCOMMENT WHEN IMPLEMENTING
			//img = ImageIO.read(new File("src/numbers/0.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Image file not found, please ensure the proper pathname was given");
			return;
		}
		
		//double[][] hiddenWeights = readInNumbers("src/hidden-weights.txt", 300, 785); //Get hidden weights
		//double[][] outputWeights = readInNumbers("src/output-weights.txt", 10, 301); //Get output weights
				
		double[][] hiddenWeights = readInNumbers("hidden-weights.txt", 300, 785); //Get hidden weights
		double[][] outputWeights = readInNumbers("output-weights.txt", 10, 301); //Get output weights
		
		//Get Pixel Data
		double[] dummy = null;
		double[] x = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), dummy); //Get all the pixels from img
		
//		for (int i = 0; i < x.length; i++) {
//			if (x[i] != 0) {
//				System.out.println(x[i]);
//			}
//		}
		
		//Rescale pixels to range between 0 to 1
		for (int i = 0; i < x.length; i++) {
			x[i] /= 255;
		}
		
		double[] hiddenLayer = getLayerOutput(x, hiddenWeights); //Compute results for hidden layer nodes
		double[] outputLayer = getLayerOutput(hiddenLayer, outputWeights); //Compute results for output layer nodes
		int result = getLargestElementIndex(outputLayer); //Get the index of the largest node
		
		System.out.printf("The network prediction is %d\n", result);
		
	}
	
	//Reads all the numbers from a given file and returns them in a 2D array
	public static double[][] readInNumbers(String fileName, int numOfRows, int numOfValuesPerRow) {
		Scanner in = null;
		try {
			in = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		double[][] nums = new double[numOfRows][numOfValuesPerRow];
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfValuesPerRow; j++) {
				nums[i][j] = in.nextDouble();
			}
		}
		
		in.close();
		return nums;
	}
	
	//Gets the nodes for the given layer
	public static double[] getLayerOutput(double[] inputs, double[][] weights) {
		if (inputs.length != weights[0].length - 1) {
			return null;
		}
		
		double[] outputs = new double[weights.length];
		for (int i = 0; i < weights.length; i++) {
			double result = 0;
			for (int j = 0; j < inputs.length; j++) {
				result += inputs[j] * weights[i][j];
			}
			result += weights[i][weights[i].length - 1];
			result = sigmoid(result);
			outputs[i] = result;
		}
		
		return outputs;
	}
	
	//Returns the result of the sigmoid function on a given value
	public static double sigmoid(double z) {
		return 1 / (1 + Math.pow(Math.E, -z));
	}
	
	//Returns the index of the largest element in an array
	public static int getLargestElementIndex(double[] array) {
		double max = array[0];
		int index = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				index = i;
			}
		}
		return index;
	}
	
	public static void showMessage() {
		System.out.println("Correct usage: java feedforward <image file name>");
	}
}