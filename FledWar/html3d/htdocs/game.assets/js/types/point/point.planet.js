

function PointPlanet(base_point) {
  BasePoint.call(this, base_point);
  
  this.graphic = new THREE.Mesh(
          new THREE.SphereGeometry(base_point.radius * SYSTEM_ZOOM_I, 20, 20),
          new THREE.MeshPhongMaterial( { 
                      ambient: 0x333333, 
                      color: 0xEE9343, 
                      specular: 0x000000, 
                      shininess: 3 } )
  );
  
  this.graphic.fled = this;
}


PointPlanet.prototype = new BasePoint();
PointPlanet.prototype.constructor = PointPlanet;


