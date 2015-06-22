package utils;

/**
 * Created by hyx on 2015/6/6.
 */
@FunctionalInterface
public interface Predicate<E> {
    boolean test(E object);
}
