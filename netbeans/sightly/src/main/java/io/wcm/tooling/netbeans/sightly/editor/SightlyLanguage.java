/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
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
 * #L%
 */
package io.wcm.tooling.netbeans.sightly.editor;

import io.wcm.tooling.netbeans.sightly.editor.hints.SightlyHintsProvider;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

@LanguageRegistration(mimeType = "text/html", useCustomEditorKit = true) //NOI18N
public class SightlyLanguage extends DefaultLanguageConfig {
    
    public SightlyLanguage() {
    }
   
    @Override
    public String getDisplayName() {
        return "Sightly";
    }
    @Override
    public HintsProvider getHintsProvider() {
        return new SightlyHintsProvider();
    }

    @Override
    public boolean hasHintsProvider() {
        return true;
    }

    @Override
    public Language getLexerLanguage() {
        return null;
    }
    
}
