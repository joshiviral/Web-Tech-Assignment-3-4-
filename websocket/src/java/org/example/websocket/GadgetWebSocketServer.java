/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.websocket;
import java.io.StringReader;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import org.example.model.Device;

/**
 *
 * @author Vjoshi0919
 */
@ApplicationScoped
@ServerEndpoint("/actions")
public class GadgetWebSocketServer {
    
     
        @Inject
    private GadgetSessionhandler sessionHandler;
    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }

    @OnClose
        public void close(Session session) {
            sessionHandler.removeSession(session);
    }

    @OnError
        public void onError(Throwable error) {
             Logger.getLogger(GadgetWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
 
    }

    @OnMessage
        public void handleMessage(String message, Session session) {

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = (JsonObject) reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Device gadget = new Device();
                gadget.setName((String) jsonMessage.getString("name"));
                gadget.setDescription((String) jsonMessage.getString("description"));
                gadget.setType((String) jsonMessage.getString("type"));
                gadget.setStatus("Off");
                sessionHandler.addGadget(gadget);
            }

           if ("remove".equals(jsonMessage.getString("action"))) {
                int id = jsonMessage.getInt("id");
                sessionHandler.removeGadget(id);
            }

            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.toggleDevice(id);
            }
        }
    }

       
    
   
    
}
