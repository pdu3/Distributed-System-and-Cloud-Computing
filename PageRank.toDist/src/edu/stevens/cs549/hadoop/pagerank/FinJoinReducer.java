package edu.stevens.cs549.hadoop.pagerank;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import java.io.IOException;
public class FinJoinReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String node = key.toString();
		String name = null;
		String rank = null;
		String list = null;
		for(Text val : values) {
			String value = val.toString();
			String[] pair = value.split("\\" + "+");
			if (value.startsWith("NAME")) {
				name = (pair.length > 1) ? pair[1]:null;
			} else if (value.startsWith("RANK")) {
				rank = (pair.length > 1) ? pair[1]:null;
			} else if (value.startsWith(PageRankDriver.MARKER_ADJ)) {
				list = (pair.length > 1) ? pair[1]:null;
			}
		}
		String outKey = ((name == null)? node : name) + "+" + rank;
		String outValue = (list == null)? "" : list;
		context.write(new Text(outKey), new Text(outValue));
	}
	
}
