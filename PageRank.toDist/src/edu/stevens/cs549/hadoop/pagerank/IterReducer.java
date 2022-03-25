package edu.stevens.cs549.hadoop.pagerank;

import java.io.*;
import java.util.*;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class IterReducer extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double d = PageRankDriver.DECAY; // Decay factor
		/* 
		 * TODO: emit key:node+rank, value: adjacency list
		 * Use PageRank algorithm to compute rank from weights contributed by incoming edges.
		 * Remember that one of the values will be marked as the adjacency list for the node.
		 */
		double currentRank = 0;
		String adjList = "";
		for (Text value:values){
			String sval = value.toString();
			if (sval.startsWith(PageRankDriver.MARKER_ADJ))
				adjList = sval.split(":")[1];
			else currentRank += Double.valueOf(value.toString());
		}
		currentRank = 1 - d + currentRank * d;
		context.write(new Text(key.toString() + "+" + String.valueOf(currentRank)), new Text(adjList));
	}
}
