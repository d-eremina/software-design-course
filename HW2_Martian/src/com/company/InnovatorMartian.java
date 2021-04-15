package com.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class InnovatorMartian<T> implements Martian<T> {
    private T geneticCode;
    private InnovatorMartian<T> parent;
    private Collection<InnovatorMartian<T>> children;

    /**
     * Конструктор для создания объекта.
     *
     * @param geneticCode Генетический код вершины.
     */
    public InnovatorMartian(T geneticCode) {
        this.geneticCode = geneticCode;
        parent = null;
        children = new ArrayList<>();
    }

    /**
     * Метод для установки нового генетического кода.
     *
     * @param newCode Новый генетический код вершины.
     */
    public void setGeneticCode(T newCode) {
        this.geneticCode = newCode;
    }

    /**
     * Метод для установки нового родителя для данной вершины.
     *
     * @param newParent Новый родитель вершины.
     * @return True/False в зависимости от успеха операции.
     */
    public boolean setParent(InnovatorMartian<T> newParent) {
        // Проверка, появятся ли циклы/петли
        if (this.hasDescendent(newParent) || this.equals(newParent)) {
            return false;
        }
        // Удаляем у текущего родителя данного ребенка
        this.parent.removeChild(this);
        // Меняем родителя и добавляем ему ребенка
        this.parent = newParent;
        parent.addChild(this);
        return true;
    }

    /**
     * Метод для присваивания новых детей данной вершине.
     *
     * @param newChildren Коллекцция новых детей.
     * @return True/False в зависимости от успеха операции.
     */
    public boolean setChildren(Collection<Martian<T>> newChildren) {
        // Сначала производится проверка на циклы, петли и типы передаваемых детей
        for (Martian<T> martian : newChildren) {
            if (this.equals(martian) || martian instanceof ConservatorMartian) {
                return false;
            }
            if (((InnovatorMartian<T>) martian).hasDescendent(this)) {
                return false;
            }
        }
        // Если все проверки пройдены, необходимо убрать текущих детей
        for (InnovatorMartian<T> martian : children) {
            martian.parent = null;
        }
        children = new ArrayList<>();
        // И далее добавить всех новых
        for (Martian<T> martian : newChildren) {
            this.addChild((InnovatorMartian<T>) martian);
        }
        return true;
    }

    /**
     * Метод для проверки наличия потомка по ссылке.
     *
     * @param child Потомок, которого нужно найти.
     * @return True/False в зависимости от результата поиска.
     */
    private boolean hasDescendent(Martian<T> child) {
        return getDescendants().contains(child);
    }

    /**
     * Метод для добавления ребенка в текущую вершину.
     *
     * @param child Ребенок, которого необходимо добавить.
     * @return True/False в зависимости от успеха вставки.
     */
    public boolean addChild(InnovatorMartian<T> child) {
        // Проверяем на наличие циклов и петель
        if (this.equals(child) || child.hasDescendent(this)) {
            return false;
        }
        // Если все хорошо, добавляем в список
        children.add(child);
        // Если это был чужой ребенок, нужно удалить его у старого родителя
        if (child.parent != null) {
            child.parent.removeChild(child);
        }
        // Меняяем на нового родителя
        child.parent = this;
        return true;
    }

    /**
     * Метод для удаления ребенка у текущей вершины.
     *
     * @param child Ребенок, которого необходимо удалить.
     * @return True/False в зависимости от успеха удаления.
     */
    public boolean removeChild(InnovatorMartian<T> child) {
        if (children.contains(child)) {
            // Убираем ссылку на данного родителя у ребенка
            child.parent = null;
            // Удаляем из списка
            children.remove(child);
            return true;
        }
        // Если такого ребенка нет у родителя
        return false;
    }

    /**
     * Метод для получения генетического кода марсианина.
     *
     * @return Генетический код марсианина.
     */
    @Override
    public T getGeneticCode() {
        return geneticCode;
    }

    /**
     * Метод для получения родителя текущего марсианина.
     *
     * @return Родитель данного марсианина.
     */
    @Override
    public Martian<T> getParent() {
        return parent;
    }

    /**
     * Метод для получения коллекции детей текущего марсианина.
     *
     * @return Незименяемая коллекция детей марсианина.
     */
    @Override
    public Collection<Martian<T>> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Метод для получения коллекции всех потомков текущего марсианина.
     *
     * @return Незименяемая коллекция всех потомков марсианина.
     */
    @Override
    public Collection<Martian<T>> getDescendants() {
        ArrayList<Martian<T>> descendants = new ArrayList<>();
        // Рекурсивный спуск в каждого из детей марсианина
        for (Martian<T> martian : children) {
            descendants.add(martian);
            // Потомки добавляются рекурсивно
            descendants.addAll(martian.getDescendants());
        }
        return Collections.unmodifiableCollection(descendants);
    }

    /**
     * Метод для проверки наличия ребенка с данным генетическим кодом.
     *
     * @param value Код для поиска.
     * @return True/False в зависимости от наличия такого кода у детей.
     */
    @Override
    public Boolean hasChildWithValue(T value) {
        for (Martian<T> martian : children) {
            if (martian.getGeneticCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод для проверки наличия потомка с данным генетическим кодом.
     *
     * @param value Код для поиска.
     * @return True/False в зависимости от наличия такого кода у потомков.
     */
    @Override
    public Boolean hasDescendantWithValue(T value) {
        for (Martian<T> martian : this.getDescendants()) {
            if (martian.getGeneticCode().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
