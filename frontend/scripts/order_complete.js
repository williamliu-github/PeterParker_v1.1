document.addEventListener("DOMContentLoaded", function () {
    // 獲取暫存的預約資料
    const parkingId = sessionStorage.getItem('parkingId');
    const date = sessionStorage.getItem('date');
    const startTime = sessionStorage.getItem('startTime');
    const endTime = sessionStorage.getItem('endTime');

    if (parkingId && date && startTime && endTime) {
        // 更新預約日期和時間段
        document.querySelector('#display-date').textContent = date;
        document.querySelector('#display-time').textContent = `${startTime}~${endTime}`;

        // 計算時數
        const startHour = parseInt(startTime.split(':')[0], 10);
        const endHour = parseInt(endTime.split(':')[0], 10);
        const timeCount = endHour - startHour;
        document.getElementById('timecount').textContent = `${timeCount}小時`;

        // 從後端獲取該停車場的詳細資訊，並在頁面中顯示
        fetch(`http://localhost:8081/order/${parkingId}`)
            .then(response => response.json())
            .then(data => {
                // 更新停車場名稱和地址
                document.getElementById('parking-name').textContent = data.parkingName;
                document.getElementById('parking-address').textContent = data.parkingLocation;
                // 更新摘要側欄
                document.getElementById('sidebar-parking-name').textContent = data.parkingName;
                document.getElementById('sidebar-parking-address').textContent = data.parkingLocation;

                // 如果後端有提供停車場的圖片
                if (data.parkingImg) {
                    document.getElementById('parking-img').src = `data:image/jpeg;base64,${data.parkingImg}`;
                }
            })
            .catch(error => {
                console.error('Error fetching parking details:', error);
            });

        // 計算預估金額
        const orderDTO = {
            parkingId: parseInt(parkingId),
            orderStartTime: `${date}T${startTime}:00`,
            orderEndTime: `${date}T${endTime}:00`,
        };

        fetch(`http://localhost:8081/order/calculate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderDTO)
        })
            .then(response => response.json())
            .then(data => {
                if (data.totalPrice) {
                    document.getElementById('total-cost').textContent = `${data.totalPrice} NTD`;
                } else {
                    console.error('無法取得總金額');
                }
            })
            .catch(error => {
                console.error('Error calculating total price:', error);
            });

    } else {
        console.error('無法取得預約信息，請返回上一頁重新選擇');
    }

    // 當用戶確認預約並提交時
    document.getElementById('confirm-booking-button').addEventListener('click', function () {
        if (!parkingId || !date || !startTime || !endTime) {
            alert('無法提交訂單，缺少必要的預約信息');
            return;
        }

        const orderDTO = {
            parkingId: parseInt(parkingId),
            orderStartTime: `${date}T${startTime}:00`,
            orderEndTime: `${date}T${endTime}:00`,
            statusId: '預約中' // 設置預約狀態
        };

        // 將訂單提交到後端
        fetch('/order/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderDTO)
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.orderId) {
                    alert(`預約成功，訂單號：${data.orderId}`);
                    // 清空 sessionStorage 中的預約資料
                    sessionStorage.clear();
                    // 跳轉到訂單確認頁面
                    window.location.href = 'parking_booking_confirm.html';
                } else {
                    console.error('創建訂單失敗');
                    alert('創建訂單失敗，請稍後重試');
                }
            })
            .catch(error => {
                console.error('Error creating order:', error);
                alert('創建訂單時發生錯誤，請稍後重試');
            });
    });
});

// 地址複製功能
function copyAddress() {
    const address = document.getElementById('parking-address').textContent;
    navigator.clipboard.writeText(address)
        .then(() => {
            alert('地址已複製到剪貼簿');
        })
        .catch(err => {
            console.error('Error copying address:', err);
        });
}
