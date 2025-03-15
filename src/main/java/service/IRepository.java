package service;

import javafx.collections.ObservableList;


public interface IRepository <T> {

    public  void add(T obj);
    public  void update(T obj);
    public  void delete(T obj);
    public ObservableList<T> getAll();
    public T getById(long id);

}
