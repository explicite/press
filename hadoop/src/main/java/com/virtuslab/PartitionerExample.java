package com.virtuslab;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

public class PartitionerExample {
    //Map class
    public static class MapClass extends MapReduceBase implements
            Mapper<LongWritable,
                    Text,
                    Text,
                    Text> {
        public void map(LongWritable key, Text value,
                        OutputCollector<Text, Text> output,
                        Reporter reporter) throws IOException {
            try {
                String[] str = value.toString().split(" ", -3);
                String gender = str[3];
                output.collect(new Text(gender), new Text(value));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Reducer class
    public static class ReduceClass extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> {
        public int max = -1;

        public void reduce(Text key, Iterator<Text> values,
                           OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            max = -1;

            while (values.hasNext()) {
                String[] str = values.next().toString().split(" ", -3);
                if (Integer.parseInt(str[4]) > max)
                    max = Integer.parseInt(str[4]);
            }

            output.collect(new Text(key), new IntWritable(max));
        }
    }

    //Partitioner class
    public static class CaderPartitioner extends MapReduceBase implements Partitioner<Text, Text> {
        public int getPartition(Text key, Text value, int numReduceTasks) {
            String[] str = value.toString().split(" ");
            int age = Integer.parseInt(str[2]);

            if (numReduceTasks == 0) {
                return 0;
            }

            if (age <= 20) {
                return 0;
            } else if (age > 20 && age <= 30) {
                return 1 % numReduceTasks;
            } else {
                return 2 % numReduceTasks;
            }
        }
    }

    public static JobConf getJob(String[] args) throws Exception {
        JobConf conf = new JobConf(PartitionerExample.class);

        conf.setJobName("topsal");
        conf.setMapperClass(MapClass.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(Text.class);

        conf.setPartitionerClass(CaderPartitioner.class);
        conf.setReducerClass(ReduceClass.class);
        conf.setNumReduceTasks(3);
        conf.setInputFormat(TextInputFormat.class);

        conf.setOutputFormat(TextOutputFormat.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        return conf;
    }

}