import java.util.ArrayList;
import java.util.List;


public class ConceptList {
	
	public List<Concept> list = new ArrayList<Concept>();
	
	public void AddNewConcept(Concept new_concept)
	{
		if(!(new_concept.semantic_type.equals("qnco")
				|| new_concept.semantic_type.equals("qlco")
				|| new_concept.semantic_type.equals("tmco")
				|| new_concept.semantic_type.equals("ftcn")
				|| new_concept.semantic_type.equals("idcn")
				|| new_concept.semantic_type.equals("inpr")
				|| new_concept.semantic_type.equals("menp")
				|| new_concept.semantic_type.equals("spco")
				|| new_concept.semantic_type.equals("lang")))
		{
			Boolean already_exist = false;
			
			for(int i = 0; i < list.size(); i++)
			{
				if(new_concept.concept_name.equals(list.get(i).concept_name))
				{
					already_exist = true;
					Concept temp_concept = list.get(i);
					temp_concept.frequency ++;
					list.set(i, temp_concept);
					break;
				}
			}
			if(already_exist == false)
			{
				new_concept.frequency = 1;
				list.add(new_concept);
			}
		}
	}

}
