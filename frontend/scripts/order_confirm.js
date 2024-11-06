
    document.addEventListener("DOMContentLoaded", function () {
    // 獲取暫存的訂單資料
    const reservationDate = sessionStorage.getItem('date');
    const reservationStartTime = sessionStorage.getItem('startTime');
    const reservationEndTime = sessionStorage.getItem('endTime');
    const totalPrice = sessionStorage.getItem('totalPrice');

    // 計算時數
    const startHour = parseInt(reservationStartTime.split(':')[0], 10);
    const endHour = parseInt(reservationEndTime.split(':')[0], 10);
    const duration = endHour - startHour;

    // 動態填充資料
    document.getElementById('reservation-date').textContent = reservationDate;
    document.getElementById('reservation-time').textContent = `${reservationStartTime} ~ ${reservationEndTime}`;
    document.getElementById('reservation-duration').textContent = `${duration} 小時`;
    document.getElementById('total-cost').textContent = totalPrice;
    // 更新停車場名稱和地址
    document.getElementById('parking-name').textContent = data.parkingName;
    document.getElementById('parking-address').textContent = data.parkingLocation;
    // 更新摘要側欄
    document.getElementById('sidebar-parking-name').textContent = data.parkingName;
    document.getElementById('sidebar-parking-address').textContent = data.parkingLocation;
});
