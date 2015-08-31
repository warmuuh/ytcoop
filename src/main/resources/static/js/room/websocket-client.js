

function WebsocketClient(room){
	this.endpoint = '/command';
	this.channel = '/app/room/'+room+'/command';
	this.topic = '/topic/room/'+room+'/command';
	this.participants = '/app/room/'+room+'/participants';
	this.stompClient = null;
	this.onParticipantListeners = [];
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


WebsocketClient.prototype.onParticipants = function(participantListener){
	this.onMessageListeners.push(participantListener);
}

WebsocketClient.prototype.notifyListenersOnParticipants = function(msg){
	for(var i = 0; i < this.onParticipantListeners.length; ++i){
		this.onParticipantListeners[i](msg);
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
        
        self.stompClient.subscribe(self.participants, function(stompMsg){
        	var msg = JSON.parse(stompMsg.body);
        	console.log("received participants: ", msg)
        	self.notifyListenersOnParticipants(msg);
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

