/*
 *    Copyright 2023 KPG-TB
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pl.kpgtb.klifesteal.util;

import com.github.kpgtb.ktools.manager.ui.bar.KBar;
import com.github.kpgtb.ktools.util.wrapper.ToolsInitializer;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;

public class LifestealWrapper extends ToolsObjectWrapper {
    private final KBar livesBar;

    public LifestealWrapper(ToolsInitializer initializer, KBar livesBar) {
        super(initializer);
        this.livesBar = livesBar;
    }

    public KBar getLivesBar() {
        return livesBar;
    }
}
