package com.example.map;


import java.util.LinkedList;

public class Floor {
    private LinkedList<Path> PathList;
    private long FloorID, BuildingID;
    private int UndrawRadius;

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