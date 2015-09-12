package com.github.warmuuh.ytcoop.provider.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.warmuuh.ytcoop.provider.ProviderId;
import com.github.warmuuh.ytcoop.provider.VideoProvider;
import com.github.warmuuh.ytcoop.video.VideoDetails;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import lombok.SneakyThrows;

@Component
public class YoutubeProvider implements VideoProvider {
	
	@Autowired
	YouTube yt;

	@Value("${spring.social.youtube.api-key}")
	String apiKey;
	
	
	
	@Override
	@SneakyThrows
	public Optional<VideoDetails> getDetails(String id) {
		
		
		VideoListResponse videos = yt.videos()
							.list("snippet")
							.setKey(apiKey)
							.setId(id).execute();
		
		if (videos.getItems().size() != 1){
			return Optional.empty();
		}
		Video video = videos.getItems().get(0);
		VideoDetails details = new VideoDetails(video.getId(), 
									video.getSnippet().getTitle(),
									video.getSnippet().getThumbnails().getDefault().getUrl(),
									ProviderId.YOUTUBE);
		
		return Optional.of(details);
	}
	
	
	@Override
	public ProviderId getProviderId() {
		return ProviderId.YOUTUBE;
	}
	
}
