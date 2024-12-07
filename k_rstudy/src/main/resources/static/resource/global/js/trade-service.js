class TradeService {
    constructor() {
        this.webSocket = new TradeWebSocket();
        this.currentCoin = null;
        this.initializeEventListeners();
    }

    initializeEventListeners() {
        // 코인 선택 이벤트
        document.querySelectorAll('.coin-select').forEach(element => {
            element.addEventListener('click', (e) => {
                const coinCode = e.target.dataset.coinCode;
                this.selectCoin(coinCode);
            });
        });

        // 주문 폼 제출 이벤트
        const buyForm = document.querySelector('#buyForm');
        if (buyForm) {
            buyForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitBuyOrder(new FormData(buyForm));
            });
        }

        // 가격/수량 입력 시 총액 자동 계산
        const priceInput = document.querySelector('.price-input');
        const quantityInput = document.querySelector('.quantity-input');
        const totalInput = document.querySelector('[data-bind="total"]');

        if (priceInput && quantityInput && totalInput) {
            const calculateTotal = () => {
                const price = parseFloat(priceInput.value) || 0;
                const quantity = parseFloat(quantityInput.value) || 0;
                totalInput.value = (price * quantity).toLocaleString();
            };

            priceInput.addEventListener('input', calculateTotal);
            quantityInput.addEventListener('input', calculateTotal);
        }
    }

    selectCoin(coinCode) {
        // 이전 구독 해제
        if (this.currentCoin) {
            this.webSocket.unsubscribe(this.currentCoin);
        }

        this.currentCoin = coinCode;

        // 새로운 코인 정보 구독
        this.webSocket.subscribe(coinCode, (hogaData) => {
            this.updateHogaDisplay(hogaData);
        });

        // 코인 정보 업데이트
        fetch(`/api/v1/coins/${coinCode}`)
            .then(response => response.json())
            .then(coinInfo => {
                this.updateCoinInfo(coinInfo);
            });
    }

    updateHogaDisplay(hogaData) {
        const hogaList = document.querySelector('#hogaList tbody');
        if (!hogaList) return;

        // 호가 데이터 업데이트
        hogaList.innerHTML = hogaData.map(hoga => `
            <tr class="${hoga.type === 'SELL' ? 'sell' : 'buy'}">
                <td class="price">${hoga.price.toLocaleString()}</td>
                <td class="quantity">${hoga.quantity.toLocaleString()}</td>
            </tr>
        `).join('');
    }

    async submitBuyOrder(formData) {
        try {
            const response = await fetch('/api/v1/orders/buy', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(Object.fromEntries(formData))
            });

            if (!response.ok) {
                throw new Error('주문 처리 실패');
            }

            const result = await response.json();
            this.showOrderResult(result);
        } catch (error) {
            this.showError(error.message);
        }
    }

    showOrderResult(result) {
        // Sweet Alert2를 사용한 결과 표시
        Swal.fire({
            icon: 'success',
            title: '주문이 접수되었습니다',
            text: `주문번호: ${result.orderId}`,
            timer: 2000
        });
    }

    showError(message) {
        Swal.fire({
            icon: 'error',
            title: '오류',
            text: message
        });
    }
}

// 페이지 로드 시 TradeService 인스턴스 생성
document.addEventListener('DOMContentLoaded', () => {
    window.tradeService = new TradeService();
});