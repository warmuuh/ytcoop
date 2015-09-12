function BulletOverlay(overlayIdStr){
	this.overlay = $('#'+overlayIdStr);
}

BulletOverlay.prototype.showBullet = function(sender, text){
	var $newdiv = $('<div class="bullet"><img src="'+sender.imageUrl+'" />'+text+'</div>');
	
	var posx = (Math.random() * (this.overlay.width())).toFixed();
    var posy = (Math.random() * (this.overlay.height())).toFixed();
    
    $newdiv.css({
    	 'position':'absolute',
         'left':posx+'px',
         'top':posy+'px',
    });
    
    this.overlay.append($newdiv);
    setTimeout(function(){
    	$newdiv.remove();
    }, 2000);
}

BulletOverlay.prototype.dimensions = function(){
	return {
		width: this.overlay.width(),
		height: this.overlay.height()
	}
}



function ParticipantList(participantIdStr){
	this.participantList = $('#'+participantIdStr);
	
}

ParticipantList.prototype.participantJoined = function(participant){
	var newEntry = $('<p class="participant" data-user-id="' +  participant.userId + '"><img src="'+participant.imageUrl+'" /> <span>'+participant.displayName+'</span> ')
	this.participantList.append(newEntry);

}

ParticipantList.prototype.participantLeft = function(participant){
	
}






function Room(elementIdStr, overlayIdStr, participantIdStr, isHost){
	
	this.overlay = new BulletOverlay(overlayIdStr);
	this.participants = new ParticipantList(participantIdStr);
	this.player = new YtPlayer(this.overlay.dimensions());
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
				/*ignored*/
			}
		})
	}
	
	this.socket.onMessage(function(msg){
		switch (msg.command){
		case 'BULLET':
			self.overlay.showBullet(msg.sender, msg.payload.text);
			break;
		}
	});
	this.socket.onParticipants(function(msg){
		switch (msg.state){
		case 'JOINED':
			self.participants.participantJoined(msg.sender);
			break;
		}
	})
}


Room.prototype.sendBullet = function(text){
	this.socket.send({command: 'BULLET', payload: {text: text}})
}

