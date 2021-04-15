package com.company;

import java.security.InvalidParameterException;

public class MartianTree {
    // В дереве хранится только корень
    Martian<?> root;

    /**
     * Конструктор для создания дерева по корню.
     *
     * @param root Корень дерева.
     */
    public MartianTree(Martian<?> root) {
        this.root = root;
    }

    /**
     * Метод для преобразования строкового представления дерева в объект.
     *
     * @param report1 Строка, по которой строится дерево
     * @return Созданное дерево.
     * @throws InvalidParameterException В случае некорректных данных выбрасывается исключение.
     */
    public static MartianTree reportToTree(StringBuilder report1)
            throws InvalidParameterException {
        // Копируем данные, чтобы исходная строка не менялась
        var report = new StringBuilder(report1.toString());
        // Базовая проверка на корректность (условное число 10, так как тип не может быть короче)
        if (report.length() < 10) {
            throw new InvalidParameterException("Wrong input format: null instead of report");
        }
        // Если в конце строки нет переноса, добавляем, чтобы не выбросилось исключение
        if (report.lastIndexOf("\n") != report.length() - 1) {
            report.append("\n");
        }
        // По корню определяем тип марсианина, чтобы сравнивать далее
        String martianType = getMartianType(report.substring(0, report.indexOf(" ")));
        report.delete(0, report.indexOf(" ") + 2);
        // Тип значения
        String valueType = getValueType(report.substring(0, report.indexOf(":")));
        report.delete(0, report.indexOf(":") + 1);

        // В зависимости от значения строим одно из трех деревьев
        switch (valueType) {
            case "String":
                return getStringTree(report, martianType);
            case "Integer":
                return getIntegerTree(report, martianType);
            case "Double":
                return getDoubleTree(report, martianType);
            // Если тип не соответствует, возвращаем пустое дерево
            default:
                return new MartianTree(null);
        }
    }

    /**
     * Метод для получения дерева, кодами в котором являются строки.
     *
     * @param report      Строка, из которой формируется дерево.
     * @param martianType Тип марсиан.
     * @return Построенное дерево.
     * @throws InvalidParameterException Выбрасывается в случае некорректного представления.
     */
    private static MartianTree getStringTree(StringBuilder report, String martianType)
            throws InvalidParameterException {
        String code = report.substring(0, report.indexOf(")"));
        if (code.length() > 256) {
            throw new InvalidParameterException("Genetic code was too long");
        }
        if (report.indexOf("\n") - report.indexOf(")") > 1) {
            throw new InvalidParameterException("Wrong input format: no line after martian");
        }
        report.delete(0, report.indexOf("\n") + 1);
        InnovatorMartian<String> currentRoot = new InnovatorMartian<>(code);

        int spaces = 0;
        InnovatorMartian<String> currentParent = currentRoot;
        while (report.length() != 0) {
            int i = 0;
            // Removes spaces and counts them
            while (report.charAt(i) == ' ') {
                ++i;
            }
            // Удаляет лидирующие пробелы
            report.delete(0, i);
            // Парсим текущую вершину
            var currentString = new StringBuilder(report.substring(0, report.indexOf("\n")));
            if (currentString.length() < 10) {
                throw new InvalidParameterException("Wrong input format");
            }
            InnovatorMartian<String> currentNode =
                    parseStringMartian(currentString, martianType);
            // Удаляем строку
            report.delete(0, report.indexOf("\n") + 1);
            // Добавляем потомка
            checkSpaces(i, spaces);
            // Стало меньше или столько же пробелов - ищем разницу, столько раз поднимаемся
            for (int p = 0; p < (spaces - i) / 4 + 1; ++p) {
                currentParent = (InnovatorMartian<String>) currentParent.getParent();
            }
            if (currentParent == null) {
                throw new InvalidParameterException("Wrong input format");
            }
            currentParent.addChild(currentNode);
            currentParent = currentNode;
            spaces = i;
        }
        if (martianType.equals("ConservatorMartian")) {
            return new MartianTree(new ConservatorMartian<>(currentRoot));
        }
        return new MartianTree(currentRoot);
    }

