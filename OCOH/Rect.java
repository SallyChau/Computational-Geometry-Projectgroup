package OCOH;

public class Rect {

	public Point p;
	public double width, height;

	Point leftUpperVertex;
	Point rightUpperVertex;
	Point leftLowerVertex;
	Point rightLowerVertex;
	
	public Rect(Point p, double width, double height){

		this.p = p;
		this.width = width;
		this.height = height;
		leftUpperVertex = p;
		rightUpperVertex = new Point(p.posX + width, p.posY);
		leftLowerVertex = new Point(p.posX, p.posY + height);
		rightLowerVertex = new Point(p.posX + width, p.posY + height);
		
	}
	
	public Rect(Point midPoint, double radius){
		// square or L-infty ball
		p = new Point(midPoint.posX - radius, midPoint.posY - radius);
		width = 2 * radius;
		height = 2 * radius;
	}
	

	
	public double getArea(){
		
		return width * height;
		
	}
	
	public Point getMidPoint(){
		
		Point midPoint = new Point(p.posX + (0.5 * width), p.posY + (0.5 * height));
		
		return midPoint;
		
	}
	
	public void move(double inXdir, double inYdir){
		
		p.posX += inXdir;
		p.posY += inYdir;
		
	}	
	
	public boolean contains(Point q){
		
		if(q.posX > p.posX && q.posX < rightUpperVertex.posX && q.posY > p.posY && q.posY < leftLowerVertex.posY)
			return true;
		else return false;
	}
	
	public String toString(){
		
		return "P1: (" + p.posX + "," + p.posY + ") - P2: (" + p.posX+width + "," + p.posY + ") - P3: (" + p.posX + "," + p.posY+height + ") - P4: (" + p.posX+width + "," + p.posY+height + ")";
		
	}
	
}
