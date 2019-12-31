import java.util.ArrayList;

public class Sort {
	private boolean lowToHigh;
	
	public Sort() {
		lowToHigh = true;
	}
	
	public void bubbleSort(ArrayList<Node> data) {
		int Switch = -1;
		Node temp;
		
		while(Switch != 0) {
			Switch = 0;
			
			if(lowToHigh) {
				for(int i = 0; i < data.size() - 1; i++) {
					if(data.get(i).getFCost() > data.get(i + 1).getFCost()) {
						temp = data.get(i);
						data.remove(i);
						data.add(i + 1, temp);
						Switch = 1;
					}
				}
			}
		}
	}
}
