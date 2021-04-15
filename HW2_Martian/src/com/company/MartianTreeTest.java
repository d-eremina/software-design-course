package com.company;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MartianTreeTest {
    @Test
    void treeToReport() {
        var root = new InnovatorMartian<>(1);
        ArrayList<InnovatorMartian<Integer>> children = new ArrayList<>();
        var child1 = new InnovatorMartian<>(2);
        var child2 = new InnovatorMartian<>(3);
        var child3 = new InnovatorMartian<>(4);
        for (int i = 100; i < 103; ++i) {
            child1.addChild(new InnovatorMartian<>(i));
        }
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        var tree = new MartianTree(root);
        assertEquals("InnovatorMartian (Integer:1)\n" +
                "    InnovatorMartian (Integer:2)\n" +
                "        InnovatorMartian (Integer:100)\n" +
                "        InnovatorMartian (Integer:101)\n" +
                "        InnovatorMartian (Integer:102)\n" +
                "    InnovatorMartian (Integer:3)\n" +
                "    InnovatorMartian (Integer:4)\n", tree.treeToReport());
    }

    @Test
    void nullTreeToReport() {
        var tree = new MartianTree(null);
        assertEquals("", tree.treeToReport());
    }

    @Test
    void reportToTreeInvalidMartinException() {
        StringBuilder report = new StringBuilder("Martian (String:Mary)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(report));

        StringBuilder differentTypesReport = new StringBuilder("InnovatorMartian (String:Mary)\n" +
                "    ConservatorMartian (String:Jane)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(differentTypesReport));
    }

    @Test
    void reportToTreeSpacesExceptions() {
        StringBuilder noSpacesReport = new StringBuilder("InnovatorMartian (String:Mary)\n" +
                "InnovatorMartian (String:Jane)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(noSpacesReport));

        StringBuilder extraSpacesReport = new StringBuilder("InnovatorMartian (String:Mary)\n" +
                "     InnovatorMartian (String:Jane)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(extraSpacesReport));

        StringBuilder notEnoughSpacesReport = new StringBuilder("InnovatorMartian (String:Mary)\n" +
                "    InnovatorMartian (String:Jane)\n" +
                "        InnovatorMartian (String:Jake)\n" +
                "     InnovatorMartian (String:June)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(notEnoughSpacesReport));
    }

    @Test
    void reportLinesExceptions() {
        StringBuilder emptyLinesReport = new StringBuilder("InnovatorMartian (String:Mary)\n\n" +
                "    InnovatorMartian (String:Jane)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(emptyLinesReport));

        StringBuilder noLineReport = new StringBuilder("InnovatorMartian (String:Mary)" +
                "    InnovatorMartian (String:Jane)");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(noLineReport));

        StringBuilder nullReport = new StringBuilder("");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(nullReport));

        StringBuilder lineReport = new StringBuilder("\n");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(lineReport));
    }

    @Test
    void incorrectCodeExceptions() {
        StringBuilder incorrectIntegerReport = new StringBuilder("InnovatorMartian (Integer:a)\n");
        assertThrows(Exception.class, () -> MartianTree.reportToTree(incorrectIntegerReport));

        StringBuilder incorrectDoubleReport = new StringBuilder("InnovatorMartian (Double:-)\n");
        assertThrows(Exception.class, () -> MartianTree.reportToTree(incorrectDoubleReport));

        StringBuilder longStringReport = new StringBuilder("ConservatorMartian (String:");
        longStringReport.append("a".repeat(257));
        longStringReport.append(")\n");
        assertThrows(InvalidParameterException.class, () -> MartianTree.reportToTree(longStringReport));
    }

    @Test
    void stringTree() {
        StringBuilder innovatorReport = new StringBuilder("InnovatorMartian (String:Root)\n" +
                "    InnovatorMartian (String:Child1)\n" +
                "    InnovatorMartian (String:Child2)\n" +
                "        InnovatorMartian (String:GrandChild1)\n" +
                "        InnovatorMartian (String:GrandChild2)\n" +
                "    InnovatorMartian (String:Child3)\n");
        assertEquals((MartianTree.reportToTree(innovatorReport)).treeToReport(), innovatorReport.toString());

        StringBuilder conservatorReport = new StringBuilder("ConservatorMartian (String:Root)\n" +
                "    ConservatorMartian (String:Child1)\n" +
                "    ConservatorMartian (String:Child2)\n" +
                "        ConservatorMartian (String:GrandChild1)\n" +
                "        ConservatorMartian (String:GrandChild2)\n" +
                "    ConservatorMartian (String:Child3)\n");
        assertEquals((MartianTree.reportToTree(conservatorReport)).treeToReport(), conservatorReport.toString());
    }

    @Test
    void integerTree() {
        StringBuilder innovatorReport = new StringBuilder("InnovatorMartian (Integer:0)\n" +
                "    InnovatorMartian (Integer:1)\n" +
                "    InnovatorMartian (Integer:2)\n" +
                "        InnovatorMartian (Integer:3)\n" +
                "        InnovatorMartian (Integer:4)\n" +
                "    InnovatorMartian (Integer:5)\n");
        assertEquals((MartianTree.reportToTree(innovatorReport)).treeToReport(), innovatorReport.toString());

        StringBuilder conservatorReport = new StringBuilder("ConservatorMartian (Integer:0)\n" +
                "    ConservatorMartian (Integer:1)\n" +
                "    ConservatorMartian (Integer:2)\n" +
                "        ConservatorMartian (Integer:3)\n" +
                "        ConservatorMartian (Integer:4)\n" +
                "    ConservatorMartian (Integer:5)\n");
        assertEquals((MartianTree.reportToTree(conservatorReport)).treeToReport(), conservatorReport.toString());
    }

    @Test
    void doubleTree() {
        StringBuilder innovatorReport = new StringBuilder("InnovatorMartian (Double:0.0)\n" +
                "    InnovatorMartian (Double:0.1)\n" +
                "    InnovatorMartian (Double:0.2)\n" +
                "        InnovatorMartian (Double:0.3)\n" +
                "        InnovatorMartian (Double:0.4)\n" +
                "    InnovatorMartian (Double:0.5)\n");
        assertEquals((MartianTree.reportToTree(innovatorReport)).treeToReport(), innovatorReport.toString());

        StringBuilder conservatorReport = new StringBuilder("ConservatorMartian (Double:0.0)\n" +
                "    ConservatorMartian (Double:0.1)\n" +
                "    ConservatorMartian (Double:0.2)\n" +
                "        ConservatorMartian (Double:0.3)\n" +
                "        ConservatorMartian (Double:0.4)\n" +
                "    ConservatorMartian (Double:0.5)\n");
        assertEquals((MartianTree.reportToTree(conservatorReport)).treeToReport(), conservatorReport.toString());
    }
}