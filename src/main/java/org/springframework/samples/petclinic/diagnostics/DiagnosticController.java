package org.springframework.samples.petclinic.diagnostics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diagnostics")
public class DiagnosticController {

	@GetMapping("/debug")
	public DiagnosticData showdebug() throws IOException, InterruptedException {
		Map<String, String> data = new HashMap<>();
		data.put("foo", "bar");

		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

		String cmd = isWindows ? "cmd.exe /c dir" : "/bin/sh -c ls -1";
		System.out.printf("running cmd '%s'%n", cmd);
		Process process = Runtime.getRuntime().exec(cmd);

		boolean normalTermination = process.waitFor(10, TimeUnit.SECONDS);
		if (!normalTermination) {
			System.out.println("listing process terminated prematurely");
			return new DiagnosticData(data);
		}

		List<String> lines = new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
			.collect(Collectors.toList());
		data.put("listing", String.join(",", lines));

		return new DiagnosticData(data);
	}

}
