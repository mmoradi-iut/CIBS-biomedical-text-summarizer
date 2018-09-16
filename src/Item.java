
public class Item {
	
	public String item_concept;
	public String item_semtype;
	public int item_frequency;
	public double item_support;
	
	public Item(String concept, String semtype)
	{
		this.item_concept = concept;
		this.item_semtype = semtype;
		this.item_frequency = 1;
	}
	
	public Item(String concept, String semtype, int frequency)
	{
		this.item_concept = concept;
		this.item_semtype = semtype;
		this.item_frequency = frequency;
	}

}
