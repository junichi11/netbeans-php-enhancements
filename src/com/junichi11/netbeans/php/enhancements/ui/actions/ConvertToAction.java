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
            NbDocument.runAtomic(document, () -> {
                try {
                    document.remove(selectionStartPosition, selectedText.length());
                    document.insertString(selectionStartPosition, convertedString, null);
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
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
