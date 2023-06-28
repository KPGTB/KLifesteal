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

package pl.kpgtb.klifesteal.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

public class LivesPlaceholder extends PlaceholderExpansion {

    private final LifestealWrapper wrapper;

    public LivesPlaceholder(LifestealWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "klifesteal";
    }

    @Override
    public @NotNull String getAuthor() {
        return "KPG_TB";
    }

    @Override
    public @NotNull String getVersion() {
        return wrapper.getPlugin().getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        OfflinePlayer target = player;
        if(!params.isEmpty()) {
            target = Bukkit.getOfflinePlayer(params);
        }
        return String.valueOf(wrapper.getLivesBar().getSaveMethod().get(wrapper,wrapper.getLivesBar(),target));
    }
}
