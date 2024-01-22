package Phase3.JFX3D;

import java.util.Arrays;
import java.util.function.Consumer;

public class SIMD {
    public static void main(String[] args) {
        String[] strings = {"Hello", "there", "world"};

        doSIMD(strings, item -> System.out.println(item.toUpperCase()));

        System.out.println(Arrays.toString(strings));
    }

    public static <T> void doSIMD(T[] arr, Consumer<T> operation) {
        for (T item : arr) {
            operation.accept(item);
        }
    }
}