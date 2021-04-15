package com.company;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InnovatorMartianTest {

    @Test
    void getGeneticCode() {
        var innovator = new InnovatorMartian<>("name");
        assertEquals("name", innovator.getGeneticCode());

        var innovatorInteger = new InnovatorMartian<>(42);
        assertEquals(42, innovatorInteger.getGeneticCode());

        var list = new ArrayList<Integer>() {{
            add(1);
            add(2);
        }};
        var innovatorList = new InnovatorMartian<>(list);
        assertEquals(list, innovatorList.getGeneticCode());

        var code = new StringBuilder("aaa");
        var innovatorObject = new InnovatorMartian<>(code);
        assertEquals(code, innovatorObject.getGeneticCode());
    }

    @Test
    void getParent() {
        var innovator = new InnovatorMartian<>(1);
        assertNull(innovator.getParent());

        var innovatorChild = new InnovatorMartian<>(2);
        innovator.addChild(innovatorChild);
        assertEquals(innovatorChild.getParent(), innovator);
        var grandChild = new InnovatorMartian<>(4);
        innovatorChild.addChild(grandChild);

        var secondChild = new InnovatorMartian<>(3);
        innovator.addChild(secondChild);
        innovatorChild.setParent(secondChild);
        assertEquals(innovatorChild.getParent(), secondChild);
        assertEquals(grandChild.getParent(), innovatorChild);
    }

    @Test
    void setGeneticCode_String() {
        var innovatorMartianString = new InnovatorMartian<>("Abc");
        innovatorMartianString.setGeneticCode("newCode");
        assertEquals(innovatorMartianString.getGeneticCode(), "newCode");
    }

    @Test
    void setGeneticCode_Integer() {
        var innovatorMartianInteger = new InnovatorMartian<>(101);
        innovatorMartianInteger.setGeneticCode(21);
        assertEquals(innovatorMartianInteger.getGeneticCode(), 21);
    }

    @Test
    void setGeneticCode_Double() {
        var innovatorMartianDouble = new InnovatorMartian<>(-2.3);
        innovatorMartianDouble.setGeneticCode(0.1);
        assertEquals(innovatorMartianDouble.getGeneticCode(), 0.1);
    }

    @Test
    void addAndGetChildren() {
        var innovatorRoot = new InnovatorMartian<>("root");
        assertEquals(innovatorRoot.getChildren().size(), 0);

        var firstChildKate = new InnovatorMartian<>("Kate");
        var secondChildMartin = new InnovatorMartian<>("Martin");
        innovatorRoot.addChild(firstChildKate);
        innovatorRoot.addChild(secondChildMartin);
        assertEquals(innovatorRoot.getChildren().size(), 2);

        Collection<Martian<String>> list = new ArrayList<>() {{
            add(firstChildKate);
            add(secondChildMartin);
        }};
        assertArrayEquals(list.toArray(), innovatorRoot.getChildren().toArray());

        list = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            var martian = new InnovatorMartian<String>(String.valueOf(i));
            firstChildKate.addChild(martian);
            list.add(martian);
        }
        assertArrayEquals(list.toArray(), firstChildKate.getChildren().toArray());

        assertFalse(firstChildKate.addChild(innovatorRoot));
        assertTrue(firstChildKate.addChild(secondChildMartin));

        assertTrue(firstChildKate.getChildren().contains(secondChildMartin));
        assertFalse(innovatorRoot.getChildren().contains(secondChildMartin));
    }

    @Test
    void hasChildWithValue() {
        var innovator = new InnovatorMartian<>(1);
        assertFalse(innovator.hasChildWithValue(100));

        var innovatorChild = new InnovatorMartian<>(2);
        var innovatorGrandChild = new InnovatorMartian<>(3);
        innovatorChild.addChild(innovatorGrandChild);

        for (int i = 0; i < 100; ++i) {
            innovator.addChild(new InnovatorMartian<>(i));
            innovatorChild.addChild(new InnovatorMartian<>(i + 100));
        }
        assertTrue(innovator.hasChildWithValue(1));
        assertFalse(innovatorChild.hasChildWithValue(10));
        assertFalse(innovatorGrandChild.hasChildWithValue(0));
    }

    @Test
    void setParent() {
        var innovator = new InnovatorMartian<>(1);
        var innovatorChild = new InnovatorMartian<>(2);
        var innovatorDescendant = new InnovatorMartian<>(3);
        var innovatorDescendantChild = new InnovatorMartian<>(4);

        innovator.addChild(innovatorChild);
        innovatorDescendant.addChild(innovatorDescendantChild);
        innovatorChild.addChild(innovatorDescendant);

        assertFalse(innovatorChild.setParent(innovatorDescendant));
        assertFalse(innovatorChild.setParent(innovatorDescendantChild));
        assertTrue(innovatorDescendantChild.setParent(innovator));
        assertFalse(innovatorDescendant.hasChildWithValue(4));
        assertEquals(innovatorDescendantChild.getParent(), innovator);
    }

    @Test
    void getDescendants() {
        Random rnd = new Random();
        var actualDescendants = new ArrayList<InnovatorMartian<Integer>>();
        var root = new InnovatorMartian<>(0);
        var child = new InnovatorMartian<>(1000);
        root.addChild(child);
        actualDescendants.add(child);

        for (int i = 0; i < 10; ++i) {
            var martian = new InnovatorMartian<>(rnd.nextInt(100) + 1);
            root.addChild(martian);
            actualDescendants.add(martian);
        }

        for (int i = 0; i < 5; ++i) {
            var martian = new InnovatorMartian<>(rnd.nextInt(1001) + 101);
            child.addChild(martian);
            actualDescendants.add(martian);
        }

        for (var martian : actualDescendants) {
            assertTrue(root.getDescendants().contains(martian));
        }
    }

    @Test
    void hasDescendantWithValue() {
        var root = new InnovatorMartian<>("abc");
        var child = new InnovatorMartian<>("bcd");
        root.addChild(child);
        root.addChild(new InnovatorMartian<>("cde"));
        root.addChild(new InnovatorMartian<>("def"));
        for (int i = 100; i < 1000; ++i) {
            child.addChild(new InnovatorMartian<>(String.valueOf(i)));
        }
        assertTrue(root.hasDescendantWithValue(String.valueOf(150)));
        assertFalse(root.hasDescendantWithValue(String.valueOf(0)));
        assertTrue(root.hasDescendantWithValue("def"));
        root.removeChild(child);
        assertFalse(root.hasDescendantWithValue(String.valueOf(150)));
        assertTrue(root.hasDescendantWithValue("cde"));
    }

    @Test
    void removeChild() {
        var innovator = new InnovatorMartian<>("aaa");
        var innovatorChild = new InnovatorMartian<>("bbb");
        innovator.addChild(innovatorChild);
        innovator.addChild(new InnovatorMartian<>("c"));
        innovator.addChild(new InnovatorMartian<>("e"));
        innovatorChild.addChild(new InnovatorMartian<>("d"));
        var childToRemove = new InnovatorMartian<>("e");
        innovatorChild.addChild(childToRemove);

        assertFalse(innovator.removeChild(new InnovatorMartian<>("d")));
        assertTrue(innovatorChild.removeChild(childToRemove));
        assertFalse(innovatorChild.hasChildWithValue("e"));
        assertTrue(innovator.hasChildWithValue("e"));
    }

    @Test
    void setChildren() {
        var root = new InnovatorMartian<>(0);
        var child = new InnovatorMartian<>(1);
        assertTrue(root.addChild(child));

        var list = new ArrayList<Martian<Integer>>();
        for (int i = 0; i < 10; ++i) {
            var martian = new InnovatorMartian<>(i);
            list.add(martian);
            child.addChild(martian);
        }
        assertFalse(child.setChildren(new ArrayList<>() {{
            add(child);
        }}));

        var conservatorChild = new ConservatorMartian<>(child);
        assertFalse(root.setChildren(new ArrayList<>() {{
            add(conservatorChild);
        }}));

        assertFalse(child.setChildren(new ArrayList<>() {{
            add(root);
        }}));

        assertTrue(root.setChildren(list));
        assertEquals(child.getChildren().size(), 0);
        assertArrayEquals(root.getChildren().toArray(), list.toArray());
    }
}