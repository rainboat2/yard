package plan;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TransportPlanGenerator {

    public static void main(String[] args) {
        double[] mixturePlan = {1067, 1067, 1067, 1067, 1067, 1067, 1067, 1067, 1067, 1067, 1067, 963.8, 0, 0, 0, 0};
        double sum = sum(mixturePlan);

        System.out.println();

        TransportPlanGenerator t = new TransportPlanGenerator(mixturePlan);

        Integer[] plan = t.rs.get(0).toArray(new Integer[0]);
        for (int i = plan.length - 1; i > 0; i--)
            plan[i] -= plan[i-1];

        System.out.println("排班计划：" + Arrays.toString(mixturePlan));
        System.out.println("运输计划总数: " + t.rs.size());
        System.out.println("运输计划举例：");
        for (int nums : plan)
            System.out.print(((double)nums/100) * sum + ", ");

        System.out.println();
        System.out.println();

        System.out.println("运行时的部分数据（运输完成百分比，每行表示一个计划）：");
        for (int i = 0; i < 100; i++)
            System.out.println(t.rs.get(i));

        System.out.println();
    }

    private Integer[] process;       // 进度条，每运送一个班次，进度条前进一次
    private int[] marker;            // 标记每个班次进度条最低限度
    private List<List<Integer>> rs;
    private int count;

    public TransportPlanGenerator(double[] mixturePlan){
        process = new Integer[mixturePlan.length];
        marker = new int[mixturePlan.length];
        rs = new LinkedList<>();

        double sum = sum(mixturePlan);

        // 将任务分为100份
        double total = 0.0;
        for (int i = 0; i < marker.length; i++){
            total += mixturePlan[i];
            marker[i] = (int) ((total / sum) * 100);
        }

        // 防止因为精度损失而导致最后一项不为100
        marker[marker.length-1] = 100;
        arrange(0);
    }

    // 安排第i个班次的运输量
    public void arrange(int i){
        if (i == process.length)
            rs.add(List.of(process));
        else{
            process[i] = (i == 0)? 0 : process[i-1];
            for (int j = 0; process[i] + j <= 100; j += 10){
                if (process[i] + j < marker[i]) continue;

                process[i] += j;
                arrange(i + 1);
                process[i] -= j;
            }
        }
    }

    public static double sum(double[] a){
        double sum = 0.0;
        for (double d : a)
            sum += d;
        return sum;
    }
}
