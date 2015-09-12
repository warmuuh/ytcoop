package com.github.warmuuh.ytcoop.provider.support;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.provider.ProviderId;
import com.github.warmuuh.ytcoop.provider.VideoProvider;
import com.github.warmuuh.ytcoop.video.VideoDetails;

@Service
public class ProviderService {

	@Autowired
	List<VideoProvider> videoProviders;
	
	public Optional<VideoDetails> loadDetails(String id, ProviderId pid){
		return getProvider(pid).getDetails(id);
	}

	private VideoProvider getProvider(ProviderId id) {
		for (VideoProvider provider : videoProviders) {
			if (provider.getProviderId() == id)
				return provider;
		}
		throw new RuntimeException("cannot find video provider for " + id);
	}
	
}
