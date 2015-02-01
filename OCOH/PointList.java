package OCOH;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import anja.geom.Point2;
import anja.geom.Rectangle2;
import anja.geom.Segment2;

public class PointList {

	private Color pointColor;
	List<Point> points;

	public PointList(Color pointColor, List<Point> points) {
		this.points = points;
		this.pointColor = pointColor;
		setPointsColor(pointColor);
	}
	

	public PointList(List<Point> points){
		this(Color.BLACK, points);
	}

	public PointList(Color pointColor) {
		this(pointColor, new ArrayList());
	}
	
	public PointList(){
		this(Color.BLACK);
	}

	public void setList(List<Point> points){
		this.points = points;
	}
	
	public void setPointsColor(Color color){
		
		for(Point p: points){
			p.setColor(color);
		}
	}
	
	public boolean collisionExists(Point p){
		
		for (Point point : points) {
			if (point.collide(p)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addPoint(Point p) {

		p.setColor(pointColor);
		points.add(p);

	}

	public void setPointList() {

	}

	public boolean enoughPoints() {
		
		if(points.size()>=2){
			return true;
		}
		
		return false;
	}
	
	public void remove(Point p){
		points.remove(p);
	}
	
	public void clear(){
		points.clear();
	}
	
	public List<Point> getPoints(){
		return points;
	}
	
	public void draw(Graphics g){
			
		setPointsColor(pointColor);
		for(Point p: points){
			p.draw(g);
			
		}
	}
	
	public int getSize(){
		return points.size();
	}
	
	public boolean contains(Point p){
		return points.contains(p);
	}
	
	public double objectMinDist(PointList list){
			
		// returns minimum distance between two objects defined by this and list
		
		double minDist = Double.POSITIVE_INFINITY;
		
		if (this.getSize() == 1 && list.getSize() == 1){
			// minimum distance between two points
			Point2 p1 =  new Point2(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p2 =  new Point2(list.points.get(0).posX, list.points.get(0).posY);
			minDist = p1.distance(p2);
		} else if (this.getSize() == 1 && list.getSize() == 2){
			// minimum distance between a point and a line segment
			Point2 p1 =  new Point2(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p21 = new Point2(list.points.get(0).posX, list.points.get(0).posY);
			Point2 p22 = new Point2(list.points.get(1).posX, list.points.get(1).posY);
			Segment2 l2 = new Segment2(p21, p22);
			minDist = l2.distance(p1);
		} else if (this.getSize() == 2 && list.getSize() == 1){
			// minimum distance between a point and a line segment
			Point2 p11 = new Point2(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p12 = new Point2(this.points.get(1).posX, this.points.get(1).posY);
			Point2 p2 =  new Point2(list.points.get(0).posX, list.points.get(0).posY);
			Segment2 l1 = new Segment2(p11, p12);
			minDist = l1.distance(p2);
		} else if (this.getSize() == 2 && list.getSize() == 2){
			// minimum distance between two line segments
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			LineSegment l1 = new LineSegment(p11, p12);
			LineSegment l2 = new LineSegment(p21, p22);
			minDist = l1.distanceTo(l2);
		} else if (this.getSize() == 4 && list.getSize() == 1){
			// minimum distance between a rectangle and a point
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p2 =  new Point(list.points.get(0).posX, list.points.get(0).posY);
			Rectangle2 r1 = new Rectangle2((float)p11.posX, (float)p11.posY, (float)(Math.abs(p13.posX - p11.posX)), (float)(Math.abs(p12.posY - p11.posY)));
			minDist = p2.distanceTo(r1);
		} else if (this.getSize() == 1 && list.getSize() == 4){
			// minimum distance between a rectangle and a point
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			Point p1 =  new Point(this.points.get(0).posX, this.points.get(0).posY);
			Rectangle2 r2 = new Rectangle2((float)p21.posX, (float)p21.posY, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs((p22.posY - p21.posY))));
			minDist = p1.distanceTo(r2);
		} else if (this.getSize() == 2 && list.getSize() == 4){
			// minimum distance between a rectangle and a line segment
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			LineSegment l1 = new LineSegment(p11, p12);
			Rectangle2 r2 = new Rectangle2((float)p21.posX, (float)p21.posY, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs(p22.posY - p21.posY)));
			minDist = l1.distanceTo(r2);
		} else if (this.getSize() == 4 && list.getSize() == 2){
			// minimum distance between a rectangle and a line segment
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Rectangle2 r1 = new Rectangle2((float)p11.posX, (float)p11.posY, (float)(Math.abs((p13.posX - p11.posX))), (float)(Math.abs((p12.posY - p11.posY))));
			LineSegment l2 = new LineSegment(p21, p22);
			minDist = l2.distanceTo(r1);
		} else if (this.getSize() == 4 && list.getSize() == 4){
			// minimum distance between two rectangles
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			Rectangle2 r1 = new Rectangle2((float)p11.posX, (float)p11.posY, (float)(Math.abs((p13.posX - p11.posX))), (float)(Math.abs((p12.posY - p11.posY))));
			Rectangle2 r2 = new Rectangle2((float)p21.posX, (float)p21.posY, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs((p22.posY - p21.posY))));
			if(r1.contains(r2)){
				minDist = 0;
			} else {
				Point[] points = this.objectMinDistPoints(list);
				minDist = Math.sqrt(points[0].distanceSquaredTo(points[1]));
			}
		}
		
		return minDist;
		
	}

	public Point[] objectMinDistPoints(PointList list){

		// returns points on this and list which build
		// minimum distance between two objects defined by this and list
		// minDistPoints[0] of this, minDistPoints[1] of list: needs to be checked!!!!
		
		Point[] minDistPoints = new Point[2];
		
		double minDist = Double.POSITIVE_INFINITY;
		
		if (this.getSize() == 1 && list.getSize() == 1){
			// minimum distance between two points
			Point p1 =  new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p2 =  new Point(list.points.get(0).posX, list.points.get(0).posY);
			minDistPoints[0] = p1;
			minDistPoints[1] = p2;
		} else if (this.getSize() == 1 && list.getSize() == 2){
			// minimum distance between a point and a line segment
			Point2 p1 =  new Point2(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p21 = new Point2(list.points.get(0).posX, list.points.get(0).posY);
			Point2 p22 = new Point2(list.points.get(1).posX, list.points.get(1).posY);
			Segment2 l2 = new Segment2(p21, p22);
			Point2 p = l2.closestPoint(p1);
			minDistPoints[0] = new Point(p1.getX(), p1.getY());
			minDistPoints[1] = new Point(p.getX(), p.getY());
		} else if (this.getSize() == 2 && list.getSize() == 1){
			// minimum distance between a point and a line segment
			Point2 p11 = new Point2(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p12 = new Point2(this.points.get(1).posX, this.points.get(1).posY);
			Point2 p2 =  new Point2(list.points.get(0).posX, list.points.get(0).posY);
			Segment2 l1 = new Segment2(p11, p12);
			Point2 p = l1.closestPoint(p2);
			minDistPoints[0] = new Point(p.getX(), p.getY());
			minDistPoints[1] = new Point(p2.getX(), p2.getY());
		} else if (this.getSize() == 2 && list.getSize() == 2){
			// minimum distance between two line segments
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			LineSegment l1 = new LineSegment(p11, p12);
			LineSegment l2 = new LineSegment(p21, p22);
			minDistPoints = l1.minDistPointsTo(l2);
		} else if (this.getSize() == 4 && list.getSize() == 1){
			// minimum distance between a rectangle and a point
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p2 =  new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point2 p = new Point2(p2.posX, p2.posY);
			Rectangle2 r1 = new Rectangle2((float)p11.posX, (float)p11.posY, (float)(Math.abs(p13.posX - p11.posX)), (float)(Math.abs(p12.posY - p11.posY)));

			Segment2 s1 = r1.top();
			Segment2 s2 = r1.bottom();
			Segment2 s3 = r1.left();
			Segment2 s4 = r1.right();
			
			List<Point2> points = new ArrayList<Point2>();
			points.add(s1.closestPoint(p));
			points.add(s2.closestPoint(p));
			points.add(s3.closestPoint(p));
			points.add(s4.closestPoint(p));

			for (int i = 0; i < points.size(); i++){
				if (p.distance(points.get(i)) < minDist) {
					minDist = p.distance(points.get(i));
					Point2 q = points.get(i);
					minDistPoints[0] = new Point(q.getX(), q.getY());
				}
			}
			minDistPoints[1] = new Point(p2.getX(), p2.getY());
			
		} else if (this.getSize() == 1 && list.getSize() == 4){
			// minimum distance between a rectangle and a point
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			Point p1 =  new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point2 p = new Point2(p1.posX, p1.posY);
			Rectangle2 r2 = new Rectangle2((float)p21.posX, (float)p21.posY, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs((p22.posY - p21.posY))));
		
			Segment2 s1 = r2.top();
			Segment2 s2 = r2.bottom();
			Segment2 s3 = r2.left();
			Segment2 s4 = r2.right();
			
			List<Point2> points = new ArrayList<Point2>();
			points.add(s1.closestPoint(p));
			points.add(s2.closestPoint(p));
			points.add(s3.closestPoint(p));
			points.add(s4.closestPoint(p));

			for (int i = 0; i < points.size(); i++){
				if (p.distance(points.get(i)) < minDist) {
					minDist = p.distance(points.get(i));
					Point2 q = points.get(i);
					minDistPoints[1] = new Point(q.getX(), q.getY());
				}
			}
			minDistPoints[0] = new Point(p1.getX(), p1.getY());
			
		} else if (this.getSize() == 2 && list.getSize() == 4){
			// minimum distance between a rectangle and a line segment
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			LineSegment l1 = new LineSegment(p11, p12);
			Rectangle2 r2 = new Rectangle2((float)p21.posX, (float)p21.posY, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs(p22.posY - p21.posY)));
			minDistPoints = l1.minDistPointsTo(r2);
		} else if (this.getSize() == 4 && list.getSize() == 2){
			// minimum distance between a rectangle and a line segment
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Rectangle2 r1 = new Rectangle2((float)p11.posX, (float)p11.posY, (float)(Math.abs((p13.posX - p11.posX))), (float)(Math.abs((p12.posY - p11.posY))));
			LineSegment l2 = new LineSegment(p21, p22);
			minDistPoints = l2.minDistPointsTo(r1);
		} else if (this.getSize() == 4 && list.getSize() == 4){
			// minimum distance between two rectangles
			Point p11 = new Point(this.points.get(0).posX, this.points.get(0).posY);
			Point p12 = new Point(this.points.get(1).posX, this.points.get(1).posY);
			Point p13 = new Point(this.points.get(2).posX, this.points.get(2).posY);
			Point p21 = new Point(list.points.get(0).posX, list.points.get(0).posY);
			Point p22 = new Point(list.points.get(1).posX, list.points.get(1).posY);
			Point p23 = new Point(list.points.get(2).posX, list.points.get(2).posY);
			Rect r1 = new Rect(p11, (float)(Math.abs((p13.posX - p11.posX))), (float)(Math.abs((p12.posY - p11.posY))));
			Rect r2 = new Rect(p21, (float)(Math.abs((p23.posX - p21.posX))), (float)(Math.abs((p22.posY - p21.posY))));
			//TODO
//			if(r1.contains(r2)){
//			}
			minDistPoints = r1.minDistPointsTo(r2);
		}
		
		return minDistPoints;
	}
	
	public boolean objectContains(Point p){
		
		// returns true if the object defined by list contains p

		boolean contains = false;
		
		if (this.getSize() == 1){
			// object defined by list is a point
			contains = this.points.get(0).equals(p);
		} else if (this.getSize() == 2){
			// object defined by list is a line segment
			Point q = new Point(Math.round(p.posX), Math.round(p.posY));
			Point p1 = new Point(Math.round(points.get(0).posX), Math.round(points.get(0).posY));
			Point p2 = new Point(Math.round(points.get(1).posX), Math.round(points.get(1).posY));
			LineSegment seg = new LineSegment(p1, p2);
			contains = seg.contains(q);
		} else if (this.getSize() == 4){
			Point p1 = new Point(points.get(0).posX, points.get(0).posY);
			Point p2 = new Point(points.get(1).posX, points.get(1).posY);
			Point p3 = new Point(points.get(2).posX, points.get(2).posY);
			Point p4 = new Point(points.get(3).posX, points.get(3).posY);
			Rect r = new Rect(p1, p3, p2, p4);
			contains = r.contains(p);
		}
		
		return contains;
		
	}
	

	
	public double objectMaxDist(PointList list){
		
		// returns max distance between p in list 1 and q in list 2

		double maxDist = Double.NEGATIVE_INFINITY;
		List<Double> distances = new ArrayList<Double>();
		
		for (int i = 0; i < this.getSize(); i++){
			for (int j = 0; j < list.getSize(); j++){
				distances.add(Math.sqrt(this.points.get(i).distanceSquaredTo(list.points.get(j))));
			}
		}

		for (Double dist : distances){
			if (dist > maxDist) maxDist = dist;
		}
		
		return maxDist;
		
	}
	/**
	 * 
	 * @param this
	 * @param list
	 * @return Point[] maxDistPoints
	 * 		maxDistPoints[0] on this, maxDistPoints[1] on list
	 */
	public Point[] objectMaxDistPoints(PointList list){
		
		double maxDist = Double.NEGATIVE_INFINITY;
		
		Point[] maxDistPoints = new Point[2]; // Point[0] on this, Point[1] on list
		Double distance;
		
		for (int i = 0; i < this.getSize(); i++){
			for (int j = 0; j < list.getSize(); j++){
				
				distance = Math.sqrt(this.points.get(i).distanceSquaredTo(list.points.get(j)));
				
				if (distance > maxDist){
					maxDist = distance;
					maxDistPoints[0] = this.getPoints().get(i);
					maxDistPoints[1] = list.getPoints().get(j);
				}
			}
		}
		
		return maxDistPoints;
		
	}
	
	public double maxDist(){
		// returns max distance between two points in set S
		double maxDist = Double.NEGATIVE_INFINITY;
		List<Double> distances = new ArrayList<Double>();
		for (int i = 0; i < points.size(); i++){
			for (int j = 0; j < points.size(); j++){
				distances.add(Math.sqrt(points.get(i).distanceSquaredTo(points.get(j))));
			}
		}

		for (Double dist : distances){
			if (dist > maxDist) maxDist = dist;
		}
		
		return maxDist;
	}
	
	public Point search(Point p){
		
		for(Point point: points){
			if(point.equals(p)){
				
				return point; 
			}
		}
		
		return null;
	}
	
	public void removeAll(Point p){
		
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
		    Point point = iterator.next();
		    if (point.equals(p)) {
		        // Remove the current element from the iterator and the list.
		        iterator.remove();
		    }
		}
	}
	
	public boolean equals(Object o){
		
		if(o instanceof PointList){
			
			PointList pointsList = (PointList)o;
			
			for(Point p: pointsList.getPoints()){
				
				if(!points.contains(p)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public int hasCode(){
		return points.size();
	}

	public String toString(){
		return points.toString();
	} 
	
	public Point[] getExtremePoints(){
		
		//extremePoints contains the points with highest and lowest x and y coordinate of the PointList
		
		Point[] extremePoints = new Point[4];
		if (points.size() == 0) return extremePoints;
		//extreme points in X direction
		Collections.sort(points, Point.COMPARE_BY_XCoord);
		extremePoints[0] = points.get(0); // smallest X coordinate
		extremePoints[1] = points.get(getSize()-1); // biggest X coordinate
		
		//extreme points in Y direction
		Collections.sort(points, Point.COMPARE_BY_YCoord);
		extremePoints[2] = points.get(0); // smallest Y coordinate
		extremePoints[3] = points.get(getSize()-1); // biggest Y coordinate
		
		return extremePoints;
	}
	
	public double delta(){
		
		// returns 1/2 of the largest L_infty distance between any 2 points of PointList
		
		double max = Double.NEGATIVE_INFINITY;
		for (Point p : points){
			for (Point q : points){
				if (p.inftyDistanceTo(q) > max){
					max = p.inftyDistanceTo(q);
				}
			}
		}
		
		return (0.5 * max);
	}
	
	public boolean isEmpty(){
		
		int size = getSize();
		if (size > 0) return false;
		else return true;
		
	}
	
	
}
