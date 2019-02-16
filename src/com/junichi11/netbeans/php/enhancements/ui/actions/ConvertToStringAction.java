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

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.util.NbBundle;

/**
 *
 * @author junichi11
 */
@ActionID(
        category = "Edit",
        id = "com.junichi11.netbeans.php.enhancements.ui.actions.HtmlEntitiesToStringAction"
)
@ActionRegistration(
        iconBase = "com/junichi11/netbeans/php/enhancements/resources/entities2string16.png",
        displayName = "#CTL_HtmlEntitiesToStringAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 3500),
    @ActionReference(path = "Editors/text/html/Popup", position = 4400),
    @ActionReference(path = "Editors/Toolbars/Default", position = 250010)
})
@NbBundle.Messages("CTL_HtmlEntitiesToStringAction=Html Entities to String")
public class ConvertToStringAction extends ConvertToAction {

    public ConvertToStringAction(EditorCookie context) {
        super(context);
    }

    @Override
    public TYPE getType() {
        return TYPE.NAME_ENTITIES_2_STRING;
    }
}
