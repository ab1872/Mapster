package floorstructures;


import worldscreenpositions.Seg;

import java.util.LinkedList;

public class Floor {
    private LinkedList<Seg> PathList;
    private long FloorID, BuildingID;
    private int UndrawRadius;
// Method to convert "to text".
    // Static method "from text";
    // Method to plot;
    // Method to check outbounds
    // Method to Draw(Canvas, Paint) --> invokes for all Seg.
}

/*
Floor--> Path
     |-> ...
     '-> Path-->From(LL), To(LL). Path.draw(canvas){canvas.drawLine(From.XScreen, From.YScreen, To.XScreen, To.YScreen, createdPaint)}
         toString(), fromString()
     |-> FloorID, BuildingID
     |-> LL() (average position)
     |-> undrawRadius
     |-> checkOutbounds(cam)
     |-> (delete)
     |-> draw(canvas)


 */