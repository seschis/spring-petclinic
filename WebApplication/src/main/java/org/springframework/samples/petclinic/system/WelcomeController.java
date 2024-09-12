/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
class WelcomeController {

	private final GeolocationIoClient client = new GeolocationIoClient("https://api.ipgeolocation.io");

	@GetMapping("/")
	public String welcome(Model model) {
		model.addAttribute("welcomeMessage", readWelcomeMessage());
		model.addAttribute("astronomyData", getAstronomicalData());
		return "welcome";
	}

	private String readWelcomeMessage() {
		String filename = "/welcome_message.txt";

		URL loc = getClass().getResource(filename);
		if (Objects.nonNull(loc)) {
			try {
				System.out.printf("reading file %s %n", filename);
				var welcome = new String(loc.openStream().readAllBytes(), StandardCharsets.UTF_8);
				System.out.printf("welcome string is %s %n", welcome);
				return welcome;
			}
			catch (IOException e) {
				System.out.printf("could not open '%s' data%n", filename);
			}
		}
		else {
			System.out.printf("resource %s not available%n", filename);
		}

		return "Welcome 3";
	}

	private String getAstronomicalData() {
		GeolocationIoClient.ResponseDto responseDto = client.fetchAstronomyData("New York, US");
		var data = String.format("%s", responseDto);
		System.out.printf("read astronomy data: %s %n", data);
		return data;
	}

}
