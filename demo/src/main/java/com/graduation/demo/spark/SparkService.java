package com.graduation.demo.spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class SparkService {
    private static final Pattern SPACE = Pattern.compile(" ");

    @Autowired
    private JavaSparkContext javaSparkContext;

    public Map<String, Object> sparkDemo() {
        Map<String, Object> res = new HashMap<String, Object>();
        JavaRDD<String> lines = javaSparkContext.textFile("src/main/resources/sdeegsa.txt").cache();
        lines.map(
                str -> {
                    System.out.println(str);
                    return str;
                }
        );
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(lines.count());

        JavaRDD<String> words = lines
                .flatMap(str -> Arrays.asList(SPACE.split(str)).iterator());
        JavaPairRDD<String, Integer> ones = words
                .mapToPair(str -> new Tuple2<String, Integer>(str, 1));
        JavaPairRDD<String, Integer> counts = ones
                .reduceByKey((Integer i1, Integer i2) -> (i1 + i2));

        JavaPairRDD<Integer, String> temp = counts
                .mapToPair(tuple -> new Tuple2<Integer, String>(tuple._2, tuple._1));

        JavaPairRDD<String, Integer> sorted = temp
                .sortByKey(false)
                .mapToPair(tuple -> new Tuple2<String, Integer>(tuple._2, tuple._1));

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(sorted.count());

        List<Tuple2<String, Integer>> output = sorted.collect();
        for (Tuple2<String, Integer> tuple : output) {
            res.put(tuple._1(), tuple._2());
        }

        return res;
    }

}
