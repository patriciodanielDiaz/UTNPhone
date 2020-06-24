package com.utn.UTN.Phone.session;

import com.utn.UTN.Phone.model.City;
import org.springframework.stereotype.Component;
import com.utn.UTN.Phone.model.User;
import java.util.*;

import static com.utn.UTN.Phone.model.User.Type.empleado;

@Component
public class SessionManager {

    Map<String, Session> sessionMap = new Hashtable<>();

    int sesionExpiration = 6000000;

    public SessionManager() {
        sessionMap = new Hashtable<>();
        createSession(new User(1,"aaaa", "aaaa", "bbbb", "cccc","12345",empleado,null,null,null,null),"1");
    }

    public String createSession(User user) {
        String token = UUID.randomUUID().toString();
        sessionMap.put(token, new Session(token, user, new Date(System.currentTimeMillis())));
        return token;
    }
    public String createSession(User user, String token) {
        sessionMap.put(token, new Session(token, user, new Date(System.currentTimeMillis())));
        return token;
    }

    public Session getSession(String token) {
        Session session = sessionMap.get(token);
        if (session!=null) {
            session.setLastAction(new Date(System.currentTimeMillis()));
        }
        return session;
    }

    public void removeSession(String token) {
        sessionMap.remove(token);
    }

    public void expireSessions() {
        for (String k : sessionMap.keySet()) {
            Session v = sessionMap.get(k);
            if (v.getLastAction().getTime() < System.currentTimeMillis() + (sesionExpiration*1000)) {
                System.out.println("Expiring session " + k);
                sessionMap.remove(k);
            }
        }
    }

    public User getCurrentUser(String token) {
        return getSession(token).getLoggedUser();
    }
}
