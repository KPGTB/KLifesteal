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

package pl.kpgtb.klifesteal.item;

import com.github.kpgtb.ktools.manager.item.KItem;
import com.github.kpgtb.ktools.manager.language.LanguageLevel;
import com.github.kpgtb.ktools.util.item.ItemBuilder;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.kpgtb.klifesteal.gui.ReviveGui;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

public class HeartItem extends KItem {
    private final LifestealWrapper wrapper;

    public HeartItem(ToolsObjectWrapper wrapper, String fullItemTag) {
        super(wrapper, fullItemTag);
        this.wrapper = (LifestealWrapper) wrapper;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_NUGGET)
                .displayname(wrapper.getLanguageManager().getSingleString(LanguageLevel.PLUGIN, "heartName"))
                .lore(wrapper.getLanguageManager().getString(LanguageLevel.PLUGIN, "heartLore"))
                .model(2003)
                .build();
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double value = attributeInstance.getBaseValue();
        value += 2.0;
        if(value > wrapper.getConfig().getDouble("maxHP")) {
            wrapper.getLanguageManager().getComponent(LanguageLevel.PLUGIN, "maxHearts")
                    .forEach(c -> wrapper.getAdventure().player(player).sendMessage(c));
            return;
        }
        attributeInstance.setBaseValue(value);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f,1f);
        ItemStack item = event.getItem();
        item.setAmount(item.getAmount() - 1);
    }
}
