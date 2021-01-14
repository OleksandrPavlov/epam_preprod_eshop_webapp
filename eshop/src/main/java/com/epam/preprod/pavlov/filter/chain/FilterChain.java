package com.epam.preprod.pavlov.filter.chain;

import com.epam.preprod.pavlov.filter.link.FilterLink;

import javax.servlet.http.HttpServletRequest;

/**
 * @param <L> Type of particular filter link
 * @param <R> Type of returned result of filer chain calculation
 * @author Pavlov Aleksandr
 * This interface provides basic methods to work with chain of filters.
 */
public interface FilterChain<L extends FilterLink, R> {
    /**
     * This method connects all links passed as a parameter, together.
     *
     * @param link that will be joined
     */
    void addLink(L link);

    /**
     * This method executes all chain. So as an input we set request parameter and after filter chain processing
     * we get result of R type in the output.
     *
     * @param request that will be on input
     * @return result R type on output
     */
    R doChain(HttpServletRequest request);
}
