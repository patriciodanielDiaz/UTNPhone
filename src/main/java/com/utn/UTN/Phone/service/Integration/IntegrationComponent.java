package com.utn.UTN.Phone.service.Integration;

import com.utn.UTN.Phone.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

public class IntegrationComponent {
   /* private RestTemplate rt;
    private static String url = "http://localhost:8080/backoffice/";

    @PostConstruct
    private void init(){
        rt =new RestTemplateBuilder().build();
    }
    public User getUserFromApi(String dni){
        //corregir
        url=url+"users/"+dni;
        return rt.getForObject(url,User.class);
    }*/
}
