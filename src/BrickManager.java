import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrickManager {
    public ArrayList<ArrayList<Integer>> map;
    public int brickWidth;
    public int brickHeight;
    public HashMap<String, Integer> hitBricks;

    public BrickManager(int row, int col) {
        map = new ArrayList<>();
        hitBricks = new HashMap<>();
        for (int i = 0; i < row; i++) {
            ArrayList<Integer> rowList = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                rowList.add(1);
            }
            map.add(rowList);
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        String key = row + "," + col;
        hitBricks.put(key, hitBricks.getOrDefault(key, 0) + 1);
        map.get(row).set(col, value);

        // Print the hit counts to the console after each hit
        printHitCounts();
    }

    public int getBrickHits(int row, int col) {
        String key = row + "," + col;
        return hitBricks.getOrDefault(key, 0);
    }

    private void printHitCounts() {
        System.out.println("Brick Hit Counts:");
        if (hitBricks.isEmpty()) {
            System.out.println("No bricks have been hit yet.");
        } else {
            for (Map.Entry<String, Integer> entry : hitBricks.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                System.out.println("Brick (" + key + "): " + value + " hits");
            }
        }
        System.out.println("--------------------");
    }
}