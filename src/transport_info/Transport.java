package transport_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 本类用于封装需要传输的原料以及对应可用传送带的相关信息
 */
public class Transport {

    private List<Route> routes = new ArrayList<>();
    private List<Material> materials = new ArrayList<>();


    private static Transport transport;

    public static Transport getInstance(){
        if (transport == null)
            transport = new Transport();
        return transport;
    }

    private Transport(){
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private void load() throws IOException {
        File f = new File("info.txt");
        FileReader in = new FileReader(f);
        BufferedReader r = new BufferedReader(in);

        Map<String, Route> map = new HashMap<>();

        // 读取路线信息
        String line;
        while (!(line = r.readLine()).equals(">>")){
            if (line.startsWith("#")) continue;

            String[] info = line.split(",");
            Route route = new Route(info[0], Double.parseDouble(info[1]), Double.parseDouble(info[2]));
            routes.add(route);
            map.put(info[0], route);
        }

        // 读取原料信息
        while ((line = r.readLine()) != null){
            if (line.startsWith("#")) continue;

            String[] info = line.split(",");
            Collection<Route> c = new ArrayList<>(3);
            for (String id : info[2].split("-"))
                c.add(map.get(id));
            Material m = new Material(info[0], Double.parseDouble(info[1]), c);
            materials.add(m);
        }
    }

    public int routeNum(){
        return routes.size();
    }

    public int materialNum(){
        return materials.size();
    }

    public Material getMaterial(int i){
        return materials.get(i);
    }

    public Route getRoute(int i){
        return routes.get(i);
    }

    public double countTransferTime(int route, int material){
        Material m = materials.get(material);
        Route r = routes.get(route);

        if (!m.contains(r)) return 24;                   // 返回一个较大的值表示这个解不可用
        else                return m.getDemand() / r.getSpeed();   // 计算需求时间
    }
}
