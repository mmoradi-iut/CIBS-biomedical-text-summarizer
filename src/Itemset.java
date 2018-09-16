import java.util.ArrayList;
import java.util.List;


public class Itemset {
	
	public int items_number;
	public List<Item> items = new ArrayList<Item>();
	public int frequency;
	public double support;
	
	public Itemset(int items_num)
	{
		this.items_number = items_num;
		this.frequency = 0;
		this.support = 0.0;
	}
	
	public Boolean IsHaveCommonItem(Itemset second_itemset)
	{
		int common_items = 0;
		for(int i=0; i<this.items.size(); i++)
		{
			for(int j=0; j<second_itemset.items.size(); j++)
			{
				if((this.items.get(i).item_concept.equals(second_itemset.items.get(j).item_concept)) && (this.items.get(i).item_semtype.equals(second_itemset.items.get(j).item_semtype)))
					common_items ++;
			}
		}
		if(common_items == (this.items.size() - 1))
			return true;
		else
			return false;
	}
	
	public void Join(Itemset second_itemset)
	{
		this.frequency = 0;
		//Itemset new_itemset = new Itemset(this.items_number + 1);
		
		/*for(int i=0; i<this.items.size(); i++)
		{
			for(int j=0; j<second_itemset.items.size(); j++)
			{
				if(this.items.get(i).item_name.equals(second_itemset.items.get(j).item_name))
				{
					new_itemset.items.add(this.items.get(i));
				}
			}
		}*/
		int is_new;
		for(int i=0; i<second_itemset.items.size(); i++)
		{
			is_new = 0;
			for(int j=0; j<this.items.size(); j++)
			{
				if(!((this.items.get(j).item_concept.equals(second_itemset.items.get(i).item_concept)) && (this.items.get(j).item_semtype.equals(second_itemset.items.get(i).item_semtype))))
				{
					is_new ++;
				}
			}
			if(is_new == this.items.size())
			{
				this.items.add(second_itemset.items.get(i));
				break;
			}
		}
		
		/*for(int i=0; i<this.items.size(); i++)
		{
			for(int j=0; j<second_itemset.items.size(); j++)
			{
				if(!(this.items.get(i).item_name.equals(second_itemset.items.get(j).item_name)))
					this.items.add(second_itemset.items.get(j));
			}
		}*/
	}
	
	public void Copy(Itemset new_itemset)
	{
		this.frequency = new_itemset.frequency;
		
		for (int i = 0; i < new_itemset.items.size(); i++)
		{
			this.items.add(new_itemset.items.get(i));
		}
	}
	
	public Boolean IsSameItemset(Itemset second_itemset)
	{
		int same_items = 0;
		for(int i=0; i<this.items.size(); i++)
		{
			for(int j=0; j<second_itemset.items.size(); j++)
			{
				if((this.items.get(i).item_concept.equals(second_itemset.items.get(j).item_concept)) && (this.items.get(i).item_semtype.equals(second_itemset.items.get(j).item_semtype)))
					same_items ++;
			}
		}
		if(same_items == this.items.size())
			return true;
		else
			return false;
	}

}
