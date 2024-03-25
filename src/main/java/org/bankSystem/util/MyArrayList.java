package org.bankSystem.util;


import java.lang.reflect.Array;

public class    MyArrayList<T> implements MyList<T> {

    private T[] array;
    private int cursor; // присвоено значение по умолчанию 0
    private static final int MULTIPLAYER = 2;


    @SuppressWarnings("unchecked") // подавляем предупреждение компилятора о непроверяемом приведении типа
    public MyArrayList() {
        array = (T[]) new Object[10];
    }

    // ++ Конструктор, принимающий в себя обычный массив и создающий RubberArray с такими де значениями
    @SuppressWarnings("unchecked")
    public MyArrayList(T[] values) { // проверить как поломается, если не изменить тип массива в параметрах конструктора
        array = (T[]) new Object[values.length * MULTIPLAYER];

        for (int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    // Добавлять в массив элемент


    @Override
    public void add(T value) {
        // Добавлять значение в наш массив

        // проверка. Есть ли место в нашем внутреннем массиве?
        if (cursor > array.length * 0.9) {
            expandArray(); // метод расширяющий наш массив
        }

        array[cursor++] = value;
    }

    //Добавлять в массив сразу несколько элементов
    @Override
    public void addAll(T... values) {
        // ints - я могу с ним обращаться точно так же как с ссылкой на массив

        for (int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }


    // Динамическое изменение размера массива
    @SuppressWarnings("unchecked")
    private void expandArray() {
        System.out.println("Расширяем массив! ================= " + cursor);
        // 1. создать новый массив бОльшего размера
        // 2. Переписать значения из старого массива в новый
        // 3. Перебросить ссылку

        T[] newArray = (T[]) new Object[array.length * MULTIPLAYER]; // создаю новый массив, в 2 раза больше предыдущего

        for (int i = 0; i < cursor; i++) {
            newArray[i] = array[i]; // Переписать значения из старого массива в новый
        }

        array = newArray; // 3. Перебросить ссылку. Переменная array теперь хранит ссылку на "новый" массив
        //System.out.println("Расширение массива завершено ==================\n");
    }

    //Вывод в консоль значений массива
    @Override
    public String toString() {
        if (cursor == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cursor; i++) {
            sb.append(array[i]).append((i < cursor - 1) ? ", " : "]");
        }
        return sb.toString();
    }

    // Текущее количество элементов в массиве
    @Override
    public int size() {
        return cursor;
    }

    //Возвращение значения по индексу
    @Override
    public T get(int index) {
        if (index >= 0 && index < cursor) {
            return array[index];
        } else {
            return null;
        }
    }

    @Override
    public void set(int index, T value) {
        // 1. Проверка индекса
        if (index < 0 || index > cursor - 1) return;

        array[index] = value;
    }

    // Поиск элемента по значению (у нас есть значение, надо узнать есть ли такое значение в массиве
    @Override
    public int indexOf(T value) {
        // так int, в качестве значение, а не индекса - проверять нам его не нужно
        for (int i = 0; i < cursor; i++) {
            if (array[i].equals(value)) return i; // здесь будут сравниваться ссылки
        }

        return -1;
    }

    @Override
    public int lastIndexOf(T value) {
        for (int i = cursor - 1; i >= 0; i--) {
            if (array[i].equals(value)) return i;
        }

        return -1;
    }

    @Override
    public boolean contains(T value) {
        return indexOf(value) >= 0; // если элемент есть - indexOf вернет какое-то положительное число
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray() {

        // 1. Создать массив нужной длинны и типа
        // 2. Переписать значения в новый массив
        // 3. Вернуть новый массив из метода

        if (cursor == 0) return (T[]) new Object[0];
        Class<T> clazz = (Class<T>) array[0].getClass();


        T[] res1 = (T[]) Array.newInstance(clazz, cursor);
        for (int i = 0; i < cursor; i++) {
            res1[i] = array[i];
        }

        return res1;

//        T[] result = (T[]) new Object[cursor]; // 1. Создаем массив длинной cursor(кол-во элементов во внутреннем массиве)
//        for (int i = 0; i < cursor; i++) {
//            result[i] = array[i];
//        }
//
//        System.arraycopy(array, 0, result, 0, cursor); // // - альтернативный вариант
//        return Arrays.copyOf(array, cursor); // - альтернативный вариант

    }

    public T[] toArray(Class<T> clazz) {

        // Рефлексия
        // 1. Создаем массив длинной cursor(кол-во элементов во внутреннем массиве)
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(clazz, cursor);
        for (int i = 0; i < cursor; i++) {
            result[i] = array[i];
        }

        System.out.println("========== instanceof: " + (result[0] instanceof Integer));
        return result; // Integer[]
    }

    //    Удаление элемента по индексу
    @Override
    public T remove(int index) {
        System.out.println("Remove by index");

        // 1. Проверка
        if (index < 0 && index >= cursor) {
            // такого индекса в массиве нет
            return null;
        }

        T value = array[index];

        // перезаписываем все значения, начиная с удаляемого индекса
        // значениями из соседней ячейки (справа)
        for (int i = index; i < cursor - 1; i++) {
            array[i] = array[i + 1];
        }

        cursor--;
        return value;
    }

    @Override
    public boolean remove(T value) {
        System.out.println("Remove by Value");
        // Понять есть ли такой элемент в массиве
        // Если нет - вернуть false и закончить работу методы
        // если есть - получить его индекс
        // переиспользовать метод, удаляющий по индексу.

        int index = indexOf(value);
        // Ctrl + Alt + <- вернуть курсор на предыдущую позицию

        if (index == -1) return false;

        remove(index);
        return true;

    }

    @Override
    public boolean isEmpty() {
        return cursor == 0;
    }


}

/*
+ 1. Динамическое изменение размера массива
+ 2. Добавлять в массив элемент
3. Добавлять в массив сразу несколько элементов
+ 4. Вывод в консоль значений массива
++ Текущее количество элементов в массиве
++ Возвращение значения по индексу
++ Конструктор, принимающий в себя обычный массив и создающий RubberArray с такими де значениями
++ Поиск элемента по значению
++ Удаление элемента по индексу
++ Удаление элемента по значению
Замена значения по индексу (есть индекс и новое значение)
Замена значения по значению (есть старое и новое значение)

 */

/*
В резиновом массиве должны быть реализованы методы:

 */