package demo;

public class HasToString {
    private String value = "x";

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HasToString(" + value + ")";
    }
}
