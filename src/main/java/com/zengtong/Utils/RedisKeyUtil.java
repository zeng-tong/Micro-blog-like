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

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String checkLike(int entityId, int entityType,int userId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(userId);
    }

    public static String getBizFollowlistKey(int myid){
        return "USER"+ String.valueOf(myid)+ SPLIT + BIZ_FOLLOWLIST;
    }

    public static String getBizFanslistKey(int myid){
        return "USER"+ String.valueOf(myid)+ SPLIT + BIZ_FANSLIST;
    }
}
