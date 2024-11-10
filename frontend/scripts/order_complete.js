document.addEventListener("DOMContentLoaded", function () {
    // 取出保存的停車場資訊
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

    if (selectedParking) {
        // 動態填充停車場的詳細信息到頁面中
        const parkingNameElement = document.getElementById('parking-name');
        const parkingAddressElement = document.getElementById('parking-address');
        const parkingInfoElement = document.getElementById('parking-info');

        if (parkingNameElement) {
            parkingNameElement.textContent = selectedParking.parkingName;
        } else {
            console.error('找不到停車場名稱元素');
        }

        if (parkingAddressElement) {
            parkingAddressElement.textContent = selectedParking.parkingLocation;
        } else {
            console.error('找不到停車場地址元素');
        }

        if (parkingInfoElement) {
            parkingInfoElement.textContent = `總數: ${selectedParking.capacity} `;
        } else {
            console.error('找不到停車場資訊元素');
        }
    } else {
        console.error("未找到選擇的停車場資訊");
    }

    // 獲取暫存的預約資料
    const parkingId = sessionStorage.getItem('parkingId');
    const dateRange = sessionStorage.getItem('date'); // 使用 dateRange 替代 date
    const startTime = sessionStorage.getItem('startTime'); // UTC 格式的開始時間
    const endTime = sessionStorage.getItem('endTime'); // UTC 格式的結束時間

    let localStartDateTime, localEndDateTime;

    if (parkingId && dateRange && startTime && endTime) {
        // 將 UTC 時間轉換為本地時間
        localStartDateTime = new Date(startTime);
        localEndDateTime = new Date(endTime);

        // 更新預約日期和時間段
        const displayDateElement = document.querySelector('#display-date');
        const displayTimeElement = document.querySelector('#display-time');
        const timeCountElement = document.getElementById('timecount');

        if (displayDateElement) {
            displayDateElement.textContent = dateRange;
        } else {
            console.error('找不到顯示預約日期的元素');
        }

        if (displayTimeElement) {
            displayTimeElement.textContent = `${localStartDateTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} ~ ${localEndDateTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
        } else {
            console.error('找不到顯示預約時間的元素');
        }

        if (timeCountElement) {
            // 計算持續時間（小時）
            const timeDifferenceInMillis = localEndDateTime - localStartDateTime;
            let durationInHours = Math.ceil(timeDifferenceInMillis / (1000 * 60 * 60)); // 無條件進位到下一小時
            timeCountElement.textContent = `${durationInHours} 小時`;
        } else {
            console.error('找不到顯示時數的元素');
        }

        // 格式化日期為 "YYYY-MM-DD"
        const formattedStartDate = localStartDateTime.toISOString().split('T')[0];
        const formattedEndDate = localEndDateTime.toISOString().split('T')[0];

        // 打印確認用
        console.log('Formatted Start Date:', formattedStartDate);
        console.log('Formatted End Date:', formattedEndDate);

        // 計算預估金額
        const orderData = {
            orderStartTime: localStartDateTime.toISOString(),
            orderEndTime: localEndDateTime.toISOString(),
            parkingId: parseInt(parkingId)
        };

        console.log('Order data:', orderData); // 測試用

        fetch(`http://localhost:8081/order/calculate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(priceData => {
                const totalCostElement = document.getElementById('total-cost');
                if (priceData && priceData.totalPrice) {
                    if (totalCostElement) {
                        totalCostElement.textContent = `${priceData.totalPrice} NTD`;
                    }
                    // 保存總金額到 sessionStorage
                    sessionStorage.setItem('totalCost', priceData.totalPrice);
                } else if (priceData.error) {
                    console.error('無法計算總金額:', priceData.error);
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
    const confirmBookingButton = document.getElementById('confirm-booking-button');
    if (confirmBookingButton) {
        confirmBookingButton.addEventListener('click', function () {
            if (!parkingId || !dateRange || !startTime || !endTime || !localStartDateTime || !localEndDateTime) {
                alert('無法提交訂單，缺少必要的預約信息');
                return;
            }

            // 使用已經計算的 localStartDateTime 和 localEndDateTime
            const orderDTO = {
                parkingId: parseInt(parkingId),
                orderStartTime: localStartDateTime.toISOString(),
                orderEndTime: localEndDateTime.toISOString(),
                statusId: '預約中', // 設置預約狀態
                totalPrice: sessionStorage.getItem('totalCost') // 保存計算的總金額
            };

            // 獲取 JWT token
            const peterParkerToken = localStorage.getItem('peterParkerToken');
            if (!peterParkerToken) {
                console.error('JWT token not found, redirecting to login');
                window.location.href = 'index.html'; // Redirect to login if token is missing
                return;
            }

            console.log("Token being sent:", peterParkerToken);

            // 將訂單提交到後端
            fetch('http://localhost:8081/order/create', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${peterParkerToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(orderDTO)
            })
                .then(response => {
                    console.log("Response status:", response.status);

                    if (response.status >= 200 && response.status < 300) {
                        return response.json();
                    } else {
                        throw new Error(`Network response was not ok, status code: ${response.status}`);
                    }
                })
                .then(data => {
                    console.log("Response data:", data);

                    if (typeof data === 'number') {
                        alert(`預約成功，訂單號：${data}`);

                        // 保存必要的預約資料到 sessionStorage 中供下一頁顯示
                        sessionStorage.setItem('orderId', data);
                        sessionStorage.setItem('date', dateRange);
                        sessionStorage.setItem('startTime', startTime);
                        sessionStorage.setItem('endTime', endTime);

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
    } else {
        console.error("找不到確認預約按鈕元素");
    }
});
