package it.fmistri.dontdieplease.db;

/**
 * Interface for categorized values. Used as interface for entries.
 */
public interface Categorized {

    /**
     * @return Get the value associated to the given category(retrieve the category with
     * {@link Categorized#getCategoryName}.
     */
    public double getValue();

    /**
     * @return The string name of the category.
     */
    public String getCategoryName();
}
