import java.util.ArrayList;
import java.util.List;


public class ItemList {
	
	public List<Item> list = new ArrayList<Item>();
	
	public void AddNewItem(Item new_item)
	{
		if(!(new_item.item_semtype.equals("qnco")
				|| new_item.item_semtype.equals("qlco")
				|| new_item.item_semtype.equals("tmco")
				|| new_item.item_semtype.equals("ftcn")
				|| new_item.item_semtype.equals("idcn")
				|| new_item.item_semtype.equals("inpr")
				|| new_item.item_semtype.equals("menp")
				|| new_item.item_semtype.equals("spco")
				|| new_item.item_semtype.equals("lang")))
		{
			Boolean already_exist = false;
			
			for(int i = 0; i < list.size(); i++)
			{
				if((new_item.item_concept.equals(list.get(i).item_concept)) && (new_item.item_semtype.equals(list.get(i).item_semtype)))
				{
					already_exist = true;
					Item temp_item = list.get(i);
					temp_item.item_frequency ++;
					list.set(i, temp_item);
					break;
				}
			}
			
			if(already_exist == false)
				list.add(new_item);
		}
	}

}
