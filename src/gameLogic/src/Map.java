package gameLogic.src;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {

    private Path[] paths;
    private TileType[][] map;
    private int mapHeight;
    private int mapWidth;
    private String name;

    public Map(String fileName) {
        try {
            loadMap(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String file) throws IOException {
        if (!file.contains(".csv")){
            throw new UnsupportedEncodingException("file format not supported");
        }

        int numberOfPaths;

        InputStream is = getClass().getResourceAsStream(file);
        if (is == null && !file.startsWith("/")) {
            is = getClass().getResourceAsStream("/" + file);
        }
        if (is == null) {
            throw new IOException("Bestand niet gevonden in resources: " + file);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<List<String>> buffer = reader.lines()
                    .map(line -> Arrays.asList(line.split(",")))
                    .toList();

            if (buffer.isEmpty()) throw new IOException("Bestand is leeg");

            name = buffer.get(0).get(0);
            numberOfPaths = Integer.parseInt(buffer.get(0).get(1));

            int rows = buffer.size() - 1;
            int cols = buffer.get(1).size();
            map = new TileType[rows][cols];

            for (int i = 1; i < buffer.size(); i++) {
                for (int j = 0; j < buffer.get(i).size(); j++) {
                    map[i - 1][j] = TileType.fromInt(Integer.parseInt(buffer.get(i).get(j)));
                }
            }
            mapHeight = rows;
            mapWidth = cols;
        }

        paths = new Path[numberOfPaths];

        for (int i = 4; i < numberOfPaths + 4; i++) {
            Globals globals = JsePlatform.standardGlobals();
            LuaValue luaScript = globals.loadfile("src/gameLogic/src/PathFinder.lua").call();
            LuaValue findPathsFunc = globals.get("findPaths");

            if (!findPathsFunc.isnil()) {
                LuaValue luaPath = findPathsFunc.call(CoerceJavaToLua.coerce(map), LuaValue.valueOf(i));
                if(!luaPath.istable()){
                    throw new RuntimeException("failed to find path for ID " + i);
                }

                List<MapPoint> temp = new ArrayList<>();
                for (int j = 1; j <= luaPath.length(); j++) {
                    LuaValue point = luaPath.get(j);
                    int x = point.get("x").checkint();
                    int y = point.get("y").checkint();
                    temp.add(new MapPoint(x, y));
                }

                paths[i - 4] = new Path(temp.toArray(new MapPoint[0]));
                System.out.println("Path " + (i-4) + " loaded: " + paths[i-4]);
            } else {
                throw new RuntimeException("failed to load function");
            }
        }
    }

    public String getName() { return name; }
    public int getMapWith() { return mapWidth; }
    public int getMapHight() { return mapHeight; }
    public TileType[][] getMap() { return map; }
    public Path[] getPaths() { return paths; }
}
