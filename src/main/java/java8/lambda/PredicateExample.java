package java8.lambda;

import lombok.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateExample {

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        apples.add(Apple.builder().color("red").weight(180).build());
        apples.add(Apple.builder().color("red").weight(120).build());
        apples.add(Apple.builder().color("green").weight(120).build());
        apples.add(Apple.builder().color("blue").weight(200).build());

        filterApples(apples, a -> "green".equals(a.getColor())).forEach(System.out::println);
        filterApples(apples, a -> a.getWeight() > 150).forEach(System.out::println);

        System.out.println(filterApples1(apples, Apple::toString));

        apples.sort((Comparator.comparingInt(Apple::getWeight)));

        Apple apple = Apple.builder().color("blue").weight(160).build();
        System.out.println(filterApples1(apples, a -> apple.toString()));
//        apple = Apple.builder().color("xx").build();
//        System.out.println(filterApples1(apples, a -> apple.toString()));

        // 比较器链，先按照重量递增排序，两个苹果一样重的时候，按照颜色排序
        apples.sort(Comparator.comparing(Apple::getWeight).thenComparing(Apple::getColor));

        System.out.println(apples);

        // 谓词复合，从左向右确定优先级，a.or(b).and(c)可以看作(a || b) && c
        Predicate<Apple> redApple = a -> "red".equals(a.getColor());
        Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
                .or(a -> "green".equals(a.getColor()));
        System.out.println(filterApples(apples, redAndHeavyAppleOrGreen));
    }

    private static String filterApples1(List<Apple> inventory, TestPredicate<Apple> p) {
        StringBuilder sb = new StringBuilder();
        inventory.forEach(s -> sb.append(p.test(s)));
        return sb.toString();
    }

    private static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
        return inventory.stream().filter(p)
                .collect(Collectors.toList());
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Apple {
        private String color;
        private int weight;
    }

}
