orderall();

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
                        <th> ${ord.ordstateStr} </th>
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
                        <th> ${ord.ordstateStr} </th>
                        <th> ${ord.coupname} </th>
                        </tbody>`
                        });
                //출력
                orderBox.innerHTML = html;

        } //success end
    }) //ajax end


} //function end

//카테고리 기능 추가 0807
function ordcategory(){
    let ordcatagory = document.querySelector('.ordcatagory').value

    console.log(ordcatagory)

   $.ajax({
        async : false,
        method : "get",
        url : "/order/manage2",
        data : {ordcatagory : ordcatagory},
        success : r =>{
        console.log(r)
        if(r == 0) {orderall();}
        else {
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
                    <th> ${ord.ordstateStr} </th>
                    <th> ${ord.coupname} </th>
                    </tbody>`
                    });
        //출력
         orderBox.innerHTML = html;
            }
        } //success end
   }) //ajax end
} //function end

function ordstatechange(){

}