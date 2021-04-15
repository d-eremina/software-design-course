package com.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ConservatorMartian<T> implements Martian<T> {
    private final T geneticCode;
    private final ConservatorMartian<T> parent;
    private final Collection<ConservatorMartian<T>> children;

    /**
     * Приватный конструктор для рекурсивного построения дерева консерваторов.
     *
     * @param innovatorMartian Инноватор, от которого создается объект.
     * @param parent           Родитель.
     */
    private ConservatorMartian(InnovatorMartian<T> innovatorMartian,
                               ConservatorMartian<T> parent) {
        this.geneticCode = innovatorMartian.getGeneticCode();
        this.parent = parent;
        this.children = new ArrayList<>();
        for (Martian<T> martian : innovatorMartian.getChildren()) {
            this.children.add(new ConservatorMartian<>((InnovatorMartian<T>) martian, this));
        }
    }

    /**
     * Публичный конструктор от новатора.
     *
     * @param innovatorMartian Новатор, с корнем в котором создается дерево консерватора.
     */
    public ConservatorMartian(InnovatorMartian<T> innovatorMartian) {
        this.geneticCode = innovatorMartian.getGeneticCode();
        this.parent = null;

        this.children = new ArrayList<>();
        for (Martian<T> martian : innovatorMartian.getChildren()) {
            this.children.add(new ConservatorMartian<>((InnovatorMartian<T>) martian, this));
        }
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
