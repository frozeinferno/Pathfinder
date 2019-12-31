import java.awt.Color;
import java.awt.Font;

/*
 * Custom fonts and colours used in Frame.java class
 */
public @interface Style {
	Font bigText = new Font("Ubuntu", Font.BOLD, 24);
	Font biggerText = new Font("Ubuntu", Font.BOLD, 68);
	Font nums = new Font("Calibri", Font.BOLD, 12);
	Font smallNums = new Font("Calibri", Font.BOLD, 11);
	
	Color greenHighlight = new Color(47, 186, 54);
	Color redHighlight = new Color(237, 43, 43);
	Color blueHighlight = new Color(43, 107, 186);
	Color btnPanel = new Color(120, 120, 120);
	Color darkText = new Color(48, 48, 48);
	Color lightText = new Color(214, 214, 214);
}
