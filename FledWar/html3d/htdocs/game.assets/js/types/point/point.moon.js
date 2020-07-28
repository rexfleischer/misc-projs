

function PointMoon(base_point) {
  BasePoint.call(this, base_point);
  
  this.graphic = new THREE.Mesh(
          new THREE.SphereGeometry(base_point.radius*SYSTEM_ZOOM_I, 20, 20), 
          new THREE.MeshPhongMaterial( { 
              ambient: 0x333333, 
              color: 0xdd00dd, 
              specular: 0xffffff, 
              shininess: 50 } )
  );
  this.graphic.fled = this;
}


PointMoon.prototype = new BasePoint();
PointMoon.prototype.constructor = PointMoon;


