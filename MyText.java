package holidaycardCS401;
public interface MyText extends MyShape
{                            
	public void setText(String newText);
	
	/*	Note: saveData is not a new method for this interface, but the format
		requires an additional field for the text.  Thus, for MyText objects,
		the output to saveData will be
		ClassName:X:Y:size:text
		
		public String saveData();
	*/
}