var map;
var marker = null;
var pathLine = null;

var redIcon = new L.Icon({
  iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

function showMap(lat, long) {
  map = L.map('map').setView([lat, long], 10);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);
}

function removeMarker() {
  if(marker != null) {
    marker.remove();
    marker = null;
  }

  if(pathLine != null) {
    pathLine.remove();
    pathLine = null;
  }
}

function addLocationToMap(location) {
  if(marker != null)
    marker.remove();
  marker = L.marker([location.lat, location.lng], {icon: redIcon}).addTo(map)
    .bindPopup("<b>Current location</b>" +
        "<br>Name: " + location.user.fullName +
        "<br>Date: " + location.date)
    .openPopup();
    map.panTo([location.lat, location.lng], 10);
}

function drawPathOfLocations(locations) {
  if(pathLine!=null)
    pathLine.remove();
  if(locations.length > 1) {
    var data = connectTheDots(locations);
    pathLine = L.polyline(data).addTo(map);
  }
}

function connectTheDots(data){
  var c = [];
  for(i in data) {
    var x = data[i].lat;
    var y = data[i].lng;
    c.push([x, y]);
  }
  return c;
}
