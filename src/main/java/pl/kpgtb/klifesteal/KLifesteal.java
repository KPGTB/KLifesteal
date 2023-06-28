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

package pl.kpgtb.klifesteal;

import com.github.kpgtb.ktools.manager.command.CommandManager;
import com.github.kpgtb.ktools.manager.data.DataManager;
import com.github.kpgtb.ktools.manager.item.ItemManager;
import com.github.kpgtb.ktools.manager.listener.ListenerManager;
import com.github.kpgtb.ktools.manager.recipe.RecipeManager;
import com.github.kpgtb.ktools.manager.resourcepack.ResourcePackManager;
import com.github.kpgtb.ktools.manager.ui.bar.BarIcons;
import com.github.kpgtb.ktools.manager.ui.bar.BarManager;
import com.github.kpgtb.ktools.manager.ui.bar.KBar;
import com.github.kpgtb.ktools.manager.ui.bar.save.ServerCacheMethod;
import com.github.kpgtb.ktools.util.bstats.Metrics;
import com.github.kpgtb.ktools.util.file.PackageUtil;
import com.github.kpgtb.ktools.util.wrapper.ToolsInitializer;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class KLifesteal extends JavaPlugin {

    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ToolsInitializer initializer = new ToolsInitializer(this)
                .prepareLanguage(getConfig().getString("lang"), "en");
        PackageUtil packageUtil = initializer.getPackageUtil();

        KBar bar = new KBar(
                "lives",
                new ServerCacheMethod(),
                getBarIcons(),
                156167,
                Math.min(120,getConfig().getInt("maxLives")),
                getConfig().getInt("defaultLives"),
                true,
                true,
                true
        );

        BarManager barManager = initializer.getGlobalManagersWrapper().getBarManager();
        barManager.registerPlugin(packageUtil.getTag(), getDescription().getVersion());
        barManager.registerBar(bar);

        ResourcePackManager resourcePack = initializer.getGlobalManagersWrapper().getResourcePackManager();
        resourcePack.setRequired(true);
        resourcePack.registerCustomModelData(packageUtil.getTag(),2000, "soul.png", getResource("assets/soul.png"), Material.IRON_NUGGET);
        resourcePack.registerCustomModelData(packageUtil.getTag(),2001, "csoul.png", getResource("assets/csoul.png"), Material.IRON_NUGGET);
        resourcePack.registerCustomModelData(packageUtil.getTag(),2002, "reviver.png", getResource("assets/reviver.png"), Material.IRON_NUGGET);

        LifestealWrapper wrapper = new LifestealWrapper(initializer,bar);
        adventure = wrapper.getAdventure();

        DataManager data = wrapper.getDataManager();
        data.registerTables(packageUtil.get("data"),getFile());

        ItemManager item = wrapper.getItemManager();
        item.registerItems(wrapper, getFile(), packageUtil.getTag(), packageUtil.get("item"));

        RecipeManager recipe = new RecipeManager(wrapper,getFile(),wrapper.getTag());
        recipe.registerRecipes(packageUtil.get("recipe"));

        CommandManager command = new CommandManager(wrapper,getFile(),wrapper.getTag());
        command.registerCommands(packageUtil.get("command"));

        ListenerManager listener = new ListenerManager(wrapper,getFile());
        listener.registerListeners(packageUtil.get("listener"));

        new Metrics(this, 18903);
    }

    @Override
    public void onDisable() {
        if(adventure != null) adventure.close();
    }

    private List<BarIcons> getBarIcons() {
        List<BarIcons> icons = new LinkedList<>();
        for (int i = 0; i < 12; i++) {
            double from = i*10.0;
            if(i > 0) from += 0.1;

            double to = (i+1) * 10.0;

            int full = i+1;
            int empty = i;

            icons.add(new BarIcons(
                    from,
                    to,
                    this,
                    "assets/h"+full+".png",
                    "assets/h"+empty+".png",
                    "assets/h"+empty+".png",
                    9,
                    10
            ));
        }
        return icons;
    }
}
