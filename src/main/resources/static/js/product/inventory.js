console.log("inventory.js");


inventoryRead();
function inventoryRead() {
    let inventoryArea = document.querySelector("#inventoryArea");
    let html = ``;

    $.ajax({
        async: false,
        method: "get",
        url: "/inventory/read",
        success: function response(result) {
            console.log(result);
            result.forEach(r => {
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
                                <button type="button" onclick="inventoryChart(${r.prodDetailcode})"> 재고 업데이트 그래프 </button>
                                <canvas id="myChart${r.prodDetailcode}" width="300" height="100"></canvas>
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
            inventoryArea.innerHTML = html;
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
    let myCt = document.getElementById(`myChart${prodDetailcode}`);
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



