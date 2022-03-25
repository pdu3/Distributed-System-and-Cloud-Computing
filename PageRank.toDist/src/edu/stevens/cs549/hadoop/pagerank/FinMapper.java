package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class FinMapper extends Mapper<LongWritable, Text, DoubleWritable, Text> {

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException, IllegalArgumentException {
		String line = value.toString(); // Converts Line to a String
		/*
		 * TODO output key:-rank, value: node
		 * See IterMapper for hints on parsing the output of IterReducer.
		 */
        String [] sections = line.split("\t");
        String [] pair = sections[0].split("\\" + "+");
        if(pair.length == 2 && pair[1]!=null && !pair[1].equals("null"))
        	context.write(new DoubleWritable(-Double.valueOf(pair[1])), new Text(pair[0]));
	}

}
