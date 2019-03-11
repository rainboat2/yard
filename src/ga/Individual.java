package ga;

import transport_info.Material;
import transport_info.Route;
import transport_info.Transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Individual {

    private static final double PC = 0.7;   // 交叉的概率
    private static final double PM = 0.3;   // 变异的概率

    private int[] chromosome;
    private double fitness;
    private Transport t = Transport.getInstance();


    // 随机生成一个个体（一种方案）
    Individual(){
        int mn = t.materialNum(), rn = t.routeNum();

        chromosome = new int[mn];
        // 为每一种原料安排一条传送带
        for (int i = 0; i < mn; i++)
            chromosome[i] = (int)(Math.random() * rn);
        fitness = countFitness();
    }

    // 使用制定的编码生成个体
    private Individual(int[] chromosome){
        this.chromosome = chromosome;
        fitness = countFitness();
    }

    // 交叉操作
    Individual[] crossover(Individual id){
        int[] ch1 = this.chromosome.clone();
        int[] ch2 = id.chromosome.clone();

        // 交换两条染色体的部分片段
        for (int i = 0; i < ch1.length; i++) {
            if (Math.random() > PC) continue;
            int temp = ch1[i];
            ch1[i] = ch2[i];
            ch2[i] = temp;
        }
        return new Individual[]{new Individual(ch1), new Individual(ch2)};
    }

    // 变异操作
    Individual mutation(){
        int[] ch = this.chromosome.clone();
        for (int i = 0; i < ch.length; i++)
            if (Math.random() < PM)
                ch[i] = (int)(Math.random() * t.routeNum());
        return new Individual(ch);
    }


    double fitness(){
        return fitness;
    }

    // 计算每个个体的适应值
    private double countFitness(){
        double ft = finishTime();
        return 24 - ft;
    }

    // 计算任务完成的时间
    private double finishTime(){
        // 统计每条传送带完成任务需要的时间
        double[] routeTime = new double[t.routeNum()];
        for (int i = 0; i < chromosome.length; i++) {
            int route = chromosome[i];
            routeTime[route] += t.countTransferTime(route, i);
        }

        // 找到最晚完成任务的一条传送带
        double finish = routeTime[0];
        for (double d : routeTime)
            finish = Math.max(finish, d);

        return finish;
    }

    // 统计所有传送带的工作量，计算对应的方差
    private double loadVariance(){
        double[] routeLoad = new double[t.routeNum()];
        double sum = 0.0;
        for (int i = 0; i < chromosome.length; i++){
            int route = chromosome[i];
            double demand = t.getMaterial(i).getDemand();
            sum += demand;
            routeLoad[route] += demand;
        }

        // 开始计算方差
        double xa = sum / t.routeNum();
        double temp = 0.0;
        for (double x : routeLoad)
            temp += Math.pow(x - xa, 2);

        return temp / t.routeNum();
    }

    /**
     * Individual类中chromosome数组为每个方案的对应的编码
     * 本类用于将编码翻译为便于人阅读的方案
     */
    public class Result{

        // 每个索引代表一条传送带，List中放的为分配到该传送带上的原料编号
        @SuppressWarnings("unchecked")
        List<Integer>[] routes = (List<Integer>[]) new ArrayList[t.routeNum()];

        private Result(){
            for (int i = 0; i < routes.length; i++)
                routes[i] = new ArrayList<>();

            for (int i = 0; i < chromosome.length; i++) {
                int route = chromosome[i];
                routes[route].add(i);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            double finishTime = Double.MIN_VALUE;

            for (int i = 0; i < routes.length; i++) {
                Route r = t.getRoute(i);
                sb.append(String.format("传输带%s的任务为：\n", r.getId()));

                double begin = 0;    // 运输开始时间

                for (int j : routes[i]){
                    Material m = t.getMaterial(j);
                    sb.append(String.format("\t原料%s, 开始运输时间：%.2f，运输总量：%.2f\n", m.getId(), begin, m.getDemand()));
                    begin += t.countTransferTime(i, j);
                }

                finishTime = Math.max(finishTime, begin);
            }

            sb.append(String.format("本班次运输 开始时间：0, 结束时间：%.2f\n", finishTime));
            return sb.toString();
        }
    }

    public Result getResult(){
        return new Result();
    }

    public String toString(){
        return Arrays.toString(chromosome);
    }
}
