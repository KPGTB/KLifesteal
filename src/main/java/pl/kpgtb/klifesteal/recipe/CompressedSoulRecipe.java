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

package pl.kpgtb.klifesteal.recipe;

import com.github.kpgtb.ktools.manager.recipe.KRecipe;
import com.github.kpgtb.ktools.util.wrapper.ToolsObjectWrapper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import pl.kpgtb.klifesteal.item.CompressedSoulItem;
import pl.kpgtb.klifesteal.item.SoulItem;
import pl.kpgtb.klifesteal.util.LifestealWrapper;

public class CompressedSoulRecipe extends KRecipe {
    private final NamespacedKey key;
    private final LifestealWrapper wrapper;

    public CompressedSoulRecipe(NamespacedKey recipeKey, ToolsObjectWrapper toolsObjectWrapper) {
        super(recipeKey, toolsObjectWrapper);

        this.key = recipeKey;
        this.wrapper = (LifestealWrapper) toolsObjectWrapper;
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(key,wrapper.getItemManager().getCustomItem(wrapper.getTag(), CompressedSoulItem.class));
        recipe.shape("sss", "sns", "sss");

        ItemStack soul = wrapper.getItemManager().getCustomItem(wrapper.getTag(), SoulItem.class);
        recipe.setIngredient('s', new RecipeChoice.ExactChoice(soul));
        recipe.setIngredient('n', Material.NETHER_STAR);

        return recipe;
    }
}
