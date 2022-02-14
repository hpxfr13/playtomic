package com.playtomic.tests.wallet.service;

public interface IService<T> {
    public T charge(T t);
    public T refund(T t);
}
