// 監聽選擇框變更事件，更新顯示的選擇結果
document.querySelectorAll('select').forEach(function(select) {
    select.addEventListener('change', function() {
        updateSelectedTime();
    });
});

function updateSelectedTime() {
    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;
    const displayElement = document.getElementById('selected-time');
    
    if (startTime && endTime) {
        displayElement.textContent = `您選擇的時間範圍為：${startTime} ~ ${endTime}`;
    } else if (startTime || endTime) {
        displayElement.textContent = '請選擇完整的開始時間和結束時間。';
    } else {
        displayElement.textContent = '您尚未選擇開始時間和結束時間。';
    }
}

// 點擊「下一步」按鈕時，儲存選擇的開始時間和結束時間，並跳轉到 B.html
function goToNextPage() {
    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;
    if (startTime && endTime) {
        // 將開始時間和結束時間儲存在 localStorage 中
        localStorage.setItem('startTime', startTime);
        localStorage.setItem('endTime', endTime);
        // 跳轉到 B.html
        window.location.href = 'parking_booking_2.html';
    } else {
        alert('請選擇完整的開始時間和結束時間後再繼續。');
    }
}

// 從 localStorage 中取得選擇的開始時間和結束時間
const startTime = localStorage.getItem('startTime');
const endTime = localStorage.getItem('endTime');
const timeDisplayElement = document.getElementById('time-display');
const displayContainer = document.getElementById('display-time');

if (startTime && endTime) {
    // 顯示選擇的時間範圍
    timeDisplayElement.textContent = `${startTime} ~ ${endTime}`;
    displayContainer.textContent = `${startTime} ~ ${endTime}`;
} else {
    // 若無選擇時間範圍，提示使用者返回選擇頁面
    timeDisplayElement.textContent = '尚未選擇時間範圍，請返回上一頁進行選擇。';
    displayContainer.textContent = '尚未選擇任何時間範圍。';
}