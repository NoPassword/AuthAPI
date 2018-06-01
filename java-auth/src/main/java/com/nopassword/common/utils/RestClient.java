package com.nopassword.common.utils;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author NoPassword
 */
public class RestClient {

    ClientConfig clientConfig;

    public RestClient() {
        clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJsonProvider.class);
    }

    public <T> T post(String url, Object payload, Class<T> clazz) {
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        return webResource
                .type(MediaType.APPLICATION_JSON)
                .post(clazz, payload);
    }

}
