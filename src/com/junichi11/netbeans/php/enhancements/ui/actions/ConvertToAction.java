/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
 */
package com.junichi11.netbeans.php.enhancements.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringEscapeUtils;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.cookies.EditorCookie;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 * Convert to action. Text must be selected.
 *
 * @author junichi11
 */
abstract class ConvertToAction implements ActionListener {

    public enum TYPE {

        STRING_2_NAME_ENTITIES,
        NAME_ENTITIES_2_STRING
    }
    private final EditorCookie context;

    public ConvertToAction(EditorCookie context) {
        this.context = context;
    }

    public abstract TYPE getType();

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            JTextComponent editor = EditorRegistry.lastFocusedComponent();
            if (editor == null) {
                return;
            }
            final StyledDocument document = context.openDocument();
            if (editor.getDocument() != document) {
                return;
            }

            final String selectedText = editor.getSelectedText();
            if (selectedText == null || selectedText.isEmpty()) {
                return;
            }

            final String convertedString = convert(selectedText);
            if (selectedText.equals(convertedString)) {
                return;
            }

            final int selectionStartPosition = editor.getSelectionStart();
            NbDocument.runAtomic(document, new Runnable() {

                @Override
                public void run() {
                    try {
                        document.remove(selectionStartPosition, selectedText.length());
                        document.insertString(selectionStartPosition, convertedString, null);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private String convert(String selectedText) throws AssertionError {
        String convertedString;
        switch (getType()) {
            case STRING_2_NAME_ENTITIES:
                // escape (name entities)
                convertedString = StringEscapeUtils.escapeHtml(selectedText);
                break;
            case NAME_ENTITIES_2_STRING:
                convertedString = StringEscapeUtils.unescapeHtml(selectedText);
                break;
            default:
                throw new AssertionError();
        }
        return convertedString;
    }
}
