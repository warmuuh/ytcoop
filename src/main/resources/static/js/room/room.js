function Room(videoId, element, isHost){
	this.player = new YtPlayer();
	this.player.load(videoId, element);
	
	this.socket = new WebsocketClient();
	this.socket.connect();
	if (isHost){
		var self = this;
		this.player.onPlay(function(){
			self.socket.send({command: 'PLAY'})
		});
	}
}

