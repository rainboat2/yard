package transport_info;

public class Route {

    private final double speed;
    private final String id;
    private final double health;

    Route(String id, double speed, double health) {
        this.speed = speed;
        this.id = id;
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public String getId() {
        return id;
    }

    // 0-1之间的值，值越大，表示该传送带越健康
    public double getHealth(){
        return health;
    }
}
