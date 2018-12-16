package hr.fer.zemris.optjava.dz8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz8.dataset.SantaFeLaserGeneratedDataSet;
import hr.fer.zemris.optjava.dz8.function.HyperbolicTangentTransferFunction;
import hr.fer.zemris.optjava.dz8.function.ITransferFunction;
import hr.fer.zemris.optjava.dz8.function.MeanSquaredError;
import hr.fer.zemris.optjava.dz8.nn.TDNN;

public class ANNTrainer {

	public static void main(String[] args) throws IOException{
		
		IReadOnlyDataSet dataset = loadData("./serija", 4, 500);
		TDNN tdnn = new TDNN(new int[] {4, 12, 1},
				new ITransferFunction[] {
						new HyperbolicTangentTransferFunction(), 
						new HyperbolicTangentTransferFunction(),
						new HyperbolicTangentTransferFunction()
//						,new HyperbolicTangentTransferFunction()
				},
				dataset);
		MeanSquaredError error = new MeanSquaredError(tdnn);
		
		

	}
	
	private static IReadOnlyDataSet loadData(String path, int l, int a) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		List<Double> data = new LinkedList<>();
		String line;
		while((line = br.readLine()) != null) {
			data.add(Double.parseDouble(line.trim()));
		}
		if(l < 1 || l >= a) {
			br.close();
			throw new IndexOutOfBoundsException("Parameter l can't be less than 1 or higher or equals than " + a + "!");
		}
		if(a == 0 || a < -1) {
			br.close();
			throw new IndexOutOfBoundsException("Parameter a can't be 0 or less than -1");
		}
		a = a > data.size() ? -1 : a;
		a = a == -1 ? data.size() : a;
		double[] normalizedData = normalize(data);
		SantaFeLaserGeneratedDataSet dataset = new SantaFeLaserGeneratedDataSet(a, l);
		for(int i = 0; i < a - l; i++) {
			dataset.addSample(Arrays.copyOfRange(normalizedData, i, i + l + 1));
		}
		br.close();
		return dataset;
	}
	
	/**
	 * Normalizes the data to the range (-1, +1) and returns them
	 * in a new list.
	 * @param data Data to be normalized.
	 * @return Normalized data.
	 */
	private static double[] normalize(List<Double> data) {
		double[] normalizedData = new double[data.size()];
		double max = data.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
		double min = data.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
		int i = 0;
		for(Double valueBeforeNormalization : data) {
			double valueAfterNormalization = 2 * ((valueBeforeNormalization - min) / (max - min)) - 1;
			normalizedData[i++] = valueAfterNormalization;
		}
		return normalizedData;
	}

}

















