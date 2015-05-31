package utils;

/**
 * Created by hyx on 2015/5/26.
 */
public class Snapshot {
    String individualStrategyPicture;
    String individualPayoffPicture;

    String allPicture;

    public Snapshot(String individualStrategyPicture, String individualPayoffPicture) {
        this.individualStrategyPicture = individualStrategyPicture;
        this.individualPayoffPicture = individualPayoffPicture;
    }

    public Snapshot(String allPicture){
        this.allPicture  = allPicture;
    }
}
