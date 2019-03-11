package transport_info;

import java.util.Collection;

public class Material {

    private final String id;
    private final double demand;
    private Collection<Route> routes;

    Material(String id, double demand, Collection<Route> routes) {
        this.id = id;
        this.demand = demand;
        this.routes = routes;
    }

    public String getId(){
        return id;
    }

    public double getDemand() {
        return demand;
    }

    boolean contains(Route r){
        return routes.contains(r);
    }
}
