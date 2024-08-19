console.log("index.js");


doLoginCheck();


function goLogin() {
    location.href = "/login";
}

function doLoginCheck() {
    $.ajax({
        async: false,
        method: "get",
        url: "/admin/login/check",
        success: function response(result) {
            if (result == '') {
                console.log("비 로그인 상태");
                alert('로그인 후 이용가능합니다.')
                location.href="/login"
            } else {
                console.log("로그인 상태");
            }
        }
    })
}

// 최근1주일매출) 테이블에 오늘부터 일주일 전까지 날짜 및 데이터 가져오는 함수
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
