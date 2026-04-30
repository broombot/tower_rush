
package towerrush;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {





    private Path[] paths;
    private TileType[][] map;
    private int mapHight;
    private int mapWith;
    private String name;

    public Map(String filleName) {
        try {
            loadMap(filleName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMap(String fille) throws IOException {
        if (!fille.contains(".csv")){
            try {
                throw new UnsupportedEncodingException("fille format not suported");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
        }

        List<List<String>> buffer = Files.readAllLines(Paths.get(fille))
                .stream()
                .map(line -> Arrays.asList(line.split(",")))
                .toList();

        name = buffer.getFirst().getFirst();
        int numberOfPaths = Integer.parseInt(buffer.getFirst().get(1));

        map = new TileType[buffer.size()][buffer.get(1).size()];

        for (int i = 1; i < buffer.size() ; i++){
            for (int j = 0; j < buffer.get(i).size(); j++){
                map[i-1][j] = TileType.fromInt( Integer.parseInt( buffer.get(i).get(j)));
            }
        }

        paths = new Path[numberOfPaths];

        for (int i = 4; i < numberOfPaths + 4; i++) {
            Globals globals = JsePlatform.standardGlobals();
            LuaValue luaScript = globals.loadfile("src/towerrush/PathFinder.lua").call();
            LuaValue findPathsFunc = globals.get("findPaths");

            if (!findPathsFunc.isnil()) {
                LuaValue luaPath = findPathsFunc.call(CoerceJavaToLua.coerce(map),LuaValue.valueOf(i));
                if(!luaPath.istable()){
                    throw new RuntimeException("failed to find map");
                }


                List<MapPoint> temp = new ArrayList<>();
                for (int j = 1; j <= luaPath.length(); j++) {
                    LuaValue point = luaPath.get(j);

                    int x = point.get("x").checkint();
                    int y = point.get("y").checkint();

                    temp.add(new MapPoint(x,y));
                }

                paths[i - 4] = new Path(temp.toArray(temp.toArray(new MapPoint[temp.size()])));
            } else {
                throw new RuntimeException("faild to load function");
            }
        }

        mapHight = map.length -1;
        mapWith = map[1].length;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMapWith() {
        return mapWith;
    }

    public void setMapWith(int mapWith) {
        this.mapWith = mapWith;
    }

    public int getMapHight() {
        return mapHight;
    }

    public void setMapHight(int mapHight) {
        this.mapHight = mapHight;
    }

    public TileType[][] getMap() {
        return map;
    }

    public void setMap(TileType[][] map) {
        this.map = map;
    }

    public Path[] getPaths() {
        return paths;
    }

    public void setPaths(Path[] paths) {
        this.paths = paths;
    }
}
