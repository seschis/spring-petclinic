package org.springframework.samples.petclinic.owner;

public record PhotoSource(
	String original,
	String large2x,
	String large,
	String medium,
	String small,
	String portrait,
	String landscape,
	String tiny
) {
}
