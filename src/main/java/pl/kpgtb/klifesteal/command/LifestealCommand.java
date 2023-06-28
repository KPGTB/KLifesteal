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

package pl.kpgtb.klifesteal.command;

import com.github.kpgtb.ktools.manager.command.KCommand;
import com.github.kpgtb.ktools.manager.command.annotation.Description;
import com.github.kpgtb.ktools.manager.language.LanguageLevel;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

@Description("Manages lives in KLifesteal plugin")
public class LifestealCommand extends KCommand {
    private final LifestealWrapper wrapper;

    public LifestealCommand(ToolsObjectWrapper wrapper, String groupPath) {
        super(wrapper, groupPath);
        this.wrapper = (LifestealWrapper) wrapper;
    }

    @Description("Set lives of player")
    public void set(CommandSender sender, Player target, int value) {
        wrapper.getBarManager().setValue(wrapper.getLivesBar(),target,value);

        wrapper.getLanguageManager().getComponent(LanguageLevel.PLUGIN, "changedLives")
                .forEach(c -> wrapper.getAdventure().sender(sender).sendMessage(c));
        wrapper.getLanguageManager().getComponent(LanguageLevel.PLUGIN, "changedLivesTarget")
                .forEach(c -> wrapper.getAdventure().player(target).sendMessage(c));
    }

    @Description("Add lives to player")
    public void add(CommandSender sender, Player target, int value) {
        set(sender,target, (int) (wrapper.getBarManager().getValue(wrapper.getLivesBar(),target)+value));
    }

    @Description("Take lives from player")
    public void take(CommandSender sender, Player target, int value) {
        add(sender,target,-value);
    }
}
