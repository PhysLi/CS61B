public class PrintStars{
	public static void main(String[] args){
        int row = 5;
	    int index = 0;
	    while (index < row){
	        int num = 0;
	        while (num < index + 1){
	            System.out.print("*");
	            num += 1;
	        }
	        index += 1;
	        System.out.println("");
	    }
	}
}