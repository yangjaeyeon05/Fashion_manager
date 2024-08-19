let revenueChart;
let saleAmountChart;
let incomeChart;

getTotalTable()

// 연단위 매출 조회 (레코드 단위 : 연도)
function getTotalTable(){
    tableHTML = ""  // 테이블 HTML
    $.ajax({  // 테이블 데이터 가져오는 ajax
        async : false,
        method : "GET",
        url : "/sales/totaltable",
        success : r => {
            console.log("response");
            let chartLabel = []
            let revenueChartData = []
            let saleAmountChartData = []
            let incomeChartData = []
            // 테이블 HTML 생성 루프
            r.forEach(dto => {                
                chartLabel.push(dto.year)
                revenueChartData.push(dto.revenue)
                saleAmountChartData.push(dto.saleAmount)
                incomeChartData.push(dto.income)
                tableHTML += `<td>${dto.year}</td><td>${dto.orders}</td><td>${dto.ordered}</td><td>${dto.returned}</td><td>${dto.canceled}</td><td>${dto.completed}</td><td>${currencyFormat(dto.revenue)}</td><td>${currencyFormat(dto.saleAmount)}</td><td>${currencyFormat(dto.income)}</td></tr>`
            })
            console.log(chartLabel);
            console.log(revenueChartData);
            console.log(saleAmountChartData);
            console.log(incomeChartData);

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

