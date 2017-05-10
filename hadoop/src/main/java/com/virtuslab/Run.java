package com.virtuslab;

import org.apache.hadoop.mapred.*;

public class Run {

    public static void partitionerExample(String[] args) throws Exception {
        JobClient.runJob(PartitionerExample.getJob(args));
    }

    public static void runMapReduceExample(String[] args) throws Exception {
        JobClient.runJob(MapReduceExample.getJob(args));
    }

    public static void main(String[] args) throws Exception {
        runMapReduceExample(args);
        //partitionerExample(args);
    }
}
