package com.acme.statusmgr.beans;

/**
 * Define interface to use for referencing Factories that create status decorators
 */
public interface DecoratorStyle {

    /**
     * The creator to be used for creating status decorators, whose nature and initial
     * contents will be based on the parameters provided to the factory.
     * @param detailtype            a String that indicates what decoration is needed
     * @param undecoratedStatus     the ServerStatus to be decorated
     * @return                      A decorator that is compatible with ServerStatus
     */
    ServerStatus createDecorator(String detailtype, ServerStatus undecoratedStatus);
}
