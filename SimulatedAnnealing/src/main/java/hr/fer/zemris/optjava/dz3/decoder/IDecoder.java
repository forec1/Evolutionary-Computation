package hr.fer.zemris.optjava.dz3.decoder;

public interface IDecoder<T> {
	
	public double[] decode(T x);
	public void decode(T x, double[] d);

}
