package drunkblood.horsecompass;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import drunkblood.horsecompass.datagen.HorseEntityTypeTagProvider;
import drunkblood.horsecompass.datagen.HorseModelProvider;
import drunkblood.horsecompass.datagen.HorseRecipeProvider;
import drunkblood.horsecompass.lang.HorseEnglishLanguageProvider;
import drunkblood.horsecompass.lang.HorseGermanLanguageProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(HorseCompass.MODID)
public class HorseCompass {
    public static final String MODID = "horsecompass";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredItem<HorseCompassItem> HORSE_COMPASS = ITEMS.registerItem("horse_compass", HorseCompassItem::new, new Item.Properties());
    public static final Supplier<DataComponentType<HorseTracker>> HORSE_TRACKER = DATA_COMPONENTS.registerComponentType("horse_tracker", builder ->
        builder.persistent(HorseTracker.CODEC)
            .networkSynchronized(HorseTracker.STREAM_CODEC)
        );
    public static final TagKey<EntityType<?>> TRACKABLE_BY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "trackable_by"));

    public HorseCompass(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::gatherData);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.addListener(this::onPlayerInteractEntity);
    }

    private void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event){
        if(event.getItemStack().getItem() == HORSE_COMPASS.get())
        {
            EntityType<?> targetType = event.getTarget().getType();
            if(!(targetType.is(TRACKABLE_BY))) return;
            Entity target = event.getTarget();
            ItemStack itemStack = event.getItemStack();
            if(target.hasCustomName()){
                String targetCustomName = ((PlainTextContents)target.getCustomName().getContents()).text();
                if(itemStack.has(DataComponents.CUSTOM_NAME)){
                    itemStack.remove(DataComponents.CUSTOM_NAME);
                }
                itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(targetCustomName));
            }
            HorseTracker tracker = new HorseTracker(target.getUUID());
            itemStack.set(HORSE_TRACKER, tracker);
            RandomSource random = target.getRandom();
            double vx = random.nextGaussian() * 0.02;
            double vy = random.nextGaussian() * 0.02;
            double vz = random.nextGaussian() * 0.02;
            target.level().addParticle(ParticleTypes.HEART, target.getRandomX(1.0), target.getRandomY() + 0.5, target.getRandomZ(1.0), vx, vy, vz);
            // prevent ridable entities to mount
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private void gatherData(GatherDataEvent.Client event){
        event.createProvider(HorseModelProvider::new);
        event.createProvider(HorseRecipeProvider.Runner::new);
        event.createProvider(HorseEnglishLanguageProvider::new);
        event.createProvider(HorseGermanLanguageProvider::new);
        event.createProvider(HorseEntityTypeTagProvider::new);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(HORSE_COMPASS);
        }
    }
}
