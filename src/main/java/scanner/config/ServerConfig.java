package scanner.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerConfig {
    private static final Path CONFIG_PATH = Path.of("config/scanner/config.json");

    public static XraySettings load() {
        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createDirectories(CONFIG_PATH.getParent());
                XraySettings defaults = new XraySettings();
                try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(defaults, writer);
                }
                return defaults;
            } catch (IOException e) {
                e.printStackTrace();
                return new XraySettings();
            }
        }
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, XraySettings.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new XraySettings();
        }
    }

    public static class XraySettings {
        public boolean isXrayEnabled = false;
        public int xrayRange = 50;
        public int TICKS_TO_RENDER = 200;
        public int HOLD_DURATION_MS = 1000;
    }
}
