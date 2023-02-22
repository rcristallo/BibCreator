
public class FileInvalidException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	// DEFAULT CONSTRUCTOR
	public FileInvalidException(){
		super("Error : Input file cannot be parsed due to missing information (i.e month={}, title={}, etc.)");
	}
	
	// PARAMETERIZED CONSTRUCTOR
	public FileInvalidException(String eMessage){
		super(eMessage);
	}
}
