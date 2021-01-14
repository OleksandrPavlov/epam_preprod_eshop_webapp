package com.epam.preprod.pavlov.filter.link;

/**
 * This interface provides method for interacting with filter link.
 *
 * @param <T> This is type of returned filter work result.
 * @param <P> This is type of input parameter
 * @author Pavlov Aleksandr
 */
public interface FilterLink<T, P> {
    /**
     * When this method invoked, it checkout if there any filter behind it and if not, it passes baton to the next
     * filter. But if there no one filter more behind, it execute his own job and returns
     * result to invoking method.
     *
     * @param request parameter that will be processed
     * @return result of calculating of T type
     */
    T doFilter(P request);

    /**
     * THis method links next method with invoking filter.
     *
     * @param filterLink filter that will be linked to invoking.
     */
    void setNext(FilterLink filterLink);
}
