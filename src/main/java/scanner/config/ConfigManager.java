package scanner.config;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.ScannerMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = Path.of("config/" + ScannerMod.MODID);


    @Getter
    private static final Map<Block, Boolean> blockBooleanMap = new HashMap<>();
    @Getter
    private static final Map<Block, String> blockStringMap = new HashMap<>();

    @Getter
    @Setter
    private static boolean isXrayEnabled = true;

    @Getter
    @Setter
    private static int xrayRange = 50;

    @Getter
    @Setter
    private static int TICKS_TO_RENDER = 100;

    @Getter
    @Setter
    private static int HOLD_DURATION_MS = 100;


    static {
        load();
    }


    public static void load() {
        loadJson("blocks.json", ConfigManager::readBlockBoolean);
        loadJson("colors.json", ConfigManager::readBlockString);
    }

    public static void save() {
        saveJson("blocks.json", writeBlockBoolean());
        saveJson("colors.json", writeBlockString());
    }



    private static void readBlockBoolean(JsonObject json) {
        json.asMap().forEach((key, element) -> {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(key));
            if (block != Blocks.AIR) {
                blockBooleanMap.put(block, element.getAsBoolean());
            }
        });
    }

    private static JsonObject writeBlockBoolean() {
        JsonObject obj = new JsonObject();
        blockBooleanMap.forEach((block, value) -> {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            if (key != null) {
                obj.addProperty(key.toString(), value);
            }
        });
        return obj;
    }



    private static void readBlockString(JsonObject json) {
        json.asMap().forEach((key, element) -> {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(key));
            if (block != Blocks.AIR) {
                blockStringMap.put(block, element.getAsString());
            }
        });
    }

    private static JsonObject writeBlockString() {
        JsonObject obj = new JsonObject();
        blockStringMap.forEach((block, value) -> {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            if (key != null) {
                obj.addProperty(key.toString(), value);
            }
        });
        return obj;
    }



    private static void loadJson(String filename, java.util.function.Consumer<JsonObject> reader) {
        try {
            Path file = CONFIG_DIR.resolve(filename);
            if (Files.exists(file)) {
                try (Reader readerFile = Files.newBufferedReader(file)) {
                    JsonElement element = GSON.fromJson(readerFile, JsonElement.class);
                    if (element.isJsonObject()) {
                        reader.accept(element.getAsJsonObject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveJson(String filename, JsonObject object) {
        try {
            Path file = CONFIG_DIR.resolve(filename);
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(file)) {
                writer.write(GSON.toJson(object));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
