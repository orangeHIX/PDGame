package utils;

/**
 * Created by hyx on 2015/6/6.
 */
@FunctionalInterface
public interface Predicate<E> {
    public boolean test(E object);
}
