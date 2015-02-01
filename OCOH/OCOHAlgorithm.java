package OCOH;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OCOHAlgorithm {

		public List<PointList> set_withoutTurnpike; // doesnt use turnpike, f 
		public List<PointList> set_withTurnpike; // uses turnpike, t
		public List<Point[]> extremePoints1;
		public List<Point[]> extremePoints2;
		public List<PointList> list_centersWithoutTurnpike; // set of points resulting from center()
		public List<PointList> list_centersWithTurnpike;
		public List<Point> minDist1; // minDistPoints
		public List<Point> minDist2;
		public List<Point> maxDist1; // maxDistPoints
		public List<Point> maxDist2;
		
		public int solutionIndex;
		public Point solution_facility;
		public Point solution_turnpikeStart;
		public double solution_radius;
		
		List<Point> facilityPoints;
		List<Point> turnpikePoints;
		List<Double> partitionRadius;
		
		private Point currentFacility = new Point(); // turnpike endpoint 
		private Point currentTurnpikeStart = new Point();
		double currentRadius = 0;
		
		private double prevY = 0;
		private double eps1, eps2, x = 0;
		private double maxDist;
		
		int highwayLength;
		int velocity;
		
	public void runAlgorithm(PointList customers, int highwayLength, int velocity) {
		
		if (!customers.isEmpty()){
			this.highwayLength = highwayLength;
			this.velocity = velocity;
			maxDist = customers.maxDist();
			
			// for drawing purposes only
			facilityPoints = new ArrayList<Point>();
			turnpikePoints = new ArrayList<Point>();
			partitionRadius = new ArrayList<Double>();
			
			set_withoutTurnpike = new ArrayList<PointList>();
			set_withTurnpike = new ArrayList<PointList>();
			extremePoints1 = new ArrayList<Point[]>();
			extremePoints2 = new ArrayList<Point[]>();
			list_centersWithoutTurnpike = new ArrayList<PointList>();
			list_centersWithTurnpike = new ArrayList<PointList>();
			
			getPartition(customers);
			
			maxDist1 = new ArrayList<Point>();
			maxDist2 = new ArrayList<Point>();
			minDist1 = new ArrayList<Point>();
			minDist2 = new ArrayList<Point>();
			
			// compute extreme points
			for (PointList p : set_withoutTurnpike){
				extremePoints1.add(p.getExtremePoints());
			}
			for (PointList p : set_withTurnpike){
				extremePoints2.add(p.getExtremePoints());
			}
			
			// solve basic problem for all partitions {W,H}
			for (int i = 0; i < set_withoutTurnpike.size(); i++){

				eps1 = Math.max(0, set_withoutTurnpike.get(i).delta() + highwayLength/velocity - set_withTurnpike.get(i).delta());
				eps2 = Math.max(0, set_withTurnpike.get(i).delta() - set_withoutTurnpike.get(i).delta() - highwayLength/velocity);

				solveBP(set_withoutTurnpike.get(i), set_withTurnpike.get(i)); 
				facilityPoints.add(currentFacility);
				turnpikePoints.add(currentTurnpikeStart);
				partitionRadius.add(currentRadius);
				
				// for drawing purposes
				// fixed length
				list_centersWithoutTurnpike.add(center(set_withoutTurnpike.get(i), set_withoutTurnpike.get(i).delta() + eps2 + x));
				list_centersWithTurnpike.add(center(set_withTurnpike.get(i), set_withTurnpike.get(i).delta() + eps1 + x));
				
				if (list_centersWithoutTurnpike.get(i).objectContains(list_centersWithoutTurnpike.get(i).objectMinDistPoints(list_centersWithTurnpike.get(i))[0])){
					minDist1.add(list_centersWithoutTurnpike.get(i).objectMinDistPoints(list_centersWithTurnpike.get(i))[0]); 
					minDist2.add(list_centersWithoutTurnpike.get(i).objectMinDistPoints(list_centersWithTurnpike.get(i))[1]);
				} else {
					minDist1.add(list_centersWithoutTurnpike.get(i).objectMinDistPoints(list_centersWithTurnpike.get(i))[1]); 
					minDist2.add(list_centersWithoutTurnpike.get(i).objectMinDistPoints(list_centersWithTurnpike.get(i))[0]);
				}
				maxDist1.add(list_centersWithoutTurnpike.get(i).objectMaxDistPoints(list_centersWithTurnpike.get(i))[0]); 
				maxDist2.add(list_centersWithoutTurnpike.get(i).objectMaxDistPoints(list_centersWithTurnpike.get(i))[1]);
			}	
			
			if(customers.getSize() > 1){
				// find optimal solution
				solutionIndex = getMinRadiusIndex(); 
				solution_facility = facilityPoints.get(solutionIndex);
				solution_turnpikeStart = turnpikePoints.get(solutionIndex);
//				solution_radius = partitionRadius.get(solutionIndex);
			}
			
//			System.out.println("F: "+currentFacility.toString() + " T: " + currentTurnpikeStart);
//			System.out.println("Distance: " + Math.sqrt(currentFacility.distanceSquaredTo(currentTurnpikeStart)));
//			System.out.println("Radius: " + currentRadius);
			
		}
	}
	
	private int getMinRadiusIndex(){
		
		double minRadius = Double.POSITIVE_INFINITY;
		int radiusIndex = 0;
		
		for (double radius : partitionRadius){
			if (radius < minRadius) {
				minRadius = radius;
				radiusIndex = partitionRadius.indexOf(minRadius);
			}
		}
		
		return radiusIndex;
		
	}

	/**
	 * Computes the smallest radius needed to place a turnpike 
	 * of length highwayLength as an input for the method 
	 * @see{OCOH.OCOHAlgorithm.center(PointList T, double radius)}.
	 * This method uses binary search to find the smallest radius 
	 * in time O(log n).
	 * 
	 * @param list1
	 * 			List of Points 
	 * @param list2
	 * 			List of Points
	 * @param m
	 * 			lower bound
	 * @param M
	 * 			upper bound
	 * @return
	 * 			the minimum radius needed to place a turnpike
	 */
	public double getBPradius(PointList list1, PointList list2, double m, double M){
		
		double y = (m+M)/2;
		
		double e1 = Math.max(0, list1.delta() + highwayLength/velocity - list2.delta());
		double e2 = Math.max(0, list2.delta() - list1.delta() - highwayLength/velocity);
		
		PointList centers1 = center(list1, list1.delta() + e2 + y); // Center(H, d(H)+e2+x)
		PointList centers2 = center(list2, list2.delta() + e1 + y); // Center(W, d(W)+e1+x)
		
		// find maximum distance between centers1 and centers2
		double maxDist = centers1.objectMaxDist(centers2);
		
		// find minimum distance between centers1 and centers2
		double minDist = centers1.objectMinDist(centers2);
		
		if ((int)Math.abs(prevY - y) == 0){
			return prevY;
		}
		if (maxDist >= highwayLength && minDist <= highwayLength){
			prevY = y;
			if ((int)Math.abs(M-m) == 0){
				return y;
			} else return getBPradius(list1, list2, m, y);
		} else {
			return getBPradius(list1, list2, y, M);
		}
		
	}
	
	private void setCurrentTurnpike(Point t){
		
		currentTurnpikeStart = t;
	
	}
	
	private void setCurrentFacility(Point f){
		
		currentFacility = f;
	
	}
	
	private void setCurrentRadius(double r){
		
		currentRadius = r;
		
	}
	
	public void solveBP(PointList list1, PointList list2){
		double e1 = Math.max(0, list1.delta() + highwayLength/velocity - list2.delta());
		double e2 = Math.max(0, list2.delta() - list1.delta() - highwayLength/velocity);
		
		x = getBPradius(list1, list2, 0, maxDist);
		
		PointList centers1 = center(list1, list1.delta() + e2 + x); // Center(H, d(H)+e2+x), t
		PointList centers2 = center(list2, list2.delta() + e1 + x); // Center(W, d(W)+e1+x), f
		
		double radius = Math.max(list1.delta() + e2 + x, list2.delta() + e1 + x);
		
		Point l1Start = new Point();
		Point l1End = new Point();
		Point l2Start = new Point();
		Point l2End = new Point();
		
		Point[] minDistPoints = centers1.objectMinDistPoints(centers2);
		Point[] maxDistPoints = centers1.objectMaxDistPoints(centers2);
		
		// find correct lines
		
		if (centers1.objectContains(minDistPoints[0])){
			l1Start = minDistPoints[0];
			l2Start = minDistPoints[1];
		} else {
			l1Start = minDistPoints[1];
			l2Start = minDistPoints[0];
		} 
		
		l1End = maxDistPoints[0];
		l2End = maxDistPoints[1];
		
		Point f = new Point();
		Point t = new Point(); 
		
		// parameterize lines
		double[] v1 = l1Start.getDirectionVectorTo(l1End);
		double[] v2 = l2Start.getDirectionVectorTo(l2End);
		
		double r = getParam(l1Start, l2Start, v1, v2);
		
		t = new Point(l1Start.posX + r * v1[0], l1Start.posY + r * v1[1]);
		f = new Point(l2Start.posX + r * v2[0], l2Start.posY + r * v2[1]);
		
		setCurrentFacility(f);
		setCurrentTurnpike(t);
		setCurrentRadius(radius);
		 
	}
	
	public double getParam(Point p1, Point p2, double[] v1, double[] v2){
		double r;
		
		double l = highwayLength;
		
		// auxiliary variables
		double A = (Math.pow(v1[0] - v2[0], 2.0)) + (Math.pow(v1[1] - v2[1], 2.0));
		double B = (2 * (p1.posX - p2.posX) * (v1[0] - v2[0])) + (2 * (p1.posY - p2.posY) * (v1[1] - v2[1]));
		double C = (Math.pow(p1.posX - p2.posX, 2.0)) + (Math.pow(p1.posY - p2.posY, 2.0));
		
		// using pq formula to find r
		double phalf = B / (2 * A);
		double q = ((C - (l * l)) / A);
		double r1 = - phalf + Math.sqrt((phalf * phalf) - q);
		double r2 = - phalf - Math.sqrt((phalf * phalf) - q);
		
		if (r1 >= 0 && r1 <= 1) r = r1;
		else r = r2;
		
		return r;
	}
	
	public PointList center(PointList T, double radius){
			
		// returns centers: locus of the centers of the axis-parallel squares of radius r that cover T
		// center(T,r) = center(extreme(T),r)
		
		PointList centers = new PointList(Color.PINK);
		Point[] extrema = new Point[4];
		Point currentCenter;
		
		double xLength;
		double yLength;
		double xStart;
		double xEnd;
		double yStart;
		double yEnd;
		
		extrema = T.getExtremePoints();
		
		if (extrema[0] == null) return centers; // centers is empty
		
		if (radius < T.delta()) return centers; // centers is empty
		
		// find X coordinates
		currentCenter = new Point(extrema[0].posX + radius, extrema[0].posY);
		if (currentCenter.posX + radius == extrema[1].posX) {
			// current x position is only possible x position
			xStart = currentCenter.posX;
			xEnd = xStart;
			
		} else {
			// we can move in x direction
			xLength = Math.abs(currentCenter.posX + radius - extrema[1].posX);
//			xStart = currentCenter.posX - xLength;
//			xEnd = currentCenter.posX + xLength;
			xStart = currentCenter.posX - xLength;
			xEnd = currentCenter.posX;
		}

		// find Y coordinates
		currentCenter = new Point(extrema[2].posX, extrema[2].posY + radius);
		if (currentCenter.posY + radius == extrema[3].posY) {
			// current y position is only possible y position
			yStart = currentCenter.posY;
			yEnd = yStart;
		} else {
			// we can move in y direction
			yLength = Math.abs(currentCenter.posY + radius - extrema[3].posY);
//			yStart = currentCenter.posY - yLength;
//			yEnd = currentCenter.posY + yLength;
			yStart = currentCenter.posY - yLength;
			yEnd = currentCenter.posY;
		}
		
		// points of centers
		/*
		 * 0----2
		 * |    |
		 * |    |
		 * 1----3
		 */
		centers.addPoint(new Point(xStart, yStart));
		if (!centers.contains(new Point(xStart, yEnd))){
			centers.addPoint(new Point(xStart, yEnd));
		}
		if (!centers.contains(new Point(xEnd, yStart))){
			centers.addPoint(new Point(xEnd, yStart));
		}
		if (!centers.contains(new Point(xEnd, yEnd))){
			centers.addPoint(new Point(xEnd, yEnd));
		}
		

		return centers;
	
	}
	
	public void getPartition(PointList customers){
		splitByQuadrant(customers);
		splitByLine(customers);
	}
	
	private void splitByQuadrant(PointList S){

	
		PointList[][] UR = new PointList[S.getSize()][S.getSize()];
		PointList[][] DL = new PointList[S.getSize()][S.getSize()];
		PointList Yincr = S;
		PointList X = S;
		PointList Y = new PointList();
		
		Collections.sort(X.points, Point.COMPARE_BY_XCoord); // p_i
		// sort S by y coordinates in decreasing order
		Collections.sort(Yincr.points, Point.COMPARE_BY_YCoord); // q_i
		for (int i = Yincr.getSize()-1; i > 0 ; i--){
				Y.addPoint(Yincr.points.get(i));
		}
		
		// initialize all point lists
		for (int i = 0; i < S.getSize(); i++){
			for (int j = 0; j < S.getSize(); j++){
				UR[i][j] = new PointList(Color.BLUE);
				DL[i][j] = new PointList(Color.RED);
			}
		}
		// find points in UR
		for (int u = 0; u < S.getSize(); u++){
			for (int i = 0; i < Y.getSize(); i++){
				for (int j = 0; j < X.getSize(); j++){
					if (S.points.get(u).posY > Y.points.get(i).posY && S.points.get(u).posX < X.points.get(j).posX){
						UR[i][j].addPoint(S.points.get(u));
					} else {
						DL[i][j].addPoint(S.points.get(u));
					}
					if (!contains(set_withoutTurnpike, UR[i][j]) && UR[i][j].getSize() > 0){
						
						set_withoutTurnpike.add(UR[i][j]);
						set_withTurnpike.add(DL[i][j]);
					}
				}
			}
		}
		
	}
	
	public boolean contains(List<PointList> list, PointList pList){
	
		for (PointList p : list){
			if (p.equals(pList)) return true;
		}
		
		return false;
	}
	
	private void splitByLine(PointList S){

		PointList[] L = new PointList[S.getSize()-1];
		PointList[] R = new PointList[S.getSize()-1];
		// case a) and b)

		// initialize all point lists
		for (int i = 0; i < S.getSize()-1; i++){
				L[i] = new PointList(Color.BLUE);
				R[i] = new PointList(Color.RED);
		}
		
		Collections.sort(S.points, Point.COMPARE_BY_XCoord);
		// Divide into L_i, R_i for all 1<=i<n
		for (int i = 0; i < S.getSize()-1; i++){
			for (int k = 0; k < i+1; k++){
				L[i].addPoint(S.points.get(k));	
			}
			for (int j = i+1; j < S.getSize(); j++){
				R[i].addPoint(S.points.get(j));
			}

			set_withoutTurnpike.add(L[i]);
			set_withTurnpike.add(R[i]);

		}
		
	}
	
}