    /**
     * Метод для получения дерева, кодами в котором являются целые числа.
     *
     * @param report      Строка, из которой формируется дерево.
     * @param martianType Тип марсиан.
     * @return Построенное дерево.
     * @throws InvalidParameterException Выбрасывается в случае некорректного представления.
     */
    private static MartianTree getIntegerTree(StringBuilder report, String martianType)
            throws InvalidParameterException {
        String code = report.substring(0, report.indexOf(")"));
        if (report.indexOf("\n") - report.indexOf(")") > 1) {
            throw new InvalidParameterException("Wrong input format: no line after martian");
        }
        report.delete(0, report.indexOf("\n") + 1);
        InnovatorMartian<Integer> currentRoot = new InnovatorMartian<>(Integer.parseInt(code));

        int spaces = 0;
        InnovatorMartian<Integer> currentParent = currentRoot;
        while (report.length() != 0) {
            int i = 0;
            while (report.charAt(i) == ' ') {
                ++i;
            }
            report.delete(0, i);
            InnovatorMartian<Integer> currentNode =
                    parseIntegerMartian(new StringBuilder(report.substring(0, report.indexOf("\n"))),
                            martianType);
            report.delete(0, report.indexOf("\n") + 1);
            checkSpaces(i, spaces);
            for (int p = 0; p < (spaces - i) / 4 + 1; ++p) {
                currentParent = (InnovatorMartian<Integer>) currentParent.getParent();
            }
            if (currentParent == null) {
                throw new InvalidParameterException("Wrong input format");
            }
            currentParent.addChild(currentNode);
            currentParent = currentNode;
            spaces = i;
        }
        if (martianType.equals("ConservatorMartian")) {
            return new MartianTree(new ConservatorMartian<>(currentRoot));
        }
        return new MartianTree(currentRoot);
    }

    /**
     * Метод для получения дерева, кодами в котором являются вещественные значения.
     *
     * @param report      Строка, из которой формируется дерево.
     * @param martianType Тип марсиан.
     * @return Построенное дерево.
     * @throws InvalidParameterException Выбрасывается в случае некорректного представления.
     */
    private static MartianTree getDoubleTree(StringBuilder report, String martianType)
            throws InvalidParameterException {
        String code = report.substring(0, report.indexOf(")"));
        if (report.indexOf("\n") - report.indexOf(")") > 1) {
            throw new InvalidParameterException("Wrong input format: no line after martian");
        }
        report.delete(0, report.indexOf("\n") + 1);
        InnovatorMartian<Double> currentRoot = new InnovatorMartian<>(Double.parseDouble(code));

        int spaces = 0;
        InnovatorMartian<Double> currentParent = currentRoot;
        while (report.length() != 0) {
            int i = 0;
            while (report.charAt(i) == ' ') {
                ++i;
            }
            report.delete(0, i);
            InnovatorMartian<Double> currentNode =
                    parseDoubleMartian(new StringBuilder(report.substring(0, report.indexOf("\n"))),
                            martianType);
            report.delete(0, report.indexOf("\n") + 1);
            checkSpaces(i, spaces);
            for (int p = 0; p < (spaces - i) / 4 + 1; ++p) {
                currentParent = (InnovatorMartian<Double>) currentParent.getParent();
            }
            if (currentParent == null) {
                throw new InvalidParameterException("Wrong input format");
            }
            currentParent.addChild(currentNode);
            currentParent = currentNode;
            spaces = i;
        }
        if (martianType.equals("ConservatorMartian")) {
            return new MartianTree(new ConservatorMartian<>(currentRoot));
        }
        return new MartianTree(currentRoot);
    }

    /**
     * Метод для проверки количества пробельных символов.
     *
     * @param lastSpaces Текущее количество пробелов.
     * @param spaces     Количество пробелов в предыдущей строке.
     * @throws InvalidParameterException Выбрасывается в случае некорректного представления.
     */
    private static void checkSpaces(int lastSpaces, int spaces) throws InvalidParameterException {
        // Если пробелов стало больше, их может быть только ровно 4, так как создаем ребенка
        if (lastSpaces > spaces && lastSpaces - spaces != 4) {
            throw new InvalidParameterException("Wrong input format");
        }
        // Если же их меньше или равно, но при этом разница не кратна четырем,
        // это тоже некорректный ввод
        if (lastSpaces <= spaces && (spaces - lastSpaces) % 4 != 0) {
            throw new InvalidParameterException("Wrong input format");
        }
    }

    /**
     * Метод для получения марсианина из текущей строки.
     * Генетический код - строка.
     *
     * @param report      Строка, из которой получается марсианин.
     * @param martianType Тип марсианина.
     * @return Возвращает созданного марсианина по строке.
     * @throws InvalidParameterException Выбрасывается в случае некорректного ввода.
     */
    private static InnovatorMartian<String> parseStringMartian(StringBuilder report, String martianType)
            throws InvalidParameterException {
        InnovatorMartian<String> currentNode;
        var code = getCode(report, martianType);
        // Проверка длины строки
        if (code.length() > 256) {
            throw new InvalidParameterException("Genetic code was too long");
        }
        currentNode = new InnovatorMartian<>(code);
        return currentNode;
    }

