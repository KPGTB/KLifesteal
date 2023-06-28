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

package pl.kpgtb.klifesteal.listener;

import com.github.kpgtb.ktools.manager.listener.KListener;
import com.github.kpgtb.ktools.manager.ui.bar.BarManager;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kpgtb.klifesteal.item.SoulItem;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

public class DeathListener extends KListener {
    private final LifestealWrapper wrapper;

    public DeathListener(ToolsObjectWrapper toolsObjectWrapper) {
        super(toolsObjectWrapper);
        this.wrapper = (LifestealWrapper) toolsObjectWrapper;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        BarManager barManager = wrapper.getBarManager();
        barManager.setValue(
                wrapper.getLivesBar(),
                player,
                barManager.getValue(wrapper.getLivesBar(),player) - 1.0
        );

        Player killer = player.getKiller();
        if(killer == null) {
            return;
        }
        player.getWorld().dropItemNaturally(
                player.getLocation(),
                wrapper.getItemManager().getCustomItem(wrapper.getTag(), SoulItem.class)
        );

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(player.getName());
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);

        player.getWorld().dropItemNaturally(
                player.getLocation(),
                head
        );

        barManager.setValue(
                wrapper.getLivesBar(),
                killer,
                barManager.getValue(wrapper.getLivesBar(),killer) + 1.0
        );
    }
}
