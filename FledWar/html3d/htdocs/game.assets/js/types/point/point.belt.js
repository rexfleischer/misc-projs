

function PointBelt(base_point) {
  BasePoint.call(this, base_point);
  
  var texture = THREE.ImageUtils.loadTexture(
          BASE_URL+"game.assets/texture/moon_1024.jpg");
  var dist = (base_point.orientation.distance * SYSTEM_ZOOM_I);
  var radius = (dist > SYSTEM_AU
                ? AU / 9 * SYSTEM_ZOOM_I
                : AU / 12 * SYSTEM_ZOOM_I);
  this.graphic = new THREE.Mesh(
          new THREE.TorusGeometry(dist, radius, 20, 50),
          new THREE.MeshPhongMaterial( {
              map       : texture, 
              bumpMap   : texture,
              bumpScale : 1,
              ambient   : 0x444444,
              color     : 0xA62D00,
              specular  : 0x000000,
              shininess : 1,
              shading   : THREE.SmoothShading } )
  );
  this.graphic.fled = this;
}


PointBelt.prototype = new BasePoint();
PointBelt.prototype.constructor = PointBelt;


PointBelt.prototype.set_server_position = function(parent_x, 
                                                   parent_y, 
                                                   alpha, 
                                                   distance) {
  this.server_data.orientation.alpha = alpha;
  this.server_data.orientation.distance = distance;
  this.x = this.graphic.position.x = parent_x;
  this.y = this.graphic.position.y = parent_y;
};