    /**
     * Метод для получения марсианина из текущей строки.
     * Генетический код - целое число.
     *
     * @param report      Строка, из которой получается марсианин.
     * @param martianType Тип марсианина.
     * @return Возвращает созданного марсианина по строке.
     * @throws InvalidParameterException Выбрасывается в случае некорректного ввода.
     */
    private static InnovatorMartian<Integer> parseIntegerMartian
    (StringBuilder report, String martianType) {
        InnovatorMartian<Integer> currentNode;
        currentNode = new InnovatorMartian<>(Integer.parseInt(getCode(report, martianType)));
        return currentNode;
    }

    /**
     * Метод для получения марсианина из текущей строки.
     * Генетический код - вещественное число.
     *
     * @param report      Строка, из которой получается марсианин.
     * @param martianType Тип марсианина.
     * @return Возвращает созданного марсианина по строке.
     * @throws InvalidParameterException Выбрасывается в случае некорректного ввода.
     */
    private static InnovatorMartian<Double> parseDoubleMartian
    (StringBuilder report, String martianType) {
        InnovatorMartian<Double> currentNode;
        currentNode = new InnovatorMartian<>(Double.parseDouble(getCode(report, martianType)));
        return currentNode;
    }

    /**
     * Метод для полученя генетического кода марсианина из текущей строки.
     *
     * @param report      Строка для выделения кода.
     * @param martianType Тип марсианина.
     * @return Полученную подстроку с кодом.
     * @throws InvalidParameterException В случае некорректного ввода.
     */
    private static String getCode(StringBuilder report, String martianType)
            throws InvalidParameterException {
        // Проверка, не конфликтуют ли типы в дереве
        if (!report.substring(0, report.indexOf(" ")).equals(martianType)) {
            throw new InvalidParameterException("Can't create tree from this string: " +
                    "different types in one tree");
        }
        // Удаляем тип марсианина
        report.delete(0, report.indexOf(" ") + 2);
        // Удаляем тип кода
        report.delete(0, report.indexOf(":") + 1);
        return report.substring(0, report.indexOf(")"));
    }

    /**
     * Метод для сравнения типа марсианина из данной строки.
     *
     * @param type Строка, которую нужно сравнить с типами.
     * @return Переданную строку, если она корректна.
     * @throws InvalidParameterException В случае, если тип марсианина некорректен.
     */
    private static String getMartianType(String type) throws InvalidParameterException {
        if (!(type.equals("InnovatorMartian") || type.equals("ConservatorMartian"))) {
            throw new InvalidParameterException("Can't create tree from this string: " +
                    "contains invalid type of martian");
        }
        return type;
    }

    /**
     * Метод для сравнения типа значения марсианина из данной строки.
     *
     * @param type Строка, которую нужно сравнить с типами.
     * @return Переданную строку, если она корректна.
     * @throws InvalidParameterException В случае, если тип значения у марсианина некорректен.
     */
    private static String getValueType(String type) throws InvalidParameterException {
        if (!(type.equals("String") || type.equals("Double") || type.equals("Integer"))) {
            throw new InvalidParameterException("Can't create tree from this string: " +
                    "code type not supported");
        }
        return type;
    }

    /**
     * Метод для перевода данного дерева в строковое представление.
     *
     * @return Строковое представление дерева.
     */
    public String treeToReport() {
        // Рекурсивно запускаем метод обхода
        return preOrder(root, 0);
    }

    /**
     * Метод для рекурсивного обхода дерева и построения его строкового представления.
     *
     * @param node  Вершина, из которой сейчас идет обход.
     * @param depth Глубина рекурсии.
     * @return Строковое представление дерева.
     */
    private String preOrder(Martian<?> node, Integer depth) {
        // Если вершина пустая, конец рекурсии
        if (node == null) {
            return "";
        }
        // Создаем новую строку и по заданному формату формируем ее.
        StringBuilder report = new StringBuilder("");
        report.append("    ".repeat(Math.max(0, depth)));
        report.append(node.getClass().getSimpleName())
                .append(" (")
                .append(node.getGeneticCode().getClass().getSimpleName())
                .append(":")
                .append(node.getGeneticCode())
                .append(")\n");
        // Рекурсивно добавляем всех детей вершины
        for (Martian<?> martian : node.getChildren()) {
            report.append(preOrder(martian, depth + 1));
        }
        // Возвращаем строковое представление
        return report.toString();
    }
}
