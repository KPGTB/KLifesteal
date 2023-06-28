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

package pl.kpgtb.klifesteal.gui;

import com.github.kpgtb.ktools.manager.gui.KGui;
import com.github.kpgtb.ktools.manager.gui.container.GuiContainer;
import com.github.kpgtb.ktools.manager.gui.container.PagedGuiContainer;
import com.github.kpgtb.ktools.manager.gui.item.GuiItem;
import com.github.kpgtb.ktools.manager.gui.item.common.CloseItem;
import com.github.kpgtb.ktools.manager.gui.item.common.LeftItem;
import com.github.kpgtb.ktools.manager.gui.item.common.RightItem;
import com.github.kpgtb.ktools.manager.language.LanguageLevel;
import com.github.kpgtb.ktools.util.item.ItemBuilder;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import com.j256.ormlite.dao.Dao;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kpgtb.klifesteal.data.Banned;
import pl.kpgtb.klifesteal.item.ReviveTotemItem;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReviveGui extends KGui {
    private final LifestealWrapper wrapper;
    private final Dao<Banned, UUID> bannedDAO;

    public ReviveGui(ToolsObjectWrapper tools) {
        super(tools.getLanguageManager().getSingleString(LanguageLevel.PLUGIN, "reviveGuiName"), 3, tools);

        this.wrapper = (LifestealWrapper) tools;
        this.bannedDAO = wrapper.getDataManager().getDao(Banned.class, UUID.class);
    }

    @Override
    public void prepareGui() {
        blockClick();
        resetContainers();

        PagedGuiContainer players = new PagedGuiContainer(this,0,0,9,2);
        try {
            players.fillWithItems(
                    bannedDAO.queryForAll().stream().map(banned -> {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(banned.getUuid());

                        if(op == null || op.getName() == null) {
                            return null;
                        }

                        ItemStack is = new ItemBuilder(Material.PLAYER_HEAD)
                                .displayname(wrapper.getLanguageManager().getSingleString(LanguageLevel.PLUGIN, "revivePlayerName", Placeholder.unparsed("player", op.getName())))
                                .lore(wrapper.getLanguageManager().getString(LanguageLevel.PLUGIN, "revivePlayerLore"))
                                .build();
                        SkullMeta meta = (SkullMeta) is.getItemMeta();
                        meta.setOwningPlayer(op);
                        is.setItemMeta(meta);

                        GuiItem item = new GuiItem(is);
                        item.setClickAction((e,place) -> {
                            ItemStack reviver = wrapper.getItemManager().getCustomItem(wrapper.getTag(), ReviveTotemItem.class);
                            Player player = (Player) e.getWhoClicked();
                            player.closeInventory();
                            if(!player.getInventory().containsAtLeast(reviver,1)) {
                                return;
                            }
                            player.getInventory().removeItem(reviver);
                            try {
                                bannedDAO.delete(banned);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            player.getWorld().strikeLightningEffect(player.getLocation());
                            wrapper.getLanguageManager().getComponent(LanguageLevel.PLUGIN, "unbanned", Placeholder.unparsed("player", op.getName()))
                                    .forEach(wrapper.getAdventure().all()::sendMessage);
                        });
                        return item;
                    }).filter(Objects::nonNull).collect(Collectors.toList())
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addContainer(players);

        GuiContainer manage = new GuiContainer(this,0,2,9,1);
        manage.setItem(0,0, LeftItem.get(wrapper,players));
        manage.setItem(4,0, CloseItem.get(wrapper));
        manage.setItem(8,0, RightItem.get(wrapper,players));
        addContainer(manage);
    }
}
