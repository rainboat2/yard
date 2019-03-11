package ga;

import java.util.*;

public class GA {

    public static void main(String[] args) {
        GA ga = new GA();
        ga.evolution();
    }

    private static final int POP = 50;        // 种群数量
    private static final int RUN_TIMES = 20;  // 进化次数

    private List<Individual> pop;

    public void evolution(){
        init();
        for (int i = 0; i < RUN_TIMES; i++) {
            System.out.println("---第" + (i + 1) + "代进化-----");

            List<Individual> matingPool = selection();
            pop.clear();

            showResult(matingPool);

            Collections.shuffle(matingPool);  // 打乱顺序，增加随机性

            for (int j = 0; j < matingPool.size() - 1; j += 2) {
                Individual a = matingPool.get(j);
                Individual b = matingPool.get(j + 1);
                // 执行交叉，变异操作
                Individual[] id = a.crossover(b);
                Collections.addAll(pop, a.mutation(), b.mutation(), id[0], id[1]);
            }
            ensureSize();
        }

        // 结束整个算法后，打印排名第一的方案
        List<Individual> matingPool = selection();
        System.out.println(matingPool.get(0).getResult());
    }

    private void showResult(List<Individual> rank){
        for (int i = 0; i < 5; i++)
            System.out.printf("排名第%d的个体，序列为%s, 适应度为%f，完成运输所需时间为%f\n",
                    i, rank.get(i), rank.get(i).fitness(), 24 - rank.get(i).fitness());
    }

    private void init(){
        pop = new ArrayList<>(POP);
        ensureSize();
    }

    private void ensureSize(){
        while (pop.size() < POP)
            pop.add(new Individual());
    }

    // 选出适应度高的个体
    private List<Individual> selection(){
        PriorityQueue<Individual> pq = new PriorityQueue<>(((o1, o2) -> Double.compare(o2.fitness(), o1.fitness())));
        pq.addAll(pop);
        List<Individual> matingPool = new ArrayList<>(POP/2);
        for (int i = 0; i < POP/2; i++)
            matingPool.add(pq.poll());
        return matingPool;
    }
}

/**
 * 快速排序算法
 * 当一个个体有多个评价条件时，定义支配关系，使用快排来对个体进行排序
 */
class Quick{

    public static <T> void sort(List<T> l, Comparator<T> c){
        sort(l, 0, l.size()-1, c);
    }

    private static <T> void sort(List<T> l, int lo, int hi, Comparator<T> c){
        if (lo >= hi) return;

        int mid = partition(l, lo, hi, c);
        sort(l, lo, mid - 1, c);
        sort(l, mid + 1, hi, c);
    }

    private static <T> int partition(List<T> l, int lo, int hi, Comparator<T> c){
        T t = l.get(lo);
        int i = lo, j = hi + 1;
        while (i < j){
            while (c.compare(t, l.get(++i)) > 0)  if (i >= hi) break;  // 找到比 l[lo] 大的元素
            while (c.compare(t, l.get(--j)) < 0)  if (j <= lo) break;  // 找到比 l[lo] 小的元素

            if (i >= j)  break;
            swap(l, i, j);
        }
        swap(l, lo, j);
        return j;
    }

    private static <T> void swap(List<T> l, int a, int b){
        T t = l.get(a);
        l.set(a, l.get(b));
        l.set(b, t);
    }
}