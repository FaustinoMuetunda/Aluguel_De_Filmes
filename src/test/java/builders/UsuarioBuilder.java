package builders;

import entidades.Usuario;

public class UsuarioBuilder {
	private Usuario usuario;
	
	//defini o construtor privado para que ninguem possa criar o construtor do builder externamente
	private UsuarioBuilder() {}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder=new UsuarioBuilder();
		builder.usuario=new Usuario();
		builder.usuario.setNome("Usuario 1");
		return builder;
	}
	
	public Usuario agora() {
		return usuario;
	}

}
