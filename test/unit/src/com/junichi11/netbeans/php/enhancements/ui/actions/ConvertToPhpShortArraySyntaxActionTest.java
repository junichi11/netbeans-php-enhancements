/*
 * Copyright 2019 junichi11.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.junichi11.netbeans.php.enhancements.ui.actions;

import java.io.File;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.api.lexer.Language;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author junichi11
 */
public class ConvertToPhpShortArraySyntaxActionTest extends NbTestCase {

    private FileObject testFile;
    private final StringBuilder expected = new StringBuilder();
    private final StringBuilder target = new StringBuilder();

    public ConvertToPhpShortArraySyntaxActionTest(String name) {
        super(name);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    @Override
    public void setUp() {
    }

    @After
    @Override
    public void tearDown() {
        target.setLength(0);
        expected.setLength(0);
        testFile = null;
    }

    /**
     * Test of convert method, of class ConvertToPhpShortArraySyntaxAction.
     *
     * @throws javax.swing.text.BadLocationException
     * @throws java.io.IOException
     */
    @Test
    public void testConvert() throws BadLocationException, IOException {
        testFile("ArraySyntaxTest.php");
    }

    /**
     * Test of convert method, of class ConvertToPhpShortArraySyntaxAction.
     *
     * @throws javax.swing.text.BadLocationException
     * @throws java.io.IOException
     */
    @Test
    public void testConvertArrayCast() throws BadLocationException, IOException {
        testFile("ArraySyntaxCastTest.php");
    }

    /**
     * Test of convert method, of class ConvertToPhpShortArraySyntaxAction.
     *
     * @throws javax.swing.text.BadLocationException
     * @throws java.io.IOException
     */
    @Test
    public void testConvertArrayType() throws BadLocationException, IOException {
        testFile("ArraySyntaxTypeTest.php");
    }

    /**
     * Test of convert method, of class ConvertToPhpShortArraySyntaxAction.
     *
     * @throws javax.swing.text.BadLocationException
     * @throws java.io.IOException
     * @see https://github.com/junichi11/netbeans-php-enhancements/issues/4
     */
    @Test
    public void testIssue4() throws BadLocationException, IOException {
        testFile("ArraySyntaxIssue4Test.php");
    }

    private void testFile(String fileName) throws IOException, BadLocationException {
        File dataDir = getDataDir();
        testFile = FileUtil.toFileObject(new File(dataDir, fileName));
        boolean isExpected = false;
        for (String line : testFile.asLines("UTF-8")) {
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("// expected")) {
                isExpected = true;
                continue;
            }
            if (isExpected) {
                expected.append(line).append("\n");
            } else {
                target.append(line).append("\n");
            }
        }

        DefaultStyledDocument document = new DefaultStyledDocument();
        document.putProperty(Language.class, PHPTokenId.language());
        document.insertString(0, target.toString(), null);
        ConvertToPhpShortArraySyntaxAction instance = new ConvertToPhpShortArraySyntaxAction(null);
        instance.convert(document);
        assertEquals(expected.toString(), document.getText(0, document.getLength()));
    }

}
