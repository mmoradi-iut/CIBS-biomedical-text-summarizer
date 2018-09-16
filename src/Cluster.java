import java.util.ArrayList;
import java.util.List;


public class Cluster {
	
	public int cluster_number;
	public List<Sentence> cluster_members = new ArrayList<Sentence>();
	public int summary_sentences;
	
	public double CalculateCoverage(Cluster second_cluster, List<SentenceCoverage> coverage_list)
	{
		double coverage = 0.0;
		
		for(int i=0; i<this.cluster_members.size(); i++)
		{
			for(int j=0; j<second_cluster.cluster_members.size(); j++)
			{
				for(int m=0; m<coverage_list.size(); m++)
				{
					if((this.cluster_members.get(i).sentence_number == coverage_list.get(m).first_sentence && second_cluster.cluster_members.get(j).sentence_number == coverage_list.get(m).second_sentence)
							|| (this.cluster_members.get(i).sentence_number == coverage_list.get(m).second_sentence && second_cluster.cluster_members.get(j).sentence_number == coverage_list.get(m).first_sentence))
					{
						coverage += coverage_list.get(m).coverage;
					}
				}
			}
		}
		
		if(coverage > 0.0)
		{
			coverage /= (double)(this.cluster_members.size() + second_cluster.cluster_members.size());
		}
		
		return coverage;
	}
	
	public void joinClusters(Cluster second_cluster)
	{
		for(int i=0; i<second_cluster.cluster_members.size(); i++)
		{
			this.cluster_members.add(second_cluster.cluster_members.get(i));
		}
	}
	
	public void CalculateSentencesScore(List<SentenceCoverage> coverage_list)
	{
		for(int i=0; i<this.cluster_members.size(); i++)
		{
			this.cluster_members.get(i).sentence_score = 0.0;
			for(int j=0; j<this.cluster_members.size(); j++)
			{
				for(int m=0; m<coverage_list.size(); m++)
				{
					if((this.cluster_members.get(i).sentence_number == coverage_list.get(m).first_sentence && this.cluster_members.get(j).sentence_number == coverage_list.get(m).second_sentence)
							|| (this.cluster_members.get(i).sentence_number == coverage_list.get(m).second_sentence && this.cluster_members.get(j).sentence_number == coverage_list.get(m).first_sentence))
					{
						this.cluster_members.get(i).sentence_score += coverage_list.get(m).coverage;
					}
				}
			}
		}
	}
	
	public void SortSentences()
	{
		for(int i=0; i<this.cluster_members.size(); i++)
		{
			for(int j=i+1; j<this.cluster_members.size(); j++)
			{
				if(this.cluster_members.get(i).sentence_score < this.cluster_members.get(j).sentence_score)
				{
					Sentence temp_sentence = new Sentence();
					temp_sentence = this.cluster_members.get(i);
					this.cluster_members.set(i, this.cluster_members.get(j));
					this.cluster_members.set(j, temp_sentence);
				}
			}
		}
	}

}
