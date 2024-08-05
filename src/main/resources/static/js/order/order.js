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
                console.log("1");
                //무엇을
                html = '';
                console.log("2");
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

//등록
function orderadd(){

}