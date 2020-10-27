package servicoss;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.DEFAULT)
public class OrdemTest {
	public static int cont = 0;

	@Test

	public void inicia() {
		cont = 1;
	}

	@Test

	public void verificacao() {
		Assert.assertEquals(1, cont);
	}

}
