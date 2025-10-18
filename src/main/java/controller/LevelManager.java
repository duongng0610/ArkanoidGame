package controller;

import objects.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import util.Constants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    // Loads a specific level buy its number from a JSon file
    public List<Brick> loadLevel(int levelNumber) {
        List<Brick> bricks = new ArrayList<Brick>();
        String pathFile = "levels/level" + levelNumber + ".json";

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(pathFile)) {
            if (is == null) {
                System.err.println("Cannot find level file: " + pathFile);
                return bricks;
            }

            JSONTokener tokener = new JSONTokener(is);
            JSONObject levelData = new JSONObject(tokener);
            JSONArray layout = levelData.getJSONArray("layout");

            System.out.println("Loading level: " + levelData.optString("levelName", "Unknown"));

            for (int row = 0; row < layout.length(); row++) {
                String line = layout.getString(row);
                String[] brickTypes = line.trim().split(" ");

                for (int col = 0; col < brickTypes.length; col++) {
                    String type = brickTypes[col];
                    if (!type.equals("0")) {

                        // intialize brick
                        double x = Constants.BRICK_START_X + col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP_X);
                        double y = Constants.BRICK_START_Y + row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP_Y);
                        Brick brick = creatBrick(type, x, y);
                        if (brick != null) {
                            bricks.add(brick);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bricks;
    }

    private Brick creatBrick(String types, double x, double y) {
        switch (types) {
            case "1" : return new NormalBrick(x, y);
            case "2" : return new StrongBrick(x, y);
            case "U": return new UnbreakableBrick(x, y);
            case "E": return new ExplosiveBrick(x, y);
            default: return null;
        }
    }
}
