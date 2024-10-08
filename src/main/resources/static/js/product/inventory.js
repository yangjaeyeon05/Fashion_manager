console.log("inventory.js");


inventoryRead();


function inventoryRead(page = 1, size = 10) {
    let inventoryArea = document.querySelector("#inventoryArea");


    let html = ``;

    $.ajax({
        async: false,
        method: "get",
        url: "/inventory/read",
        data: { page: page, size: size },
        success: function response(result) {


            console.log(result);
            let inventory = result.data;
            let total = result.totalPage; //전체 페이지수
            let currentPage = result.page; // 현재 페이지 번호

            if (inventory && inventory.length > 0) {
                inventory.forEach(r => {

                    html += `<tr>
                            <td>
                                ${r.prodName}
                            </td>
                            <td>
                                ${r.prodGender}
                            </td>
                            <td>
                                ${r.prodSize}
                            </td>
                            <td>
                                ${r.prodAmount}
                            </td>
                            <td>
                                <select id="inventoryLog">
                                    <option value="1"> 재고 입고</option>
                                    <option value="2"> 판매 </option>
                                    <option value="3"> 취소 </option>
                                    <option value="4"> 환불 </option>
                                </select>
                                <input type="text" id="inventoryChange${r.prodDetailcode}">
                                <button type="button" onclick="inventoryLog(${r.prodDetailcode})"> 재고 현황 업데이트 </button>
                            </td>
                            <td>
                                <button type="button" onclick="inventoryChart(${r.prodDetailcode})"> 재고 현황 그래프 </button>
                            </td>
                            `
                    // ===================================  2024-08-13 김민석 ========================================= //
                    $.ajax({
                        async: false,
                        method: 'get',
                        url: '/inventory/alarm',
                        data: { proddetailcode: r.prodDetailcode },
                        success: function response(result) {
                            html += `
                            <td>
                                ${result}                            
                            </td>
                            </tr>
                            `;
                        }
                    }) // ajax2 end
                    // ===================================  2024-08-13 김민석 ========================================= //
                }); // forEach end 
            }
            let paginationBox = document.querySelector('.pagination');
            let pageHTML = '';

            if (total > 0) { //if start 총페이지수가 0 보다 많으면
                for (let i = 1; i <= total; i++) { //for start 반복문을 돌려서
                    pageHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="inventoryRead(${i}, ${size})">${i}</a>
                    </li>
                    `;
                    //페이지의 번호와 데이터에 맞는 버튼 생성

                } //for end
            } //if end

            //페이지네이션 버튼출력

            inventoryArea.innerHTML = html;
            paginationBox.innerHTML = pageHTML;


            // let myCt = document.getElementById(`myChart`)
            // $.ajax({
            //     async: false,
            //     method: "get",
            //     url: "/inventory/chart",
            //     data: { proddetailcode: 1 },
            //     success: function response(result) {
            //         chartXlabel = [];
            //         chartYlabel = [];
            //         console.log(result);
            //         result.forEach(r => {
            //             chartXlabel.push(r.invdate);
            //             chartYlabel.push(r.prodAmount);
            //         })
            //         console.log(chartXlabel);
            //         console.log(chartYlabel);

            //         let myChart = new Chart(myCt, {
            //             type: 'bar',
            //             data: {
            //                 labels: chartXlabel,
            //                 datasets: [
            //                     {
            //                         label: '일별 재고 현황',
            //                         data: chartYlabel,
            //                     }
            //                 ]
            //             },
            //         });


            //     }
            // })

        } // ajax1 success end 
    }) // ajax1 end 

}


// ===================================  2024-08-08 김민석 ========================================= //

function inventoryLog(prodDetailcode) {
    let invlogchange = document.querySelector(`#inventoryChange${prodDetailcode}`).value;
    let invlogdetail = document.querySelector("#inventoryLog").value;

    // 수량 미 입력시
    if (invlogchange == '') {
        alert('수량입력 해주세요');
        return;
    }

    $.ajax({
        async: false,
        method: "post",
        url: "/inventory/update",
        contentType: "application/json",
        data: JSON.stringify({ proddetailcode: prodDetailcode, invlogchange: invlogchange, invlogdetail: invlogdetail }),
        success: function response(result) {
            console.log(result);
            if (result) {
                console.log("재고 업데이트 성공");
                inventoryRead();
            } else {
                console.log("재고 업데이트 실패");
            }
        }
    });
}


//
// ===================================  2024-08-12 김민석 ========================================= //

function inventoryChart(prodDetailcode) {
    let chartStatus = Chart.getChart('myChart');
    if (chartStatus !== undefined) {
        chartStatus.destroy();
    }
    document.querySelector('.modal_btn').click() // 강제 버튼 클릭. 모달 열기 

    let myCt = document.getElementById(`myChart`);
    $.ajax({
        async: false,
        method: "get",
        url: "/inventory/chart",
        data: { proddetailcode: prodDetailcode },
        success: function response(result) {
            chartXlabel = [];
            chartYlabel = [];
            console.log(result);
            result.forEach(r => {
                chartXlabel.push(r.invdate);
                chartYlabel.push(r.prodAmount);
            })
            console.log(chartXlabel);
            console.log(chartYlabel);
            let myChart = new Chart(myCt, {
                type: 'bar',
                data: {
                    labels: chartXlabel,
                    datasets: [
                        {
                            label: '일별 재고 현황',
                            data: chartYlabel,
                        }
                    ]
                },
            });
        }
    })
}

// ===================================  2024-08-14 김민석 ========================================= //



const modal = document.querySelector('.modal');
const modalOpen = document.querySelector('.modal_btn');
const modalClose = document.querySelector('.close_btn');

//열기 버튼을 눌렀을 때 모달팝업이 열림
modalOpen.addEventListener('click', function () {
    //'on' class 추가
    modal.classList.add('on');
});
//닫기 버튼을 눌렀을 때 모달팝업이 닫힘
modalClose.addEventListener('click', function () {
    //'on' class 제거
    modal.classList.remove('on');
});