

function WebsocketClient(){
	this.endpoint = '/command';
	this.channel = '/app/command';
	this.topic = '/topic/command';
	this.stompClient = null;
	this.onMessageListeners = [];
}

WebsocketClient.prototype.onMessage = function(messageListener){
	this.onMessageListeners.push(messageListener);
}

WebsocketClient.prototype.notifyListenersOnMessage = function(msg){
	for(var i = 0; i < this.onMessageListeners.length; ++i){
		this.onMessageListeners[i](msg);
	}
}

WebsocketClient.prototype.connect = function() {
    var socket = new SockJS(this.endpoint);
    this.stompClient = Stomp.over(socket);
    var self = this;
    this.stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        self.stompClient.subscribe(self.topic, function(stompMsg){
        	var msg = JSON.parse(stompMsg.body);
        	console.log("received command: ", msg)
        	self.notifyListenersOnMessage(msg);
        });
    });
}

WebsocketClient.prototype.disconnect = function () {
    if (this.stompClient != null) {
        this.stompClient.disconnect();
    }
    console.log("Disconnected");
}



WebsocketClient.prototype.send = function(msg) {
	console.log("Sending command:", msg)
    this.stompClient.send(this.channel, {}, JSON.stringify(msg));
}

