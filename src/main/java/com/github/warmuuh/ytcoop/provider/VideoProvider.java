package com.github.warmuuh.ytcoop.provider;

import java.util.Optional;

import com.github.warmuuh.ytcoop.video.VideoDetails;

public interface VideoProvider {

	public abstract ProviderId getProviderId();
	public abstract Optional<VideoDetails> getDetails(String id);

}
