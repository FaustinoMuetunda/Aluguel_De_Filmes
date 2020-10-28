package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import servicos.Calculadora;
import servicoss.CalculadoraTest;
import servicoss.CalculoValorLocacaoTest;
import servicoss.LocacaoServiceTeste;

//@RunWith(Suite.class)

@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTeste.class
	
})

public class SuiteExecucao {
	

}
