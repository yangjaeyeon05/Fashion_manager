let incomeCalcChart;
let completedCalcChart;

getCompareDatesTable()

// 대비기간매출
function getCompareDatesTable(){
    let firstDateStart = document.querySelector(".firstDateStart").value
    let firstDateEnd = document.querySelector(".firstDateEnd").value
    let secondDateStart = document.querySelector(".secondDateStart").value
    let secondDateEnd = document.querySelector(".secondDateEnd").value

    tableHTML = ""  // 테이블 HTML
    $.ajax({  // 테이블 데이터 가져오는 ajax
        async : false,
        method : "GET",
        url : "/sales/comparedatestable",
        data : {firstDateStart : firstDateStart, 
            firstDateEnd : firstDateEnd, 
            secondDateStart : secondDateStart, 
            secondDateEnd : secondDateEnd},
        success : r => {

            let chartLabel = []
            let incomeCalcChartData = []
            let completedCalcChartData = []

            // 테이블 HTML 생성 루프
            r.forEach(dto => {                
                chartLabel.push(dto.prodcatename)
                incomeCalcChartData.push(dto.incomecalc)
                completedCalcChartData.push(dto.completedcalc)
                tableHTML += `<td>${dto.prodcatename}</td><td>${dto.firstcompleted}</td><td>${currencyFormat(dto.firstincome)}</td><td>${dto.secondcompleted}</td><td>${currencyFormat(dto.secondincome)}</td><td>${dto.completedcalc}</td><td>${dto.incomecalc}</td></tr>`
            })
            
            // 완성된 HTML을 삽입
            document.querySelector("#tablePrintBox").innerHTML = tableHTML
            
            // 차트 생성
            createIncomeCalcChart(chartLabel, incomeCalcChartData,'실매출신장률')
            createCompletedCalcChart(chartLabel, completedCalcChartData,'실판매수량신장률')
        
        }
    })
}

// 차트 생성
function createIncomeCalcChart(lables, data, name){
    if (incomeCalcChart){
        incomeCalcChart.destroy()
    }

    incomeCalcChart = new Chart(document.getElementById('incomeCalcChart'), {
        type: 'bar',
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

function createCompletedCalcChart(lables, data, name){
    if (completedCalcChart){
        completedCalcChart.destroy()
    }

    completedCalcChart = new Chart(document.getElementById('completedCalcChart'), {
        type: 'bar',
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

