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

import com.github.kpgtb.ktools.manager.language.LanguageLevel;
import com.github.kpgtb.ktools.manager.listener.KListener;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kpgtb.klifesteal.data.Banned;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

import java.sql.SQLException;
import java.util.UUID;

public class JoinListener extends KListener {
    private final LifestealWrapper wrapper;

    public JoinListener(ToolsObjectWrapper toolsObjectWrapper) {
        super(toolsObjectWrapper);
        this.wrapper = (LifestealWrapper) toolsObjectWrapper;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        if(!wrapper.getDataManager().getDao(Banned.class, UUID.class).idExists(player.getUniqueId())) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(String.join(
                        "\n",
                        wrapper.getLanguageManager().getString(LanguageLevel.PLUGIN, "banReason")
                ));
            }
        }.runTaskLater(wrapper.getPlugin(), 5L);
    }
}
