console.log('support.js');

getSalesData()

// 테이블에 오늘부터 일주일 전까지 날짜 및 데이터 가져오는 함수
function getSalesData(){
    let today = new Date();  // 오늘 날짜
    let date = new Date(today)  // 오늘 날짜 복사, 날짜 계산 용도
    console.log(date);
    table = ""  // 테이블 HTML
    $.ajax({  // 테이블 데이터 가져오는 ajax
        async : false,
        method : "GET",
        url : "/sales/totaltable",
        success : r => {
            console.log(r);
            // 2000-00-00 방식으로 잘라낸 날짜 목록
            let formatDates = []
            // DTO 필터링할 날짜 목록
            let days = []
            // 필터링된 DTO 배열
            let records = []
            // 최근 일주일 날짜가 있는 테이블을 만들고 조회한 날짜 레코드가 있으면 채우고 없으면 0으로 채우기
            // 테이블에 적을 날짜 목록 7칸
            for(let i = 0; i < 7; i++){
                date.setDate(today.getDate() - i);
                let formatDate = date.toISOString().split("T")[0] // 2000-00-00
                let formatDay = formatDate.split("-")[2]
                days.push(parseInt(formatDay))
                formatDates.push(formatDate)
            }
            console.log(days);
            r.forEach(dto => {
                console.log("dto day: " + dto.day);
                // 입력할 레코드를 날짜를 이용해 필터링
                if (days.includes(dto.day)){
                    console.log("filtered");
                    records.push(dto)
                } 
            })
            console.log(records);
            // 테이블 HTML 생성 루프
            for (let i = 0; i < 7; i++){
                console.log("for문 루프중 " + formatDates[i]);
                // 항상 날짜 행 7개 생성
                table += `<tr><td>${formatDates[i]}</td>`
                // 모든 칸을 0으로 채울지 체크
                let hasData = false
                // 필터링한 DTO 목록을 순회하며 날짜가 맞으면 데이터 입력
                records.forEach(dto => {
                    if (dto.day == days[i]){
                        table += `<td>${dto.orders}</td><td>${dto.ordered}</td><td>${dto.returned}</td><td>${dto.canceled}</td><td>${dto.completed}</td><td>${dto.revenue}</td><td>${dto.saleAmount}</td><td>${dto.income}</td></tr>`
                        hasData = true
                    }
                })
                // forEach 루프 후 데이터가 안 들어있다 = 0으로 채울 칸
                if (hasData == false){
                    table += `<td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>`
                }
            }
        }
    })
    // 완성된 HTML을 삽입
    document.querySelector("#tableBox").innerHTML = table
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

// 답변 내용 수정
function createBox(replycode , supcode){
    console.log('replyUpdate()');
    // 수정을 위한 텍스트 입력 창 먼저 출력
    // 어디에 
    let replyUpdatebox = document.querySelector(".replyUpdatebox");
    // 무엇을
    let html = `
                <th> 답변수정내용  </th>
                <td colspan="5"> 
                <div class="updateBox">
                <input type="text" style="width:80%;"class="replycontent" placeholder="수정할 내용을 입력하세요" /> 
                <button type="button" class="btn btn-success btn-sm" onclick="submitUpdate(${replycode} , ${supcode})">수정등록</button>
                </div>
                </td>
                `;
    // 출력
    replyUpdatebox.innerHTML = html;
    replyUpdate(replycode , supcode);
}   // createBox() end

// 답변 수정 요청을 서버로 보내는 함수
function submitUpdate(replycode , supcode){
    // 출력 후 입력된 값 가져오기
    let replycontent = document.querySelector(".replycontent").value;
    console.log(replycontent);
    // 입력 내용이 비어 있는 경우 경고 메시지
    if (!replycontent) {
        alert('답변 내용을 입력해 주세요.');
        return;
    }
    // 객체 만들어서 전달
    let info = {
        replycontent : replycontent , replycode : replycode
    }
    console.log(info);
    // ajax 통신
    $.ajax({
        async : false , 
        method : "put" , 
        url : "/support/respedit" , 
        data : JSON.stringify(info) , // 객체를 JSON 문자열로 변환
        contentType : "application/json" , 
        success : (r) => {
            console.log(r);
            if(r){
                alert('답변수정성공');
                replyRead(supcode);     // 새로고침
                supDetailRead(supcode);
            }else{
                alert('답변수정실패')
            }
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // submitUpdate() end