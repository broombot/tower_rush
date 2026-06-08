package gameLogic.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class LevelCatalog {

    private static final String RESOURCE_FOLDER = "/gameLogic/src/levels";
    private static final String FALLBACK_FOLDER = "src/gameLogic/src/levels";

    private LevelCatalog() {
    }

    public static List<LevelEntry> loadAvailableLevels() {
        List<java.nio.file.Path> levelFiles = discoverLevelFiles();
        List<LevelEntry> levels = new ArrayList<>();

        for (java.nio.file.Path levelFile : levelFiles) {
            String fileName = levelFile.getFileName().toString();
            if (!fileName.toLowerCase().endsWith(".csv")) {
                continue;
            }

            levels.add(new LevelEntry(
                    resolveDisplayName(levelFile),
                    RESOURCE_FOLDER + "/" + fileName));
        }

        levels.sort(Comparator.comparing(LevelEntry::getDisplayName, String.CASE_INSENSITIVE_ORDER));
        return levels;
    }

    private static List<java.nio.file.Path> discoverLevelFiles() {
        List<java.nio.file.Path> fromClasspath = discoverFromClasspath();
        if (!fromClasspath.isEmpty()) {
            return fromClasspath;
        }

        java.nio.file.Path fallbackDirectory = Paths.get(FALLBACK_FOLDER);
        if (!Files.isDirectory(fallbackDirectory)) {
            return List.of();
        }

        try (Stream<java.nio.file.Path> stream = Files.list(fallbackDirectory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } catch (IOException e) {
            System.err.println("Failed to read levels from " + FALLBACK_FOLDER + ": " + e.getMessage());
            return List.of();
        }
    }

    private static List<java.nio.file.Path> discoverFromClasspath() {
        try {
            URL resource = LevelCatalog.class.getResource(RESOURCE_FOLDER);
            if (resource == null) {
                return List.of();
            }

            java.nio.file.Path directory = Paths.get(resource.toURI());
            if (!Files.isDirectory(directory)) {
                return List.of();
            }

            try (Stream<java.nio.file.Path> stream = Files.list(directory)) {
                return stream
                        .filter(Files::isRegularFile)
                        .sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                        .toList();
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to read levels from classpath: " + e.getMessage());
            return List.of();
        }
    }

    private static String resolveDisplayName(java.nio.file.Path levelFile) {
        try (BufferedReader reader = Files.newBufferedReader(levelFile, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            if (firstLine != null && !firstLine.isBlank()) {
                String[] parts = firstLine.split(",", 2);
                if (parts.length > 0 && !parts[0].isBlank()) {
                    return parts[0].trim().replace('_', ' ');
                }
            }
        } catch (IOException ignored) {
            // Fall back to the file name below.
        }

        String fileName = levelFile.getFileName().toString();
        int extensionIndex = fileName.lastIndexOf('.');
        String displayName = extensionIndex > 0 ? fileName.substring(0, extensionIndex) : fileName;
        return displayName.replace('_', ' ');
    }

    public static final class LevelEntry {
        private final String displayName;
        private final String resourcePath;

        public LevelEntry(String displayName, String resourcePath) {
            this.displayName = displayName;
            this.resourcePath = resourcePath;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getResourcePath() {
            return resourcePath;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
