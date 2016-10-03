import java.awt.*;
import java.util.ArrayList;


public class AStarNode implements Comparable<AStarNode>{

    public Point point;
    public AStarNode prevNode;
    public int gValue; //points from start
    public int hValue; //distance from target
    public boolean isValid = false;

    public AStarNode(Point point, boolean isValid) {
        this.point = point;
        this.isValid = isValid;
        this.gValue = Integer.MAX_VALUE;
    }

    public void setGValue(int amount) {
        this.gValue = amount;
    }

    public void calculateHValue(Point goalPoint) {
        this.hValue = (Math.abs(point.x - goalPoint.x) + Math.abs(point.y - goalPoint.y));
    }

    public void calculateGValue(AStarNode point) {//change this hardcore
        this.gValue = point.gValue;
    }

    public int getFValue() {
        return this.gValue + this.hValue;
    }

    public int compareTo(AStarNode a) {
        return this.getFValue() - a.getFValue();
    }

    public boolean equals(Object o){
        if (o instanceof AStarNode){
            if (this.point.equals(((AStarNode)o).point)) return true;
            else return false;
        }
        else{
            return false;
        }
    }

    public ArrayList<AStarNode> getNeighbors(AStarNode[][] grid){
        ArrayList<AStarNode> nbrs = new ArrayList<>();
        int x = this.point.x;
        int y = this.point.y;
        if(x+1 < grid.length && grid[x+1][y].isValid) nbrs.add(grid[x+1][y]);
        if(x-1 >= 0 && grid[x-1][y].isValid) nbrs.add(grid[x-1][y]);
        if(y+1 < grid[0].length && grid[x][y+1].isValid) nbrs.add(grid[x][y+1]);
        if(y-1 >= 0 && grid[x][y-1].isValid) nbrs.add(grid[x][y-1]);
        if(x+1 < grid.length && y+1 < grid[0].length && grid[x+1][y+1].isValid) nbrs.add(grid[x+1][y+1]);
        if(x+1 < grid.length && y-1 >=0 && grid[x+1][y-1].isValid) nbrs.add(grid[x+1][y-1]);
        if(x-1 >= 0 && y+1 < grid[0].length && grid[x-1][y+1].isValid) nbrs.add(grid[x-1][y+1]);
        if(x-1 >= 0 && y-1 >= 0 && grid[x-1][y-1].isValid) nbrs.add(grid[x-1][y-1]);

        return nbrs;
    }

}