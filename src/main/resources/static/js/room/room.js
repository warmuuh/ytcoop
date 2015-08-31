function Room(elementIdStr, isHost){
	this.player = new YtPlayer();
	var playerEle = $('#'+elementIdStr);
	var videoId = playerEle.data('video-id');
	var roomId = playerEle.data('room-id');
	this.player.load(videoId, elementIdStr);
	
	this.socket = new WebsocketClient(roomId);
	this.socket.connect();
	var self = this;
	if (isHost){
		
		this.player.on('play', function(){
			self.socket.send({command: 'PLAY'})
		});
		this.player.on('pause', function(){
			self.socket.send({command: 'PAUSE'})
		});
	} else {
		this.socket.onMessage(function(msg){
			switch (msg.command){
			case 'PLAY': 
				self.player.play(); 
				break;
			case 'PAUSE':
				self.player.pause(); 
				break;
			default:
				console.log("Unknown command:", msg)
			}
		})
	}
}

