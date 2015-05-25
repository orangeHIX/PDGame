package utils;

public class Parameter<T> {

    public String name;
    public T value;

    public Parameter(String name, T value) {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + name + "=" + value;
    }
}
