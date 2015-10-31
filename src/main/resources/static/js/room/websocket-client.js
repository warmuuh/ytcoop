
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

function WebsocketClient(room){
	this.endpoint = '/command';
	this.channel = '/app/room/'+room+'/command';
	this.topic = '/topic/room/'+room+'/command';
	this.participants = '/topic/room/'+room+'/participants';
	this.stompClient = null;
	this.onParticipantListeners = [];
	this.onMessageListeners = [];
	this.headers = function(){return {
									auth: getCookie("AUTH-TOKEN")
								};
							};
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
	this.onParticipantListeners.push(participantListener);
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
    this.stompClient.connect(this.headers(), function(frame) {
        console.log('Connected: ' + frame);
        self.stompClient.subscribe(self.topic, function(stompMsg){
        	var msg = JSON.parse(stompMsg.body);
        	console.log("received command: ", msg)
        	self.notifyListenersOnMessage(msg);
        }, self.headers());
        
        self.stompClient.subscribe(self.participants, function(stompMsg){
        	var msg = JSON.parse(stompMsg.body);
        	console.log("received participants: ", msg)
        	self.notifyListenersOnParticipants(msg);
        }, self.headers());
        
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
    this.stompClient.send(this.channel, this.headers(), JSON.stringify(msg), this.headers());
}

