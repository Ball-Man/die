package it.fmistri.dontdieplease.functional;

import android.util.Pair;

import androidx.arch.core.util.Function;

import java.util.Collection;
import java.util.Vector;

/**
 * Static class that provides functional methods(e.g. map) for arrays. Necessary
 * because the java builtin requires API level 24(minimum for this project is 21).
 */
public class ArrayFunctional {
    /**
     * Get a collection obtained by applying a map function to all the elements of an array.
     * @param function The function to apply(the apply method shall be implemented, which accepts a
     *                 TInput and returns a TOutput.
     * @param array The array on which the function should be applied.
     * @param <TInput> The input type(type of the elements of the given array).
     * @param <TOutput> The output type(type of the elements of the returned collection).
     * @return A {@link java.util.Collection} resulting from the given array and function
     *         (note that an array can't be returned due to generics limitations).
     */
    public static <TInput, TOutput> Collection<TOutput> map(Function<TInput, TOutput> function,
                                           TInput... array) {
        Vector<TOutput> output = new Vector<TOutput>();
        for (TInput element : array)
            output.add(function.apply(element));

        return output;
    }

    /**
     * Find an element in an array which satisfies a predicate.
     * @param predicate The predicate (will be applied to all the elements of the input array until
     *                  any element satisfies it).
     * @param array The input array.
     * @param <TInput> The type of the elements.
     * @return A pair containing:
     *              - The first element of the input array which satisfies the given predicate
     *                (returned value for the predicate is true).
     *              - The integer index of that element in the array
     *         null if none of the elements satisfies the predicate.
     */
    public static <TInput> Pair<TInput, Integer> find(Function<TInput, Boolean> predicate,
                                     TInput... array) {
        for (int i = 0; i < array.length; i++) {
            TInput element = array[i];

            if (predicate.apply(element))
                return new Pair<>(element, i);
        }

        return null;
    }
}
