package com.virtuslab;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MapReduceExample {
    //Mapper class
    public static class ExampleMapper extends MapReduceBase implements
            Mapper<LongWritable,    /*Input key Type */
                    Text,           /*Input value Type*/
                    Text,           /*Output key Type*/
                    IntWritable>    /*Output value Type*/ {

        //Map function
        public void map(LongWritable key, Text value,
                        OutputCollector<Text, IntWritable> output,
                        Reporter reporter) throws IOException {
            String line = value.toString();
            String lastToken = null;
            StringTokenizer s = new StringTokenizer(line, " ");
            String year = s.nextToken();

            while (s.hasMoreTokens()) {
                lastToken = s.nextToken();
            }

            int avgprice = Integer.parseInt(lastToken);
            output.collect(new Text(year), new IntWritable(avgprice));
        }
    }


    //Reducer class
    public static class ExampleReduce extends MapReduceBase implements
            Reducer<Text, IntWritable, Text, IntWritable> {

        //Reduce function
        public void reduce(Text key, Iterator<IntWritable> values,
                           OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int maxavg = 30;
            int val;

            while (values.hasNext()) {
                if ((val = values.next().get()) > maxavg) {
                    output.collect(key, new IntWritable(val));
                }
            }

        }
    }

    public static JobConf getJob(String[] args) throws Exception {
        JobConf conf = new JobConf(MapReduceExample.class);

        conf.setJobName("mapreduce_example");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setMapperClass(ExampleMapper.class);
        conf.setCombinerClass(ExampleReduce.class);
        conf.setReducerClass(ExampleReduce.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        return conf;
    }
}