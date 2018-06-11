package com.zengtong.Utils;

/**
 * Created by nowcoder on 2016/7/13.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_FOLLOWLIST = "FOLLOWLIST";
    private static String BIZ_FANSLIST = "FANSLIST";
    private static String BIZ_UPSTREAM = "FEEDCENTER"; //Feed 流

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityType,int entityID) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String .valueOf(entityID);
    }

    public static String checkLike( int entityType,int entityId) {
        return "CHECK"+BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getBizFollowlistKey(int myid){
        return "USER"+ String.valueOf(myid)+ SPLIT + BIZ_FOLLOWLIST;
    }

    public static String getBizFanslistKey(int myid){
        return "USER"+ String.valueOf(myid)+ SPLIT + BIZ_FANSLIST;
    }

    public static String getFeedKey(int userId,int entityType){
        return BIZ_UPSTREAM + SPLIT + String .valueOf(userId) + SPLIT + String .valueOf(entityType) ;
    }

    public static String getFeedValue(int entityType,int entityId){
        return  String .valueOf(entityType) + SPLIT + String .valueOf(entityId);
    }
}
