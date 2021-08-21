package java8.stream;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("season fruit", true, 120, Type.OTHER),
                new Dish("pizza", true, 550, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH));

        // map方法就是将流中每一个元素映射成一个新的元素，T -> R
        // 还有reduce方法，归约操作，例如对流求和，流中的最大值和最小值
        List<String> collect = menu.stream().filter(d -> d.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(collect);

        List<Integer> dishNameLengths = menu.stream().map(Dish::getName)
                .map(String::length)
                .sorted(Integer::compareTo)
                //.max(Integer::compareTo).ifPresent(System.out::println)
                .collect(Collectors.toList());
        System.out.println(dishNameLengths);

        //  给定两个数字列表，如何返回所有的数对呢？例如，给定列表[1, 2, 3]和列表[3, 4]，应该返回[(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        // flatmap方法就是将流扁平化，相当于将Stream<Stream<int[]>> -> Stream<int[]>
        numbers1.stream()
                .flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .forEach(nums -> System.out.println("[" + nums[0] + ", " + nums[1] + "]"));

        // 利用reduce方法得到菜单的卡路里总和
        int calories = menu.stream().map(Dish::getCalories).reduce(0, Integer::sum);
        System.out.println(calories);
        // intStream、 DoubleStream和LongStream，分别将流中的元素特化为int、 long和double，从而避免了暗含的装箱成本
        System.out.println(menu.stream().mapToInt(Dish::getCalories).sum());
        // 利用boxed方法将数值流转化为对象流
        Stream<Integer> stream = menu.stream().mapToInt(Dish::getCalories).boxed();
        System.out.println(stream);

        // 找出0-100中满足勾股定理的三元数组
        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));
        pythagoreanTriples.forEach(a -> System.out.println("[" + a[0] + ", " + a[1] + ", " + a[2] + "]"));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Dish {
        private String name;
        private boolean vegetarian;
        private int calories;
        private Type type;
    }

    public enum Type { MEAT, FISH, OTHER }

}
