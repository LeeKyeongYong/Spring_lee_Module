class TradeWebSocket {
    constructor() {
        this.socket = null;
        this.subscribers = new Map();
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.connect();
    }

    connect() {
        const wsUrl = `ws://${window.location.host}/ws/trade`;
        this.socket = new WebSocket(wsUrl);

        this.socket.onopen = () => {
            console.log('WebSocket 연결 성공');
            this.reconnectAttempts = 0;

            // 기존 구독 정보 복구
            this.subscribers.forEach((callback, coinCode) => {
                this.subscribeToCoin(coinCode);
            });
        };

        this.socket.onmessage = (event) => {
            const message = JSON.parse(event.data);

            if (message.type === 'hoga') {
                const callback = this.subscribers.get(message.coinCode);
                if (callback) {
                    callback(message.data);
                }
            }
        };

        this.socket.onclose = () => {
            console.log('WebSocket 연결 종료');
            this.handleReconnect();
        };

        this.socket.onerror = (error) => {
            console.error('WebSocket 에러:', error);
        };
    }

    handleReconnect() {
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(`재연결 시도 ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
            setTimeout(() => this.connect(), 3000); // 3초 후 재연결
        }
    }

    subscribe(coinCode, callback) {
        this.subscribers.set(coinCode, callback);
        if (this.socket.readyState === WebSocket.OPEN) {
            this.subscribeToCoin(coinCode);
        }
    }

    subscribeToCoin(coinCode) {
        this.socket.send(JSON.stringify({
            type: 'SUBSCRIBE',
            coinCode: coinCode
        }));
    }

    unsubscribe(coinCode) {
        this.subscribers.delete(coinCode);
        if (this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify({
                type: 'UNSUBSCRIBE',
                coinCode: coinCode
            }));
        }
    }
}