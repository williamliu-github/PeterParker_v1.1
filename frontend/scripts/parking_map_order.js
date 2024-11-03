import { config, artifact } from './config.js';


function fetchAndDisplayParking() {
    // Assuming the back-end API provides filtered parking listings based on current map bounds
    var bounds = googleMap.getBounds();
    if (!bounds) return;

    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();

    fetch(`http://${config}/${artifact}/api/getParkingListings`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            northEast: { lat: ne.lat(), lng: ne.lng() },
            southWest: { lat: sw.lat(), lng: sw.lng() },
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            clearMarkers(); // Clear previous markers
            data.forEach(parking => {
                createMarker(parking);
            });
        })
        .catch(error => {
            console.error('Error fetching parking listings:', error);
        });
}