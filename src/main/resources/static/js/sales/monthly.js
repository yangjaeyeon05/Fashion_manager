let revenueChart;
let saleAmountChart;
let incomeChart;
let days = [];
getMonthlyTable()

// 일자 배열 ([1 ... 30 / 31])
function daysArray(year, month) {
    let dayArray = []
    // 월의 첫 날을 설정
    let firstDay = new Date(year, month, 1);
    // 월의 마지막 날을 설정
    // 다음 달의 첫 날에서 하루를 빼서 현재 달의 마지막 날을 구한다
    let lastDay = new Date(year, month + 1, 0);
    // 첫 날부터 마지막 날까지 반복하여 일자를 배열에 추가한다
    for (let day = firstDay; day <= lastDay; day.setDate(day.getDate() + 1)) {
        dayArray.push(day.getDate()); // 일자를 배열에 추가
    }
    days = dayArray;
}

// 월단위 매출 조회 (레코드 : 일 단위)
function getMonthlyTable(){
    let yearMonth = document.querySelector(".yearMonth").value
    let year = yearMonth.split("-")[0]
    let month = yearMonth.split("-")[1]
    
    tableHTML = ""  // 테이블 HTML
    $.ajax({  // 테이블 데이터 가져오는 ajax
        async : false,
        method : "GET",
        url : "/sales/monthlytable",
        data : {year : year, month : month},
        success : r => {
            console.log("response");
            let chartLabel = []
            let revenueChartData = []
            let saleAmountChartData = []
            let incomeChartData = []
            daysArray(year, parseInt(month)-1)
            
            // 테이블 HTML 생성 루프
            for(let i = 1; i <= days.length; i++){
                let hasRecord = false
                r.forEach(dto => {
                    if(dto.day == i){
                        hasRecord = true
                        chartLabel.push(dto.day)
                        revenueChartData.push(dto.revenue)
                        saleAmountChartData.push(dto.saleAmount)
                        incomeChartData.push(dto.income)
                        tableHTML += `<td>${dto.day}</td><td>${dto.orders}</td><td>${dto.ordered}</td><td>${dto.returned}</td><td>${dto.canceled}</td><td>${dto.completed}</td><td>${currencyFormat(dto.revenue)}</td><td>${currencyFormat(dto.saleAmount)}</td><td>${currencyFormat(dto.income)}</td></tr>`
                        return
                    }
                })
                
                if (hasRecord == false) {
                    chartLabel.push(i)
                    revenueChartData.push(0)
                    saleAmountChartData.push(0)
                    incomeChartData.push(0)
                    tableHTML += `<td>${i}</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>${currencyFormat(0)}</td><td>${currencyFormat(0)}</td><td>${currencyFormat(0)}</td></tr>`
                }
            }
            
            // 완성된 HTML을 삽입
            document.querySelector("#tablePrintBox").innerHTML = tableHTML
            
            // 차트 생성
            createRevenueChart(chartLabel, revenueChartData,'총매출금액')
            createsaleAmountChart(chartLabel, saleAmountChartData,'할인금액')
            createIncomeChart(chartLabel, incomeChartData,'실주문금액')
        }
    })
}

// 차트 생성
function createRevenueChart(lables, data, name){
    if (revenueChart){
        revenueChart.destroy()
    }

    revenueChart = new Chart(document.getElementById('revenueChart'), {
        type: 'line',
        data: {
            labels: lables,
            datasets: [{
                label: name,
                data: data,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                fill: true
            }]
        },
        options: {
            // chart options
            responsive: true, // 화면 크기에 따라 차트 크기 조절
        }
    });
}

function createsaleAmountChart(lables, data, name){
    if (saleAmountChart){
        saleAmountChart.destroy()
    }

    saleAmountChart = new Chart(document.getElementById('saleAmountChart'), {
        type: 'line',
        data: {
            labels: lables,
            datasets: [{
                label: name,
                data: data,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                fill: true
            }]
        },
        options: {
            // chart options
            responsive: true, // 화면 크기에 따라 차트 크기 조절
        }
    });
}

function createIncomeChart(lables, data, name){
    if (incomeChart){
        incomeChart.destroy()
    }

    incomeChart = new Chart(document.getElementById('incomeChart'), {
        type: 'line',
        data: {
            labels: lables,
            datasets: [{
                label: name,
                data: data,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                fill: true
            }]
        },
        options: {
            // chart options
            responsive: true, // 화면 크기에 따라 차트 크기 조절
        }
    });
}


// 엑셀로 테이블 데이터 다운받기
function excelExport(){
    console.log('excelExport');
    var xhr = new XMLHttpRequest();
            xhr.open('GET', '/file/export/excel', true);
            xhr.responseType = 'blob'; // 서버에서 반환된 데이터를 Blob으로 처리
            xhr.onload = function() {
                if (xhr.status === 200) {
                    var disposition = xhr.getResponseHeader('Content-Disposition');
                    var filename = 'downloaded-file';
                    if (disposition && disposition.indexOf('filename=') !== -1) {
                        filename = disposition.split('filename=')[1].replace(/"/g, '');
                    }

                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(xhr.response);
                    link.download = filename;
                    document.body.appendChild(link); // 링크를 문서에 추가
                    link.click(); // 링크 클릭하여 다운로드
                    document.body.removeChild(link); // 다운로드 후 링크 제거

                    // 메모리 정리를 위한 객체 URL 해제
                    window.URL.revokeObjectURL(link.href);
                } else {
                    console.error('Download failed: ' + xhr.statusText);
                }
            };
            xhr.onerror = function() {
                console.error('Download failed');
            };
            xhr.send();
}


// 특정 열의 숫자를 원화 형식으로 변환
function currencyFormat(value) {
    return value.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' });
}

