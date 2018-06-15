package com.demo.starwars.repository;

public class DataWrapper<T>
{
    public T data;
    public Throwable error; //or A message String, Or whatever

    public DataWrapper(T obj)
    {
        data = obj;
    }
    public DataWrapper(Throwable t)
    {
        error = t;
    }
}