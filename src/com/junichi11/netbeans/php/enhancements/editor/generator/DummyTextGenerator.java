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
package com.junichi11.netbeans.php.enhancements.editor.generator;

import com.junichi11.netbeans.php.enhancements.editor.generator.ui.DummyTextPanel;
import java.awt.Dialog;
import java.util.Collections;
import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.lib.editor.codetemplates.api.CodeTemplate;
import org.netbeans.lib.editor.codetemplates.api.CodeTemplateManager;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.netbeans.spi.editor.codegen.CodeGeneratorContextProvider;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class DummyTextGenerator implements CodeGenerator {

    JTextComponent textComp;

    /**
     *
     * @param context containing JTextComponent and possibly other items
     * registered by {@link CodeGeneratorContextProvider}
     */
    private DummyTextGenerator(Lookup context) { // Good practice is not to save Lookup outside ctor
        textComp = context.lookup(JTextComponent.class);
    }

    @MimeRegistrations({
        @MimeRegistration(mimeType = "text/x-php5", service = CodeGenerator.Factory.class),
        @MimeRegistration(mimeType = "text/html", service = CodeGenerator.Factory.class)
    })
    public static class Factory implements CodeGenerator.Factory {

        @Override
        public List<? extends CodeGenerator> create(Lookup context) {
            JTextComponent jtc = context.lookup(JTextComponent.class);
            if (jtc == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new DummyTextGenerator(context));
        }
    }

    /**
     * The name which will be inserted inside Insert Code dialog
     */
    @NbBundle.Messages("DummyTextGenerator.DisplayName=Dummy Text")
    @Override
    public String getDisplayName() {
        return Bundle.DummyTextGenerator_DisplayName();
    }

    /**
     * This will be invoked when user chooses this Generator from Insert Code
     * dialog
     */
    @Override
    public void invoke() {
        Document document = textComp.getDocument();
        if (document == null) {
            return;
        }

        // create dialog
        DummyTextPanel dummyTextPanel = DummyTextPanel.getDefault();
        DialogDescriptor descriptor = new DialogDescriptor(dummyTextPanel, Bundle.DummyTextGenerator_DisplayName());
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.dispose();

        if (descriptor.getValue() == DialogDescriptor.OK_OPTION) {
            // text for inserting
            String text = dummyTextPanel.getInsertText();
            if (text == null || text.isEmpty()) {
                return;
            }

            // insert code with CodeTemplateManager
            CodeTemplateManager templateManager = CodeTemplateManager.get(document);
            CodeTemplate codeTemplate = templateManager.createTemporary(text);
            codeTemplate.insert(textComp);
        }
    }
}
