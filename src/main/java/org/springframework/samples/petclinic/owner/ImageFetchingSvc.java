package org.springframework.samples.petclinic.owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageFetchingSvc {
	private static final String baseUrl = "https://api.pexels.com/v1/search";
	private static final Path baseImageRepo = Path.of("/tmp","pet_images");
	private String authKey = "6fZchQe9zFAwSWiqK4IsOTadgsUX9cFhezKWXSmhE5U2Ljv9uqvJxYLn";
	private WebClient webClient = WebClient.builder()
		.baseUrl(baseUrl)
		.defaultHeader("Authorization", authKey)
		.build();

	public URL findImageAndDownload(String typeName, String petId) throws IOException {
		System.out.println("finding images! ...");

		var value = webClient.get().uri(uriBuilder -> uriBuilder
				.queryParam("query", typeName)
				.queryParam("per_page", 10)
				.build())
			.retrieve()
			.bodyToMono(PhotoResponse.class)
			.onErrorResume(e -> Mono.empty())
			.block();

		System.out.println("got a response!!!! " + value);


		var imageUrl = value.photos().stream().map(p -> {
			try {
				return new URL(p.src().small());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		})
			.filter(Objects::nonNull)
			.findFirst()
			.orElseThrow();
		downloadImageUrl(imageUrl, petId);
		return imageUrl;
	}

	private void downloadImageUrl(URL imageUrl, String petId) throws IOException {
		var filename = baseImageRepo.resolve(String.format("%s.jpeg", petId));
		System.out.println("downloading to " + filename.toAbsolutePath());
		var buffer = WebClient.builder()
			.defaultHeader("Authorization", authKey)
			.baseUrl("https://" + imageUrl.getHost())
			.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer ->
					configurer.defaultCodecs()
						.maxInMemorySize(20 * 1024 * 1024)).build())
			.build()
			.get().uri(uriBuilder -> uriBuilder.path(imageUrl.getPath()).build())
			.retrieve()
			.bodyToMono(DataBuffer.class);

		var m = DataBufferUtils.write(buffer,filename, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		m.block();
	}
}
