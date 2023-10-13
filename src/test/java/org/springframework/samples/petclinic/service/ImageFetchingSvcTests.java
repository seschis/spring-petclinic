package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.ImageFetchingSvc;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ImageFetchingSvcTests {
	@Test
	void tryit() throws IOException {
		var svc = new ImageFetchingSvc();
		var items = svc.findImageAndDownload("birds", "1234");
		assertThat(items).isNotNull();
	}
}
