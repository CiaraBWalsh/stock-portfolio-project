package project;
public class StockData {
	public static void main(String[] args) {

		FileIO io = new FileIO();
		String[] original = io.load("./stockdata.txt");
		int numrows = original.length;
		int numcols = original[0].split("\t").length;
		double[][] array = new double[numrows][numcols];

		/* Load in the data */
		for (int i = 1; i < numrows-1; i++) {
			for (int j = 1; j < numcols; j++) {
				array[i][j] = Double.parseDouble(original[i].split("\t")[j]);
			}
		}
		double drawdown = 0;
		String startdate = "";
		String finishdate = "";
		String company = "";
		
		/* First check each company for it's max drawdown
		 * Then compare the current company to the overall record
		 */
		for (int j = 1; j < numcols; j++) { // repeat for all companies
			double current = 100; // start current price at 100%
			double peak = 100; // start peak is 100%
			double trough = 0;
			String localstartdate = "";
			String localfinishdate = "";
			String recorddate = "";
			// go through each day - data is backwards
			for (int i = numrows - 1; i > 0; i--) {
				//change price for today
				current = current + (current * (array[i][j] / 100));
				// if it's a record high update
				if (current > peak) {
					peak = current;
					// keep track of the date
					recorddate = original[i].split("\t")[0];
					
				} 
				// otherwise, are we lower than ever before below the
				// current peak?
				else if (1 - current / peak > trough) {
					// keep track of this super low
					trough = 1 - current / peak;
					localstartdate = recorddate;
					localfinishdate = original[i].split("\t")[0];
				}
			}
			// now we've found the drawdown for this company - is it bigger
			// than the other companies?
			if (trough > drawdown) {
				drawdown = trough;
				startdate = localstartdate;
				finishdate = localfinishdate;
				company = original[0].split("\t")[j]; // remember the company
			}
		} 
		// print out the overall results
		System.out.println("The company with the highest drawdown was " + company + 
				" which suffered a drawdown of "
				+ String.format("%.1f", drawdown * 100) + 
				"% between the dates of " + startdate + " and " + finishdate
				);
	}
}
