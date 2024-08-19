
getColorSizeTable()

// 연단위 매출 조회 (레코드 단위 : 연도)
function getColorSizeTable(){
    let startDate = document.querySelector(".startDate").value
    let endDate = document.querySelector(".endDate").value
    
    tableHTML = ""  // 테이블 HTML
    $.ajax({  // 테이블 데이터 가져오는 ajax
        async : false,
        method : "GET",
        url : "/sales/colorsizetable",
        data : {startDate : startDate, endDate : endDate},
        success : r => {
            console.log(r);
            
            // 테이블 HTML 생성 루프
            r.forEach(dto => {                  
                tableHTML += `<td>${dto.prodcode}</td><td>${dto.prodname}</td><td>${dto.colorsize.S}</td><td>${dto.colorsize.M}</td><td>${dto.colorsize.L}</td><td>${dto.colorsize.XL}</td><td>${dto.colorsize.XXL}</td></tr>`
            })
            // 완성된 HTML을 삽입
            document.querySelector("#tablePrintBox").innerHTML = tableHTML
        }
    })
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

