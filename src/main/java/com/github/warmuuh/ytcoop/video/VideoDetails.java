package com.github.warmuuh.ytcoop.video;

import javax.persistence.Embeddable;

import com.github.warmuuh.ytcoop.provider.ProviderId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetails {

	private String videoId;
	private String title;
	private String thumbnailUrl;
	private ProviderId provider;
}
