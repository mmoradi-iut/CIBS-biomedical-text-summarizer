import java.util.ArrayList;
import java.util.List;


public class Sentence {
	
	public int sentence_number;
	public String sentence_text;
	public List<Concept> concept_list = new ArrayList<Concept>();
	public double sentence_score;
	public List<Itemset> covered_itemsets = new ArrayList<Itemset>();
	
	
	public void AddNewConcept(Concept new_concept)
	{
		Boolean already_exist = false;
		
		for(int i = 0; i < concept_list.size(); i++)
		{
			if(new_concept.concept_name.equals(concept_list.get(i).concept_name) && new_concept.semantic_type.equals(concept_list.get(i).semantic_type))
			{
				already_exist = true;
				break;
			}
		}
		
		if(already_exist == false)
			this.concept_list.add(new_concept);
	}
	
	public Boolean IsContainItem(String item_concept, String item_semtype)
	{
		for(int i = 0; i < concept_list.size(); i++)
		{
			if ((item_concept.equals(concept_list.get(i).concept_name)) && (item_semtype.equals(concept_list.get(i).semantic_type)))
				return true;
		}
		return false;
	}
	
	public Boolean IsItemsetOccurs(Itemset input_itemset)
	{
		int matched_items = 0;
		for(int i = 0; i < input_itemset.items.size(); i++)
		{
			for(int j = 0; j < this.concept_list.size(); j++)
			{
				if((input_itemset.items.get(i).item_concept.equals(this.concept_list.get(j).concept_name)) && (input_itemset.items.get(i).item_semtype.equals(this.concept_list.get(j).semantic_type)))
				{
					matched_items ++;
					break;
				}
			}
		}
		if (matched_items == input_itemset.items.size())
			return true;
		else
			return false;
	}
	
	public Boolean IsConceptOccurs(Concept input_concept)
	{
		Boolean is_occurs = false;
		for(int i=0; i<this.concept_list.size(); i++)
		{
			if((input_concept.concept_name.equals(this.concept_list.get(i).concept_name)) && (input_concept.semantic_type.equals(this.concept_list.get(i).semantic_type)))
			{
				is_occurs = true;
				break;
			}
		}
		
		return is_occurs;
	}
	
	public double CalculateCoverage (Sentence second_sentence)
	{
		double coverage = 0.0;
		
		for(int i=0; i<this.covered_itemsets.size(); i++)
		{
			for(int j=0; j<second_sentence.covered_itemsets.size(); j++)
			{
				if(this.covered_itemsets.get(i).IsSameItemset(second_sentence.covered_itemsets.get(j)) == true)
				{
					coverage += this.covered_itemsets.get(i).support;
				}
			}
		}
		
		return coverage;
	}

}
