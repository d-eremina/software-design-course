package com.company;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ConservatorMartianTest {
    @Test
    void getGeneticCode() {
        var innovatorRoot = new InnovatorMartian<>(-1);
        for (int i = 0; i < 100; ++i) {
            innovatorRoot.addChild(new InnovatorMartian<>(i));
        }
        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        int i = 0;
        for (var item : conservatorRoot.getChildren()) {
            assertEquals(i, item.getGeneticCode());
            ++i;
        }
    }

    @Test
    void hasChildWithValue() {
        Random rnd = new Random();
        var innovatorRoot = new InnovatorMartian<>(-1);
        ArrayList<Integer> codes = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            int code = rnd.nextInt(1000) + 1;
            codes.add(code);
            innovatorRoot.addChild(new InnovatorMartian<>(code));
        }
        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        for (int i : codes) {
            assertTrue(conservatorRoot.hasChildWithValue(i));
        }
        for (int i = 1001; i < 2000; i += 10) {
            assertFalse(conservatorRoot.hasChildWithValue(i));
        }
    }

    @Test
    void getChildren() {
        var innovatorRoot = new InnovatorMartian<>(-1);
        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        assertArrayEquals(conservatorRoot.getChildren().toArray(), new ArrayList<>().toArray());

        var list = new ArrayList<ConservatorMartian<Integer>>();
        for (int i = 0; i < 100; ++i) {
            var martian = new InnovatorMartian<>(i);
            innovatorRoot.addChild(martian);
            list.add(new ConservatorMartian<>(martian));
        }
        conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        assertEquals(conservatorRoot.getChildren().size(), list.size());
        for (var martian : list) {
            assertTrue(conservatorRoot.hasChildWithValue(martian.getGeneticCode()));
        }
    }

    @Test
    void getParent() {
        var innovatorRoot = new InnovatorMartian<>(-1);
        var child = new InnovatorMartian<>(42);
        innovatorRoot.addChild(child);
        for (int i = 0; i < 10; ++i) {
            child.addChild(new InnovatorMartian<>(i));
        }

        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        assertNull(conservatorRoot.getParent());

        for (var martian : conservatorRoot.getChildren()) {
            assertEquals(martian.getParent(), conservatorRoot);
        }

        var secondRoot = new ConservatorMartian<>(child);
        for (var martian : secondRoot.getChildren()) {
            assertEquals(martian.getParent(), secondRoot);
        }
    }

    @Test
    void hasDescendantWithValue() {
        Random rnd = new Random();
        var innovatorRoot = new InnovatorMartian<>(-1);
        var child = new InnovatorMartian<>(-2);
        innovatorRoot.addChild(child);
        assertTrue(innovatorRoot.hasDescendantWithValue(-2));
        ArrayList<Integer> codes = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            int code = rnd.nextInt(1000) + 1;
            codes.add(code);
            child.addChild(new InnovatorMartian<>(code));
        }
        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        for (int i : codes) {
            assertTrue(conservatorRoot.hasDescendantWithValue(i));
            assertTrue(child.hasDescendantWithValue(i));
        }
        for (int i = 1001; i < 2000; i += 10) {
            assertFalse(conservatorRoot.hasDescendantWithValue(i));
            assertFalse(child.hasDescendantWithValue(i));
        }
    }

    @Test
    void getDescendants() {
        var innovatorRoot = new InnovatorMartian<>(-1);
        var child = new InnovatorMartian<>(-2);
        var conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        assertArrayEquals(conservatorRoot.getDescendants().toArray(), new ArrayList<>().toArray());
        innovatorRoot.addChild(child);
        var list = new ArrayList<ConservatorMartian<Integer>>();
        for (int i = 0; i < 100; ++i) {
            var martian = new InnovatorMartian<>(i);
            child.addChild(martian);
            list.add(new ConservatorMartian<>(martian));
        }
        conservatorRoot = new ConservatorMartian<>(innovatorRoot);
        assertEquals(conservatorRoot.getDescendants().size(), list.size() + 1);
        for (var martian : list) {
            assertTrue(conservatorRoot.hasDescendantWithValue(martian.getGeneticCode()));
        }
    }
}