import java.io.*;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class Summarizer {
	
	public static void main(String[] args){
		try{
			
			int threshold;// Threshold of significance of itemsets in percent
			int cluster_number;
			int compresion_rate = 30;
			
			for(cluster_number=6; cluster_number<=6; cluster_number++)
			{
				for(threshold=4; threshold<=15; threshold++)
				{
					int doc_num;
					
					for(doc_num=1; doc_num<=24; doc_num++)
					{
						System.out.println("\n************************* Document: " + doc_num + " *************************");
						
						SAXBuilder file_builder = new SAXBuilder();
						File input_file = new File("G:\\eclipse\\Dataset\\Converted\\" + doc_num + ".xml");
						
						org.jdom2.Document input_document = (org.jdom2.Document) file_builder.build(input_file);
						
						org.jdom2.Element Document = input_document.getRootElement();
						
						List Sentences = Document.getChildren("Sentence");
						
						List<Sentence> sentence_list = new ArrayList<Sentence>();
						
						int sentence_num = 0;
						for(int i = 0; i < Sentences.size(); i++)
						{
							sentence_num++;
							org.jdom2.Element Sentence = (org.jdom2.Element) Sentences.get(i);
			        		
			        		Sentence temp_sentence = new Sentence();
			        		temp_sentence.sentence_number = sentence_num;
			        		temp_sentence.sentence_score = 0;
			        		temp_sentence.sentence_text = Sentence.getChildText("SentenceText");
			        		sentence_list.add(temp_sentence);
			        		
			        		System.out.println("Sentence Number : " + temp_sentence.sentence_number);
			        		
			        		List ConceptList = Sentence.getChildren("ConceptList");
			        		for(int i2 = 0; i2 < ConceptList.size(); i2++)
			        		{
			        			org.jdom2.Element concept_list = (org.jdom2.Element)ConceptList.get(i2);
			        			List Concepts = concept_list.getChildren("Concept");
				        		
				        		for(int i3 = 0; i3 < Concepts.size(); i3++)
				        		{
				        			org.jdom2.Element Concept = (org.jdom2.Element) Concepts.get(i3);
				        			String ConceptName = Concept.getChildText("ConceptName");
				        			String SemanticType = Concept.getChildText("SemanticType");
				        			
				        			if(!(SemanticType.equals("fndg") && ConceptName.endsWith("%")))
				        			{
				        				Concept temp_concept = new Concept(ConceptName, SemanticType);
						  	            sentence_list.get(sentence_num-1).AddNewConcept(temp_concept);
				        			}
				        		}
			        		}
						}
			        		
						
						//-----------------------------------------------------------------
						//-----------------------------------------------------------------
						
				        ItemList all_items = new ItemList();
				        for(int i = 0; i < sentence_list.size(); i++)
				        {
				        	System.out.println("*****************************************************************");
				        	System.out.println("Sentence No: " + sentence_list.get(i).sentence_number + ", Text: " + sentence_list.get(i).sentence_text);
				        	System.out.println("\nConcepts:");
				        	for (int j = 0; j < sentence_list.get(i).concept_list.size(); j++)
				        	{
				        		// Extract semantic types from sentences, each one form an item, insert all of them in all_items list
				        		Item temp_item = new Item(sentence_list.get(i).concept_list.get(j).concept_name, sentence_list.get(i).concept_list.get(j).semantic_type);
				        		all_items.AddNewItem(temp_item);
				        		System.out.println(sentence_list.get(i).concept_list.get(j).concept_name + ", sem type: " + sentence_list.get(i).concept_list.get(j).semantic_type);
				        	}
				        }
				        
				        System.out.println("---------------------------------------------------------------------------");
				        
				        // Put items frequency to zero, and calculate frequency of items again (only one occurrence of an item in each sentence is considered)
				        for(int i=0; i<all_items.list.size(); i++)
				        {
				        	all_items.list.get(i).item_frequency = 0;
				        	for(int j = 0; j < sentence_list.size(); j++)
				        	{
				        		if(sentence_list.get(j).IsContainItem(all_items.list.get(i).item_concept, all_items.list.get(i).item_semtype))
				        			all_items.list.get(i).item_frequency ++;
				        	}
				        }
				        
				        // Print all items
				        System.out.println("\n*************************** All items ***************************");
				        for(int i=0; i<all_items.list.size(); i++)
				        {
				        	System.out.println("Item: " + all_items.list.get(i).item_concept + " (" + all_items.list.get(i).item_semtype + ")" + ", Support: " + all_items.list.get(i).item_support);
				        }
				        
				        //---------------------------------------- Calculating frequent one-itemsets
				        
				        
				        List<Itemset> frequent_itemsets = new ArrayList<Itemset>();
				        
				        for(int i = 0; i < all_items.list.size(); i++)
				        {
				        	// If frequency of one item be equal or greater than the threshold it is added to frequent_itemsets list
				        	all_items.list.get(i).item_support = (double)all_items.list.get(i).item_frequency / (double)sentence_num;
				        	if(all_items.list.get(i).item_support >= (double)threshold/100)
				        	{
				        		Itemset temp_itemset = new Itemset(1);
				        		temp_itemset.frequency = all_items.list.get(i).item_frequency;
				        		temp_itemset.support = all_items.list.get(i).item_support;
				        		temp_itemset.items.add(all_items.list.get(i));
				        		frequent_itemsets.add(temp_itemset);
				        	}
				        }
				        
				        // Printing frequent one-itemsets
				        System.out.println("\n*************************** Frequent 1-itemsets ***************************");
				        for(int i = 0; i < frequent_itemsets.size(); i++)
				        {
				        	System.out.println("Items: " + frequent_itemsets.get(i).items_number + ", Item: " + frequent_itemsets.get(i).items.get(0).item_concept + " (" + frequent_itemsets.get(i).items.get(0).item_semtype + ")" + ", Support: " + frequent_itemsets.get(i).support);
				        }
				        
				      //---------------------------------------- A priori algorithm
				        
				        int item_number = 1;// It holds number of items in frequent itemsets in each iteration
				        Boolean is_continue = true;// It determines the ending of algorithm
				        int added_itemsets = 0;// It holds number of itemsets which are added to frequent itemsets in each iteration
				        
				        List<Itemset> candidate_itemsets = new ArrayList<Itemset>();
				        List<Itemset> final_candidates = new ArrayList<Itemset>();
				        
				        while(is_continue)
				        {
				        	item_number ++;
				        	added_itemsets = 0;
				        	
				        	
				        	
				        	// Construction of Ck candidate itemsets by joining Fk-1 with itself
				        	if(item_number == 2)
				        	{
				        		for(int i = 0; i < frequent_itemsets.size(); i++)
					        	{
					        		for(int j = i + 1; j < frequent_itemsets.size(); j++)
					        		{
					        			Itemset new_candid_itemset = new Itemset(item_number);
					        			new_candid_itemset.Copy(frequent_itemsets.get(i));
					        			new_candid_itemset.Join(frequent_itemsets.get(j));
				        				candidate_itemsets.add(new_candid_itemset);
					        		}
					        	}
				        	}
				        	else if(item_number > 2)
				        	{
				        		for(int i = 0; i < frequent_itemsets.size(); i++)
					        	{
					        		for(int j = i + 1; j < frequent_itemsets.size(); j++)
					        		{
					        			if((frequent_itemsets.get(i).items.size() == (item_number - 1)) 
					        					&& (frequent_itemsets.get(j).items.size() == (item_number - 1)) 
					        					&& (frequent_itemsets.get(i).IsHaveCommonItem(frequent_itemsets.get(j))))
					        			{
					        				Itemset new_candid_itemset = new Itemset(item_number);
					        				new_candid_itemset.Copy(frequent_itemsets.get(i));
						        			new_candid_itemset.Join(frequent_itemsets.get(j));
						        			
						        			Boolean new_itemset = true;
						        			for(int i2=0; i2<candidate_itemsets.size(); i2++)
						        			{
						        				if(new_candid_itemset.IsSameItemset(candidate_itemsets.get(i2)))
						        				{
						        					new_itemset = false;
						        					break;
						        				}
						        			}
						        			if(new_itemset)
						        				candidate_itemsets.add(new_candid_itemset);
					        			}
					        		}
					        	}
				        	}
				        	
				        	
				        	// Pruning Ck candidate itemsets
				        	
				        	if(item_number == 2)// For 2-itemsets there is no need to testing them whether their subsets are frequent
				        	{
				        		for(int i = 0; i < candidate_itemsets.size(); i++)
				        		{
				        			final_candidates.add(candidate_itemsets.get(i));
				        		}
				        	}
				        	else if(item_number > 2)
				        	{
				        		// Generating k-1 subsets for each candidate, testing them whether they are frequent
				        		for(int i = 0; i < candidate_itemsets.size(); i++)
					        	{
					        		Boolean frequent_subsets = true;
					        		for(int i1 = 0; i1 < candidate_itemsets.get(i).items.size(); i1++)
					        		{
					        			// Generating k-1 subsets
					        			Itemset subset = new Itemset(0);
					        			subset.Copy(candidate_itemsets.get(i));
					        			subset.items_number --;
					        			subset.items.remove(i1);
					        			subset.frequency = 0;
					        			// Calculating how many times each subset occurs
					        			for(int i2 = 0; i2 < sentence_list.size(); i2++)
					        			{
					        				if(sentence_list.get(i2).IsItemsetOccurs(subset))
					        					subset.frequency ++;
					        			}
					        			// Testing whether subset is frequent
					        			subset.support = (double)subset.frequency / (double)sentence_num;
					        			if(subset.support < (double)threshold/100)
					        			{
					        				frequent_subsets = false;
					        				break;
					        			}
					        		}
					        		// If all subsets of candidate are frequent then candidate are added to final_candidates
					        		if(frequent_subsets == true)
					        			final_candidates.add(candidate_itemsets.get(i));
					        	}
				        	}
				        	
				        	// Selecting frequent itemsets from final_candidates and add them to frequent itemsets list
				        	for(int i = 0; i < final_candidates.size(); i++)// for every final candidate
				        	{
				        		final_candidates.get(i).frequency = 0;
				        		for(int i2 = 0; i2 < sentence_list.size(); i2++)// for every sentence
				        		{
				        			int matched_items = 0;
				        			for(int i3 = 0; i3 < final_candidates.get(i).items.size(); i3++)// for every item in candidate
				        			{
				        				boolean is_item_occurs = false;
				        				for(int i4 = 0; i4 < sentence_list.get(i2).concept_list.size(); i4++)// for every item in sentence
				        				{
				        					if((final_candidates.get(i).items.get(i3).item_concept.equals(sentence_list.get(i2).concept_list.get(i4).concept_name)) && (final_candidates.get(i).items.get(i3).item_semtype.equals(sentence_list.get(i2).concept_list.get(i4).semantic_type)))
				        					{
				        						is_item_occurs = true;
				        					}
				        				}
				        				if(is_item_occurs == true)
				        					matched_items ++;
				        			}
				        			if(matched_items == final_candidates.get(i).items.size())
				        				final_candidates.get(i).frequency ++;
				        		}
				        		
				        		final_candidates.get(i).support = (double)final_candidates.get(i).frequency / (double)sentence_num;
				        		if(final_candidates.get(i).support >= (double)threshold/100)
				        		{
				        			frequent_itemsets.add(final_candidates.get(i));
				        			added_itemsets ++;
				        		}
				        	}
				        	
				        	
				        	candidate_itemsets.clear();
				        	final_candidates.clear();
				        	
				        	if(added_itemsets == 0)
				        		is_continue = false;
				        	
				        }// End of a priori algorithm
				        
				        System.out.println("\n************************** Final Frequent Itemsets ****************************");
				        for(int i = 0; i < frequent_itemsets.size(); i++)
				        {
				        	System.out.println("\n---------" + (i+1) + "-----------");
				        	System.out.println("Items: " + frequent_itemsets.get(i).items_number);
				        	for(int j=0; j<frequent_itemsets.get(i).items.size(); j++)
				        	{
				        		System.out.println("* Item: " + frequent_itemsets.get(i).items.get(j).item_concept + " (" + frequent_itemsets.get(i).items.get(j).item_semtype + ")");
				        	}
				        	System.out.println("Support: " + frequent_itemsets.get(i).support);
				        }
				        System.out.println("\n********* " + item_number + " ************");
				        
				        
				        // for each sentence, adding the covered itemsets to the covered_itemsets list
				        for(int i=0; i<sentence_list.size(); i++)
				        {
				        	for(int j=0; j<frequent_itemsets.size(); j++)
				        	{
				        		if(sentence_list.get(i).IsItemsetOccurs(frequent_itemsets.get(j)))
				        		{
				        			sentence_list.get(i).covered_itemsets.add(frequent_itemsets.get(j));
				        		}
				        	}
				        }
				        
				        // creating a list for all sentences coverages
				        List<SentenceCoverage> sentence_coverage = new ArrayList<SentenceCoverage>();
				        
				        for(int i=0; i<sentence_list.size(); i++)
				        {
				        	for(int j=i+1; j<sentence_list.size(); j++)
				        	{
				        		SentenceCoverage temp_coverage = new SentenceCoverage();
				        		temp_coverage.first_sentence = sentence_list.get(i).sentence_number;
				        		temp_coverage.second_sentence = sentence_list.get(j).sentence_number;
				        		temp_coverage.coverage = sentence_list.get(i).CalculateCoverage(sentence_list.get(j));
				        		
				        		sentence_coverage.add(temp_coverage);
				        	}
				        }
				        
				        // printing all coverages
				        for(int i=0; i<sentence_coverage.size(); i++)
				        {
				        	System.out.println(sentence_coverage.get(i).first_sentence + ", " + sentence_coverage.get(i).second_sentence + " : " + sentence_coverage.get(i).coverage);
				        }
				        
				        
				        // creating initial clusters
				        List<Cluster> clusters = new ArrayList<Cluster>();
				        for(int i=0; i<sentence_list.size(); i++)
				        {
				        	Cluster temp_cluster = new Cluster();
				        	temp_cluster.cluster_number = i+1;
				        	temp_cluster.cluster_members.add(sentence_list.get(i));
				        	
				        	clusters.add(temp_cluster);
				        }
				        
				        // clustering
				        boolean clustering_end = false;
				        while(clustering_end != true)
				        {
				        	boolean is_merged = false;
				        	
				        	int index1 = 0;
				        	int index2 = 0;
				        	double max_coverage = 0.0;
				        	
				        	for(int i=0; i<clusters.size(); i++)
				        	{
				        		for(int j=i+1; j<clusters.size(); j++)
				        		{
				        			if(clusters.get(i).cluster_members.size() <= (sentence_num/cluster_number) && clusters.get(j).cluster_members.size() <= (sentence_num/cluster_number))
				        			{
				        				double coverage = clusters.get(i).CalculateCoverage(clusters.get(j), sentence_coverage);
					        			if(coverage > max_coverage)
					        			{
					        				max_coverage = coverage;
					        				index1 = i;
					        				index2 = j;
					        			}
				        			}
				        		}
				        	}
				        	
				        	if(max_coverage > 0.0)
				        	{
				        		System.out.println(clusters.get(index1).cluster_number + " and " + clusters.get(index2).cluster_number + " merged");
				        		clusters.get(index1).joinClusters(clusters.get(index2));
				        		clusters.remove(index2);
				        		
				        		is_merged = true;
				        	}
				        	
				        	if(is_merged == false || clusters.size() <= cluster_number)
				        		clustering_end = true;
				        }// end of clustering
				        
				        // removing clusters that have one member
				        boolean all_removed = false;
				        while(all_removed != true)
				        {
				        	int not_removed = 0;
				        	for(int i=0; i<clusters.size(); i++)
					        {
					        	if(clusters.get(i).cluster_members.size() == 1)
					        	{
					        		clusters.remove(i);
					        	}
					        }
				        	for(int i=0; i<clusters.size(); i++)
					        {
					        	if(clusters.get(i).cluster_members.size() == 1)
					        	{
					        		not_removed ++;
					        	}
					        }
				        	if(not_removed == 0)
				        		all_removed = true;
				        }
				        
				        
				        // print all clusters
				        for(int i=0; i<clusters.size(); i++)
				        {
				        	System.out.println("Cluster: " + clusters.get(i).cluster_number);
				        	for(int j=0; j<clusters.get(i).cluster_members.size(); j++)
				        	{
				        		System.out.println(clusters.get(i).cluster_members.get(j).sentence_number);
				        	}
				        	System.out.println("-----------------------------");
				        }
				        
				        
				        // sort sentences in each cluster
				        for(int i=0; i<clusters.size(); i++)
				        {
				        	clusters.get(i).CalculateSentencesScore(sentence_coverage);
				        	clusters.get(i).SortSentences();
				        }
				        
				        // select sentences for summary
				        int summary_size = ((sentence_num * compresion_rate) / 100) + 1;
				        
				        int remaining_sentences = 0;
				        for(int i=0; i<clusters.size(); i++)// calculate the number of remaining sentences
				        {
				        	remaining_sentences += clusters.get(i).cluster_members.size();
				        }
				        
				        for(int i=0; i<clusters.size(); i++)
				        {
				        	double proportion = (double)clusters.get(i).cluster_members.size() / (double)remaining_sentences;
				        	clusters.get(i).summary_sentences = ((int)(proportion * summary_size)) + 1;
				        }
				        
				        // select sentences for summary
				        List<Sentence> summary_sentences = new ArrayList<Sentence>();
				        for(int i=0; i<clusters.size(); i++)
				        {
				        	for(int j=0; j<clusters.get(i).summary_sentences; j++)
				        	{
				        		if(j < clusters.get(i).cluster_members.size())
				        		{
				        			summary_sentences.add(clusters.get(i).cluster_members.get(j));
					        		summary_size --;
					        		if(summary_size == 0)
					        			break;
				        		}
				        	}
				        	if(summary_size == 0)
			        			break;
				        }
				        
				        //sort selected sentences based on their number
				        for(int i=0; i<summary_sentences.size(); i++)
				        {
				        	for(int j=i+1; j<summary_sentences.size(); j++)
				        	{
				        		if(summary_sentences.get(i).sentence_number > summary_sentences.get(j).sentence_number)
				        		{
				        			Sentence temp_sentence = new Sentence();
				        			temp_sentence = summary_sentences.get(i);
				        			summary_sentences.set(i, summary_sentences.get(j));
				        			summary_sentences.set(j, temp_sentence);
				        		}
				        	}
				        }
				        
				        
				        // Print selected sentences for summary
				        System.out.println("\n************************* Selected sentences *************************");
				        for(int i=0; i<summary_sentences.size(); i++)
				        {
				        	//System.out.println("Sentence: " + sentence_list.get(summary_index.get(i).sentence_number - 1).sentence_number + " Score: " + sentence_list.get(summary_index.get(i).sentence_number - 1).sentence_score);
				        	System.out.println("Sentence: " + summary_sentences.get(i).sentence_number + " Score: " + summary_sentences.get(i).sentence_score);
				        }
				        
				        // Print final summary
				        String final_summary = "<html>"
				        		+ "\n<head>"
				        		+ "\n</head>"
				        		+ "\n<body bgcolor=\"white\">"
				        		+ "\n<a name=\"1\">[1]</a> <a href=\"#1\" id=1>";
				        System.out.println("\n************************* Final summary *************************");
				        for(int i=0; i<summary_sentences.size(); i++)
				        {
				        	final_summary += summary_sentences.get(i).sentence_text;
				        	System.out.println(summary_sentences.get(i).sentence_text);
				        }
				        final_summary += "\n</a>"
				        		+ "\n</body>"
				        		+ "\n</html>";
				        
				        // Write summary in output file
				        String filename = "G:\\eclipse\\Evaluation\\Itemset-based\\Itemset-C" + cluster_number + "-T" + threshold + "-" + doc_num + ".html";
				        FileWriter filewriter = new FileWriter(filename);
				        BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
				        bufferedwriter.write(final_summary);
				        bufferedwriter.close();
					}
				}
			}
			
			
			
		}
		catch (IOException e)
		{
	         e.printStackTrace();
	    }
		catch (JDOMException e)
		{
			e.printStackTrace();
		}
	}

}
