import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class PerfectHashTrupletTest {

	@Test
	public void test() {
		PerfectHashTruplet aaa = new PerfectHashTruplet("aaa");
		assertEquals(931, aaa.hashCode());
		PerfectHashTruplet ööö = new PerfectHashTruplet("ööö");
		assertEquals(30+30*30+30*900, ööö.hashCode());
	}

}
