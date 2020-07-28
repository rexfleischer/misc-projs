

function PointStar(base_point) {
  BasePoint.call(this, base_point);
  
  this.graphic = new THREE.Mesh(
          new THREE.SphereGeometry(base_point.radius * SYSTEM_ZOOM_I, 20, 20),
          new THREE.MeshPhongMaterial( { 
                      ambient: 0x333333, 
                      color: 0xffaa00, 
                      specular: 0xffffff, 
                      shininess: 50 } )
  );
  this.graphic.fled = this;
  
  this.light1 = new THREE.PointLight(0xffaa00);
  this.light2 = new THREE.PointLight(0xffaa00);
  this.graphic.add(this.light1);
  this.graphic.add(this.light2);
}


PointStar.prototype = new BasePoint();
PointStar.prototype.constructor = PointStar;


PointStar.prototype.set_server_position = function(parent_x, 
                                                   parent_y, 
                                                   alpha, 
                                                   distance) {
  BasePoint.prototype.set_server_position.call(this, 
        parent_x, parent_y, alpha, distance);
  this.light1.position.x = this.light2.position.x = this.graphic.position.x;
  this.light1.position.y = this.light2.position.y = this.graphic.position.y;
  this.light1.position.z = (this.server_data.radius * SYSTEM_ZOOM_I * 5);
  this.light2.position.z = -(this.server_data.radius * SYSTEM_ZOOM_I * 5);
};

