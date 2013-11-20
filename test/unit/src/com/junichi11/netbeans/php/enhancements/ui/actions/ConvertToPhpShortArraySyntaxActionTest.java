/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package com.junichi11.netbeans.php.enhancements.ui.actions;

import java.io.File;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import static junit.framework.Assert.assertEquals;
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
