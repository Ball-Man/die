package it.fmistri.dontdieplease.functional;

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
     * @return A java.util.Collection resulting from the given array and function
     *         (note that an array can't be returned due to generics limitations).
     */
    static <TInput, TOutput> Collection<TOutput> map(Function<TInput, TOutput> function,
                                           TInput... array) {
        Vector<TOutput> output = new Vector<TOutput>();
        for (TInput element : array)
            output.add(function.apply(element));

        return output;
    }
}
