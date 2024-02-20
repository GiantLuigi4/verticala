package tfc.verticala.util;

public class MathHelpers {
	public static int remEuclid(int input, int modulo) {
		int remainder = input % modulo;
		if(remainder < 0)
			remainder += modulo;
		return remainder;
	}
}
