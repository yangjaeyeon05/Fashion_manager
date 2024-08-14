orderall();

////출력
//function orderall(page = 1, size = 10){
//
//    $.ajax({
//        async : false,
//        method : "get",
//        url : "/order/getorder",
//        data : {page : page , size : size}, //데이터에 현재페이지 , 데이터출력사이즈 받아오기
//        success : r => {
//            console.log(r);
//
//                //페이지 데이터 및 페이지네이션 정보 가져 오기
//                let orders = r.data; //조호된 게시물 정보
//                let total = r.totalPage; //전체 페이지수
//                let currentPage = r.page; // 현재 페이지 번호
//
//                //어디에
//                let orderBox = document.querySelector('.orderBox');
//                //무엇을
//                html = '';
//
//                if (orders && orders.length > 0){ //if start
//                orders.forEach(ord => { //forEach start
//                    html +=   `
//                        <tr class="orderBox">
//                        <th> <input type="checkbox"> </th>
//                        <th> ${ord.orddetailcode} </th>
//                        <th> ${ord.memname} </th>
//                        <th> ${ord.orddate} </th>
//                        <th> ${ord.prodsize} </th>
//                        <th> ${ord.prodname} </th>
//                        <th> ${ord.ordamount} </th>
//                        <th> ${ord.ordstateStr} </th>
//                        <th> ${ord.coupname} </th>
//                        </tr>`
//                        }); //forEach end
//                        }; //if end
//                //목록 출력
//                orderBox.innerHTML = html;
//
//                //페이지 네이션 버튼 생성
//                let paginationBox = document.querySelector('.pagination');
//                let pageHTML = '';
//
//                if (total > 0){ //if start 총페이지수가 0 보다 많으면
//                for(let i = 1; i<= total; i++){ //for start 반복문을 돌려서
//                    pageHTML += `
//                    <li class="page-item ${i === currentPage ? 'active' : ''}">
//                    <a class="page-link" href="#" onclick="orderall(${i}, ${size})">${i}</a>
//                    </li>
//                    `;
//                    //페이지의 번호와 데이터에 맞는 버튼 생성
//
//                    } //for end
//                } //if end
//
//                //페이지네이션 버튼출력
//                paginationBox.innerHTML = pageHTML;
//        },
//        error : e => {console.log(e)}
//    })
//} //function end
//
////주문 날짜 검색
////function orddatesearch(){ //function start
////
////    let firstdate = document.querySelector('.firstdate').value
////    let todayDate = document.querySelector('#todayDate').value
////
////    console.log(firstdate);
////    console.log(todayDate);
////
////    $.ajax({
////        async : false,
////        method : "get",
////        url : "/order/manage",
////        data : { 'firstdate' : firstdate , 'todayDate' : todayDate},
////        success : r => {
////        console.log(r)
////
////                //어디에
////                let orderBox = document.querySelector('.orderBox');
////                //무엇을
////                html = '';
////                r.forEach(ord => {
////                    html +=   `
////                        <tbody class="orderBox">
////                        <th> <input type="checkbox"> </th>
////                        <th> ${ord.orddetailcode} </th>
////                        <th> ${ord.memname} </th>
////                        <th> ${ord.orddate} </th>
////                        <th> ${ord.prodsize} </th>
////                        <th> ${ord.prodname} </th>
////                        <th> ${ord.ordamount} </th>
////                        <th> ${ord.ordstateStr} </th>
////                        <th> ${ord.coupname} </th>
////                        </tbody>`
////                        });
////                //출력
////                orderBox.innerHTML = html;
////
////        } //success end
////    }) //ajax end
//
//
////} //function end
//
////카테고리 기능 추가 0807
//function ordcategory(){
//    let ordcatagory = document.querySelector('.ordcatagory').value
//
//    console.log(ordcatagory)
//
//   $.ajax({
//        async : false,
//        method : "get",
//        url : "/order/manage2",
//        data : {ordcatagory : ordcatagory},
//        success : r =>{
//        console.log(r)
//        if(r == 0) {orderall();}
//        else {
//        //어디에
//        let orderBox = document.querySelector('.orderBox');
//        //무엇을
//        html = '';
//        r.forEach(ord => {
//        html +=   `
//                    <tbody class="orderBox">
//                    <th> <input type="checkbox"> </th>
//                    <th> ${ord.orddetailcode} </th>
//                    <th> ${ord.memname} </th>
//                    <th> ${ord.orddate} </th>
//                    <th> ${ord.prodsize} </th>
//                    <th> ${ord.prodname} </th>
//                    <th> ${ord.ordamount} </th>
//                    <th> ${ord.ordstateStr} </th>
//                    <th> ${ord.coupname} </th>
//                    </tbody>`
//                    });
//        //출력
//         orderBox.innerHTML = html;
//            }
//        } //success end
//   }) //ajax end
//} //function end

//페이지 전체 검색 종합
function orderall(page = 1, size = 10){

 let firstdate = document.querySelector('.firstdate').value;
 let todayDate = document.querySelector('#todayDate').value;
 let ordcatagory = document.querySelector('.ordcatagory').value;

    console.log(firstdate);
    console.log(todayDate);
    console.log(ordcatagory);


    $.ajax({
        async : false,
        method : "get",
        url : "/order/manage",
        data : {ordcatagory : ordcatagory, page : page , size : size ,firstdate : firstdate, todayDate : todayDate}, //데이터에 현재페이지 , 데이터출력사이즈 받아오기
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
                        <th> <input type="checkbox"> </th>
                        <th> ${ord.orddetailcode} </th>
                        <th> ${ord.memname} </th>
                        <th> ${ord.orddate} </th>
                        <th> ${ord.prodsize} </th>
                        <th> ${ord.prodname} </th>
                        <th> ${ord.ordamount} </th>
                        <th> ${ord.ordstateStr} </th>
                        <th> ${ord.coupname} </th>
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
