document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM加載完成");
    // 取出保存的停車場資訊
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));
    console.log(selectedParking);

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
});

function goToNextPage() {
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));
    const dateRange = document.getElementById('date-picker').value;
    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;

    if (selectedParking && dateRange && startTime && endTime) {
        // 格式化日期範圍並進行拆分
        const [startDateStr, endDateStr] = dateRange.split(" - ");
        const [startMonth, startDay, startYear] = startDateStr.split("/");
        const [endMonth, endDay, endYear] = endDateStr.split("/");

        // 格式化日期為 "YYYY-MM-DD"
        const formattedStartDate = `${startYear}-${startMonth.padStart(2, '0')}-${startDay.padStart(2, '0')}`;
        const formattedEndDate = `${endYear}-${endMonth.padStart(2, '0')}-${endDay.padStart(2, '0')}`;

        // 如果開始日期和結束日期相同，則驗證開始和結束時間
        if (formattedStartDate === formattedEndDate) {
            const startHour = parseInt(startTime.split(':')[0], 10);
            const endHour = parseInt(endTime.split(':')[0], 10);
            const startMinute = parseInt(startTime.split(':')[1], 10);
            const endMinute = parseInt(endTime.split(':')[1], 10);

            if (startHour > endHour || (startHour === endHour && startMinute >= endMinute)) {
                alert('當預約日期為同一天時，結束時間必須晚於開始時間，請重新選擇。');
                return;
            }
        }

        // 將日期和時間合併成完整的時間字符串
        const localStartDateTime = new Date(`${formattedStartDate}T${startTime}:00`);
        const localEndDateTime = new Date(`${formattedEndDate}T${endTime}:00`);

        // 將時間轉換為 UTC 格式
        const utcStartDateTime = localStartDateTime.toISOString();
        const utcEndDateTime = localEndDateTime.toISOString();

        // 暫存選擇的資料，包括 parkingId 和時間
        if (selectedParking.parkingId) {
            sessionStorage.setItem('parkingId', selectedParking.parkingId);
        } else {
            console.error('選擇的停車場資訊中沒有找到 parkingId');
            alert('系統錯誤，請重試');
            return;
        }

        // 存儲 UTC 格式的時間到 sessionStorage
        sessionStorage.setItem('date', dateRange);
        sessionStorage.setItem('startTime', utcStartDateTime);
        sessionStorage.setItem('endTime', utcEndDateTime);

        // 跳轉至下一頁
        window.location.href = 'parking_booking_2.html';
    } else {
        alert('請選擇日期和時間段');
    }
}






// // 透過連絡業主按鈕發送消息給業主
// document.getElementById('contact-owner-button').addEventListener('click', function () {
//     const message = document.getElementById('owner-message').value;

//     if (message.trim() === "") {
//         alert("請填寫您想要發給業主的訊息");
//         return;
//     }

//     // 取出保存的停車場資訊
//     const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

//     if (selectedParking) {
//         // 獲取 JWT token
//         const peterParkerToken = localStorage.getItem('peterParkerToken');
//         if (!peterParkerToken) {
//             console.error('JWT token not found, redirecting to login');
//             window.location.href = 'index.html'; // 重定向到登入頁面
//             return;
//         }

//         // 傳送消息至後端
//         fetch('http://localhost:8081/contactOwner', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json',
//                 'Authorization': `Bearer ${peterParkerToken}`,
//             },
//             body: JSON.stringify({
//                 parkingId: selectedParking.parkingId,
//                 message: message,
//             })
//         })
//             .then(response => {
//                 if (!response.ok) {
//                     throw new Error('Network response was not ok');
//                 }
//                 return response.json();
//             })
//             .then(data => {
//                 alert('訊息已發送成功！');
//             })
//             .catch(error => {
//                 console.error('Error sending message:', error);
//                 alert('發送訊息失敗，請稍後再試');
//             });
//     } else {
//         console.error("未找到選擇的停車場資訊，無法發送訊息");
//     }
// });
