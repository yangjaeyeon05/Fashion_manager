orderall() //주문취소 목록출력
returnOrderall() //취소확인 목록 출력

function orderall(page = 1, size = 10){

 let firstdate = document.querySelector('.firstdate').value;
 let todayDate = document.querySelector('#todayDate').value;

    console.log(firstdate);
    console.log(todayDate);


    $.ajax({
        async : false,
        method : "get",
        url : "/order/ordReturn",
        data : {page : page , size : size ,firstdate : firstdate, todayDate : todayDate}, //데이터에 현재페이지 , 데이터출력사이즈 받아오기
        success : r => {
            console.log(r);


                //페이지 데이터 및 페이지네이션 정보 가져 오기
                let orders = r.data; //조호된 게시물 정보
                let total = r.totalPage; //전체 페이지수
                let currentPage = r.page; // 현재 페이지 번호

                //어디에
                let orderBox = document.querySelector('.orderBox');
                //무엇을
                html = '';

                if (orders && orders.length > 0){ //if start
                orders.forEach(ord => { //forEach start
                    html +=   `
                        <tr class="orderBox">
                        <th> ${ord.orddetailcode} </th>
                        <th> ${ord.memname} </th>
                        <th> ${ord.orddate} </th>
                        <th> ${ord.prodsize} </th>
                        <th> ${ord.prodname} </th>
                        <th> ${ord.ordamount} </th>
                        <th> ${ord.ordstateStr} </th>
                        <th> ${ord.coupname} </th>
                        <th> <button type="button" class="cancelButton" onclick="returnCheck(${ord.orddetailcode})"> 취소확정 </button> </th>
                        </tr>`
                        }); //forEach end
                        }; //if end
                //목록 출력
                orderBox.innerHTML = html;

                //페이지 네이션 버튼 생성
                let paginationBox = document.querySelector('.pagination');
                let pageHTML = '';

                if (total > 0){ //if start 총페이지수가 0 보다 많으면
                for(let i = 1; i<= total; i++){ //for start 반복문을 돌려서
                    pageHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="orderall(${i}, ${size})">${i}</a>
                    </li>
                    `;
                    //페이지의 번호와 데이터에 맞는 버튼 생성

                    } //for end
                } //if end

                //페이지네이션 버튼출력
                paginationBox.innerHTML = pageHTML;
        },
        error : e => {console.log(e)}
    })
} //function end

//취소 확정 함수
function returnCheck(orddetailcode){console.log('returnCheck()')

    console.log(orddetailcode)

    $.ajax({
        async : false,
        method : "put",
        url : "/order/returnCheck",
        data : {orddetailcode: orddetailcode},
        success : r => {console.log(r)
            if(r){alert("반품 처리 완료");
            orderall(); returnOrderall(); }
            else {alert("반품 처리 실패")
            orderall(); returnOrderall(); }
        } //success end

    }) //ajax end
} //function end

//반품 완료 호출 함수
function returnOrderall(page = 1 ,size = 10){

    $.ajax({
        async : false,
        method : "get",
        url : "/order/returnOrd",
        data : {page : page , size : size}, //데이터에 현재페이지 , 데이터출력사이즈 받아오기
        success : r => {
            console.log(r);

                //페이지 데이터 및 페이지네이션 정보 가져 오기
                let orders = r.data; //조호된 게시물 정보
                let total = r.totalPage; //전체 페이지수
                let currentPage = r.page; // 현재 페이지 번호

            //어디에
            let returnCheckBox = document.querySelector('.returnCheckBox');
            //무엇을
            html = '';

                if (orders && orders.length > 0){ //if start
                    orders.forEach(ord => { //forEach start
                    html +=   `
                        <tr class="returnCheckBox">
                        <th> ${ord.orddetailcode} </th>
                        <th> ${ord.memname} </th>
                        <th> ${ord.orddate} </th>
                        <th> ${ord.prodsize} </th>
                        <th> ${ord.prodname} </th>
                        <th> ${ord.ordamount} </th>
                        <th> ${ord.ordstateStr} </th>
                        <th> ${ord.coupname} </th>
                        <th> 취소 확정 <th>
                        </tr>`
                        }); //forEach end
                        }; //if end

                        //연결
                        returnCheckBox.innerHTML = html;

                    //페이지 네이션 버튼 생성
                    let paginationBox = document.querySelector('#paginaition');
                    let pageHTML = '';

                    if (total > 0){ //if start 총페이지수가 0 보다 많으면
                    for(let i = 1; i<= total; i++){ //for start 반복문을 돌려서
                    pageHTML += `
                   <li class="page-item ${i === currentPage ? 'active' : ''}">
                   <a class="page-link" href="#" onclick="returnOrderall(${i}, ${size})">${i}</a>
                   </li>
                   `;
                   //페이지의 번호와 데이터에 맞는 버튼 생성

                   } //for end
                   } //if end

                   //페이지네이션 버튼출력
                   paginationBox.innerHTML = pageHTML;

        }, //success end
        error : e => {console.log(e)}
    })
} //function end