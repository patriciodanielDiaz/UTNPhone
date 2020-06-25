package com.utn.UTN.Phone.config;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class RestUtil {

        public static URI getLocation(Integer id) {
            return ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();
        }
}
