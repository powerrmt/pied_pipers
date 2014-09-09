package piedpipers.dumb2;

import java.util.*;

import piedpipers.sim.Point;

public class Player extends piedpipers.sim.Player {
	static int npipers;
	
	static double pspeed = 0.49;
	static double mpspeed = 0.09;
	
	static Point target = new Point();
	static int[] thetas;
	static boolean finishround = true;
        static boolean hasrat = false;
	static boolean initi = false;
	
	public void init() {
		thetas = new int[npipers];
	}

	static double distance(Point a, Point b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	// Return: the next position
	// my position: pipers[id]

	public Point move(Point[] pipers, // positions of pipers
			Point[] rats) { // positions of the rats
		npipers = pipers.length;
		System.out.println(initi);
		Point gate = new Point(dimension/2, dimension/2);
		if (!initi) {
			this.init();initi = true;
		}
		Point current = pipers[id];
		double ox = 0, oy = 0;
		if (getSide(current) == 0 && !hasrat) {
			finishround = true;
			this.music = false;
			double dist = distance(current, gate);
			assert dist > 0;
			ox = (gate.x - current.x) / dist * pspeed;
			oy = (gate.y - current.y) / dist * pspeed;
			Random random = new Random();
			int theta = random.nextInt(180);
			thetas[id]=theta;
			System.out.println("move toward the right side");
		} 
                else if (getSide(current) == 0){
                        double dist = distance(current,gate);
                        if (dist > 5){
                          this.music = false;
                          hasrat = false;
                          ox = (gate.x - current.x) / dist * pspeed;
                          oy = (gate.y - current.y) / dist * pspeed;
                        }
                        else{
                          dist = dist * -1;
                          ox = (gate.x - current.x) / dist * mpspeed;
                          oy = (gate.y - current.y) / dist * mpspeed;
                        }
                }
		else if (!closetoWall(current) && finishround && !hasrat) {
			this.music = false;
			ox = pspeed * Math.sin(thetas[id] * Math.PI / 180);
			oy = pspeed * Math.cos(thetas[id] * Math.PI / 180);
                        for (Point rat : rats){
                          if (distance(current,rat) < 10 && distance(rat,gate) > 10){
                            hasrat = true;
                            this.music = true;
                            ox = mpspeed * Math.sin(thetas[id] * Math.PI / 180);
                            oy = mpspeed * Math.cos(thetas[id] * Math.PI / 180);
                          }
                        }
		}
		else {
			finishround = false;
			this.music = true;
			double dist = distance(current, gate);
			assert dist > 0;
			ox = (gate.x - current.x) / dist * mpspeed;
			oy = (gate.y - current.y) / dist * mpspeed;
			System.out.println("move toward the left side");
		}
		
		current.x += ox;
		current.y += oy;
		return current;
	}
	boolean closetoWall (Point current) {
		boolean wall = false;
		if (Math.abs(current.x-dimension)<pspeed) {
			wall = true;
		}
		if (Math.abs(current.y-dimension)<pspeed) {
			wall = true;
		}
		if (Math.abs(current.y)<pspeed) {
			wall = true;
		}
		return wall;
	}
	int getSide(double x, double y) {
		if (x < dimension * 0.5)
			return 0;
		else if (x > dimension * 0.5)
			return 1;
		else
			return 2;
	}

	int getSide(Point p) {
		return getSide(p.x, p.y);
	}

}
