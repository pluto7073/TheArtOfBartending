package ml.pluto7073.bartending.foundations.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.item.PourableBottleItem;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PouringRecipe implements Recipe<Inventory> {

    private final ResourceLocation id;
    private final AlcoholicDrink drink;
    private final float ounces;
    private final Ingredient glass;
    private final ItemStack result;

    public PouringRecipe(ResourceLocation id, AlcoholicDrink drink, float ounces, Ingredient glass, ItemStack result) {
        this.id = id;
        this.drink = drink;
        this.ounces = ounces;
        this.glass = glass;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, Level level) {
        ItemStack bottle = inventory.getSelected();
        if (!(bottle.getItem() instanceof PourableBottleItem pourable)) return false;
        if (!drink.equals(pourable.drink)) return false;
        if (bottle.isDamaged() && bottle.getMaxDamage() - bottle.getDamageValue() < ounces) return false;
        return glass.test(inventory.offhand.get(0));
    }

    public float ounces() {
        return ounces;
    }

    public AlcoholicDrink drink() { return drink; }

    public Ingredient glass() { return glass; }

    public ItemStack result() { return result; }

    @Override
    public ItemStack assemble(Inventory inventory, RegistryAccess registryAccess) {
        ItemStack result = this.result.copy();
        int dev = BrewingUtil.getAlcoholDeviation(inventory.getSelected());
        return BrewingUtil.setAlcoholDeviation(result, dev);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BartendingRecipes.POURING.serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return BartendingRecipes.POURING.type();
    }

    public static class Serializer implements RecipeSerializer<PouringRecipe> {

        @Override
        public PouringRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation alc = new ResourceLocation(GsonHelper.getAsString(json, "alcoholType"));
            AlcoholicDrink drink = AlcoholicDrinks.getOptional(alc).orElseThrow(() -> new JsonSyntaxException("Unknown Alcoholic Drink '" + alc + "'"));
            float ounces = GsonHelper.getAsFloat(json, "ounces");
            Ingredient glass = Ingredient.fromJson(json.get("glass"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new PouringRecipe(id, drink, ounces, glass, result);
        }

        @Override
        public PouringRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ResourceLocation alc = buf.readResourceLocation();
            AlcoholicDrink drink = AlcoholicDrinks.get(alc);
            float ounces = buf.readFloat();
            Ingredient glass = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return new PouringRecipe(id, drink, ounces, glass, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PouringRecipe recipe) {
            buf.writeResourceLocation(AlcoholicDrinks.getId(recipe.drink));
            buf.writeFloat(recipe.ounces);
            recipe.glass.toNetwork(buf);
            buf.writeItem(recipe.result);
        }
    }

}
