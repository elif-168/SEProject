/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xwpf.usermodel;

import static org.apache.poi.xwpf.XWPFTestDataSamples.writeOutAndReadBack;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipArchiveFakeEntry;
import org.apache.poi.openxml4j.util.ZipInputStreamZipEntrySource;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.apache.poi.xwpf.usermodel.XWPFRun.FontCharRange;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;

class TestXWPFBugs {
    @Test
    void bug55802() throws Exception {
        String blabla =
                "Bir, iki, \u00fc\u00e7, d\u00f6rt, be\u015f,\n" +
                        "\nalt\u0131, yedi, sekiz, dokuz, on.\n" +
                        "\nK\u0131rm\u0131z\u0131 don,\n" +
                        "\ngel bizim bah\u00e7eye kon,\n" +
                        "\nsar\u0131 limon";
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFRun run = doc.createParagraph().createRun();

            for (String str : blabla.split("\n")) {
                run.setText(str);
                run.addBreak();
            }

            run.setFontFamily("Times New Roman");
            run.setFontSize(20);
            assertEquals("Times New Roman", run.getFontFamily());
            assertEquals("Times New Roman", run.getFontFamily(FontCharRange.cs));
            assertEquals("Times New Roman", run.getFontFamily(FontCharRange.eastAsia));
            assertEquals("Times New Roman", run.getFontFamily(FontCharRange.hAnsi));
            run.setFontFamily("Arial", FontCharRange.hAnsi);
            assertEquals("Arial", run.getFontFamily(FontCharRange.hAnsi));
        }
    }

    @Test
    void bug57312_NullPointException() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("57312.docx")) {
            assertNotNull(doc);

            for (IBodyElement bodyElement : doc.getBodyElements()) {
                BodyElementType elementType = bodyElement.getElementType();

                if (elementType == BodyElementType.PARAGRAPH) {
                    XWPFParagraph paragraph = (XWPFParagraph) bodyElement;

                    for (IRunElement iRunElem : paragraph.getIRuns()) {

                        if (iRunElem instanceof XWPFRun) {
                            XWPFRun runElement = (XWPFRun) iRunElem;

                            UnderlinePatterns underline = runElement.getUnderline();
                            assertNotNull(underline);

                            //System.out.println("Found: " + underline + ": " + runElement.getText(0));
                        }
                    }
                }
            }
        }
    }

    @Test
    void bug57495_getTableArrayInDoc() throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            //let's create a few tables for the test
            for (int i = 0; i < 3; i++) {
                doc.createTable(2, 2);
            }
            XWPFTable table = doc.getTableArray(0);
            assertNotNull(table);
            //let's check also that returns the correct table
            XWPFTable same = doc.getTables().get(0);
            assertEquals(table, same);
        }
    }

    @Test
    void bug57495_getParagraphArrayInTableCell() throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            //let's create a table for the test
            XWPFTable table = doc.createTable(2, 2);
            assertNotNull(table);
            XWPFParagraph p = table.getRow(0).getCell(0).getParagraphArray(0);
            assertNotNull(p);
            //let's check also that returns the correct paragraph
            XWPFParagraph same = table.getRow(0).getCell(0).getParagraphs().get(0);
            assertEquals(p, same);
        }
    }

    @Test
    void bug57495_convertPixelsToEMUs() {
        int pixels = 100;
        int expectedEMU = 952500;
        int result = Units.pixelToEMU(pixels);
        assertEquals(expectedEMU, result);
    }

    @Test
    void test56392() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("56392.docx")) {
            assertNotNull(doc);
        }
    }

    /**
     * Removing a run needs to remove it from both Runs and IRuns
     */
    @Test
    void test57829() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("sample.docx")) {
            assertNotNull(doc);
            assertEquals(3, doc.getParagraphs().size());

            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                paragraph.removeRun(0);
                assertNotNull(paragraph.getText());
            }
        }
    }

    /**
     * Removing a run needs to take into account position of run if paragraph contains hyperlink runs
     */
    @Test
    void test58618() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("58618.docx")) {
            XWPFParagraph para = (XWPFParagraph) doc.getBodyElements().get(0);
            assertNotNull(para);
            assertEquals("Some text  some hyper links link link and some text.....", para.getText());
            XWPFRun run = para.insertNewRun(para.getRuns().size());
            run.setText("New Text");
            assertEquals("Some text  some hyper links link link and some text.....New Text", para.getText());
            para.removeRun(para.getRuns().size() - 2);
            assertEquals("Some text  some hyper links link linkNew Text", para.getText());
        }
    }

    @Test
    void test59378() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("59378.docx")) {
            try (XWPFDocument doc2 = writeOutAndReadBack(doc)) {
                assertNotNull(doc2);
            }

            try (XWPFDocument docBack = writeOutAndReadBack(doc)) {
                assertNotNull(docBack);
            }
        }
    }

    @Test
    void test63788() throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {

            XWPFNumbering numbering = doc.createNumbering();

            for (int i = 10; i >= 0; i--) {
                addNumberingWithAbstractId(numbering, i);        //add numbers in reverse order
            }

            for (int i = 0; i <= 10; i++) {
                assertEquals(i, numbering.getAbstractNum(BigInteger.valueOf(i)).getAbstractNum().getAbstractNumId().longValue());
            }

            //attempt to remove item with numId 2
            assertTrue(numbering.removeAbstractNum(BigInteger.valueOf(2)));

            //adding one level to numbering with id 1
            numbering.getAbstractNum(BigInteger.valueOf(1)).getCTAbstractNum().addNewLvl();

            XWPFDocument docReloaded = writeOutAndReadBack(doc);
            XWPFNumbering numberingReloaded = docReloaded.getNumbering();

            for (int id = 0; id <= 10; id++) {
                XWPFAbstractNum abstractNum = numberingReloaded.getAbstractNum(BigInteger.valueOf(id));

                // we removed id "2", so this one should be empty, all others not
                if (id == 2) {
                    assertNull(abstractNum, "Failed for " + id);
                } else {
                    assertNotNull(abstractNum, "Failed for " + id);
                    assertEquals(id, abstractNum.getAbstractNum().getAbstractNumId().longValue());

                    // we added one level for numbering with id "1"
                    if (id == 1) {
                        assertEquals(1, abstractNum.getAbstractNum().getLvlList().size());
                    } else {
                        assertEquals(0, abstractNum.getAbstractNum().getLvlList().size());
                    }
                }
            }

            // removing the same again fails
            assertFalse(numberingReloaded.removeAbstractNum(BigInteger.valueOf(2)));

            // removing another one works
            assertTrue(numberingReloaded.removeAbstractNum(BigInteger.valueOf(4)));
        }
    }

    @Test
    public void test65099() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("65099.docx")) {
            XWPFStyles styles = doc.getStyles();
            assertNotNull(styles);

            XWPFStyle normal = styles.getStyle("Normal");
            assertNotNull(normal);

            XWPFStyle style1 = styles.getStyle("EdfTitre3Car");
            assertNotNull(style1);

            List<XWPFStyle> list = styles.getUsedStyleList(normal);
            assertNotNull(list);
            assertEquals(1, list.size());

            list = styles.getUsedStyleList(style1);
            assertNotNull(list);
            assertEquals(7, list.size());

            assertThrows(NullPointerException.class,
                    () -> styles.getUsedStyleList(null),
                    "Passing in 'null' triggers an exception");

            XWPFStyle style = doc.getStyles().getStyle("TableauGrille41");
            doc.getStyles().getUsedStyleList(style);
        }
    }

    @Test
    void bug65738() throws Exception {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("bug65738.docx")) {
            XWPFStyles styles = doc.getStyles();
            assertNotNull(styles);
            assertEquals(22, doc.getParagraphs().size());
        }
    }

    @Test
    void test66080() throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFNumbering numbering = doc.createNumbering();

            // Add abstract numbering with id 1
            addNumberingWithAbstractId(numbering, 1);

            // Add abstract numbering with auto-generated id
            numbering.addAbstractNum(new XWPFAbstractNum());

            // Check that all abstract numbering ids are unique
            long uniqueIdCount = numbering
                    .getAbstractNums().stream()
                    .map(e -> e.getCTAbstractNum().getAbstractNumId().intValue())
                    .distinct().count();
            assertEquals(numbering.getAbstractNums().size(), uniqueIdCount);
        }
    }

    @Test
    void testEditNumberings() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("NumberingWithOutOfOrderId.docx")) {
            XWPFNumbering numbering = doc.createNumbering();

            // Abstract numbering with id = 1 already exists in the file, and has 9 levels
            XWPFAbstractNum abstractNum1 = numbering.getAbstractNum(BigInteger.ONE);
            assertEquals(9, abstractNum1.getAbstractNum().getLvlList().size());
            // Remove all levels from this numbering
            while (abstractNum1.getAbstractNum().getLvlList().size() > 0) {
                abstractNum1.getAbstractNum().removeLvl(0);
            }

            // Add abstract numbering with id = 0
            addNumberingWithAbstractId(numbering, 0);

            // Add abstract numbering with auto-generated id, this should be 2
            numbering.addAbstractNum(new XWPFAbstractNum());

            // Add (id + 1) levels to each abstract numbering
            for (int id = 0; id < 3; id++) {
                XWPFAbstractNum num = numbering.getAbstractNum(BigInteger.valueOf(id));
                for (int j = 0; j < id + 1; j++) {
                    num.getAbstractNum().addNewLvl();
                }
            }

            // Check that all levels added successfully
            for (int id = 0; id < 3; id++) {
                assertEquals(id + 1, numbering.getAbstractNum(BigInteger.valueOf(id)).getAbstractNum().getLvlList().size());
            }

            XWPFDocument docReloaded = writeOutAndReadBack(doc);
            XWPFNumbering numberingReloaded = docReloaded.getNumbering();

            // Check that all added levels persisted after document reload
            for (int id = 0; id < 3; id++) {
                assertEquals(id + 1, numberingReloaded.getAbstractNum(BigInteger.valueOf(id)).getAbstractNum().getLvlList().size());
            }
        }
    }

    private static void addNumberingWithAbstractId(XWPFNumbering documentNumbering, int id) {
        // create a numbering scheme
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
        // give the scheme an ID
        cTAbstractNum.setAbstractNumId(BigInteger.valueOf(id));

        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
        BigInteger abstractNumID = documentNumbering.addAbstractNum(abstractNum);

        documentNumbering.addNum(abstractNumID);
    }

    @Test
    void testBug69628() throws IOException {
        final int expectedParagraphs = 24;
        // bug69628.docx has -1 entry sizes in the zip data
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("bug69628.docx")) {
            assertEquals(expectedParagraphs, doc.getParagraphs().size());
        }
        // test again with smaller byte array max
        ZipArchiveFakeEntry.setMaxEntrySize(30 * 1024 * 1024);
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("bug69628.docx")) {
            assertEquals(expectedParagraphs, doc.getParagraphs().size());
        } finally {
            ZipArchiveFakeEntry.setMaxEntrySize(-1);
        }
        // test again with smaller byte array max
        IOUtils.setByteArrayMaxOverride(30 * 1024 * 1024);
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("bug69628.docx")) {
            assertEquals(expectedParagraphs, doc.getParagraphs().size());
        } finally {
            IOUtils.setByteArrayMaxOverride(-1);
        }
        // test again but temp files enabled
        ZipInputStreamZipEntrySource.setThresholdBytesForTempFiles(1000);
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("bug69628.docx")) {
            assertEquals(expectedParagraphs, doc.getParagraphs().size());
        } finally {
            ZipInputStreamZipEntrySource.setThresholdBytesForTempFiles(-1);
        }
    }
}
