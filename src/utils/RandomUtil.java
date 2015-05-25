package utils;

import java.util.Random;

/**
 * 随机数包装类
 */
public class RandomUtil {

    public static Random random = new Random();

    public static int nextInt() {
        // TODO Auto-generated method stub
        return random.nextInt();
    }

    public static long nextLong() {
        // TODO Auto-generated method stub
        return random.nextLong();
    }

    public static boolean nextBoolean() {
        // TODO Auto-generated method stub
        return random.nextBoolean();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed {@code float} value
     * between {@code 0.0} and {@code 1.0} from this random number generator's
     * sequence.
     * <p/>
     * <p/>
     * The general contract of {@code nextFloat} is that one {@code float}
     * value, chosen (approximately) uniformly from the range {@code 0.0f}
     * (inclusive) to {@code 1.0f} (exclusive), is pseudorandomly generated and
     * returned. All 2<sup>24</sup> possible {@code float} values of the form
     * <i>m&nbsp;x&nbsp;</i>2<sup>-24</sup>, where <i>m</i> is a positive
     * integer less than 2<sup>24</sup>, are produced with (approximately) equal
     * probability.
     * <p/>
     * <p/>
     * The method {@code nextFloat} is implemented by class {@code Random} as if
     * by:
     * <p/>
     * <pre>
     * {@code
     * public float nextFloat() {
     *   return next(24) / ((float)(1 << 24));
     * }}
     * </pre>
     * <p/>
     * <p/>
     * The hedge "approximately" is used in the foregoing description only
     * because the next method is only approximately an unbiased source of
     * independently chosen bits. If it were a perfect source of randomly chosen
     * bits, then the algorithm shown would choose {@code float} values from the
     * stated range with perfect uniformity.
     * <p/>
     * [In early versions of Java, the result was incorrectly calculated as:
     * <p/>
     * <pre>
     * {@code
     *   return next(30) / ((float)(1 << 30));}
     * </pre>
     * <p/>
     * This might seem to be equivalent, if not better, but in fact it
     * introduced a slight nonuniformity because of the bias in the rounding of
     * floating-point numbers: it was slightly more likely that the low-order
     * bit of the significand would be 0 than that it would be 1.]
     *
     * @return the next pseudorandom, uniformly distributed {@code float} value
     * between {@code 0.0} and {@code 1.0} from this random number
     * generator's sequence
     */
    public static float nextFloat() {
        // TODO Auto-generated method stub
        return random.nextFloat();
    }

    public static double nextDouble() {
        // TODO Auto-generated method stub
        return random.nextDouble();
    }

}
