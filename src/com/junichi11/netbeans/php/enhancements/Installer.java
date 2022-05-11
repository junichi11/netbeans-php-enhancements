/*
 * Copyright 2022 junichi11.
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
package com.junichi11.netbeans.php.enhancements;

import com.junichi11.netbeans.php.enhancements.options.PHPEnhancementsOptions;
import java.util.prefs.Preferences;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Preferences preferences = NbPreferences.forModule(PHPEnhancementsOptions.class);
        preferences.addPreferenceChangeListener(evt -> {
            if (evt.getKey() == null) {
                return;
            }
            if (PHPEnhancementsOptions.isPHPEnhancementsOptions(evt.getKey())) {
                PHPEnhancementsOptions.getInstance().initialize(evt.getKey());
            }
        });
    }

}
