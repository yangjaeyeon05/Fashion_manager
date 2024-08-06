orderall();

let pageInfo = {page : 1, bcno : 0, searchKey : 'btitle', searchKeyword : ''};

//페이지 관리 전역변수
let orderPageInfo = {}

//출력
function orderall(){

    $.ajax({
        async : false,
        method : "get",
        url : "/order/getorder",
        success : r => {
            console.log(r);

                //어디에
                let orderBox = document.querySelector('.orderBox');
                //무엇을
                html = '';
                r.forEach(ord => {
                    html +=   `
                        <tbody class="orderBox">
                        <th> <input type="checkbox"> </th>
                        <th> ${ord.orddetailcode} </th>
                        <th> ${ord.memname} </th>
                        <th> ${ord.orddate} </th>
                        <th> ${ord.prodsize} </th>
                        <th> ${ord.prodname} </th>
                        <th> ${ord.ordamount} </th>
                        <th> ${ord.ordstate} </th>
                        <th> ${ord.coupname} </th>
                        </tbody>`
                        });
                //출력
                orderBox.innerHTML = html;
        }
    })


}

//주문 날짜 검색
function orddatesearch(){ //function start

    let firstdate = document.querySelector('.firstdate').value
    let todayDate = document.querySelector('#todayDate').value

    console.log(firstdate);
    console.log(todayDate);

    $.ajax({
        async : false,
        method : "get",
        url : "/order/manage",
        data : { 'firstdate' : firstdate , 'todayDate' : todayDate},
        success : r => {
        console.log(r)

                //어디에
                let orderBox = document.querySelector('.orderBox');
                //무엇을
                html = '';
                r.forEach(ord => {
                    html +=   `
                        <tbody class="orderBox">
                        <th> <input type="checkbox"> </th>
                        <th> ${ord.orddetailcode} </th>
                        <th> ${ord.memname} </th>
                        <th> ${ord.orddate} </th>
                        <th> ${ord.prodsize} </th>
                        <th> ${ord.prodname} </th>
                        <th> ${ord.ordamount} </th>
                        <th> ${ord.ordstate} </th>
                        <th> ${ord.coupname} </th>
                        </tbody>`
                        });
                //출력
                orderBox.innerHTML = html;

        } //success end
    }) //ajax end


} //function end

function ordcategory(){
    let ordcatagory = document.querySelector('.ordcatagory').value

    console.log(ordcatagory)

   $.ajax({
        async : false,
        method : "get",
        url : "/order/manage2",
        data : {'ordcatagory' : ordcatagory}
        success : r =>{
        console.log(r)

        //어디에
        let orderBox = document.querySelector('.orderBox');
        //무엇을
        html = '';
        r.forEach(ord => {
        html +=   `
                    <tbody class="orderBox">
                    <th> <input type="checkbox"> </th>
                    <th> ${ord.orddetailcode} </th>
                    <th> ${ord.memname} </th>
                    <th> ${ord.orddate} </th>
                    <th> ${ord.prodsize} </th>
                    <th> ${ord.prodname} </th>
                    <th> ${ord.ordamount} </th>
                    <th> ${ord.ordstate} </th>
                    <th> ${ord.coupname} </th>
                    </tbody>`
                    });
        //출력
         orderBox.innerHTML = html;

        } //success end
   }) //ajax end
} //function end