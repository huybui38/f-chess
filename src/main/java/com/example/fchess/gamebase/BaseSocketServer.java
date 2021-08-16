package com.example.fchess.gamebase;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IPacketHandler;
import com.example.fchess.interfaces.PacketHandler;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class BaseSocketServer {
    private static final Logger log = LoggerFactory.getLogger(BaseSocketServer.class);
    protected final SocketIONamespace namespace;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    protected HashMap<String, IPacketHandler> handlers;
    protected ConcurrentHashMap<String, BaseClient> clients;
    public BaseSocketServer(SocketIOServer server) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.debug("Scan for clearing unused client (Total:{})...", clients.size());
                clearUnusedClient();
            }
        }, 0, 15, TimeUnit.SECONDS);
        clients = new ConcurrentHashMap<>();
        handlers = new HashMap<>();
        this.namespace = server.addNamespace("/chess");
        this.namespace.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        searchService();
    }
    private void searchService() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(PacketHandler.class));
        for (BeanDefinition bd : scanner.findCandidateComponents("com.example.fchess.packethandler")){
            IPacketHandler handler = (IPacketHandler) Class.forName(bd.getBeanClassName()).newInstance();
            PacketHandler ano = handler.getClass().getAnnotation(PacketHandler.class);
            this.namespace.addEventListener(ano.packetName().getValue(), ano.dataType(), packetDispatcher(ano.packetName().getValue()));
            registerHandler(ano.packetName().getValue(), handler);
        }

    }
    protected abstract DataListener packetDispatcher(String packet);


    private void clearUnusedClient(){
        for(Map.Entry<String, BaseClient> entry : clients.entrySet()) {
            try{
                String key = entry.getKey();
                BaseClient value = entry.getValue();
                if (value.canRemove()){
                    log.debug("Clear unused client: " + key);
                    value.onDisconnected();
                    clients.remove(key);
                }
            }catch (Exception ex){
                log.error(ex.toString());
            }
        }
    }
    private ConnectListener onConnected() {
        return client -> {
            String token  = client.getHandshakeData().getSingleUrlParam("token");
            HandshakeData handshakeData = client.getHandshakeData();
            BaseClient base = null;
            if (clients.containsKey(token)){
                base = clients.get(token);
                base.socket.sendEvent("onconflict");
                base.socket.disconnect();
                base.setSocket(client);
                base.onReconnect();
                log.debug("Same client");
            }else{
                base = getNewClient(client, token);
            }
            clients.put(token, base);
            base.onConnected();
            log.info("Client[{}] - Connected to chat module through '{}'", token, handshakeData.getUrl());
        };
    }
    protected BaseClient getNewClient(SocketIOClient client, String token){
        return new BaseClient(client, token);
    }
    private void registerHandler(String id, IPacketHandler handler){
        handlers.put(id, handler);
    }
    private DisconnectListener onDisconnected() {
        return client -> {
            String token  = client.getHandshakeData().getSingleUrlParam("token");
            BaseClient base = clients.get(token);
            if ( base != null){
                base.setDisconnectedAt(DateTime.now());
                base.onClosed();
            }
            log.debug("Client[{}] - Disconnected from chat module.", token);
        };
    }
}
