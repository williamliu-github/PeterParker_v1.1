document.addEventListener("DOMContentLoaded", function () {
    // 獲取暫存的訂單資料
    const reservationDate = sessionStorage.getItem('date');
    const reservationStartTime = sessionStorage.getItem('startTime');
    const reservationEndTime = sessionStorage.getItem('endTime');
    const totalPrice = sessionStorage.getItem('totalCost'); // 注意，使用 'totalCost' 而非 'totalPrice'

    // 定義日期與時間的顯示格式 options
    const dateOptions = { year: 'numeric', month: '2-digit', day: '2-digit', weekday: 'short' };
    const timeOptions = { hour: '2-digit', minute: '2-digit', hourCycle: 'h23' };

    // 動態填充資料
    if (reservationDate) {
        // 格式化日期並添加星期幾
        const [startDateStr, endDateStr] = reservationDate.split(" - ");
        const startDate = new Date(startDateStr);
        const endDate = new Date(endDateStr);

        const formattedStartDate = startDate.toLocaleDateString('zh-TW', dateOptions);
        const formattedEndDate = endDate.toLocaleDateString('zh-TW', dateOptions);

        // 顯示日期範圍
        document.getElementById('reservation-date').textContent = formattedStartDate === formattedEndDate 
            ? formattedStartDate 
            : `${formattedStartDate} - ${formattedEndDate}`;
    } else {
        console.error("找不到預約日期的資料");
    }

    if (reservationStartTime && reservationEndTime) {
        const startDateTime = new Date(reservationStartTime);
        const endDateTime = new Date(reservationEndTime);

        const formattedStartDate = `(${(startDateTime.getMonth() + 1).toString().padStart(2, '0')}/${startDateTime.getDate().toString().padStart(2, '0')})`;
        const formattedEndDate = `(${(endDateTime.getMonth() + 1).toString().padStart(2, '0')}/${endDateTime.getDate().toString().padStart(2, '0')})`;
        
        const formattedStartTime = startDateTime.toLocaleTimeString('zh-TW', timeOptions);
        const formattedEndTime = endDateTime.toLocaleTimeString('zh-TW', timeOptions);

        document.getElementById('reservation-time').textContent = 
            `${formattedStartDate} ${formattedStartTime} ~ ${formattedEndDate} ${formattedEndTime}`;
    } else {
        console.error("找不到預約時間段的資料");
    }

    // 計算時數
    let duration = 0;
    if (reservationStartTime && reservationEndTime) {
        const startDateTime = new Date(reservationStartTime);
        const endDateTime = new Date(reservationEndTime);
        duration = Math.ceil((endDateTime - startDateTime) / (1000 * 60 * 60));
    }

    if (duration > 0) {
        document.getElementById('reservation-duration').textContent = `${duration} 小時`;
    } else {
        console.error("計算預約時數時發生錯誤");
    }

    if (totalPrice && !isNaN(totalPrice)) {
        document.getElementById('total-cost').textContent = `${totalPrice} NTD`;
    } else {
        console.error("找不到總金額的資料或總金額不正確");
    }

    // 從 sessionStorage 獲取並更新停車場相關資料
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

    if (selectedParking) {
        if (selectedParking.parkingName) {
            document.getElementById('parking-name').textContent = selectedParking.parkingName;
        } else {
            console.error("找不到停車場名稱資料");
        }

        if (selectedParking.parkingLocation) {
            document.getElementById('parking-address').textContent = selectedParking.parkingLocation;
        } else {
            console.error("找不到停車場地址資料");
        }

        // 更新摘要側欄
        const sidebarParkingNameElement = document.getElementById('sidebar-parking-name');
        const sidebarParkingAddressElement = document.getElementById('sidebar-parking-address');

        if (sidebarParkingNameElement) {
            sidebarParkingNameElement.textContent = selectedParking.parkingName;
        } else {
            console.error("找不到側欄停車場名稱的元素");
        }

        if (sidebarParkingAddressElement) {
            sidebarParkingAddressElement.textContent = selectedParking.parkingLocation;
        } else {
            console.error("找不到側欄停車場地址的元素");
        }
    } else {
        console.error("未找到選擇的停車場資訊");
    }

    // 當頁面跳轉或關閉時，清除 sessionStorage 中的資料
    window.addEventListener("beforeunload", function () {
        sessionStorage.clear();
    });
});
