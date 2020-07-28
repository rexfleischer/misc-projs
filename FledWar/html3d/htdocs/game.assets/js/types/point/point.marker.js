

function PointMarker(base_point) {
  BasePoint.call(this, base_point);
  
  this.graphic = null;
}


PointMarker.prototype = new BasePoint();
PointMarker.prototype.constructor = PointMarker;


