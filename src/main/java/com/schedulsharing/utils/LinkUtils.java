package com.schedulsharing.utils;

import org.springframework.hateoas.Link;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkUtils {

    public static List<Link> createSelfProfileLink(Class<?> tClass, String slashNext, String profile) {
        return Arrays.asList(
                linkTo(tClass).slash(slashNext).withSelfRel(),
                Link.of(profile, "profile")
        );
    }

    public static URI createURI(Class<?> tClass, Long slashNext) {
        return linkTo(tClass).slash(slashNext).toUri();
    }

    public static List<Link> createSelfProfileLink(Class<?> tClass, Long slashNext, String profile) {
        return Arrays.asList(
                linkTo(tClass).slash(slashNext).withSelfRel(),
                Link.of(profile, "profile")
        );
    }

}
