package com.example.fchess.packethandler;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameData;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IPacketHandler;
import com.example.fchess.interfaces.PacketHandler;
import com.example.fchess.transmodel.DataPackage;
import com.example.fchess.transmodel.GameDataPackage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

@PacketHandler(packetName = eChessPackage.GAME_DATA, handler = GameDataHandler.class, dataType = DataPackage.class)
public class GameDataHandler implements IPacketHandler {
    private static final Logger log = LoggerFactory.getLogger(GameDataHandler.class);
    @Override
    public void handle(GameClient client, Object data) {
        ObjectMapper map = new ObjectMapper();
        if (client.gamePlayer.isViewer())
            return;
        if (client.currentBaseGameRoom == null)
            return;
        if (client.currentBaseGameRoom.isPlaying() == false)
            return;
        DataPackage dataPackage = (DataPackage)data;
        eGameData packageType = eGameData.fromId(dataPackage.getType());
        switch (packageType){
            case GAME_DATA:
                GameDataPackage gameData = new GameDataPackage();
                gameData.setSource((String) ((LinkedHashMap)dataPackage.getData()).get("source"));
                gameData.setTarget((String) ((LinkedHashMap)dataPackage.getData()).get("target"));
                client.currentBaseGameRoom.onGameData(client, gameData);
                break;
            case GAME_SURRENDER:
                if ( client.currentBaseGameRoom.isPlaying() == false){
                    return;
                }
                if (client.gamePlayer.isViewer()){
                    return;
                }
                        client.currentBaseGameRoom.endGame(1 - client.gamePlayer.getTeam());
                break;
            default:
                log.error("NOT FOUND eGameData:{}",packageType);
                break;
        }
    }
}
