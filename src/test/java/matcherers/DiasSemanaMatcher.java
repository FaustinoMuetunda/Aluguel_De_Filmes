package matcherers;
import java.util.*;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import utils.DataUtils;

public class DiasSemanaMatcher extends TypeSafeMatcher <Date>{
	
	
	private Integer diaSemana;
	public DiasSemanaMatcher(Integer diaSemana) {
		
		this.diaSemana=diaSemana;
	}

	public void describeTo(Description description) {
		Calendar data=Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK,diaSemana);
		String dataExtenso=data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "PT"));
		description.appendText(dataExtenso);


	}

	@Override
	protected boolean matchesSafely(Date data) {
		// TODO Auto-generated method stub
		return DataUtils.verificarDiaSemana(data, diaSemana);
	}

}
