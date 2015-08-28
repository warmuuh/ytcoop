
function YtPlayer(){
	this.player = null;
	this.listeners = {};
}

function addListener(evt, listeners, listener){
	if (!listeners[evt])
		listeners[evt] = [];
	listeners[evt].append(listener);
}

YtPlayer.prototype.onLoaded = function(listener){
	addListener('loaded', this.listeners, listener)
}

YtPlayer.prototype.onPlay = function(listener){
	addListener('play', this.listeners, listener)
}

YtPlayer.prototype.onPause = function(listener){
	addListener('pause', this.listeners, listener)
}

YtPlayer.prototype.notify = function(evt, data){
	if (this.listeners[evt]){
		for(var i = 0; i < this.listeners[evt].length; ++i)
			this.listeners[evt][i](data);
	}
}

YtPlayer.prototype.load = function(videoid, element){
	
	var self = this;
	window.onYouTubeIframeAPIReady = function(){
		self.player = new YT.Player(element, {
			height : '390',
			width : '640',
			videoId : videoid,
			events : {
				'onReady' : self.onPlayerReady.bind(self),
				'onStateChange' : self.onPlayerStateChange.bind(self)
			}
		});
	}
	
	// 2. This code loads the IFrame Player API code asynchronously.
	var tag = document.createElement('script');
	tag.src = "https://www.youtube.com/iframe_api";
	var firstScriptTag = document.getElementsByTagName('script')[0];
	firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
	
}



// 4. The API will call this function when the video player is ready.
YtPlayer.prototype.onPlayerReady = function(event) {
	this.notify('loaded')
}

YtPlayer.prototype.onPlayerStateChange = function(event) {
	if (event.data == YT.PlayerState.PLAYING) {
		this.notify('play')
	}
	if (event.data == YT.PlayerState.PAUSED) {
		this.notify('pause')
	}
}
