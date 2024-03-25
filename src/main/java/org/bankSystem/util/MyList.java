package org.bankSystem.util;

public interface MyList<T> {


    /**
     * Добавить элемент
     *
     * @param value
     */
    void add(T value);


    /**
     * добавить несколько элементов
     *
     * @param values
     */
    void addAll(T... values);


    /**
     * Возвращает количество элементов в массиве
     *
     * @return int - количество элементов в массиве
     */
    int size();

    //Поиск элемента по значению. Возвращает индекс первого вхождения.

    /**
     * Поиск элемента по значению
     *
     * @param value
     * @return int - индекс элемента
     */
    int indexOf(T value);


    /**
     * Поиск элемента по значению
     *
     * @param value
     * @return int -  индекс последнего вхождения
     */
    int lastIndexOf(T value);


    /**
     * Содержит ли массив элемент с таким значением
     *
     * @param value
     * @return boolean
     */
    boolean contains(T value);


    /**
     * Возвращает представление элементов в виде массива
     *
     * @return Array - представление элементов в виде массива
     */
    T[] toArray();


    /**
     * Удаляет один элемент по значению
     *
     * @param value
     * @return boolean. True - в случае успешного удаления.
     */
    boolean remove(T value);


    /**
     * Удаляет элемент по индексу.
     *
     * @param index
     * @return - Возвращает значение удаленного элемента
     */
    T remove(int index);


    /**
     * Является ли коллекция пустой
     *
     * @return boolean. Если элементов в коллекции нет - вернет true
     */
    boolean isEmpty();


    /**
     * Получить значение по индексу
     *
     * @param index
     * @return value
     */
    T get(int index);


    /**
     * Перезаписывает значение по указанному индексу
     *
     * @param index
     * @param value
     */
    void set(int index, T value);


}