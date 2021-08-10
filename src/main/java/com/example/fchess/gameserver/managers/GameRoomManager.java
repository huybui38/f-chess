package com.example.fchess.gameserver.managers;

import com.example.fchess.gameserver.GameClient;
import com.example.fchess.gameserver.xiangqiroom.CreateRoomAction;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;
import com.example.fchess.gameserver.xiangqiroom.RemoveRoomAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;


public class GameRoomManager{
    private static final Logger log = LoggerFactory.getLogger(GameRoomManager.class);
    private static Set<Integer> freeRooms;
    public static ConcurrentHashMap<String, BaseGameRoom> rooms;
    private static BlockingQueue<Runnable> workQueue;
    private static RejectedExecutionHandler handler;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 120;

    private static Object lock = new Object();
    private static ThreadPoolExecutor threadPoolExecutor;
    private static ScheduledExecutorService scheduledExecutorService;

    public static void start(){
        log.info("GameRoomManager is starting up....");
        rooms = new ConcurrentHashMap<>();
        freeRooms = new HashSet<>();
        workQueue = new LinkedBlockingQueue<>();
        handler = new ThreadPoolExecutor.CallerRunsPolicy();
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, handler);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(clearRoom(), 0, 15, TimeUnit.SECONDS);
        for (int i = 0; i < 1000; i++) {
            freeRooms.add(i);
        }
    }
    public static int getFreeRoom(){
        int index = -1;
        synchronized(lock){
            index = freeRooms.iterator().next();
            freeRooms.remove(index);
        }
        return index;
    }
    public static void addFreeRoom(int index){
        synchronized (lock){
            freeRooms.add(index);
        }
    }
    public static void addAction(Runnable action){
        threadPoolExecutor.execute(action);
    }
    public static void addRoom(GameClient client){
        addAction(new CreateRoomAction(client));
    }
    public static void removeRoom(BaseGameRoom room){
        addAction(new RemoveRoomAction(room));
    }
    public static BaseGameRoom findRoomByID(String roomID) {
        if (rooms.containsKey(roomID))
            return rooms.get(roomID);
        return null;
    }
    public static Runnable clearRoom(){
        return new Runnable() {
            @Override
            public void run() {
                log.debug("Scanning unused room");
            }
        };
    }
}
